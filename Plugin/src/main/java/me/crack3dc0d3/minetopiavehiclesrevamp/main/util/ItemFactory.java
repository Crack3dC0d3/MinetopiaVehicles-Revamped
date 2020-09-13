package me.crack3dc0d3.minetopiavehiclesrevamp.main.util;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ItemFactory {

    private ItemStack is;
    private String skullOwner;
    private static Enchantment glow;

    /**
     * Constructor for the ItemFactory
     * @param material The Material for the ItemStack
     */

    public ItemFactory(Material material) {

        this(material, 1);

    }

    /**
     * Constructor for the ItemFactory
     * @param itemStack The ItemStack to use
     */

    public ItemFactory(ItemStack itemStack) {

        this.is = itemStack;

    }

    /**
     * Constructor for the ItemFactory
     * @param material The Material for the ItemStack
     * @param amount The amount for the ItemStack
     */

    public ItemFactory(Material material, int amount) {

        this.is = new ItemStack(material, amount);

    }

    /**
     * Constructor for the ItemFactory
     * @param material The Material for the ItemStack
     * @param amount The amount for the ItemStack
     * @param durability The durability for the ItemStack
     */

    public ItemFactory(Material material, int amount, byte durability) {

        this.is = new ItemStack(material, amount, (short) durability);

    }

    /**
     * Clone the ItemFactory
     * @return The cloned ItemFactory
     */

    public ItemFactory clone() {

        return new ItemFactory(this.is);

    }

    /**
     * Set the durability for the ItemStack
     * @param durability The durability for the ItemStack
     * @return The current ItemFactory
     * @since 1.0.0
     */

    public ItemFactory setDurability(short durability) {

        this.is.setDurability(durability);

        return this;

    }

    /**
     * Set the type for the ItemStack
     * @param material The Material for the ItemStack
     * @return The current ItemFactory
     */

    public ItemFactory setType(Material material) {

        this.is.setType(material);

        return this;

    }

    /**
     * Set the name for the ItemStack
     * @param name The name for the ItemStack
     * @return The current ItemFactory
     */

    public ItemFactory setName(String name) {

        ItemMeta im = this.is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        this.is.setItemMeta(im);

        return this;

    }

    /**
     * Set the amount for the ItemStack
     * @param amount The amount for the ItemStack
     * @return The current ItemFactory
     */

    public ItemFactory setAmount(int amount) {

        this.is.setAmount(amount);

        return this;

    }

    /**
     * Get the lore of the ItemStack
     * @return The lore as List
     */

    public List<String> getLore() {

        return this.is.getItemMeta().getLore();

    }

    /**
     * Set the lore for the ItemStack
     * @param lore The lore of the ItemStack
     * @return The current ItemFactory
     */

    public ItemFactory setLore(List<String> lore) {

        ItemMeta im = this.is.getItemMeta();

        List<String> formatted = new ArrayList<>();

        for(String str : lore) {
            formatted.add(ChatColor.translateAlternateColorCodes('&', str));
        }

        im.setLore(formatted);

        this.is.setItemMeta(im);

        return this;

    }

    /**
     * Remove the lore of the ItemStack
     * @return The current ItemFactory
     */

    public ItemFactory removeLore() {

        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        lore.clear();
        im.setLore(lore);

        this.is.setItemMeta(im);

        return this;

    }

    /**
     * Hide the attributes of the ItemStack
     * @return The current ItemFactory
     */

    public ItemFactory hideAttributes() {

        ItemMeta im = this.is.getItemMeta();

        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        this.is.setItemMeta(im);

        return this;

    }

    /**
     * Add an unsafe enchantment to the ItemStack
     * @param enchantment The Enchantment to add
     * @param level The level for the Enchantment
     * @return The current ItemFactory
     */

    public ItemFactory addUnsafeEnchantment(Enchantment enchantment, int level) {

        this.is.addUnsafeEnchantment(enchantment, level);

        return this;

    }

    /**
     * Remove an enchantment from the ItemStack
     * @param enchantment The Enchantment to remove
     * @return The current ItemFactory
     */

    public ItemFactory removeEnchantment(Enchantment enchantment) {

        this.is.removeEnchantment(enchantment);

        return this;

    }

    /**
     * Add an enchant to the ItemStack
     * @param enchantment The Enchantment to add
     * @param level The level for the Enchantment
     * @return The current ItemFactory
     */

    public ItemFactory addEnchant(Enchantment enchantment, int level) {

        ItemMeta im = this.is.getItemMeta();
        im.addEnchant(enchantment, level, true);

        this.is.setItemMeta(im);

        return this;

    }
    
    

	/**
     * Add a glowing Enchantment to the ItemStack
     * @param enchantment The Enchantment to add
     * @param level The level of the Enchantment
     * @return The current ItemFactory
     */

    public ItemFactory addEnchantGlow(Enchantment enchantment, int level) {

        ItemMeta im = this.is.getItemMeta();
        im.addEnchant(enchantment, level, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        this.is.setItemMeta(im);

        return this;


    }

    /**
     * Add multiple Enchantments to the ItemStack
     * @param enchantments The Enchantments to add
     * @return The current ItemFactory
     */

    public ItemFactory addEnchantments(java.util.Map<Enchantment, Integer> enchantments) {
    	
    	List<Enchantment[]>enchantsList = new ArrayList<Enchantment[]>();

        this.is.addEnchantments(enchantments);

        return this;

    }

    /**
     * Set the durability of the ItemStack to infinite
     * @return The current ItemFactory
     */

    public ItemFactory setInfinityDurability() {

        this.is.setDurability(Short.MAX_VALUE);

        return this;

    }

    /**
     * Add a lore line to the ItemStack
     * @param lore The lore line to add
     * @return The current ItemFactory
     */

    public ItemFactory setLore(String... lore) {

        List<String> loreList = new ArrayList<>();

        for(String loreLine : lore) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', loreLine));
        }

        ItemMeta im = this.is.getItemMeta();
        im.setLore(loreList);

        this.is.setItemMeta(im);

        return this;

    }

    /**
     * Add multiple lore lines to the ItemStack
     * @param line The lore lines to add
     * @return The current ItemFactory
     */

    public ItemFactory addLoreLines(List<String> line) {

        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>();

        if (im.hasLore()) {

            lore = new ArrayList<>(im.getLore());

        }
        for (String s : line) {

            lore.add(ChatColor.translateAlternateColorCodes('&', s));

        }

        im.setLore(lore);

        this.is.setItemMeta(im);

        return this;

    }

    /**
     * Remove a lore line from the ItemStack
     * @param line The line to remove
     * @return The current ItemFactory
     */

    public ItemFactory removeLoreLine(String line) {

        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());

        if (!lore.contains(line)) {

            return this;

        }

        lore.remove(line);
        im.setLore(lore);

        this.is.setItemMeta(im);

        return this;

    }

    /**
     * Remove a lore line from the ItemStack by index
     * @param index The index of the lore to remove
     * @return The current ItemFactory
     */

    public ItemFactory removeLoreLine(int index) {

        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());

        if ((index < 0) || (index > lore.size())) {

            return this;

        }

        lore.remove(index);
        im.setLore(lore);

        this.is.setItemMeta(im);

        return this;

    }

    /**
     * Add a lore line to the ItemStack
     * @param line The lore line to add
     * @return The current ItemFactory
     */

    public ItemFactory addLoreLine(String line) {

        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>();

        if (im.hasLore()) {

            lore = new ArrayList<>(im.getLore());

        }

        lore.add(ChatColor.translateAlternateColorCodes('&', line));
        im.setLore(lore);

        this.is.setItemMeta(im);

        return this;

    }

    /**
     * Add a lore line to the ItemStack by position (index)
     * @param line The lore line to add
     * @param pos The position of the line (index)
     * @return The current ItemFactory
     */

    public ItemFactory addLoreLine(String line, int pos) {

        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        lore.set(pos, ChatColor.translateAlternateColorCodes('&', line));
        im.setLore(lore);

        this.is.setItemMeta(im);

        return this;

    }

    /**
     * Set the DyeColor of the ItemStack (if available)
     * @param color The color to change to
     * @return The current ItemFactory
     */

    @SuppressWarnings("deprecation")
    public ItemFactory setDyeColor(DyeColor color) {

        this.is.setDurability((short) color.getDyeData());

        return this;

    }


    /**
     * Set the color of the leather armor (if available)
     * @param color The color to change to
     * @return The current ItemFactory
     */

    public ItemFactory setLeatherArmorColor(Color color) {

        try {

            LeatherArmorMeta im = (LeatherArmorMeta) this.is.getItemMeta();
            im.setColor(color);

            this.is.setItemMeta(im);

        } catch (ClassCastException ignored) { }

        return this;

    }

    /**
     * Convert from ItemFactory to ItemStack
     * @return The converted ItemStack
     */

    public ItemStack toItemStack() {

        return this.is;

    }

    /**
     * Get the owner of the skull
     * @return The owner of the skull
     */

    public String getSkullOwner() {

        return this.skullOwner;

    }

    /**
     * Set the owner of the skull
     * @param owner The skull owner
     * @return The current ItemFactory
     */

    public ItemFactory setSkullOwner(String owner) {

        try {

            SkullMeta im = (SkullMeta) this.is.getItemMeta();
            im.setOwner(owner);

            this.is.setItemMeta(im);

        } catch (ClassCastException ignored) { }

        return this;

    }

    /**
     * Set the ItemStack unbreakable
     * @return The current ItemFactory
     * @since 1.0.0
     */

    public ItemFactory unbreakable() {
        ItemMeta meta = is.getItemMeta();
        meta.setUnbreakable(true);
        is.setItemMeta(meta);

        return this;

    }

    /**
     * Get NMS class.
     *
     * @param nmsClassString The name of the class.
     * @return The class.
     */

    private Class<?> getNMSClass(String nmsClassString) {

        try {

            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
            String name = "net.minecraft.server." + version + nmsClassString;
            return Class.forName(name);

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        }

    }

    /**
     * Get Bukkit class.
     *
     * @param packageName The name of the package.
     * @param className   The name of the class.
     * @return The class.
     */

    private Class<?> getBukkitClass(String packageName, String className) {

        try {

            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
            String name = "org.bukkit.craftbukkit." + version + packageName + "." + className;
            return Class.forName(name);

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        }

    }

}
