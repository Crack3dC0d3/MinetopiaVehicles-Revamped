package me.crack3dc0d3.minetopiavehiclesrevamp.main.commands;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Vehicle;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SubCommandGive extends SubCommand {
    public SubCommandGive() {
        super("give", "Geef een voertuig aan een speler", "give <player> <vehiclename>", "minetopiavehicles.command.give");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length < 2) return false;
        OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[0]);
        if (!offlineTarget.isOnline()) {
            Messages.send(sender, Messages.PLAYER_NOT_ONLINE, args[0]);
            return true;
        }
        VehicleBase vehicle = VehicleManager.getVehicleBaseByName(args[1]);
        if (vehicle == null) {
            Messages.send(sender, Messages.VEHICLE_DOESNT_EXIST, args[1]);
            return true;
        }

        VehicleManager.giveVehicle(vehicle, offlineTarget);

        return true;
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        if (args.length == 1) return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        else if (args.length == 2) return VehicleManager.getBaseVehicles().stream().map(VehicleBase::getName).collect(Collectors.toList());
        return new ArrayList<>();
    }
}
