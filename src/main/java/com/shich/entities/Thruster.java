package com.shich.entities;

import com.shich.entities.render.Texture;
import com.shich.util.Input;
import com.shich.util.KEYS;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Thruster extends Block {
    protected Texture off_texture;
    protected Texture on_texture;

    @XmlTransient
    protected boolean on = false;

    protected float thrust = 1;

    protected KEYS activator;

    public Thruster(int mass, int maxHealth, int thrust, KEYS activator, String on_texture_file,
            String off_texture_file) {
        super(mass, maxHealth);
        this.thrust = thrust;

        on_texture = new Texture(on_texture_file);
        off_texture = new Texture(off_texture_file);

        texture = off_texture;
        this.activator = activator;
    }

    public Thruster(int mass, int maxHealth, int thrust, KEYS activator) {
        this(mass, maxHealth, thrust, activator, "block/thruster_on.png", "block/thruster.png");
    }

    public Thruster() {
    }

    // public void update(Timer timer, Ship ship) {
    // if (on) {

    // }
    // }

    public void input(Input input) {
        if (input.isKeyDown(activator)) {
            texture = on_texture;
            on = true;
        } else {
            texture = off_texture;
            on = false;
        }
    }
}
