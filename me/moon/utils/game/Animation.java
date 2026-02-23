/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.game;

import me.moon.utils.game.AnimationEnum;
import me.moon.utils.game.Animator;

public final class Animation
extends Animator {
    private boolean revert;
    private int progressBackInt;
    private int progressForthInt;
    private float progressBackFloat;
    private float progressForthFloat;

    private void toggleAnimation(boolean b, long n) {
        if (b) {
            if (!this.revert) {
                this.restart(n);
                this.revert = true;
            }
        } else if (this.revert) {
            this.restart(n);
            this.revert = false;
        }
    }

    public int getAnimatedValue(int n, int n2, boolean b, long n3, AnimationEnum enumSpeedCurve) {
        this.toggleAnimation(b, n3);
        return n | (b ? (this.progressBackInt = this.progressForthInt | Math.round((float)(n2 - n - this.progressForthInt) * this.getMultiplier(enumSpeedCurve))) : (this.progressForthInt = Math.round((float)this.progressBackInt - (float)this.progressBackInt * this.getMultiplier(enumSpeedCurve))));
    }

    public float getAnimatedValue(float n, float n2, boolean b, long n3, AnimationEnum enumSpeedCurve) {
        this.toggleAnimation(b, n3);
        return n + (b ? (this.progressBackFloat = this.progressForthFloat + (n2 - n - this.progressForthFloat) * this.getMultiplier(enumSpeedCurve)) : (this.progressForthFloat = this.progressBackFloat - this.progressBackFloat * this.getMultiplier(enumSpeedCurve)));
    }
}

