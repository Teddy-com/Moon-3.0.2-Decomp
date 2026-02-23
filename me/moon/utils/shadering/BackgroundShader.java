/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 */
package me.moon.utils.shadering;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class BackgroundShader {
    private Map<String, Integer> uniformIDs;
    private int program;
    private long startTime;
    private Framebuffer frameBuffer;
    private final Map<String, Object> shaderSamplers = Maps.newHashMap();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public BackgroundShader(String fragmentShader, String vertexShader) {
        this.uniformIDs = new HashMap<String, Integer>();
        int vertexShaderId = 0;
        int fragmentShaderId = 0;
        try {
            vertexShaderId = this.createShader(vertexShader, 35633);
            fragmentShaderId = this.createShader(fragmentShader, 35632);
            if (vertexShaderId == 0 || fragmentShaderId == 0) {
                return;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            if (vertexShaderId == 0 || fragmentShaderId == 0) {
                return;
            }
        }
        this.program = GL20.glCreateProgram();
        if (this.program == 0) {
            return;
        }
        GL20.glAttachShader((int)this.program, (int)vertexShaderId);
        GL20.glAttachShader((int)this.program, (int)fragmentShaderId);
        GL20.glLinkProgram((int)this.program);
        GL20.glValidateProgram((int)this.program);
        this.startTime = System.currentTimeMillis();
        this.frameBuffer = Minecraft.getMinecraft().getFramebuffer();
        this.frameBuffer.setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void draw() {
        Minecraft mc = Minecraft.getMinecraft();
        GL11.glPushMatrix();
        GL20.glUseProgram((int)this.program);
        GL20.glUniform1f((int)this.getUniformID("time"), (float)((float)(System.currentTimeMillis() - this.startTime) / 1000.0f));
        GL20.glUniform2f((int)this.getUniformID("resolution"), (float)mc.displayWidth, (float)mc.displayHeight);
        GL20.glUniform1f((int)this.getUniformID("time"), (float)((float)(System.currentTimeMillis() - this.startTime) / 1000.0f));
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glColor3f((float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glBegin((int)7);
        GL11.glVertex2f((float)-1.0f, (float)-1.0f);
        GL11.glVertex2f((float)1.0f, (float)-1.0f);
        GL11.glVertex2f((float)1.0f, (float)1.0f);
        GL11.glVertex2f((float)-1.0f, (float)1.0f);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL20.glUseProgram((int)0);
        GL11.glPopMatrix();
    }

    private int getUniformID(String name) {
        if (this.uniformIDs.containsKey(name)) {
            return this.uniformIDs.get(name);
        }
        int uniformID = GL20.glGetUniformLocation((int)this.program, (CharSequence)name);
        this.uniformIDs.put(name, uniformID);
        return uniformID;
    }

    private int createShader(String shaderSource, int shaderType) {
        int shaderId = GL20.glCreateShader((int)shaderType);
        if (shaderId == 0) {
            return -1;
        }
        GL20.glShaderSource((int)shaderId, (CharSequence)shaderSource);
        GL20.glCompileShader((int)shaderId);
        if (GL20.glGetShaderi((int)shaderId, (int)35713) == 0) {
            System.err.println(GL20.glGetShaderInfoLog((int)shaderId, (int)GL20.glGetShaderi((int)shaderId, (int)35716)));
            return -1;
        }
        return shaderId;
    }
}

