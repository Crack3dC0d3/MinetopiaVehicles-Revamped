package me.crack3dc0d3.minetopiavehiclesrevamp.main.util;

import me.crack3dc0d3.minetopiavehiclesrevamp.api.ApiMethods;
import me.crack3dc0d3.minetopiavehiclesrevamp.api.InputHandler;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.Main;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Seat;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Methods implements ApiMethods {

    private static HashMap<Player, BossBar> bars = new HashMap<>();

    public static void setPosition(ArmorStand as, Location loc) {
        Main.getInstance().getNms().setPosition(as, loc);
    }

    public static String generateLicencePlate() {
        StringBuilder plate = new StringBuilder();
        plate.append(RandomStringUtils.random(2, true, false));
        plate.append("-");
        plate.append(RandomStringUtils.random(2, true, false));
        plate.append("-");
        plate.append(RandomStringUtils.random(2, true, false));
        return plate.toString().toUpperCase();
    }

    @Override
    public void handleInput(InputHandler handler) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (handler.getPlayer().getVehicle() instanceof ArmorStand) {
                    ArmorStand arm = (ArmorStand) handler.getPlayer().getVehicle();
                    Seat seat = Seat.getSeat(arm);
                    if (seat == null) {
                        return;
                    }
                    InputManager inputManager = new InputManager(handler.isW(), handler.isA(), handler.isS(), handler.isD(), handler.isSpace());

                    VehicleMover.doMovement(inputManager, handler.getPlayer());
                }
            }
        }.runTask(Main.getInstance());
    }

    public static void updateBar(Player player, BarColor color, String text, BarStyle style, float progress, boolean visible) {
        BossBar bar = bars.get(player);
        if(bar == null) {
            bar = Bukkit.getServer().createBossBar(text, color, style);
            bar.setProgress(progress);
            bar.addPlayer(player);
            bars.put(player, bar);
        } else {
            bar.setColor(color);
            bar.setProgress(progress);
            bar.setStyle(style);
            bar.setTitle(text);
        }
        bar.setVisible(visible);
    }

    public static void setBarVisible(Player p, boolean visible) {
        BossBar bar = bars.get(p);
        bar.setVisible(visible);
    }

}
