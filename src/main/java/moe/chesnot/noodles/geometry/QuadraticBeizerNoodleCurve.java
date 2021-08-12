package moe.chesnot.noodles.geometry;

import org.joml.Vector3f;
import org.joml.Vector3fc;

public class QuadraticBeizerNoodleCurve implements NoodleCurve {

    private final Vector3fc P0;
    private final Vector3fc P1;
    private final Vector3fc P2;

    public QuadraticBeizerNoodleCurve(Vector3fc p0, Vector3fc p1, Vector3fc p2) {
        P0 = p0;
        P1 = p1;
        P2 = p2;
    }

    @Override
    public Vector3fc getPosition(float t) {
        Vector3f a = new Vector3f();
        P0.mul((float) Math.pow(1 - t, 2), a);
        Vector3f b = new Vector3f();
        P1.mul(2 * (1 - t), b);
        Vector3f c = new Vector3f();
        P2.mul(t * t, c);
        return a.add(b).add(c);
    }

    @Override
    public Vector3fc getTangent(float t) {
        Vector3f a = new Vector3f();
        P1.sub(P0, a);
        a.mul(2 * (1 - t));
        Vector3f b = new Vector3f();
        P2.sub(P1, b);
        b.mul(2 * t);
        return a.add(b);
    }
}
