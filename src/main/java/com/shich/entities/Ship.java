package com.shich.entities;

import java.util.ArrayList;
import java.util.List;

import com.shich.entities.render.Renderer;

import org.joml.Vector2f;
import org.joml.Vector2i;

public class Ship {

    private List<Block> components = new ArrayList<Block>();
    private Block core = new Core();

    private Vector2f centerOfMass = new Vector2f(0, 0);
    private int totalMass;
    private float inertia;

    public Ship() {
        components.add(core);
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

        centerOfMass.mul(totalMass);
        centerOfMass.add(blockCoM);

        totalMass += block.mass;
        centerOfMass.div(totalMass);
    }

    public void ReCalculateCoM() {
        totalMass = 0;
        Vector2i totalCoM = new Vector2i(0, 0);
        for (Block b : components) {
            totalMass += b.mass;
            totalCoM.add(b.location.mul(b.mass, new Vector2i()));
        }
        centerOfMass = new Vector2f(totalCoM).div(totalMass);
    }

    public void update() {

    }

    public void render(Renderer renderer) {
        for (Block block : components) {
            block.render(renderer);
        }

    }

    public void checkIntegrety() {
        List<Block> connected = new ArrayList<Block>();

        int idx = 0;
        while (connected.size() != idx) {

        }
    }
}
