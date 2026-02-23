/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  org.apache.commons.io.FileUtils
 *  org.apache.commons.io.IOCase
 *  org.apache.commons.io.filefilter.IOFileFilter
 *  org.apache.commons.io.filefilter.WildcardFileFilter
 */
package me.moon.module.impl.visuals.hud;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.moon.Moon;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.visuals.HUD;
import me.moon.utils.MathUtils;
import me.moon.utils.font.MCFontRenderer;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.FontValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public abstract class Component {
    public String componentName;
    public double x;
    public double y;
    public boolean isEnabled;
    private List<Value> valueList = new ArrayList<Value>();
    protected HUD hud = (HUD)Moon.INSTANCE.getModuleManager().getModule("HUD");
    protected Minecraft mc = Minecraft.getMinecraft();

    public Component(String name, double x, double y) {
        this.componentName = name;
        this.x = x;
        this.y = y;
    }

    public abstract void onCompRender(ScaledResolution var1);

    public abstract void onCompUpdate(UpdateEvent var1);

    public abstract void onInit();

    public void save(JsonObject directory) {
        directory.addProperty("x", (Number)this.x);
        directory.addProperty("y", (Number)this.y);
        directory.addProperty("enabled", Boolean.valueOf(this.isEnabled));
        this.valueList.forEach(val -> {
            if (val instanceof FontValue) {
                FontValue fontValue = (FontValue)val;
                directory.addProperty(fontValue.getLabel(), ((MCFontRenderer)fontValue.getValue()).getFont().getName() + " - " + ((MCFontRenderer)fontValue.getValue()).getFont().getSize() + " - " + ((MCFontRenderer)fontValue.getValue()).getFont().getStyle() + " - " + ((MCFontRenderer)fontValue.getValue()).isAntiAlias() + " - " + ((MCFontRenderer)fontValue.getValue()).isFractionalMetrics());
            } else {
                directory.addProperty(val.getLabel(), val.getValue().toString());
            }
        });
    }

    public void load(JsonObject directory) {
        directory.entrySet().forEach(data -> {
            switch ((String)data.getKey()) {
                case "name": {
                    return;
                }
                case "x": {
                    this.setX(((JsonElement)data.getValue()).getAsInt());
                    return;
                }
                case "y": {
                    this.setY(((JsonElement)data.getValue()).getAsInt());
                    return;
                }
                case "enabled": {
                    this.setEnabled(((JsonElement)data.getValue()).getAsBoolean());
                    return;
                }
                case "Color": {
                    Value val = this.searchCompValue((String)data.getKey());
                    val.setValue(((JsonElement)data.getValue()).getAsInt());
                    return;
                }
                case "Font": {
                    Value value = this.searchCompValue((String)data.getKey());
                    String valueString = ((JsonElement)data.getValue()).getAsString();
                    String fontName = valueString.split(" - ")[0];
                    String fontSize = valueString.split(" - ")[1];
                    String fontType = valueString.split(" - ")[2];
                    String antiAliasing = valueString.split(" - ")[3];
                    String fractionalMetrics = valueString.split(" - ")[4];
                    FontValue valFont = (FontValue)value;
                    ArrayList<String> fonts = new ArrayList<String>();
                    for (String string : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
                        fonts.add(string.toLowerCase());
                    }
                    File fontDirectory = new File(System.getenv("LOCALAPPDATA") + "\\Microsoft\\Windows\\Fonts");
                    for (File file : FileUtils.listFiles((File)fontDirectory, (IOFileFilter)new WildcardFileFilter("*.ttf", IOCase.INSENSITIVE), null)) {
                        try {
                            Font font = Font.createFont(0, file);
                            if (fonts.contains(font.getName().toLowerCase())) continue;
                            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
                            fonts.add(font.getName().toLowerCase());
                        }
                        catch (FontFormatException | IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                    if (fonts.contains(fontName.toLowerCase())) {
                        try {
                            FontValue fontValue = valFont;
                            int style = (int)MathUtils.clamp(Integer.parseInt(fontType), 2.0f, 0.0f);
                            int n = Integer.parseInt(fontSize);
                            boolean aa = Boolean.parseBoolean(antiAliasing);
                            boolean fractionalmetrics = Boolean.parseBoolean(fractionalMetrics);
                            MCFontRenderer mcFontRenderer = new MCFontRenderer(new Font(fontName, style, n), aa, fractionalmetrics);
                            fontValue.setValue(mcFontRenderer);
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                    }
                    return;
                }
            }
            Value val = this.searchCompValue((String)data.getKey());
            val.setValue(((JsonElement)data.getValue()).getAsString());
        });
    }

    public Value searchCompValue(String valueName) {
        for (Value value : this.valueList) {
            if (!value.getLabel().equalsIgnoreCase(valueName)) continue;
            return value;
        }
        return null;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public String getComponentName() {
        return this.componentName;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public List<Value> getValueList() {
        return this.valueList;
    }

    public void initValues(Value ... values) {
        this.valueList.addAll(Arrays.asList(values));
    }
}

