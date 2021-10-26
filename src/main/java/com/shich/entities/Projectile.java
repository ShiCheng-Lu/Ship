package com.shich.entities;

import com.shich.entities.render.Model;
import com.shich.entities.render.Renderer;
import com.shich.entities.render.Texture;
import com.shich.util.Timer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Projectile {

    protected float rot;
    protected Vector2f vel;
    protected Vector2f pos;

    protected Model model;
    protected Texture texture;

    protected float lifespan;
    protected int damage;

    public Projectile(Vector2f loc, Vector2f vel, float rot, float lifespan) {
        model = new Model(new Vector3f(0.0625f, 0.5f, 0));
        texture = new Texture("block/proj.png");

        this.vel = vel;
        this.pos = loc;
        this.rot = rot;

        this.lifespan = lifespan;
        this.damage = 1;
    }

    public void update(Timer timer) {
        pos.add(vel);
    }

    public void render(Renderer renderer) {
        Matrix4f transform = new Matrix4f();

        transform.translate(pos.x, pos.y, 0);
        transform.rotate(rot, 0, 0, 1);

        renderer.render(transform, model, texture);
    }
}
