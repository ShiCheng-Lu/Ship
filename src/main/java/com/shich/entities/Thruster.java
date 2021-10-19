package com.shich.entities;

import com.shich.entities.render.Texture;
import com.shich.util.Input;
import com.shich.util.KEYS;

import org.joml.Vector2i;

public class Thruster extends Block {
    Texture thruster_off = new Texture("block/thruster.png");
    Texture thruster_on = new Texture("block/thruster_on.png");
    protected boolean on = false;
    protected float thrust = 1;

    protected KEYS activator;

    public Thruster(Vector2i location, int mass, int maxHealth, KEYS activator) {
        super(location, mass, maxHealth);
        // TODO Auto-generated constructor stub
        thrust = 10;
        texture = thruster_off;
        this.activator = activator;
    }

    // public void update(Timer timer, Ship ship) {
    //     if (on) {
            
    //     }
    // }

    public void input(Input input) {
        if (input.isKeyDown(activator)) {
            texture = thruster_on;
            on = true;
        } else {
            texture = thruster_off;
            on = false;
        }
    }
}
