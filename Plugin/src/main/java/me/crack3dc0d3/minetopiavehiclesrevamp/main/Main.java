package me.crack3dc0d3.minetopiavehiclesrevamp.main;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.crack3dc0d3.minetopiavehiclesrevamp.api.NMS;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.Config;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleBase;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleManager;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.RegistryHandler;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.enums.Messages;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class Main extends JavaPlugin {

    private NMS nms;

    private static Main instance;

    private static Config settings;
    private static Config messages;

    private static ProtocolManager protocolManager;


    @Override
    public void onEnable() {

        instance = this;
        loadFiles();


        protocolManager = ProtocolLibrary.getProtocolManager();
        loadNMS();
        nms.handleInput(protocolManager, this);

        try {
            loadVehicles();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        RegistryHandler.regsiter(this);
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
            this.getLogger().severe("Support voor deze CraftBukkit versie is niet gevonden.");
            this.getLogger().info("Voor updates kijk op <hiermoetnogeenspigoturl>");
            this.setEnabled(false);
            return;
        }
        this.getLogger().info("Support word geladen voor versie " + version);
    }

    private void loadVehicles() throws IOException, InvalidConfigurationException {
        File dir = new File(getDataFolder() + File.separator + "vehicles");
        File[] directoryListing = dir.listFiles();
        if(directoryListing != null) {
            for(File f : directoryListing) {
                FileConfiguration config = new YamlConfiguration();
                config.load(f);
                VehicleManager.addBase(VehicleBase.fromYML(config));
                this.getLogger().info("Loaded vehicle " + f.getName());
            }
        }
    }

    private void loadFiles() {
        getLogger().info("Loading messages.yml");
        messages = new Config("messages.yml");
        messages.loadConfig();
        Messages.updateMessagesFile();
        getLogger().info("Loaded messages.yml");

        getLogger().info("Loading settings.yml");
        settings = new Config("settings.yml");
        settings.loadConfig();
        getLogger().info("Loaded settings.yml");

        getLogger().info("Loading vehicle files");
        try {
            File dir = new File(getDataFolder(), "vehicles");
            if (!dir.exists()) {
                dir.mkdirs();
                File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
                JarFile jar = new JarFile(jarFile);
                Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while(entries.hasMoreElements()) {
                    JarEntry element = entries.nextElement();
                    if (element.getName().startsWith("vehicles/") && !element.getName().equals("vehicles/")) { //filter according to the path
                        saveResource(element.getName(), false);
                    }
                }
                jar.close();

            }
        } catch (Exception ex) {
            getLogger().severe("Er ging iets mis met het laden van de voertuigbestanden, De plugin word nu gestopt.");
            ex.printStackTrace();
            getPluginLoader().disablePlugin(this);
            return;
        }
        getLogger().info("Loaded vehicle files");
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
