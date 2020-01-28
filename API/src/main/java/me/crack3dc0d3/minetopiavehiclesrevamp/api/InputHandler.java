package me.crack3dc0d3.minetopiavehiclesrevamp.api;

import org.bukkit.entity.Player;

public class InputHandler {

    private boolean w, a, s, d, space;

    private Player player;

    public InputHandler(boolean w, boolean a, boolean s, boolean d, boolean space, Player player) {
        this.w = w;
        this.a = a;
        this.s = s;
        this.d = d;
        this.space = space;
        this.player = player;
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

    public Player getPlayer() {
        return player;
    }
}
