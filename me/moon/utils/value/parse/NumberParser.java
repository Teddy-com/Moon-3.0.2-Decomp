/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.value.parse;

import me.moon.utils.value.parse.NumberCaster;

public final class NumberParser {
    public static <T extends Number> T parse(String input, Class<T> numberType) throws NumberFormatException {
        return NumberCaster.cast(numberType, Double.parseDouble(input));
    }
}

