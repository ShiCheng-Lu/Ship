package com.shich.entities.bounds;

import org.joml.Vector3f;

// help with collision resolution
public class Collision implements Comparable<Collision> {
    public AABB target;
    public boolean intersects;
    public Vector3f normal;
    public float time;
    public Vector3f distance;

    public Collision(boolean intersects, AABB target, Vector3f normal, float time, Vector3f distance) {
        this.intersects = intersects;
        this.normal = normal;
        this.time = time;
        this.target = target;
        this.distance = distance;
    }

    public Collision(boolean intersects, AABB target, Vector3f distance) {
        this(intersects, target, null, -1, distance);
    }

    @Override
    public int compareTo(Collision o) {
        if (time == o.time) {
            if (distance.lengthSquared() < o.distance.lengthSquared()) {
                return -1;
            } else {
                return 1;
            }
        }
        if (time < o.time) {
            return -1;
        } else if (time > o.time) {
            return 1;
        } else {
            return 0;   
        }
    }
}