package moe.chesnot.noodles;

import org.joml.Vector3f;

import java.util.List;

public interface NoodleCurve {

    Vector3f getPosition(float t);

    Vector3f getTangent(float t);
}
