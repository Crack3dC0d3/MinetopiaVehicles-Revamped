package me.crack3dc0d3.minetopiavehiclesrevamp.main.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface ISubCommand {

    String getPermission();

    String getName();

    String getDescription();

    String getUsage();

    void execute(CommandSender sender, Command command, String[] args);

}
