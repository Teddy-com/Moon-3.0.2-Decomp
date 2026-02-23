/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  org.apache.commons.io.FileUtils
 *  org.apache.commons.io.FilenameUtils
 */
package me.moon.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.ArrayList;
import me.moon.Moon;
import me.moon.config.Config;
import me.moon.module.Module;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class ConfigManager {
    private File dir;
    private ArrayList<Config> configs = new ArrayList();
    public Config currentConfig;

    public ConfigManager(File dir) {
        this.dir = dir;
    }

    public ArrayList<Config> getConfigs() {
        return this.configs;
    }

    public void clear() throws IOException {
        FileUtils.cleanDirectory((File)new File(this.dir + "/"));
    }

    public void setCurrentConfig(Config config) {
        this.currentConfig = config;
    }

    public void load() {
        if (!this.dir.exists()) {
            this.dir.mkdirs();
        }
        if (this.dir != null) {
            File[] files;
            for (File f2 : files = this.dir.listFiles(f -> !f.isDirectory() && FilenameUtils.getExtension((String)f.getName()).equals("json"))) {
                Config config = new Config(FilenameUtils.removeExtension((String)f2.getName()).replace(" ", ""));
                this.setCurrentConfig(config);
                this.configs.add(config);
            }
        }
    }

    public void deleteConfig(String cfgname) {
        File f = new File(this.dir, cfgname + ".json");
        if (f.exists()) {
            try {
                Files.delete(f.toPath());
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        Moon.INSTANCE.getConfigManager().getConfigs().remove(this.getConfig(cfgname));
    }

    public void saveConfig(String cfgname, boolean key) {
        File f = new File(this.dir, cfgname + ".json");
        if (!f.exists()) {
            try {
                f.createNewFile();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        JsonArray arr = new JsonArray();
        Moon.INSTANCE.getModuleManager().getModuleMap().values().forEach(module -> {
            JsonObject obj = new JsonObject();
            obj.addProperty("name", module.getLabel().toLowerCase());
            module.save(obj, key);
            arr.add((JsonElement)obj);
        });
        try (FileWriter writer = new FileWriter(f);){
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)arr));
        }
        catch (IOException e) {
            f.delete();
        }
    }

    public void loadConfig(String cfgname, boolean key) {
        File f = new File(this.dir, cfgname + ".json");
        if (!f.exists()) {
            return;
        }
        try {
            FileReader reader = new FileReader(f);
            JsonElement node = new JsonParser().parse((Reader)reader);
            JsonArray arr = node.getAsJsonArray();
            if (!arr.isJsonArray()) {
                return;
            }
            arr.forEach(element -> {
                JsonObject subobj = element.getAsJsonObject();
                String name = subobj.get("name").getAsString();
                Module m = Moon.INSTANCE.getModuleManager().getModule(name);
                if (m != null) {
                    m.load(subobj, key);
                }
            });
        }
        catch (FileNotFoundException fileNotFoundException) {
            // empty catch block
        }
    }

    public Config getPresetByName(String name) {
        for (Config p : this.configs) {
            if (!p.getName().equals(name)) continue;
            return p;
        }
        return null;
    }

    public Config getConfig(String name) {
        for (Config cfg : this.configs) {
            if (!cfg.getName().equalsIgnoreCase(name)) continue;
            return cfg;
        }
        return null;
    }

    public boolean isConfig(String name) {
        return this.getConfig(name) != null;
    }

    public File getDir() {
        return this.dir;
    }
}

