/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals;

import java.util.HashMap;
import java.util.Map;
import me.moon.event.Handler;
import me.moon.event.impl.render.Render3DEvent;
import me.moon.module.Module;
import me.moon.module.impl.visuals.components.ComponentManager;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class SkeletonESP
extends Module {
    private final BooleanValue visibleonly = new BooleanValue("VisibleOnly", false);
    private static final Map<EntityPlayer, float[][]> entities = new HashMap<EntityPlayer, float[][]>();
    private final NumberValue<Float> width = new NumberValue<Float>("Width", Float.valueOf(1.0f), Float.valueOf(0.5f), Float.valueOf(10.0f), Float.valueOf(0.1f));

    public SkeletonESP() {
        super("SkeletonESP", Module.Category.VISUALS, 0);
        this.setDescription("Pretty obvious");
        this.setHidden(true);
    }

    @Handler(value=Render3DEvent.class)
    public void onRender2D(Render3DEvent e) {
        this.startEnd(true);
        GL11.glPushMatrix();
        GL11.glEnable((int)2903);
        GL11.glDisable((int)2848);
        entities.keySet().removeIf(this::doesntContain);
        this.mc.theWorld.playerEntities.forEach(player -> this.drawSkeleton(e, (EntityPlayer)player));
        this.startEnd(false);
        GL11.glPopMatrix();
    }

    private void drawSkeleton(Render3DEvent event, EntityPlayer e) {
        float[][] entPos;
        if ((!e.isInvisible() || e.isInvisible() && this.visibleonly.getValue().booleanValue()) && (entPos = entities.get(e)) != null && e.deathTime == 0 && RenderUtil.isInViewFrustrum(e) && !e.isDead && e != this.mc.thePlayer && !e.isPlayerSleeping() && (!this.visibleonly.isEnabled() || this.mc.thePlayer.canEntityBeSeen(e))) {
            GL11.glPushMatrix();
            GL11.glEnable((int)2848);
            GL11.glLineWidth((float)((Float)this.width.getValue()).floatValue());
            if (ComponentManager.isGenesisTheme()) {
                GL11.glLineWidth((float)3.0f);
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            Vec3 vec = this.getVec3(event, e);
            double x = vec.xCoord - this.mc.getRenderManager().renderPosX;
            double y = vec.yCoord - this.mc.getRenderManager().renderPosY;
            double z = vec.zCoord - this.mc.getRenderManager().renderPosZ;
            GL11.glTranslated((double)x, (double)y, (double)z);
            float xOff = e.prevRenderYawOffset + (e.renderYawOffset - e.prevRenderYawOffset) * event.getPartialTicks();
            if (ComponentManager.isGenesisTheme()) {
                xOff = this.mc.getRenderManager().playerViewY;
            }
            GL11.glRotatef((float)(-xOff), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glTranslated((double)0.0, (double)0.0, (double)(e.isSneaking() ? -0.235 : 0.0));
            float yOff = e.isSneaking() ? 0.6f : 0.75f;
            GL11.glPushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslated((double)-0.125, (double)yOff, (double)0.0);
            if (!ComponentManager.isGenesisTheme()) {
                if (entPos[3][0] != 0.0f) {
                    GL11.glRotatef((float)((float)((double)(entPos[3][0] * 180.0f) / Math.PI)), (float)1.0f, (float)0.0f, (float)0.0f);
                }
                if (entPos[3][1] != 0.0f) {
                    GL11.glRotatef((float)((float)((double)(entPos[3][1] * 180.0f) / Math.PI)), (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (entPos[3][2] != 0.0f) {
                    GL11.glRotatef((float)((float)((double)(entPos[3][2] * 180.0f) / Math.PI)), (float)0.0f, (float)0.0f, (float)1.0f);
                }
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)(-yOff), (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslated((double)0.125, (double)yOff, (double)0.0);
            if (!ComponentManager.isGenesisTheme()) {
                if (entPos[4][0] != 0.0f) {
                    GL11.glRotatef((float)((float)((double)(entPos[4][0] * 180.0f) / Math.PI)), (float)1.0f, (float)0.0f, (float)0.0f);
                }
                if (entPos[4][1] != 0.0f) {
                    GL11.glRotatef((float)((float)((double)(entPos[4][1] * 180.0f) / Math.PI)), (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (entPos[4][2] != 0.0f) {
                    GL11.glRotatef((float)((float)((double)(entPos[4][2] * 180.0f) / Math.PI)), (float)0.0f, (float)0.0f, (float)1.0f);
                }
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)(-yOff), (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glTranslated((double)0.0, (double)0.0, (double)(e.isSneaking() ? 0.25 : 0.0));
            GL11.glPushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslated((double)0.0, (double)(e.isSneaking() ? -0.05 : 0.0), (double)(e.isSneaking() ? -0.01725 : 0.0));
            GL11.glPushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslated((double)-0.375, (double)((double)yOff + 0.55), (double)0.0);
            if (!ComponentManager.isGenesisTheme()) {
                if (entPos[1][0] != 0.0f) {
                    GL11.glRotatef((float)((float)((double)(entPos[1][0] * 180.0f) / Math.PI)), (float)1.0f, (float)0.0f, (float)0.0f);
                }
                if (entPos[1][1] != 0.0f) {
                    GL11.glRotatef((float)((float)((double)(entPos[1][1] * 180.0f) / Math.PI)), (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (entPos[1][2] != 0.0f) {
                    GL11.glRotatef((float)((float)((double)(-entPos[1][2] * 180.0f) / Math.PI)), (float)0.0f, (float)0.0f, (float)1.0f);
                }
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)-0.5, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.375, (double)((double)yOff + 0.55), (double)0.0);
            if (!ComponentManager.isGenesisTheme()) {
                if (entPos[2][0] != 0.0f) {
                    GL11.glRotatef((float)((float)((double)(entPos[2][0] * 180.0f) / Math.PI)), (float)1.0f, (float)0.0f, (float)0.0f);
                }
                if (entPos[2][1] != 0.0f) {
                    GL11.glRotatef((float)((float)((double)(entPos[2][1] * 180.0f) / Math.PI)), (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (entPos[2][2] != 0.0f) {
                    GL11.glRotatef((float)((float)((double)(-entPos[2][2] * 180.0f) / Math.PI)), (float)0.0f, (float)0.0f, (float)1.0f);
                }
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)-0.5, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glRotatef((float)(xOff - e.rotationYawHead), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glPushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslated((double)0.0, (double)((double)yOff + 0.55), (double)0.0);
            if (!ComponentManager.isGenesisTheme() && entPos[0][0] != 0.0f) {
                GL11.glRotatef((float)((float)((double)(entPos[0][0] * 180.0f) / Math.PI)), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)0.3, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GL11.glRotatef((float)(e.isSneaking() ? 25.0f : 0.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glTranslated((double)0.0, (double)(e.isSneaking() ? -0.16175 : 0.0), (double)(e.isSneaking() ? -0.48025 : 0.0));
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.0, (double)yOff, (double)0.0);
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)-0.125, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.125, (double)0.0, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslated((double)0.0, (double)yOff, (double)0.0);
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)0.55, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.0, (double)((double)yOff + 0.55), (double)0.0);
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)-0.375, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.375, (double)0.0, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }

    private Vec3 getVec3(Render3DEvent event, EntityPlayer var0) {
        float pt = event.getPartialTicks();
        double x = var0.lastTickPosX + (var0.posX - var0.lastTickPosX) * (double)pt;
        double y = var0.lastTickPosY + (var0.posY - var0.lastTickPosY) * (double)pt;
        double z = var0.lastTickPosZ + (var0.posZ - var0.lastTickPosZ) * (double)pt;
        return new Vec3(x, y, z);
    }

    public static void addEntity(EntityPlayer e, ModelPlayer model) {
        entities.put(e, new float[][]{{model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ}, {model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY, model.bipedRightArm.rotateAngleZ}, {model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY, model.bipedLeftArm.rotateAngleZ}, {model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY, model.bipedRightLeg.rotateAngleZ}, {model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ}});
    }

    private boolean doesntContain(EntityPlayer var0) {
        return !this.mc.theWorld.playerEntities.contains(var0);
    }

    private void startEnd(boolean revert) {
        if (revert) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GL11.glEnable((int)2848);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GL11.glHint((int)3154, (int)4354);
        } else {
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GL11.glDisable((int)2848);
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
        GlStateManager.depthMask(!revert);
    }
}

