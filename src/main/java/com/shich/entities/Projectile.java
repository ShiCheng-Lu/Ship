package com.shich.entities;

import java.io.Serializable;

import com.shich.entities.render.Model;
import com.shich.entities.render.Renderer;
import com.shich.entities.render.Texture;
import com.shich.util.Timer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Projectile implements Serializable {

    protected float rot;
    protected Vector2f vel;
    protected Vector2f pos;

    protected Model model;
    protected Texture texture;

    protected float lifespan;

    public Projectile(Vector2f loc, float rot, float lifespan) {
        model = new Model(new Vector3f(0.0625f, 0.5f, 0));
        texture = new Texture("block/proj.png");

        vel = new Vector2f((float) -Math.sin(rot), (float) Math.cos(rot));
        vel.mul(0.5f);
        this.pos = loc;
        this.rot = rot;

        this.lifespan = lifespan;
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
