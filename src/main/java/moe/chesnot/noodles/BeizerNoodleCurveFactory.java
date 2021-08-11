package moe.chesnot.noodles;

import org.joml.Vector3f;
import org.joml.Intersectiond;

import java.util.List;

public class BeizerNoodleCurveFactory implements NoodleCurveFactory<BeizerNoodleCurve>{
    private final int degree;

    public BeizerNoodleCurveFactory(int degree) {
        this.degree = degree;
    }

    @Override
    public BeizerNoodleCurve fit(List<Vector3f> points) {
        Vector3f[] _points = new Vector3f[degree];
        for(int i = 0; i < degree; i++){
            _points[degree - i - 1] = points.get(i);
        }
        return new BeizerNoodleCurve(degree,_points);
    }

    @Override
    public int getFitCount() {
        return 3;
    }

    @Override
    public int getFitIncrement() {
        return 1;
    }
}
