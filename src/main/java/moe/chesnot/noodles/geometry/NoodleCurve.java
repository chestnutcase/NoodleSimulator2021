package moe.chesnot.noodles.geometry;

import org.joml.Vector3fc;

public interface NoodleCurve {

    Vector3fc getPosition(float t);

    Vector3fc getTangent(float t);
}
