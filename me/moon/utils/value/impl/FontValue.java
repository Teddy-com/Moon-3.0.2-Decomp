/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.value.impl;

import me.moon.utils.font.MCFontRenderer;
import me.moon.utils.value.Value;

public class FontValue
extends Value<MCFontRenderer> {
    public FontValue(String label, MCFontRenderer value) {
        super(label, value);
    }

    public FontValue(String label, MCFontRenderer value, Value parentValueObject, String parentValue) {
        super(label, value, parentValueObject, parentValue);
    }

    @Override
    public void setValue(MCFontRenderer value) {
        this.value = value;
    }

    @Override
    public void setValue(String value) {
    }
}

