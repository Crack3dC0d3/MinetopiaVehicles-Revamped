package me.crack3dc0d3.minetopiavehiclesrevamp.main;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.crack3dc0d3.minetopiavehiclesrevamp.api.NMS;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Seat;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.Vehicle;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleBase;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.api.vehicle.VehicleManager;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.*;
import me.crack3dc0d3.minetopiavehiclesrevamp.main.util.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    private NMS nms;

    private static Main instance;

    private static Config settings;
    private static Config messages;
    private static IDataSource databaseUtil;

    private static ProtocolManager protocolManager;

    private String version = "0.0.1-BETA";


    @Override
    public void onEnable() {

        instance = this;

        // Updat3r

        Updat3r updat3r = Updat3r.getInstance();
        Updat3r.Update update = updat3r.getLatestUpdate("mtvehicles-opensource", "qq8lwF7kREzIYLZs3p38GNXUaccvBQ2c");
        if(update != null) {
            if(!update.getVersion().equals(version)) {
                if(update.isCritical()) {
                    this.getLogger().log(Level.SEVERE, "A Critical update has been found. Server will restart now!");
                    updat3r.downloadLatest(update.getDownloadLink(), "mtvehicles-opensource", this);
                    Bukkit.shutdown();
                    return;
                }
                updat3r.downloadLatest(update.getDownloadLink(), "mtvehicles-opensource", this);
                getLogger().log(Level.INFO, "An update has been found and downloaded. Restart your server to apply!");
            }
        }


        loadFiles();

        protocolManager = ProtocolLibrary.getProtocolManager();
        loadNMS();
        nms.handleInput(protocolManager, this);
        databaseUtil = new SQLiteDataSource();
        databaseUtil.init();
        //Bukkit.getPluginManager().registerEvents(new Interact(), this);

        try {
            loadVehicles();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        RegistryHandler.register(this);
        for (Vehicle v : VehicleManager.getVehicles()) {
            int seatCount = 1;
            if(v.isSpawned()) {
                for (World w : Bukkit.getWorlds()) {
                    for (Entity entity : w.getEntities()) {
                        if(entity instanceof ArmorStand) {
                            if(entity.getCustomName() != null) {
                                String[] strings  = entity.getCustomName().split("_");
                                if(strings[2].equals(v.getLicensePlate())) {
                                    switch (strings[1]) {
                                        case "SKIN":
                                            v.setSkinStand((ArmorStand) entity);
                                            break;
                                        case "VEHICLE":
                                            v.setMainStand((ArmorStand) entity);
                                            break;
                                        case "WIEKEN":
                                            v.setWiekStand((ArmorStand) entity);
                                            break;
                                        case "MAINSEAT":
                                            v.getMainSeat().setSeat((ArmorStand) entity);
                                            v.getMainSeat().setMainVehicle(v);
                                            Seat.addSeat(v.getMainSeat());
                                            v.getMainSeat().updateOffset();
                                            break;
                                        case "SEAT":
                                            v.getSeats()[seatCount].setMainVehicle(v);
                                            v.getSeats()[seatCount].setSeat((ArmorStand) entity);
                                            v.getSeats()[seatCount].updateOffset();
                                            Seat.addSeat(v.getSeats()[seatCount]);
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDisable() {
        for (Vehicle v :VehicleManager.getVehicles()) {
            databaseUtil.saveVehicle(v);
        }
    }

//    private int vehicleCount = 0;
//    @Override
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        if(command.getName().equals("givevehicleitems")) {
//            for (int i = 0; i < 27 ; i++) {
//                ItemStack vehicle = new ItemStack(Material.DIAMOND_HOE, 1, (short)(1003 + vehicleCount));
//                ItemMeta meta = vehicle.getItemMeta();
//                meta.setDisplayName(String.valueOf(1003+vehicleCount));
//                meta.setUnbreakable(true);
//                vehicle.setItemMeta(meta);
//                ((Player) sender).getInventory().addItem(vehicle);
//                vehicleCount++;
//            }
//        }
//        return true;
//    }

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
        VehicleManager.addVehicle(databaseUtil.getVehicles());
        for (Vehicle v : VehicleManager.getVehicles()
             ) {
            for (Seat s : v.getSeats()
                 ) {
                s.setMainVehicle(v);
                s.updateOffset();
                Seat.addSeat(s);
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

        settings.getConfig().addDefault("max-helicopter-height", 200);
        settings.getConfig().addDefault("breakSpeed", 0.05);
        settings.getConfig().options().copyDefaults(true);
        settings.saveConfig();



        getLogger().info("Loaded settings.yml");

//        getLogger().info("Loading vehicle files");
//        try {
//            File dir = new File(getDataFolder(), "vehicles");
//            if (!dir.exists()) {
//                dir.mkdirs();
//                File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
//                JarFile jar = new JarFile(jarFile);
//                Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
//                while(entries.hasMoreElements()) {
//                    JarEntry element = entries.nextElement();
//                    if (element.getName().startsWith("vehicles/") && !element.getName().equals("vehicles/")) { //filter according to the path
//                        saveResource(element.getName(), false);
//                    }
//                }
//                jar.close();
//            }
//            if(dir.isDirectory() && dir.listFiles() != null) {
//                for (File f: dir.listFiles()) {
//                    FileConfiguration config = new YamlConfiguration();
//                    config.load(f);
//                    VehicleManager.addBase(VehicleBase.fromYML(config));
//            }
//            }
//        } catch (Exception ex) {
//            getLogger().severe("Er ging iets mis met het laden van de voertuigbestanden, De plugin word nu gestopt.");
//            ex.printStackTrace();
//            getPluginLoader().disablePlugin(this);
//            return;
//        }
//        getLogger().info("Loaded vehicle files");
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

    public static IDataSource getDatabaseUtil() {
        return databaseUtil;
    }
}
