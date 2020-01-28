package me.crack3dc0d3.minetopiavehiclesrevamp.api;

import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface NMS {

    InputHandler handleInput(ProtocolManager manager, Plugin main);

    void setPosition(ArmorStand a, Location pos);

    void openMenu(Player p, Plugin main);



}
