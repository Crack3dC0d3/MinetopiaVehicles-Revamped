package me.crack3dc0d3.minetopiavehiclesrevamp.main.util;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Vehicle;

import javax.annotation.Nullable;
import java.util.List;

public interface IDataSource {

    /**
     * Initializes the datasource. Ex. Setup Hikari to work for SQL
     */
    void init();

    /**
     * Requests the vehicles from the datasource
     * @return {@link java.util.ArrayList<Vehicle>} with all of the vehicles stored in the table. Null if none stored
     */
    List<Vehicle> getVehicles();


    /**
     * Saves the given vehicle into the datasource
     * @param vehicle The vehicle that should be saved
     */
    void saveVehicle(Vehicle vehicle);

}
