package me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.Main;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.enums.VehicleType;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.ItemFactory;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.Methods;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Vehicle {

    private Seat[] seats;
    private String licensePlate, name;
    private Seat mainSeat;
    private ArmorStand mainStand;
    private ArmorStand skinStand, wiekStand;
    private double curSpeed = 0;
    private double curUpSpeed = 0;
    private double traction;
    private double baseSpeed;
    private List<UUID> members, riders;
    private ItemStack skinitem;
    private VehicleType type;
    private double upSpeed, downSpeed, maxUpSpeed;
    private boolean glow;

    public Vehicle(ArmorStand mainStand, double traction, String licensePlate, double baseSpeed, ItemStack skinItem, String name, VehicleType type, boolean glow) {
        this.mainStand = mainStand;
        this.traction = traction;
        this.licensePlate = licensePlate;
        this.baseSpeed = baseSpeed;
        this.name = name;

        skinStand = mainStand.getWorld().spawn(mainStand.getLocation(), ArmorStand.class);
        if(glow) {
            ItemMeta im = skinItem.getItemMeta();
            im.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            skinItem.setItemMeta(im);
        }
        skinStand.setHelmet(skinItem);
        skinStand.setVisible(false);
        skinStand.setInvulnerable(true);
        skinStand.setCustomName("MINETOPIAVEHICLES_SKIN_" + licensePlate);
        skinStand.setGravity(false);
        this.skinitem = skinItem;
        this.glow = glow;

        if(type == VehicleType.HELICOPTER) {

        	
            this.wiekStand = mainStand.getWorld().spawn(mainStand.getLocation().add(0, -0.15, -0.8), ArmorStand.class);
            wiekStand.setGravity(false);
            wiekStand.setVisible(false);
            wiekStand.setCustomName("MINETOPIAVEHICLES_WIEKEN_" + licensePlate);
            
            
            
            new BukkitRunnable() {
                @Override
                public void run() {
                        Location locvp = mainStand.getLocation().clone();
                        Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(-0.8));
                        float zvp = (float) (fbvp.getZ() + (0 * Math.sin(Math.toRadians(fbvp.getYaw()))));
                        float xvp = (float) (fbvp.getX() + (0* Math.cos(Math.toRadians(fbvp.getYaw()))));
                        Location loc = new Location(mainStand.getWorld(),
                                xvp,
                                mainStand.getLocation().add(0, -0.15, -0.8).getY(),
                                zvp,
                                fbvp.getYaw(),
                                fbvp.getPitch());
                        loc.setYaw(wiekStand.getLocation().getYaw() + 10F);

                        Methods.setPosition(wiekStand, loc);
                }
            }.runTaskTimer(Main.getInstance(), 0L, 1L);

        }


        riders = new ArrayList<>();
        members = new ArrayList<>();
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public VehicleType getType() {
        return type;
    }

    public ArmorStand getSkinStand() {
        return skinStand;
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    public double getSpeed() {
        return baseSpeed / 20D;
    }

    public double getOptrekSpeed() {
        return traction;
    }

    public void setSeats(Seat[] seats) {
        this.seats = seats;
    }

    public void setMainSeat(Seat mainSeat) {
        this.mainSeat = mainSeat;
    }

    public Seat[] getSeats() {
        return seats;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public Seat getMainSeat() {
        return mainSeat;
    }

    public ArmorStand getMainStand() {
        return mainStand;
    }

    public void addRider(Player rider) {
        riders.add(rider.getUniqueId());
    }

    public void addMember(Player member) {
        members.add(member.getUniqueId());
    }

    public List<UUID> getMembers() {
        return members;
    }

    public List<UUID> getRiders() {
        return riders;
    }

    public ItemStack getSkinitem() {
        return skinitem;
    }

    public double getMaxUpSpeed() {
        return maxUpSpeed;
    }

    public void setMaxUpSpeed(double maxUpSpeed) {
        this.maxUpSpeed = maxUpSpeed;
    }

    public double getCurUpSpeed() {
        return curUpSpeed;
    }

    public void setCurUpSpeed(double curUpSpeed) {
        this.curUpSpeed = curUpSpeed;
    }

    public void setUpSpeed(double upSpeed) {
        this.upSpeed = upSpeed;
    }

    public void setDownSpeed(double downSpeed) {
        this.downSpeed = downSpeed;
    }

    public double getUpSpeed() {
        return upSpeed;
    }

    public double getDownSpeed() {
        return downSpeed;
    }
    
    public void remove() {
        mainStand.remove();
        skinStand.remove();
        for(Seat s : seats) {
            s.getSeatStand().remove();
        }
        if(type == VehicleType.HELICOPTER) {
            wiekStand.remove();
        }
        //DataUtils.removeVehicleFromTemp(this);
        VehicleManager.removeVehicle(this);
    }

    public void removeNoTemp() {

        mainStand.remove();
        skinStand.remove();
        for(Seat s : seats) {
            for(Entity e : s.getSeatStand().getPassengers()) {
                s.getSeatStand().removePassenger(e);
            }
            s.getSeatStand().remove();
        }
        if(type == VehicleType.HELICOPTER) {
            wiekStand.remove();
        }
        VehicleManager.removeVehicle(this);
    }




    public void updatePositions() {
        for (Seat seat: seats
             ) {
            Location locvp = mainStand.getLocation().clone();
            Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(seat.getOffset().getX()));
            float zvp = (float) (fbvp.getZ() + (seat.getOffset().getZ() * Math.sin(Math.toRadians(fbvp.getYaw()))));
            float xvp = (float) (fbvp.getX() + (seat.getOffset().getZ() * Math.cos(Math.toRadians(fbvp.getYaw()))));
            Location loc = new Location(seat.getMainStand().getWorld(),
                    xvp,
                    mainStand.getLocation().getY() + seat.getOffset().getY(),
                    zvp,
                    fbvp.getYaw(),
                    fbvp.getPitch());

                    Methods.setPosition(seat.getSeatStand(), loc);
        }
        Location loc = mainStand.getLocation().clone();
        Methods.setPosition(skinStand, loc);
        if(type == VehicleType.HELICOPTER){
            Location locvp = mainStand.getLocation().clone();
            Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(-0.8));
            float zvp = (float) (fbvp.getZ() + (0 * Math.sin(Math.toRadians(fbvp.getYaw()))));
            float xvp = (float) (fbvp.getX() + (0* Math.cos(Math.toRadians(fbvp.getYaw()))));
            Location locnew = new Location(mainStand.getWorld(),
                    xvp,
                    mainStand.getLocation().add(0, -0.15, -0.8).getY(),
                    zvp,
                    fbvp.getYaw(),
                    fbvp.getPitch());

            locnew.setYaw(wiekStand.getLocation().getYaw());
            Methods.setPosition(wiekStand, locnew);
        }
    }

    public double getCurSpeed() {
        return curSpeed;
    }

    public void setCurSpeed(double curSpeed) {
        this.curSpeed = curSpeed;
    }

    public void showWieken() {
        if(type == VehicleType.HELICOPTER) {
            if (this.glow) {
                wiekStand.setHelmet(new ItemFactory(Material.DIAMOND_HOE).setDurability((short) 1058).unbreakable().addEnchant(Enchantment.ARROW_INFINITE, 1).toItemStack());
            } else {
                wiekStand.setHelmet(new ItemFactory(Material.DIAMOND_HOE).setDurability((short) 1058).unbreakable().toItemStack());
            }
        }
    }
    public void hideWieken() {
        if(type == VehicleType.HELICOPTER) {
            wiekStand.setHelmet(new ItemStack(Material.AIR));
        }
    }

}
