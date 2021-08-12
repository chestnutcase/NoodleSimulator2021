package moe.chesnot.noodles.physics;

import org.joml.Vector3fc;

public interface ForceComponent {
    Vector3fc getForce(PointMass mass);
}
