package me.crack3dc0d3.minetopiavehiclesrevamp.main.commands;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Vehicle;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleManager;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SubCommandSetowner extends SubCommand {
    public SubCommandSetowner() {
        super("setowner", "Verander de eigenaar van een voertuig", "setowner <player>",
                "minetopiavehicles.command.setowner");
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
                v.setOwner(offlineTarget);
                Messages.send(sender, Messages.OWNER_CHANGED, offlineTarget.getName());
            } else Messages.send(sender, Messages.NO_VEHICLE_IN_HAND);
        } else Messages.send(sender, Messages.ONLY_PLAYER);
        return true;
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        List<String> tabComplete = new ArrayList<>();

        if (args.length == 1) {
            tabComplete = Bukkit.getOnlinePlayers()
                    .stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
        }

        return tabComplete;
    }
}
