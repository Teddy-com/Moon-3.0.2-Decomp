/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals.hud.impl;

import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.visuals.hud.Component;
import me.moon.utils.value.impl.BooleanValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class ArmorComponent
extends Component {
    public final BooleanValue sideways = new BooleanValue("Sideways", true);

    public ArmorComponent(String name) {
        super(name, 2.0, 2.0);
        this.initValues(this.sideways);
    }

    @Override
    public void onCompRender(ScaledResolution sr) {
        for (int i = 0; i < Minecraft.getMinecraft().thePlayer.inventory.armorInventory.length; ++i) {
            if (Minecraft.getMinecraft().thePlayer.inventory.armorInventory[i] == null) continue;
            if (this.sideways.getValue().booleanValue()) {
                this.renderItemStackSideways(i, Minecraft.getMinecraft().thePlayer.inventory.armorInventory[i]);
                continue;
            }
            this.renderItemStack(i, Minecraft.getMinecraft().thePlayer.inventory.armorInventory[i]);
        }
    }

    @Override
    public void onCompUpdate(UpdateEvent event) {
    }

    @Override
    public void onInit() {
    }

    private void renderItemStack(Integer i, ItemStack iss) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (iss == null) {
            return;
        }
        GL11.glPushMatrix();
        int yAdd = -16 * i + 48;
        RenderHelper.enableStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(iss, (int)(this.getX() > (double)((float)sr.getScaledWidth() / 2.0f) ? this.getX() + (double)Minecraft.getMinecraft().fontRendererObj.getStringWidth(iss.getMaxDamage() - iss.getItemDamage() + "") + 3.0 : this.getX()), (int)this.getY() + yAdd);
        Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, iss, (int)(this.getX() > (double)((float)sr.getScaledWidth() / 2.0f) ? this.getX() + (double)Minecraft.getMinecraft().fontRendererObj.getStringWidth(iss.getMaxDamage() - iss.getItemDamage() + "") + 3.0 : this.getX()), (int)this.getY() + yAdd);
        RenderHelper.disableStandardItemLighting();
        GL11.glPopMatrix();
    }

    private void renderItemStackSideways(Integer i, ItemStack iss) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (iss == null) {
            return;
        }
        GL11.glPushMatrix();
        int xAdd = -16 * i + 48;
        RenderHelper.enableStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(iss, (int)(this.getX() > (double)((float)sr.getScaledWidth() / 2.0f) ? this.getX() + (double)Minecraft.getMinecraft().fontRendererObj.getStringWidth(iss.getMaxDamage() - iss.getItemDamage() + "") + 3.0 + (double)xAdd : this.getX() + (double)xAdd), (int)this.getY());
        Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, iss, (int)(this.getX() > (double)((float)sr.getScaledWidth() / 2.0f) ? this.getX() + (double)Minecraft.getMinecraft().fontRendererObj.getStringWidth(iss.getMaxDamage() - iss.getItemDamage() + "") + 3.0 + (double)xAdd : this.getX() + (double)xAdd), (int)this.getY());
        RenderHelper.disableStandardItemLighting();
        GL11.glPopMatrix();
    }
}

