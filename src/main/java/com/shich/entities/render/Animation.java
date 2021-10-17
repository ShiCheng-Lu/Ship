package com.shich.entities.render;

import com.shich.util.Timer;

public class Animation extends Texture {

    private Texture[] frames;
    private int frame_count;
    private int current_frame;
    private float time_since_last_frame;
    private float frame_time;

    public Animation(String file, int frame_count, float frame_time) {
        super();
        this.frame_count = frame_count;
        this.frame_time = frame_time;

        frames = new Texture[frame_count];
        for (int i = 0; i < frame_count; ++i) {
            frames[i] = new Texture(file + "/anim_" + i + ".png");
        }
    }

    public void set_frame(int frame) {
        current_frame = frame;
    }

    public void update(Timer timer, boolean increment) {
        if (increment) {
            time_since_last_frame += timer.delta;
            if (time_since_last_frame >= frame_time) {
                time_since_last_frame -= frame_time;
                current_frame++;
            }

            if (current_frame >= frame_count) {
                current_frame = 0;
            }
        }
    }

    public void bind() {
        frames[current_frame].bind();
    }
}
