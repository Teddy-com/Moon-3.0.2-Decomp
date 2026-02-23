/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.game;

public class AnimationUtil {
    private double posX;
    private double posY;

    public AnimationUtil(double x, double y) {
        this.posX = x;
        this.posY = y;
    }

    public final void interpolate(double xNew, double yNew, double speed) {
        this.posX = (float)AnimationUtil.interpolate2(xNew, this.posX, speed);
        this.posY = (float)AnimationUtil.interpolate2(yNew, this.posY, speed);
    }

    public double getPosX() {
        return this.posX;
    }

    public void setPosX(double newX) {
        this.posX = newX;
    }

    public double getPosY() {
        return this.posY;
    }

    public void setPosY(double newY) {
        if (this.posY != newY) {
            this.posY = newY;
        }
    }

    public static double interpolate2(double newPos, double oldPos, double speed) {
        if (speed < 0.0) {
            speed = 1.0;
        }
        if (speed > 1.0) {
            speed = 1.0;
        }
        return oldPos + (newPos - oldPos) * speed;
    }

    public final void linear(double xNew, double yNew, double speed) {
        this.posX = (float)AnimationUtil.interpolate3(xNew, this.posX, speed);
        this.posY = (float)AnimationUtil.interpolate3(yNew, this.posY, speed);
    }

    public final void linearX(double xNew, double speed) {
        this.posX = (float)AnimationUtil.interpolate3(xNew, this.posX, speed);
    }

    public static double interpolate3(double newPos, double oldPos, double speed) {
        double animated = oldPos;
        if (newPos < animated || newPos > animated) {
            animated = Math.abs(newPos - animated) <= speed ? newPos : (animated += animated < newPos ? speed : -speed);
        }
        return animated;
    }
}

