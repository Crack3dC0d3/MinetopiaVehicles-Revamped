package me.crack3dc0d3.minetopiavehiclesrevamp.main.util;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.Main;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.commands.CommandHandler;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.events.Interact;
import org.bukkit.Bukkit;

public class RegistryHandler {

    public static void register(Main main) {
        registerCommands(main);
        registerEvents(main);
    }

    private static void registerEvents(Main main) {
        Bukkit.getPluginManager().registerEvents(new Interact(), main);
    }

    private static void registerCommands(Main main) {
        main.getCommand("minetopiavehicles").setExecutor(new CommandHandler());
    }

}
