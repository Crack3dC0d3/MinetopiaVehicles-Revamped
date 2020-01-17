package me.crack3dc0d3.minetopiavehiclesrevamp.main.commands;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.enums.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandHandler implements CommandExecutor {

    public static List<ISubCommand> commands = new ArrayList<>();

    public CommandHandler() {
        commands.add(new SubCommandHelp());
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length <= 0) {
        	sender.sendMessage(colorFormat("&3---------------------------------------------"));
        	sender.sendMessage(colorFormat("&bDeze server maakt gebruik van &3MinetopiaVehicles&b!"));
        	sender.sendMessage(colorFormat("&bGemaakt door &3Crack3dC0d3&b!"));
        	sender.sendMessage(colorFormat("&bVoor meer informatie bekijk de spigot page!"));
        	sender.sendMessage(colorFormat("&3---------------------------------------------"));
            return true;
        }

        String subCommand = args[0];
        List<String> newArgsList = new ArrayList<>(Arrays.asList(args));
        newArgsList.remove(0);


        if(sender instanceof Player) {

            for (ISubCommand subcommand : commands
            ) {
                if (subcommand.getName().equalsIgnoreCase(subCommand)) {

                    if(subcommand.getPermission() == null) {
                        subcommand.execute((Player) sender, command, newArgsList.toArray(new String[0]));
                        return true;
                    }

                    if(sender.hasPermission(subcommand.getPermission())) {
                        subcommand.execute((Player) sender, command, newArgsList.toArray(new String[0]));
                        return true;
                    } else {
                        Messages.send((Player) sender, Messages.NO_PERMISSION);
                        return true;
                    }
                }
            }
        }





        return true;
    }

    public static List<ISubCommand> getCommands() {
        return commands;
    }
    
    public String colorFormat(String s){
        return s.replace('&', ChatColor.COLOR_CHAR);
    }
    
}
