package com.shich.entities;

import com.shich.entities.render.Model;
import com.shich.entities.render.Renderer;
import com.shich.entities.render.Texture;
import com.shich.util.Input;
import com.shich.util.Timer;

import org.joml.Vector2i;
import org.joml.Vector3f;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Block {
    // 1x1 square model
    public static final Model model = new Model(new Vector3f(0.5f, 0.5f, 0));
    protected Texture texture;

    protected String name;

    protected int mass;
    protected float inertia;
    
    protected int health;
    protected int maxHealth;

    protected int rotation; // 0 - 3,

    protected byte attachment = 0b1111;

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

    public void update(Timer timer, Ship ship, Vector2i blockPos) {
    }

    public Texture getTexture() {
        return texture;
    }

    public Texture getDefaultTexture() {
        return texture;
    }

    public void options() {

    }
}
