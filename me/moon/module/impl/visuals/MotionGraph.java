/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals;

import me.moon.event.Handler;
import me.moon.event.impl.game.TickEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.Module;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class MotionGraph
extends Module {
    private NumberValue<Integer> secondsUntilDisappear = new NumberValue<Integer>("Dissapear Time", 10, 1, 1000, 1);
    private int lastLength;
    private float[] motions;
    private float lastXDifference;
    private float lastZDifference;

    public MotionGraph() {
        super("MotionGraph", Module.Category.VISUALS, -1);
    }

    @Handler(value=Render2DEvent.class)
    public void onRender(Render2DEvent event) {
        ScaledResolution sr = event.getScaledResolution();
        if (this.mc.thePlayer != null && this.motions != null) {
            int i;
            float mid = (float)sr.getScaledWidth() / 2.0f;
            float width = 150.0f;
            float x = mid - width / 2.0f;
            float y = sr.getScaledHeight() - 100;
            float differenceX = (float)(this.mc.thePlayer.posX - this.mc.thePlayer.lastTickPosX);
            float differenceZ = (float)(this.mc.thePlayer.posZ - this.mc.thePlayer.lastTickPosZ);
            if (!Float.isNaN(this.lastZDifference) && !Float.isNaN(this.lastXDifference)) {
                differenceX = (float)RenderUtil.interpolate(differenceX, this.lastXDifference, this.mc.timer.renderPartialTicks);
                differenceZ = (float)RenderUtil.interpolate(differenceZ, this.lastZDifference, this.mc.timer.renderPartialTicks);
            }
            this.motions[0] = (float)Math.hypot(differenceX, differenceZ) * 50.0f;
            for (i = this.lastLength - 1; i > 0; --i) {
                this.motions[i] = this.motions[i - 1];
            }
            GL11.glPushMatrix();
            GL11.glLineWidth((float)1.5f);
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GL11.glEnable((int)2848);
            GL11.glBegin((int)3);
            for (i = 0; i < this.lastLength; ++i) {
                float currentMotion = i > 0 ? (float)RenderUtil.interpolate(this.motions[i], this.motions[i - 1], this.mc.timer.renderPartialTicks) : this.motions[i];
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glVertex2d((double)(x + (float)i * (width / (float)this.lastLength)), (double)(y - currentMotion));
            }
            GL11.glEnd();
            GL11.glDisable((int)2848);
            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GL11.glPopMatrix();
        }
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        int secondsToTicks = (Integer)this.secondsUntilDisappear.getValue() * 50;
        if (this.lastLength != secondsToTicks) {
            this.lastLength = secondsToTicks;
            this.motions = new float[this.lastLength];
        }
    }

    @Handler(value=TickEvent.class)
    public void onTick(TickEvent event) {
        if (this.mc.thePlayer != null) {
            this.lastXDifference = (float)(this.mc.thePlayer.posX - this.mc.thePlayer.lastTickPosX);
            this.lastZDifference = (float)(this.mc.thePlayer.posZ - this.mc.thePlayer.lastTickPosZ);
        }
    }
}

