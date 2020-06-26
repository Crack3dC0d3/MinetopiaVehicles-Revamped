package me.crack3dc0d3.minetopiavehiclesrevamp.main.events;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.Main;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.enums.VehicleType;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Seat;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.enums.Messages;
import net.minecraft.server.v1_12_R1.DedicatedServer;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

public class Dismount implements Listener {

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if(event.getEntity() instanceof Player) {
            if(event.getDismounted() instanceof ArmorStand) {
                ArmorStand stand = (ArmorStand) event.getDismounted();
                Seat s = Seat.getSeat(stand);
                if(s == null) {
                    return;
                }
                if(getDistance(s.getMainVehicle().getMainStand()) >= 2) {
                    (new BukkitRunnable() {
                        @Override
                        public void run() {
                            event.getDismounted().addPassenger(event.getEntity());
                            Messages.send(event.getEntity(), Messages.CANNOT_EXIT_IN_AIR);
                        }
                    }).runTaskLaterAsynchronously(Main.getInstance(), 1);
                } else {
                    s.getMainVehicle().getMainStand().setVelocity(s.getMainVehicle().getMainStand().getVelocity().add(new Vector(0, -100, 0)));
                    s.getMainVehicle().setCurUpSpeed(0);
                    s.getMainVehicle().setCurSpeed(0);
                    s.getMainVehicle().getMainStand().setVelocity(new Vector(0, 0,0));
                    ((Player) event.getEntity()).setAllowFlight(Boolean.parseBoolean((String) ((DedicatedServer) MinecraftServer.getServer()).propertyManager.properties.get("allow-flight")));
                    if(s.getMainVehicle().getType() == VehicleType.HELICOPTER) {
                        s.getMainVehicle().hideWieken();
                    }
                }
            }
        }
    }
    public static int getDistance(Entity e) {
        Location loc = e.getLocation().clone();
        double y = loc.getBlockY();
        int distance = 0;
        for (double i = y; i >= 0; i--) {
            loc.setY(i);
            if (loc.getBlock().getType().isSolid()) break;
            distance++;
        }
        return distance;
    }
}
