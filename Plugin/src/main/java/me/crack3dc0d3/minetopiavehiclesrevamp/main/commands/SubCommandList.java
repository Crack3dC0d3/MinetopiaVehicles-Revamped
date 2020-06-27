package me.crack3dc0d3.minetopiavehiclesrevamp.main.commands;

import com.google.common.collect.Lists;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleBase;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleManager;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.enums.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class SubCommandList extends SubCommand {
    public SubCommandList() {
        super("list", "Lijst met alle voertuigen", "list [page]", "minetopiavehicles.command.list");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        int page;
        try {
            page = Integer.parseInt(args[0]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
            return false;
        }
        List<List<VehicleBase>> pages = Lists.partition(VehicleManager.getBaseVehicles(), 8);
        String balkje = ChatColor.AQUA + "------";
        StringJoiner joiner = new StringJoiner("\n", balkje + "[Vehicle List]" + balkje,
                balkje + "[Page " + page + "/" + pages.size() + "]" + balkje);
        for (VehicleBase base : pages.get(page - 1)) {
            joiner.add(ChatColor.GREEN + base.getName() + " - /" + label + " give <player> " + base.getName());
        }

        sender.sendMessage(joiner.toString());
        return true;
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        if (args.length == 1) return new ArrayList<String>() {{
            for (int i = 1; i <= Lists.partition(VehicleManager.getBaseVehicles(), 8).size(); i++) add("" + i);
        }};
        return new ArrayList<>();
    }
}
