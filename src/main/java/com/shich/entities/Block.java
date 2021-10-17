package com.shich.entities;

import com.shich.entities.render.Model;
import com.shich.entities.render.Texture;
import com.shich.util.Input;

import org.joml.Vector2i;
import org.joml.Vector3f;

public class Block {
    protected int mass;
    // texture
    protected int health;
    protected int maxHealth;

    protected Vector2i location;
    protected int rotation;

    protected boolean[] attachment = new boolean[4];

    protected Model model;
    protected Texture texture;

    // protected Asset();

    public Block(Vector2i location, int mass, int maxHealth) {
        this.location = location;
        this.mass = mass;
        this.maxHealth = maxHealth;
        this.health = maxHealth;

        model = new Model(new Vector3f(0.5f, 0.5f, 0));
        texture = new Texture("block/block.png");
    }

    public void input(Input input) {

    }
}
