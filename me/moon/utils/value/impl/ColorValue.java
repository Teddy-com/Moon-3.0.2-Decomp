/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.value.impl;

import java.awt.Color;
import me.moon.utils.value.Value;

public class ColorValue
extends Value<Integer> {
    public ColorValue(String label, int value) {
        super(label, value);
    }

    public ColorValue(String label, int value, Value parentValueObject, String parentValue) {
        super(label, value, parentValueObject, parentValue);
    }

    @Override
    public void setValue(String value) {
    }

    public Color getColor() {
        return new Color((Integer)this.getValue());
    }
}

