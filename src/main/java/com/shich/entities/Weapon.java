package com.shich.entities;

import java.util.ArrayList;

import com.shich.entities.render.Renderer;
import com.shich.entities.render.Texture;
import com.shich.util.Input;
import com.shich.util.KEYS;

import org.joml.Vector2i;

public class Weapon extends Thruster {

    private Texture weapon_off = new Texture("block/weapon.png");
    private Texture weapon_on = new Texture("block/weapon_on.png");

    private int timer;

    public Weapon(int mass, int maxHealth, KEYS activator) {
        super(mass, maxHealth, activator);
        // TODO Auto-generated constructor stub
        thrust = -5;

    }

    @Override
    public void input(Input input) {
        if (!on && input.isKeyDown(activator)) {
            texture = weapon_on;
            on = true;
            timer = 0;
        } else {
            timer++;
            if (timer > 7) {
                texture = weapon_off;
            }
            if (timer > 10) {
                on = false;
            }
        }
    }

    public boolean shot() {
        return on && timer == 0;
    }
}
