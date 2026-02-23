/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 */
package me.moon.module.impl.visuals.hud;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.event.impl.render.ResizeEvent;
import me.moon.module.impl.visuals.HUD;
import me.moon.module.impl.visuals.hud.Component;
import me.moon.module.impl.visuals.hud.editscreen.GuiHudEditScreen;
import me.moon.module.impl.visuals.hud.impl.ArmorComponent;
import me.moon.module.impl.visuals.hud.impl.ArraylistComponent;
import me.moon.module.impl.visuals.hud.impl.PotionComponent;
import me.moon.module.impl.visuals.hud.impl.RadarComponent;
import me.moon.module.impl.visuals.hud.impl.ScoreboardComponent;
import me.moon.module.impl.visuals.hud.impl.StringComponent;
import me.moon.module.impl.visuals.hud.impl.TabGuiComponent;
import me.moon.module.impl.visuals.hud.impl.TargetHUDComponent;
import net.minecraft.client.Minecraft;

public class ComponentManager {
    private Map<String, Component> componentMap = new HashMap<String, Component>();
    private File directory;
    private int lastDisplayWidth;
    private int lastDislayHeight;
    private Minecraft mc = Minecraft.getMinecraft();

    public void initComponents() {
        this.componentMap.clear();
        Moon.INSTANCE.getEventBus().unregisterListener(this);
        if (this.directory.list() != null) {
            for (String s : Objects.requireNonNull(this.directory.list())) {
                if (s.contains("String")) {
                    this.initComp(new StringComponent(s.replaceAll(".json", "")));
                    continue;
                }
                if (s.contains("Arraylist")) {
                    this.initComp(new ArraylistComponent(s.replaceAll(".json", "")));
                    continue;
                }
                if (s.contains("Scoreboard")) {
                    this.initComp(new ScoreboardComponent(s.replaceAll(".json", "")));
                    continue;
                }
                if (s.contains("PotionHUD")) {
                    this.initComp(new PotionComponent(s.replaceAll(".json", "")));
                    continue;
                }
                if (s.contains("ArmorHUD")) {
                    this.initComp(new ArmorComponent(s.replaceAll(".json", "")));
                    continue;
                }
                if (s.contains("TargetHUD")) {
                    this.initComp(new TargetHUDComponent(s.replaceAll(".json", "")));
                    continue;
                }
                if (s.contains("Radar")) {
                    this.initComp(new RadarComponent(s.replaceAll(".json", "")));
                    continue;
                }
                if (!s.contains("TabGui")) continue;
                this.initComp(new TabGuiComponent(s.replaceAll(".json", "")));
            }
        }
        if (!this.directory.exists()) {
            this.directory.mkdirs();
        }
        Moon.INSTANCE.getComponentManager().loadComps();
        Moon.INSTANCE.getEventBus().registerListener(this);
    }

    public void initComp(Component component) {
        this.componentMap.put(component.componentName, component);
        component.onInit();
    }

    @Handler(value=Render2DEvent.class)
    public void onRender(Render2DEvent event) {
        if (this.mc.gameSettings.showDebugInfo) {
            return;
        }
        HUD hud = (HUD)Moon.INSTANCE.getModuleManager().getModule("hud");
        if (!Minecraft.getMinecraft().gameSettings.showDebugInfo) {
            for (Component component : this.componentMap.values()) {
                if (Minecraft.getMinecraft().currentScreen instanceof GuiHudEditScreen || !component.isEnabled()) continue;
                if (HUD.modes.getValue() != HUD.hudModes.CUSTOM) continue;
                component.onCompRender(event.getScaledResolution());
            }
        }
    }

    @Handler(value=ResizeEvent.class)
    public void onResize(ResizeEvent event) {
        int height = event.getHeight();
        int width = event.getWidth();
        if (event.isPre()) {
            this.lastDislayHeight = height;
            this.lastDisplayWidth = width;
        } else {
            for (Component component : this.componentMap.values()) {
                double d;
                if ((double)((float)this.lastDisplayWidth / 2.0f) - component.getX() < 50.0) {
                    d = (double)((float)this.lastDisplayWidth / 2.0f) - component.getX();
                }
                if (!((double)((float)this.lastDislayHeight / 2.0f) - component.getY() < 50.0)) continue;
                d = (double)((float)this.lastDislayHeight / 2.0f) - component.getY();
            }
        }
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (this.mc.gameSettings.showDebugInfo) {
            return;
        }
        HUD hud = (HUD)Moon.INSTANCE.getModuleManager().getModule("hud");
        for (Component component : this.componentMap.values()) {
            if (HUD.modes.getValue() != HUD.hudModes.CUSTOM) continue;
            component.onCompUpdate(event);
        }
    }

    public void saveComps() {
        if (this.componentMap.values().isEmpty()) {
            this.directory.delete();
        }
        File[] files = this.directory.listFiles();
        if (!this.directory.exists()) {
            this.directory.mkdir();
        } else if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        this.componentMap.values().forEach(comp -> {
            File file = new File(this.directory, comp.getComponentName() + ".json");
            JsonObject node = new JsonObject();
            comp.save(node);
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
        files = this.directory.listFiles();
        if (files == null || files.length == 0) {
            this.directory.delete();
        }
    }

    public void loadComps() {
        this.componentMap.values().forEach(comp -> {
            File file = new File(this.directory, comp.getComponentName() + ".json");
            if (!file.exists()) {
                return;
            }
            try (FileReader reader = new FileReader(file);){
                JsonElement node = new JsonParser().parse((Reader)reader);
                if (!node.isJsonObject()) {
                    return;
                }
                comp.load(node.getAsJsonObject());
            }
            catch (IOException iOException) {
                // empty catch block
            }
        });
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public File getDirectory() {
        return this.directory;
    }

    public Map<String, Component> getComponentMap() {
        return this.componentMap;
    }
}

