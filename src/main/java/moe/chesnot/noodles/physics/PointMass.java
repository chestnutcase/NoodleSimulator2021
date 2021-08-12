package moe.chesnot.noodles.physics;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.HashSet;

public class PointMass {
    private final HashSet<ForceComponent> forces;


    private final float mass;
    private final Vector3f position;
    private final Vector3f velocity;

    public PointMass(Vector3fc position, float mass) {
        forces = new HashSet<>();
        this.mass = mass;
        this.position = new Vector3f(position);
        this.velocity = new Vector3f();
    }

    public void addForceComponent(ForceComponent force){
        forces.add(force);
    }

    public Vector3fc getAcceleration(){
        Vector3f netForce = new Vector3f();
        for(ForceComponent force : forces){
            netForce.add(force.getForce(this));
        }
        return netForce.div(mass);
    }

    public Vector3fc getPosition(){
        return this.position;
    }

    public Vector3fc getVelocity(){
        return this.velocity;
    }

    public void tick(float t){
        Vector3f dv = new Vector3f();
        getAcceleration().mul(t, dv);
        velocity.add(dv);
        Vector3f dx = new Vector3f();
        velocity.mul(t, dx);
        position.add(velocity);
    }

    public float getMass() {
        return mass;
    }
}
