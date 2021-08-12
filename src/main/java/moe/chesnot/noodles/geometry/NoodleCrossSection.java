package moe.chesnot.noodles.geometry;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjglb.engine.graph.IMesh;
import org.lwjglb.engine.graph.Renderable;

import java.util.List;

public interface NoodleCrossSection extends IMesh {
    Vector3fc getNormal();
    Vector3fc getCenter();
    float getRadius();
    List<Vector3fc> getPoints();
}
