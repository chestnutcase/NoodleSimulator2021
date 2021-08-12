package moe.chesnot.noodles.geometry;

import org.joml.Vector3f;
import org.joml.Vector3fc;

public class BeizerNoodleCurve implements NoodleCurve {
    public BeizerNoodleCurve(int degree, Vector3fc[] points) {
        this.degree = degree;
        this.points = points;
    }

    private final int degree;
    private final Vector3fc[] points;


    @Override
    public Vector3fc getPosition(float t) {
        assert this.points.length == degree;
        Vector3f b = new Vector3f();
        for (int d = 0; d < degree; d++) {
            Vector3fc p = points[d];
            int degree_choose_d = fact(degree-1) / (fact(d)*fact(degree-d-1));
            double coefficient = degree_choose_d * Math.pow(t, d) * Math.pow(1-t,degree - d-1);
            Vector3f bd = new Vector3f();
            p.mul((float) coefficient, bd);
            b.add(bd);
        }
        return b;
    }

    @Override
    public Vector3fc getTangent(float t) {
        Vector3f b = new Vector3f();
        for (int d = 0; d < degree; d++) {
            Vector3fc p = points[d];
            int degree_choose_d = fact(degree-1) / (fact(d)*fact(degree-d-1));
            double coefficient = degree_choose_d * Math.pow(1-t, degree-d-2) * Math.pow(t,d-1) * (d*(t-1) + t*(d-degree+1));
            Vector3f bd = new Vector3f();
            p.mul((float) coefficient, bd);
            b.add(bd);
        }
        return b.normalize();
    }

    private static int fact(int number) {
        int f = 1;
        int j = 1;
        while (j <= number) {
            f = f * j;
            j++;
        }
        return f;
    }
}
