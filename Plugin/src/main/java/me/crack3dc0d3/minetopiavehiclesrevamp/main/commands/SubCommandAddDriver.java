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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SubCommandAddDriver extends SubCommand {
    public SubCommandAddDriver() {
        super("adddriver", "Voegt een bestuurder toe aan een voertuig", "adddriver <player>",null);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length < 1) return false;
        if (sender instanceof Player) {
            Player p = (Player) sender;
            OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[0]);
            if(!offlineTarget.hasPlayedBefore()) {
                Messages.send(sender, Messages.NEVER_JOINED, args[0]);
                return true;
            }
            ItemStack itemInHand = p.getInventory().getItemInMainHand();
            if(itemInHand.getItemMeta() != null && itemInHand.getItemMeta().getLore() != null) {
                Vehicle v = VehicleManager.getVehicleByPlate(itemInHand.getItemMeta().getLore().get(1).replace("\u00A7a", ""));
                if(v == null) {
                    Messages.send(sender, Messages.NO_VEHICLE_IN_HAND);
                    return true;
                }
                v.addRider(offlineTarget);
                Messages.send(sender, Messages.DRIVER_ADDED, offlineTarget.getName());
            } else Messages.send(sender, Messages.NO_VEHICLE_IN_HAND);
        } else Messages.send(sender, Messages.ONLY_PLAYER);
        return true;
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        if (args.length == 1) return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        else return new ArrayList<>();
    }
}
