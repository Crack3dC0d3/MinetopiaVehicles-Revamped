package me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle;

import com.google.gson.annotations.Expose;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Seat {

    public static List<Seat> seats = new ArrayList<>();
    @Expose
    private String offset;
    private Location offsetLoc;
    private Vehicle mainVehicle;
    private ArmorStand seat;
    @Expose
    private boolean main;
    @Expose
    private String plate;

    public Seat(Vehicle mainVehicle, Location offset, boolean isMain) {
        this.mainVehicle = mainVehicle;
        this.offsetLoc = offset;
        this.offset = getStringLocation(offset);
        this.main = isMain;
        this.plate = mainVehicle.getLicensePlate();
        seats.add(this);
    }

    /**
     * Spawns the seat and assigns the armorstand variables
     */
    public void spawn() {
        this.offsetLoc.setWorld(mainVehicle.getMainStand().getWorld());
        ArmorStand stand = mainVehicle.getMainStand().getWorld().spawn(mainVehicle.getMainStand().getLocation().add(offsetLoc), ArmorStand.class);
        //stand.setVisible(false);
        stand.setInvulnerable(true);
        stand.setCustomNameVisible(false);
        stand.setCollidable(false);
        stand.setGravity(false);
        stand.setVisible(false);
        if(this.getMainVehicle().getMainSeat() == this) {
            stand.setCustomName("MINETOPIAVEHICLES_MAINSEAT_" + mainVehicle.getLicensePlate());
        } else {
            stand.setCustomName("MINETOPIAVEHICLES_SEAT_" + mainVehicle.getLicensePlate());
        }
        seat = stand;
    }

    /**
     * Gets a {@link Seat} object with the given armorstand. If none was fount returns null
     * @param stand The armorstand to lookup
     * @return Seat object, Null if none found
     */
    @Nullable
    public static Seat getSeat(ArmorStand stand) {
        for(Seat seat : seats) {
            if(seat.seat == stand) {
                return seat;
            }
        }
        return null;
    }

    /**
     * Gets the vehicle assigned to this seat
     * @return The vehicle assigned to this seat
     */
    public Vehicle getMainVehicle() {
        return mainVehicle;
    }

    /**
     * Gets the offset from the main armorstand
     * @return The offset from the main armorstand
     */
    public Location getOffsetLoc() {
        return offsetLoc;
    }

    /**
     * Gets the armorstand this seat is assigned to
     * @return The armorstand that this seat is assigned to
     */
    public ArmorStand getSeatStand() {
        return seat;
    }

    /**
     * Check whether the seat is the drivers seat or not
     * @return true if its the drivers seat, False if not
     */
    public boolean isMain() {
        return main;
    }

    /**
     * Sets the main vehicle (Used for datastorage purposes)
     * @param mainVehicle The vehicle to set the main to.
     */
    public void setMainVehicle(Vehicle mainVehicle) {
        this.mainVehicle = mainVehicle;
    }

    public void setSeat(ArmorStand seat) {
        this.seat = seat;
    }

    public static void addSeat(Seat s) {
        seats.add(s);
    }

    public void updateOffset() {
        offsetLoc = getLocationString(offset);
    }

    public String getStringLocation(final Location l) {
        if (l == null) {
            return "";
        }
        return "fietsbel:" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
    }

    public Location getLocationString(final String s) {
        if (s == null || s.trim().equals("")) {
            return null;
        }
        final String[] parts = s.split(":");
        if (parts.length == 4) {
            final World w = null;
            final int x = Integer.parseInt(parts[1]);
            final int y = Integer.parseInt(parts[2]);
            final int z = Integer.parseInt(parts[3]);
            return new Location(w, x, y, z);
        }
        return null;
    }

    public String getPlate() {
        return plate;
    }
}
