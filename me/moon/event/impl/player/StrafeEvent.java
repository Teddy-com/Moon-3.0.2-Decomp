/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.impl.player;

import me.moon.event.Event;

public class StrafeEvent
extends Event {
    private float yaw;
    private float forward;
    private float strafe;

    public float getStrafe() {
        return this.strafe;
    }

    public float getForward() {
        return this.forward;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }

    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public StrafeEvent(float yaw, float forward, float strafe) {
        this.yaw = yaw;
        this.forward = forward;
        this.strafe = strafe;
    }
}

