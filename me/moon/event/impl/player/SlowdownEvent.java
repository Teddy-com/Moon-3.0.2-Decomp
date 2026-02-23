/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.impl.player;

import me.moon.event.Event;

public class SlowdownEvent
extends Event {
    private Type type;
    private double motionX = 0.0;
    private double motionY = 0.0;
    private double motionZ = 0.0;

    public double getMotionX() {
        return this.motionX;
    }

    public double getMotionY() {
        return this.motionY;
    }

    public double getMotionZ() {
        return this.motionZ;
    }

    public void setMotionX(double x) {
        this.motionX = x;
    }

    public void setMotionY(double y) {
        this.motionY = y;
    }

    public void setMotionZ(double z) {
        this.motionZ = z;
    }

    public SlowdownEvent(Type type, float motionX, float motionY, float motionZ) {
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
    }

    public SlowdownEvent(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public static enum Type {
        Item,
        Sprinting,
        SoulSand,
        Water,
        Web;

    }
}

