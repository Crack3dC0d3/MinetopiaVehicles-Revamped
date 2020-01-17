package me.crack3dc0d3.minetopiavehiclesrevamp.main;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.crack3dc0d3.minetopiavehiclesrevamp.api.NMS;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.Config;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.enums.Messages;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private NMS nms;

    private static Main instance;

    private static Config settings;
    private static Config messages;

    private static ProtocolManager protocolManager;


    @Override
    public void onEnable() {
        instance = this;
        messages = new Config("messages.yml");
        messages.loadConfig();
        Messages.updateMessagesFile();

        settings = new Config("settings.yml");
        settings.loadConfig();


        protocolManager = ProtocolLibrary.getProtocolManager();
        loadNMS();
        nms.handleInput(protocolManager, this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }



    private void loadNMS() {
        String packageName = this.getServer().getClass().getPackage().getName();
        // Get full package string of CraftServer.
        // org.bukkit.craftbukkit.version
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        // Get the last element of the package

        try {
            final Class<?> clazz = Class.forName("me.crack3dc0d3.minetopiavehiclesrevamp.nms." + version + ".NMSHandler");
            // Check if we have a NMSHandler class at that location.
            if (NMS.class.isAssignableFrom(clazz)) { // Make sure it actually implements NMS
                this.nms = (NMS) clazz.getConstructor().newInstance(); // Set our handler
            }
        } catch (final Exception e) {
            e.printStackTrace();
            this.getLogger().severe("Could not find support for this CraftBukkit version.");
            this.getLogger().info("Check for updates at URL HERE");
            this.setEnabled(false);
            return;
        }
        this.getLogger().info("Loading support for " + version);
    }

    public NMS getNms() {
        return nms;
    }

    public static Main getInstance() {
        return instance;
    }

    public static Config getSettings() {
        return settings;
    }

    public static Config getMessages() {
        return messages;
    }

    public static ProtocolManager getProtocolManager() {
        return protocolManager;
    }
}
