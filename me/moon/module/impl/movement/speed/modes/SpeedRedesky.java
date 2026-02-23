/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement.speed.modes;

import me.moon.event.impl.player.MotionEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.movement.Speed;
import me.moon.module.impl.movement.speed.SpeedMode;
import me.moon.utils.game.MoveUtil;

public class SpeedRedesky
extends SpeedMode {
    private double stage;
    private double counter;
    private double groundX;
    private double groundZ;

    public SpeedRedesky() {
        super("Redesky", Speed.Mode.REDESKY);
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
                this.counter += 1.0;
                this.stage = 0.0;
                this.groundX = this.mc.thePlayer.posX;
                this.groundZ = this.mc.thePlayer.posZ;
            } else {
                double d;
                this.stage += 1.0;
                if (d <= 2.0 && this.counter % 3.0 != 0.0) {
                    MoveUtil.setSpeed(MoveUtil.getSpeed() + 0.1);
                }
            }
        }
        super.onUpdate(event);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        this.stage = 0.0;
        this.counter = 0.0;
    }
}

