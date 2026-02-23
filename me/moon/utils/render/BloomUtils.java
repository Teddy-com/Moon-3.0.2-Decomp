/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL13
 *  org.lwjgl.opengl.GL20
 */
package me.moon.utils.render;

import java.nio.FloatBuffer;
import me.moon.Moon;
import me.moon.gui.ConfigGUI;
import me.moon.module.impl.combat.KillAura;
import me.moon.module.impl.visuals.BreadCrumbs;
import me.moon.module.impl.visuals.Chams;
import me.moon.module.impl.visuals.ClickGui;
import me.moon.module.impl.visuals.ESP;
import me.moon.module.impl.visuals.HUD;
import me.moon.utils.shadering.ShaderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

public class BloomUtils {
    private static ShaderUtil bloomShader = new ShaderUtil("bloom_shader.fsh", "vertex_shader.vsh");
    private static Framebuffer rectFramebuffer;
    private static Framebuffer blurFramebuffer;
    private static Framebuffer cutBuffer;
    private static int lastScale;
    private static int lastWidth;
    private static int lastHeight;

    public static void drawBloom() {
        GlStateManager.disableAlpha();
        GlStateManager.alphaFunc(516, 0.0f);
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        blurFramebuffer.bindFramebuffer(true);
        bloomShader.startShader();
        BloomUtils.blurUniformUpdate(bloomShader.getProgramId(), 1.0f, 0.0f);
        bloomShader.drawQuads(rectFramebuffer);
        bloomShader.stopShader();
        blurFramebuffer.unbindFramebuffer();
        bloomShader.startShader();
        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
        BloomUtils.blurUniformUpdate(bloomShader.getProgramId(), 0.0f, 1.0f);
        GL13.glActiveTexture((int)34004);
        GL11.glBindTexture((int)3553, (int)BloomUtils.rectFramebuffer.framebufferTexture);
        GL13.glActiveTexture((int)33984);
        bloomShader.drawQuads(blurFramebuffer);
        bloomShader.stopShader();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableAlpha();
    }

    public static void blurUniformUpdate(int programID, float leftDir, float rightDir) {
        GL20.glUniform1i((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"texture"), (int)0);
        GL20.glUniform1i((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"texture2"), (int)20);
        GL20.glUniform2f((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"texelSize"), (float)(1.0f / (float)Minecraft.getMinecraft().displayWidth), (float)(1.0f / (float)Minecraft.getMinecraft().displayHeight));
        GL20.glUniform2f((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"direction"), (float)leftDir, (float)rightDir);
        FloatBuffer buffer = BufferUtils.createFloatBuffer((int)256);
        float blurRadius = 20.0f;
        int i = 0;
        while ((float)i <= blurRadius) {
            buffer.put(BloomUtils.getGaussianOffset(i, 10.0f));
            ++i;
        }
        buffer.rewind();
        GL20.glUniform1((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"weights"), (FloatBuffer)buffer);
    }

    private static float getGaussianOffset(float x, float sigma) {
        float pow = x / sigma;
        return (float)(1.0 / ((double)Math.abs(sigma) * 2.50662827463) * Math.exp(-0.5 * (double)pow * (double)pow));
    }

    public static void callBloom() {
        Minecraft.getMinecraft().gameSettings.ofFastRender = false;
        ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());
        int factor = scale.getScaleFactor();
        int widthScaled = scale.getScaledWidth();
        int heightScaled = scale.getScaledHeight();
        if (rectFramebuffer != null && blurFramebuffer != null) {
            rectFramebuffer.framebufferClear();
            blurFramebuffer.framebufferClear();
        }
        if (lastScale != factor || lastWidth != widthScaled || lastHeight != heightScaled || rectFramebuffer == null || blurFramebuffer == null) {
            rectFramebuffer = bloomShader.createBuffer(rectFramebuffer);
            blurFramebuffer = bloomShader.createBuffer(blurFramebuffer);
            lastHeight = heightScaled;
            lastWidth = widthScaled;
            lastScale = factor;
        }
        HUD hud = (HUD)Moon.INSTANCE.getModuleManager().getModule("HUD");
        KillAura aura = (KillAura)Moon.INSTANCE.getModuleManager().getModule("KillAura");
        ClickGui clickGui = (ClickGui)Moon.INSTANCE.getModuleManager().getModule("ClickGUI");
        Chams chams = (Chams)Moon.INSTANCE.getModuleManager().getModule("Chams");
        rectFramebuffer.bindFramebuffer(true);
        hud.onBloom();
        ESP esp = (ESP)Moon.INSTANCE.getModuleManager().getModule("ESP");
        esp.blur();
        chams.blur();
        aura.onShadering();
        ConfigGUI.bloom();
        BreadCrumbs breadcrumbs = (BreadCrumbs)Moon.INSTANCE.getModuleManager().getModule("Breadcrumbs");
        breadcrumbs.onShadering();
        Minecraft.getMinecraft().ingameGUI.bloom();
        Minecraft.getMinecraft().ingameGUI.getChatGUI().bloom();
        Moon.INSTANCE.getNotificationManager().bloom();
        if (clickGui.getDiscordClickGUI() != null) {
            clickGui.getDiscordClickGUI().onBloom();
        }
        rectFramebuffer.unbindFramebuffer();
        BloomUtils.drawBloom();
    }

    public static void callBloomNoCut() {
        HUD hud = (HUD)Moon.INSTANCE.getModuleManager().getModule("HUD");
        hud.onBloomNC();
    }
}

