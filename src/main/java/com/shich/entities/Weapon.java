package com.shich.entities;

import com.shich.util.Input;
import com.shich.util.Timer;

import org.joml.Vector2f;
import org.joml.Vector2i;

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

    private float projVel;

    public Weapon(int mass, int maxHealth, int activator) {
        super(mass, maxHealth, -10, activator, "block/weapon_on.png", "block/weapon.png");

        charge_time = 5;
        reload_time = 10;

        projVel = 1;
    }

    public Weapon() {
    }

    @Override
    public void input(Input input) {
        super.input(input);

        if (on || timer != 0) {
            timer++;
            if (timer > charge_time) {
                on = false;
            }
            if (timer > charge_time + reload_time) {
                timer = 0;
            }
        }
    }

    public void update(Timer timer, Ship ship, Vector2i blockPos) {
        super.update(timer, ship, blockPos);
        if (this.timer == this.charge_time) {

            ship.torque += (blockPos.x - ship.centerOfMass.x) * thrust;

            Vector2f projPos = new Vector2f(blockPos);
            Vector2f projVel;
            switch (rotation) {
            case 0:
                projVel = new Vector2f(0, this.projVel);
                break;
            case 1:
                projVel = new Vector2f(-this.projVel, 0);
                break;
            case 2:
                projVel = new Vector2f(0, -this.projVel);
                break;
            case 3:
                projVel = new Vector2f(this.projVel, 0);
                break;
            default:
                System.out.println("invalid rotation");
                projVel = new Vector2f(0, 0);
            }
            float projRot = rotation * (float) Math.PI * 0.5f;

            ship.addProjectile(new Projectile(projPos, projVel, projRot, timer.getTime() + 1));
        }
    }
}
