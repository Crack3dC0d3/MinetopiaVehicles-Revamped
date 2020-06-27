package me.crack3dc0d3.minetopiavehiclesrevamp.main.util;

import me.crack3dc0d3.minetopiavehiclesrevamp.main.Main;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Logger;

public class LibraryLoader {
    String LIB_PATH;
    public LibraryLoader() {
        LIB_PATH = Main.getInstance().getDataFolder() + File.separator + "libs";
        File libsFolder = new File(LIB_PATH);
        if (!libsFolder.exists()) libsFolder.mkdirs();
    }

    public void loadLibrary(String filename, String url) {
        Logger logger = Main.getInstance().getLogger();
        try {
            File libraryFile = new File(LIB_PATH + File.separator + filename);
            if (!libraryFile.exists()) {
                logger.info("Downloading " + filename + " library...");
                FileUtils.copyURLToFile(new URL(url), libraryFile, 10000, 10000);
                if (!libraryFile.exists() || libraryFile.getTotalSpace() == 0) {
                    logger.warning("Something went wrong with downloading library " + filename);
                    return;
                } else logger.info("Library " + filename + " is downloaded");
            }

            URL discord = libraryFile.toURI().toURL();
            URLClassLoader ucl = (URLClassLoader) getClass().getClassLoader();
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            add.setAccessible(true);
            add.invoke(ucl, discord);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
