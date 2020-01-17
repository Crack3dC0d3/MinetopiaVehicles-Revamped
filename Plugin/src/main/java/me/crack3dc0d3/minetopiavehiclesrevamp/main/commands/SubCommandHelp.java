package me.crack3dc0d3.minetopiavehiclesrevamp.main.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class SubCommandHelp  implements ISubCommand {
    @Override
    public String getPermission() {
        return "minetopiavehicles.help";
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Help bericht.";
    }

    @Override
    public String getUsage() {
        return "vehicle help";
    }

    @Override
    public void execute(Player p, Command command, String[] args) {

        StringBuilder helpBuilder = new StringBuilder();
        

        helpBuilder.append(colorFormat("&2---------------------------------------------\n"));

        for (ISubCommand subCommand:CommandHandler.getCommands()
             ) {
        	
            helpBuilder.append(colorFormat("&2/&a")).append(subCommand.getUsage()).append(colorFormat(" &2- &a")).append(subCommand.getDescription()).append("\n");
            
        }
        helpBuilder.append(colorFormat("&2---------------------------------------------"));

        p.sendMessage(helpBuilder.toString());
    }
    
    public String colorFormat(String s){
        return s.replace('&', ChatColor.COLOR_CHAR);
    }
}
