package me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle;

import com.sun.istack.internal.Nullable;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class Seat {

    public static List<Seat> seats = new ArrayList<>();

    private ArmorStand mainStand;
    private Location offset;
    private Vehicle mainVehicle;
    private ArmorStand seat;
    private static Seat thisSeat;

    public Seat(Vehicle mainVehicle, Location offset) {
        this.mainVehicle = mainVehicle;
        this.offset = offset;
        seats.add(this);
        this.mainStand = mainVehicle.getMainStand();

    }



    public void spawn() {

        Entity armorstand = mainStand.getWorld().spawn(mainStand.getLocation().add(offset), ArmorStand.class);
        ArmorStand stand = (ArmorStand) armorstand;
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


    @Nullable
    public static Seat getSeat(ArmorStand stand) {
        for(Seat seat : seats) {
            if(seat.seat == stand) {
                return seat;
            }
        }
        return null;
    }

    public Vehicle getMainVehicle() {
        return mainVehicle;
    }

    public ArmorStand getMainStand() {
        return mainStand;
    }

    public Location getOffset() {
        return offset;
    }

    public ArmorStand getSeatStand() {
        return seat;
    }
}
