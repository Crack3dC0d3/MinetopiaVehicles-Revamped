package me.crack3dc0d3.minetopiavehiclesrevamp.main;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
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
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    private NMS nms;

    private static Main instance;

    private static Config settings;
    private static Config messages;
    private static IDataSource databaseUtil;

    private static ProtocolManager protocolManager;

    public static boolean criticalUpdate = false;
    private static boolean cancelUpdater = false;
    private static TaskChainFactory taskChainFactory;



    @Override
    public void onEnable() {
        taskChainFactory = BukkitTaskChainFactory.create(this);
        instance = this;
        RegistryHandler.register(this);
        // Updat3r

        Updat3r updat3r = Updat3r.getInstance();

        (new BukkitRunnable() {
            @Override
            public void run() {
                newChain()
                        .sync(() -> getLogger().log(Level.INFO, "Checking for updates..."))
                        .<Updat3r.Update>asyncFirstCallback(next -> {
                            Updat3r.Update update = updat3r.getLatestUpdate("Mtvehicles-opensource", "qq8lwF7kREzIYLZs3p38GNXUaccvBQ2c");
                            if(update == null) {
                                getLogger().info("Update check failed. No versions found!");
                            }
                            next.accept(update);
                        })
                        .abortIfNull()
                        .sync(input -> {
                            if (!input.getVersion().equals(Main.getInstance().getDescription().getVersion())) {
                                if (input.isCritical()) {
                                    Main.getInstance().getLogger().log(Level.SEVERE, "A Critical update has been found. Please restart the server immediately!");
                                    updat3r.downloadLatest(input.getDownloadLink(), "Mtvehicles-opensource", Main.getInstance());
                                    return input;
                                }
                                updat3r.downloadLatest(input.getDownloadLink(), "Mtvehicles-opensource", Main.getInstance());
                                getLogger().log(Level.INFO, "An update has been found and downloaded. Will be applied next update!");
                                for (Player p : Bukkit.getOnlinePlayers()
                                     ) {
                                    if(p.isOp()) {
                                        p.sendMessage("\u00a74MinetopiaVehicles has found an update. Restart your server to apply it!");
                                    }
                                }
                            }
                            return input;
                        })
                        .syncLast(input -> criticalUpdate = input.isCritical())
                        .execute((task) -> getLogger().info("Update check completed!"));
            }
        }).runTaskTimerAsynchronously(this, 0, 20L*60*30);

        loadFiles();

        protocolManager = ProtocolLibrary.getProtocolManager();
        loadNMS();
        nms.handleInput(protocolManager, this);
        if ("mysql".equals(settings.getConfig().getString("datastorage-type"))) {
            databaseUtil = new MySQLDataSource();
        } else {
            databaseUtil = new SQLiteDataSource();
        }
        databaseUtil.init();
        //Bukkit.getPluginManager().registerEvents(new Interact(), this);

        try {
            loadVehicles();
        } catch (IOException | InvalidConfigurationException | URISyntaxException e) {
            e.printStackTrace();
        }
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
                                    Bukkit.getLogger().log(Level.INFO, "Updated vehicle " + strings[2]);
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
            this.getLogger().info("Voor updates kijk op http://download.minetopiavehicles.me/");
            this.setEnabled(false);
            return;
        }
        this.getLogger().info("Support word geladen voor versie " + version);
    }

    private void loadVehicles() throws IOException, InvalidConfigurationException, URISyntaxException {
        File dir = new File(getDataFolder() + File.separator + "vehicles");
        File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        JarFile jar = new JarFile(jarFile);
        Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
        while(entries.hasMoreElements()) {
            JarEntry element = entries.nextElement();
            if (element.getName().startsWith("vehicles/") && !element.getName().equals("vehicles/")) { //filter according to the path
                saveResource(element.getName(), true);
            }
        }
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
        settings.getConfig().addDefault("enable-levelcheck-vehicles", true);
        settings.getConfig().addDefault("datastorage-type", "sqlite");
        settings.getConfig().addDefault("mysql.username", "username");
        settings.getConfig().addDefault("mysql.password", "password");
        settings.getConfig().addDefault("mysql.host", "host");
        settings.getConfig().addDefault("mysql.port", "port");
        settings.getConfig().addDefault("mysql.database", "database");
        settings.getConfig().addDefault("enable-cruisecontrol", false);
        settings.getConfig().addDefault("enable-achterbak", false);
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


    public static <T> TaskChain<T> newChain() {
        return taskChainFactory.newChain();
    }
    public static <T> TaskChain<T> newSharedChain(String name) {
        return taskChainFactory.newSharedChain(name);
    }

}
