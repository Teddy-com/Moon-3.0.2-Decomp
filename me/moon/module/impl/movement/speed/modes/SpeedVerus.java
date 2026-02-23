/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement.speed.modes;

import me.moon.event.impl.player.MotionEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.movement.Speed;
import me.moon.module.impl.movement.speed.SpeedMode;
import me.moon.utils.game.MoveUtil;

public class SpeedVerus
extends SpeedMode {
    private double stage;
    private double counter;
    private double level;

    public SpeedVerus() {
        super("Verus", Speed.Mode.VERUS);
    }

    @Override
    public void onMotion(MotionEvent event) {
        double speed = 0.0;
        switch ((Speed.verusModes)((Object)Speed.verusMode.getValue())) {
            case GROUND: {
                if (!this.mc.thePlayer.onGround || this.mc.thePlayer.posY % 0.015625 != 0.0 || this.mc.thePlayer.lastTickPosY % 0.015625 != 0.0) break;
                speed = this.mc.thePlayer.ticksExisted % 2 != 0 ? 0.612 : 0.34;
                MoveUtil.setMoveSpeed(event, speed);
                break;
            }
            case HOP: {
                speed = this.mc.thePlayer.onGround ? 0.612 : 0.359;
                MoveUtil.setMoveSpeed(event, speed);
                break;
            }
            case FASTGROUND: {
                if (!(this.level >= 10.0)) break;
                speed = this.stage <= 9.0 && this.stage > 0.0 ? 0.28 + this.stage * 0.124 : 0.612;
                MoveUtil.setMoveSpeed(event, speed);
                break;
            }
            case FLOAT: {
                if (!(this.counter > 10.0)) break;
                speed = this.stage <= 8.0 && this.stage > 0.0 ? 0.28 + this.stage * 0.124 : (!this.mc.thePlayer.onGround && this.stage <= 9.0 ? 0.135 : (this.stage == 0.0 ? 0.612 : 0.28));
                MoveUtil.setMoveSpeed(event, speed);
            }
        }
        super.onMotion(event);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        switch ((Speed.verusModes)((Object)Speed.verusMode.getValue())) {
            case FLOAT: {
                if (!event.isPre()) break;
                if (this.mc.thePlayer.isMoving()) {
                    double d;
                    this.counter += 1.0;
                    if (this.mc.thePlayer.onGround) {
                        this.mc.thePlayer.motionY = 0.42f;
                        this.stage = 1.0;
                        break;
                    }
                    this.stage += 1.0;
                    if (!(d <= 10.0)) break;
                    this.mc.thePlayer.motionY = 0.0;
                    event.setOnGround(true);
                    break;
                }
                this.counter = 0.0;
                break;
            }
            case FASTGROUND: {
                if (!event.isPre()) break;
                if (this.mc.thePlayer.isMoving() && this.mc.thePlayer.onGround && this.mc.thePlayer.posY % 0.015625 == 0.0 && this.mc.thePlayer.lastTickPosY % 0.015625 == 0.0) {
                    double d;
                    this.level += 1.0;
                    double y = this.mc.thePlayer.posY;
                    if (this.counter == 0.0) {
                        y += 0.02;
                        this.stage = 1.0;
                        this.counter = 1.0;
                        event.setOnGround(true);
                    }
                    this.stage += 1.0;
                    if (d > 10.0) {
                        event.setOnGround(false);
                        this.counter = 0.0;
                        this.stage = 1.0;
                    }
                    event.setY(y);
                    break;
                }
                this.level = 0.0;
                break;
            }
            case GROUND: {
                if (!event.isPre() || !this.mc.thePlayer.onGround || this.mc.thePlayer.posY % 0.015625 != 0.0 || this.mc.thePlayer.lastTickPosY % 0.015625 != 0.0) break;
                event.setOnGround(this.mc.thePlayer.ticksExisted % 2 == 0);
                double y = this.mc.thePlayer.posY;
                if (this.mc.thePlayer.ticksExisted % 2 == 0) {
                    y += 0.02;
                }
                event.setY(y);
                break;
            }
            case HOP: {
                if (!event.isPre() || !this.mc.thePlayer.onGround || !this.mc.thePlayer.isMoving()) break;
                this.mc.thePlayer.motionY = 0.42f;
            }
        }
        super.onUpdate(event);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        this.level = 0.0;
        this.stage = 0.0;
        this.counter = 0.0;
    }
}

