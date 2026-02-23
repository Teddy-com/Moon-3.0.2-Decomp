/*
 * Decompiled with CFR 0.152.
 */
package me.moon.debug;

import me.moon.utils.game.AnimationUtil;

public class Debug {
    public long timeCreated;
    public String message;
    public float alpha = 1.0f;
    public AnimationUtil animationUtil = new AnimationUtil(0.0, 0.0);

    public Debug(String message, long timeCreated) {
        this.message = message;
        this.timeCreated = timeCreated;
    }
}

