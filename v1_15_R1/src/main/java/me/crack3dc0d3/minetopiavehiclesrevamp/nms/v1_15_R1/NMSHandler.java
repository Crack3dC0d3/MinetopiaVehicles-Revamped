package me.crack3dc0d3.minetopiavehiclesrevamp.nms.v1_15_R1;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.crack3dc0d3.minetopiavehiclesrevamp.api.ApiMethods;
import me.crack3dc0d3.minetopiavehiclesrevamp.api.InputHandler;
import me.crack3dc0d3.minetopiavehiclesrevamp.api.NMS;
import net.minecraft.server.v1_15_R1.EntityArmorStand;
import net.minecraft.server.v1_15_R1.PacketPlayInSteerVehicle;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;

public class NMSHandler implements NMS {

    private ApiMethods methods;

    public NMSHandler() {
        try {
            final Class<?> clazz = Class.forName("me.crack3dc0d3.minetopiavehiclesrevamp.main.util.Methods");
            this.methods = (ApiMethods) clazz.getConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleInput(ProtocolManager manager, Plugin main) {
        manager.addPacketListener(
                new PacketAdapter(main, ListenerPriority.LOWEST, PacketType.Play.Client.STEER_VEHICLE) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        if (event.getPacketType().equals(PacketType.Play.Client.STEER_VEHICLE)) {
                            PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle) event.getPacket().getHandle();
                            float forward = ppisv.b();
                            float side = ppisv.c();
                            boolean w, a, s, d;
                            if (forward > 0) {
                                w = true;
                                s = false;
                            } else if (forward < 0) {
                                w = false;
                                s = true;
                            } else {
                                w = false;
                                s = false;
                            }
                            if (side > 0) {
                                a = true;
                                d = false;
                            } else if (side < 0) {
                                a = false;
                                d = true;
                            } else {
                                a = false;
                                d = false;
                            }
                            methods.handleInput(new InputHandler(w, a, s, d, ppisv.d(), event.getPlayer()));
                        }
                    }
                });
    }

    @Override
    public void setPosition(ArmorStand a, Location pos) {
        EntityArmorStand stand = ((CraftArmorStand)a).getHandle();
        stand.setLocation(pos.getX(), pos.getY(), pos.getZ(), pos.getYaw(), pos.getPitch());
    }

    @Override
    public void openMenu(Player p, Plugin main) {

    }
}
