package me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle;

import com.google.gson.annotations.Expose;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.Main;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.enums.VehicleType;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.ItemFactory;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Vehicle {

    private int id;
    @Expose
    private Seat[] seats;
    @Expose
    private String licensePlate, name;
    private Seat mainSeat;
    private ArmorStand mainStand;
    private ArmorStand skinStand, bladeStand;
    private double curSpeed = 0;
    private double curUpSpeed = 0;
    @Expose
    private double traction;
    @Expose
    private double baseSpeed;
    @Expose
    private List<UUID> members, riders;
    private ItemStack skinItem;
    @Expose
    private VehicleType type;
    @Expose
    private double upSpeed, downSpeed;
    @Expose
    private double maxUpSpeed, maxDownSpeed;
    @Expose
    private boolean spawned;
    @Expose
    private UUID owneruuid;
    private OfflinePlayer owner;

    public Vehicle(@NotNull VehicleBase base, String licensePlate, @NotNull OfflinePlayer owner) {
        this.traction = base.getTraction();
        this.licensePlate = licensePlate;
        this.baseSpeed = base.getBaseSpeed();
        this.name = base.getName();
        this.skinItem = base.getSkinItem();
        this.owner = owner;
        owneruuid = owner.getUniqueId();

        List<Seat> seatList = new ArrayList<>();
        seatList.add(new Seat(this, base.getMainSeatOffset(), true));
        for (Location seatOffset: base.getSeatOffsets()
             ) {
            seatList.add(new Seat(this, seatOffset, false));
        }
        this.seats = seatList.toArray(new Seat[0]);

        riders = new ArrayList<>();
        members = new ArrayList<>();
        this.type = base.getType();

        if(base.getType() == VehicleType.HELICOPTER) {
            maxUpSpeed = base.getMaxUpSpeed();
            upSpeed = base.getUpSpeed();
            maxDownSpeed = base.getMaxDownSpeed();
            downSpeed = base.getDownSpeed();
        }

        spawned = false;
        VehicleManager.addVehicle(this);
        Main.getDatabaseUtil().saveVehicle(this);
    }

    public void spawn(Location spawnLoc) {
        Location ploc = spawnLoc.add(0, 1, 0);
        ArmorStand vehicle = (ArmorStand) ploc.getWorld().spawn(ploc, ArmorStand.class);
        //vehicle.setVisible(false);
        vehicle.setInvulnerable(true);
        vehicle.setCustomName("MINETOPIAVEHICLES_VEHICLE_" + licensePlate);
        vehicle.setCustomNameVisible(false);
        vehicle.setCollidable(false);
        vehicle.setGravity(true);
        vehicle.setVisible(false);
        vehicle.setCustomNameVisible(true);

        this.mainStand = vehicle;

        for(Seat s : seats) {
            s.spawn();
        }

        skinStand = mainStand.getWorld().spawn(mainStand.getLocation(), ArmorStand.class);
//        if(glow) {
//            ItemMeta im = skinItem.getItemMeta();
//            im.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
//            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
//            skinItem.setItemMeta(im);
//        }
        skinStand.setHelmet(skinItem);
        skinStand.setVisible(false);
        skinStand.setInvulnerable(true);
        skinStand.setCustomName("MINETOPIAVEHICLES_SKIN_" + licensePlate);
        skinStand.setGravity(false);
        skinStand.setCustomNameVisible(true);

        if(type == VehicleType.HELICOPTER) {
            this.bladeStand = mainStand.getWorld().spawn(mainStand.getLocation().add(0, -0.15, -0.8), ArmorStand.class);
            bladeStand.setGravity(false);
            bladeStand.setVisible(false);
            bladeStand.setCustomName("MINETOPIAVEHICLES_WIEKEN_" + licensePlate);
            bladeStand.setCustomNameVisible(true);



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
                    loc.setYaw(bladeStand.getLocation().getYaw() + 10F);

                    Methods.setPosition(bladeStand, loc);
                }
            }.runTaskTimer(Main.getInstance(), 0L, 1L);

        }
        Main.getDatabaseUtil().saveVehicle(this);
        spawned = true;
    }

    public void despawn(Player player) {
        mainStand.remove();
        ItemStack skin = skinStand.getHelmet();
        skinStand.remove();
        setCurSpeed(0);
        setCurUpSpeed(0);
        if(type == VehicleType.HELICOPTER) {
            bladeStand.remove();
        }
        for(Seat s : seats) {
            s.getSeatStand().remove();
        }
        spawned = false;
        Main.getDatabaseUtil().saveVehicle(this);
        player.getInventory().addItem(skin);
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

    public void setOwner(OfflinePlayer owner) {
        this.owner = owner;
        this.owneruuid = owner.getUniqueId();
        Main.getDatabaseUtil().saveVehicle(this);
    }

    public void setSkinItem(ItemStack skinItem) {
        this.skinItem = skinItem;
    }

    public double getMaxDownSpeed() {
        return maxDownSpeed;
    }

    public Seat getMainSeat() {

        if(mainSeat == null) {
            for (Seat s: seats
                 ) {
                if(s.isMain()) {
                    mainSeat = s;
                    break;
                }
            }
        }
        return mainSeat;
    }

    public ArmorStand getMainStand() {
        return mainStand;
    }

    public void addRider(OfflinePlayer rider) {
        riders.add(rider.getUniqueId());
        Main.getDatabaseUtil().saveVehicle(this);
    }

    public void addMember(OfflinePlayer member) {
        members.add(member.getUniqueId());
        Main.getDatabaseUtil().saveVehicle(this);
    }

    public void removeRider(OfflinePlayer rider) {
        riders.remove(rider.getUniqueId());
        Main.getDatabaseUtil().saveVehicle(this);
    }

    public void removeMember(OfflinePlayer member) {
        members.remove(member.getUniqueId());
        Main.getDatabaseUtil().saveVehicle(this);
    }

    public List<UUID> getMembers() {
        return members;
    }

    public List<UUID> getRiders() {
        return riders;
    }

    public ItemStack getSkinItem() {
        return skinItem;
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

    public void setMainStand(ArmorStand mainStand) {
        this.mainStand = mainStand;
    }

    public void setSkinStand(ArmorStand skinStand) {
        this.skinStand = skinStand;
    }

    public void setWiekStand(ArmorStand bladeStand) {
        this.bladeStand = bladeStand;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public OfflinePlayer getOwner() {
        if(owner == null) {
            owner = Bukkit.getOfflinePlayer(owneruuid);
        }
        return owner;
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
            bladeStand.remove();
        }
        VehicleManager.removeVehicle(this);
    }




    public void updatePositions() {
        for (Seat seat: seats
             ) {
            Location locvp = mainStand.getLocation().clone();
            Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(seat.getOffsetLoc().getX()));
            float zvp = (float) (fbvp.getZ() + (seat.getOffsetLoc().getZ() * Math.sin(Math.toRadians(fbvp.getYaw()))));
            float xvp = (float) (fbvp.getX() + (seat.getOffsetLoc().getZ() * Math.cos(Math.toRadians(fbvp.getYaw()))));
            Location loc = new Location(seat.getMainVehicle().getMainStand().getWorld(),
                    xvp,
                    mainStand.getLocation().getY() + seat.getOffsetLoc().getY(),
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

            locnew.setYaw(bladeStand.getLocation().getYaw());
            Methods.setPosition(bladeStand, locnew);
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
//            if (this.glow) {
//                wiekStand.setHelmet(new ItemFactory(Material.DIAMOND_HOE).setDurability((short) 1058).unbreakable().addEnchant(Enchantment.ARROW_INFINITE, 1).toItemStack());
//            } else {
//                wiekStand.setHelmet(new ItemFactory(Material.DIAMOND_HOE).setDurability((short) 1058).unbreakable().toItemStack());
//            }
            bladeStand.setHelmet(new ItemFactory(Material.DIAMOND_HOE).setDurability((short) 1058).unbreakable().toItemStack());
        }
    }

    public void hideWieken() {
        if(type == VehicleType.HELICOPTER) {
            bladeStand.setHelmet(new ItemStack(Material.AIR));
        }
    }
}
