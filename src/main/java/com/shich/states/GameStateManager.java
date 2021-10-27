package com.shich.states;

import java.util.ArrayList;

import com.shich.entities.Asteroid;
import com.shich.entities.Block;
import com.shich.entities.Ship;
import com.shich.entities.ShipFactory;
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
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.CallbackI.P;

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

        ship = new Ship();

        ship.checkIntegrity();
        ship.CalculateCoM();
        ship.CalculateInertia();

        ShipFactory.loadBlocks("res/block-data");

        ShipFactory.put("block", new Block(10, 10, "block/block.png"));
        ShipFactory.put("thruster", new Thruster(10, 10, 5, GLFW.GLFW_KEY_W));
        ShipFactory.put("weapon", new Weapon(10, 10, GLFW.GLFW_KEY_S));

        ShipFactory.saveBlocks("res/block-data");

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
                selected_pos = input.getMousePos(true, false);

                if (selected != null) {
                    if (input.isKeyPressed(GLFW.GLFW_KEY_A)) {
                        selected.rotate(1);
                    } else if (input.isKeyPressed(GLFW.GLFW_KEY_D)) {
                        selected.rotate(-1);
                    }
                } else if (selected_pos.distance(10, 4) < 0.5) {
                    selected = ShipFactory.get("block");
                } else if (selected_pos.distance(10, 2) < 0.5) {
                    selected = ShipFactory.get("thruster");
                } else if (selected_pos.distance(10, 0) < 0.5) {
                    selected = ShipFactory.get("weapon");
                } else {
                    int pos_x = Math.round(selected_pos.x);
                    int pos_y = Math.round(selected_pos.y);
                    selected = ship.delBlock(new Vector2i(pos_x, pos_y));
                }

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

            renderer.setDefaultOptions(true, false);

            renderer.render(new Matrix4f(), new Model(new Vector3f(1, 1, 0)), new Texture("mask/lightgrey.png"), false,
                    false);

            int pos = 0;
            for (Block block : ShipFactory.getAllBlocks()) {
                block.render(renderer, new Matrix4f().translate(10, pos, 0));
                pos += 2;
            }

            // render components
            ship.components.forEach((loc, block) -> {
                Matrix4f transform = new Matrix4f().translate(loc.x, loc.y, 0);
                block.render(renderer, transform);
            });

            if (selected != null) {
                Vector2i neighbours[] = { new Vector2i(0, -1), new Vector2i(0, 1), new Vector2i(-1, 0),
                        new Vector2i(1, 0) };
                int pos_x = Math.round(selected_pos.x);
                int pos_y = Math.round(selected_pos.y);
                boolean nearPlaced = false;

                for (Vector2i neighbour : neighbours) {
                    neighbour.add(pos_x, pos_y);

                    if (ship.components.get(neighbour) != null) {
                        nearPlaced = true;
                        break;
                    }
                }

                if (nearPlaced && selected_pos.distance(pos_x, pos_y) < 0.3) {
                    selected_pos = new Vector2f(pos_x, pos_y);
                }

                Matrix4f transform = new Matrix4f().translate(selected_pos.x, selected_pos.y, 0);
                selected.render(renderer, transform);
            }

            // center of mass marker
            Matrix4f result = new Matrix4f().translate(ship.centerOfMass.x, ship.centerOfMass.y, 0);
            renderer.render(result, Block.model, new Texture("block/marker.png"));

        } else {
            renderer.setDefaultOptions(true, true);

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
