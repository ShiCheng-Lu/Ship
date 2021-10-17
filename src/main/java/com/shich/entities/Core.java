package com.shich.entities;

import com.shich.entities.render.Texture;

import org.joml.Vector2i;

public class Core extends Block {

    public Core() {
        super(new Vector2i(0, 0), 100, 100);
        texture = new Texture("block/core.png");
    }
}
