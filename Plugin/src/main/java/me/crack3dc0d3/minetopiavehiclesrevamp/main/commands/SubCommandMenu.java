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
        CustomHolder holder = new CustomHolder(27, "\u00a76\u00a7lVoertuigen");


        holder.addIcon(new Icon(
                new ItemFactory(Material.DIAMOND_HOE)
                        .setDurability((short) 1045)
                        .setName("&6Fietsen")
                        .unbreakable()
                        .toItemStack())
                .addClickAction(player -> player.openInventory(generateMenu("FIETS", "Fietsen", holder).getInventory())));

        holder.addIcon(new Icon(
                new ItemFactory(Material.DIAMOND_HOE)
                        .setDurability((short) 1017)
                        .setName("&62 Persoons autos")
                        .unbreakable()
                        .toItemStack())
                .addClickAction(player -> player.openInventory(generateMenu("SEDAN", "2 Persoons autos", holder).getInventory())));

        holder.addIcon(new Icon(
                new ItemFactory(Material.DIAMOND_HOE)
                        .setDurability((short) 1130)
                        .setName("&6Helicopters")
                        .unbreakable()
                        .toItemStack())
                .addClickAction(player -> player.openInventory(generateMenu("HELI", "Helicopters", holder).getInventory())));

        holder.addIcon(new Icon(
                new ItemFactory(Material.DIAMOND_HOE)
                        .setDurability((short) 1006)
                        .setName("&6Hotrods")
                        .unbreakable()
                        .toItemStack())
                .addClickAction(player -> player.openInventory(generateMenu("HOTROD", "Hotrods", holder).getInventory())));

        holder.addIcon(new Icon(
                new ItemFactory(Material.DIAMOND_HOE)
                        .setDurability((short) 1033)
                        .setName("&6Oxboards")
                        .unbreakable()
                        .toItemStack())
                .addClickAction(player -> player.openInventory(generateMenu("OXBOARD", "Oxboards", holder).getInventory())));

        holder.addIcon(new Icon(
                new ItemFactory(Material.DIAMOND_HOE)
                        .setDurability((short) 1041)
                        .setName("&6Motoren")
                        .unbreakable()
                        .toItemStack())
                .addClickAction(player -> player.openInventory(generateMenu("MOTOR", "Motoren", holder).getInventory())));

        holder.addIcon(new Icon(
                new ItemFactory(Material.DIAMOND_HOE)
                        .setDurability((short) 1073)
                        .setName("&6Jeeps")
                        .unbreakable()
                        .toItemStack())
                .addClickAction(player -> player.openInventory(generateMenu("JEEP", "Jeeps", holder).getInventory())));

        holder.addIcon(new Icon(
                new ItemFactory(Material.DIAMOND_HOE)
                        .setDurability((short) 1166)
                        .setName("&6Racewagens")
                        .unbreakable()
                        .toItemStack())
                .addClickAction(player -> player.openInventory(generateMenu("RACE", "Racewagens", holder).getInventory())));

        holder.addIcon(new Icon(
                new ItemFactory(Material.DIAMOND_HOE)
                        .setDurability((short) 1063)
                        .setName("&6Sportautos")
                        .unbreakable()
                        .toItemStack())
                .addClickAction(player -> player.openInventory(generateMenu("SPORT", "Sportautos", holder).getInventory())));

        holder.addIcon(new Icon(
                new ItemFactory(Material.DIAMOND_HOE)
                        .setDurability((short) 1137)
                        .setName("&6SUV's")
                        .unbreakable()
                        .toItemStack())
                .addClickAction(player -> player.openInventory(generateMenu("SUV", "SUV's", holder).getInventory())));


        ((Player) sender).openInventory(holder.getInventory());
        return true;
    }


    public static CustomHolder generateMenu(String vehicleType, String displayname, CustomHolder main) {
        CustomHolder menu = new CustomHolder(54, "\u00a76\u00a7l" + displayname);
        int pos = 0;
        for (VehicleBase base : VehicleManager.getBaseVehicles()) {
            if (base.getName().contains(vehicleType)) {
                menu.setIcon(pos, new Icon(new ItemFactory(base.getSkinItem())
                        .setLore(" ", "&aXX-XX-XX")
                        .setName(base.getDisplayname()).toItemStack())
                        .addClickAction(player1 -> {
                            VehicleManager.giveVehicle(base, Bukkit.getOfflinePlayer(player1.getUniqueId()));
                            player1.closeInventory();
                        }));
                pos++;
            }
        }
        menu.setIcon(49, new Icon(
                new ItemFactory(Material.BARRIER)
                        .setName("&cTerug")
                        .toItemStack())
                .addClickAction(player1 -> player1.openInventory(main.getInventory())));
        return menu;
    }


}
