/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.FileUtils
 *  org.apache.commons.io.IOCase
 *  org.apache.commons.io.filefilter.IOFileFilter
 *  org.apache.commons.io.filefilter.WildcardFileFilter
 */
package me.moon.module.impl.visuals.hud.settingsscreen.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.moon.module.impl.visuals.hud.settingsscreen.Component;
import me.moon.module.impl.visuals.hud.settingsscreen.SettingsHud;
import me.moon.module.impl.visuals.hud.settingsscreen.impl.BooleanComponent;
import me.moon.module.impl.visuals.hud.settingsscreen.impl.NumberComponent;
import me.moon.module.impl.visuals.hud.settingsscreen.impl.StringComponent;
import me.moon.utils.MathUtils;
import me.moon.utils.font.MCFontRenderer;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.game.Printer;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.FontValue;
import me.moon.utils.value.impl.NumberValue;
import me.moon.utils.value.impl.StringValue;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class FontComponent
extends Component {
    private FontValue fontValue;
    private SettingsHud hud;
    private ArrayList<Component> components = new ArrayList();
    private List<Value> values = new ArrayList<Value>();
    private StringValue fontName = new StringValue("Font", "Verdana");
    private NumberValue<Integer> fontSize = new NumberValue<Integer>("Font Size", 18, 0, 40, 1);
    private NumberValue<Integer> fontType = new NumberValue<Integer>("Font Type", 0, 0, 2, 1);
    private BooleanValue antiAliasing = new BooleanValue("Anti Aliasing", true);
    private BooleanValue fractionalMetrics = new BooleanValue("Fractional Metrics", true);

    public FontComponent(SettingsHud hud, FontValue fontValue, float posX, float posY) {
        super(fontValue.getLabel(), posX, posY);
        this.fontValue = fontValue;
        this.hud = hud;
        this.initValues(this.fontName, this.fontSize, this.fontType, this.antiAliasing, this.fractionalMetrics);
        this.fontName.setValue(((MCFontRenderer)fontValue.getValue()).getFont().getName());
        this.fontSize.setValue(((MCFontRenderer)fontValue.getValue()).getFont().getSize());
        this.fontType.setValue(((MCFontRenderer)fontValue.getValue()).getFont().getStyle());
        this.antiAliasing.setValue(((MCFontRenderer)fontValue.getValue()).isAntiAlias());
        this.fractionalMetrics.setValue(((MCFontRenderer)fontValue.getValue()).isFractionalMetrics());
    }

    @Override
    public void init() {
        super.init();
        int y = (int)(this.getPosY() + 12.0f);
        for (Value value : this.values) {
            if (value instanceof BooleanValue) {
                BooleanValue booleanValue = (BooleanValue)value;
                this.components.add(new BooleanComponent(booleanValue, this.getPosX() + 5.0f, (float)y));
                y += 16;
            }
            if (value instanceof NumberValue) {
                NumberValue numberValue = (NumberValue)value;
                this.components.add(new NumberComponent(numberValue, this.getPosX() + 5.0f, (float)y));
                y += 16;
            }
            if (!(value instanceof StringValue)) continue;
            StringValue stringValue = (StringValue)value;
            this.components.add(new StringComponent(stringValue, this.getPosX() + 5.0f, (float)y));
            y += 16;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float pTicks) {
        RenderUtil.drawBorderedRect(this.getPosX(), this.getPosY() - 2.0f, 167.0, 104.0, 0.5, new Color(-11184811).getRGB(), 0xFFFFFF);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Font Settings", this.getPosX() + 2.0f, this.getPosY(), -1);
        RenderUtil.drawRect(this.getPosX() + 4.0f, this.getPosY() + 89.0f, Minecraft.getMinecraft().fontRendererObj.getStringWidth("Confirm") + 2, Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2, 0x67000000);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Confirm", this.getPosX() + 5.0f, this.getPosY() + 89.5f, -1);
        super.drawScreen(mouseX, mouseY, pTicks);
        if (this.hud.dragging) {
            float yLol = this.getPosY() + 12.0f;
            for (Component component2 : this.components) {
                component2.setPosY(yLol);
                component2.setPosX(this.getPosX() + 5.0f);
                yLol += 16.0f;
            }
        }
        this.components.forEach(component -> component.drawScreen(mouseX, mouseY, pTicks));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mButton) {
        this.components.forEach(component -> component.mouseClicked(mouseX, mouseY, mButton));
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX() + 4.0f, this.getPosY() + 89.0f, Minecraft.getMinecraft().fontRendererObj.getStringWidth("Confirm") + 2, Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2)) {
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
            if (fonts.contains(this.fontName.getValueAsString().toLowerCase())) {
                try {
                    FontValue fontValue = this.fontValue;
                    int style = (int)MathUtils.clamp(Integer.parseInt(this.fontType.getValueAsString()), 2.0f, 0.0f);
                    int n = Integer.parseInt(this.fontSize.getValueAsString());
                    boolean aa = Boolean.parseBoolean(this.antiAliasing.getValueAsString());
                    boolean fractionalmetrics = Boolean.parseBoolean(this.fractionalMetrics.getValueAsString());
                    MCFontRenderer mcFontRenderer = new MCFontRenderer(new Font(this.fontName.getValueAsString(), style, n), aa, fractionalmetrics);
                    fontValue.setValue(mcFontRenderer);
                    Printer.print("Set Font " + fontValue.getLabel() + " to " + ((MCFontRenderer)fontValue.getValue()).getFont().getName() + ", style " + ((MCFontRenderer)fontValue.getValue()).getFont().getStyle() + ", size " + ((MCFontRenderer)fontValue.getValue()).getFont().getSize() + ", antialiasing " + aa + ", fractionalmetrics " + fractionalmetrics + ".");
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.components.forEach(component -> component.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        this.components.forEach(component -> component.keyTyped(typedChar, keyCode));
    }

    public void initValues(Value ... values) {
        this.values.addAll(Arrays.asList(values));
    }
}

