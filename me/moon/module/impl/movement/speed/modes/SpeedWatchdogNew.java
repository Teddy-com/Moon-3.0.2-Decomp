/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement.speed.modes;

import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.player.MotionEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.movement.Speed;
import me.moon.module.impl.movement.speed.SpeedMode;
import me.moon.utils.game.MoveUtil;
import net.minecraft.potion.Potion;

public class SpeedWatchdogNew
extends SpeedMode {
    public SpeedWatchdogNew() {
        super("Watchdog New", Speed.Mode.WATCHDOGNEW);
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
                this.lastDist = 0.0;
                float motionY = 0.42f;
                if (this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f || !this.mc.thePlayer.onGround) break;
                if (this.mc.thePlayer.isPotionActive(Potion.jump)) {
                    motionY += (float)(this.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.101f;
                }
                this.mc.thePlayer.motionY = motionY;
                event.setY(this.mc.thePlayer.motionY);
                double multiplied = this.moveSpeed * 1.65;
                this.moveSpeed = 0.6;
                break;
            }
            case 2: {
                double boost = 0.66;
                this.moveSpeed = this.lastDist - boost * (this.lastDist - this.getBaseMoveSpeed());
                break;
            }
            default: {
                if ((this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically) && this.stage > 0) {
                    this.stage = 0;
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.9;
            }
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
}

