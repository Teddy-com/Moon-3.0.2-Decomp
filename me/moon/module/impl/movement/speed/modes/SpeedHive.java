/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement.speed.modes;

import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.movement.Speed;
import me.moon.module.impl.movement.speed.SpeedMode;
import me.moon.utils.game.MoveUtil;

public class SpeedHive
extends SpeedMode {
    public SpeedHive() {
        super("Hive", Speed.Mode.HIVE);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            if (this.mc.thePlayer.onGround && this.mc.thePlayer.isMoving()) {
                this.mc.thePlayer.motionY = 0.38f;
                MoveUtil.setJumpSpeed(1.021f);
            } else if (this.mc.thePlayer.motionY <= 0.005) {
                this.mc.thePlayer.motionY -= 0.0246;
                this.mc.thePlayer.motionX *= 1.021;
                this.mc.thePlayer.motionZ *= 1.021;
            } else if (this.mc.thePlayer.motionY >= 0.006) {
                this.mc.thePlayer.motionY -= 0.02485;
                this.mc.thePlayer.motionX *= 1.0205;
                this.mc.thePlayer.motionZ *= 1.0205;
            }
        }
        super.onUpdate(event);
    }
}

