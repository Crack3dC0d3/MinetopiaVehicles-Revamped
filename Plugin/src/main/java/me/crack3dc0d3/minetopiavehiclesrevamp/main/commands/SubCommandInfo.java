package me.crack3dc0d3.minetopiavehiclesrevamp.main.commands;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Vehicle;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleManager;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.enums.Messages;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collectors;

public class SubCommandInfo extends SubCommand {
    public SubCommandInfo() {
        super("info", "Krijg info over een voertuig", "info", "minetoipavehicles.command.info");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            ItemStack itemInHand = p.getInventory().getItemInMainHand();
            if(itemInHand.getItemMeta() != null && itemInHand.getItemMeta().getLore() != null) {
                Vehicle v = VehicleManager.getVehicleByPlate(itemInHand.getItemMeta().getLore().get(1).replace("\u00A7a", ""));
                if(v == null) {
                    Messages.send(sender, Messages.NO_VEHICLE_IN_HAND);
                    return true;
                }

                String members = v.getMembers().stream().map(uuid -> Bukkit.getOfflinePlayer(uuid).getName())
                        .collect(Collectors.joining(", "));
                String riders = v.getRiders().stream().map(uuid -> Bukkit.getOfflinePlayer(uuid).getName())
                        .collect(Collectors.joining(", "));

                StringJoiner joiner = new StringJoiner("\n" + ChatColor.AQUA,
                        ChatColor.AQUA + "------[&aVehicle Info&2]------\n", "");
                joiner.add("Kenteken: " + ChatColor.GREEN + v.getLicensePlate())
                      .add("Owner: " + ChatColor.GREEN + v.getOwner().getName())
                      .add("Drivers: " + ChatColor.GREEN + riders)
                      .add("Members: " + ChatColor.GREEN + members)
                      .add("Type: " + ChatColor.GREEN + v.getType())
                      .add("Name: " + ChatColor.GREEN + v.getName());

                sender.sendMessage(joiner.toString());
            } else Messages.send(sender, Messages.NO_VEHICLE_IN_HAND);
        } else Messages.send(sender, Messages.ONLY_PLAYER);
        return true;
    }
}
