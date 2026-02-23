/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.font;

import me.moon.module.impl.visuals.HUD;
import me.moon.utils.font.Fonts;
import me.moon.utils.font.MCFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class FontUtil {
    private static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    private static MCFontRenderer customFont = Fonts.moonThemeSF;

    public static float getStringWidth(String text) {
        if (FontUtil.useFont()) {
            return customFont.getStringWidth(text);
        }
        return fontRenderer.getStringWidth(text);
    }

    public static float getHeight() {
        if (FontUtil.useFont()) {
            return customFont.getHeight();
        }
        return FontUtil.fontRenderer.FONT_HEIGHT;
    }

    public static float getStringHeight(String text) {
        if (FontUtil.useFont()) {
            return customFont.getStringHeight(text);
        }
        return FontUtil.fontRenderer.FONT_HEIGHT;
    }

    public static void drawShadowedString(String text, float x, float y, int color) {
        if (FontUtil.useFont()) {
            customFont.drawStringWithShadow(text, x, y, color);
        } else {
            fontRenderer.drawStringWithShadow(text, x, y, color);
        }
    }

    public static void drawString(String text, float x, float y, int color) {
        if (FontUtil.useFont()) {
            customFont.drawString(text, x, y, color);
        } else {
            fontRenderer.drawString(text, x, y, color);
        }
    }

    public static boolean useFont() {
        return HUD.fontForTabGUI.getValue();
    }
}

