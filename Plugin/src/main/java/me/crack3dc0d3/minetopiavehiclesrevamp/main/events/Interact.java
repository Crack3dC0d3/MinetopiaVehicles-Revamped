package me.crack3dc0d3.minetopiavehiclesrevamp.main.events;

import com.comphenix.protocol.PacketType;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.Main;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.enums.VehicleType;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Seat;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Vehicle;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleManager;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.ItemFactory;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.Methods;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.TrunkUtil;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.enums.Messages;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.inventories.ClickAction;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.inventories.CustomHolder;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.inventories.Icon;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

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
                        event.setCancelled(true);
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
                    if (Main.getSettings().getConfig().getBoolean("enable-achterbak")) {
                        CustomHolder holder = new CustomHolder(9, "Voertuig menu");
                        holder.setIcon(2, new Icon(
                                new ItemFactory(Material.BARRIER)
                                .setName("&6Oppakken")
                                .toItemStack())
                        .addClickAction(player -> {
                            player.closeInventory();
                            v.despawn(player);
                        }));
                        holder.setIcon(6, new Icon(
                                new ItemFactory(Material.CHEST)
                                .setName("&6Achterbak")
                                .toItemStack())
                        .addClickAction(player -> TrunkUtil.openTrunk(v, player)));
                        event.getPlayer().openInventory(holder.getInventory());
                        return;
                    } else {
                        v.despawn(event.getPlayer());
                        return;
                    }
                }
                if(!v.getMainSeat().getSeatStand().getPassengers().isEmpty()) {
                    for(Seat seat : v.getSeats()) {
                        if(event.getRightClicked() == seat.getSeatStand()) {
                            if(!event.getRightClicked().getPassengers().isEmpty() && !event.getPlayer().isInsideVehicle()) {
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
                if (!s.getSeatStand().getPassengers().isEmpty() && !event.getPlayer().isInsideVehicle()) {
                    Messages.send(event.getPlayer(), Messages.SEAT_FULL);
                } else if (v.getOwner() == event.getPlayer() || v.getRiders().contains(event.getPlayer().getUniqueId()) || event.getPlayer().hasPermission("minetopiavehicles.staff.overrideowner")) {
                    s.getSeatStand().addPassenger(event.getPlayer());
                    event.getPlayer().setAllowFlight(true);
                    if (v.getType() == VehicleType.HELICOPTER) {
                        v.showWieken();
                    }
                    if (!v.getName().contains("FIETS")) {
                        Methods.updateBar(event.getPlayer(), v.getFuelLevel() <= 10 ? BarColor.RED : v.getFuelLevel() <= 75 ? BarColor.YELLOW : BarColor.GREEN, "Brandstof: " + v.getFuelLevel() + "%", BarStyle.SOLID, v.getFuelLevel() / 100f, true);
                    }
                } else {
                    Messages.send(event.getPlayer(), Messages.NO_PERMISSION);
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPunch(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player && event.getEntity() instanceof ArmorStand) {
            if(event.getEntity().getCustomName() != null && event.getEntity().getCustomName().startsWith("MINETOPIAVEHICLES_")) {
                Player p = (Player) event.getDamager();
                ItemStack item = p.getInventory().getItemInMainHand();
                if (item.getType() == Material.DIAMOND_HOE && item.getDurability() == 59) {
                    p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                }
                String[] vehiclename = event.getEntity().getCustomName().split("_");
                Vehicle v = VehicleManager.getVehicleByPlate(vehiclename[2]);
                v.setFuelLevel(100);
                Methods.updateBar(p, v.getFuelLevel() <= 10 ? BarColor.RED : v.getFuelLevel() <= 75 ? BarColor.YELLOW : BarColor.GREEN, "Brandstof: " + v.getFuelLevel() + "%", BarStyle.SOLID, v.getFuelLevel() / 100f, true);
            }
        }
    }


}
