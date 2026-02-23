/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals.components.nostalgia;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.impl.visuals.components.Component;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class NPotionComponent
extends Component {
    private final ResourceLocation INVENTORY_RESOURCE = new ResourceLocation("textures/gui/container/inventory.png");

    @Override
    public void render(Render2DEvent event) {
        ScaledResolution sr = event.getScaledResolution();
        float height = 0.0f;
        height = -this.mc.fontRendererObj.FONT_HEIGHT - 2 - 30;
        ArrayList<Potion> sorted = new ArrayList<Potion>();
        for (Potion potion : Potion.potionTypes) {
            if (potion == null || !Minecraft.getMinecraft().thePlayer.isPotionActive(potion) || !potion.hasStatusIcon()) continue;
            sorted.add(potion);
        }
        for (Potion potion : sorted) {
            PotionEffect effect = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(potion);
            String label = StatCollector.translateToLocal(effect.getEffectName()) + ChatFormatting.DARK_AQUA + this.getAmplifierNumerals(effect.getAmplifier());
            String potionDuration = Potion.getDurationString(effect);
            float maxBlocks = 600.0f;
            float hue = Math.max(0.0f, Math.min((float)effect.getDuration(), maxBlocks) / maxBlocks);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.INVENTORY_RESOURCE);
            int index = potion.getStatusIconIndex();
            double x = sr.getScaledWidth() - 20;
            RenderUtil.drawTexturedModalRect((int)x, (int)((float)sr.getScaledHeight() + height), index % 8 * 18, 198 + index / 8 * 18, 18, 18, -150.0f);
            this.mc.fontRendererObj.drawStringWithShadow(label, (float)(x - (double)this.mc.fontRendererObj.getStringWidth(label)), (float)sr.getScaledHeight() + height, -1);
            this.mc.fontRendererObj.drawStringWithShadow(potionDuration, (float)(x - (double)this.mc.fontRendererObj.getStringWidth(potionDuration)), (float)sr.getScaledHeight() + height + 9.0f, (float)effect.getDuration() > maxBlocks ? Color.WHITE.getRGB() : Color.HSBtoRGB(hue / 10.0f, 1.0f, 1.0f) | 0xFF000000);
            height -= 21.0f;
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

    @Override
    public void init() {
        super.init();
    }
}

