package com.shich;

import static org.lwjgl.glfw.GLFW.glfwInit;

import com.shich.entities.render.Renderer;
import com.shich.entities.render.Shader;
import com.shich.states.GameStateManager;
import com.shich.util.Camera;
import com.shich.util.Input;
import com.shich.util.KEYS;
import com.shich.util.Timer;
import com.shich.util.Window;

import org.lwjgl.glfw.GLFWErrorCallback;

public class Main implements Runnable {
    private Thread game;
    private Window window;
    private Shader shader;
    private Input input;
    private Camera camera;
    private Renderer renderer;
    private Timer timer;

    private GameStateManager gsm;

    // temps

    public void start() {
        game = new Thread(this, "game thread");
        game.start();
    }

    private void init() {
        GLFWErrorCallback.createPrint().set();

        if (!glfwInit()) {
            System.err.println("GLFW failed to initiate");
            System.exit(1); // exit with error code 1
        }

        window = new Window(1920, 1080, "Ship Thing", true);
        window.setBackgroundColour(0, 0, 0);
        shader = new Shader("shaders/shader");
        camera = new Camera(window);
        input = new Input(window, camera);
        renderer = new Renderer(shader, camera);
        timer = new Timer();

        gsm = new GameStateManager(camera);
    }

    public void destroy() {
        window.destroy();
    }

    public void close() {
        destroy();
    }

    @Override
    public void run() {
        init();

        while (!window.shouldClose()) {
            input(input);
            update(timer);
            render(renderer);
        }

        destroy();
    }

    public void input(Input input) {
        if (input.isButtonPressed(KEYS.MOUSE_RIGHT)) {
            System.out.println(input.mouse_pos);
        }

        gsm.input(input);
        window.input(input);
    }

    public void update(Timer timer) {
        timer.update(); // must be updated before updates with timers
        input.update();

        gsm.update(timer);

        window.update(timer);

        if (gsm.states.isEmpty()) {
            window.setShouldClose(true);
        }

        camera.update(); // must be updated after player update (after camera.setposition)
    }

    public void render(Renderer renderer) {
        gsm.render(renderer);
        window.swapBuffers();
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
