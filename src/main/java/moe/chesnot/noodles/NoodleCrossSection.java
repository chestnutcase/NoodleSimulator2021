package moe.chesnot.noodles;

import org.joml.Vector3f;
import org.lwjglb.engine.graph.Renderable;

import java.util.List;

public interface NoodleCrossSection extends Renderable {
    Vector3f getNormal();
    Vector3f getCenter();
    float getRadius();
    List<Vector3f> getPoints();
}
