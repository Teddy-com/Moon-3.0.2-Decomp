/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.ghost;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.module.impl.combat.AntiBot;
import me.moon.utils.MathUtils;
import me.moon.utils.game.CombatUtil;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class AimAssist
extends Module {
    private EntityLivingBase target;
    private final List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
    private final NumberValue<Float> range = new NumberValue<Float>("Range", Float.valueOf(4.0f), Float.valueOf(1.0f), Float.valueOf(7.0f), Float.valueOf(0.1f));
    private final BooleanValue players = new BooleanValue("Players", "Target Players", true);
    private final BooleanValue animals = new BooleanValue("Animals", "Target Animals", false);
    private final BooleanValue monsters = new BooleanValue("Monsters", "Target Monsters", false);
    private final BooleanValue invisibles = new BooleanValue("Invisibles", "Target Invisibles", false);

    public AimAssist() {
        super("AimAssist", Module.Category.GHOST, new Color(168, 166, 158).getRGB());
        this.setDescription("Aim for you!");
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            this.target = this.getTarget();
            if (this.target != null) {
                float[] dstAngle = this.getRotationsToEnt(this.target, this.mc.thePlayer);
                float[] srcAngle = new float[]{this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch};
                float[] angles = this.smoothAngle(dstAngle, srcAngle);
                this.mc.thePlayer.LegitRotation(angles[0], angles[1], 1, true);
            }
        }
    }

    private EntityLivingBase getTarget() {
        this.targets.clear();
        double Dist = Double.MAX_VALUE;
        if (this.mc.theWorld != null) {
            for (Object object : this.mc.theWorld.loadedEntityList) {
                EntityLivingBase e;
                if (!(object instanceof EntityLivingBase) || !((double)this.mc.thePlayer.getDistanceToEntity(e = (EntityLivingBase)object) < Dist) || !this.isTargetable(e, this.mc.thePlayer)) continue;
                this.targets.add(e);
            }
        }
        if (this.targets.isEmpty()) {
            return null;
        }
        this.targets.sort(Comparator.comparingDouble(target -> CombatUtil.yawDist(target)));
        return this.targets.get(0);
    }

    private boolean isTargetable(EntityLivingBase entity, EntityPlayerSP clientPlayer) {
        return entity.getUniqueID() != clientPlayer.getUniqueID() && entity != clientPlayer && entity.isEntityAlive() && !AntiBot.getBots().contains(entity) && !Moon.INSTANCE.getFriendManager().isFriend(entity.getName()) && (!entity.isInvisible() || this.invisibles.isEnabled()) && clientPlayer.getDistanceToEntity(entity) <= ((Float)this.range.getValue()).floatValue() && (entity instanceof EntityPlayer && this.players.isEnabled() || (entity instanceof EntityMob || entity instanceof EntityGolem) && this.monsters.isEnabled() || entity instanceof IAnimals && this.animals.isEnabled());
    }

    private float[] smoothAngle(float[] dst, float[] src) {
        float[] smoothedAngle = new float[]{src[0] - dst[0], src[1] - dst[1]};
        smoothedAngle = MathUtils.constrainAngle(smoothedAngle);
        smoothedAngle[0] = src[0] - smoothedAngle[0] / 100.0f * (float)MathUtils.getRandomInRange(14, 24);
        smoothedAngle[1] = src[1] - smoothedAngle[1] / 100.0f * (float)MathUtils.getRandomInRange(3, 8);
        return smoothedAngle;
    }

    private float[] getRotationsToEnt(Entity ent, EntityPlayerSP playerSP) {
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

