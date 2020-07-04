package me.crack3dc0d3.minetopiavehiclesrevamp.main.events;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(Main.criticalUpdate) {
            event.getPlayer().sendMessage("\u00a74MinetopiaVehicles has found a critical update. If you're an restart the server. If you're not please ask an admin to restart the server!");
        }
    }


}
