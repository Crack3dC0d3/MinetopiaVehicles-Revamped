package me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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

}
