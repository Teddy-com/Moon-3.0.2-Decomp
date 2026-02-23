/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement.speed.modes;

import me.moon.event.impl.player.MotionEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.movement.Speed;
import me.moon.module.impl.movement.speed.SpeedMode;
import me.moon.utils.MathUtils;
import me.moon.utils.game.MoveUtil;

public class SpeedSpartan
extends SpeedMode {
    private double stage;
    private double counter;

    public SpeedSpartan() {
        super("Spartan", Speed.Mode.SPARTAN);
    }

    @Override
    public void onMotion(MotionEvent event) {
        if (this.mc.thePlayer.onGround) {
            if (this.stage == 1.0) {
                this.counter = 0.0;
                MoveUtil.setMoveSpeed(event, 0.679);
                this.stage = 0.0;
            }
        } else {
            double speed = 0.3625 * Math.pow(0.985f, Math.min(5.0, this.counter));
            MoveUtil.setMoveSpeed(event, speed -= MathUtils.getRandomInRange(1.0E-4, 1.1E-4));
            this.counter += 1.0;
        }
        super.onMotion(event);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            this.mc.timer.timerSpeed = 1.0f;
            if (this.mc.thePlayer.isMoving()) {
                this.mc.timer.timerSpeed = 1.0525f;
                if (this.mc.thePlayer.onGround) {
                    this.stage = 1.0;
                    this.mc.thePlayer.motionY = 0.42f;
                } else if (this.mc.thePlayer.motionY <= 0.0) {
                    this.mc.thePlayer.motionY = Math.min(this.mc.thePlayer.motionY, -0.2);
                    this.mc.thePlayer.motionY -= 0.05;
                }
            }
        }
        super.onUpdate(event);
    }

    @Override
    public void onDisable() {
        this.mc.timer.timerSpeed = 1.0f;
    }

    @Override
    public void onEnable() {
        this.stage = 0.0;
        this.counter = 0.0;
    }
}

