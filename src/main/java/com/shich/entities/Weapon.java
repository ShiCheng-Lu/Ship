package com.shich.entities;

import com.shich.entities.render.Texture;
import com.shich.util.Input;
import com.shich.util.KEYS;

import org.joml.Vector2i;

public class Weapon extends Thruster {

    Texture weapon_off = new Texture("block/weapon.png");
    Texture weapon_on = new Texture("block/weapon_on.png");

    private int timer;

    public Weapon(Vector2i location, int mass, int maxHealth, KEYS activator) {
        super(location, mass, maxHealth, activator);
        // TODO Auto-generated constructor stub
        thrust = -5;

    }

    @Override
    public void input(Input input) {
        if (input.isKeyPressed(activator)) {
            texture = weapon_on;
            on = true;
        } else {
            timer++;
            if (timer > 5) {
                timer = 0;
                texture = weapon_off;
                on = false;
            }
        }
    }
}
