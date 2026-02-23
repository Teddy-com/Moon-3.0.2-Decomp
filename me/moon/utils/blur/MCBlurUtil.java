/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.utils.blur;

import me.moon.Moon;
import me.moon.module.impl.visuals.Blur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class MCBlurUtil {
    private static ShaderGroup shaderGroup;
    private static final Minecraft mc;
    private static Framebuffer framebuffer;
    private static int lastScalingFactor;
    private static int lastScalingWidth;
    private static int lastScalingHeight;
    private static final ResourceLocation shader;

    public static void init() {
        try {
            shaderGroup = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), shader);
            shaderGroup.createBindFramebuffers(MCBlurUtil.mc.displayWidth, MCBlurUtil.mc.displayHeight);
            framebuffer = new Framebuffer(MCBlurUtil.mc.displayWidth, MCBlurUtil.mc.displayHeight, true);
            framebuffer.setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void drawBLURRRR(int x, int y, int width, int height, float intensity) {
        ScaledResolution scale = new ScaledResolution(mc);
        Blur blur = (Blur)Moon.INSTANCE.getModuleManager().getModule("Blur");
        if (!blur.isEnabled() || !Blur.toasterBlur.getValue().booleanValue() || MCBlurUtil.mc.gameSettings.ofFastRender) {
            return;
        }
        int factor = scale.getScaleFactor();
        int widthScaled = scale.getScaledWidth();
        int heightScaled = scale.getScaledHeight();
        if (lastScalingFactor != factor || lastScalingWidth != widthScaled || lastScalingHeight != heightScaled || framebuffer == null || shaderGroup == null) {
            MCBlurUtil.init();
        }
        lastScalingFactor = factor;
        lastScalingWidth = widthScaled;
        lastScalingHeight = heightScaled;
        shaderGroup.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(((Integer)blur.radius.getValue()).intValue());
        shaderGroup.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(((Integer)blur.radius.getValue()).intValue());
        framebuffer.bindFramebuffer(true);
        shaderGroup.getShaders().get(0).getShaderManager().getShaderUniform("BlurDir").set(0.0f, 1.0f);
        shaderGroup.getShaders().get(0).loadShader(MCBlurUtil.mc.timer.elapsedPartialTicks / 20.0f);
        framebuffer.unbindFramebuffer();
        shaderGroup.getShaders().get(1).getShaderManager().getShaderUniform("BlurDir").set(1.0f, 0.0f);
        GL11.glEnable((int)3089);
        GL11.glScissor((int)(x * factor), (int)(MCBlurUtil.mc.displayHeight - y * factor - height * factor), (int)(width * factor), (int)(height * factor));
        shaderGroup.getShaders().get(1).loadShader(MCBlurUtil.mc.timer.elapsedPartialTicks / 20.0f);
        GL11.glDisable((int)3089);
        mc.getFramebuffer().bindFramebuffer(true);
    }

    public static void drawGuiBlur(int x, int y, int width, int height, float intensity) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        int widthScaled = scale.getScaledWidth();
        int heightScaled = scale.getScaledHeight();
        if (MCBlurUtil.mc.gameSettings.ofFastRender) {
            return;
        }
        if (lastScalingFactor != factor || lastScalingWidth != widthScaled || lastScalingHeight != heightScaled || framebuffer == null || shaderGroup == null) {
            MCBlurUtil.init();
        }
        lastScalingFactor = factor;
        lastScalingWidth = widthScaled;
        lastScalingHeight = heightScaled;
        shaderGroup.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
        shaderGroup.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(intensity);
        framebuffer.bindFramebuffer(true);
        shaderGroup.getShaders().get(0).getShaderManager().getShaderUniform("BlurDir").set(0.0f, 1.0f);
        shaderGroup.getShaders().get(0).loadShader(MCBlurUtil.mc.timer.elapsedPartialTicks / 20.0f);
        framebuffer.unbindFramebuffer();
        shaderGroup.getShaders().get(1).getShaderManager().getShaderUniform("BlurDir").set(1.0f, 0.0f);
        GL11.glEnable((int)3089);
        GL11.glScissor((int)(x * factor), (int)(MCBlurUtil.mc.displayHeight - y * factor - height * factor), (int)(width * factor), (int)(height * factor));
        shaderGroup.getShaders().get(1).loadShader(MCBlurUtil.mc.timer.elapsedPartialTicks / 20.0f);
        GL11.glDisable((int)3089);
        mc.getFramebuffer().bindFramebuffer(true);
    }

    static {
        mc = Minecraft.getMinecraft();
        shader = new ResourceLocation("textures/client/post/blur.json");
    }
}

