/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement.speed.modes;

import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.movement.Speed;
import me.moon.module.impl.movement.speed.SpeedMode;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;

public class SpeedWatchdogGround
extends SpeedMode {
    public SpeedWatchdogGround() {
        super("Watchdog Ground", Speed.Mode.WATCHDOGGROUND);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (!Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(0.0, -1.0E-4, 0.0)).isEmpty()) {
            this.mc.thePlayer.motionY -= 0.15;
            if (this.mc.thePlayer.isMoving() && this.mc.thePlayer.onGround && !this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isOnLadder() && !this.mc.thePlayer.isCollidedHorizontally) {
                if (!this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, 0.42, 0.0)).isEmpty()) {
                    event.setY(this.mc.thePlayer.posY + (this.mc.thePlayer.ticksExisted % 2 != 0 ? 0.2 : 0.0));
                } else {
                    event.setY(this.mc.thePlayer.posY + (this.mc.thePlayer.ticksExisted % 2 != 0 ? 0.42 : 0.0));
                }
            }
            if (this.speed == 2.55) {
                ++this.stage;
            }
            this.speed = Math.max(this.mc.thePlayer.ticksExisted % 2 == 0 ? SpeedWatchdogGround.getBaseMoveSpeed(2.5) : SpeedWatchdogGround.getBaseMoveSpeed(1.35), this.getBaseMoveSpeed());
            if (this.stage >= 5 && this.speed == SpeedWatchdogGround.getBaseMoveSpeed(2.5)) {
                this.stage = 0;
                this.speed *= 1.45;
            }
            MovementInput movementInput = this.mc.thePlayer.movementInput;
            double forward = MovementInput.moveForward;
            double strafe = MovementInput.moveStrafe;
            double yaw = this.mc.thePlayer.rotationYaw;
            if (forward == 0.0 && strafe == 0.0) {
                this.mc.thePlayer.motionX = 0.0;
                this.mc.thePlayer.motionZ = 0.0;
            } else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += (double)(forward > 0.0 ? -45 : 45);
                    } else if (strafe < 0.0) {
                        yaw += (double)(forward > 0.0 ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 0.15f;
                    } else if (forward < 0.0) {
                        forward = -0.15f;
                    }
                }
                if (strafe > 0.0) {
                    strafe = 0.15f;
                } else if (strafe < 0.0) {
                    strafe = -0.15f;
                }
                this.mc.thePlayer.motionX = forward * this.speed * Math.cos(Math.toRadians(yaw + 90.0)) + strafe * this.speed * Math.sin(Math.toRadians(yaw + 90.0));
                this.mc.thePlayer.motionZ = forward * this.speed * Math.sin(Math.toRadians(yaw + 90.0)) - strafe * this.speed * Math.cos(Math.toRadians(yaw + 90.0));
            }
        }
    }
}

