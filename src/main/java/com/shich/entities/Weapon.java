package com.shich.entities;

import com.shich.util.Input;
import com.shich.util.KEYS;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Weapon extends Thruster {

    @XmlTransient
    private int timer;

    private int charge_time;
    private int reload_time;

    public Weapon(int mass, int maxHealth, KEYS activator) {
        super(mass, maxHealth, -5, activator, "block/weapon_on.png", "block/weapon.png");

        charge_time = 5;
        reload_time = 10;
    }

    public Weapon() {
    }

    @Override
    public void input(Input input) {
        super.input(input);

        if (on || timer != 0) {
            if (timer > charge_time) {
                on = false;
            }
            if (timer > charge_time + reload_time) {
                timer = 0;
            }
            timer++;
        }
    }

    public boolean shot() {
        return timer == charge_time;
    }
}
