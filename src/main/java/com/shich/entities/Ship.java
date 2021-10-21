package com.shich.entities;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import com.shich.entities.render.Model;
import com.shich.entities.render.Renderer;
import com.shich.entities.render.Texture;
import com.shich.util.Input;
import com.shich.util.KEYS;
import com.shich.util.Timer;

import org.joml.Matrix2f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Ship {
    public static JAXBContext contextObj;
    private static Marshaller marshallerObj;
    private static Unmarshaller unmarshallerObj;

    static {
        try {
            contextObj = JAXBContext.newInstance(Ship.class, Block.class, Thruster.class, Weapon.class);
            marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            unmarshallerObj = contextObj.createUnmarshaller();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * save the {ship} to the file
     * 
     * @param file
     */
    public void save(String file) {
        try {
            marshallerObj.marshal(this, new File(file));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * load a {ship} from a file
     * 
     * @param file
     */
    public static Ship load(String file) {
        try {
            return (Ship) unmarshallerObj.unmarshal(new File(file));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Projectile> bolts = new ArrayList<Projectile>();

    protected Map<Vector2i, Block> components = new Hashtable<Vector2i, Block>();

    private Vector2f centerOfMass = new Vector2f(0, 0);
    private int mass;
    private float inertia;

    private float aRot;
    private float aVel;
    private float aAcc;

    // pos is the position of the CoM
    private Vector2f pos = new Vector2f();
    private Vector2f vel = new Vector2f();
    private Vector2f acc = new Vector2f();

    public Ship() {
        addBlock(new Vector2i(0, 0), new Block(10, 100, "block/core.png"));
    }

    public Vector2f getCoM() {
        return centerOfMass;
    }

    public void addBlock(Vector2i location, Block block) {
        // add to components;
        components.put(new Vector2i(location), block);
        // add to centerOfMass;
        Vector2f blockCoM = new Vector2f(location);

        blockCoM.mul(block.mass);

        centerOfMass.mul(mass);
        centerOfMass.add(blockCoM);

        mass += block.mass;
        centerOfMass.div(mass);
    }

    public void ReCalculateCoM() {
        mass = 0;
        centerOfMass.set(0, 0);

        components.forEach((loc, block) -> {
            mass += block.mass;
            centerOfMass.add(new Vector2f(loc).mul(block.mass));
        });

        centerOfMass.div(mass);
    }

    public void CalculateInertia() {
        inertia = 0;

        components.forEach((loc, block) -> {
            inertia += block.inertia;
            float distance = 0;
            distance += (centerOfMass.x - loc.x) * (centerOfMass.x - loc.x);
            distance += (centerOfMass.y - loc.y) * (centerOfMass.y - loc.y);
            inertia += block.mass * distance;
        });
    }

    public void update(Timer timer) {
        Vector2f forces = new Vector2f();

        components.forEach((loc, block) -> {
            if (block instanceof Thruster && ((Thruster) block).on) {
                forces.x += ((Thruster) block).thrust;
                forces.y += (loc.x - centerOfMass.x) * ((Thruster) block).thrust;

                if (block instanceof Weapon && ((Weapon) block).shot()) {
                    Vector2f projLoc = new Vector2f(loc).sub(centerOfMass);

                    projLoc.mul(new Matrix2f().rotate(aRot));
                    projLoc.add(pos);

                    bolts.add(new Projectile(projLoc, aRot, timer.getTime() + 1));

                }
            }
        });
        // force
        float force = forces.x / mass * timer.delta;
        acc.y = force * (float) Math.cos(aRot);
        acc.x = force * (float) -Math.sin(aRot);

        vel.add(acc);
        pos.add(vel);

        // torque
        aAcc = forces.y / inertia * timer.delta;
        aVel += aAcc;

        aRot += aVel;

        //
        if (acc.x == 0 && acc.y == 0) {
            vel.mul(0.99f);
            aVel *= 0.99;
        }

        for (Projectile proj : bolts) {
            proj.update(timer);
        }
        bolts.removeIf((proj) -> timer.getTime() > proj.lifespan);
    }

    public void input(Input input) {

        for (Block block : components.values()) {
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
        Matrix4f transform = new Matrix4f();
        transform.translate(pos.x, pos.y, 0);
        transform.rotate(aRot, 0, 0, 1);
        transform.translate(-centerOfMass.x, -centerOfMass.y, 0);

        // render components
        components.forEach((loc, block) -> {
            Matrix4f result = transform.translate(loc.x, loc.y, 0, new Matrix4f());
            renderer.render(result, Block.model, block.texture);
        });

        // render center of mass marker
        Matrix4f result = transform.translate(centerOfMass.x, centerOfMass.y, 0, new Matrix4f());
        result.rotate(aRot, 0, 0, -1);
        renderer.render(result, new Model(new Vector3f(0.5f, 0.5f, 0)), new Texture("block/marker.png"));

        for (Projectile proj : bolts) {
            proj.render(renderer);
        }
    }

    public void checkIntegrety() {
        ArrayList<Block> connected = new ArrayList<Block>();

        int idx = 0;
        while (connected.size() != idx) {

        }
    }

    public Vector2f getPosition() {
        return new Vector2f(pos);
    }

    public String toString() {
        return Integer.toString(components.size());
    }
}
