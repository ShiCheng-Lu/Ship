package com.shich.entities;

import com.shich.entities.render.Texture;
import com.shich.util.Input;
import com.shich.util.KEYS;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Weapon extends Thruster {

    private static Texture weapon_off = new Texture("block/weapon.png");
    private static Texture weapon_on = new Texture("block/weapon_on.png");

    private int timer;

    public Weapon(int mass, int maxHealth, KEYS activator) {
        super(mass, maxHealth, activator);
        // TODO Auto-generated constructor stub
        thrust = -5;

    }

    public Weapon() {
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
