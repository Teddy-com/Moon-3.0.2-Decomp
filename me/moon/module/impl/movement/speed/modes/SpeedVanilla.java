/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement.speed.modes;

import me.moon.event.impl.player.MotionEvent;
import me.moon.module.impl.movement.Speed;
import me.moon.module.impl.movement.speed.SpeedMode;
import me.moon.utils.game.MoveUtil;

public class SpeedVanilla
extends SpeedMode {
    public SpeedVanilla() {
        super("Vanilla", Speed.Mode.VANILLA);
    }

    @Override
    public void onMotion(MotionEvent event) {
        MoveUtil.setMoveSpeed(event, (Double)Speed.vanillaSpeed.getValue());
    }
}

