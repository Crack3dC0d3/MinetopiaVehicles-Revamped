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

public class SubCommandSetowner implements ISubCommand {
    @Override
    public String getPermission() {
        return "minetopiavehicles.command.setowner";
    }

    @Override
    public String getName() {
        return "setowner";
    }

    @Override
    public String getDescription() {
        return "Changes the owner of a vehicle";
    }

    @Override
    public String getUsage() {
        return "vehicle setowner <player>";
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
                v.setOwner(offlineTarget);
                Messages.send(sender, Messages.OWNER_CHANGED, offlineTarget.getName());
            } else {
                Messages.send(sender, Messages.NO_VEHICLE_IN_HAND);
            }
        } else {
            Messages.send(sender, Messages.ONLY_PLAYER);
        }


    }
}
