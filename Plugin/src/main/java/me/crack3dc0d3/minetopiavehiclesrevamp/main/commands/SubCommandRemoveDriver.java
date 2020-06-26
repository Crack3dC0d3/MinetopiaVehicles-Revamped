package me.crack3dc0d3.minetopiavehiclesrevamp.main.commands;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Vehicle;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleManager;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SubCommandRemoveDriver implements ISubCommand {

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public String getName() {
        return "removedriver";
    }

    @Override
    public String getDescription() {
        return "Removes a driver from your vehicle";
    }

    @Override
    public String getUsage() {
        return "vehicle removedriver <player>";
    }

    @Override
    public void execute(CommandSender sender, Command command, String[] args) {
        if(args.length != 1) {
            Messages.send(sender, Messages.INVALID_ARGUMENTS, "/" + getUsage());
            return;
        }
        if(sender instanceof Player) {
            Player p = (Player) sender;
            OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[0]);
            if(!offlineTarget.hasPlayedBefore()) {
                Messages.send(sender, Messages.NEVER_JOINED, args[0]);
                return;
            }
            ItemStack itemInHand = p.getInventory().getItemInMainHand();
            if(itemInHand.getItemMeta() != null && itemInHand.getItemMeta().getLore() != null) {
                Vehicle v = VehicleManager.getVehicleByPlate(itemInHand.getItemMeta().getLore().get(1).replace("\u00A7a", ""));
                if(v == null) {
                    Messages.send(sender, Messages.NO_VEHICLE_IN_HAND);
                    return;
                }
                if(!v.getRiders().contains(offlineTarget.getUniqueId())) {
                    Messages.send(sender, Messages.NO_DRIVER, offlineTarget.getName());
                    return;
                }
                v.removeRider(offlineTarget);
                Messages.send(sender, Messages.MEMBER_REMOVED, offlineTarget.getName());
            } else {
                Messages.send(sender, Messages.NO_VEHICLE_IN_HAND);
            }
        } else {
            Messages.send(sender, Messages.ONLY_PLAYER);
        }
    }
}
