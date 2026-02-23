/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.value.clamper;

public class NumberClamper {
    private NumberClamper() {
    }

    public static <T extends Number> T clamp(T value, T min, T max) {
        return ((Comparable)((Object)value)).compareTo(min) < 0 ? min : (((Comparable)((Object)value)).compareTo(max) > 0 ? max : value);
    }
}

