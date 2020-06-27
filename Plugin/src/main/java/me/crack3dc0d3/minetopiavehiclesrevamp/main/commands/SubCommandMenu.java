package me.crack3dc0d3.minetopiavehiclesrevamp.main.commands;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleBase;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleManager;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.ItemFactory;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.inventories.CustomHolder;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.inventories.Icon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SubCommandMenu extends SubCommand {

    public SubCommandMenu() {
        super("menu", "Opent een menu waar je alle voertuigen uit kan halen", "vehicle menu", "minetopiavehicles.command.menu");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        int totalPages = VehicleManager.getBaseVehicles().size() / 45;
        if(totalPages == 0) {
            totalPages = 1;
        }

        List<CustomHolder> pages = new ArrayList<>();

        int vehiclesize = VehicleManager.getBaseVehicles().size();
        int vehiclePos = 0;

        pages:
        for (int i = 0; i < totalPages ; i++) {

            int curPage = i+1;
            CustomHolder holder = new CustomHolder(54, "\u00a76\u00a7lVoertuigen " + curPage + "/" + totalPages);

//            int a = vehiclesToAdd / 45;
//            int overige = vehiclesToAdd % 45;
            int pos = 0;
            while(vehiclePos < vehiclesize) {

                VehicleBase base = VehicleManager.getBaseVehicles().get(vehiclePos);

                holder.setIcon(pos, new Icon(new ItemFactory(base.getSkinItem())
                        .setLore(" ", "&aXX-XX-XX")
                        .setName(base.getDisplayname()).toItemStack())
                        .addClickAction(player1 -> {
                            VehicleManager.giveVehicle(base, Bukkit.getOfflinePlayer(player1.getUniqueId()));
                            player1.closeInventory();
                        }));
                if(vehiclePos % 45 == 0 && vehiclePos >= 45 || vehiclePos == vehiclesize - 1) {
                    holder.setIcon(45, new Icon(
                            new ItemFactory(Material.PAPER)
                                    .setName("&6Terug naar het begin")
                                    .toItemStack())
                            .addClickAction(player1 -> player1.openInventory(pages.get(0).getInventory())));
                    holder.setIcon(49, new Icon(
                            new ItemFactory(Material.BARRIER)
                                    .setName("&6Sluiten")
                                    .toItemStack())
                            .addClickAction(HumanEntity::closeInventory)
                    );
                    holder.setIcon(53, new Icon(
                            new ItemFactory(Material.PAPER)
                                    .setName("&6Volgende Pagina")
                                    .toItemStack())
                            .addClickAction(player1 -> player1.openInventory(pages.get(curPage).getInventory())));
                    pages.add(holder);
                    vehiclePos++;
                    continue pages;
                } else {
                    vehiclePos++;
                    pos++;
                }


            }

        }
        ((Player) sender).openInventory(pages.get(0).getInventory());
        return false;
    }
}
