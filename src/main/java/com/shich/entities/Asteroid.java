package com.shich.entities;

import java.util.ArrayList;

import com.shich.entities.render.Model;
import com.shich.entities.render.Renderer;
import com.shich.entities.render.Texture;
import com.shich.util.Timer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Asteroid {

    private int health;
    private int size;

    private Vector2f pos;

    protected Texture texture;
    protected Model model;

    public Asteroid(int size, int health, Vector2f pos) {
        model = new Model(new Vector3f(size, size, 0));
        texture = new Texture("block/block.png");
        pos = new Vector2f(5, 5);
        this.size = size;
        this.health = health;
        this.pos = pos;
    }

    protected void collide(Timer timer, Projectile projectile) {
        if (projectile.pos.distanceSquared(pos) < size) {
            health -= projectile.damage;
            projectile.lifespan = timer.getTime();
        }
    }

    public void update(Timer timer, ArrayList<Projectile> projs) {
        projs.forEach(projectile -> collide(timer, projectile));
    }

    public void render(Renderer renderer) {
        Matrix4f transform = new Matrix4f();
        transform.translate(pos.x, pos.y, 0);
        // transform.rotate(aRot, 0, 0, 1);
        renderer.render(transform, model, texture);
    }

    public boolean destroyed() {
        return health <= 0;
    }
}
