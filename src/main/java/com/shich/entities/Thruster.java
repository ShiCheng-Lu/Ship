package com.shich.entities;

import com.shich.entities.render.Texture;
import com.shich.util.Input;
import com.shich.util.KEYS;
import com.shich.util.Timer;

import org.joml.Vector2i;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Thruster extends Block {
    protected Texture on_texture;

    @XmlTransient
    protected boolean on = false;

    protected float thrust = 1;

    protected KEYS activator;

    public Thruster(int mass, int maxHealth, int thrust, KEYS activator, String on_texture_file,
            String off_texture_file) {
        super(mass, maxHealth, off_texture_file);
        this.thrust = thrust;

        on_texture = new Texture(on_texture_file);
        this.activator = activator;
    }

    public Thruster(int mass, int maxHealth, int thrust, KEYS activator) {
        this(mass, maxHealth, thrust, activator, "block/thruster_on.png", "block/thruster.png");
    }

    public Thruster() {
    }

    public void update(Timer timer, Ship ship, Vector2i blockPos) {
        if (on) {
            ship.force += thrust;
            ship.torque += (blockPos.x - ship.centerOfMass.x) * thrust;
        }
    }

    public void input(Input input) {
        on = input.isKeyDown(activator);
    }

    public Texture getTexture() {
        return on ? on_texture : texture;
    }
}
