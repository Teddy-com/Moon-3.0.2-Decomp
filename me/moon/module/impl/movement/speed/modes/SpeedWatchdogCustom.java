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

public class SpeedWatchdogCustom
extends SpeedMode {
    public SpeedWatchdogCustom() {
        super("Watchdog Custom", Speed.Mode.WATCHDOGCUSTOM);
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
    public void onUpdate(UpdateEvent event) {
        if (event.isPre() && Speed.strafeBypass.isEnabled()) {
            if (event.getY() % 0.015625 == 0.0) {
                event.setY(event.getY() + 5.3424E-4);
                event.setOnGround(false);
            }
            if (this.mc.thePlayer.motionY > 0.3) {
                event.setOnGround(true);
            }
        }
        super.onUpdate(event);
    }

    @Override
    public void onMotion(MotionEvent event) {
        switch (this.stage) {
            case 0: {
                ++this.stage;
                this.lastDist = 0.0;
                break;
            }
            case 2: {
                this.lastDist = 0.0;
                float motionY = ((Float)Speed.jumpValue.getValue()).floatValue();
                if (this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f || !this.mc.thePlayer.onGround) break;
                if (this.mc.thePlayer.isPotionActive(Potion.jump)) {
                    motionY += (float)(this.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.101f;
                }
                this.mc.thePlayer.motionY = motionY;
                event.setY(this.mc.thePlayer.motionY);
                if (!Moon.INSTANCE.getModuleManager().getModule("scaffold").isEnabled()) {
                    double multiplier = (Double)Speed.groundBoost.getValue();
                    if (this.mc.thePlayer.isPotionActive(Potion.moveSpeed) || this.mc.thePlayer.isPotionActive(Potion.jump)) {
                        multiplier -= 0.2;
                    }
                    this.moveSpeed *= multiplier;
                    break;
                }
                this.moveSpeed *= (double)1.4f;
                break;
            }
            case 3: {
                double boost = this.mc.thePlayer.isPotionActive(Potion.moveSpeed) ? (Double)Speed.groundFriction.getValue() : (Double)Speed.groundFriction.getValue() + (double)0.1f;
                this.moveSpeed = this.lastDist - boost * (this.lastDist - this.getBaseMoveSpeed());
                break;
            }
            default: {
                ++this.stage;
                if ((this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically) && this.stage > 0) {
                    this.stage = this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f ? 0 : 1;
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
            }
        }
        this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
        MoveUtil.setMoveSpeed(event, this.moveSpeed);
        ++this.stage;
        super.onMotion(event);
    }

    @Override
    public void onPacket(PacketEvent event) {
        super.onPacket(event);
    }
}

