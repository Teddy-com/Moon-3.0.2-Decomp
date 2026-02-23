/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals.components.impl;

import java.awt.Color;
import me.moon.Moon;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.impl.combat.KillAura;
import me.moon.module.impl.other.StreamerMode;
import me.moon.module.impl.visuals.HUD;
import me.moon.module.impl.visuals.components.Component;
import me.moon.utils.MathUtils;
import me.moon.utils.blur.MCBlurUtil;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.game.ServerUtils;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TargetHUDComponent
extends Component {
    private AnimationUtil animationUtil = new AnimationUtil(0.0, 0.0);
    private AnimationUtil animationUtil2 = new AnimationUtil(0.0, 0.0);
    private AnimationUtil animationUtil3 = new AnimationUtil(0.0, 0.0);
    private AnimationUtil animationUtil4 = new AnimationUtil(0.0, 0.0);

    @Override
    public void render(Render2DEvent event) {
        ScaledResolution sr = event.getScaledResolution();
        if (Moon.INSTANCE.getModuleManager().getModule("Killaura").isEnabled() && KillAura.target != null) {
            GlStateManager.enableDepth();
            GlStateManager.disableAlpha();
            switch (this.getTargetHUDMode()) {
                case ASTOLFOOLD: {
                    if (KillAura.target == null) break;
                    EntityLivingBase entity = KillAura.target;
                    FontRenderer font = this.mc.fontRendererObjCustom;
                    float width = 127.0f;
                    float height = 45.0f;
                    float x = (float)sr.getScaledWidth() / 2.0f - width / 2.0f;
                    float y = (float)sr.getScaledHeight() / 2.0f + 30.0f;
                    float healthWidth = 94.0f;
                    if (Minecraft.getMinecraft().thePlayer == null) break;
                    RenderUtil.drawRect(x, y, width, height, -1442840576);
                    font.drawStringWithShadow((Moon.INSTANCE.getModuleManager().getModule("StreamerMode").isEnabled() && StreamerMode.hideNames.getValue() != false ? "\u00a7k" : "") + entity.getName(), x + 28.0f, y + 4.0f, Color.white.getRGB());
                    float healthRectWidth = healthWidth / entity.getMaxHealth() * Math.min(entity.getHealth(), entity.getMaxHealth());
                    this.animationUtil.interpolate(healthRectWidth, 0.0, 25.0f / (float)Minecraft.getDebugFPS());
                    RenderUtil.drawRect((double)x + 28.0, (double)(y + height) - 31.0, this.animationUtil.getPosX(), 12.0, this.getHealthColor(entity));
                    font.drawStringWithShadow(MathUtils.round(entity.getHealth() / 2.0f, 2) + "", x + 65.0f, y + height - 31.0f, this.getHealthColor(entity));
                    this.drawEntityOnScreen((int)x + 14, (int)y + 41, 18, entity.rotationYaw, -entity.rotationPitch, entity);
                    break;
                }
                case MOON: {
                    double end;
                    double inc;
                    String targetName;
                    EntityLivingBase targetEntity;
                    if (KillAura.target == null || !((targetEntity = KillAura.target) instanceof EntityOtherPlayerMP)) break;
                    EntityOtherPlayerMP target = (EntityOtherPlayerMP)targetEntity;
                    float health = target.getHealth();
                    String string = targetName = Moon.INSTANCE.getModuleManager().getModule("StreamerMode").isEnabled() && StreamerMode.hideNames.getValue() != false ? "Censored" : target.getName();
                    if (!Float.isNaN(target.getHealth())) {
                        if (this.animationUtil.getPosX() == 0.0) {
                            this.animationUtil.setPosX((float)(35 + Math.max(Fonts.moon.getStringWidth(targetName), Fonts.moon.getStringWidth("Health: " + MathUtils.roundDouble(health, 1))) + 8) / target.getMaxHealth());
                        }
                        MCBlurUtil.drawBLURRRR(sr.getScaledWidth() / 2 + 32, sr.getScaledHeight() / 2 + 43, 35 + Math.max(Fonts.moonMiddle.getStringWidth(targetName), Fonts.moon.getStringWidth("Health: " + MathUtils.roundDouble(health, 1))) + 10, 36, 15.0f);
                        RenderUtil.drawBorderedRect((float)sr.getScaledWidth() / 2.0f + 32.0f, (double)((float)sr.getScaledHeight() / 2.0f) + 42.5, 35 + Math.max(Fonts.moonMiddle.getStringWidth(targetName), Fonts.moon.getStringWidth("Health: " + MathUtils.roundDouble(health, 1))) + 10, 36.5, 0.0, new Color(11, 11, 11, 60).getRGB(), new Color(21, 21, 21, 95).getRGB());
                        inc = (float)(35 + Math.max(Fonts.moonMiddle.getStringWidth(targetName), Fonts.moon.getStringWidth("Health: " + MathUtils.roundDouble(health, 1))) + 8) / 20.0f;
                        end = inc * (double)Math.min(target.getTotalArmorValue(), 20);
                        this.animationUtil2.interpolate(Math.max(end, 0.5), 0.0, 15.0f / (float)Minecraft.getDebugFPS());
                        RenderUtil.drawBorderedRect(sr.getScaledWidth() / 2 + 33, (float)sr.getScaledHeight() / 2.0f + 75.5f, 35 + Math.max(Fonts.moonMiddle.getStringWidth(targetName), Fonts.moon.getStringWidth("Health: " + MathUtils.roundDouble(health, 1))) + 8, 2.0, 0.5, new Color(25, 25, 25).getRGB(), new Color(11, 11, 11, 150).getRGB());
                        RenderUtil.drawBorderedRect(sr.getScaledWidth() / 2 + 33, (float)sr.getScaledHeight() / 2.0f + 75.5f, this.animationUtil2.getPosX(), 2.0, 0.5, new Color(25, 25, 25).getRGB(), -15374912);
                    } else {
                        MCBlurUtil.drawBLURRRR(sr.getScaledWidth() / 2 + 32, sr.getScaledHeight() / 2 + 43, 35 + Math.max(Fonts.moonMiddle.getStringWidth(targetName), Fonts.moon.getStringWidth("Health: " + MathUtils.roundDouble(0.0, 1))) + 10, 36, 15.0f);
                        RenderUtil.drawBorderedRect((float)sr.getScaledWidth() / 2.0f + 32.0f, (double)((float)sr.getScaledHeight() / 2.0f) + 42.5, 35 + Math.max(Fonts.moonMiddle.getStringWidth(targetName), Fonts.moon.getStringWidth("Health: " + MathUtils.roundDouble(0.0, 1))) + 10, 36.5, 0.0, new Color(11, 11, 11, 60).getRGB(), new Color(21, 21, 21, 95).getRGB());
                    }
                    this.drawFace(sr.getScaledWidth() / 2 + 34, (float)(sr.getScaledHeight() / 2) + 45.5f, 8.0f, 8.0f, 8, 8, 25, 25, 64.0f, 64.0f, target);
                    if (!Float.isNaN(target.getHealth())) {
                        inc = (float)(35 + Math.max(Fonts.moonMiddle.getStringWidth(targetName), Fonts.moon.getStringWidth("Health: " + MathUtils.roundDouble(health, 1))) + 8) / target.getMaxHealth();
                        end = inc * (double)Math.min(health, target.getMaxHealth());
                        this.animationUtil.interpolate(Math.max(end, 0.5), 0.0, 15.0f / (float)Minecraft.getDebugFPS());
                        RenderUtil.drawBorderedRect(sr.getScaledWidth() / 2 + 33, (float)sr.getScaledHeight() / 2.0f + 71.5f, 35 + Math.max(Fonts.moonMiddle.getStringWidth(targetName), Fonts.moon.getStringWidth("Health: " + MathUtils.roundDouble(health, 1))) + 8, 2.0, 0.5, new Color(25, 25, 25).getRGB(), new Color(11, 11, 11, 150).getRGB());
                        RenderUtil.drawBorderedRect(sr.getScaledWidth() / 2 + 33, (float)sr.getScaledHeight() / 2.0f + 71.5f, this.animationUtil.getPosX(), 2.0, 0.5, new Color(25, 25, 25).getRGB(), this.getHealthColor(target));
                        Fonts.moon.drawStringWithShadow("Health: " + MathUtils.roundDouble(health, 1), sr.getScaledWidth() / 2 + 61, sr.getScaledHeight() / 2 + 60, -1);
                    } else {
                        RenderUtil.drawBorderedRect(sr.getScaledWidth() / 2 + 33, (float)sr.getScaledHeight() / 2.0f + 71.5f, 35 + Math.max(Fonts.moonMiddle.getStringWidth(targetName), Fonts.moon.getStringWidth("Health: " + MathUtils.roundDouble(0.0, 1))) + 8, 2.0, 0.5, new Color(25, 25, 25).getRGB(), new Color(11, 11, 11, 150).getRGB());
                        Fonts.moon.drawStringWithShadow("Health: " + MathUtils.roundDouble(0.0, 1), sr.getScaledWidth() / 2 + 61, sr.getScaledHeight() / 2 + 60, -1);
                    }
                    Fonts.moonMiddle.drawStringWithShadow(targetName, sr.getScaledWidth() / 2 + 61, sr.getScaledHeight() / 2 + 46, -1);
                    break;
                }
                case ASTOLFOO: {
                    EntityLivingBase targetEntity;
                    if (KillAura.target == null || !((targetEntity = KillAura.target) instanceof EntityOtherPlayerMP)) break;
                    EntityOtherPlayerMP target = (EntityOtherPlayerMP)targetEntity;
                    float health = target.getHealth();
                    if (!Float.isNaN(target.getHealth())) {
                        if (this.animationUtil.getPosX() == 0.0) {
                            this.animationUtil.setPosX(194.0f / target.getMaxHealth() / 2.0f);
                        }
                        RenderUtil.drawBorderedRect((float)sr.getScaledWidth() / 2.0f - 100.0f, (float)sr.getScaledHeight() / 2.0f + 45.0f, 200.0, 70.0, 0.0, new Color(11, 11, 11, 220).getRGB(), new Color(11, 11, 11, 159).getRGB());
                    } else {
                        RenderUtil.drawBorderedRect((float)sr.getScaledWidth() / 2.0f - 100.0f, (float)sr.getScaledHeight() / 2.0f + 45.0f, 200.0, 70.0, 0.0, new Color(11, 11, 11, 220).getRGB(), new Color(11, 11, 11, 159).getRGB());
                    }
                    if (!Float.isNaN(target.getHealth())) {
                        double inc = 194.0f / target.getMaxHealth();
                        double end = inc * (double)Math.min(health, target.getMaxHealth());
                        this.animationUtil.linear(Math.max(end, 0.5), 0.0, 200.0f / (float)Minecraft.getDebugFPS());
                        RenderUtil.drawBorderedRect(sr.getScaledWidth() / 2 - 97, (float)sr.getScaledHeight() / 2.0f + 106.5f, 194.0, 6.0, 0.0, new Color(this.getHealthColor(target)).darker().darker().getRGB(), new Color(this.getHealthColor(target)).darker().darker().getRGB());
                        RenderUtil.drawBorderedRect(sr.getScaledWidth() / 2 - 97, (float)sr.getScaledHeight() / 2.0f + 106.5f, this.animationUtil.getPosX(), 6.0, 0.0, new Color(this.getHealthColor(target)).darker().darker().getRGB(), this.getHealthColor(target));
                        GL11.glPushMatrix();
                        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
                        this.mc.fontRendererObj.drawStringWithShadow(MathUtils.round(target.getHealth() / 2.0f, 1) + " \u2764", ((float)sr.getScaledWidth() / 2.0f - 54.0f) / 2.0f, (sr.getScaledHeight() / 2 + 64) / 2, this.getHealthColor(target));
                        GL11.glPopMatrix();
                    } else {
                        RenderUtil.drawBorderedRect(sr.getScaledWidth() / 2 - 97, (float)sr.getScaledHeight() / 2.0f + 106.5f, 194.0, 6.0, 0.5, new Color(this.getHealthColor(target)).darker().darker().getRGB(), new Color(this.getHealthColor(target)).darker().darker().getRGB());
                        GL11.glPushMatrix();
                        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
                        this.mc.fontRendererObj.drawStringWithShadow("NaN \u2764", ((float)sr.getScaledWidth() / 2.0f - 54.0f) / 2.0f, (sr.getScaledHeight() / 2 + 64) / 2, this.getHealthColor(target));
                        GL11.glPopMatrix();
                    }
                    this.mc.fontRendererObj.drawStringWithShadow(target.getName(), (float)sr.getScaledWidth() / 2.0f - 52.0f, sr.getScaledHeight() / 2 + 50, -1);
                    if (!ServerUtils.isOnHypixel()) {
                        GL11.glPushMatrix();
                        GL11.glScalef((float)1.5f, (float)1.5f, (float)1.5f);
                        EntityOtherPlayerMP abstractClientPlayer = target;
                        NetworkPlayerInfo networkPlayerInfo = abstractClientPlayer.getPlayerInfo();
                        if (networkPlayerInfo != null && !Float.isNaN(networkPlayerInfo.getResponseTime()) && networkPlayerInfo.getResponseTime() != 0) {
                            this.mc.fontRendererObj.drawStringWithShadow(networkPlayerInfo.getResponseTime() + "ms", ((float)sr.getScaledWidth() / 2.0f - 54.0f) / 1.5f, (float)(sr.getScaledHeight() / 2 + 84) / 1.5f, new Color(150, 150, 150).getRGB());
                        }
                        GL11.glPopMatrix();
                    }
                    int rwidth = 0;
                    for (int i = 1; i < 5; ++i) {
                        boolean height;
                        ItemStack ia = target.getEquipmentInSlot(i);
                        if (ia == null) continue;
                        RenderHelper.enableStandardItemLighting();
                        boolean width = height = false;
                        if (i == 1) {
                            rwidth = (int)((float)sr.getScaledHeight() / 2.0f + 77.0f);
                        } else if (i == 2) {
                            rwidth = (int)((float)sr.getScaledHeight() / 2.0f + 62.0f);
                        } else if (i == 3) {
                            rwidth = (int)((float)sr.getScaledHeight() / 2.0f + 46.0f);
                        } else if (i == 4) {
                            rwidth = (int)((float)sr.getScaledHeight() / 2.0f + 32.0f);
                        }
                        GlStateManager.pushMatrix();
                        this.mc.getRenderItem().renderItemIntoGUI(ia, (float)sr.getScaledWidth() / 2.0f + 82.0f, rwidth + 14);
                        this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, ia, (float)sr.getScaledWidth() / 2.0f + 82.0f, rwidth + 14);
                        GlStateManager.popMatrix();
                        RenderHelper.disableStandardItemLighting();
                        NBTTagList enchants = ia.getEnchantmentTagList();
                        if (enchants == null) continue;
                        for (int index = 0; index < enchants.tagCount(); ++index) {
                            short id = enchants.getCompoundTagAt(index).getShort("id");
                            short level = enchants.getCompoundTagAt(index).getShort("lvl");
                            Enchantment enc = Enchantment.getEnchantmentById(id);
                            if (enc != Enchantment.protection) continue;
                            GL11.glPushMatrix();
                            GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
                            this.mc.fontRendererObj.drawStringWithShadow("protection" + this.getAmplifierNumerals(level).toLowerCase(), ((float)sr.getScaledWidth() / 2.0f + 78.0f - (float)this.mc.fontRendererObj.getStringWidth("protection" + this.getAmplifierNumerals(level).toLowerCase()) / 2.0f) * 2.0f, (float)(rwidth + 20) * 2.0f, -8070169);
                            GL11.glPopMatrix();
                            GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
                        }
                    }
                    this.drawEntityOnScreen(sr.getScaledWidth() / 2 - 82, sr.getScaledHeight() / 2 + 102, 27, 310.0f, 0.0f, target);
                    break;
                }
                case ASTOLFO: {
                    if (KillAura.target == null) break;
                    EntityLivingBase targetEntity = KillAura.target;
                    String targetName = (Moon.INSTANCE.getModuleManager().getModule("StreamerMode").isEnabled() && StreamerMode.hideNames.getValue() != false ? "\u00a7k" : "") + targetEntity.getName();
                    if (!(targetEntity instanceof EntityOtherPlayerMP)) break;
                    EntityOtherPlayerMP target = (EntityOtherPlayerMP)targetEntity;
                    float health = target.getHealth();
                    if (!Float.isNaN(target.getHealth())) {
                        if (this.animationUtil.getPosX() == 0.0) {
                            this.animationUtil.setPosX(194.0f / target.getMaxHealth() / 2.0f);
                        }
                        RenderUtil.drawBorderedRect((float)sr.getScaledWidth() / 2.0f - 70.0f, (float)sr.getScaledHeight() / 2.0f + 45.0f, 140.0, 49.0, 0.0, new Color(11, 11, 11, 220).getRGB(), new Color(11, 11, 11, 159).getRGB());
                    } else {
                        RenderUtil.drawBorderedRect((float)sr.getScaledWidth() / 2.0f - 70.0f, (float)sr.getScaledHeight() / 2.0f + 45.0f, 140.0, 49.0, 0.0, new Color(11, 11, 11, 220).getRGB(), new Color(11, 11, 11, 159).getRGB());
                    }
                    if (!Float.isNaN(target.getHealth())) {
                        double inc = 111.0f / target.getMaxHealth();
                        double end = inc * (double)Math.min(health, target.getMaxHealth());
                        this.animationUtil.linear(Math.max(end, 0.5), 0.0, 300.0f / (float)Minecraft.getDebugFPS());
                        RenderUtil.drawRect(sr.getScaledWidth() / 2 - 44, (float)sr.getScaledHeight() / 2.0f + 85.5f, 111.0, 6.0, new Color(HUD.getColorHUD()).darker().darker().darker().getRGB());
                        RenderUtil.drawRect(sr.getScaledWidth() / 2 - 44, (float)sr.getScaledHeight() / 2.0f + 85.5f, this.animationUtil.getPosX() - 5.0, 6.0, HUD.getColorHUD());
                        RenderUtil.drawRect(Math.max((double)(sr.getScaledWidth() / 2 - 44) + this.animationUtil.getPosX() - 5.0, (double)((float)sr.getScaledWidth() / 2.0f - 54.0f)), (float)sr.getScaledHeight() / 2.0f + 85.5f, 5.0, 6.0, new Color(HUD.getColorHUD()).darker().getRGB());
                        GL11.glPushMatrix();
                        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
                        this.mc.fontRendererObj.drawStringWithShadow(MathUtils.round(target.getHealth() / 2.0f, 1) + " \u2764", ((float)sr.getScaledWidth() / 2.0f - 44.0f) / 2.0f, ((float)(sr.getScaledHeight() / 2) + 63.5f) / 2.0f, HUD.getColorHUD());
                        GL11.glPopMatrix();
                    } else {
                        RenderUtil.drawRect(sr.getScaledWidth() / 2 - 44, (float)sr.getScaledHeight() / 2.0f + 85.5f, 111.0, 6.0, new Color(HUD.getColorHUD()).darker().darker().darker().getRGB());
                        GL11.glPushMatrix();
                        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
                        this.mc.fontRendererObj.drawStringWithShadow("NaN \u2764", ((float)sr.getScaledWidth() / 2.0f - 44.0f) / 2.0f, ((float)(sr.getScaledHeight() / 2) + 63.5f) / 2.0f, HUD.getColorHUD());
                        GL11.glPopMatrix();
                    }
                    this.mc.fontRendererObj.drawStringWithShadow(targetName, (float)sr.getScaledWidth() / 2.0f - 42.0f, sr.getScaledHeight() / 2 + 50, -1);
                    boolean rwidth = false;
                    this.drawEntityOnScreen(sr.getScaledWidth() / 2 - 58, sr.getScaledHeight() / 2 + 88, 20, 310.0f, 0.0f, target);
                    break;
                }
                case NOVOLINEO: {
                    EntityLivingBase targetEntity = KillAura.target;
                    if (!(targetEntity instanceof EntityOtherPlayerMP)) break;
                    EntityOtherPlayerMP target = (EntityOtherPlayerMP)targetEntity;
                    float addition = 0.0f;
                    for (int i = 0; i < 5; ++i) {
                        ItemStack ia = target.getEquipmentInSlot(4 - i);
                        if (ia == null) continue;
                        addition += 15.0f;
                    }
                    float baseWidth = Math.max(50.0f + addition, (float)(35 + this.mc.fontRendererObj.getStringWidth(target.getName()) + 5));
                    float baseX = (float)sr.getScaledWidth() / 2.0f - baseWidth / 2.0f;
                    float health = target.getHealth();
                    RenderUtil.drawRect(baseX, (double)((float)sr.getScaledHeight() / 2.0f) + 42.5, baseWidth, 43.0, 0x67000000);
                    this.drawEntityOnScreen((int)(baseX + 12.0f), sr.getScaledHeight() / 2 + 78, 16, target.rotationYaw, -target.rotationPitch, target);
                    this.mc.fontRendererObj.drawStringWithShadow(target.getName(), baseX + 25.0f, (float)sr.getScaledHeight() / 2.0f + 48.0f, -1);
                    if (!Float.isNaN(target.getHealth())) {
                        double end = baseWidth / target.getMaxHealth() * Math.min(health, target.getMaxHealth());
                        this.animationUtil.linear(Math.max(end, 0.5), 0.0, 150.0f / (float)Minecraft.getDebugFPS());
                        RenderUtil.drawRect(baseX, (float)sr.getScaledHeight() / 2.0f + 83.5f, this.animationUtil.getPosX(), 2.0, this.getHealthColor(target));
                    }
                    float rwidth = baseX + 3.0f;
                    for (int i = 0; i < 5; ++i) {
                        ItemStack ia = target.getEquipmentInSlot(4 - i);
                        if (ia == null) continue;
                        GlStateManager.pushMatrix();
                        RenderHelper.enableStandardItemLighting();
                        this.mc.getRenderItem().renderItemIntoGUI(ia, rwidth += 19.0f, sr.getScaledHeight() / 2 + 61);
                        RenderHelper.disableStandardItemLighting();
                        GlStateManager.popMatrix();
                    }
                    break;
                }
                case NOVOLINE: {
                    if (KillAura.target == null) break;
                    EntityLivingBase targetEntity = KillAura.target;
                    if (targetEntity instanceof EntityOtherPlayerMP) {
                        String targetName = (Moon.INSTANCE.getModuleManager().getModule("StreamerMode").isEnabled() && StreamerMode.hideNames.getValue() != false ? "\u00a7k" : "") + targetEntity.getName();
                        EntityOtherPlayerMP target = (EntityOtherPlayerMP)targetEntity;
                        float width = 130.0f;
                        float x = (float)(sr.getScaledWidth() / 2) - width / 2.0f;
                        float y = sr.getScaledHeight() / 2 + 75;
                        float height = 40.0f;
                        NetworkPlayerInfo networkPlayerInfo = this.mc.getNetHandler().getPlayerInfo(target.getUniqueID());
                        try {
                            int ping = networkPlayerInfo.getResponseTime();
                        }
                        catch (NullPointerException ex) {
                            boolean ping = true;
                        }
                        RenderUtil.drawBorderedRect(x, y, width, height, 1.0, new Color(45, 45, 45).getRGB(), new Color(66, 66, 66).getRGB());
                        RenderUtil.drawRect(x + 1.0f, y + 1.0f, 32.0, height - 2.0f, new Color(58, 58, 62).getRGB());
                        this.drawEntityOnScreen((int)(x + 16.0f), (int)(y + 35.0f), 15, target.rotationYaw, -targetEntity.rotationPitch, target);
                        this.mc.fontRendererObj.drawString(targetName, x + 37.0f, y + 5.0f, -1);
                        this.mc.fontRendererObj.drawString(MathUtils.roundDouble(target.getHealth(), 1) + " \u00a7c\u2764", x + 37.0f, y + 28.5f, -1);
                        RenderUtil.drawBorderedRect(x + 37.0f, y + 15.0f, width - 42.0f, 10.0, 1.0, new Color(50, 50, 50).getRGB(), new Color(56, 56, 56).getRGB());
                        if (Float.isNaN(target.getHealth())) break;
                        float healthWidth = Math.min(target.getHealth() / target.getMaxHealth(), 1.0f);
                        float rectWidth = (width - 44.0f) * healthWidth;
                        float health = Math.max(0.0f, Math.min(target.getHealth(), target.getMaxHealth()) / target.getMaxHealth());
                        this.animationUtil.interpolate(rectWidth, 0.0, 25.0f / (float)Minecraft.getDebugFPS());
                        this.animationUtil2.interpolate(rectWidth, 0.0, 6.0f / (float)Minecraft.getDebugFPS());
                        RenderUtil.drawRect(x + 38.0f, y + 16.0f, this.animationUtil2.getPosX(), 8.0, new Color(Color.HSBtoRGB(health / 3.0f, 1.0f, 1.0f)).darker().getRGB() | 0xFF000000);
                        RenderUtil.drawRect(x + 38.0f, y + 16.0f, this.animationUtil.getPosX(), 8.0, Color.HSBtoRGB(health / 3.0f, 1.0f, 1.0f) | 0xFF000000);
                        break;
                    }
                    this.animationUtil.setPosX(0.0);
                    break;
                }
                case REMIX: {
                    EntityLivingBase targetEntity;
                    if (KillAura.target == null || !((targetEntity = KillAura.target) instanceof EntityOtherPlayerMP)) break;
                    EntityOtherPlayerMP target = (EntityOtherPlayerMP)targetEntity;
                    float health = target.getHealth();
                    String targetName = (Moon.INSTANCE.getModuleManager().getModule("StreamerMode").isEnabled() && StreamerMode.hideNames.getValue() != false ? "\u00a7k" : "") + targetEntity.getName();
                    RenderUtil.drawBorderedRect((float)sr.getScaledWidth() / 2.0f - 70.0f, (float)sr.getScaledHeight() / 2.0f + 45.0f, 140.0, 49.0, 2.0, -15592942, -14935012);
                    if (!Float.isNaN(target.getHealth())) {
                        double inc = 130.0f / target.getMaxHealth();
                        double end = inc * (double)Math.min(health, target.getMaxHealth());
                        RenderUtil.drawBorderedRect(sr.getScaledWidth() / 2 - 65, (float)sr.getScaledHeight() / 2.0f + 85.0f, 130.0, 5.0, 0.0, new Color(this.getHealthColor(target)).darker().darker().getRGB(), -9892341);
                        RenderUtil.drawRect(sr.getScaledWidth() / 2 - 65, (float)sr.getScaledHeight() / 2.0f + 85.0f, end, 5.0, this.getHealthColor(target));
                        if (end <= 129.0) {
                            RenderUtil.drawRect((double)(sr.getScaledWidth() / 2 - 65) + end, (float)sr.getScaledHeight() / 2.0f + 85.0f, 1.0, 5.0, new Color(this.getHealthColor(target)).darker().getRGB());
                        }
                    } else {
                        RenderUtil.drawBorderedRect(sr.getScaledWidth() / 2 - 65, (float)sr.getScaledHeight() / 2.0f + 85.0f, 130.0, 5.0, 0.5, new Color(this.getHealthColor(target)).darker().darker().getRGB(), -9892341);
                    }
                    this.mc.fontRendererObj.drawStringWithShadow(targetName, (float)sr.getScaledWidth() / 2.0f - 30.0f, sr.getScaledHeight() / 2 + 50, -1);
                    EntityOtherPlayerMP abstractClientPlayer = target;
                    NetworkPlayerInfo networkPlayerInfo = abstractClientPlayer.getPlayerInfo();
                    GL11.glPushMatrix();
                    GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
                    this.mc.fontRendererObj.drawStringWithShadow(networkPlayerInfo.getResponseTime() + "ms", ((float)sr.getScaledWidth() / 2.0f + 65.0f - (float)this.mc.fontRendererObj.getStringWidth(networkPlayerInfo.getResponseTime() + "ms") / 2.0f) * 2.0f, (sr.getScaledHeight() / 2 + 73) * 2, -1);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    this.mc.getTextureManager().bindTexture(Gui.icons);
                    int i = 0;
                    int j = 0;
                    j = networkPlayerInfo.getResponseTime() < 0 ? 5 : (networkPlayerInfo.getResponseTime() < 150 ? 0 : (networkPlayerInfo.getResponseTime() < 300 ? 1 : (networkPlayerInfo.getResponseTime() < 600 ? 2 : (networkPlayerInfo.getResponseTime() < 1000 ? 3 : 4))));
                    RenderUtil.drawTexturedModalRect(sr.getScaledWidth() / 2 + 55, sr.getScaledHeight() / 2 + 62, 0 + i * 10, 176 + j * 8, 10, 8, 1.0f);
                    GL11.glPopMatrix();
                    RenderUtil.drawRect((float)sr.getScaledWidth() / 2.0f - 65.0f, (float)sr.getScaledHeight() / 2.0f + 49.5f, 32.0, 32.0, -7500403);
                    this.drawFace((float)sr.getScaledWidth() / 2.0f - 64.0f, (float)sr.getScaledHeight() / 2.0f + 51.0f, 8.0f, 8.0f, 8, 8, 30, 30, 64.0f, 64.0f, target);
                    float xPos = (float)sr.getScaledWidth() / 2.0f - 30.0f;
                    for (int i2 = 0; i2 < 4; ++i2) {
                        RenderUtil.drawBorderedRect(xPos, sr.getScaledHeight() / 2 + 60, 18.0, 18.0, 1.0, -15856114, -11776948);
                        xPos += 19.0f;
                    }
                    RenderUtil.drawRect(sr.getScaledWidth() / 2 - 29, sr.getScaledHeight() / 2 + 80, 94.0, 2.0, -16777092);
                    RenderUtil.drawRect(sr.getScaledWidth() / 2 - 29, sr.getScaledHeight() / 2 + 80, 94.0f * ((float)target.getTotalArmorValue() / 20.0f), 2.0, -16776961);
                    float rwidth = (float)sr.getScaledWidth() / 2.0f - 29.0f;
                    for (i = 1; i < 5; ++i) {
                        boolean height;
                        ItemStack ia = target.getEquipmentInSlot(5 - i);
                        if (ia == null) continue;
                        GlStateManager.pushMatrix();
                        RenderHelper.enableStandardItemLighting();
                        boolean width = height = false;
                        rwidth = (float)sr.getScaledWidth() / 2.0f - 29.0f + (float)(19 * (i - 1));
                        this.mc.getRenderItem().renderItemIntoGUI(ia, rwidth, sr.getScaledHeight() / 2 + 61);
                        this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, ia, rwidth, sr.getScaledHeight() / 2 + 61);
                        RenderHelper.disableStandardItemLighting();
                        GlStateManager.popMatrix();
                    }
                    break;
                }
            }
            GlStateManager.disableDepth();
        }
    }

    public HUD.TargetHUDMode getTargetHUDMode() {
        return (HUD.TargetHUDMode)((Object)HUD.targetHUDMode.getValue());
    }

    private int getHealthColor(EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0f, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0f, 1.0f, 1.0f) | 0xFF000000;
    }

    private void drawFace(double x, double y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight, AbstractClientPlayer target) {
        try {
            ResourceLocation skin = target.getLocationSkin();
            this.mc.getTextureManager().bindTexture(skin);
            GL11.glEnable((int)3042);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            Gui.drawScaledCustomSizeModalRect((int)x, (int)y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
            GL11.glDisable((int)3042);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent) {
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
        rendermanager.setPlayerViewY(180.0f);
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
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    private String getAmplifierNumerals(int amplifier) {
        switch (amplifier) {
            case 0: {
                return " I";
            }
            case 1: {
                return " II";
            }
            case 2: {
                return " III";
            }
            case 3: {
                return " IV";
            }
            case 4: {
                return " V";
            }
            case 5: {
                return " VI";
            }
            case 6: {
                return " VII";
            }
            case 7: {
                return " VIII";
            }
            case 8: {
                return " IX";
            }
            case 9: {
                return " X";
            }
        }
        if (amplifier < 1) {
            return "";
        }
        return " " + amplifier;
    }

    @Override
    public void bloom() {
        if (this.getTargetHUDMode() == HUD.TargetHUDMode.MOON && Moon.INSTANCE.getModuleManager().getModule("Killaura").isEnabled() && KillAura.target != null) {
            EntityLivingBase targetEntity;
            ScaledResolution sr = new ScaledResolution(this.mc);
            if (KillAura.target != null && (targetEntity = KillAura.target) instanceof EntityOtherPlayerMP) {
                EntityOtherPlayerMP target = (EntityOtherPlayerMP)targetEntity;
                float health = target.getHealth();
                if (!Float.isNaN(target.getHealth())) {
                    RenderUtil.drawRect((float)sr.getScaledWidth() / 2.0f + 32.0f, (float)sr.getScaledHeight() / 2.0f + 42.0f, 35 + Math.max(Fonts.moonMiddle.getStringWidth(target.getName()), Fonts.moon.getStringWidth("Health: " + MathUtils.roundDouble(health, 1))) + 10, 36.5, -1);
                } else {
                    RenderUtil.drawRect((float)sr.getScaledWidth() / 2.0f + 32.0f, (float)sr.getScaledHeight() / 2.0f + 42.0f, 35 + Math.max(Fonts.moonMiddle.getStringWidth(target.getName()), Fonts.moon.getStringWidth("Health: " + MathUtils.roundDouble(0.0, 1))) + 10, 36.5, -1);
                }
            }
        }
        super.bloom();
    }

    @Override
    public void blur() {
        if (this.getTargetHUDMode() == HUD.TargetHUDMode.MOON && Moon.INSTANCE.getModuleManager().getModule("Killaura").isEnabled() && KillAura.target != null) {
            EntityLivingBase targetEntity;
            ScaledResolution sr = new ScaledResolution(this.mc);
            if (KillAura.target != null && (targetEntity = KillAura.target) instanceof EntityOtherPlayerMP) {
                EntityOtherPlayerMP target = (EntityOtherPlayerMP)targetEntity;
                float health = target.getHealth();
                if (!Float.isNaN(target.getHealth())) {
                    RenderUtil.drawRect((float)sr.getScaledWidth() / 2.0f + 32.0f, (float)sr.getScaledHeight() / 2.0f + 42.0f, 35 + Math.max(Fonts.moonMiddle.getStringWidth(target.getName()), Fonts.moon.getStringWidth("Health: " + MathUtils.roundDouble(health, 1))) + 10, 36.5, -1);
                } else {
                    RenderUtil.drawRect((float)sr.getScaledWidth() / 2.0f + 32.0f, (float)sr.getScaledHeight() / 2.0f + 42.0f, 35 + Math.max(Fonts.moonMiddle.getStringWidth(target.getName()), Fonts.moon.getStringWidth("Health: " + MathUtils.roundDouble(0.0, 1))) + 10, 36.5, -1);
                }
            }
        }
        super.blur();
    }
}

