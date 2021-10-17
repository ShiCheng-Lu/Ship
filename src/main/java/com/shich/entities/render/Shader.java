package com.shich.entities.render;

import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import com.shich.util.Files;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

public class Shader {
    private int program;
    private int vert;
    private int frag;

    public Shader(String file_name) {
        program = glCreateProgram();
        // create vertex shader
        vert = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vert, Files.readFile(file_name + ".vert"));
        glCompileShader(vert);
        if (glGetShaderi(vert, GL_COMPILE_STATUS) == GL_FALSE) { // error checking
            System.err.println("Vertex Shader: " + glGetShaderInfoLog(vert));
            System.exit(1);
        }
        // create fragment shader
        frag = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(frag, Files.readFile(file_name + ".frag"));
        glCompileShader(frag);
        if (glGetShaderi(frag, GL_COMPILE_STATUS) == GL_FALSE) { // error checking
            System.err.println("Fragment Shader: " + glGetShaderInfoLog(frag));
            System.exit(1);
        }

        glAttachShader(program, vert);
        glAttachShader(program, frag);

        glBindAttribLocation(program, 0, "vertices");

        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("Program Linking: " + glGetProgramInfoLog(program));
            System.exit(1);
        }
        glValidateProgram(program);
        if (glGetProgrami(program, GL_VALIDATE_STATUS) == GL_FALSE) {
            System.err.println("Program Validate" + glGetProgramInfoLog(program));
            System.exit(1);
        }

        glDeleteShader(vert);
        glDeleteShader(frag);
    }

    public void bind() {
        glUseProgram(program);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void destroy() {
        glDetachShader(program, vert);
        glDetachShader(program, frag);
        glDeleteShader(vert);
        glDeleteShader(frag);
        glDeleteProgram(program);
    }

    public void setUniform(String name, int value) {
        int location = glGetUniformLocation(program, name);
        if (location != -1) {
            glUniform1i(location, value);
        } else {
            System.out.println(name);
        }
    }

    public void setUniform(String name, Matrix4f value) {
        int location = glGetUniformLocation(program, name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);
        if (location != -1) {
            glUniformMatrix4fv(location, false, buffer);
        } else {
            System.out.println(name);
        }
    }
}
