/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.render.Render3DEvent;
import me.moon.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemSnowball;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class Trajectories
extends Module {
    public Trajectories() {
        super("Trajectories", Module.Category.VISUALS, new Color(12697657).getRGB());
    }

    @Handler(value=Render3DEvent.class)
    public void onRender(Render3DEvent event) {
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        this.mc.entityRenderer.orientCamera(event.getPartialTicks());
        GL11.glTranslated((double)(-this.mc.getRenderManager().renderPosX), (double)(-this.mc.getRenderManager().renderPosY), (double)(-this.mc.getRenderManager().renderPosZ));
        EntityPlayerSP player = this.mc.thePlayer;
        if (this.mc.thePlayer.getHeldItem() != null && this.isThrowable(this.mc.thePlayer.getHeldItem().getItem())) {
            double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)event.getPartialTicks() - (double)(MathHelper.cos((float)Math.toRadians(player.rotationYaw)) * 0.16f);
            double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)event.getPartialTicks() + (double)player.getEyeHeight() - 0.100149011612;
            double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)event.getPartialTicks() - (double)(MathHelper.sin((float)Math.toRadians(player.rotationYaw)) * 0.16f);
            float con = 1.0f;
            if (!(player.inventory.getCurrentItem().getItem() instanceof ItemBow)) {
                con = 0.4f;
            }
            double motionX = -MathHelper.sin((float)Math.toRadians(player.rotationYaw)) * MathHelper.cos((float)Math.toRadians(player.rotationPitch)) * con;
            double motionZ = MathHelper.cos((float)Math.toRadians(player.rotationYaw)) * MathHelper.cos((float)Math.toRadians(player.rotationPitch)) * con;
            double motionY = -MathHelper.sin((float)Math.toRadians(player.rotationPitch)) * con;
            double ssum = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
            motionX /= ssum;
            motionY /= ssum;
            motionZ /= ssum;
            GL11.glColor4d((double)1.0, (double)0.0, (double)0.0, (double)0.5);
            if (player.inventory.getCurrentItem().getItem() instanceof ItemBow) {
                float pow = (float)(72000 - player.getItemInUseCount()) / 20.0f;
                if ((pow = (pow * pow + pow * 2.0f) / 3.0f) > 1.0f) {
                    pow = 1.0f;
                }
                if (pow <= 0.1f) {
                    pow = 1.0f;
                }
                pow *= 2.0f;
                motionX *= (double)(pow *= 1.5f);
                motionY *= (double)pow;
                motionZ *= (double)pow;
            } else {
                motionX *= 1.5;
                motionY *= 1.5;
                motionZ *= 1.5;
            }
            GL11.glPushMatrix();
            this.enableDefaults();
            GL11.glLineWidth((float)1.5f);
            GL11.glBegin((int)3);
            double gravity = this.getGravity(player.inventory.getCurrentItem().getItem());
            for (int q = 0; q < 1000; ++q) {
                double rx = x * 1.0;
                double ry = y * 1.0;
                double rz = z * 1.0;
                GL11.glColor3d((double)1.0, (double)0.0, (double)0.0);
                GL11.glVertex3d((double)rx, (double)ry, (double)rz);
                x += motionX;
                y += motionY;
                z += motionZ;
                motionX *= 0.99;
                motionY *= 0.99;
                motionZ *= 0.99;
                motionY -= gravity;
            }
            GL11.glEnd();
            GL11.glPopMatrix();
            this.disableDefaults();
        }
        GL11.glTranslated((double)this.mc.getRenderManager().renderPosX, (double)this.mc.getRenderManager().renderPosY, (double)this.mc.getRenderManager().renderPosZ);
        GL11.glPopMatrix();
    }

    private void enableDefaults() {
        GL11.glDisable((int)2896);
        GL11.glEnable((int)2848);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glEnable((int)32925);
        GL11.glDepthMask((boolean)false);
    }

    private void disableDefaults() {
        GL11.glEnable((int)2929);
        GL11.glDisable((int)32925);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)2896);
        GL11.glEnable((int)2929);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2884);
    }

    private double getGravity(Item item) {
        return item instanceof ItemBow ? 0.05 : 0.03;
    }

    private boolean isThrowable(Item item) {
        return item instanceof ItemBow || item instanceof ItemSnowball || item instanceof ItemEgg || item instanceof ItemEnderPearl;
    }
}

