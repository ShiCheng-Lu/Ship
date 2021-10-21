package com.shich.entities;

import com.shich.entities.render.Model;
import com.shich.entities.render.Texture;
import com.shich.util.Input;

import org.joml.Vector3f;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Block {
    // 1x1 square model
    protected static final Model model = new Model(new Vector3f(0.5f, 0.5f, 0));

    protected int mass;
    protected float inertia;
    // texture
    protected int health;
    protected int maxHealth;

    protected int rotation; // 0 - 3, 

    protected boolean[] attachment = new boolean[4];

    protected Texture texture;

    public Block(int mass, int maxHealth, String textureFile) {
        this.mass = mass;
        this.maxHealth = maxHealth;
        this.health = maxHealth;

        inertia = mass / 6;

        texture = new Texture(textureFile);
    }

    public Block() {
    }

    public Block(int mass, int maxHealth) {
        this(mass, maxHealth, "block/block.png");
    }

    public void input(Input input) {

    }

    public String toString() {
        return String.format("- mass: %d\n  maxHealth: %d\n  inertia: %f", mass, maxHealth, inertia);
    }
}
