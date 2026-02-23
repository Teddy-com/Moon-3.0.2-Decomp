/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals;

import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.event.impl.render.Render3DEvent;
import me.moon.module.Module;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class BreadCrumbs
extends Module {
    private NumberValue<Integer> secondsUntilDisappear = new NumberValue<Integer>("Seconds until Disappear", 10, 0, 100, 1);
    private Vec3[] vectors;
    private int lastLength;

    public BreadCrumbs() {
        super("BreadCrumbs", Module.Category.VISUALS, -1);
    }

    @Handler(value=Render3DEvent.class)
    public void onRender3d(Render3DEvent event) {
        this.renderBreadcrumbs();
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        int secondsToTicks = (Integer)this.secondsUntilDisappear.getValue() * 50;
        if (this.lastLength != secondsToTicks) {
            this.lastLength = secondsToTicks;
            this.vectors = new Vec3[this.lastLength];
        }
    }

    public void onShadering() {
        this.mc.entityRenderer.setupCameraTransform(this.mc.timer.renderPartialTicks, 0);
        if (this.isEnabled()) {
            this.renderBreadcrumbs();
        }
        this.mc.entityRenderer.setupOverlayRendering();
    }

    public void renderBreadcrumbs() {
        if (this.mc.thePlayer != null && this.vectors != null) {
            int i;
            float xplayer = (float)RenderUtil.interpolate(this.mc.thePlayer.posX, this.mc.thePlayer.lastTickPosX, this.mc.timer.renderPartialTicks);
            float yplayer = (float)RenderUtil.interpolate(this.mc.thePlayer.posY, this.mc.thePlayer.lastTickPosY, this.mc.timer.renderPartialTicks);
            float zplayer = (float)RenderUtil.interpolate(this.mc.thePlayer.posZ, this.mc.thePlayer.lastTickPosZ, this.mc.timer.renderPartialTicks);
            this.vectors[0] = new Vec3(xplayer, yplayer, zplayer);
            for (i = this.lastLength - 1; i > 0; --i) {
                this.vectors[i] = this.vectors[i - 1];
            }
            GL11.glPushMatrix();
            GL11.glLineWidth((float)2.0f);
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.translate(-this.mc.getRenderManager().renderPosX, -this.mc.getRenderManager().renderPosY, -this.mc.getRenderManager().renderPosZ);
            GL11.glEnable((int)2848);
            GL11.glBegin((int)3);
            for (i = 0; i < this.lastLength; ++i) {
                Vec3 currVec = this.vectors[i];
                if (currVec == null) continue;
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f - 1.0f / (float)this.lastLength * (float)i);
                GL11.glVertex3d((double)currVec.xCoord, (double)currVec.yCoord, (double)currVec.zCoord);
            }
            GL11.glEnd();
            GL11.glDisable((int)2848);
            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GL11.glPopMatrix();
        }
    }
}

