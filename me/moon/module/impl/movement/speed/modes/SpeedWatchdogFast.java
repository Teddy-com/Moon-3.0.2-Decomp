/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement.speed.modes;

import me.moon.Moon;
import me.moon.event.impl.player.MotionEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.movement.Speed;
import me.moon.module.impl.movement.speed.SpeedMode;
import me.moon.utils.game.MoveUtil;
import net.minecraft.potion.Potion;

public class SpeedWatchdogFast
extends SpeedMode {
    public SpeedWatchdogFast() {
        super("Watchdog Fast", Speed.Mode.WATCHDOGFAST);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
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
                float motionY = 0.4001f;
                if (this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f || !this.mc.thePlayer.onGround) break;
                if (this.mc.thePlayer.isPotionActive(Potion.jump)) {
                    motionY += (float)(this.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.101f;
                }
                this.mc.thePlayer.motionY = motionY;
                event.setY(this.mc.thePlayer.motionY);
                if (!Moon.INSTANCE.getModuleManager().getModule("scaffold").isEnabled()) {
                    this.moveSpeed *= this.mc.thePlayer.isPotionActive(Potion.moveSpeed) ? (double)(this.mc.thePlayer.isPotionActive(Potion.jump) ? 2.025f : 2.2f) : (double)1.9f;
                    break;
                }
                this.moveSpeed *= (double)1.4f;
                break;
            }
            case 3: {
                double boost = Moon.INSTANCE.getModuleManager().getModule("scaffold").isEnabled() ? 0.715 : (double)(this.mc.thePlayer.isPotionActive(Potion.moveSpeed) ? (this.mc.thePlayer.isPotionActive(Potion.jump) ? 0.895f : 0.72f) : 0.68f);
                this.moveSpeed = this.lastDist - boost * (this.lastDist - this.getBaseMoveSpeed());
                break;
            }
            default: {
                ++this.stage;
                if ((this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically) && this.stage > 0) {
                    this.stage = this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f ? 0 : 1;
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.9;
            }
        }
        this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
        MoveUtil.setMoveSpeed(event, this.moveSpeed);
        ++this.stage;
        super.onMotion(event);
    }
}

