/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement.speed.modes;

import me.moon.event.impl.player.MotionEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.movement.Speed;
import me.moon.module.impl.movement.speed.SpeedMode;
import me.moon.utils.game.MoveUtil;

public class SpeedStrafe
extends SpeedMode {
    public SpeedStrafe() {
        super("Strafe", Speed.Mode.STRAFE);
    }

    @Override
    public void onMotion(MotionEvent event) {
        super.onMotion(event);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (event.isPre() && this.mc.thePlayer.isMoving()) {
            if (this.mc.thePlayer.onGround) {
                this.mc.thePlayer.jump();
            } else {
                MoveUtil.setSpeed(MoveUtil.getSpeed());
            }
        }
        super.onUpdate(event);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
    }
}

