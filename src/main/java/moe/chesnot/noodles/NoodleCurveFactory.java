package moe.chesnot.noodles;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.List;

public interface NoodleCurveFactory<T extends NoodleCurve> {
    T fit(List<Vector3fc> points);
    int getFitCount();

    default int getFitIncrement() {
        return 1;
    }
}
