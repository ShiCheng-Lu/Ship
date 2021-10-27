package com.shich.entities;

import com.shich.entities.render.Model;
import com.shich.entities.render.Renderer;
import com.shich.entities.render.Texture;
import com.shich.util.Input;
import com.shich.util.Timer;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Block implements Cloneable {
    // 1x1 square model
    public static final Model model = new Model(new Vector3f(0.5f, 0.5f, 0));
    protected Texture texture;

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

        rotation = 0;
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

    public void render(Renderer renderer, Matrix4f transform) {
        transform.rotate(rotation * (float) Math.PI * 0.5f, 0, 0, 1);
        renderer.render(transform, Block.model, getTexture());
    }

    public Texture getTexture() {
        return texture;
    }

    public Texture getDefaultTexture() {
        return texture;
    }

    public void rotate(int amount) {
        rotation += amount;
        rotation = rotation & ~-4; // modulus by 4, positive remainder % 4
    }

    @Override
    public Block clone() {
        try {
            return (Block) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
