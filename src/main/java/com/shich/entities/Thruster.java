package com.shich.entities;

import org.joml.Vector2i;

public class Thruster extends Block {
    public Thruster(Vector2i location, int mass, int maxHealth) {
        super(location, mass, maxHealth);
        // TODO Auto-generated constructor stub
    }

    public int thrust; // force produced
}
