package me.crack3dc0d3.minetopiavehiclesrevamp.main.events;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.Main;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Vehicle;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleManager;
import nl.minetopiasdb.api.events.player.PlayerLevelcheckEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LevelCheck implements Listener {

    @EventHandler
    public void onLevelCheck(PlayerLevelcheckEvent event) {
        if(Main.getSettings().getConfig().getBoolean("enable-levelcheck-vehicles")) {
            int vehicles = 0;

            for (Vehicle v : VehicleManager.getVehicles()) {
                if(v.getOwner().getUniqueId() == event.getPlayer().getUniqueId()) {
                    vehicles++;
                }
            }
            event.setVehicles(vehicles);
        }
    }

}
