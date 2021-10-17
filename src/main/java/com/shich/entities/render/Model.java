package com.shich.entities.render;

import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

public class Model {

    private int draw_count;
    private int vao;
    private int vbo, tbo, ibo;

    public Model(float[] vertices, float[] texture, int[] indices) {
        draw_count = indices.length;

        createModel(vertices, texture, indices);
    }

    public Model(Vector3f half_extent) {
        float[] vertices = new float[] {
            -half_extent.x,  half_extent.y, 0,
             half_extent.x,  half_extent.y, 0,
             half_extent.x, -half_extent.y, 0,
            -half_extent.x, -half_extent.y, 0,
        };

        float[] texture = new float[] { 
            0, 0, 
            1, 0, 
            1, 1, 
            0, 1, 
        };
            
        int[] indices = new int[] { 
            0, 1, 2, 
            2, 3, 0, 
        };
        draw_count = indices.length;

        createModel(vertices, texture, indices);
    }

    public void createModel(float[] vertices, float[] texture, int[] indices) {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = storeData(floatBuffer(vertices), 0, 3);
        tbo = storeData(floatBuffer(texture), 1, 2);

        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private int storeData(FloatBuffer buffer, int index, int size) {
        int buffer_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, buffer_id);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return buffer_id;
    }

    private FloatBuffer floatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public void destroy() {
        glDeleteBuffers(vbo);
        glDeleteBuffers(tbo);
        glDeleteBuffers(ibo);
        glDeleteVertexArrays(vao);
    }
    
    public int getDraw_count() {
        return draw_count;
    }

    public int getVao() {
        return vao;
    }

    public int getVbo() {
        return vbo;
    }

    public int getTbo() {
        return tbo;
    }

    public int getIbo() {
        return ibo;
    }


}
