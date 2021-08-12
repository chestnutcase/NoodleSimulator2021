package moe.chesnot.noodles.geometry;

import org.joml.Vector3fc;

import java.util.List;

public class StraightNoodleCurveFactory implements NoodleCurveFactory<StraightNoodleCurve> {
    @Override
    public StraightNoodleCurve fit(List<Vector3fc> points) {
        return new StraightNoodleCurve(points.get(0), points.get(1));
    }

    @Override
    public int getFitCount() {
        return 2;
    }
}
