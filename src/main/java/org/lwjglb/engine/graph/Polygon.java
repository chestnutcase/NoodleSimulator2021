package org.lwjglb.engine.graph;

import org.joml.Vector3f;

import static java.lang.Math.PI;
import static org.lwjgl.opengl.GL11.*;

public class Polygon implements Renderable {
    private final float[] positions;

    public Polygon(Vector3f center, float radius, Vector3f normal, int sides) {
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
        positions = new float[sides * 3];
        for (int i = 0; i < sides; i++) {
            float angle = (float) (2 * PI / sides * i);
            radiusVector.rotateAxis(angle, radiusVector.x, radiusVector.y, radiusVector.z, currentRadiusVector);
            center.add(currentRadiusVector);
            positions[(i * 3)] = center.x;
            positions[(i * 3) + 1] = center.y;
            positions[(i * 3) + 2] = center.z;
        }
    }

    @Override
    public void render() {
        glBegin(GL_TRIANGLE_FAN);
        for (int i = 0; i < positions.length / 3; i++) {
            glVertex3f(positions[(i * 3)], positions[(i * 3) + 1], positions[(i * 3) + 2]);
        }
        glEnd();
    }
}
