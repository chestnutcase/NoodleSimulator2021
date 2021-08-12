package moe.chesnot.noodles.physics;

import org.joml.Vector3f;
import org.joml.Vector3fc;

public class DampedSpringForceComponent implements ForceComponent{

    private final float k;
    private final PointMass parent;

    public DampedSpringForceComponent(float k, PointMass parent) {
        this.k = k;
        this.parent = parent;
    }

    @Override
    public Vector3fc getForce(PointMass mass) {
        float c = (float) (2 * Math.sqrt(mass.getMass() * k));
        float x = parent.getPosition().distance(mass.getPosition()) - 0.5f;
        float dx_dt = (float) Math.sqrt(Math.pow(parent.getVelocity().x() - mass.getVelocity().x(), 2)
                + Math.pow(parent.getVelocity().y() - mass.getVelocity().y(), 2)
                + Math.pow(parent.getVelocity().z() - mass.getVelocity().y(), 2));
        float force_magnitude = (-k * x)  - (c * dx_dt);
        Vector3f force = new Vector3f();
        mass.getPosition().sub(parent.getPosition(), force);
        force.normalize(force_magnitude);
        return force;
    }
}
