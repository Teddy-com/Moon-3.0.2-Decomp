/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.game;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.MathHelper;

public class RotationUtil {
    public float[] getFacingRotations(int paramInt1, double d, int paramInt3) {
        EntityPig localEntityPig = new EntityPig(Minecraft.getMinecraft().theWorld);
        localEntityPig.posX = (double)paramInt1 + 0.5;
        localEntityPig.posY = d - 1.5;
        localEntityPig.posZ = (double)paramInt3 + 0.5;
        return this.getRotationsNeeded(localEntityPig);
    }

    public float[] getFacingRotations(double paramInt1, double d, double paramInt3) {
        EntityPig localEntityPig = new EntityPig(Minecraft.getMinecraft().theWorld);
        localEntityPig.posX = paramInt1 + 0.5;
        localEntityPig.posY = d + 0.5;
        localEntityPig.posZ = paramInt3 + 0.5;
        return this.getRotationsNeeded(localEntityPig);
    }

    public float[] getLegitFacingRotations(int paramInt1, double d, int paramInt3) {
        EntityPig localEntityPig = new EntityPig(Minecraft.getMinecraft().theWorld);
        localEntityPig.posX = (double)paramInt1 + 0.5;
        localEntityPig.posY = d - 4.5;
        localEntityPig.posZ = (double)paramInt3 + 0.5;
        return this.getRotationsNeeded(localEntityPig);
    }

    public float[] getRotationsNeeded(Entity entity) {
        if (entity == null) {
            return null;
        }
        Minecraft mc = Minecraft.getMinecraft();
        double xSize = entity.posX - mc.thePlayer.posX;
        double ySize = entity.posY + (double)entity.getEyeHeight() / 2.0 - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
        double zSize = entity.posZ - mc.thePlayer.posZ;
        double theta = MathHelper.sqrt_double(xSize * xSize + zSize * zSize);
        float yaw = (float)(Math.atan2(zSize, xSize) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(ySize, theta) * 180.0 / Math.PI));
        return new float[]{(mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw)) % 360.0f, (mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)) % 360.0f};
    }
}

