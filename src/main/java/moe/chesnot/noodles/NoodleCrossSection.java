package moe.chesnot.noodles;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjglb.engine.graph.Renderable;

import java.util.List;

public interface NoodleCrossSection extends Renderable {
    Vector3fc getNormal();
    Vector3fc getCenter();
    float getRadius();
    List<Vector3fc> getPoints();
}
