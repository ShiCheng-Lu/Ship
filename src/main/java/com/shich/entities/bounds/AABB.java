// AABB, implemented swept AABB collision
package com.shich.entities.bounds;

import java.util.ArrayList;

import org.joml.Vector3f;

public class AABB { // axis aligned bounding box
    public Vector3f center;
    public Vector3f half_extent;

    public AABB(Vector3f center, Vector3f half_extent) {
        this.center = center;
        this.half_extent = half_extent;
    }

    public AABB(float x, float y, float z, float width, float height, float depth) {
        this(new Vector3f(x, y, z), new Vector3f(width / 2, height / 2, depth / 2));
    }

    public AABB(float x, float y, float width, float height) {
        this(x, y, 0, width, height, 0);
    }

    public boolean getCollision(AABB other) {
        if (other == null) return false;

        Vector3f distance = new Vector3f();

        distance.add(other.center).sub(center);
        distance.absolute();
        distance.sub(half_extent).sub(other.half_extent);

        return (distance.x < 0 && distance.y < 0);
    }

    public Collision getCollision(AABB other, Vector3f other_dir) {

        Vector3f original_half_extent = new Vector3f(half_extent);
        half_extent.add(other.half_extent);

        // do rayCollision;
        Collision result = rayCollision(other.center, other_dir);

        half_extent = original_half_extent;

        if (result.time >= 0 && result.time < 1) {
            return result;
        } else {
            Vector3f distance = new Vector3f(other.center).sub(center);
            distance.absolute().sub(half_extent).sub(other.half_extent);
            return new Collision(false, this, distance);
        }
    }

    public Collision rayCollision(Vector3f origin, Vector3f direction) {
        Vector3f distance = new Vector3f(origin).sub(center);
        distance.absolute();

        Vector3f invdir = new Vector3f(1, 1, 1).div(direction);

        Vector3f t_near = new Vector3f();
        center.sub(origin, t_near).sub(half_extent);
        t_near.mul(invdir);

        Vector3f t_far = new Vector3f();
        center.sub(origin, t_far).add(half_extent);
        t_far.mul(invdir);

        if (Float.isNaN(t_near.x) || Float.isNaN(t_near.y) || Float.isNaN(t_far.x) || Float.isNaN(t_far.y)) {
            return new Collision(false, this, distance);
        }

        if (t_near.x > t_far.x) {
            float temp = t_near.x;
            t_near.x = t_far.x;
            t_far.x = temp;
        }

        if (t_near.y > t_far.y) {
            float temp = t_near.y;
            t_near.y = t_far.y;
            t_far.y = temp;
        }

        if (t_near.x >= t_far.y || t_near.y >= t_far.x) {
            return new Collision(false, this, distance);
        }

        float t_hit_near = Math.max(t_near.x, t_near.y);
        float t_hit_far = Math.min(t_far.x, t_far.y);

        if (t_hit_far < 0) {
            return new Collision(false, this, distance);
        }

        Vector3f cancel_dir = new Vector3f(1, 1, 1);
        if (t_near.x > t_near.y) {
            cancel_dir = new Vector3f(1, 0, 0);
        } else if (t_near.x < t_near.y) {
            cancel_dir = new Vector3f(0, 1, 0);
        }
        return new Collision(true, this, cancel_dir, t_hit_near, distance);
    }

    public boolean resolveCollision(Vector3f vel, ArrayList<AABB> targets) {
        boolean ret_val = false;

        ArrayList<Collision> collisions = new ArrayList<>();

        for (AABB target : targets) {
            collisions.add(target.getCollision(this, vel));
        }

        collisions.sort(null);
        for (Collision c : collisions) {
            // collide and update velocity
            Collision new_c = c.target.getCollision(this, vel);
            if (new_c.intersects) {
                vel.sub(new_c.normal.mul(vel).mul(1 - new_c.time));

                // upward collision
                if (new_c.normal.y < 0) {
                    ret_val = true;
                }
            }
        }

        return ret_val;
    }

    public boolean contains(Vector3f pos) {
        Vector3f distance = new Vector3f();
        pos.sub(center, distance);
        distance.absolute();

        return (distance.x <= half_extent.x && distance.y <= half_extent.y && distance.z <= half_extent.z);
    }
}
