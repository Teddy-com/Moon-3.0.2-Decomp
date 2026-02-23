/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement.speed.modes;

import me.moon.Moon;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.player.MotionEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.movement.Speed;
import me.moon.module.impl.movement.speed.SpeedMode;
import me.moon.utils.game.MoveUtil;
import net.minecraft.potion.Potion;

public class SpeedWatchdog
extends SpeedMode {
    public SpeedWatchdog() {
        super("Watchdog", Speed.Mode.WATCHDOG);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onMotion(MotionEvent event) {
        ++this.stage;
        switch (this.stage) {
            case 1: {
                float motionY = 0.42f;
                if (!this.mc.thePlayer.onGround) break;
                if (this.mc.thePlayer.isMoving()) {
                    if (this.mc.thePlayer.isPotionActive(Potion.jump)) {
                        motionY += (float)(this.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.101f;
                    }
                    this.mc.thePlayer.motionY = motionY;
                    event.setY(this.mc.thePlayer.motionY);
                }
                if (this.moveSpeed <= this.getBaseMoveSpeed()) {
                    this.moveSpeed = SpeedWatchdog.getBaseMoveSpeed((Double)Speed.startingSpeed.getValue());
                    break;
                }
                if (!Moon.INSTANCE.getModuleManager().getModule("scaffold").isEnabled()) {
                    this.moveSpeed *= this.mc.thePlayer.isPotionActive(Potion.moveSpeed) ? (double)(this.mc.thePlayer.isPotionActive(Potion.jump) ? 1.8f : 1.95f) : (double)1.7f;
                    break;
                }
                this.moveSpeed *= 1.25;
                break;
            }
            case 2: {
                double boost = Moon.INSTANCE.getModuleManager().getModule("scaffold").isEnabled() ? 0.725 : (double)0.66f;
                this.moveSpeed = this.lastDist - boost * (this.lastDist - this.getBaseMoveSpeed());
                break;
            }
            default: {
                if ((this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically) && this.stage > 0) {
                    this.stage = this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f ? 0 : 0;
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.9;
            }
        }
        if (this.velocity != null) {
            double x = this.velocity.func_149149_c();
            double y = this.velocity.func_149144_d();
            double z = this.velocity.func_149147_e();
            double horizontal = Math.hypot(x, z);
            if (Speed.damageJump.isEnabled()) {
                this.verticalVelocity = this.verticalVelocity == null ? Double.valueOf(y) : Double.valueOf(Math.max(this.verticalVelocity, y));
            }
            if (Speed.damageBoostMode.getValue() == Speed.damageBoostModes.SET) {
                this.moveSpeed = Math.max(this.moveSpeed, horizontal * (Double)Speed.damageBoost.getValue());
            } else if (Speed.damageBoostMode.getValue() == Speed.damageBoostModes.ADDITION) {
                this.moveSpeed += horizontal * (Double)Speed.damageBoost.getValue();
            }
            this.velocity = null;
        }
        if (this.verticalVelocity != null) {
            if (this.verticalVelocity > this.mc.thePlayer.motionY && this.verticalVelocity <= (double)0.42f) {
                this.mc.thePlayer.motionY = this.verticalVelocity;
                event.setY(this.mc.thePlayer.motionY);
            }
            this.verticalVelocity = null;
        }
        this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
        MoveUtil.setMoveSpeed(event, this.moveSpeed);
        super.onMotion(event);
    }

    @Override
    public void onPacket(PacketEvent event) {
        super.onPacket(event);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            if (event.getY() % 0.015625 == 0.0 && this.mc.thePlayer.isMoving()) {
                event.setY(event.getY() + 1.0E-14);
                event.setOnGround(true);
            }
            if (this.mc.thePlayer.motionY > 0.3) {
                event.setOnGround(false);
            }
        }
        super.onUpdate(event);
    }
}

