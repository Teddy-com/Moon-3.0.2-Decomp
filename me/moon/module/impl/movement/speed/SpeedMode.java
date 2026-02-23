/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement.speed;

import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.player.MotionEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.movement.Speed;
import me.moon.module.impl.movement.TargetStrafe;
import me.moon.utils.game.MoveUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class SpeedMode {
    protected final Minecraft mc = Minecraft.getMinecraft();
    public String name;
    public Speed.Mode mode;
    protected int stage = 1;
    protected int stageOG = 1;
    protected int voidTicks;
    protected double moveSpeed;
    protected double lastDist;
    protected double counter;
    protected double speed;
    protected S27PacketExplosion velocity;
    protected Double verticalVelocity;

    public SpeedMode(String name, Speed.Mode mode2) {
        this.name = name;
        this.mode = mode2;
    }

    public void onEnable() {
        if (this.mc.thePlayer == null) {
            return;
        }
        this.lastDist = 0.0;
        this.moveSpeed = 0.0;
        this.stage = 0;
        this.stageOG = 0;
        this.counter = 0.0;
    }

    public void onDisable() {
        if (this.mc.thePlayer == null) {
            return;
        }
        this.mc.timer.timerSpeed = 1.0f;
        this.velocity = null;
    }

    public void onUpdate(UpdateEvent event) {
        this.lastDist = Math.sqrt((this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX) * (this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX) + (this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ) * (this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ));
        if (this.lastDist > 5.0) {
            this.lastDist = 0.0;
        }
    }

    public void onMotion(MotionEvent event) {
    }

    public void onPacket(PacketEvent event) {
        S27PacketExplosion packet;
        if (!event.isSending() && event.getPacket() instanceof S27PacketExplosion && (Math.abs((packet = (S27PacketExplosion)event.getPacket()).func_149149_c()) > 0.0f || packet.func_149144_d() > 0.0f || Math.abs(packet.func_149147_e()) > 0.0f)) {
            this.velocity = packet;
        }
        if (!event.isSending() && event.getPacket() instanceof S08PacketPlayerPosLook) {
            this.lastDist = 0.0;
        }
    }

    protected double[] getOffsets(float speed) {
        float yaw = MoveUtil.getDirection();
        return new double[]{-Math.sin(yaw) * (double)speed, Math.cos(yaw) * (double)speed};
    }

    protected float getRotationFromPosition(double x, double z) {
        double xDiff = x - this.mc.thePlayer.posX;
        double zDiff = z - this.mc.thePlayer.posZ;
        return (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
    }

    protected boolean inVoid() {
        for (int i = (int)Math.ceil(this.mc.thePlayer.posY); i >= 0; --i) {
            if (this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, (double)i, this.mc.thePlayer.posZ)).getBlock() == Blocks.air) continue;
            return false;
        }
        return true;
    }

    protected float findYaw(EntityLivingBase target, EntityPlayerSP player) {
        float yaw = this.getRotationsToEnt(player, target);
        double direction = Math.toRadians(yaw += Speed.strafeDirection ? ((Float)TargetStrafe.angle.getValue()).floatValue() : -((Float)TargetStrafe.angle.getValue()).floatValue());
        double x = target.posX;
        double z = target.posZ;
        return this.getRotationFromPosition(x += (double)(-((Float)TargetStrafe.distance.getValue()).floatValue() * MathHelper.sin((float)direction)), z += (double)(((Float)TargetStrafe.distance.getValue()).floatValue() * MathHelper.cos((float)direction)));
    }

    protected float getRotationsToEnt(EntityLivingBase target, EntityLivingBase attacker) {
        double differenceX = target.posX - attacker.posX;
        double differenceZ = target.posZ - attacker.posZ;
        float rotationYaw = (float)(Math.atan2(differenceZ, differenceX) * 180.0 / Math.PI) - 90.0f;
        float finishedYaw = attacker.rotationYaw + MathHelper.wrapAngleTo180_float(rotationYaw - attacker.rotationYaw);
        return finishedYaw;
    }

    protected double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (double)this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
        }
        return baseSpeed;
    }

    public static double getBaseMoveSpeed(double speed) {
        double baseSpeed = speed;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }
}

