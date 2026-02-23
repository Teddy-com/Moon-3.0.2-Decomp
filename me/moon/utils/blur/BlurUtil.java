/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL13
 *  org.lwjgl.opengl.GL20
 */
package me.moon.utils.blur;

import java.nio.FloatBuffer;
import me.moon.Moon;
import me.moon.module.impl.visuals.Blur;
import me.moon.utils.shadering.ShaderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

public class BlurUtil {
    private static ShaderUtil shaderUtil = new ShaderUtil("blur_shader.fsh", "vertex_shader.vsh");
    private static Framebuffer framebuffer;
    private static Framebuffer framebufferRender;
    private static final Minecraft mc;
    private static int radiusLast;
    private static int lastScale;
    private static int lastWidth;
    private static int lastHeight;

    public static void init() {
        shaderUtil = new ShaderUtil("blur_shader.fsh", "vertex_shader.vsh");
    }

    public static void blur() {
        if (Blur.toasterBlur.getValue().booleanValue() || !Moon.INSTANCE.getModuleManager().getModule("Blur").isEnabled()) {
            return;
        }
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        int widthScaled = scale.getScaledWidth();
        int heightScaled = scale.getScaledHeight();
        if (framebufferRender != null && framebuffer != null) {
            framebufferRender.framebufferClear();
            framebuffer.framebufferClear();
        }
        if (lastScale != factor || lastWidth != widthScaled || lastHeight != heightScaled || framebufferRender == null || framebuffer == null) {
            framebufferRender = shaderUtil.createBuffer(framebufferRender);
            framebuffer = shaderUtil.createBuffer(framebuffer);
            lastScale = factor;
            lastWidth = widthScaled;
            lastHeight = heightScaled;
        }
        framebufferRender.bindFramebuffer(true);
        Moon.INSTANCE.blur();
        framebufferRender.unbindFramebuffer();
        framebuffer.bindFramebuffer(true);
        shaderUtil.startShader();
        BlurUtil.updateUniforms(shaderUtil.getProgramId(), 0);
        shaderUtil.drawQuads(mc.getFramebuffer());
        shaderUtil.stopShader();
        framebuffer.unbindFramebuffer();
        mc.getFramebuffer().bindFramebuffer(true);
        shaderUtil.startShader();
        BlurUtil.updateUniforms(shaderUtil.getProgramId(), 1);
        GL13.glActiveTexture((int)34004);
        GL11.glBindTexture((int)3553, (int)BlurUtil.framebufferRender.framebufferTexture);
        GL13.glActiveTexture((int)33984);
        shaderUtil.drawQuads(framebuffer);
        shaderUtil.stopShader();
    }

    public static void updateUniforms(int programID, int pass) {
        Blur blur = (Blur)Moon.INSTANCE.getModuleManager().getModule("Blur");
        GL20.glUniform1i((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"texture"), (int)0);
        GL20.glUniform1i((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"texture2"), (int)20);
        GL20.glUniform2f((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"texelSize"), (float)(1.0f / (float)Minecraft.getMinecraft().displayWidth), (float)(1.0f / (float)Minecraft.getMinecraft().displayHeight));
        GL20.glUniform1f((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"radius"), (float)((Integer)blur.radius.getValue()).intValue());
        GL20.glUniform2f((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"direction"), (float)(pass == 1 ? 1.0f : 0.0f), (float)(pass == 1 ? 0.0f : 1.0f));
        if (radiusLast != (Integer)blur.radius.getValue()) {
            FloatBuffer buffer = BufferUtils.createFloatBuffer((int)256);
            float blurRadius = (float)((Integer)blur.radius.getValue()).intValue() / 2.0f;
            int i = 0;
            while ((float)i <= blurRadius) {
                buffer.put(BlurUtil.getGaussianOffset(i, (float)((Integer)blur.radius.getValue()).intValue() / 2.0f));
                ++i;
            }
            buffer.rewind();
            GL20.glUniform1((int)GL20.glGetUniformLocation((int)programID, (CharSequence)"weights"), (FloatBuffer)buffer);
            radiusLast = (Integer)blur.radius.getValue();
        }
    }

    private static float getGaussianOffset(float x, float sigma) {
        float pow = x / sigma;
        return (float)(1.0 / ((double)Math.abs(sigma) * 2.50662827463) * Math.exp(-0.5 * (double)pow * (double)pow));
    }

    static {
        mc = Minecraft.getMinecraft();
        radiusLast = 0;
    }
}

