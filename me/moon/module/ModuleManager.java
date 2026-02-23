/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 */
package me.moon.module;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.input.KeyInputEvent;
import me.moon.module.Module;
import me.moon.utils.font.FontUtil;
import net.minecraft.client.gui.Gui;

public class ModuleManager {
    public static final Map<String, Module> moduleHashMap = new HashMap<String, Module>();
    private File dir;

    public void initializeModules() {
        Moon.INSTANCE.getEventBus().registerListener(this);
    }

    @Handler(value=KeyInputEvent.class)
    public void onKeyInput(KeyInputEvent event) {
        for (Module module : moduleHashMap.values()) {
            if (module.getKeyBind() != event.getKey()) continue;
            module.setEnabled(!module.isEnabled());
        }
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public Map<String, Module> getMap() {
        return moduleHashMap;
    }

    public File getDir() {
        return this.dir;
    }

    public Map<String, Module> getModuleMap() {
        return moduleHashMap;
    }

    public boolean isModule(String moduleName) {
        for (Module mod : this.getModuleMap().values()) {
            if (!mod.getLabel().equalsIgnoreCase(moduleName)) continue;
            return true;
        }
        return false;
    }

    public Module getModuleClass(Class<?> moduleClass) {
        for (Module mod : this.getModuleMap().values()) {
            if (!mod.getClass().equals(moduleClass)) continue;
            return mod;
        }
        return null;
    }

    public ArrayList<Module> getModulesInCategory(Module.Category category) {
        ArrayList<Module> mods = new ArrayList<Module>();
        for (Module module : moduleHashMap.values()) {
            if (module.getCategory() != category) continue;
            mods.add(module);
        }
        return mods;
    }

    public float getLongestModInCategory(Module.Category category) {
        ArrayList modules = (ArrayList)this.getModuleMap().values().stream().filter(module -> module.getCategory() == category).collect(Collectors.toList());
        modules.sort(Comparator.comparingDouble(module -> -FontUtil.getStringWidth(module.getLabel())));
        return FontUtil.getStringWidth(((Module)modules.get(0)).getLabel());
    }

    public Module getModule(String name) {
        return this.getModuleMap().get(name.toLowerCase());
    }

    public void saveModules() {
        File[] files = this.dir.listFiles();
        if (!this.dir.exists()) {
            this.dir.mkdir();
        } else if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        this.getModuleMap().values().forEach(module -> {
            File file = new File(this.dir, module.getLabel() + ".json");
            JsonObject node = new JsonObject();
            module.save(node, true);
            if (node.entrySet().isEmpty()) {
                return;
            }
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                return;
            }
            try (FileWriter writer = new FileWriter(file);){
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)node));
            }
            catch (IOException e) {
                file.delete();
            }
        });
        files = this.dir.listFiles();
        if (files == null || files.length == 0) {
            this.dir.delete();
        }
    }

    public void loadModules() {
        this.getModuleMap().values().forEach(module -> {
            File file = new File(this.dir, module.getLabel() + ".json");
            if (!file.exists()) {
                return;
            }
            try (FileReader reader = new FileReader(file);){
                JsonElement node = new JsonParser().parse((Reader)reader);
                if (!node.isJsonObject() || !Gui.protectionIsPassed) {
                    return;
                }
                module.load(node.getAsJsonObject(), true);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        });
    }
}

