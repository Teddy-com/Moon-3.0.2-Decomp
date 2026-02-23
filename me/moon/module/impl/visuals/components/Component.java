/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.components;

import java.awt.Color;
import me.moon.event.impl.input.KeyInputEvent;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.impl.visuals.HUD;
import me.moon.utils.font.Fonts;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;

public class Component {
    protected Minecraft mc = Minecraft.getMinecraft();

    public void init() {
    }

    public void render(Render2DEvent event) {
    }

    public void keyPress(KeyInputEvent event) {
    }

    public void blur() {
    }

    public void bloom() {
    }

    public void update() {
    }

    public int getColor() {
        int color = -1;
        switch ((HUD.ColorModes)((Object)HUD.colorModes.getValue())) {
            case ASTOLFO: {
                color = RenderUtil.getGradient((double)(-(2.0f / (float)Fonts.novolineFont.getHeight() / 13.0f)) + (double)Math.abs(System.currentTimeMillis()) / 1000.0, new Color(243, 111, 122), new Color(142, 95, 255), new Color(55, 230, 239)).getRGB();
                break;
            }
            case MOON: {
                color = RenderUtil.getGradientOffset(new Color(2, 163, 246), new Color(21, 70, 193), (double)(-(2.0f / (float)Fonts.novolineFont.getHeight() / 13.0f)) + (double)Math.abs(System.currentTimeMillis()) / 1000.0).getRGB();
                break;
            }
            case RAINBOW: {
                color = RenderUtil.getRainbow(4500, (int)((double)((int)(-(2.0f / (float)Fonts.novolineFont.getHeight() / 13.0f))) + (double)Math.abs(System.currentTimeMillis()) / 1000.0), 0.5f);
                break;
            }
            case STATICFADE: {
                color = this.color(Float.valueOf((float)((double)(-(2.0f / (float)Fonts.novolineFont.getHeight() / 13.0f)) + (double)Math.abs(System.currentTimeMillis()) / 1000.0)), HUD.colorValue.getColor().getRed(), HUD.colorValue.getColor().getGreen(), HUD.colorValue.getColor().getBlue());
                break;
            }
            case CHRISTMAS: {
                color = RenderUtil.getGradientOffset(new Color(255, 69, 69), new Color(255, 255, 255), (double)(-(2.0f / (float)Fonts.novolineFont.getHeight() / 13.0f)) + (double)Math.abs(System.currentTimeMillis()) / 1000.0).getRGB();
                break;
            }
            case CUSTOM: {
                color = (Integer)HUD.colorValue.getValue();
            }
        }
        return color;
    }

    public int color(Float offset, int red, int green, int blue) {
        double timer = (float)(System.currentTimeMillis() % 1700L) / 850.0f;
        float[] hsb = new float[3];
        Color.RGBtoHSB(red, green, blue, hsb);
        float brightness = (float)((double)hsb[2] * Math.abs(((double)offset.floatValue() + timer) % 1.0 - (double)0.55f) + (double)0.45f);
        return Color.HSBtoRGB(hsb[0], hsb[1], brightness);
    }
}

