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

public class SpeedMineplex
extends SpeedMode {
    public SpeedMineplex() {
        super("Mineplex", Speed.Mode.MINEPLEX);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        super.onUpdate(event);
    }

    @Override
    public void onMotion(MotionEvent event) {
        double speed = 0.0;
        this.mc.timer.timerSpeed = 1.0f;
        ++this.stage;
        if (this.mc.thePlayer.isCollidedHorizontally) {
            this.stage = 50;
        }
        if (this.mc.thePlayer.onGround && (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f)) {
            this.mc.timer.timerSpeed = 3.0f;
            this.mc.thePlayer.jump();
            this.mc.thePlayer.motionY = 0.42;
            event.setY(0.42);
            this.stage = 0;
            speed = 0.0;
            this.counter += 1.0;
        }
        if (!this.mc.thePlayer.onGround) {
            this.mc.thePlayer.motionY = this.mc.thePlayer.motionY > -0.38 ? (this.mc.thePlayer.motionY += 0.023) : (this.mc.thePlayer.motionY += 0.01);
            double slowdown = 0.006;
            speed = 0.8 - (double)this.stage * slowdown;
            if (speed < 0.0) {
                speed = 0.0;
            }
        }
        MoveUtil.setMoveSpeed(event, speed);
        super.onMotion(event);
    }

    @Override
    public void onPacket(PacketEvent event) {
        super.onPacket(event);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }
}

