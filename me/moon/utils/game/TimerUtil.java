/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.game;

public final class TimerUtil {
    private long time = System.nanoTime() / 1000000L;

    public boolean hasReached(long time) {
        return this.time() >= time;
    }

    public void reset() {
        this.time = System.nanoTime() / 1000000L;
    }

    public boolean sleep(long time) {
        if (this.time() >= time) {
            this.reset();
            return true;
        }
        return false;
    }

    public short convertToMS(float perSecond) {
        return (short)(1000.0f / perSecond);
    }

    public long time() {
        return System.nanoTime() / 1000000L - this.time;
    }
}

