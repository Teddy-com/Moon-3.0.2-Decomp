/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.utils.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class GLUtil {
    private static Map<Integer, Boolean> glCapMap = new HashMap<Integer, Boolean>();

    public static void setGLCap(int cap, boolean flag) {
        glCapMap.put(cap, GL11.glGetBoolean((int)cap));
        if (flag) {
            GL11.glEnable((int)cap);
        } else {
            GL11.glDisable((int)cap);
        }
    }

    public static void revertGLCap(int cap) {
        Boolean origCap = glCapMap.get(cap);
        if (origCap != null) {
            if (origCap.booleanValue()) {
                GL11.glEnable((int)cap);
            } else {
                GL11.glDisable((int)cap);
            }
        }
    }

    public static void glEnable(int cap) {
        GLUtil.setGLCap(cap, true);
    }

    public static void glDisable(int cap) {
        GLUtil.setGLCap(cap, false);
    }

    public static void revertAllCaps() {
        for (int cap : glCapMap.keySet()) {
            GLUtil.revertGLCap(cap);
        }
    }

    public static void setColor(int n) {
        GLUtil.setColor(new Color(n, true));
    }

    public static void setColor(Color color) {
        GlStateManager.color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    public static void toggleGuiPreCaps() {
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
    }

    public static void toggleGuiPostCaps() {
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
    }

    public static void setLineVertices(float n, float n2, float n3, float n4) {
        GL11.glVertex2f((float)n, (float)n2);
        GL11.glVertex2f((float)n3, (float)n4);
    }

    public static void setRectVertices(float n, float n2, float n3, float n4) {
        GL11.glVertex2f((float)n, (float)n2);
        GL11.glVertex2f((float)n3, (float)n2);
        GL11.glVertex2f((float)n3, (float)n4);
        GL11.glVertex2f((float)n, (float)n4);
    }

    public static void setPartialCircleVertices(float n, float n2, int n3, int n4, float n5) {
        for (int i = n3; i <= n4; ++i) {
            GL11.glVertex2f((float)(n + MathHelper.sin((float)Math.toRadians(i)) * n5), (float)(n2 + MathHelper.cos((float)Math.toRadians(i)) * n5));
        }
    }
}

