package com.shich.entities;

import com.shich.entities.render.Texture;
import com.shich.util.Input;
import com.shich.util.KEYS;

import org.joml.Vector2i;

public class Thruster extends Block {
    Texture thruster_off = new Texture("block/thruster.png");
    Texture thruster_on = new Texture("block/thruster_on.png");
    protected boolean on = false;

    public Thruster(Vector2i location, int mass, int maxHealth) {
        super(location, mass, maxHealth);
        // TODO Auto-generated constructor stub
        texture = thruster_off;
    }

    public int thrust; // force produced

    public void input(Input input) {
        if (input.isKeyDown(KEYS.UP)) {
            texture = thruster_on;
            on = true;
        } else {
            texture = thruster_off;
            on = false;
        }
    }
}
