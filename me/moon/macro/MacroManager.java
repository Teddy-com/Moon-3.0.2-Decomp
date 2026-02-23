/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.reflect.TypeToken
 */
package me.moon.macro;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.input.KeyInputEvent;
import me.moon.macro.Macro;
import net.minecraft.client.Minecraft;

public class MacroManager {
    private Map<String, Macro> macros = new HashMap<String, Macro>();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private File macroFile;

    public MacroManager(File directory) {
        this.macroFile = new File(directory.toString() + File.separator + "macros.json");
        this.init();
    }

    public void init() {
        Moon.INSTANCE.getEventBus().registerListener(this);
        try {
            if (!this.macroFile.exists()) {
                this.macroFile.createNewFile();
                this.save();
                return;
            }
            this.load();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    @Handler(value=KeyInputEvent.class)
    public void onKey(KeyInputEvent event) {
        if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null) {
            return;
        }
        this.getMacros().values().forEach(macro -> {
            if (macro.getKey() == event.getKey()) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage(macro.getText());
            }
        });
    }

    public void save() {
        if (this.macroFile.exists()) {
            try (PrintWriter writer = new PrintWriter(this.macroFile);){
                writer.print(this.GSON.toJson(this.getMacros()));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public void load() {
        try (FileReader inFile = new FileReader(this.macroFile);){
            this.setMacros((Map)this.GSON.fromJson((Reader)inFile, new TypeToken<Map<String, Macro>>(){}.getType()));
            if (this.getMacros() == null) {
                this.setMacros(new HashMap<String, Macro>());
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void removeMacroByLabel(String label) {
        this.macros.remove(label.toLowerCase());
    }

    public Macro getMacro(String label) {
        return this.macros.get(label.toLowerCase());
    }

    public Macro getMacroByKey(int key) {
        for (Macro macro : this.macros.values()) {
            if (macro.getKey() != key) continue;
            return macro;
        }
        return null;
    }

    public File getMacroFile() {
        return this.macroFile;
    }

    public void clearMacros() {
        this.macros.clear();
    }

    public void addMacro(String label, int key, String text) {
        this.macros.put(label.toLowerCase(), new Macro(label, key, text));
    }

    public boolean isMacro(String label) {
        return this.getMacro(label) != null;
    }

    public void setMacros(Map<String, Macro> macros) {
        this.macros = macros;
    }

    public Map<String, Macro> getMacros() {
        return this.macros;
    }
}

