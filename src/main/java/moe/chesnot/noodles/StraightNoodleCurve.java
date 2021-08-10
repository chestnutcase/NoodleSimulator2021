package moe.chesnot.noodles;

import org.joml.Vector3f;

import java.util.List;

/**
 * What even is a straight curve?? im high
 */
public class StraightNoodleCurve implements NoodleCurve {
    private final Vector3f diff = new Vector3f();
    private final Vector3f start = new Vector3f();

    public StraightNoodleCurve(Vector3f a, Vector3f b){
        a.sub(b, diff);
        a.div(1, start);
    }

    @Override
    public Vector3f getPosition(float t) {
        Vector3f result = new Vector3f();
        Vector3f _d = new Vector3f();
        diff.mul(t, _d);
        start.add(_d, result);
        return result;
    }

    @Override
    public Vector3f getTangent(float t) {
        return new Vector3f(diff).normalize();
    }
}
