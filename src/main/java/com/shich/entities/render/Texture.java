package com.shich.entities.render;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Hashtable;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Texture implements Serializable {

    private int texture_id = -1;

    @XmlElement
    private String texture_file;

    private static Hashtable<String, Integer> loaded = new Hashtable<String, Integer>();

    public Texture(String texture_file) {
        this.texture_file = texture_file;

        if (loaded.containsKey(texture_file)) { // texture already loaded
            texture_id = loaded.get(texture_file);
            return;
        }
        texture_id = loadTexture();
        loaded.put(texture_file, texture_id);
    }

    public Texture() {
    }

    public int loadTexture() {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        ByteBuffer image = STBImage.stbi_load("res/graphics/" + texture_file, width, height, channels, 0);

        if (image == null) {
            System.out.println("texture not found: " + texture_file);
            return -1;
        }

        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
        glGenerateMipmap(GL_TEXTURE_2D);

        return id;
    }

    public void bind() {
        if (texture_id == -1) {
            if (!loaded.containsKey(texture_file)) {
                texture_id = loadTexture();
                loaded.put(texture_file, texture_id);
            } else {
                texture_id = loaded.get(texture_file);
            }
        }

        glBindTexture(GL_TEXTURE_2D, texture_id);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
