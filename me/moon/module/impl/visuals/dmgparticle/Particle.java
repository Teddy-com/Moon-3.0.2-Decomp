/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.dmgparticle;

import java.awt.Color;
import me.moon.utils.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;

public class Particle {
    private Minecraft mc = Minecraft.getMinecraft();
    private EntityLivingBase entity;
    public float posX;
    public float posY;
    public float posZ;
    public float healthOfEntity;
    public float maxHealthOfEntity;
    public long creationDate;
    public float damage;
    private String colorCode = "";

    public Particle(EntityLivingBase entity, float healthOfEntity, float maxHealthOfEntity, long creationDate, float damage) {
        this.posX = (float)entity.getPosition().getX() + entity.width / 2.0f + MathUtils.getRandomInRange(-0.2f, 0.2f);
        this.posY = (float)entity.getPosition().getY() + entity.height / 2.0f + MathUtils.getRandomInRange(-0.1f, 0.2f);
        this.posZ = (float)entity.getPosition().getZ() + entity.width / 2.0f + MathUtils.getRandomInRange(-0.2f, 0.2f);
        this.healthOfEntity = healthOfEntity;
        this.creationDate = creationDate;
        this.maxHealthOfEntity = maxHealthOfEntity;
        this.damage = damage;
        this.entity = entity;
        if (entity instanceof EntityOtherPlayerMP) {
            EntityOtherPlayerMP finishedEntity = (EntityOtherPlayerMP)entity;
            this.colorCode = damage <= 2.0f && finishedEntity.isBlocking() || damage <= 2.0f && finishedEntity.isSwingInProgress ? "\u00a7a" : (damage <= maxHealthOfEntity / 4.0f ? "\u00a7e" : "\u00a7c");
        } else {
            this.colorCode = damage <= maxHealthOfEntity / 4.0f ? "\u00a7e" : "\u00a7c";
        }
    }

    public void drawParticle() {
        this.posY += 0.1f / (float)Minecraft.getDebugFPS();
        GlStateManager.pushMatrix();
        GlStateManager.translate((double)this.posX - this.mc.getRenderManager().renderPosX, (double)this.posY - this.mc.getRenderManager().renderPosY, (double)this.posZ - this.mc.getRenderManager().renderPosZ);
        GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-0.027f, -0.027f, 0.027f);
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        this.mc.fontRendererObjCustom.drawStringWithShadow(this.colorCode + this.damage, 0.0f, 0.0f, -1);
        GlStateManager.enableDepth();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    private int getHealthColor() {
        float f = this.healthOfEntity;
        float f1 = this.maxHealthOfEntity;
        float f2 = Math.max(0.0f, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0f, 1.0f, 1.0f) | 0xFF000000;
    }
}

