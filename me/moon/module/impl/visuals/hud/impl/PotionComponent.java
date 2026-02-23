/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals.hud.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Comparator;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.visuals.hud.Component;
import me.moon.utils.font.MCFontRenderer;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.FontValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class PotionComponent
extends Component {
    public final BooleanValue useFont = new BooleanValue("Use font", true);
    public final FontValue fontValue = new FontValue("Font", new MCFontRenderer(new Font("Verdana", 0, 18), true, false));

    public PotionComponent(String name) {
        super(name, 2.0, 2.0);
        this.initValues(this.useFont, this.fontValue);
    }

    @Override
    public void onCompRender(ScaledResolution sr) {
        if (this.mc.thePlayer == null) {
            return;
        }
        ArrayList<Potion> sorted = new ArrayList<Potion>();
        int height = (int)this.getY();
        for (Potion potion2 : Potion.potionTypes) {
            if (potion2 == null || !Minecraft.getMinecraft().thePlayer.isPotionActive(potion2) || !potion2.hasStatusIcon()) continue;
            sorted.add(potion2);
        }
        sorted.sort(Comparator.comparingDouble(potion -> -((MCFontRenderer)this.fontValue.getValue()).getStringWidth(StatCollector.translateToLocal(potion.getName()) + this.getAmplifierNumerals(Minecraft.getMinecraft().thePlayer.getActivePotionEffect((Potion)potion).getAmplifier()) + " : " + Potion.getDurationString(Minecraft.getMinecraft().thePlayer.getActivePotionEffect((Potion)potion)))));
        for (Potion potion3 : sorted) {
            double x;
            PotionEffect effect = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(potion3);
            String label = StatCollector.translateToLocal(effect.getEffectName()) + this.getAmplifierNumerals(effect.getAmplifier()) + ChatFormatting.GRAY + " : " + Potion.getDurationString(effect);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            if (this.useFont.getValue().booleanValue()) {
                x = this.getX() - (double)(this.getX() > (double)((float)sr.getScaledWidth() / 2.0f) ? ((MCFontRenderer)this.fontValue.getValue()).getStringWidth(label) : 0);
                ((MCFontRenderer)this.fontValue.getValue()).drawStringWithShadow(label, (float)(x + 20.0), height, potion3.getLiquidColor());
                if (this.getY() > (double)((float)sr.getScaledHeight() / 2.0f)) {
                    height -= ((MCFontRenderer)this.fontValue.getValue()).getHeight() + 2;
                    continue;
                }
                height += ((MCFontRenderer)this.fontValue.getValue()).getHeight() + 2;
                continue;
            }
            x = this.getX() - (double)(this.getX() > (double)((float)sr.getScaledWidth() / 2.0f) ? this.mc.fontRendererObj.getStringWidth(label) : 0);
            this.mc.fontRendererObj.drawStringWithShadow(label, (float)(x + 20.0), height, potion3.getLiquidColor());
            if (this.getY() > (double)((float)sr.getScaledHeight() / 2.0f)) {
                height -= this.mc.fontRendererObj.FONT_HEIGHT + 2;
                continue;
            }
            height += this.mc.fontRendererObj.FONT_HEIGHT + 2;
        }
    }

    @Override
    public void onCompUpdate(UpdateEvent event) {
    }

    @Override
    public void onInit() {
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
        return " " + amplifier + 1;
    }
}

