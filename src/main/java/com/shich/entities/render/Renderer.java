package com.shich.entities.render;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import com.shich.util.Camera;

import org.joml.Matrix4f;

public class Renderer {

    private Shader shader;
    private Camera camera;

    public Renderer(Shader shader, Camera camera) {
        this.camera = camera;
        this.shader = shader;
    }

    public void render(Matrix4f trans, Model model, Texture texture) {
        render(trans, model, texture, true, true);
    }

    public void render(Matrix4f trans, Model model, Texture texture, boolean useTransform, boolean useTranslate) {
        Matrix4f projection = useTransform ? camera.getTransform() : new Matrix4f();
        if (useTranslate) {
            projection.translate(camera.getPosition());
        }
        projection.mul(trans);

        shader.bind();
        texture.bind();

        shader.setUniform("tex", 0);
        shader.setUniform("projection", projection);
        renderModel(model);

        texture.unbind();
        shader.unbind();
    }

    private void renderModel(Model model) {

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glBindVertexArray(model.getVao());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, model.getIbo());

        glDrawElements(GL_TRIANGLES, model.getDraw_count(), GL_UNSIGNED_INT, 0);
        // unbind and disable state
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }
}
