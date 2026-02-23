/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3d
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 */
package me.moon.utils.render;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import javax.vecmath.Vector3d;
import me.moon.module.impl.visuals.ESP;
import me.moon.module.impl.visuals.HUD;
import me.moon.utils.font.MCFontRenderer;
import me.moon.utils.render.GLUtil;
import me.moon.utils.vector.Vec3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class RenderUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final Frustum frustrum = new Frustum();
    private static final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
    private static final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static void renderTag(String name, double pX, double pY, double pZ, int color) {
        float scale = (float)(RenderUtil.mc.thePlayer.getDistance(pX + RenderUtil.mc.getRenderManager().renderPosX, pY + RenderUtil.mc.getRenderManager().renderPosY, pZ + RenderUtil.mc.getRenderManager().renderPosZ) / 8.0);
        if (scale < 1.6f) {
            scale = 1.6f;
        }
        scale /= 50.0f;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)((float)pX), (float)((float)pY + 1.4f), (float)((float)pZ));
        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)(-RenderUtil.mc.getRenderManager().playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)RenderUtil.mc.getRenderManager().playerViewX, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glScalef((float)(-scale), (float)(-scale), (float)scale);
        GL11.glDisable((int)2896);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        int width = RenderUtil.mc.fontRendererObj.getStringWidth(name) / 2;
        GL11.glPushMatrix();
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Gui.drawRect(-width - 1, -(RenderUtil.mc.fontRendererObj.FONT_HEIGHT + 8), -width - 1 + 2 + width * 2, -(RenderUtil.mc.fontRendererObj.FONT_HEIGHT - 1), 0);
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        RenderUtil.mc.fontRendererObj.drawStringWithShadow(name, -width, -(RenderUtil.mc.fontRendererObj.FONT_HEIGHT + 7), color);
        GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.enableTexture2D();
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)2929);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void setColor(Color c) {
        GL11.glColor4d((double)((float)c.getRed() / 255.0f), (double)((float)c.getGreen() / 255.0f), (double)((float)c.getBlue() / 255.0f), (double)((float)c.getAlpha() / 255.0f));
    }

    public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, float zLevel) {
        float var7 = 0.00390625f;
        float var8 = 0.00390625f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(x, y + height, zLevel).tex((float)u * 0.00390625f, (float)(v + height) * 0.00390625f).endVertex();
        worldRenderer.pos(x + width, y + height, zLevel).tex((float)(u + width) * 0.00390625f, (float)(v + height) * 0.00390625f).endVertex();
        worldRenderer.pos(x + width, y, zLevel).tex((float)(u + width) * 0.00390625f, (float)v * 0.00390625f).endVertex();
        worldRenderer.pos(x, y, zLevel).tex((float)u * 0.00390625f, (float)v * 0.00390625f).endVertex();
        tessellator.draw();
    }

    public static int withTransparency(int rgb, float alpha) {
        float r = (float)(rgb >> 16 & 0xFF) / 255.0f;
        float g = (float)(rgb >> 8 & 0xFF) / 255.0f;
        float b = (float)(rgb >> 0 & 0xFF) / 255.0f;
        return new Color(r, g, b, alpha).getRGB();
    }

    public static ScaledResolution getResolution() {
        return new ScaledResolution(mc);
    }

    public static Vec3 to2D(double x, double y, double z) {
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer((int)3);
        IntBuffer viewport = BufferUtils.createIntBuffer((int)16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer((int)16);
        FloatBuffer projection = BufferUtils.createFloatBuffer((int)16);
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)projection);
        GL11.glGetInteger((int)2978, (IntBuffer)viewport);
        boolean result = GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)modelView, (FloatBuffer)projection, (IntBuffer)viewport, (FloatBuffer)screenCoords);
        if (result) {
            return new Vec3(screenCoords.get(0), (float)Display.getHeight() - screenCoords.get(1), screenCoords.get(2));
        }
        return null;
    }

    public static void drawArrow(float x, float y, boolean up, int hexColor) {
        GL11.glPushMatrix();
        GL11.glScaled((double)1.3, (double)1.3, (double)1.3);
        x = (float)((double)x / 1.3);
        y = (float)((double)y / 1.3);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)3553);
        RenderUtil.hexColor(hexColor);
        GL11.glLineWidth((float)2.0f);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)(y + (float)(up ? 4 : 0)));
        GL11.glVertex2d((double)(x + 3.0f), (double)(y + (float)(up ? 0 : 4)));
        GL11.glEnd();
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)(x + 3.0f), (double)(y + (float)(up ? 0 : 4)));
        GL11.glVertex2d((double)(x + 6.0f), (double)(y + (float)(up ? 4 : 0)));
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    }

    public static void drawTracerPointer(float x, float y, float size, float widthDiv, float heightDiv, int color) {
        boolean blend = GL11.glIsEnabled((int)3042);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glPushMatrix();
        RenderUtil.hexColor(color);
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)5);
        GL11.glVertex2d((double)Math.round(x), (double)Math.round(y));
        GL11.glVertex2d((double)((float)Math.round(x) - size / widthDiv), (double)((float)Math.round(y) + size));
        GL11.glVertex2d((double)Math.round(x), (double)((float)Math.round(y) + size / heightDiv));
        GL11.glVertex2d((double)((float)Math.round(x) + size / widthDiv), (double)((float)Math.round(y) + size));
        GL11.glVertex2d((double)Math.round(x), (double)Math.round(y));
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glShadeModel((int)7424);
        GL11.glEnable((int)3553);
        if (!blend) {
            GL11.glDisable((int)3042);
        }
        GL11.glDisable((int)2848);
    }

    public static void hexColor(int hexColor) {
        float red = (float)(hexColor >> 16 & 0xFF) / 255.0f;
        float green = (float)(hexColor >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hexColor & 0xFF) / 255.0f;
        float alpha = (float)(hexColor >> 24 & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void drawEntityESP(double x, double y, double z, double height, double width, Color color, boolean outline) {
        GL11.glPushMatrix();
        GLUtil.setGLCap(3042, true);
        GLUtil.setGLCap(3553, false);
        GLUtil.setGLCap(2896, false);
        GLUtil.setGLCap(2929, false);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)1.8f);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtil.setGLCap(2848, true);
        GL11.glDepthMask((boolean)true);
        RenderUtil.BB(new AxisAlignedBB(x - width + 0.25, y + 0.1, z - width + 0.25, x + width - 0.25, y + height + 0.25, z + width - 0.25), new Color(color.getRed(), color.getGreen(), color.getBlue(), 120).getRGB());
        if (outline) {
            RenderUtil.OutlinedBB(new AxisAlignedBB(x - width + 0.25, y + 0.1, z - width + 0.25, x + width - 0.25, y + height + 0.25, z + width - 0.25), 1.0f, color.getRGB());
        }
        GLUtil.revertAllCaps();
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static int getRainbow(int speed, int offset, float s) {
        float hue = (System.currentTimeMillis() + (long)offset) % (long)speed;
        return Color.getHSBColor(hue /= (float)speed, s, 1.0f).getRGB();
    }

    public static float getStringWidth(String str, boolean usesFont, MCFontRenderer fontRenderer) {
        if (usesFont) {
            return fontRenderer.getStringWidth(str);
        }
        return RenderUtil.mc.fontRendererObj.getStringWidth(str);
    }

    public static Color getGradientOffset(Color color1, Color color2, double offset) {
        if (offset > 1.0) {
            double left = offset % 1.0;
            int off = (int)offset;
            offset = off % 2 == 0 ? left : 1.0 - left;
        }
        double inverse_percent = 1.0 - offset;
        int redPart = (int)((double)color1.getRed() * inverse_percent + (double)color2.getRed() * offset);
        int greenPart = (int)((double)color1.getGreen() * inverse_percent + (double)color2.getGreen() * offset);
        int bluePart = (int)((double)color1.getBlue() * inverse_percent + (double)color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }

    public static Color getGradient(double offset, Color color1, Color color2, Color color3) {
        Color color11 = color1;
        Color color22 = color2;
        if (offset > 1.0) {
            double left = offset % 1.0;
            int off = (int)offset;
            double d = offset = off % 2 == 0 ? left : 1.0 - left;
        }
        if ((offset *= 2.0) >= 1.0) {
            offset -= 1.0;
            color11 = color2;
            color22 = color3;
        }
        double inverse_percent = 1.0 - offset;
        int redPart = (int)((double)color11.getRed() * inverse_percent + (double)color22.getRed() * offset);
        int greenPart = (int)((double)color11.getGreen() * inverse_percent + (double)color22.getGreen() * offset);
        int bluePart = (int)((double)color11.getBlue() * inverse_percent + (double)color22.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }

    public static void drawArrowS(float x, float y, boolean up, int hexColor) {
        GL11.glPushMatrix();
        GL11.glScaled((double)1.3, (double)1.3, (double)1.3);
        x = (float)((double)x / 1.3);
        y = (float)((double)y / 1.3);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)3553);
        RenderUtil.hexColor(hexColor);
        GL11.glLineWidth((float)2.0f);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)(y + (float)(up ? 4 : 0)));
        GL11.glVertex2d((double)(x + 2.0f), (double)(y + (float)(up ? 0 : 4)));
        GL11.glEnd();
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)(x + 2.0f), (double)(y + (float)(up ? 0 : 4)));
        GL11.glVertex2d((double)(x + 4.0f), (double)(y + (float)(up ? 4 : 0)));
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    }

    public static void drawCircleTargetESP(Entity entity, Color color, float partialTicks, double yThing) {
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GlStateManager.disableAlpha();
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        if (color.getAlpha() >= 255) {
            GL11.glEnable((int)2848);
        }
        GL11.glShadeModel((int)7425);
        double x = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, partialTicks) - RenderUtil.mc.getRenderManager().renderPosX;
        double y = RenderUtil.interpolate(entity.posY + yThing, entity.lastTickPosY + yThing, partialTicks) - RenderUtil.mc.getRenderManager().renderPosY;
        double z = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks) - RenderUtil.mc.getRenderManager().renderPosZ;
        GL11.glLineWidth((float)2.0f);
        ArrayList posArrayList = new ArrayList();
        float r = 0.003921569f * (float)color.getRed();
        float g = 0.003921569f * (float)color.getGreen();
        float b = 0.003921569f * (float)color.getBlue();
        GL11.glColor4f((float)r, (float)g, (float)b, (float)((float)color.getAlpha() / 255.0f));
        GL11.glBegin((int)3);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex3d((double)(x + (double)entity.width * Math.cos((double)i * (Math.PI * 2) / 180.0)), (double)y, (double)(z + (double)entity.width * Math.sin((double)i * (Math.PI * 2) / 180.0)));
        }
        GL11.glEnd();
        GL11.glDepthMask((boolean)true);
        if (color.getAlpha() >= 255) {
            GL11.glDisable((int)2848);
        }
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GlStateManager.enableAlpha();
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }

    public static float[] getRGBAs(int rgb) {
        return new float[]{(float)(rgb >> 16 & 0xFF) / 255.0f, (float)(rgb >> 8 & 0xFF) / 255.0f, (float)(rgb & 0xFF) / 255.0f, (float)(rgb >> 24 & 0xFF) / 255.0f};
    }

    public static void drawImage(ResourceLocation image, float x, float y, int width, int height) {
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture2(x, y, 0.0f, height, width, height, width, height);
    }

    public static void drawImage(ResourceLocation image, float x, float y, float width, float height) {
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture2(x, y, 0.0f, height, width, height, width, height);
    }

    public static void drawCircle(float x, float y, float r, int c) {
        double y2;
        double x2;
        int i;
        float f = (float)(c >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(c >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(c >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(c & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glLineWidth((float)1.0f);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glEnable((int)2881);
        GL11.glBegin((int)6);
        for (i = 0; i <= 360; ++i) {
            x2 = Math.sin((double)i * Math.PI / 180.0) * (double)(r / 2.0f);
            y2 = Math.cos((double)i * Math.PI / 180.0) * (double)(r / 2.0f);
            GL11.glVertex2d((double)((double)(x + r / 2.0f) + x2), (double)((double)(y + r / 2.0f) + y2));
        }
        GL11.glEnd();
        GL11.glBegin((int)6);
        for (i = 0; i <= 360; ++i) {
            x2 = Math.sin((double)i * Math.PI / 180.0) * (double)(r / 2.0f);
            y2 = Math.cos((double)i * Math.PI / 180.0) * (double)(r / 2.0f);
            GL11.glVertex2d((double)((double)(x + r / 2.0f) + x2), (double)((double)(y + r / 2.0f) + y2));
        }
        GL11.glEnd();
        GL11.glDisable((int)2881);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static void drawUnsmoothedCircle(float x, float y, float r, int c) {
        float f = (float)(c >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(c >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(c >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(c & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glLineWidth((float)1.0f);
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glBegin((int)6);
        for (int i = 0; i <= 360; ++i) {
            double x2 = Math.sin((double)i * Math.PI / 180.0) * (double)(r / 2.0f);
            double y2 = Math.cos((double)i * Math.PI / 180.0) * (double)(r / 2.0f);
            GL11.glVertex2d((double)((double)(x + r / 2.0f) + x2), (double)((double)(y + r / 2.0f) + y2));
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GL11.glPopMatrix();
    }

    public static void drawUnfilledCircle(float x, float y, float r, int c) {
        float f = (float)(c >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(c >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(c >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(c & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glLineWidth((float)1.0f);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)3);
        for (int i = 0; i <= 360; ++i) {
            double x2 = Math.sin((double)i * Math.PI / 180.0) * (double)(r / 2.0f);
            double y2 = Math.cos((double)i * Math.PI / 180.0) * (double)(r / 2.0f);
            GL11.glVertex2d((double)((double)(x + r / 2.0f) + x2), (double)((double)(y + r / 2.0f) + y2));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static void drawUnfilledCircleAnimated(float x, float y, float r, int c, int fromRadius, int toRadius) {
        float f = (float)(c >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(c >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(c >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(c & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glLineWidth((float)2.0f);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)3);
        for (int i = fromRadius; i <= toRadius; ++i) {
            double x2 = Math.sin((double)i * Math.PI / 180.0) * (double)(r / 2.0f);
            double y2 = Math.cos((double)i * Math.PI / 180.0) * (double)(r / 2.0f);
            GL11.glVertex2d((double)((double)(x + r / 2.0f) + x2), (double)((double)(y + r / 2.0f) + y2));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void OutlinedBB(AxisAlignedBB bb, float width, int color) {
        RenderUtil.enable3D();
        GL11.glLineWidth((float)width);
        RenderUtil.color(color);
        RenderUtil.drawOutlinedBoundingBox(bb);
        RenderUtil.disable3D();
    }

    public static void BB(AxisAlignedBB bb, int color) {
        RenderUtil.enable3D();
        RenderUtil.color(color);
        RenderUtil.drawBoundingBox(bb);
        RenderUtil.disable3D();
    }

    public static void enable3D() {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
    }

    public static void disable3D() {
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static void color(int color) {
        GL11.glColor4f((float)((float)(color >> 16 & 0xFF) / 255.0f), (float)((float)(color >> 8 & 0xFF) / 255.0f), (float)((float)(color & 0xFF) / 255.0f), (float)((float)(color >> 24 & 0xFF) / 255.0f));
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static Vector3d project(double x, double y, double z) {
        GL11.glGetFloat((int)2982, (FloatBuffer)modelview);
        GL11.glGetFloat((int)2983, (FloatBuffer)projection);
        GL11.glGetInteger((int)2978, (IntBuffer)viewport);
        if (GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)modelview, (FloatBuffer)projection, (IntBuffer)viewport, (FloatBuffer)vector)) {
            return new Vector3d((double)(vector.get(0) / (float)RenderUtil.getResolution().getScaleFactor()), (double)(((float)Display.getHeight() - vector.get(1)) / (float)RenderUtil.getResolution().getScaleFactor()), (double)vector.get(2));
        }
        return null;
    }

    public static void drawCheckMark(float x, float y, int width, int color) {
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glLineWidth((float)1.5f);
        GL11.glBegin((int)3);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glVertex2d((double)((double)(x + (float)width) - 6.5), (double)(y + 3.0f));
        GL11.glVertex2d((double)((double)(x + (float)width) - 11.5), (double)(y + 10.0f));
        GL11.glVertex2d((double)((double)(x + (float)width) - 13.5), (double)(y + 8.0f));
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawCheckMarkSectio(float x, float y, int width, int color) {
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glLineWidth((float)2.5f);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glBegin((int)3);
        GL11.glVertex2d((double)((double)(x + (float)width) - 5.75), (double)(y + 4.0f));
        GL11.glVertex2d((double)((double)(x + (float)width) - 11.0), (double)((double)y + 9.8));
        GL11.glVertex2d((double)((double)(x + (float)width) - 14.0), (double)(y + 7.0f));
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawLoginCheckmark(float x, float y, int width, int color) {
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glLineWidth((float)2.5f);
        GL11.glBegin((int)3);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glVertex2d((double)((double)(x + (float)width) - 8.75), (double)(y - 9.0f));
        GL11.glVertex2d((double)((double)(x + (float)width) - 25.0), (double)((double)y + 9.8));
        GL11.glVertex2d((double)((double)(x + (float)width) - 34.0), (double)(y + 3.0f));
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawCheckmark1(float x, float y, int hexColor) {
        GL11.glPushMatrix();
        GL11.glEnable((int)2848);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        RenderUtil.hexColor(hexColor);
        GL11.glLineWidth((float)2.0f);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)(x + 1.0f), (double)(y + 1.0f));
        GL11.glVertex2d((double)(x + 3.0f), (double)(y + 4.0f));
        GL11.glEnd();
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)(x + 3.0f), (double)(y + 4.0f));
        GL11.glVertex2d((double)(x + 7.0f), (double)(y - 2.0f));
        GL11.glEnd();
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return RenderUtil.isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }

    public static void prepareScissorBox(ScaledResolution sr, float x, float y, float width, float height) {
        float x2 = x + width;
        float y2 = y + height;
        int factor = sr.getScaleFactor();
        GL11.glScissor((int)((int)(x * (float)factor)), (int)((int)(((float)sr.getScaledHeight() - y2) * (float)factor)), (int)((int)((x2 - x) * (float)factor)), (int)((int)((y2 - y) * (float)factor)));
    }

    public static void drawBorderedRect(double x, double y, double width, double height, double lineSize, int borderColor, int color) {
        Gui.drawRect(x, y, x + width, y + height, color);
        Gui.drawRect(x, y, x + width, y + lineSize, borderColor);
        Gui.drawRect(x, y, x + lineSize, y + height, borderColor);
        Gui.drawRect(x + width, y, x + width - lineSize, y + height, borderColor);
        Gui.drawRect(x, y + height, x + width, y + height - lineSize, borderColor);
    }

    public static void drawCornerRect(double x, double y, double width, double height, double thickness, int hex, boolean border, double borderwidth) {
        double w = width / 4.0;
        double h = height / 4.0;
        RenderUtil.drawRect(x, y, w + (border ? borderwidth : 0.0), thickness, hex);
        double x1 = x + width - (w + (border ? borderwidth : 0.0));
        RenderUtil.drawRect(x1, y, w, thickness, hex);
        RenderUtil.drawRect(x, y + height - thickness, w + (border ? borderwidth : 0.0), thickness, hex);
        RenderUtil.drawRect(x1, y + height - thickness, w, thickness, hex);
        RenderUtil.drawRect(x, y, thickness, h + (border ? borderwidth : 0.0), hex);
        RenderUtil.drawRect(x + width - thickness, y, thickness, h + (border ? borderwidth : 0.0), hex);
        double y1 = y + height - (h + (border ? borderwidth : 0.0));
        RenderUtil.drawRect(x, y1, thickness, h, hex);
        RenderUtil.drawRect(x + width - thickness, y1, thickness, h, hex);
    }

    public static void drawBordered(double x, double y, double x2, double y2, double thickness, int inside, int outline) {
        double fix = 0.0;
        if (thickness < 1.0) {
            fix = 1.0;
        }
        RenderUtil.drawRect2(x + thickness, y + thickness, x2 - thickness, y2 - thickness, inside);
        RenderUtil.drawRect2(x, y + 1.0 - fix, x + thickness, y2, outline);
        RenderUtil.drawRect2(x, y, x2 - 1.0 + fix, y + thickness, outline);
        RenderUtil.drawRect2(x2 - thickness, y, x2, y2 - 1.0 + fix, outline);
        RenderUtil.drawRect2(x + 1.0 - fix, y2 - thickness, x2, y2, outline);
    }

    public static void drawBar(Entity entity, float x, float y, float width, float height, float max, float value, int color, double posw, double posy) {
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        EntityLivingBase ent = (EntityLivingBase)entity;
        double inc = height / ent.getMaxHealth();
        double end = inc * (double)Math.min(ent.getHealth(), ent.getMaxHealth());
        double offset = posw - (double)y;
        double percentOffset = height / ent.getMaxHealth();
        double finalNum = end;
        GL11.glPushMatrix();
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        RenderUtil.drawBordered(x, y, x + width, y + height, 0.1f, -2013265920, 0x67000000);
        RenderUtil.drawRect(x + 0.5f, (double)y + offset - 0.5, width - 1.0f, -finalNum + 1.0, ESP.hudColored.getValue() != false ? HUD.getColorHUD() : color);
        GL11.glPopMatrix();
    }

    public static void drawSmoothedRounded(float x, float y, float width, float height, float cornerRadius) {
        int slices = 45;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GL11.glDisable((int)3553);
        RenderUtil.fillRectangle(x + cornerRadius, y, width - 2.0f * cornerRadius, height);
        RenderUtil.fillRectangle(x, y + cornerRadius, cornerRadius, height - 2.0f * cornerRadius);
        RenderUtil.fillRectangle(x + width - cornerRadius, y + cornerRadius, cornerRadius, height - 2.0f * cornerRadius);
        RenderUtil.drawCirclePart(x + cornerRadius, y + cornerRadius, (float)(-Math.PI), -1.5707964f, cornerRadius, 45);
        RenderUtil.drawCirclePart(x + cornerRadius, y + height - cornerRadius, -1.5707964f, 0.0f, cornerRadius, 45);
        RenderUtil.drawCirclePart(x + width - cornerRadius, y + cornerRadius, 1.5707964f, (float)Math.PI, cornerRadius, 45);
        RenderUtil.drawCirclePart(x + width - cornerRadius, y + height - cornerRadius, 0.0f, 1.5707964f, cornerRadius, 45);
        GlStateManager.disableBlend();
        GL11.glEnable((int)3553);
        GlStateManager.popMatrix();
    }

    public static void drawCirclePart(float x, float y, float fromAngle, float toAngle, float radius, int slices) {
        GL11.glEnable((int)3042);
        GL11.glBegin((int)9);
        GL11.glVertex2f((float)x, (float)y);
        float increment = (toAngle - fromAngle) / (float)slices;
        for (int i = 0; i <= slices; ++i) {
            float angle = fromAngle + (float)i * increment;
            float dX = MathHelper.sin(angle);
            float dY = MathHelper.cos(angle);
            GL11.glVertex2f((float)(x + dX * radius), (float)(y + dY * radius));
        }
        GL11.glEnd();
        GL11.glDisable((int)2881);
    }

    public static void fillRectangle(float x, float y, float width, float height) {
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glBegin((int)7);
        GL11.glVertex2f((float)x, (float)(y + height));
        GL11.glVertex2f((float)(x + width), (float)(y + height));
        GL11.glVertex2f((float)(x + width), (float)y);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glEnd();
    }

    public static void drawBarA(Entity entity, float x, float y, float width, float height, float max, float value, int color, double posw, double posy) {
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        float inc = height / max;
        EntityLivingBase ent = (EntityLivingBase)entity;
        double offset = posw - posy;
        double percentOffset = offset / 20.0;
        double finalNum = percentOffset * (double)ent.getTotalArmorValue();
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        RenderUtil.drawBorderedRect(x, y, width, height, 0.5, -16777216, -1442840576);
        RenderUtil.drawRect(x + 0.5f, (double)y + offset - 0.5, width - 1.0f, -finalNum + 1.0, color);
    }

    public static void drawBar(Entity entity, float x, float y, float width, float height, float max, float value, int color) {
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        float inc = height / max;
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        RenderUtil.drawBordered(x, y, width, height, 0.5, -16777216, -1442840576);
        float incY = y + height - inc;
        int i = 0;
        while ((float)i < value) {
            RenderUtil.drawBordered(x + 0.25f, incY, width - 0.5f, inc, 0.25, -16777216, color);
            incY -= inc;
            ++i;
        }
    }

    public static void drawRect(double x, double y, double width, double height, int color) {
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        Gui.drawRect(x, y, x + width, y + height, color);
    }

    public static void drawRect2(double x, double y, double x2, double y2, int color) {
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        Gui.drawRect(x, y, x2, y2, color);
    }

    public static void drawBorderedRoundedRect(float x, float y, float width, float height, float radius, float linewidth, int insideC, int borderC) {
        RenderUtil.drawRoundedRect(x, y, width, height, radius, borderC);
        RenderUtil.drawOutlinedRoundedRect(x, y, width, height, radius, linewidth, insideC);
    }

    public static void drawRoundedRectWithShadow(double x, double y, double width, double height, double radius, int color) {
        RenderUtil.drawRoundedRect(x + 2.0, y + 1.0, width, height + 1.0, radius, new Color(0).getRGB());
        RenderUtil.drawRoundedRect(x, y, width, height, radius, color);
    }

    public static void drawOutlinedRoundedRect(double x, double y, double width, double height, double radius, float linewidth, int color) {
        int i;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        double x1 = x + width;
        double y1 = y + height;
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glLineWidth((float)linewidth);
        GL11.glDisable((int)3553);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)1.0f);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)2);
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x + radius + Math.sin((double)i * Math.PI / 180.0) * (radius * -1.0)), (double)(y + radius + Math.cos((double)i * Math.PI / 180.0) * (radius * -1.0)));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)(x + radius + Math.sin((double)i * Math.PI / 180.0) * (radius * -1.0)), (double)(y1 - radius + Math.cos((double)i * Math.PI / 180.0) * (radius * -1.0)));
        }
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x1 - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(y1 - radius + Math.cos((double)i * Math.PI / 180.0) * radius));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)(x1 - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(y + radius + Math.cos((double)i * Math.PI / 180.0) * radius));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRoundedRect(double x, double y, double width, double height, double radius, int color) {
        int i;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        double x1 = x / 2.0 + width / 2.0;
        double y1 = y / 2.0 + height / 2.0;
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glPushAttrib((int)0);
        x *= 2.0;
        y *= 2.0;
        x1 *= 2.0;
        y1 *= 2.0;
        GL11.glDisable((int)3553);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glBegin((int)6);
        for (i = 0; i <= 90; ++i) {
            GL11.glVertex2d((double)(x / 2.0 + radius + Math.sin((double)i * Math.PI / 180.0) * (radius * -1.0)), (double)(y / 2.0 + radius + Math.cos((double)i * Math.PI / 180.0) * (radius * -1.0)));
        }
        for (i = 90; i <= 180; ++i) {
            GL11.glVertex2d((double)(x / 2.0 + radius + Math.sin((double)i * Math.PI / 180.0) * (radius * -1.0)), (double)(y1 - radius + Math.cos((double)i * Math.PI / 180.0) * (radius * -1.0)));
        }
        for (i = 0; i <= 90; ++i) {
            GL11.glVertex2d((double)(x1 - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(y1 - radius + Math.cos((double)i * Math.PI / 180.0) * radius));
        }
        for (i = 90; i <= 180; ++i) {
            GL11.glVertex2d((double)(x1 - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(y / 2.0 + radius + Math.cos((double)i * Math.PI / 180.0) * radius));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glPopAttrib();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradient(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        float f4 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(col2 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f4);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static void drawGradientCPicker(double left, double top, double right, double bottom, int col1, int col2, int col3, int col4) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        float f12 = (float)(col4 >> 24 & 0xFF) / 255.0f;
        float f13 = (float)(col4 >> 16 & 0xFF) / 255.0f;
        float f14 = (float)(col4 >> 8 & 0xFF) / 255.0f;
        float f15 = (float)(col4 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)3008);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)6);
        GL11.glColor4f((float)f13, (float)f14, (float)f15, (float)f12);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glEnd();
        GL11.glBegin((int)6);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3008);
        GL11.glShadeModel((int)7424);
    }

    public static void drawGradient2(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        float f4 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(col2 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f4);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static void drawGradient3(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        float f4 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(col2 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static void coloring(int rgba) {
        float f = (float)(rgba >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(rgba >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(rgba >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(rgba & 0xFF) / 255.0f;
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
    }

    public static void drawCircle2(double x, double y, double radius, int color) {
        GL11.glPushMatrix();
        RenderUtil.coloring(color);
        GL11.glDisable((int)3553);
        GL11.glLineWidth((float)5.0f);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)2);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double)(x + Math.sin(Math.toRadians(i)) * radius), (double)(y + Math.cos(Math.toRadians(i)) * radius));
        }
        GL11.glEnd();
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }

    public static void drawColorPickerCircle(double x, double y, double radius, float alpha) {
        GL11.glPushMatrix();
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)2);
        for (int i = 0; i <= 360; ++i) {
            RenderUtil.coloring(Color.getHSBColor(1.0f, 0.0f, alpha).getRGB());
            GL11.glVertex2d((double)x, (double)y);
            RenderUtil.coloring(Color.HSBtoRGB((float)i / 360.0f, 1.0f, alpha));
            GL11.glVertex2d((double)(x + Math.sin(Math.toRadians(i)) * radius), (double)(y + Math.cos(Math.toRadians(i)) * radius));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static ScaledResolution translateGuiScale(ScaledResolution sr) {
        return sr;
    }
}

