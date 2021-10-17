package com.shich.entities.move;

import com.shich.util.Input;
import com.shich.util.KEYS;
import com.shich.util.Timer;

public class Dash {
    
    private Movement move;

    private boolean can_dash;
    private boolean in_dash;
    private float time;
    private float dash_time;
    private float cooldown;
    private float dash_vel;
    private float dash_dist;

    public Dash(Movement move) {
        this.move = move;

        can_dash = true;
        in_dash = false;
        time = 0;
        cooldown = 2;
        dash_vel = 50f;
        dash_dist = 5f;
        dash_time = dash_dist / dash_vel;
    }

    public void input(Input input) {
        // cannot dash, no input to take
        if (!can_dash) {
            return;
        }

        if (input.isKeyPressed(KEYS.DASH)) {
            time = 0;
            can_dash = false;
            in_dash = true;
        }
    }

    public void update(Timer timer) {
        // can dash, no update for dash
        if (can_dash) {
            return;
        }
        // cannot dash
        time += timer.delta;
        if (in_dash) {
            // update dash
            move.vel.y = 0;
            move.vel.x = dash_vel * move.facing;
            
            if (time >= dash_time) {
                in_dash = false;
            }
        } else if (time >= dash_time + cooldown) {
            can_dash = true;
        }
    }
}
