package com.shich.states;

import java.util.ArrayList;

import com.shich.entities.Ship;
import com.shich.entities.render.Model;
import com.shich.entities.render.Renderer;
import com.shich.entities.render.Texture;
import com.shich.util.Camera;
import com.shich.util.Input;
import com.shich.util.KEYS;
import com.shich.util.Timer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GameStateManager {
    /*
     * starts / end / pause the game
     */

    Camera camera;
    public ArrayList<GameState> states = new ArrayList<GameState>();

    Ship ship;
    boolean building = false;

    public GameStateManager(Camera camera) {
        states.add(new GameState(this));
        this.camera = camera;

        ship = Ship.load("res/ship-data");
    }

    public void update(Timer timer) {
        // if (!states.isEmpty()) {
        // states.get(0).update(timer);
        // }

        ship.update(timer);
    }

    public void input(Input input) {
        // states.get(0).input(input);

        // if (input.isKeyReleased(input.EXIT)) {
        // if (!states.isEmpty())
        // remove(0);
        // setCameraOffset(new Vector3f());
        // setCameraPosition(new Vector3f());
        // }
        if (input.isKeyDown(KEYS.ACTION)) {
            Vector2f shipPos = ship.getPosition().mul(0.7f);
            Vector3f old = camera.getPosition().mul(0.3f);

            old.sub(shipPos.x, shipPos.y, 0);

            // camera.setPosition(old);
        }

        if (input.isKeyPressed(KEYS.EXIT)) {
            building = !building;
        }

        ship.input(input);
    }

    public void render(Renderer renderer) {
        // if (!states.isEmpty())
        // states.get(0).render(renderer);
        if (building) {
            ship.render(renderer);
            renderer.render(new Matrix4f(), new Model(new Vector3f(100, 100, 0)), new Texture("mask/lightgrey.png"));
        } else {
            ship.render(renderer);
        }
    }

    // public void addState(GameState gs) {
    // states.add(0, gs);
    // }

    // public void remove(int idx) {
    // states.remove(idx);
    // }

    // public void setCameraPosition(Vector3f pos) {
    // camera.setPosition(pos);
    // }

    // public void setCameraOffset(Vector3f pos) {
    // camera.setOffset(pos);
    // }
}
