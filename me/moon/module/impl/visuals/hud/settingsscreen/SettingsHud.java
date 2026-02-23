/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 */
package me.moon.module.impl.visuals.hud.settingsscreen;

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
import me.moon.Moon;
import me.moon.module.impl.visuals.hud.Component;
import me.moon.module.impl.visuals.hud.editscreen.GuiHudEditScreen;
import me.moon.module.impl.visuals.hud.settingsscreen.impl.BooleanComponent;
import me.moon.module.impl.visuals.hud.settingsscreen.impl.ColorComponent;
import me.moon.module.impl.visuals.hud.settingsscreen.impl.EnumComponent;
import me.moon.module.impl.visuals.hud.settingsscreen.impl.FontComponent;
import me.moon.module.impl.visuals.hud.settingsscreen.impl.NumberComponent;
import me.moon.module.impl.visuals.hud.settingsscreen.impl.StringComponent;
import me.moon.utils.blur.MCBlurUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.ColorValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.FontValue;
import me.moon.utils.value.impl.NumberValue;
import me.moon.utils.value.impl.StringValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class SettingsHud
extends GuiScreen {
    private Component hudComp;
    private ArrayList<me.moon.module.impl.visuals.hud.settingsscreen.Component> components = new ArrayList();
    private float widthComp = 200.0f;
    private float heightComp = 300.0f;
    private float x = 2.0f;
    private float y = 2.0f;
    private float lastX;
    private float lastY;
    public boolean dragging = false;
    public File directory;

    public SettingsHud(Component hudComp) {
        this.hudComp = hudComp;
    }

    public File getDirectory() {
        return this.directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.components.clear();
        this.loadCoord();
        int y = (int)(this.y + 20.0f);
        for (Value value : this.hudComp.getValueList()) {
            if (value instanceof EnumValue) {
                EnumValue enumValue = (EnumValue)value;
                this.components.add(new EnumComponent(enumValue, this.x + 5.0f, (float)y));
                y += 16;
            }
            if (value instanceof BooleanValue) {
                BooleanValue booleanValue = (BooleanValue)value;
                this.components.add(new BooleanComponent(booleanValue, this.x + 5.0f, (float)y));
                y += 16;
            }
            if (value instanceof NumberValue) {
                NumberValue numberValue = (NumberValue)value;
                this.components.add(new NumberComponent(numberValue, this.x + 5.0f, (float)y));
                y += 16;
            }
            if (value instanceof StringValue) {
                StringValue stringValue = (StringValue)value;
                this.components.add(new StringComponent(stringValue, this.x + 5.0f, (float)y));
                y += 16;
            }
            if (value instanceof ColorValue) {
                ColorValue colorValue = (ColorValue)value;
                this.components.add(new ColorComponent(colorValue, this.x + 5.0f, (float)y));
                y += 16;
            }
            if (!(value instanceof FontValue)) continue;
            FontValue fontValue = (FontValue)value;
            this.components.add(new FontComponent(this, fontValue, this.x + 5.0f, y));
            y += 106;
        }
        this.components.forEach(component -> component.init());
    }

    public static int getColor3(int brightness) {
        return SettingsHud.getColor6(brightness, brightness, brightness, 255);
    }

    public static int getColor6(int red, int green, int blue, int alpha) {
        int color = alpha << 24;
        color |= red << 16;
        color |= green << 8;
        return color |= blue;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        if (!this.mc.gameSettings.ofFastRender) {
            MCBlurUtil.drawBLURRRR(0, 0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), 5.0f);
        }
        RenderUtil.drawBorderedRect(this.x, this.y, this.widthComp, this.heightComp, 1.0, SettingsHud.getColor3(61), SettingsHud.getColor3(91));
        RenderUtil.drawBorderedRect(this.x + 1.0f, this.y + 1.0f, this.widthComp - 2.0f, this.heightComp - 2.0f, 1.0, SettingsHud.getColor3(90), SettingsHud.getColor3(61));
        RenderUtil.drawBorderedRect((double)this.x + 2.5, (double)this.y + 2.5, (double)this.widthComp - 5.0, (double)this.heightComp - 5.0, 0.5, SettingsHud.getColor3(61), SettingsHud.getColor3(0));
        RenderUtil.drawBorderedRect(this.x + 3.0f, this.y + 3.0f, this.widthComp - 6.0f, this.heightComp - 6.0f, 0.5, SettingsHud.getColor3(21), SettingsHud.getColor3(35));
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("x", (float)((double)(this.x + this.widthComp / 2.0f) + 88.5), this.y + 5.0f, -1);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("x", this.x + this.widthComp / 2.0f + 86.0f, this.y + this.heightComp - 15.0f, -65536);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.hudComp.getComponentName().replaceAll("Comp", "") + " Settings", this.x + this.widthComp / 2.0f - 95.0f, this.y + 5.0f, -1);
        this.components.forEach(component -> component.drawScreen(mouseX, mouseY, partialTicks));
        if (this.dragging) {
            this.x = (float)mouseX + this.lastX;
            this.y = (float)mouseY + this.lastY;
            float yLol = this.y + 20.0f;
            for (me.moon.module.impl.visuals.hud.settingsscreen.Component component2 : this.components) {
                component2.setPosY(yLol);
                component2.setPosX(this.x + 5.0f);
                if (component2 instanceof FontComponent) {
                    yLol += 106.0f;
                    continue;
                }
                yLol += 16.0f;
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        this.components.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
        if (this.mouseWithinBounds(mouseX, mouseY, this.x + this.widthComp / 2.0f + 86.0f, this.y + 5.0f, 10.0, 10.0)) {
            this.mc.displayGuiScreen(new GuiHudEditScreen());
        }
        if (this.mouseWithinBounds(mouseX, mouseY, this.x + this.widthComp / 2.0f + 86.0f, this.y + this.heightComp - 15.0f, 10.0, 10.0)) {
            Moon.INSTANCE.getComponentManager().getComponentMap().remove(this.hudComp.componentName, this.hudComp);
            Moon.INSTANCE.getComponentManager().saveComps();
            this.mc.displayGuiScreen(new GuiHudEditScreen());
        }
        if (this.mouseWithinBounds(mouseX, mouseY, this.x, this.y, this.widthComp, 10.0)) {
            this.dragging = true;
            this.lastX = this.x - (float)mouseX;
            this.lastY = this.y - (float)mouseY;
            this.saveCoord();
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.dragging = false;
        this.components.forEach(component -> component.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.components.forEach(component -> component.keyTyped(typedChar, keyCode));
    }

    public boolean mouseWithinBounds(int mouseX, int mouseY, double x, double y, double widthComp, double heightComp) {
        return (double)mouseX >= x && (double)mouseX <= x + widthComp && (double)mouseY >= y && (double)mouseY <= y + heightComp;
    }

    public void saveCoord() {
        File[] files = this.directory.listFiles();
        if (!this.directory.exists()) {
            this.directory.mkdir();
        } else if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        File file = new File(this.directory, "menu.json");
        JsonObject node = new JsonObject();
        node.addProperty("x", (Number)Float.valueOf(this.x));
        node.addProperty("y", (Number)Float.valueOf(this.y));
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
        files = this.directory.listFiles();
        if (files == null || files.length == 0) {
            this.directory.delete();
        }
    }

    public void loadCoord() {
        File file = new File(this.directory, "menu.json");
        if (!file.exists()) {
            return;
        }
        try (FileReader reader = new FileReader(file);){
            JsonElement node = new JsonParser().parse((Reader)reader);
            if (!node.isJsonObject()) {
                return;
            }
            node.getAsJsonObject().entrySet().forEach(data -> {
                switch ((String)data.getKey()) {
                    case "x": {
                        this.x = ((JsonElement)data.getValue()).getAsInt();
                        return;
                    }
                    case "y": {
                        this.y = ((JsonElement)data.getValue()).getAsInt();
                        return;
                    }
                }
            });
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }
}

