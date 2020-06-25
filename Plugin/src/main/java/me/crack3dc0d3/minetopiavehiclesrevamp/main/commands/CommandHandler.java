package me.crack3dc0d3.minetopiavehiclesrevamp.main.commands;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.enums.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandHandler implements CommandExecutor {

    public static List<ISubCommand> commands = new ArrayList<>();

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

        for (ISubCommand subCommand : commands) {
            if (!subCommand.getName().equalsIgnoreCase(args[0])) continue;
            if(subCommand.getPermission() == null || sender.hasPermission(subCommand.getPermission())) {
                subCommand.execute(sender, command, Arrays.stream(args).skip(1).toArray(String[]::new));
            } else Messages.send(sender, Messages.NO_PERMISSION);
            break;
        }
        return true;
    }

    public static List<ISubCommand> getCommands() {
        return commands;
    }    
}
