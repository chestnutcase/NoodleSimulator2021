package moe.chesnot.noodles;

import org.joml.Vector3f;

import java.util.List;

public interface NoodleCurveFactory<T extends NoodleCurve> {
    T fit(List<Vector3f> points);
    int getFitCount();
}
