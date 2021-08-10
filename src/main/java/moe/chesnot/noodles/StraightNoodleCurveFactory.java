package moe.chesnot.noodles;

import org.joml.Vector3f;

import java.util.List;

public class StraightNoodleCurveFactory implements NoodleCurveFactory<StraightNoodleCurve> {
    @Override
    public StraightNoodleCurve fit(List<Vector3f> points) {
        return new StraightNoodleCurve(points.get(0), points.get(1));
    }

    @Override
    public int getFitCount() {
        return 2;
    }
}
