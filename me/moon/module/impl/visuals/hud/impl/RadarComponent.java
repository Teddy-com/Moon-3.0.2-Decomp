/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.hud.impl;

import java.awt.Color;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.visuals.hud.Component;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class RadarComponent
extends Component {
    public NumberValue<Integer> width = new NumberValue<Integer>("Size", 100, 50, 250, 1);

    public RadarComponent(String name) {
        super(name, 2.0, 2.0);
    }

    @Override
    public void onCompRender(ScaledResolution sr) {
        float xValue = (float)this.getX();
        float yValue = (float)this.getY();
        RenderUtil.drawBorderedRect(xValue, yValue, ((Integer)this.width.getValue()).intValue(), ((Integer)this.width.getValue()).intValue(), 0.5, new Color(15, 15, 15).getRGB(), new Color(35, 35, 35).getRGB());
        RenderUtil.drawRect((double)xValue + 1.5, (double)yValue + 1.5, (Integer)this.width.getValue() - 3, (Integer)this.width.getValue() - 3, new Color(40, 40, 40).getRGB());
        RenderUtil.drawRect(xValue + (float)((Integer)this.width.getValue()).intValue() / 2.0f, (double)yValue + 1.5, 1.0, (double)((Integer)this.width.getValue()).intValue() - 3.0, new Color(25, 25, 25, 255).getRGB());
        RenderUtil.drawRect((double)xValue + 1.5, yValue + (float)((Integer)this.width.getValue()).intValue() / 2.0f, (double)((Integer)this.width.getValue()).intValue() - 3.0, 1.0, new Color(25, 25, 25, 255).getRGB());
        GlStateManager.pushMatrix();
        GlStateManager.translate(xValue + (float)((Integer)this.width.getValue()).intValue() / 2.0f, yValue + (float)((Integer)this.width.getValue()).intValue() / 2.0f, 0.0f);
        for (Entity ent : Minecraft.getMinecraft().theWorld.playerEntities) {
            if (ent.isInvisible() || ent == Minecraft.getMinecraft().thePlayer) continue;
            double difX = ent.prevPosX + (ent.posX - ent.prevPosX) * (double)Minecraft.getMinecraft().timer.renderPartialTicks - (Minecraft.getMinecraft().thePlayer.prevPosX + (Minecraft.getMinecraft().thePlayer.posX - Minecraft.getMinecraft().thePlayer.prevPosX) * (double)Minecraft.getMinecraft().timer.renderPartialTicks);
            double difZ = ent.prevPosZ + (ent.posZ - ent.prevPosZ) * (double)Minecraft.getMinecraft().timer.renderPartialTicks - (Minecraft.getMinecraft().thePlayer.prevPosZ + (Minecraft.getMinecraft().thePlayer.posZ - Minecraft.getMinecraft().thePlayer.prevPosZ) * (double)Minecraft.getMinecraft().timer.renderPartialTicks);
            float cos = (float)Math.cos((double)this.mc.thePlayer.rotationYaw * Math.PI / 180.0);
            float sin = (float)Math.sin((double)this.mc.thePlayer.rotationYaw * Math.PI / 180.0);
            float posY = (float)(-(difZ * (double)cos - difX * (double)sin));
            float posX = (float)(-(difX * (double)cos + difZ * (double)sin));
            float clampedX = MathHelper.clamp_float(posX, -((float)((Integer)this.width.getValue()).intValue() / 2.0f) + 2.5f, (float)((Integer)this.width.getValue()).intValue() / 2.0f - 5.0f);
            float clampedY = MathHelper.clamp_float(posY, -((float)((Integer)this.width.getValue()).intValue() / 2.0f) + 2.5f, (float)((Integer)this.width.getValue()).intValue() / 2.0f - 5.0f);
            RenderUtil.drawBorderedRect(clampedX, clampedY, 3.0, 3.0, 0.5, -16777216, this.getBlockColor((int)this.mc.thePlayer.getDistance(ent.posX, this.mc.thePlayer.posY, ent.posZ)));
        }
        GlStateManager.popMatrix();
    }

    private int getBlockColor(int count) {
        float maxDistance = 70.0f;
        float hue = Math.max(0.0f, Math.min((float)count, maxDistance) / maxDistance);
        return Color.HSBtoRGB(hue / 3.0f, 1.0f, 1.0f) | 0xFF000000;
    }

    @Override
    public void onCompUpdate(UpdateEvent event) {
    }

    @Override
    public void onInit() {
    }
}

