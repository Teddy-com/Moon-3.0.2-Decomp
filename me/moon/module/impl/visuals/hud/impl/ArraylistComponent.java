/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.moon.module.impl.visuals.hud.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Comparator;
import me.moon.Moon;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.module.impl.visuals.hud.Component;
import me.moon.module.impl.visuals.hud.editscreen.GuiHudEditScreen;
import me.moon.utils.font.MCFontRenderer;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.ColorValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.FontValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class ArraylistComponent
extends Component {
    public final BooleanValue sorting = new BooleanValue("Arraylist sorting", true);
    public final BooleanValue useFont = new BooleanValue("Use font", true);
    public final BooleanValue background = new BooleanValue("Background", true);
    public final BooleanValue sideRect = new BooleanValue("Side Rect", true);
    public final EnumValue<colorMode> colors = new EnumValue<colorMode>("Color Mode", colorMode.White);
    public final NumberValue yAddition = new NumberValue<Double>("Y-Addition", 0.0, 0.0, 5.0, 0.5);
    public final FontValue fontValue = new FontValue("Font", new MCFontRenderer(new Font("Verdana", 0, 18), true, false));
    public final ColorValue colorXD = new ColorValue("Color", -1);

    public ArraylistComponent(String name) {
        super(name, 5.0, 5.0);
        this.initValues(this.sorting, this.useFont, this.background, this.sideRect, this.fontValue, this.yAddition, this.colors, this.colorXD);
    }

    @Override
    public void onCompRender(ScaledResolution sr) {
        GlStateManager.bindTexture(0);
        ArrayList<Module> modules = new ArrayList<Module>(Moon.INSTANCE.getModuleManager().getModuleMap().values());
        if (this.sorting.getValue().booleanValue()) {
            if (this.useFont.getValue().booleanValue()) {
                modules.sort(Comparator.comparingDouble(module -> -((MCFontRenderer)this.fontValue.getValue()).getStringWidth(this.getModuleString((Module)module))));
            } else {
                modules.sort(Comparator.comparingDouble(module -> -this.mc.fontRendererObj.getStringWidth(this.getModuleString((Module)module))));
            }
        }
        modules.removeIf(module -> !module.isEnabled());
        float posY = (float)(this.getY() + 2.0);
        for (Module module2 : modules) {
            float posX;
            String renderString = this.getModuleString(module2);
            if (module2.isShouldAnimate()) {
                module2.getCustomAnimation().setPosY(posY);
                module2.setShouldAnimate(false);
            }
            if (this.useFont.getValue().booleanValue()) {
                posX = this.getX() > (double)((float)sr.getScaledWidth() / 2.0f) ? (float)this.getX() - (float)((MCFontRenderer)this.fontValue.getValue()).getStringWidth(renderString) + (float)((MCFontRenderer)this.fontValue.getValue()).getStringWidth(this.getComponentName()) : (float)this.getX();
            } else {
                float f = posX = this.getX() > (double)((float)sr.getScaledWidth() / 2.0f) ? (float)this.getX() - (float)this.mc.fontRendererObj.getStringWidth(renderString) + (float)this.mc.fontRendererObj.getStringWidth(this.getComponentName()) : (float)this.getX();
            }
            if (GuiHudEditScreen.isDragging()) {
                module2.getCustomAnimation().setPosY(posY);
            }
            int color = -1;
            switch ((colorMode)((Object)this.colors.getValue())) {
                case White: {
                    color = -1;
                    break;
                }
                case Rainbow: {
                    color = RenderUtil.getRainbow(4500, (int)(-(posY * 15.0f)), 0.5f);
                    break;
                }
                case Gray: {
                    color = -5592406;
                    break;
                }
                case Astolfo: {
                    color = RenderUtil.getGradient((double)(-(posY / (float)((MCFontRenderer)this.fontValue.getValue()).getHeight() / 13.0f)) + (double)Math.abs(System.currentTimeMillis()) / 1000.0, new Color(243, 111, 122), new Color(142, 95, 255), new Color(55, 230, 239)).getRGB();
                    break;
                }
                case AstolfoOld: {
                    color = RenderUtil.getGradientOffset(new Color(255, 60, 145), new Color(27, 179, 255), (double)(-(posY / (float)((MCFontRenderer)this.fontValue.getValue()).getHeight() / 13.0f)) + (double)Math.abs(System.currentTimeMillis()) / 1000.0).getRGB();
                    break;
                }
                case Fading: {
                    color = this.color(Float.valueOf((posY + 1.0f) * 0.88f), this.colorXD.getColor().getRed(), this.colorXD.getColor().getGreen(), this.colorXD.getColor().getBlue());
                }
            }
            if (this.useFont.getValue().booleanValue()) {
                if (this.background.getValue().booleanValue()) {
                    RenderUtil.drawRect(posX - 1.0f, module2.getCustomAnimation().getPosY() - 1.0, ((MCFontRenderer)this.fontValue.getValue()).getStringWidth(renderString) + 3, ((MCFontRenderer)this.fontValue.getValue()).getHeight() + 2, 0x67000000);
                }
                if (this.sideRect.getValue().booleanValue()) {
                    RenderUtil.drawRect(posX + (float)((MCFontRenderer)this.fontValue.getValue()).getStringWidth(renderString) + 2.0f, module2.getCustomAnimation().getPosY() - 1.0, 1.0, ((MCFontRenderer)this.fontValue.getValue()).getHeight() + 2, color);
                }
                ((MCFontRenderer)this.fontValue.getValue()).drawStringWithShadow(renderString, posX, module2.getCustomAnimation().getPosY() + ((Number)this.yAddition.getValue()).doubleValue(), color);
            } else {
                if (this.background.getValue().booleanValue()) {
                    RenderUtil.drawRect(posX - 1.0f, module2.getCustomAnimation().getPosY() - 1.0, this.mc.fontRendererObj.getStringWidth(renderString) + 3, this.mc.fontRendererObj.FONT_HEIGHT + 2, 0x67000000);
                }
                if (this.sideRect.getValue().booleanValue()) {
                    RenderUtil.drawRect(posX + (float)this.mc.fontRendererObj.getStringWidth(renderString) + 2.0f, module2.getCustomAnimation().getPosY() - 1.0, 1.0, this.mc.fontRendererObj.FONT_HEIGHT + 2, color);
                }
                this.mc.fontRendererObj.drawStringWithShadow(renderString, posX, (float)((double)((float)module2.getCustomAnimation().getPosY()) + ((Number)this.yAddition.getValue()).doubleValue()), color);
            }
            module2.getCustomAnimation().interpolate(0.0, posY, 14.0f / (float)Minecraft.getDebugFPS());
            if (this.useFont.getValue().booleanValue()) {
                if (this.getY() > (double)((float)sr.getScaledHeight() / 2.0f)) {
                    posY -= (float)(((MCFontRenderer)this.fontValue.getValue()).getHeight() + 2);
                    continue;
                }
                posY += (float)(((MCFontRenderer)this.fontValue.getValue()).getHeight() + 2);
                continue;
            }
            if (this.getY() > (double)((float)sr.getScaledHeight() / 2.0f)) {
                posY -= (float)(this.mc.fontRendererObj.FONT_HEIGHT + 2);
                continue;
            }
            posY += (float)(this.mc.fontRendererObj.FONT_HEIGHT + 2);
        }
    }

    @Override
    public void onCompUpdate(UpdateEvent event) {
    }

    @Override
    public void onInit() {
    }

    public int color(Float offset, int red, int green, int blue) {
        double timer = (float)(System.currentTimeMillis() % 1700L) / 850.0f;
        float[] hsb = new float[3];
        Color.RGBtoHSB(red, green, blue, hsb);
        float brightness = (float)((double)hsb[2] * Math.abs(((double)offset.floatValue() + timer) % 1.0 - (double)0.55f) + (double)0.45f);
        return Color.HSBtoRGB(hsb[0], hsb[1], brightness);
    }

    private float getOffset() {
        return (float)(System.currentTimeMillis() % 2000L) / 1000.0f;
    }

    public String getModuleString(Module module) {
        StringBuilder moduleString = new StringBuilder(module.getRenderLabel() != null ? module.getRenderLabel() : module.getLabel());
        if (module.getSuffix() != null) {
            moduleString.append(ChatFormatting.GRAY).append(" ").append(module.getSuffix());
        }
        return moduleString.toString();
    }

    public static enum colorMode {
        White,
        Gray,
        Rainbow,
        Astolfo,
        AstolfoOld,
        Fading;

    }
}

