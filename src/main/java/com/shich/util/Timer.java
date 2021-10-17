package com.shich.util;

public class Timer {
    
    public float delta;
    private long last_update_time;
    private float targetDelta;

    public Timer() {
        targetDelta = 1.0f / 60;
    }

    public void update() {
        long current_time = System.nanoTime();
        delta = ns_to_s(current_time - last_update_time);
        last_update_time = current_time;
    }

    public float getTime() {
        return ns_to_s(System.nanoTime());
    }

    public float ns_to_s(long nano_sec) {
        return (float) nano_sec / 1000000000;
    }

    public float getTargetTime() {
        return targetDelta;
    }

}
