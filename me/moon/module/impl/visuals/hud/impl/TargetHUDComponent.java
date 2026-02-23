/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.hud.impl;

import java.awt.Color;
import java.util.Objects;
import me.moon.Moon;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.combat.KillAura;
import me.moon.module.impl.visuals.hud.Component;
import me.moon.utils.MathUtils;
import me.moon.utils.blur.MCBlurUtil;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;

public class TargetHUDComponent
extends Component {
    private double desiredWidth;

    public TargetHUDComponent(String name) {
        super(name, 2.0, 2.0);
    }

    @Override
    public void onCompRender(ScaledResolution sr) {
        KillAura killAura = (KillAura)Moon.INSTANCE.getModuleManager().getModule("KillAura");
        if (this.mc.thePlayer != null && this.mc.thePlayer.ticksExisted > 5 && this.mc.theWorld != null && KillAura.target != null && KillAura.target instanceof EntityPlayer || KillAura.target instanceof EntityWither) {
            if (this.mc.thePlayer.getDistanceToEntity(KillAura.target) <= ((Float)killAura.range.getValue()).floatValue() && KillAura.target != this.mc.thePlayer && killAura.isTargetable(KillAura.target, this.mc.thePlayer, false)) {
                GlStateManager.pushMatrix();
                if (!this.mc.gameSettings.ofFastRender) {
                    MCBlurUtil.drawBLURRRR((int)this.getX(), (int)this.getY(), 150, 55, 10.0f);
                }
                EntityLivingBase target = KillAura.target;
                NetworkPlayerInfo networkPlayerInfo = this.mc.getNetHandler().getPlayerInfo(target.getUniqueID());
                String ping = "Ping: " + (Objects.isNull(networkPlayerInfo) ? "0ms" : networkPlayerInfo.getResponseTime() + "ms");
                String playerName = "Name: " + StringUtils.stripControlCodes(target.getName());
                RenderUtil.drawRect(this.getX(), this.getY(), 150.0, 55.0, new Color(0, 0, 0, 120).getRGB());
                this.mc.fontRendererObj.drawStringWithShadow(playerName, (float)this.getX() + 34.5f, (float)this.getY() + 4.0f, Color.gray.getRGB());
                int rwidth = 0;
                int rheight = 0;
                GlStateManager.enableDepth();
                for (int i = 1; i < 5; ++i) {
                    boolean height;
                    ItemStack ia = target.getEquipmentInSlot(i);
                    if (ia == null) continue;
                    RenderHelper.enableStandardItemLighting();
                    boolean width = height = false;
                    if (i == 0) {
                        rwidth = (int)(this.getX() + 5.0);
                        rheight = (int)this.getX();
                    }
                    if (i == 1) {
                        rwidth = (int)(this.getX() + 30.0);
                    } else if (i == 2) {
                        rwidth = (int)(this.getX() + 60.0);
                    } else if (i == 3) {
                        rwidth = (int)(this.getX() + 90.0);
                    } else if (i == 4) {
                        rwidth = (int)(this.getX() + 120.0);
                    } else if (i == 5) {
                        rwidth = (int)(this.getX() + 150.0);
                    }
                    rheight = (int)this.getY();
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(1.5, 1.5, 1.5);
                    this.mc.getRenderItem().renderItemIntoGUI(ia, (float)rwidth / 1.5f, (float)(this.getY() + 14.0) / 1.5f);
                    this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, ia, (float)rwidth / 1.5f, (float)((int)(this.getY() + 14.0)) / 1.5f);
                    GlStateManager.popMatrix();
                    RenderHelper.disableStandardItemLighting();
                }
                GlStateManager.disableDepth();
                TargetHUDComponent.drawEntityOnScreen((int)this.getX() + 15, (int)this.getY() + 42, 20, target.rotationYaw, -target.rotationPitch, target);
                RenderUtil.drawBorderedRect(this.getX() + 3.0, this.getY() + 45.0, 144.0, 8.0, 0.5, new Color(0).getRGB(), new Color(35, 35, 35).getRGB());
                double xSpeed = 143.0f / ((float)Minecraft.getDebugFPS() / 1.2f);
                if (!Float.isNaN(target.getHealth())) {
                    double healthPercent = target.getHealth() / target.getMaxHealth();
                    healthPercent = MathHelper.clamp_double(healthPercent, 0.0, 1.0);
                    double newHp = 143.0 * healthPercent;
                    double animationSpeed = 15.0f / (float)Minecraft.getDebugFPS();
                    this.desiredWidth = AnimationUtil.interpolate2(newHp, this.desiredWidth, animationSpeed);
                    this.desiredWidth = MathUtils.round(this.desiredWidth, 1);
                    RenderUtil.drawRect(this.getX() + 3.5, this.getY() + 45.5, this.desiredWidth, 7.0, this.getHealthColor(target));
                } else {
                    RenderUtil.drawRect(this.getX() + 3.5, this.getY() + 45.5, 143.0, 7.0, this.getHealthColor(target));
                }
                GlStateManager.popMatrix();
            } else {
                this.desiredWidth = 143.0;
            }
        }
    }

    private int getHealthColor(EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0f, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0f, 1.0f, 0.75f) | 0xFF000000;
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.enableDepth();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 255.0f);
        GlStateManager.translate(posX, posY, 50.0f);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-((float)Math.atan(mouseY / 40.0f)) * 20.0f, 1.0f, 0.0f, 0.0f);
        ent.renderYawOffset = mouseX;
        ent.rotationYaw = mouseX;
        ent.rotationPitch = -mouseY;
        ent.rotationYawHead = mouseX;
        ent.prevRotationYawHead = mouseX;
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(ent, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    @Override
    public void onCompUpdate(UpdateEvent event) {
    }

    @Override
    public void onInit() {
    }
}

