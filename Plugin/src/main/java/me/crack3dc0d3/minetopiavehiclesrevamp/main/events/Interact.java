package me.crack3dc0d3.minetopiavehiclesrevamp.main.events;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.enums.VehicleType;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Seat;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Vehicle;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleManager;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.enums.Messages;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class Interact implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(event.getHand() == EquipmentSlot.HAND) {
                if (event.getItem() != null && event.getItem().getItemMeta() != null && event.getItem().getItemMeta().getLore() != null) {
//                    for (VehicleBase base : VehicleManager.getBaseVehicles()) {
//                        if (event.getItem().getType() == base.getSkinItem().getType()) {
//                            if (event.getItem().getDurability() == base.getSkinItem().getDurability()) {
//                                Vehicle v = new Vehicle(base, event.getItem().getItemMeta().getLore().get(1).replace("\u00A7a", ""));
//                                v.spawn(event.getClickedBlock().getLocation().add(0, 1, 0));
//                                VehicleManager.addVehicle(v);
//                            }
//                        }
//                    }
                    Vehicle v = VehicleManager.getVehicleByPlate(event.getItem().getItemMeta().getLore().get(1).replace("\u00A7a", ""));
                    if(v == null) {
                        return;
                    }
                    if(v.getOwner() == event.getPlayer() || event.getPlayer().hasPermission("minetopiavehicles.staff.overrideowner")) {
                        v.setSkinItem(event.getItem());
                        v.spawn(event.getClickedBlock().getLocation().add(0, 0.3, 0));
                        event.getPlayer().getInventory().remove(event.getItem());
                    } else {
                        Messages.send(event.getPlayer(), Messages.NO_PERMISSION);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent event) {
        if(event.getRightClicked().getType() == EntityType.ARMOR_STAND) {
            if(event.getRightClicked().getCustomName().startsWith("MINETOPIAVEHICLES")) {
                String[] strings  = event.getRightClicked().getCustomName().split("_");
                Vehicle v = VehicleManager.getVehicleByPlate(strings[strings.length-1]);
                // Pickup system
                if(event.getPlayer().isSneaking() && (v.getOwner() == event.getPlayer() || event.getPlayer().hasPermission("minetopiavehicles.staff.overrideowner")) ) {
                    v.despawn(event.getPlayer());
                    return;
                }
                if(!v.getMainSeat().getSeatStand().getPassengers().isEmpty()) {
                    for(Seat seat : v.getSeats()) {
                        if(event.getRightClicked() == seat.getSeatStand()) {
                            if(!event.getRightClicked().getPassengers().isEmpty()) {
                                Messages.send(event.getPlayer(), Messages.SEAT_FULL);
                                return;
                            }
                            if(v.getOwner() == event.getPlayer() || v.getRiders().contains(event.getPlayer().getUniqueId()) || v.getMembers().contains(event.getPlayer().getUniqueId()) || event.getPlayer().hasPermission("minetopiavehicles.staff.overrideowner")) {
                                seat.getSeatStand().addPassenger(event.getPlayer());
                                event.getPlayer().setAllowFlight(true);
                            } else {
                                Messages.send(event.getPlayer(), Messages.NO_PERMISSION);
                            }
                        }
//                        if(seat != v.getMainSeat() || seat.getSeatStand().getPassengers().isEmpty()) {
//                            seat.getSeatStand().addPassenger(event.getPlayer());
//                        }
                    }
                }
                Seat s = v.getMainSeat();
                if(!s.getSeatStand().getPassengers().isEmpty()) {
                    Messages.send(event.getPlayer(), Messages.SEAT_FULL);
                }
                if(v.getOwner() == event.getPlayer() || v.getRiders().contains(event.getPlayer().getUniqueId()) || event.getPlayer().hasPermission("minetopiavehicles.staff.overrideowner")) {
                    s.getSeatStand().addPassenger(event.getPlayer());
                    event.getPlayer().setAllowFlight(true);
                    if(v.getType() == VehicleType.HELICOPTER) {
                        v.showWieken();
                    }
                } else {
                    Messages.send(event.getPlayer(), Messages.NO_PERMISSION);
                }
                event.setCancelled(true);
            }
        }

    }

}
