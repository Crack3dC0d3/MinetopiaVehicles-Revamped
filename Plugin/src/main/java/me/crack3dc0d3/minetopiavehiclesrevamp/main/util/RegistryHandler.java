package me.crack3dc0d3.minetopiavehiclesrevamp.main.util;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.Main;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.commands.CommandHandler;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.events.Dismount;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.events.Interact;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.events.LevelCheck;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.events.Quit;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.inventories.InventoryEvents;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;

public class RegistryHandler {

    public static void register(Main main) {
        registerCommands(main);
        registerEvents(main);
    }

    private static void registerEvents(Main main) {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new Interact(), main);
        pm.registerEvents(new Dismount(), main);
        pm.registerEvents(new LevelCheck(), main);
        pm.registerEvents(new Quit(), main);
        pm.registerEvents(new InventoryEvents(), main);
    }

    private static void registerCommands(Main main) {
        PluginCommand cmd = main.getCommand("minetopiavehicles");
        CommandHandler handler = new CommandHandler();
        cmd.setExecutor(handler);
        cmd.setTabCompleter(handler);
    }
}
