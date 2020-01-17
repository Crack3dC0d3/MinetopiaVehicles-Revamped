package me.crack3dc0d3.minetopiavehiclesrevamp.main.util;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.Main;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Seat;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleMover;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Methods {

    public static void handleInput(boolean w, boolean a, boolean s, boolean d, boolean space, Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (p.getVehicle() instanceof ArmorStand) {
                    ArmorStand arm = (ArmorStand) p.getVehicle();
                    Seat seat = Seat.getSeat(arm);
                    if (seat == null) {
                        return;
                    }
                    InputManager inputManager = new InputManager(w, a, s, d, space);

                    VehicleMover.doMovement(inputManager, p);
                }
            }
        }.runTask(Main.getInstance());
    }

    public static void setPosition(ArmorStand as, Location loc) {
        Main.getInstance().getNms().setPosition(as, loc);
    }
}
