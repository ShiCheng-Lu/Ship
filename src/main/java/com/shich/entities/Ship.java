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
import jakarta.xml.bind.annotation.XmlTransient;

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

    /**
     * save the {ship} to the file
     * 
     * @param file
     */
    public static void save(Ship ship, String file) {
        try {
            marshallerObj.marshal(ship, new File(file));
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
        Ship.save(this, file);
    }

    @XmlTransient
    private ArrayList<Projectile> bolts = new ArrayList<Projectile>();

    public Map<Vector2i, Block> components = new Hashtable<Vector2i, Block>();

    public Vector2f centerOfMass = new Vector2f(0, 0);
    private int mass;
    private float inertia;

    private float aRot;
    private float aVel;
    protected float torque;

    // pos is the position of the CoM
    private Vector2f pos = new Vector2f();
    private Vector2f vel = new Vector2f();
    protected float force;

    public Ship() {
        addBlock(new Vector2i(0, 0), new Block(10, 100, "block/core.png"));
    }

    public Vector2f getCoM() {
        return centerOfMass;
    }

    public void addBlock(Vector2i location, Block block) {
        // add to components;
        if (components.containsKey(location)) {
            delBlock(location);
        }
        components.put(new Vector2i(location), block);
        // add to centerOfMass;
        Vector2f blockCoM = new Vector2f(location);

        blockCoM.mul(block.mass);

        centerOfMass.mul(mass);
        centerOfMass.add(blockCoM);

        mass += block.mass;
        centerOfMass.div(mass);
    }

    public void delBlock(Vector2i location) {
        Block block = components.remove(location);
        if (block == null) {
            return; // Block not in ship
        }
        // remove from centerOfMass
        Vector2f blockCoM = new Vector2f(location);

        blockCoM.mul(block.mass);

        centerOfMass.mul(mass);
        centerOfMass.sub(blockCoM);

        mass -= block.mass;
        centerOfMass.div(mass);
    }

    public void CalculateCoM() {
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

        components.forEach((loc, block) -> block.update(timer, this, loc));
        // force
        force = force / mass * timer.delta;

        vel.add(force * (float) -Math.sin(aRot), force * (float) Math.cos(aRot));
        pos.add(vel);

        // torque
        aVel += torque / inertia * timer.delta;

        aRot += aVel;

        //
        if (force == 0) {
            vel.mul(0.99f);
            aVel *= 0.99;
        }

        for (Projectile proj : bolts) {
            proj.update(timer);
        }
        bolts.removeIf((proj) -> timer.getTime() >= proj.lifespan);

        force = 0;
        torque = 0;

    }

    public void input(Input input) {

        for (Block block : components.values()) {
            block.input(input);
        }
        if (input.isKeyDown(KEYS.LEFT)) {
            torque = 1;
        }
        if (input.isKeyDown(KEYS.RIGHT)) {
            torque = -1;
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
            renderer.render(result, Block.model, block.getTexture());
        });
        // render center of mass marker
        Matrix4f result = transform.translate(centerOfMass.x, centerOfMass.y, 0, new Matrix4f());
        result.rotate(aRot, 0, 0, -1);
        renderer.render(result, Block.model, new Texture("block/marker.png"));

        for (Projectile proj : bolts) {
            proj.render(renderer);
        }
    }

    public void checkIntegrity() {
        ArrayList<Vector2i> connected = new ArrayList<Vector2i>();
        connected.add(new Vector2i(0, 0));

        int idx = 0;
        while (connected.size() != idx) {
            Vector2i pos = connected.get(idx);
            idx++;

            Block block = components.get(pos);
            if (block == null) {
                continue;
            }
            if ((block.attachment & 0b0001) != 0) {
                Vector2i npos = new Vector2i(pos).add(0, 1);
                if (!connected.contains(npos)) {
                    connected.add(npos);
                }
            }
            if ((block.attachment & 0b0010) != 0) {
                Vector2i npos = new Vector2i(pos).add(0, -1);
                if (!connected.contains(npos)) {
                    connected.add(npos);
                }
            }
            if ((block.attachment & 0b0100) != 0) {
                Vector2i npos = new Vector2i(pos).add(1, 0);
                if (!connected.contains(npos)) {
                    connected.add(npos);
                }
            }
            if ((block.attachment & 0b1000) != 0) {
                Vector2i npos = new Vector2i(pos).add(-1, 0);
                if (!connected.contains(npos)) {
                    connected.add(npos);
                }
            }
        }

        components.keySet().removeIf((Vector2i pos) -> !connected.contains(pos));
    }

    public float getSpeed(boolean squared) {
        float speed = vel.x * vel.x + vel.y * vel.y;
        return squared ? speed : (float) Math.sqrt(speed);
    }

    public Vector2f getPosition() {
        return new Vector2f(pos);
    }

    public String toString() {
        return Integer.toString(components.size());
    }

    public ArrayList<Projectile> getProjectiles() {
        return bolts;
    }

    /**
     * add a new projectile, projectile x, y is the origin relative to the ship's
     * (0, 0)
     * 
     * @param projectile
     */
    public void addProjectile(Projectile projectile) {
        Matrix2f rotation = new Matrix2f().rotate(aRot);

        projectile.pos.sub(centerOfMass);
        projectile.pos.mul(rotation);
        projectile.pos.add(pos);

        projectile.vel.mul(rotation);
        projectile.vel.add(vel);

        projectile.rot += aRot;

        bolts.add(projectile);
    }
}
