package me.crack3dc0d3.minetopiavehiclesrevamp.main.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.StringJoiner;

public class SubCommandHelp extends SubCommand {
    public SubCommandHelp() {
        super("help", "Help bericht", "help", "minetopiavehicles.commands.help");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        String balkje = ChatColor.AQUA + "---------------------------------------------";
        StringJoiner joiner = new StringJoiner("\n", balkje + "\n", balkje);
        for (SubCommand subCommand : CommandHandler.getCommands()) {
            joiner.add(String.format("%s/%s%s %s %s- %s%s", ChatColor.AQUA, ChatColor.GREEN, label, subCommand.getUsage(),
                    ChatColor.AQUA, ChatColor.GREEN, subCommand.getDescription()));
        }
        sender.sendMessage(joiner.toString());
        return true;
    }
}
