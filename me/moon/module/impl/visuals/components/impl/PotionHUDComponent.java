/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals.components.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.impl.visuals.HUD;
import me.moon.module.impl.visuals.components.Component;
import me.moon.utils.font.Fonts;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class PotionHUDComponent
extends Component {
    private final ResourceLocation INVENTORY_RESOURCE = new ResourceLocation("textures/gui/container/inventory.png");

    @Override
    public void render(Render2DEvent event) {
        ScaledResolution sr = event.getScaledResolution();
        int height = 0;
        switch ((HUD.ReleaseBuildMode)((Object)HUD.releaseBuildModes.getValue())) {
            case MOON: {
                height -= HUD.mcFont.getValue() != false ? 21 : 18;
                break;
            }
            case ASTOLFO: {
                height = -18;
                break;
            }
            case REMIX: {
                height = -21;
                break;
            }
            case NOVOLINE: {
                height = -20;
                break;
            }
            case NONE: {
                height = -12;
            }
        }
        switch ((HUD.InformationMode)((Object)HUD.informationMode.getValue())) {
            case XAVE: {
                height = this.mc.ingameGUI.getChatGUI().getChatOpen() ? -47 : -33;
                if (HUD.releaseBuildModes.getValue() == HUD.ReleaseBuildMode.NONE) break;
                height -= 9;
            }
        }
        switch (this.getPotionHUDModes()) {
            case FLUX: {
                if (this.mc.thePlayer == null) break;
                height = -this.mc.fontRendererObj.FONT_HEIGHT - 2 - (HUD.informationMode.getValue() == HUD.InformationMode.XAVE ? 19 : 0) - (this.mc.ingameGUI.getChatGUI().getChatOpen() ? 14 : 0);
                ArrayList<Potion> sorted = new ArrayList<Potion>();
                for (Potion potion2 : Potion.potionTypes) {
                    if (potion2 == null || !Minecraft.getMinecraft().thePlayer.isPotionActive(potion2) || !potion2.hasStatusIcon()) continue;
                    sorted.add(potion2);
                }
                sorted.sort(Comparator.comparingDouble(potion -> this.mc.fontRendererObj.getStringWidth(StatCollector.translateToLocal(potion.getName()) + (Minecraft.getMinecraft().thePlayer.getActivePotionEffect((Potion)potion).getAmplifier() > 0 ? this.getAmplifierNumerals(Minecraft.getMinecraft().thePlayer.getActivePotionEffect((Potion)potion).getAmplifier()) : "") + " : " + Potion.getDurationString(Minecraft.getMinecraft().thePlayer.getActivePotionEffect((Potion)potion)))));
                for (Potion potion3 : sorted) {
                    PotionEffect effect = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(potion3);
                    String label = StatCollector.translateToLocal(effect.getEffectName()) + ChatFormatting.WHITE + (effect.getAmplifier() > 0 ? this.getAmplifierNumerals(effect.getAmplifier()) : "") + " : ";
                    GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                    int index = potion3.getStatusIconIndex();
                    double x = (float)sr.getScaledWidth() / 2.0f + 96.0f;
                    this.mc.fontRendererObj.drawStringWithShadow(label, (float)(x + 19.0) - (float)this.mc.fontRendererObj.getStringWidth(Potion.getDurationString(effect)), sr.getScaledHeight() + height, potion3.getLiquidColor());
                    this.mc.fontRendererObj.drawStringWithShadow(Potion.getDurationString(effect), (float)(x + 20.0 + (double)this.mc.fontRendererObj.getStringWidth(label) - (double)this.mc.fontRendererObj.getStringWidth(Potion.getDurationString(effect))), sr.getScaledHeight() + height, -1);
                    height -= this.mc.fontRendererObj.FONT_HEIGHT;
                }
                break;
            }
            case NOVOLINE: {
                if (this.mc.thePlayer == null) break;
                ArrayList<Potion> sorted = new ArrayList<Potion>();
                for (Potion potion4 : Potion.potionTypes) {
                    if (potion4 == null || !Minecraft.getMinecraft().thePlayer.isPotionActive(potion4) || !potion4.hasStatusIcon()) continue;
                    sorted.add(potion4);
                }
                sorted.sort(Comparator.comparingDouble(potion -> -Fonts.novolineFont.getStringWidth(StatCollector.translateToLocal(potion.getName()) + this.getAmplifierNumerals(Minecraft.getMinecraft().thePlayer.getActivePotionEffect((Potion)potion).getAmplifier()) + " : " + Potion.getDurationString(Minecraft.getMinecraft().thePlayer.getActivePotionEffect((Potion)potion)))));
                for (Potion potion5 : sorted) {
                    PotionEffect effect = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(potion5);
                    String label = StatCollector.translateToFallback(effect.getEffectName()) + (effect.getAmplifier() > 0 ? this.getAmplifierNumerals(effect.getAmplifier()) : "") + ChatFormatting.GRAY + " : ";
                    double x = sr.getScaledWidth() - 22 - Fonts.novolineFont.getStringWidth(label);
                    Fonts.novolineFont.drawStringWithShadow(label, (float)(x + 20.0) - (float)Fonts.novolineFont.getStringWidth(Potion.getDurationString(effect)), sr.getScaledHeight() + height, this.getColor());
                    Fonts.novolineFont.drawStringWithShadow(Potion.getDurationString(effect), x + 20.0 + (double)Fonts.novolineFont.getStringWidth(label) - (double)Fonts.novolineFont.getStringWidth(Potion.getDurationString(effect)), sr.getScaledHeight() + height, this.getPotionColor(effect.getDuration()));
                    height -= Fonts.novolineFont.getHeight() + 2;
                }
                break;
            }
            case XAVE: {
                if (this.mc.thePlayer == null) break;
                ArrayList<Potion> sorted = new ArrayList<Potion>();
                for (Potion potion6 : Potion.potionTypes) {
                    if (potion6 == null || !Minecraft.getMinecraft().thePlayer.isPotionActive(potion6) || !potion6.hasStatusIcon()) continue;
                    sorted.add(potion6);
                }
                sorted.sort(Comparator.comparingDouble(potion -> -Fonts.hudNormal.getStringWidth(StatCollector.translateToLocal(potion.getName()) + this.getAmplifierNumerals(Minecraft.getMinecraft().thePlayer.getActivePotionEffect((Potion)potion).getAmplifier()) + " : " + Potion.getDurationString(Minecraft.getMinecraft().thePlayer.getActivePotionEffect((Potion)potion)))));
                for (Potion potion7 : sorted) {
                    PotionEffect effect = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(potion7);
                    String label = StatCollector.translateToLocal(effect.getEffectName()) + this.getAmplifierNumerals(effect.getAmplifier()) + ChatFormatting.GRAY + ": ";
                    GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                    int index = potion7.getStatusIconIndex();
                    double x = sr.getScaledWidth() - 22 - Fonts.hudNormal.getStringWidth(label);
                    Fonts.hudNormal.drawStringWithShadow(label, (float)(x + 25.0) - (float)Fonts.hudNormal.getStringWidth(Potion.getDurationString(effect) + ": "), sr.getScaledHeight() + height, potion7.getLiquidColor());
                    Fonts.hudNormal.drawStringWithShadow("\u00a77" + Potion.getDurationString(effect), (float)(x + 25.0 + (double)Fonts.hudNormal.getStringWidth(label) - (double)Fonts.hudNormal.getStringWidth(Potion.getDurationString(effect) + ": ")), sr.getScaledHeight() + height, this.getPotionColor(effect.getDuration()));
                    height -= Fonts.hudNormal.getStringHeight("a") + 2;
                }
                break;
            }
            case ASTOLFO: {
                if (this.mc.thePlayer == null) break;
                ArrayList<Potion> sorted = new ArrayList<Potion>();
                for (Potion potion8 : Potion.potionTypes) {
                    if (potion8 == null || !Minecraft.getMinecraft().thePlayer.isPotionActive(potion8) || !potion8.hasStatusIcon()) continue;
                    sorted.add(potion8);
                }
                sorted.sort(Comparator.comparingDouble(potion -> -Fonts.astolfoFont.getStringWidth(StatCollector.translateToLocal(potion.getName()) + " " + (Minecraft.getMinecraft().thePlayer.getActivePotionEffect((Potion)potion).getAmplifier() + 1) + " - " + Potion.getDurationString(Minecraft.getMinecraft().thePlayer.getActivePotionEffect((Potion)potion)))));
                for (Potion potion9 : sorted) {
                    PotionEffect effect = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(potion9);
                    String label = StatCollector.translateToLocal(effect.getEffectName()) + ChatFormatting.GRAY + " " + (effect.getAmplifier() + 1) + ChatFormatting.GRAY + " - " + Potion.getDurationString(effect);
                    GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(this.INVENTORY_RESOURCE);
                    int index = potion9.getStatusIconIndex();
                    double x = sr.getScaledWidth() - 22 - Fonts.astolfoFont.getStringWidth(label);
                    GL11.glPushMatrix();
                    GlStateManager.scale(0.5f, 0.5f, 0.5f);
                    RenderUtil.drawTexturedModalRect((int)(x * 2.0) + 18, (int)((double)(sr.getScaledHeight() - 12 + height) * 2.0) + 19, index % 8 * 18, 198 + index / 8 * 18, 18, 18, -99.0f);
                    GL11.glPopMatrix();
                    Fonts.astolfoFont.drawStringWithShadow(label, (float)(x + 20.0), sr.getScaledHeight() + height, potion9.getLiquidColor());
                    height -= Fonts.astolfoFont.getHeight() + 2;
                }
                break;
            }
            case MOON: {
                if (this.mc.thePlayer == null) break;
                ArrayList<Potion> sorted = new ArrayList<Potion>();
                for (Potion potion10 : Potion.potionTypes) {
                    if (potion10 == null || !Minecraft.getMinecraft().thePlayer.isPotionActive(potion10) || !potion10.hasStatusIcon()) continue;
                    sorted.add(potion10);
                }
                if (HUD.mcFont.getValue().booleanValue()) {
                    sorted.sort(Comparator.comparingDouble(potion -> -this.mc.fontRendererObj.getStringWidth(StatCollector.translateToLocal(potion.getName()) + " " + (Minecraft.getMinecraft().thePlayer.getActivePotionEffect((Potion)potion).getAmplifier() + 1) + " - " + Potion.getDurationString(Minecraft.getMinecraft().thePlayer.getActivePotionEffect((Potion)potion)))));
                } else {
                    sorted.sort(Comparator.comparingDouble(potion -> -Fonts.moonThemeSF.getStringWidth(StatCollector.translateToLocal(potion.getName()) + " " + (Minecraft.getMinecraft().thePlayer.getActivePotionEffect((Potion)potion).getAmplifier() + 1) + " - " + Potion.getDurationString(Minecraft.getMinecraft().thePlayer.getActivePotionEffect((Potion)potion)))));
                }
                for (Potion potion11 : sorted) {
                    PotionEffect effect = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(potion11);
                    String label = StatCollector.translateToLocal(effect.getEffectName()) + ChatFormatting.GRAY + " " + (effect.getAmplifier() + 1) + ChatFormatting.GRAY + ": " + Potion.getDurationString(effect);
                    GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(this.INVENTORY_RESOURCE);
                    int index = potion11.getStatusIconIndex();
                    double x = sr.getScaledWidth() - 22 - (HUD.mcFont.getValue() != false ? this.mc.fontRendererObj.getStringWidth(label) : Fonts.moonThemeSF.getStringWidth(label));
                    GL11.glPushMatrix();
                    GlStateManager.scale(0.5f, 0.5f, 0.5f);
                    RenderUtil.drawTexturedModalRect((int)(x * 2.0) + 18, (int)((double)(sr.getScaledHeight() - 12 + height) * 2.0) + 19, index % 8 * 18, 198 + index / 8 * 18, 18, 18, -99.0f);
                    GL11.glPopMatrix();
                    if (HUD.mcFont.getValue().booleanValue()) {
                        this.mc.fontRendererObj.drawStringWithShadow(label, (float)(x + 20.0), sr.getScaledHeight() + height, potion11.getLiquidColor());
                    } else {
                        Fonts.moonThemeSF.drawStringWithShadow(label, (float)(x + 20.0), sr.getScaledHeight() + height, potion11.getLiquidColor());
                    }
                    height -= Fonts.moonThemeSF.getHeight() + 2;
                }
                break;
            }
        }
        super.render(event);
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

    private int getPotionColor(int count) {
        float maxBlocks = 600.0f;
        if ((float)count > maxBlocks) {
            return Color.gray.getRGB();
        }
        float hue = Math.max(0.0f, Math.min((float)count, maxBlocks) / maxBlocks);
        return Color.HSBtoRGB(hue / 10.0f, 1.0f, 1.0f) | 0xFF000000;
    }

    public HUD.PotionHUDMode getPotionHUDModes() {
        return (HUD.PotionHUDMode)((Object)HUD.potionHUDMode.getValue());
    }
}

