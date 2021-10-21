package com.shich.states;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.shich.entities.Block;
import com.shich.entities.Ship;
import com.shich.entities.Thruster;
import com.shich.entities.Weapon;
import com.shich.entities.render.Renderer;
import com.shich.util.Camera;
import com.shich.util.Input;
import com.shich.util.KEYS;
import com.shich.util.Timer;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class GameStateManager {
    /*
     * starts / end / pause the game
     */

    Camera camera;
    public ArrayList<GameState> states = new ArrayList<GameState>();

    Ship ship;

    public GameStateManager(Camera camera) {
        states.add(new GameState(this));
        this.camera = camera;

        ship = new Ship();

        // ship = new Gson().fromJson(Files.readFile("ship.json"), Ship.class);

        ship.addBlock(new Vector2i(-1, 1), new Block(10, 100));
        ship.addBlock(new Vector2i(0, 1), new Block(10, 100));
        ship.addBlock(new Vector2i(1, 1), new Block(10, 100));

        ship.addBlock(new Vector2i(0, 2), new Block(10, 100));

        ship.addBlock(new Vector2i(-1, 0), new Block(10, 100));
        ship.addBlock(new Vector2i(1, 0), new Block(10, 100));

        ship.addBlock(new Vector2i(-1, -1), new Thruster(0, 100, 10, KEYS.LEFT));
        ship.addBlock(new Vector2i(1, -1), new Thruster(0, 100, 10, KEYS.RIGHT));
        ship.addBlock(new Vector2i(0, -1), new Thruster(0, 100, 10, KEYS.DOWN));

        ship.addBlock(new Vector2i(1, 2), new Weapon(2, 100, KEYS.UP));

        ship.ReCalculateCoM();
        ship.CalculateInertia();

        try {
            JAXBContext contextObj = JAXBContext.newInstance(Ship.class, Block.class, Thruster.class, Weapon.class);
            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);


            Unmarshaller unmarshallerObj = contextObj.createUnmarshaller();

            // save
            marshallerObj.marshal(ship, new FileOutputStream("res/ship-data"));

            // load
            // ship = (Ship) unmarshallerObj.unmarshal(new FileInputStream("res/ship-data"));

        } catch (JAXBException | FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(ship);

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

        ship.input(input);
    }

    public void render(Renderer renderer) {
        // if (!states.isEmpty())
        // states.get(0).render(renderer);
        ship.render(renderer);
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
