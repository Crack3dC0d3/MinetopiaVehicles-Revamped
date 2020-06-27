package me.crack3dc0d3.minetopiavehiclesrevamp.main.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Updat3r {

    private static Updat3r instance = null;

    public static Updat3r getInstance() {
        if (instance == null) {
            instance = new Updat3r();
        }
        return instance;
    }

    public Update getLatestUpdate(String project, String apiKey) {
        try {
            URL url = new URL("https://updates.mrwouter.nl/api/v1/updates/?project=" + project + "&key=" + apiKey
                    + "&show=latest");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", project + " UpdateChecker");
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) conn.getContent()));
            JsonObject rootobj = root.getAsJsonObject();

            int errorcode = rootobj.get("status").getAsInt();
            if (errorcode != 200) {
                System.out.println(
                        "[Minetopiavehicles Updat3r] An error has occured whilst downloading this resource. Contact support on discord: discord.gg/cuUfg4m\n Error:" + rootobj.get("message").getAsString());
                return null;
            }
            JsonArray updates = rootobj.get("updates").getAsJsonArray();
            for (JsonElement update : updates) {
                JsonObject updateObj = update.getAsJsonObject();
                String version = updateObj.get("version").getAsString();
                String download = updateObj.get("download").getAsString();
                String releaseDate = updateObj.get("releaseDate").getAsString();
                boolean critical = updateObj.get("critical").getAsBoolean();
                return new Update(version, download, releaseDate, critical);
            }
            return null;
        } catch (Exception ex) {
            System.out.println("[Minetopiavehicles Updat3r] An error has occured whilst downloading this resource. Contact support on discord: discord.gg/cuUfg4m");
            ex.printStackTrace();
            return null;
        }
    }

    public void downloadLatest(String fileURL, String project, Plugin pl) {
        try {
            URL url = new URL(fileURL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestProperty("User-Agent", project + " UpdateChecker");
            int responseCode = httpConn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpConn.getInputStream();
                String saveFilePath = new File(pl.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();

                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                int bytesRead = -1;
                byte[] buffer = new byte[4096];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();
                Bukkit.shutdown();
            } else {
                System.out.println("[Minetopiavehicles Updat3r] An error has occured whilst downloading this resource. Contact support on discord: discord.gg/cuUfg4m  (resp. code: " + responseCode + ")");
            }
            httpConn.disconnect();
        } catch (Exception ex) {
            System.out.println("[Minetopiavehicles Updat3r] An error has occured whilst downloading this resource. Contact support on discord: discord.gg/cuUfg4m");
            ex.printStackTrace();
        }
    }

    public static class Update {

        private String version, downloadlink, releaseDate;
        private boolean critical;

        public Update(String version, String downloadlink, String releaseDate, boolean critical) {
            this.version = version;
            this.downloadlink = downloadlink;
            this.releaseDate = releaseDate;
            this.critical = critical;
        }

        public String getVersion() {
            return version;
        }

        public String getDownloadLink() {
            return downloadlink;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public boolean isCritical() {
            return critical;
        }
    }
}
