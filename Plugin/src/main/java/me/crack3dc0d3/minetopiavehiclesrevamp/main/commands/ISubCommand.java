package me.crack3dc0d3.minetopiavehiclesrevamp.main.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public interface ISubCommand {

    String getPermission();

    String getName();

    String getDescription();

    String getUsage();

    void execute(Player p, Command command, String[] args);
    
}
