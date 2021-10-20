package com.shich.entities;

import com.shich.entities.render.Model;
import com.shich.entities.render.Texture;
import com.shich.util.Input;

import org.joml.Vector2i;
import org.joml.Vector3f;

public class Block {
    protected float mass;
    protected float inertia;
    // texture
    protected int health;
    protected int maxHealth;

    protected int rotation;

    protected boolean[] attachment = new boolean[4];

    protected Model model;
    protected Texture texture;

    public Block(int mass, int maxHealth, String textureFile) {
        this.mass = mass;
        this.maxHealth = maxHealth;
        this.health = maxHealth;

        inertia = mass / 6;

        model = new Model(new Vector3f(0.5f, 0.5f, 0));
        texture = new Texture(textureFile);
    }

    public Block(int mass, int maxHealth) {
        this(mass, maxHealth, "block/block.png");
    }

    public void input(Input input) {

    }
}
