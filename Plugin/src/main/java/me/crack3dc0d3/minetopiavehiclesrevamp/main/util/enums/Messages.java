package me.crack3dc0d3.minetopiavehiclesrevamp.main.util.enums;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public enum Messages {

    MAX_HELICOPTER_HEIGHT("&cJe kan niet hoger vliegen!"),
    VEHICLE_DOESNT_EXIST("&cVoertuig &4%s &cbestaat niet!"),
    VEHICLE_GIVEN("&aJe hebt het voertuig \"%s\" gekregen."),
    INVALID_ARGUMENTS("&cVerkeerde argumenten, gebruik &4%s"),
    PLAYER_NOT_ONLINE("&cSpeler &4%s &cis niet online!"),
    SEAT_FULL("&cEr zit al iemand op deze stoel!"),
    NO_PERMISSION("&4Je hebt hier geen permissie voor!");


    String message;

    Messages(String message) {
        this.message = message;
    }

    public static void send(CommandSender player, Messages key, String... placeholders) {
        if (Main.getMessages().getConfig().getString(key.toString()) != null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    String.format(Main.getMessages().getConfig().getString(key.toString()), placeholders)));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "Messages." + key.toString()));
        }
    }

    public static void send(CommandSender player, Messages key) {
        if (Main.getMessages().getConfig().getString(key.toString()) != null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Main.getMessages().getConfig().getString(key.toString())));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "Messages." + key.toString()));
        }
    }

    public static String getKey(Messages key) {
        if (Main.getMessages().getConfig().getString(key.toString()) != null) {
            return ChatColor.translateAlternateColorCodes('&', Main.getMessages().getConfig().getString(key.toString()));
        } else {
            return ChatColor.translateAlternateColorCodes('&', "Messages." + key.toString());
        }
    }

    public static void updateMessagesFile() {
        for (Messages key : Messages.values()) {
            if (!Main.getMessages().getConfig().contains(key.toString())) {
                Main.getMessages().getConfig().set(key.toString(), key.getMessage());
                Main.getMessages().saveConfig();
            }
        }
    }

    public String getMessage() {
        return message;
    }
}

