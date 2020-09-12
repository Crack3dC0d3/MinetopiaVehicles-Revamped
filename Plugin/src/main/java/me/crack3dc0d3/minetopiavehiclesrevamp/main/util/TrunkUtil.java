package me.crack3dc0d3.minetopiavehiclesrevamp.main.util;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.Main;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Vehicle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.HashMap;

public class TrunkUtil implements Listener {

    private static HashMap<Player, Vehicle> openTrunks = new HashMap<>();

    public static void openTrunk(Vehicle vehicle, Player p) {
        Inventory trunk = vehicle.getTrunk();
        if(trunk == null) {
            if(vehicle.getTrunkString() == null) {
                trunk = Bukkit.createInventory(null, 27);
                p.openInventory(trunk);
                openTrunks.put(p, vehicle);
            }
            try {
                trunk = InventorySerializer.fromBase64(vehicle.getTrunkString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            p.openInventory(trunk);
            openTrunks.put(p, vehicle);
        }
        p.openInventory(trunk);
        openTrunks.put(p, vehicle);
    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(openTrunks.containsKey((Player) event.getPlayer())) {
            Vehicle vehicle = openTrunks.get((Player) event.getPlayer());
            Inventory trunk = event.getInventory();
            vehicle.setTrunk(trunk);
            vehicle.setTrunkString(InventorySerializer.toBase64(trunk));
            Main.getDatabaseUtil().saveVehicle(vehicle);
            openTrunks.remove((Player) event.getPlayer());
        }
    }

}
