package moe.chesnot.noodles;

import org.joml.Vector3f;
import org.lwjgl.system.CallbackI;

import java.util.List;

public class QuadraticBeizerNoodleCurveFactory implements NoodleCurveFactory<QuadraticBeizerNoodleCurve> {

    private final float radius;

    public QuadraticBeizerNoodleCurveFactory(float radius) {
        this.radius = radius;
    }


    @Override
    public QuadraticBeizerNoodleCurve fit(List<Vector3f> points) {
        assert points.size() == 2;
        Vector3f normal = new Vector3f();
        points.get(0).sub(points.get(1), normal);
        normal.normalize();
        // ax + by + cz = 0
        Vector3f radiusVector;
        if (normal.z != 0) {
            // set a and b = 1
            // c = (-x - y) / z
            float c = (-normal.x - normal.y) / normal.z;
            radiusVector = new Vector3f(1, 1, c).normalize(radius);
        } else if (normal.y != 0) {
            // set a and c = 1
            // b = (-x -z) / y
            float b = (-normal.x - normal.z) / normal.y;
            radiusVector = new Vector3f(1, b, 1).normalize(radius);
        } else {
            // set b and c = 1
            // a = (-y -z) / x
            float a = (-normal.y - normal.z) / normal.x;
            radiusVector = new Vector3f(a, 1, 1).normalize(radius);
        }
        normal.normalize(points.get(0).distance(points.get(1)));
        Vector3f P1 = new Vector3f();
        points.get(0).add(normal, P1);
        P1.add(radiusVector);
        return new QuadraticBeizerNoodleCurve(points.get(0), P1, points.get(1));
    }

    @Override
    public int getFitCount() {
        return 2;
    }
}
