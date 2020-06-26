package me.crack3dc0d3.minetopiavehiclesrevamp.main.commands;

import com.google.common.collect.Lists;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleBase;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleManager;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.enums.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SubCommandList implements ISubCommand {
    @Override
    public String getPermission() {
        return "minetopiavehicles.command.list";
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Lists all vehicles";
    }

    @Override
    public String getUsage() {
        return "vehicle list [page]";
    }

    @Override
    public void execute(CommandSender sender, Command command, String[] args) {
        int page;
        try {
            page = Integer.parseInt(args[0]);
        }catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
            Messages.send(sender, Messages.INVALID_ARGUMENTS, "/" + getUsage());
            return;
        }
        List<List<VehicleBase>> pages = Lists.partition(VehicleManager.getBaseVehicles(), 8);
        StringBuilder builder = new StringBuilder();
        builder.append("&2------[Vehicle List]------\n");
        for (VehicleBase base : pages.get(page - 1)) {
            builder.append("&a").append(base.getName()).append(" - /vehicle give <username> ").append(base.getName()).append("\n");
        }
        builder.append("&2------[Page " + page + "/" + pages.size() + "]-------");

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', builder.toString()));
    }
}
