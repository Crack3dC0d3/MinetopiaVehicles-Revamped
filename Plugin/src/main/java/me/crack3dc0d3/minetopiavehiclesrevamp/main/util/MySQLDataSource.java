package me.crack3dc0d3.minetopiavehiclesrevamp.main.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.Main;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Seat;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Vehicle;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLDataSource implements IDataSource {

    private HikariDataSource source;
    @Override
    public void init() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setUsername(Main.getSettings().getConfig().getString("mysql.username"));
        config.setPassword(Main.getSettings().getConfig().getString("mysql.password"));
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s", Main.getSettings().getConfig().getString("mysql.host"), Main.getSettings().getConfig().getString("mysql.port"), Main.getSettings().getConfig().getString("mysql.database")));
        source = new HikariDataSource(config);

        try(Connection con = source.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS vehicles (plate VARCHAR(20) NOT NULL UNIQUE PRIMARY KEY, vehicle LONGTEXT NOT NULL)"
            );
            stmt.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Nullable
    @Override
    public List<Vehicle> getVehicles() {
        try(Connection con = source.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT * FROM vehicles"
            );
            ResultSet set = stmt.executeQuery();
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            List<Vehicle> vehicles = new ArrayList<>();
            while (set.next()) {
                Vehicle v = gson.fromJson(set.getString(2), Vehicle.class);
                for (Seat s : v.getSeats()
                ) {
                    s.setMainVehicle(v);
                }
                vehicles.add(v);
            }
            return vehicles;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveVehicle(Vehicle vehicle) {
        try(Connection con = source.getConnection()) {
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            String vehiclejson = gson.toJson(vehicle);
            try {
                String query = "INSERT INTO vehicles (plate, vehicle) VALUES (?, ?);";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1, vehicle.getLicensePlate());
                statement.setString(2, vehiclejson);
                statement.executeUpdate();
            } catch (SQLException ignored) {}

            try {
                String query2 = "UPDATE vehicles SET vehicle = ? WHERE plate = ?";
                PreparedStatement statement2 = con.prepareStatement(query2);
                statement2.setString(1, vehiclejson);
                statement2.setString(2, vehicle.getLicensePlate());
                statement2.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
