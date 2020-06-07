package me.crack3dc0d3.minetopiavehiclesrevamp.main.events;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Vehicle;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleBase;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Interact implements Listener {


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            for(VehicleBase base : VehicleManager.getBaseVehicles()) {
                if(event.getItem().getType() == base.getSkinItem().getType()) {
                    if(event.getItem().getDurability() == base.getSkinItem().getDurability()) {
                        Vehicle v = new Vehicle(base, event.getItem().getItemMeta().getLore().get(1).replace("\uA007a", ""));
                        v.spawn(event.getClickedBlock().getLocation().add(0, 1, 0));
                        VehicleManager.addVehicle(v);
                    }
                }
            }
        }
    }

}
