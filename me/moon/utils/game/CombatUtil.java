/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.game;

import java.util.List;
import me.moon.Moon;
import me.moon.event.impl.input.SwingEvent;
import me.moon.module.impl.combat.AntiBot;
import me.moon.module.impl.combat.AuraDev;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import optifine.Reflector;

public class CombatUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static double yawDist(EntityLivingBase e) {
        if (e != null) {
            Vec3 difference = e.getPositionVector().addVector(0.0, e.getEyeHeight() / 2.0f, 0.0).subtract(Minecraft.getMinecraft().thePlayer.getPositionVector().addVector(0.0, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0));
            double d = Math.abs((double)Minecraft.getMinecraft().thePlayer.rotationYaw - (Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0)) % 360.0;
            return d > 180.0 ? 360.0 - d : d;
        }
        return 0.0;
    }

    public double yawDistance(EntityLivingBase e) {
        if (e != null) {
            Vec3 difference = e.getPositionVector().addVector(0.0, e.getEyeHeight() / 2.0f, 0.0).subtract(Minecraft.getMinecraft().thePlayer.getPositionVector().addVector(0.0, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0));
            double d = Math.abs((double)Minecraft.getMinecraft().thePlayer.rotationYaw - (Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0)) % 360.0;
            return d > 180.0 ? 360.0 - d : d;
        }
        return 0.0;
    }

    public static double rotationDist(EntityLivingBase e) {
        if (e != null) {
            Vec3 difference = e.getPositionVector().addVector(0.0, e.getEyeHeight() / 2.0f, 0.0).subtract(Minecraft.getMinecraft().thePlayer.getPositionVector().addVector(0.0, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0));
            double d = Math.abs((double)Minecraft.getMinecraft().thePlayer.rotationYaw - (Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0)) % 360.0;
            return d > 180.0 ? 360.0 - d : d;
        }
        return 0.0;
    }

    public static double serverYawDist(EntityLivingBase e) {
        if (e != null) {
            Vec3 difference = e.getPositionVector().addVector(0.0, e.getEyeHeight() / 2.0f, 0.0).subtract(Minecraft.getMinecraft().thePlayer.getPositionVector().addVector(0.0, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0));
            double d = Math.abs((double)Minecraft.getMinecraft().thePlayer.getLastReportedYaw() - (Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0)) % 360.0;
            return d > 180.0 ? 360.0 - d : d;
        }
        return 0.0;
    }

    public static float updateRotation(float yaw, float wanted, float max) {
        float f = MathHelper.wrapAngleTo180_float(wanted - yaw);
        if (f > max) {
            f = max;
        }
        if (f < -max) {
            f = -max;
        }
        return yaw + f;
    }

    public static float getSensitivityMultiplier() {
        float f = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6f + 0.2f;
        return f * f * f * 8.0f * 0.15f;
    }

    public void attackEntity(EntityLivingBase entity, boolean unBlock, int particlesMultiplier) {
        Moon.INSTANCE.getEventBus().fireEvent(new SwingEvent());
        if (unBlock) {
            CombatUtil.mc.playerController.syncCurrentPlayItem();
            CombatUtil.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
        CombatUtil.mc.thePlayer.swingItem();
        CombatUtil.mc.playerController.attackEntity(CombatUtil.mc.thePlayer, entity);
        for (int i = 0; i < particlesMultiplier; ++i) {
            boolean flag = CombatUtil.mc.thePlayer.fallDistance > 0.0f && !CombatUtil.mc.thePlayer.onGround && !CombatUtil.mc.thePlayer.isOnLadder() && !CombatUtil.mc.thePlayer.isInWater() && !CombatUtil.mc.thePlayer.isPotionActive(Potion.blindness) && CombatUtil.mc.thePlayer.ridingEntity == null && entity instanceof EntityLivingBase;
            float f1 = entity != null ? EnchantmentHelper.func_152377_a(CombatUtil.mc.thePlayer.getHeldItem(), entity.getCreatureAttribute()) : EnchantmentHelper.func_152377_a(CombatUtil.mc.thePlayer.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
            if (flag) {
                CombatUtil.mc.thePlayer.onCriticalHit(entity);
            }
            if (!(f1 > 0.0f)) continue;
            CombatUtil.mc.thePlayer.onEnchantmentCritical(entity);
        }
    }

    public boolean nearbyTargets(AuraDev auraDev, boolean block) {
        for (Object e : CombatUtil.mc.theWorld.loadedEntityList) {
            if (!(e instanceof EntityLivingBase) || !this.isTargetable(auraDev, (EntityLivingBase)e, CombatUtil.mc.thePlayer, block)) continue;
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isTargetable(AuraDev auraDev, EntityLivingBase entity, EntityPlayerSP clientPlayer, boolean block) {
        boolean isTeamMob = false;
        if (entity instanceof EntityWither) {
            float f = clientPlayer.getDistanceToEntity(entity);
            Float f2 = block ? (Float)auraDev.blockRange.getValue() : (Float)auraDev.range.getValue();
            if (f <= f2.floatValue() && auraDev.monsters.isEnabled()) {
                EntityWither a = (EntityWither)entity;
                boolean teamChecks = false;
                EnumChatFormatting myCol = null;
                EnumChatFormatting enemyCol = null;
                for (EnumChatFormatting col : EnumChatFormatting.values()) {
                    if (col == EnumChatFormatting.RESET) continue;
                    if (CombatUtil.mc.thePlayer.getDisplayName().getFormattedText().substring(0, 2).contains(col.toString()) && myCol == null) {
                        myCol = col;
                    }
                    if (!a.getDisplayName().getFormattedText().contains(col.toString()) || enemyCol != null) continue;
                    enemyCol = col;
                }
                try {
                    if (myCol != null && enemyCol != null) {
                        isTeamMob = teamChecks = myCol == enemyCol;
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        if (!auraDev.teams.isEnabled()) {
            isTeamMob = false;
        }
        if (entity.getUniqueID() == clientPlayer.getUniqueID()) return false;
        if (entity instanceof EntityPlayer) {
            if (this.isTeammate(auraDev, (EntityPlayer)entity)) return false;
        }
        if (AntiBot.getBots().contains(entity)) return false;
        if (Moon.INSTANCE.getFriendManager().isFriend(entity.getName())) return false;
        if (entity.isInvisible()) {
            if (!auraDev.invisibles.isEnabled()) return false;
        }
        float f = clientPlayer.getDistanceToEntity(entity);
        Float f3 = block ? (Float)auraDev.blockRange.getValue() : (Float)auraDev.range.getValue();
        if (!(f <= f3.floatValue())) return false;
        if (entity instanceof EntityPlayer) {
            if (auraDev.players.isEnabled()) return true;
        }
        if ((entity instanceof EntityMob || entity instanceof EntityGolem || entity instanceof EntitySlime) && auraDev.monsters.isEnabled()) {
            if (!isTeamMob) return true;
        }
        if (!(entity instanceof EntityVillager)) {
            if (!(entity instanceof EntityAnimal)) return false;
        }
        if (!auraDev.animals.isEnabled()) return false;
        return true;
    }

    public boolean isTeammate(AuraDev auraDev, EntityPlayer target) {
        boolean teamChecks = false;
        if (CombatUtil.mc.thePlayer.getDistanceToEntity(target) <= ((Float)auraDev.blockRange.getValue()).floatValue()) {
            if (!auraDev.teams.isEnabled()) {
                return false;
            }
            EnumChatFormatting myCol = null;
            EnumChatFormatting enemyCol = null;
            for (EnumChatFormatting col : EnumChatFormatting.values()) {
                if (col == EnumChatFormatting.RESET) continue;
                if (CombatUtil.mc.thePlayer.getDisplayName().getFormattedText().contains(col.toString()) && myCol == null) {
                    myCol = col;
                }
                if (!target.getDisplayName().getFormattedText().substring(0, 2).contains(col.toString()) || enemyCol != null) continue;
                enemyCol = col;
            }
            try {
                if (myCol != null && enemyCol != null) {
                    teamChecks = myCol == enemyCol;
                } else if (CombatUtil.mc.thePlayer.getTeam() != null) {
                    teamChecks = CombatUtil.mc.thePlayer.isOnSameTeam(target);
                } else if (CombatUtil.mc.thePlayer.inventory.armorInventory[3].getItem() instanceof ItemBlock) {
                    teamChecks = !ItemStack.areItemStacksEqual(CombatUtil.mc.thePlayer.inventory.armorInventory[3], target.inventory.armorInventory[3]);
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return teamChecks;
    }

    public static boolean isInRayTrace(float pitch, float yaw, double distance, double expand, Entity target) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity var2 = mc.getRenderViewEntity();
        Entity entity = null;
        if (var2 == null || mc.theWorld == null) {
            return false;
        }
        mc.mcProfiler.startSection("pick");
        Vec3 var3 = var2.getPositionEyes(0.0f);
        Vec3 var4 = var2.getLookCustom(pitch, yaw, 0.0f);
        Vec3 var5 = var3.addVector(var4.xCoord * distance, var4.yCoord * distance, var4.zCoord * distance);
        Vec3 var6 = null;
        float var7 = 1.0f;
        List<Entity> var8 = mc.theWorld.getEntitiesWithinAABBExcludingEntity(var2, var2.getEntityBoundingBox().addCoord(var4.xCoord * distance, var4.yCoord * distance, var4.zCoord * distance).expand(1.0, 1.0, 1.0));
        double var9 = distance;
        for (int var10 = 0; var10 < var8.size(); ++var10) {
            double var15;
            Entity var11 = var8.get(var10);
            if (!var11.canBeCollidedWith()) continue;
            float var12 = var11.getCollisionBorderSize();
            AxisAlignedBB var13 = var11.getEntityBoundingBox().expand(var12, var12, var12);
            var13 = var13.expand(expand, expand, expand);
            MovingObjectPosition var14 = var13.calculateIntercept(var3, var5);
            if (var13.isVecInside(var3)) {
                if (!(0.0 < var9) && var9 != 0.0) continue;
                entity = var11;
                var6 = var14 == null ? var3 : var14.hitVec;
                var9 = 0.0;
                continue;
            }
            if (var14 == null || !((var15 = var3.distanceTo(var14.hitVec)) < var9) && var9 != 0.0) continue;
            boolean canRiderInteract = false;
            if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                canRiderInteract = Reflector.callBoolean(var11, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
            }
            if (var11 == var2.ridingEntity && !canRiderInteract) {
                if (var9 != 0.0) continue;
                entity = var11;
                var6 = var14.hitVec;
                continue;
            }
            entity = var11;
            var6 = var14.hitVec;
            var9 = var15;
        }
        if (var9 < distance && !(entity instanceof EntityLivingBase) && !(entity instanceof EntityItemFrame)) {
            entity = null;
        }
        mc.mcProfiler.endSection();
        if (entity == null || var6 == null) {
            return false;
        }
        return target.getUniqueID().equals(entity.getUniqueID());
    }

    public static float[] getRotationsToEnt(Entity ent, EntityPlayerSP playerSP) {
        double differenceX = ent.posX - playerSP.posX;
        double differenceY = ent.posY + (double)ent.height - (playerSP.posY + (double)playerSP.height);
        double differenceZ = ent.posZ - playerSP.posZ;
        float rotationYaw = (float)(Math.atan2(differenceZ, differenceX) * 180.0 / Math.PI) - 90.0f;
        float rotationPitch = (float)(Math.atan2(differenceY, playerSP.getDistanceToEntity(ent)) * 180.0 / Math.PI);
        float finishedYaw = playerSP.rotationYaw + MathHelper.wrapAngleTo180_float(rotationYaw - playerSP.rotationYaw);
        float finishedPitch = playerSP.rotationPitch + MathHelper.wrapAngleTo180_float(rotationPitch - playerSP.rotationPitch);
        return new float[]{finishedYaw, -finishedPitch};
    }
}

