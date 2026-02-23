/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.game;

import me.moon.utils.game.AnimationEnum;
import net.minecraft.util.MathHelper;

public class Animator {
    private long startMs;
    private long durationMs;

    public final void restart(long durationMs) {
        this.startMs = System.currentTimeMillis();
        this.durationMs = durationMs;
    }

    private double getRawMultiplier() {
        return (double)(this.durationMs - (this.startMs + this.durationMs - System.currentTimeMillis())) / (double)this.durationMs;
    }

    public final float getMultiplier(AnimationEnum enumSpeedCurve) {
        if (System.currentTimeMillis() >= this.startMs + this.durationMs) {
            return 1.0f;
        }
        switch (enumSpeedCurve) {
            case LINEAR: {
                return (float)this.getRawMultiplier();
            }
            case EASE_IN: {
                return (1.0f + MathHelper.sin((float)Math.toRadians(-90.0 + this.getRawMultiplier() * 180.0))) * 0.5f;
            }
            case EASE_OUT: {
                return MathHelper.sin((float)Math.toRadians(this.getRawMultiplier() * 90.0));
            }
            case EASE_IN_OUT: {
                return 1.0f + MathHelper.sin((float)Math.toRadians(-90.0 + this.getRawMultiplier() * 90.0));
            }
        }
        return (float)this.getRawMultiplier();
    }
}

