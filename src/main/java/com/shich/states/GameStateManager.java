package com.shich.states;

import java.util.ArrayList;

import com.shich.entities.Asteroid;
import com.shich.entities.Block;
import com.shich.entities.Ship;
import com.shich.entities.Thruster;
import com.shich.entities.Weapon;
import com.shich.entities.render.Model;
import com.shich.entities.render.Renderer;
import com.shich.entities.render.Texture;
import com.shich.util.Camera;
import com.shich.util.Input;
import com.shich.util.KEYS;
import com.shich.util.Timer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class GameStateManager {
    /*
     * starts / end / pause the game
     */

    Camera camera;
    public ArrayList<GameState> states = new ArrayList<GameState>();

    Ship ship;
    boolean building = false;

    Block selected;
    Vector2f selected_pos;

    ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();

    public GameStateManager(Camera camera) {
        states.add(new GameState(this));
        this.camera = camera;

        ship = Ship.load("res/ship-data");

        asteroids.add(new Asteroid(3, 10, new Vector2f()));
        asteroids.add(new Asteroid(3, 10, new Vector2f()));
        asteroids.add(new Asteroid(3, 10, new Vector2f()));
    }

    public void update(Timer timer) {
        // if (!states.isEmpty()) {
        // states.get(0).update(timer);
        // }
        if (!building) {
            ship.update(timer);
            for (Asteroid asteroid : asteroids) {
                asteroid.update(timer, ship.getProjectiles());
            }
            asteroids.removeIf((Asteroid asteroid) -> asteroid.destroyed());
        }
    }

    public void input(Input input) {
        // states.get(0).input(input);

        // if (input.isKeyReleased(input.EXIT)) {
        // if (!states.isEmpty())
        // remove(0);
        // setCameraOffset(new Vector3f());
        // setCameraPosition(new Vector3f());
        // }
        if (input.isKeyPressed(KEYS.EXIT)) {
            building = !building;
            if (!building) {
                ship.checkIntegrity();
                ship.CalculateCoM();
                ship.CalculateInertia();
                ship.save("res/ship-data");
            }
        }

        if (building) {
            if (input.isButtonDown(KEYS.MOUSE_LEFT)) {
                if (selected == null) {
                    selected = new Block(10, 10, "block/block.png");
                } else if (input.isKeyDown(KEYS.LEFT)) {
                    selected = new Block(10, 10, "block/block.png");
                } else if (input.isKeyDown(KEYS.DOWN)) {
                    selected = new Thruster(10, 10, 5, KEYS.DOWN);
                } else if (input.isKeyDown(KEYS.RIGHT)) {
                    selected = new Weapon(10, 10, KEYS.UP);
                }
                selected_pos = input.getMousePos(true, false);

            } else if (selected != null) {
                int pos_x = Math.round(selected_pos.x);
                int pos_y = Math.round(selected_pos.y);
                ship.addBlock(new Vector2i(pos_x, pos_y), selected);
                selected = null;
            }

            if (input.isButtonDown(KEYS.MOUSE_RIGHT)) {
                selected_pos = input.getMousePos(true, false);

                int pos_x = Math.round(selected_pos.x);
                int pos_y = Math.round(selected_pos.y);
                ship.delBlock(new Vector2i(pos_x, pos_y));
            }
        } else {
            // camera follow
            float speed = ship.getSpeed(true);
            float lerp = (speed > 10) ? speed / (10 + speed) : speed / 40 + 0.25f;

            Vector2f shipPos = ship.getPosition().mul(lerp);
            camera.getPosition().mul(1 - lerp).sub(shipPos.x, shipPos.y, 0);

            // camera.setPosition(old);
            ship.input(input);
        }
    }

    public void render(Renderer renderer) {
        // if (!states.isEmpty())
        // states.get(0).render(renderer);
        if (building) {
            renderer.render(new Matrix4f(), new Model(new Vector3f(1, 1, 0)), new Texture("mask/lightgrey.png"), false,
                    false);

            // render components
            ship.components.forEach((loc, block) -> {
                Matrix4f result = new Matrix4f().translate(loc.x, loc.y, 0);
                renderer.render(result, Block.model, block.getDefaultTexture(), true, false);
            });

            if (selected != null) {
                Matrix4f result = new Matrix4f().translate(selected_pos.x, selected_pos.y, 0);
                renderer.render(result, Block.model, selected.getDefaultTexture(), true, false);
            }

            // center of mass marker
            Matrix4f result = new Matrix4f().translate(ship.centerOfMass.x, ship.centerOfMass.y, 0);
            renderer.render(result, Block.model, new Texture("block/marker.png"), true, false);
        } else {
            ship.render(renderer);
            for (Asteroid asteroid : asteroids) {
                asteroid.render(renderer);
            }
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
