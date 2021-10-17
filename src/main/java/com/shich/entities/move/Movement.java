package com.shich.entities.move;

import com.shich.util.Input;
import com.shich.util.KEYS;
import com.shich.util.Timer;

import org.joml.Vector3f;

public class Movement {
    
    public Vector3f pos;
    public Vector3f vel;
    public Vector3f acc;
    public int facing;

    Vector3f max_vel;

    public Movement(Vector3f pos, Vector3f vel, Vector3f acc) {
        this.pos = pos;
        this.vel = vel;
        this.acc = acc;
        facing = 1;
    }

    public void input(Input input) {
        if (input.isKeyDown(KEYS.LEFT)) {
            vel.x = -10;
        }
        if (input.isKeyDown(KEYS.RIGHT)) {
            vel.x =  10;
        }
        if (input.isKeyDown(KEYS.DOWN)) {
            vel.y = -10;
        }
        if (input.isKeyDown(KEYS.UP)) {
            vel.y =  10;
        }
    }

    public void update(Timer timer) {
        // pos.add(vel.mul(timer.delta, new Vector3f()));
        vel.add(acc.mul(timer.delta, new Vector3f()));

        if (vel.x > 0) {
            facing = 1;
        } else if (vel.x < 0) {
            facing = -1;
        }

        vel.x = 0;
    }
}
