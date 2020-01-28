package me.crack3dc0d3.minetopiavehiclesrevamp.main.commands;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleBase;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleManager;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.Methods;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SubCommandGive implements ISubCommand {
    @Override
    public String getPermission() {
        return "minteopiavehicles.command.give";
    }

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getDescription() {
        return "Geef een voertuig aan iemand";
    }

    @Override
    public String getUsage() {
        return "/vehicle give <player> <vehiclename>";
    }


    @Override
    public void execute(CommandSender sender, Command command, String[] args) {
        if(args.length != 2) {
            Messages.send(sender, Messages.INVALID_ARGUMENTS, getUsage());
            return;
        }
        OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[0]);
        if(!offlineTarget.isOnline()) {
            Messages.send(sender, Messages.PLAYER_NOT_ONLINE, args[0]);
            return;
        }
        VehicleBase vehicle = VehicleManager.getVehicleBaseByName(args[1]);
        if(vehicle == null) {
            Messages.send(sender, Messages.VEHICLE_DOESNT_EXIST, args[1]);
            return;
        }
        Player target = offlineTarget.getPlayer();
        ItemStack toGive = vehicle.getSkinItem();
        ItemMeta im = toGive.getItemMeta();
        im.setDisplayName(vehicle.getDisplayname());
        im.setLore(Arrays.asList(" ", ChatColor.COLOR_CHAR + "a" + Methods.generateLicencePlate()));
        target.getInventory().addItem(toGive);
    }
}
