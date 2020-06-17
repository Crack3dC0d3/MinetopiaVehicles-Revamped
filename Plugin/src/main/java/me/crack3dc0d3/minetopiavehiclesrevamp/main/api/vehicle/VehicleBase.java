package me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.enums.VehicleType;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class VehicleBase {

    private Location mainSeatOffset;

    private Location[] seatOffsets;

    private String name, displayname;

    private int baseSpeed;
    private double traction;

    private ItemStack skinItem;

    private VehicleType type;

    public VehicleBase(Location mainSeatOffset, Location[] seatOffsets, String name, String displayname, int baseSpeed, double traction, ItemStack skinItem, VehicleType type) {
        this.mainSeatOffset = mainSeatOffset;
        this.seatOffsets = seatOffsets;
        this.name = name;
        this.displayname = displayname;
        this.baseSpeed = baseSpeed;
        this.traction = traction;
        this.skinItem = skinItem;
        this.type = type;
    }

    public Location getMainSeatOffset() {
        return mainSeatOffset;
    }

    public Location[] getSeatOffsets() {
        return seatOffsets;
    }

    public String getName() {
        return name;
    }

    public String getDisplayname() {
        return displayname;
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public double getTraction() {
        return traction;
    }

    public ItemStack getSkinItem() {
        return skinItem;
    }

    public VehicleType getType() {
        return type;
    }

    public static VehicleBase fromYML(FileConfiguration config) {
        List<Location> seatOffsets = new ArrayList<>();
        Location mainSeatOffset = null;
        for(String s : config.getConfigurationSection("seatOffsets").getKeys(false)) {
            if(s.equals(config.getString("mainSeat"))) {
                mainSeatOffset = new Location(null, config.getInt("seatOffsets." + s + ".x"), config.getInt("seatOffsets." + s + ".y"), config.getInt("seatOffsets." + s + ".z"));
            }
            seatOffsets.add(new Location(null, config.getInt("seatOffsets." + s + ".x"), config.getInt("seatOffsets." + s + ".y"), config.getInt("seatOffsets." + s + ".z")));
        }
        return new VehicleBase(
                mainSeatOffset,
                seatOffsets.toArray(new Location[0]),
                config.getString("name"),
                config.getString("displayname"),
                config.getInt("baseSpeed"),
                config.getDouble("traction"),
                config.getItemStack("skin"),
                VehicleType.valueOf(config.getString("type")));
    }

}
