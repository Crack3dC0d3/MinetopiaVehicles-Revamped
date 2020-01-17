package me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.Main;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.enums.VehicleType;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.InputManager;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.Methods;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.enums.Messages;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class VehicleMover {
    float yaw = 0;
    int w = 0;
    boolean w2 = false;
    double w3 = 0;



    public static Vector addRightVelocity(ArmorStand as, int direction, float vectorSize) {
        Vector vel = as.getVelocity().clone();
        Vector newVel = vel.clone();

        if (direction == 1) {
        newVel.setX(-vel.getZ());
        newVel.setZ(vel.getX());
        } else {
        newVel.setX(vel.getZ());
        newVel.setZ(-vel.getX());
        }

        newVel = newVel.normalize().multiply(vectorSize);
        return newVel;
        }




    public static void doMovement(InputManager manager, Player p) {

        boolean w, a, s, d, space;

        w = manager.isW();
        a = manager.isA();
        s = manager.isS();
        d = manager.isD();
        space = manager.isSpace();


        int rotspeed = 6;
        Vehicle vehicle = null;
        for (Vehicle vehicle1 : VehicleManager.getVehicles()) {
            if(Seat.getSeat((ArmorStand) p.getVehicle()).getMainVehicle() == null) {
                return;
            }
            if (Seat.getSeat((ArmorStand) p.getVehicle()).getMainVehicle() == vehicle1) {
                vehicle = vehicle1;
            }
        }

        if (vehicle == null) {
            return;
        }

        if(!w && !s && !a && !d && !space) {
            vehicle.getMainStand().setVelocity(new Vector(0, 0, 0));
        }



        if (w) {
            if (vehicle.getCurSpeed() < vehicle.getSpeed()) {
                if (vehicle.getCurSpeed() < 0) {
                    vehicle.setCurSpeed(vehicle.getCurSpeed() + (vehicle.getOptrekSpeed() + 0.02D));
                } else {
                    vehicle.setCurSpeed(vehicle.getCurSpeed() + vehicle.getOptrekSpeed());
                }
            } else {
                vehicle.setCurSpeed(vehicle.getSpeed());
            }
        } else if (s) {
            if (vehicle.getCurSpeed() > -(vehicle.getSpeed() / 4D)) {
                if (vehicle.getCurSpeed() > 0) {
                    vehicle.setCurSpeed(vehicle.getCurSpeed() - (vehicle.getOptrekSpeed() + 0.02D));
                } else {
                    vehicle.setCurSpeed(vehicle.getCurSpeed() - vehicle.getOptrekSpeed());
                }
            } else {
                vehicle.setCurSpeed(-(vehicle.getSpeed() / 4D));
            }
        } else {
            double kloenk = Main    .getInstance().getConfig().getDouble("kloenksnelheid");
            if(vehicle.getCurSpeed() < 0) {
                vehicle.setCurSpeed(vehicle.getCurSpeed() + kloenk);
            } else if(vehicle.getCurSpeed() > 0){
                vehicle.setCurSpeed(vehicle.getCurSpeed() - kloenk);
            } else {
                vehicle.setCurSpeed(0);
            }
        }
        if(vehicle.getType() == VehicleType.HELICOPTER) {
            if (space) {
                if (vehicle.getCurUpSpeed() < vehicle.getMaxUpSpeed()) {
                    if (vehicle.getCurUpSpeed() < 0) {
                        vehicle.setCurUpSpeed(vehicle.getCurUpSpeed() + (vehicle.getUpSpeed() + 0.1D));
                    } else {
                        vehicle.setCurUpSpeed(vehicle.getCurUpSpeed() + vehicle.getUpSpeed());
                    }
                } else {
                    vehicle.setCurUpSpeed(vehicle.getMaxUpSpeed());
                }
            }
            if(!space && !vehicle.getMainStand().isOnGround()) {
                if (vehicle.getCurUpSpeed() - 0.05D < -(vehicle.getMaxUpSpeed())) {
                    vehicle.setCurUpSpeed(-(vehicle.getMaxUpSpeed()));
                }
                if(vehicle.getCurUpSpeed() > -0.45D) {
                    vehicle.setCurUpSpeed(vehicle.getCurUpSpeed() - 0.005);
                }

            }
            if(vehicle.getMainStand().isOnGround()) {
                vehicle.setCurUpSpeed(0);
            }
        }
        Vector toAdd = new Vector();
        if (a) {
            Location loc = vehicle.getMainStand().getLocation().clone();
            if (vehicle.getCurSpeed() > 0) {
                loc.setYaw((float) (loc.getYaw() - rotspeed));
                toAdd = addRightVelocity(vehicle.getMainStand(), -1, (float) vehicle.getCurSpeed() / 20f);

            } else if (vehicle.getCurSpeed() < 0) {
                loc.setYaw((float) (loc.getYaw() +  rotspeed));
            } else if (vehicle.getCurSpeed() == 0) {
                loc.setYaw((float) (loc.getYaw() - rotspeed));
            }
            Methods.setPosition(vehicle.getMainStand(), loc);
        }
        if (d) {
            Location loc = vehicle.getMainStand().getLocation().clone();
            if (vehicle.getCurSpeed() > 0) {

                loc.setYaw((float) (loc.getYaw() + rotspeed));
                toAdd = addRightVelocity(vehicle.getMainStand(), 1, (float) vehicle.getCurSpeed() / 20f);

            } else if (vehicle.getCurSpeed() < 0) {
                loc.setYaw((float) (loc.getYaw() - rotspeed));
            } else if (vehicle.getCurSpeed() == 0) {
                loc.setYaw((float) (loc.getYaw() + rotspeed));
            }
            Methods.setPosition(vehicle.getMainStand(), loc);
        }



        if(vehicle.getType() == VehicleType.HELICOPTER) {
            if(vehicle.getMainStand().isOnGround()) {
                vehicle.setCurSpeed(0);
                toAdd = new Vector(0,0,0);
            }
        }


        vehicle.getMainStand().setGravity(true);
        Vector main = new Vector(vehicle.getMainStand().getLocation().getDirection().multiply(0.5).getX(), -1 * vehicle.getCurSpeed(), vehicle.getMainStand().getLocation().getDirection().multiply(0.5).getZ()).multiply(vehicle.getCurSpeed());
        if(vehicle.getType() == VehicleType.HELICOPTER) {



            main = new Vector(vehicle.getMainStand().getLocation().getDirection().multiply(0.5).getX(), vehicle.getCurUpSpeed(), vehicle.getMainStand().getLocation().getDirection().multiply(0.5).getZ()).multiply(vehicle.getCurSpeed());
            main.setY(vehicle.getCurUpSpeed());


            //Bukkit.broadcastMessage( "" + vehicle.getCurUpSpeed());
        }
        vehicle.getMainStand().setVelocity((main.add(toAdd)));
        if(vehicle.getMainStand().getLocation().getY() > Main.getInstance().getConfig().getInt("max-helicopter-height")) {
            Location toLoc = new Location(vehicle.getMainStand().getLocation().getWorld(), vehicle.getMainStand().getLocation().getX(), Main.getInstance().getConfig().getInt("max-helicopter-height") - 2, vehicle.getMainStand().getLocation().getZ());
            vehicle.setUpSpeed(0);
            vehicle.getMainStand().teleport(toLoc);
            Messages.send(p, Messages.MAX_HELICOPTER_HEIGHT);
        }
        vehicle.updatePositions();
    }

}

