package com.shich.entities;

import com.shich.entities.render.Model;
import com.shich.entities.render.Renderer;
import com.shich.entities.render.Texture;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import org.joml.Vector2i;

public class Block {
    public int mass;
    // texture
    public int health;
    public int maxHealth;

    public Vector2i location;
    public int rotation;

    public boolean[] attachment = new boolean[4];

    public Model model;
    public Texture texture;

    // protected Asset();

    public Block(Vector2i location, int mass, int maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.location = location;

        model = new Model(new Vector3f(2, 2, 0));
        texture = new Texture("player.png");
    }

    public void input() {

    }

    public void render(Renderer renderer) {
        renderer.render(new Matrix4f(), model, texture);
    }
}
