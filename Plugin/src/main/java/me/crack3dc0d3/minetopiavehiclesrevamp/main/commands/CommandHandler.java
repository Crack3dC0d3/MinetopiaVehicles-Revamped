package me.crack3dc0d3.minetopiavehiclesrevamp.main.commands;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.enums.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandHandler implements CommandExecutor, TabCompleter {

    public static List<SubCommand> commands = new ArrayList<>();

    public CommandHandler() {
        commands.addAll(Arrays.asList(
            new SubCommandHelp(),
            new SubCommandList(),
            new SubCommandGive(),
            new SubCommandSetowner(),
            new SubCommandAddDriver(),
            new SubCommandAddMember(),
            new SubCommandRemoveDriver(),
            new SubCommandRemoveMember(),
            new SubCommandInfo()
        ));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                   "&3---------------------------------------------\n"
                 + "&bDeze server maakt gebruik van &3MinetopiaVehicles&b!\n"
                 + "&bGemaakt door &3Crack3dC0d3&b!\n"
                 + "&bVoor meer informatie bekijk de spigot page!\n"
                 + "&3---------------------------------------------"));
            return true;
        }

        SubCommand subCommand = getCommand(args[0]);
        if (subCommand == null) {
            Messages.send(sender, Messages.UNKNOWN_COMMAND, label);
        } else if (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) {
            Messages.send(sender, Messages.NO_PERMISSION);
        } else if (!subCommand.execute(sender, label, Arrays.copyOfRange(args, 1, args.length))) {
            Messages.send(sender, Messages.INVALID_ARGUMENTS, label, subCommand.getUsage());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) return commands.stream().map(SubCommand::getName).filter(name -> name.startsWith(args[0]))
                .collect(Collectors.toList());

        SubCommand subCommand = getCommand(args[0]);
        if (subCommand == null || subCommand.getPermission() == null || !sender.hasPermission(subCommand.getPermission())) return null;
        else return subCommand.onTabComplete(Arrays.copyOfRange(args, 1, args.length)).stream()
                .filter(name -> name.toLowerCase().startsWith(args[args.length - 1])).collect(Collectors.toList());
    }

    public static List<SubCommand> getCommands() {
        return commands;
    }

    SubCommand getCommand(String name) {
        for (SubCommand subCmd : commands)
            if (subCmd.getName().equalsIgnoreCase(name)) return subCmd;
        return null;
    }
}
