package com.shich.util;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class Input {

    private GLFWKeyCallback key_callback;
    private GLFWMouseButtonCallback mouse_callback;
    private GLFWCursorPosCallback cursor_callback;

    private Window window;

    private boolean[] key_state = new boolean[GLFW_KEY_LAST];
    private boolean[] button_action = new boolean[GLFW_MOUSE_BUTTON_LAST];
    public Vector3f mouse_pos; // with z = 0;
    public Vector3f mouse_pos_raw;
    public Camera camera;

    public int max_binding = 5;
    public HashMap<KEYS, ArrayList<Integer>> bindings = new HashMap<KEYS, ArrayList<Integer>>(KEYS.size);

    public Input(Window window, Camera camera) {

        this.window = window;
        this.camera = camera;

        mouse_pos = new Vector3f();
        mouse_pos_raw = new Vector3f();

        key_callback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action != GLFW_REPEAT) {
                    key_state[key] = true;
                }
            }

            public void close() {
                super.close();
            }

            public void callback(long args) {
                super.callback(args);
            }

            public String getSignature() {
                return super.getSignature();
            }
        };

        mouse_callback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (button != GLFW_REPEAT) {
                    button_action[button] = true;
                }
            }

            public void close() {
                super.close();
            }

            public void callback(long args) {
                super.callback(args);
            }

            public String getSignature() {
                return super.getSignature();
            }
        };

        cursor_callback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                float width = Input.this.window.getWidth();
                float height = Input.this.window.getHeight();

                mouse_pos_raw.x = (float) (xpos * 2 / width - 1);
                mouse_pos_raw.y = (float) (1 - ypos * 2 / height);

                mouse_pos = Input.this.camera
                        .reverseProjection(new Vector3f((float) (xpos - width / 2.0), (float) (height / 2.0 - ypos), 0));
            }

            public void close() {
                super.close();
            }

            public void callback(long args) {
                super.callback(args);
            }

            public String getSignature() {
                return super.getSignature();
            }
        };

        glfwSetKeyCallback(window.window, key_callback);
        glfwSetMouseButtonCallback(window.window, mouse_callback);
        glfwSetCursorPosCallback(window.window, cursor_callback);

        resetKeyBind();
    }

    public void resetKeyBind() {
        // read file "res/input/active"
        String[] content = Files.readFile("input/default").split("\n");
        KEYS[] keys = KEYS.values();

        for (int i = 0; i < KEYS.size; ++i) {
            KEYS key = keys[i];
            String[] bindings = content[i].split(" ");

            this.bindings.put(key, new ArrayList<Integer>());

            for (String binding : bindings) {
                addKeyBind(key, Integer.parseInt(binding));
            }
        }
    }

    public void saveKeyBind(String file) {
        String out = "";
        KEYS[] keys = KEYS.values();

        for (int i = 0; i < KEYS.size; ++i) {
            for (int k : bindings.get(keys[i])) {
                out += k + " ";
            }
            out += "\n";
        }
        Files.writeFile("input/" + file, out);
        System.out.println("saved");
    }

    public void addKeyBind(KEYS key, int binding) {
        bindings.get(key).add(binding);
    }

    public void removeKeyBind(KEYS key, int binding) {
        bindings.get(key).remove(binding);
    }

    public boolean isGLFW_KeyDown(int glfw_key) {
        return glfwGetKey(window.window, glfw_key) == GLFW_PRESS;
    }

    public boolean isGLFWButtonDown(int glfw_button) {
        return glfwGetMouseButton(window.window, glfw_button) == GLFW_PRESS;
    }

    public boolean isKeyPressed(KEYS key) {
        for (int glfw_key : bindings.get(key)) {
            if (key_state[glfw_key] && isGLFW_KeyDown(glfw_key)) {
                return true;
            }
        }
        return false;
    }

    public boolean isKeyReleased(KEYS key) {
        for (int glfw_key : bindings.get(key)) {
            if (key_state[glfw_key] && !isGLFW_KeyDown(glfw_key)) {
                return true;
            }
        }
        return false;
    }

    public boolean isKeyDown(KEYS key) {
        for (int glfw_key : bindings.get(key)) {
            if (isGLFW_KeyDown(glfw_key)) {
                return true;
            }
        }
        return false;
    }

    public boolean isButtonPressed(KEYS button) {
        for (int glfw_button : bindings.get(button)) {
            if (button_action[glfw_button] && isGLFWButtonDown(glfw_button)) {
                return true;
            }
        }
        return false;
    }

    public boolean isButtonReleased(KEYS button) {
        for (int glfw_button : bindings.get(button)) {
            if (button_action[glfw_button] && !isGLFWButtonDown(glfw_button)) {
                return true;
            }
        }
        return false;
    }

    public boolean isButtonDown(KEYS button) {
        for (int glfw_button : bindings.get(button)) {
            if (isGLFWButtonDown(glfw_button)) {
                return true;
            }
        }
        return false;
    }

    public void update() {
        if (isButtonPressed(KEYS.MOUSE_MIDDLE)) {
            saveKeyBind("active");
        }

        for (int i = GLFW_KEY_SPACE; i < GLFW_KEY_LAST; ++i) {
            key_state[i] = false;
        }
        for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST; ++i) {
            button_action[i] = false;
            ;
        }
    }

    public void destroy() {
        key_callback.free();
        mouse_callback.free();
        cursor_callback.free();
    }
}
