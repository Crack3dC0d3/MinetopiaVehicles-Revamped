package me.crack3dc0d3.minetopiavehiclesrevamp.main.util;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.Main;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.commands.CommandHandler;

public class RegistryHandler {

    public static void regsiter(Main main) {
        registerCommands(main);
        registerEvents(main);
    }

    private static void registerEvents(Main main) {

    }

    private static void registerCommands(Main main) {
        main.getCommand("minetopiavehicles").setExecutor(new CommandHandler());
    }

}
