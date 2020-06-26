package me.crack3dc0d3.minetopiavehiclesrevamp.main.commands;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Vehicle;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleManager;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SubCommandInfo implements ISubCommand {
    @Override
    public String getPermission() {
        return "minetopiavehicles.command.info";
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Returns the vehicle info";
    }

    @Override
    public String getUsage() {
        return "vehicle info";
    }

    @Override
    public void execute(CommandSender sender, Command command, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            ItemStack itemInHand = p.getInventory().getItemInMainHand();
            if(itemInHand.getItemMeta() != null && itemInHand.getItemMeta().getLore() != null) {
                Vehicle v = VehicleManager.getVehicleByPlate(itemInHand.getItemMeta().getLore().get(1).replace("\u00A7a", ""));
                if(v == null) {
                    Messages.send(sender, Messages.NO_VEHICLE_IN_HAND);
                    return;
                }

                StringBuilder memberBuilder = new StringBuilder();

                for (UUID member : v.getMembers()) {
                    memberBuilder.append(Bukkit.getOfflinePlayer(member).getName() + ",");
                }
                StringBuilder riderBuilder = new StringBuilder();

                for (UUID rider : v.getRiders()) {
                    riderBuilder.append(Bukkit.getOfflinePlayer(rider).getName() + ",");
                }

                StringBuilder builder = new StringBuilder();
                builder.append("&2------[&aVehicle Info&2]------")
                        .append("\n&2Kenteken: &a" + v.getLicensePlate())
                        .append("\n&2Owner: &a" + v.getOwner().getName())
                        .append("\n&2Drivers: &a" + riderBuilder.toString())
                        .append("\n&2Members: &a" + memberBuilder.toString())
                        .append("\n&2Type: &a" + v.getType())
                        .append("\n&2Name: &a" + v.getName());
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', builder.toString()));
            } else {
                Messages.send(sender, Messages.NO_VEHICLE_IN_HAND);
            }
        } else {
            Messages.send(sender, Messages.ONLY_PLAYER);
        }
    }
}
