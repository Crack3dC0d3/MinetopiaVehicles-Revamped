package me.crack3dc0d3.minetopiavehiclesrevamp.main.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class SubCommand {
    String name, description, usage, permission;

    public SubCommand(String name, String description, String usage, String permission) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.permission = permission;
    }

    public abstract boolean execute(CommandSender sender, String label, String[] args);

    public List<String> onTabComplete(String[] args) {
        return new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPermission() {
        return this.permission;
    }

    public String getUsage() {
        return this.usage;
    }
}
