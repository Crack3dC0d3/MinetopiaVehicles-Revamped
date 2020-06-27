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
        List<List<VehicleBase>> pages = Lists.partition(VehicleManager.getBaseVehicles(), 8);
        List<VehicleBase> page;
        try {
            int index = Integer.parseInt(args[0]) - 1;
            page = pages.get(index);
        } catch (NumberFormatException | IndexOutOfBoundsException exception) {
            Messages.send(sender, Messages.INVALID_ARGUMENTS, "/" + getUsage());
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("&2------[Vehicle List]------\n");
        for (VehicleBase base : page) {
            builder.append("&a").append(base.getName()).append(" - /vehicle give <username> ").append(base.getName()).append("\n");
        }
        builder.append("&2------[Page ").append(page).append("/").append(pages.size()).append("]-------");

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', builder.toString()));
    }
}
