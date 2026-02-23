/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.components.keystrokes;

import java.awt.Color;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;

public class KeystrokesCircle {
    private float radius;
    private float x;
    private float y;
    private float width;
    private float height;
    private float alpha;
    private boolean pressed;
    private AnimationUtil animationUtil = new AnimationUtil(0.0, 0.0);

    public KeystrokesCircle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render() {
        this.radius = (float)this.animationUtil.getPosX();
        RenderUtil.drawUnsmoothedCircle(this.x + this.width / 2.0f - this.radius / 2.0f, this.y + this.height / 2.0f - this.radius / 2.0f, this.radius, new Color(225, 225, 225, Math.max((int)this.alpha, 1)).getRGB());
        if (this.pressed) {
            this.alpha = 125.0f;
            this.animationUtil.setPosY(125.0);
        }
        this.animationUtil.interpolate(52.0, !this.pressed ? 0.0 : 125.0, 14.0f / (float)Minecraft.getDebugFPS());
        this.alpha = (float)this.animationUtil.getPosY();
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return this.radius;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
}

