package me.crack3dc0d3.minetopiavehiclesrevamp.main.util.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

public class CustomHolder implements InventoryHolder {

    private final Map<Integer, Icon> icons = new HashMap<>();

    private final int size;
    private final String title;

    public CustomHolder(int size, String title) {
        this.size = size;
        this.title = title;
    }

    public void setIcon(int position, Icon icon) {
        this.icons.put(position, icon);
    }

    public void addIcon(Icon icon) {
        this.icons.put(icons.size(), icon);
    }

    public Icon getIcon(int position) {
        return this.icons.get(position);
    }

    public Map<Integer, Icon> getIcons() { return icons; }

    public int getSize() {
        return size;
    }


    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, this.size, ChatColor.translateAlternateColorCodes('&', this.title));

        //You should check for inventory size so you don't get errors
        for (Map.Entry<Integer, Icon> entry : this.icons.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue().itemStack);
        }

        return inventory;
    }
}