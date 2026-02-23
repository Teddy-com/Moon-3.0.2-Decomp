/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.components.nostalgia;

import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.impl.visuals.components.Component;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class NArmorComponent
extends Component {
    @Override
    public void render(Render2DEvent event) {
        ScaledResolution sr = event.getScaledResolution();
        for (int i = 1; i < 5; ++i) {
            if (this.mc.thePlayer.getEquipmentInSlot(5 - i) == null) continue;
            ItemStack itemStack = this.mc.thePlayer.getEquipmentInSlot(5 - i);
            GlStateManager.pushMatrix();
            RenderHelper.enableStandardItemLighting();
            float yPos = sr.getScaledHeight();
            float rwidth = (float)sr.getScaledWidth() / 2.0f;
            switch (i) {
                case 1: {
                    yPos -= 33.0f;
                    rwidth -= 110.0f;
                    break;
                }
                case 2: {
                    yPos -= 18.0f;
                    rwidth -= 110.0f;
                    break;
                }
                case 3: {
                    yPos -= 33.0f;
                    rwidth += 95.0f;
                    break;
                }
                case 4: {
                    yPos -= 18.0f;
                    rwidth += 95.0f;
                }
            }
            this.mc.getRenderItem().renderItemIntoGUI(itemStack, rwidth, yPos);
            this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, itemStack, rwidth, yPos);
            RenderHelper.disableStandardItemLighting();
            this.mc.fontRendererObj.drawStringWithShadow(itemStack.getMaxDamage() - itemStack.getItemDamage() + "", rwidth < (float)sr.getScaledWidth() / 2.0f ? rwidth - (float)this.mc.fontRendererObj.getStringWidth(itemStack.getMaxDamage() - itemStack.getItemDamage() + "") - 2.0f : rwidth + 18.0f, yPos + 4.0f, -1);
            GlStateManager.popMatrix();
        }
        super.render(event);
    }
}

