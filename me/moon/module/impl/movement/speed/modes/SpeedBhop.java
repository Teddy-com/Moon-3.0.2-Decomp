/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement.speed.modes;

import me.moon.event.impl.player.MotionEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.movement.Speed;
import me.moon.module.impl.movement.speed.SpeedMode;
import me.moon.utils.game.MoveUtil;

public class SpeedBhop
extends SpeedMode {
    public SpeedBhop() {
        super("BHop", Speed.Mode.BHOP);
    }

    @Override
    public void onMotion(MotionEvent event) {
        MoveUtil.setMoveSpeed(event, (Double)Speed.bSpeed.getValue());
        super.onMotion(event);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (this.mc.thePlayer.onGround) {
            this.mc.thePlayer.jump();
        }
        super.onUpdate(event);
    }
}

