package me.crack3dc0d3.minetopiavehiclesrevamp.main.events;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Seat;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Vehicle;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleBase;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleManager;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class Interact implements Listener {

    private int fire = 0;
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(event.getHand() == EquipmentSlot.HAND) {
                if (event.getItem() != null) {
                    for (VehicleBase base : VehicleManager.getBaseVehicles()) {
                        if (event.getItem().getType() == base.getSkinItem().getType()) {
                            if (event.getItem().getDurability() == base.getSkinItem().getDurability()) {
                                Vehicle v = new Vehicle(base, event.getItem().getItemMeta().getLore().get(1).replace("\u00A7a", ""));
                                v.spawn(event.getClickedBlock().getLocation().add(0, 1, 0));
                                VehicleManager.addVehicle(v);
                            }
                        }
                    }
                }
            }
        }
        fire++;
        Bukkit.broadcastMessage(fire + "");
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent event) {
        if(event.getRightClicked().getType() == EntityType.ARMOR_STAND) {
            if(event.getRightClicked().getCustomName().startsWith("MINETOPIAVEHICLES")) {
                String[] strings  = event.getRightClicked().getCustomName().split("_");
                Vehicle v = VehicleManager.getVehicleByPlate(strings[strings.length-1]);
                Seat s = v.getMainSeat();
                if(!v.getMainSeat().getSeatStand().getPassengers().isEmpty()) {
                    for(Seat seat : v.getSeats()) {
                        if(seat != v.getMainSeat() || seat.getSeatStand().getPassengers().isEmpty()) {
                            seat.getSeatStand().addPassenger(event.getPlayer());
                        }
                    }
                }
                if(!s.getSeatStand().getPassengers().isEmpty()) {
                    Messages.send(event.getPlayer(), Messages.SEAT_FULL);
                }
                s.getSeatStand().addPassenger(event.getPlayer());
            }
        }

    }

}
