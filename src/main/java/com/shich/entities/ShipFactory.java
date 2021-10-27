package com.shich.entities;

import java.io.File;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
class BlockStore {
    public Map<String, Block> data = new Hashtable<String, Block>();
}

public class ShipFactory {
    private static JAXBContext contextObj;
    private static Marshaller marshallerObj;
    private static Unmarshaller unmarshallerObj;
    private static BlockStore allBlocks = new BlockStore();

    static {
        try {
            contextObj = JAXBContext.newInstance(Ship.class, Block.class, Thruster.class, Weapon.class,
                    BlockStore.class);
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

    public static void loadBlocks(String file) {
        try {
            allBlocks = (BlockStore) unmarshallerObj.unmarshal(new File(file));
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    public static void saveBlocks(String file) {
        try {
            marshallerObj.marshal(allBlocks, new File(file));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static void put(String key, Block value) {
        allBlocks.data.put(key, value);
    }

    public static Block get(String key) {
        return allBlocks.data.get(key).clone();
    }

    public static Collection<Block> getAllBlocks() {
        return allBlocks.data.values();
    }
}
