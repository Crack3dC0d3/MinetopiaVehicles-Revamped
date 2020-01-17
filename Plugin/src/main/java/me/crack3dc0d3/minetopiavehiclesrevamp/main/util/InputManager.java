package me.crack3dc0d3.minetopiavehiclesrevamp.main.util;

public class InputManager {

    private boolean w, a, s, d, space;

    public InputManager(boolean w, boolean a, boolean s, boolean d, boolean space) {
        this.w = w;
        this.a = a;
        this.s = s;
        this.d = d;
        this.space = space;
    }

    public boolean isW() {
        return w;
    }

    public boolean isA() {
        return a;
    }

    public boolean isS() {
        return s;
    }

    public boolean isD() {
        return d;
    }

    public boolean isSpace() {
        return space;
    }
}
