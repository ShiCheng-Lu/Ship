package com.shich.entities;

import java.util.ArrayList;
import java.util.List;

import com.shich.entities.render.Model;
import com.shich.entities.render.Renderer;
import com.shich.entities.render.Texture;
import com.shich.util.Input;
import com.shich.util.KEYS;
import com.shich.util.Timer;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Matrix4f;

public class Ship {

    private List<Block> components = new ArrayList<Block>();
    private Block core = new Core();

    private Vector2f centerOfMass = new Vector2f(0, 0);
    private int mass;
    private float inertia;

    private float aRot;
    private float aVel;
    private float aAcc;

    // relative to CoM
    private Vector2f pos = new Vector2f();
    private Vector2f vel = new Vector2f();
    private Vector2f acc = new Vector2f();

    public Ship() {
        addBlock(core);
    }

    public Vector2f getCoM() {
        return centerOfMass;
    }

    public void addBlock(Block block) {
        // add to components;
        components.add(block);

        // add to centerOfMass;
        Vector2f blockCoM = new Vector2f(block.location);
        blockCoM.mul(block.mass);

        centerOfMass.mul(mass);
        centerOfMass.add(blockCoM);

        mass += block.mass;
        System.out.println(mass);
        centerOfMass.div(mass);
    }

    public void ReCalculateCoM() {
        mass = 0;
        Vector2f totalCoM = new Vector2f();
        for (Block b : components) {
            mass += b.mass;
            totalCoM.add(new Vector2f(b.location).mul(b.mass));
        }
        centerOfMass = new Vector2f(totalCoM).div(mass);
    }

    public void CalculateInertia() {
        inertia = 0;
        for (Block b : components) {
            inertia += b.inertia;
            float distance = 0;
            distance += (centerOfMass.x - b.location.x) * (centerOfMass.x - b.location.x);
            distance += (centerOfMass.y - b.location.y) * (centerOfMass.y - b.location.y);
            inertia += b.mass * distance;
        }
    }

    public void update(Timer timer) {
        float force = 0;
        float torque = 0;

        for (Block b : components) {
            if (b instanceof Thruster && ((Thruster) b).on) {
                force += ((Thruster) b).thrust;
                torque += (b.location.x - centerOfMass.x) * ((Thruster) b).thrust;
            }
        }
        // force
        force = force / mass * timer.delta;
        acc.y = force * (float) Math.cos(aRot);
        acc.x = force * (float) -Math.sin(aRot);

        vel.add(acc);
        pos.add(vel);

        // torque
        aAcc = torque / inertia * timer.delta;
        aVel += aAcc;

        aRot += aVel;

        //
        if (acc.x == 0 && acc.y == 0) {
            vel.mul(0.99f);
            aVel *= 0.99;
        }

    }

    public void input(Input input) {
        for (Block block : components) {
            block.input(input);
        }
        if (input.isKeyDown(KEYS.LEFT)) {
            aAcc = 1;
        }
        if (input.isKeyDown(KEYS.RIGHT)) {
            aAcc = -1;
        }
    }

    public void render(Renderer renderer) {

        // Matrix4f trans = new Matrix4f();
        Matrix4f trans = new Matrix4f();
        trans.translate(pos.x, pos.y, 0);
        trans.rotate(aRot, 0, 0, 1);
        trans.translate(-centerOfMass.x, -centerOfMass.y, 0);

        // render components
        for (Block block : components) {
            Matrix4f result = trans.translate(block.location.x, block.location.y, 0, new Matrix4f());

            renderer.render(result, block.model, block.texture);
        }

        // render center of mass marker
        Matrix4f result = trans.translate(centerOfMass.x, centerOfMass.y, 0, new Matrix4f());
        result.rotate(aRot, 0, 0, -1);
        renderer.render(result, new Model(new Vector3f(0.5f, 0.5f, 0)), new Texture("block/marker.png"));
    }

    public void checkIntegrety() {
        List<Block> connected = new ArrayList<Block>();

        int idx = 0;
        while (connected.size() != idx) {

        }
    }

    public Vector2f getPosition() {
        return new Vector2f(pos);
    }
}
