package com.shich.util;

public enum KEYS {
    ACTION,
    LEFT,
    RIGHT,
    UP,
    DOWN,
    JUMP,
    DASH,
    FULLSCREEN,
    EXIT,
    MOUSE_LEFT,
    MOUSE_RIGHT,
    MOUSE_MIDDLE,
    ;
    public static final int size;
    static {
        size = KEYS.values().length;
    }
}
