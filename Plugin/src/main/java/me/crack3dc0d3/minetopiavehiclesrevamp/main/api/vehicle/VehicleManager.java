package me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.Methods;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class VehicleManager {
    private static List<Vehicle> vehicles = new ArrayList<>();

    private static List<VehicleBase> baseVehicles = new ArrayList<>();

    public static List<Vehicle> getVehicles() {
        return vehicles;
    }

    public static void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public static void addVehicle(Collection<Vehicle> vehicle) {vehicles.addAll(vehicle);}

    public static void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
    }

    public static void addBase(VehicleBase base) {
        baseVehicles.add(base);
    }

    @Nullable
    public static Vehicle getVehicleByPlate(String plate) {
        for(Vehicle v : vehicles) {
            if(v.getLicensePlate().equals(plate)) {
                return v;
            }
        }
        return null;
    }

    @Nullable
    public static VehicleBase getVehicleBaseByName(String name) {
        for(VehicleBase base : baseVehicles) {
            if(base.getName().equals(name)) {
                return base;
            }
        }
        return null;
    }

    public static void giveVehicle(VehicleBase vehicle, OfflinePlayer owner) {
        String plate = Methods.generateLicencePlate();
        Vehicle vehicleObj = new Vehicle(vehicle, plate, owner);
        Player target = owner.getPlayer();
        ItemStack toGive = vehicle.getSkinItem();
        ItemMeta im = toGive.getItemMeta();
        im.setDisplayName(vehicle.getDisplayname());
        im.setLore(Arrays.asList(" ", ChatColor.GREEN + plate));
        toGive.setItemMeta(im);
        target.getInventory().addItem(toGive);
        vehicleObj.setSkinItem(toGive);
    }

    public static List<VehicleBase> getBaseVehicles() {
        return baseVehicles;
    }
}
