/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.IOUtils
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 */
package me.moon.utils.shadering;

import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class ShaderUtil {
    private int program;

    public ShaderUtil(String fragmenShader, String vertexShader) {
        int fragment;
        int vertex;
        try {
            InputStream vertexStream = this.getClass().getResourceAsStream("/assets/minecraft/textures/client/shaders/" + vertexShader);
            vertex = this.newShader(IOUtils.toString((InputStream)vertexStream), 35633);
            IOUtils.closeQuietly((InputStream)vertexStream);
            InputStream fragmentStream = this.getClass().getResourceAsStream("/assets/minecraft/textures/client/shaders/" + fragmenShader);
            fragment = this.newShader(IOUtils.toString((InputStream)fragmentStream), 35632);
            IOUtils.closeQuietly((InputStream)fragmentStream);
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (vertex == 0 || fragment == 0) {
            return;
        }
        this.program = GL20.glCreateProgram();
        if (this.program == 0) {
            return;
        }
        GL20.glAttachShader((int)this.program, (int)vertex);
        GL20.glAttachShader((int)this.program, (int)fragment);
        GL20.glLinkProgram((int)this.program);
        GL20.glValidateProgram((int)this.program);
    }

    public void startShader() {
        if (Minecraft.getMinecraft().gameSettings.ofFastRender) {
            return;
        }
        GL20.glUseProgram((int)this.program);
    }

    public void stopShader() {
        if (Minecraft.getMinecraft().gameSettings.ofFastRender) {
            return;
        }
        GL20.glUseProgram((int)0);
    }

    private int newShader(String src, int type) {
        int id = 0;
        try {
            id = GL20.glCreateShader((int)type);
            if (id == 0) {
                return 0;
            }
            GL20.glShaderSource((int)id, (CharSequence)src);
            GL20.glCompileShader((int)id);
            return id;
        }
        catch (Exception exception) {
            GL20.glDeleteShader((int)id);
            throw exception;
        }
    }

    public Framebuffer createBuffer(Framebuffer framebuffer) {
        if (framebuffer != null) {
            framebuffer.deleteFramebuffer();
        }
        framebuffer = new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, true);
        return framebuffer;
    }

    public void drawQuads(Framebuffer framebuffer) {
        if (Minecraft.getMinecraft().gameSettings.ofFastRender) {
            return;
        }
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int width = sr.getScaledWidth();
        int height = sr.getScaledHeight();
        GL11.glBindTexture((int)3553, (int)framebuffer.framebufferTexture);
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex2f((float)0.0f, (float)0.0f);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex2f((float)0.0f, (float)height);
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex2f((float)width, (float)height);
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex2f((float)width, (float)0.0f);
        GL11.glEnd();
    }

    public int getProgramId() {
        return this.program;
    }
}

