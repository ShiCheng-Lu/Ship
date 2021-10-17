package com.shich.states;

import com.shich.entities.render.Renderer;
import com.shich.util.Input;
import com.shich.util.Timer;

public class GameState {

    protected GameStateManager gsm;
    // protected Pause pause;

    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
    }

    public void input(Input input) {
        // if (pause != null)
        //     pause.input(input);
    }

    public void update(Timer timer) {
    }

    public void render(Renderer renderer) {
    }

}
