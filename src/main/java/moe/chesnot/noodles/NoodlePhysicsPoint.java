package moe.chesnot.noodles;

import org.joml.Vector3f;

public class NoodlePhysicsPoint {
    public Vector3f position;
    public Vector3f velocity;
    public Vector3f acceleration;

    public NoodlePhysicsPoint(Vector3f position){
        this.position = new Vector3f(position);
        this.velocity = new Vector3f();
        this.acceleration = new Vector3f();
    }

    public void step(float dt){
        velocity.add(acceleration.mul(dt));
        position.add(velocity.mul(dt));
    }
}
