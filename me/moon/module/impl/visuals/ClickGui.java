/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals;

import java.awt.Color;
import me.moon.gui.discordclickgui.DiscordClickGUI;
import me.moon.gui.materialui.MaterialUI;
import me.moon.gui.moderngui.ModernClickGui;
import me.moon.gui.othergui.OtherClickGUI;
import me.moon.module.Module;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.ColorValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.Minecraft;

public class ClickGui
extends Module {
    public OtherClickGUI newClickGui = null;
    public MaterialUI materialGUI = null;
    public DiscordClickGUI discordClickGUI = null;
    public ModernClickGui modernClickGui = null;
    public EnumValue<Mode> mode = new EnumValue<Mode>("Mode", Mode.MATERIAL);
    public BooleanValue noScroll = new BooleanValue("No Scrolling", true, (Value)this.mode, "Modern");
    public final ColorValue color = new ColorValue("Color", new Color(37, 37, 37).getRGB());
    public final NumberValue<Integer> transparency = new NumberValue<Integer>("Transparency", 255, 1, 255, 1);
    public final NumberValue<Integer> blurRadius = new NumberValue<Integer>("Blur Radius", 20, 0, 200, 1, this.mode, "Material");

    public ClickGui() {
        super("ClickGUI", Module.Category.VISUALS, -1);
        this.setHidden(true);
        this.setKeyBind(54);
    }

    @Override
    public void onDisable() {
        if (Minecraft.getMinecraft().entityRenderer.getShaderGroup() != null) {
            Minecraft.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
        }
        super.onDisable();
    }

    @Override
    public void onEnable() {
        if (this.mc.theWorld != null) {
            switch ((Mode)((Object)this.mode.getValue())) {
                case MATERIAL: {
                    if (this.materialGUI == null) {
                        this.materialGUI = new MaterialUI();
                        this.materialGUI.initializedUI();
                    }
                    this.mc.displayGuiScreen(this.materialGUI);
                    break;
                }
                case MODERN: {
                    if (this.modernClickGui == null) {
                        this.modernClickGui = new ModernClickGui();
                        this.modernClickGui.init();
                    }
                    this.mc.displayGuiScreen(this.modernClickGui);
                    break;
                }
                default: {
                    if (this.newClickGui == null) {
                        this.newClickGui = new OtherClickGUI();
                        this.newClickGui.init();
                    }
                    this.mc.displayGuiScreen(this.newClickGui);
                }
            }
        }
        this.toggle();
    }

    public void callClickGui() {
        switch ((Mode)((Object)this.mode.getValue())) {
            case MATERIAL: {
                if (this.materialGUI == null) {
                    this.materialGUI = new MaterialUI();
                    this.materialGUI.initializedUI();
                }
                this.mc.displayGuiScreen(this.materialGUI);
                break;
            }
            case MODERN: {
                if (this.modernClickGui == null) {
                    this.modernClickGui = new ModernClickGui();
                    this.modernClickGui.init();
                }
                this.mc.displayGuiScreen(this.modernClickGui);
                break;
            }
            default: {
                if (this.newClickGui == null) {
                    this.newClickGui = new OtherClickGUI();
                    this.newClickGui.init();
                }
                this.mc.displayGuiScreen(this.newClickGui);
            }
        }
    }

    @Override
    public int getColor() {
        return new Color(35, 35, 35).getRGB();
    }

    public DiscordClickGUI getDiscordClickGUI() {
        return this.discordClickGUI;
    }

    private static enum Mode {
        MATERIAL("Material"),
        DROPDOWN("Dropdown"),
        MODERN("Modern");

        private final String name;

        private Mode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

