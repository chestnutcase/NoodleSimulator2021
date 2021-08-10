package org.lwjglb.engine;

import org.joml.Vector3f;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.Renderable;

import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;

public class GameItem implements IGameItem {

    private final Renderable mesh;
    
    private final Vector3f position;
    
    private float scale;

    private final Vector3f rotation;

    public GameItem(Renderable mesh) {
        this.mesh = mesh;
        position = new Vector3f();
        scale = 1;
        rotation = new Vector3f();
    }

    @Override
    public Vector3f getPosition() {
        return position;
    }

    @Override
    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    @Override
    public float getScale() {
        return scale;
    }

    @Override
    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public Vector3f getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    @Override
    public Iterable<Renderable> getRenderables() {
        LinkedList<Renderable> l = new LinkedList<>();
        l.add(mesh);
        return l;
    }
}