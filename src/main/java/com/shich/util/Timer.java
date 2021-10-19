package com.shich.util;

public class Timer {

    public float delta;
    private long last_update_time;
    private double targetDelta;

    public Timer(int targetFps) {
        targetDelta = 1.0 / targetFps;
    }

    public Timer() {
        this(60);
    }

    public void update() {
        long current_time = System.nanoTime();
        delta = ns_to_s(current_time - last_update_time);
        last_update_time = current_time;
    }

    /**
     * get the time in seconds as a float
     * 
     * @return
     */
    public float getTime() {
        return ns_to_s(System.nanoTime());
    }

    public float ns_to_s(long nano_sec) {
        return (float) nano_sec / 1000000000;
    }

    public double getTargetTime() {
        return targetDelta;
    }

}
