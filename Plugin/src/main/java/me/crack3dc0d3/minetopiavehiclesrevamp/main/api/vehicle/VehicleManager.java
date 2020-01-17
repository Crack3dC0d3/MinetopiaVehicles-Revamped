package me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VehicleManager {
    private static List<Vehicle> vehicles = new ArrayList<>();

    public static List<Vehicle> getVehicles() {
        return vehicles;
    }

    public static void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public static void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
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

}
