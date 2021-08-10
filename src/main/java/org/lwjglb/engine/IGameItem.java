package org.lwjglb.engine;

import org.joml.Vector3f;
import org.lwjglb.engine.graph.Renderable;

public interface IGameItem {
    Vector3f getPosition();

    void setPosition(float x, float y, float z);

    float getScale();

    void setScale(float scale);

    Vector3f getRotation();

    void setRotation(float x, float y, float z);

    Iterable<Renderable> getRenderables();
}
