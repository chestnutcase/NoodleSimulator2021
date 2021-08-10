package moe.chesnot.noodles;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;
import org.lwjglb.engine.graph.Renderable;
import org.lwjglb.engine.graph.Texture;

import java.lang.reflect.Array;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.PI;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class PolygonCrossSection implements NoodleCrossSection {
    private float[] positions;
    private int[] indices;
    private float[] textCoords;
    private int vaoId;

    private List<Integer> vboIdList;

    private int vertexCount;

    private Texture texture;

    private final Vector3f normal;
    private final Vector3f center;
    private final float radius;
    private final int sides;
    private final ArrayList<Vector3f> points;

    public PolygonCrossSection(Vector3f center, float radius, Vector3f normal, int sides, Texture texture) {
        this.normal = normal;
        this.center = center;
        this.radius = radius;
        this.sides = sides;
        this.points = new ArrayList<Vector3f>();
        // ax + by + cz = 0
        Vector3f radiusVector;
        if (normal.z != 0) {
            // set a and b = 1
            // c = (-x - y) / z
            float c = (-normal.x - normal.y) / normal.z;
            radiusVector = new Vector3f(1, 1, c).normalize(radius);
        } else if (normal.y != 0) {
            // set a and c = 1
            // b = (-x -z) / y
            float b = (-normal.x - normal.z) / normal.y;
            radiusVector = new Vector3f(1, b, 1).normalize(radius);
        } else {
            // set b and c = 1
            // a = (-y -z) / x
            float a = (-normal.y - normal.z) / normal.x;
            radiusVector = new Vector3f(a, 1, 1).normalize(radius);
        }
        Vector3f currentRadiusVector = new Vector3f();
        float[] positions = new float[sides * 3];
        for (int i = 0; i < sides; i++) {
            float angle = (float) (2 * PI / sides * i);
            radiusVector.rotateAxis(angle, normal.x, normal.y, normal.z, currentRadiusVector);
            Vector3f newPoint = new Vector3f();
            center.add(currentRadiusVector, newPoint);
            positions[(i * 3)] = newPoint.x;
            positions[(i * 3) + 1] = newPoint.y;
            positions[(i * 3) + 2] = newPoint.z;
            this.points.add(newPoint);
        }
        this.positions = positions;
        indices = new int[sides];
        for (int i = 0; i < sides; i++) {
            indices[i] = i;
        }
        textCoords = new float[sides * 2];
        for (int i = 0; i < sides; i++) {
            float angle = (float) (2 * PI / sides * i);
            textCoords[(i * 2)] = (float) ((0.5) + 0.5*Math.sin(angle));
            textCoords[(i * 2) + 1] = (float) (0.5 + 0.5*Math.cos(angle));
        }
        this.texture = texture;
        allocate();
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    @Override
    public void allocate() {
        FloatBuffer posBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            this.texture = texture;
            vertexCount = indices.length;
            vboIdList = new ArrayList<>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Texture coordinates VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textCoordsBuffer.put(textCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            // Index VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (textCoordsBuffer != null) {
                MemoryUtil.memFree(textCoordsBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    @Override
    public void render() {
        // Activate firs texture bank
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, texture.getId());

        // Draw the mesh
        glBindVertexArray(getVaoId());

        glDrawElements(GL_TRIANGLE_FAN, getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glBindVertexArray(0);
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the texture
        texture.cleanup();

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    @Override
    public Vector3f getNormal() {
        return this.normal;
    }

    @Override
    public Vector3f getCenter() {
        return center;
    }

    @Override
    public float getRadius() {
        return radius;
    }

    @Override
    public List<Vector3f> getPoints() {
        ArrayList<Vector3f> points = new ArrayList<Vector3f>();
        for (Vector3f p : this.points) {
            points.add(new Vector3f(p));
        }
        return points;
    }
}
