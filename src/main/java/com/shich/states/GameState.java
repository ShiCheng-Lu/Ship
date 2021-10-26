package com.shich.states;

import com.shich.entities.render.Renderer;
import com.shich.util.Input;
import com.shich.util.Timer;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GameState {

    // protected GameStateManager gsm;
    // protected Pause pause;
    public int size;
    public int number;

    public GameState(GameStateManager gsm) {
        // this.gsm = gsm;
        size = 1;
        number = 2;
    }

    public GameState() {
    }

    public void input(Input input) {
        // if (pause != null)
        // pause.input(input);
    }

    public void update(Timer timer) {
    }

    public void render(Renderer renderer) {
    }

}
