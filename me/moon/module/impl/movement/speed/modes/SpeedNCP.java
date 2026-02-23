/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement.speed.modes;

import me.moon.event.impl.player.MotionEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.movement.Speed;
import me.moon.module.impl.movement.speed.SpeedMode;
import me.moon.utils.game.MoveUtil;
import net.minecraft.potion.Potion;

public class SpeedNCP
extends SpeedMode {
    public SpeedNCP() {
        super("NCP", Speed.Mode.NCP);
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
                double motionY = 0.4025;
                if (this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f || !this.mc.thePlayer.onGround) break;
                if (this.mc.thePlayer.isPotionActive(Potion.jump)) {
                    motionY += (double)((float)(this.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f);
                }
                this.mc.thePlayer.motionY = motionY;
                event.setY(this.mc.thePlayer.motionY);
                this.moveSpeed *= 2.0;
                break;
            }
            case 3: {
                this.moveSpeed = this.lastDist - 0.8125 * (this.lastDist - this.getBaseMoveSpeed());
                break;
            }
            default: {
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
    public void onUpdate(UpdateEvent event) {
        super.onUpdate(event);
    }
}

