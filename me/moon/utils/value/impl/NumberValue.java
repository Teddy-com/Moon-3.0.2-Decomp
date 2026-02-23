/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.value.impl;

import me.moon.utils.value.Value;
import me.moon.utils.value.clamper.NumberClamper;
import me.moon.utils.value.parse.NumberParser;

public class NumberValue<T extends Number>
extends Value<T> {
    private final T minimum;
    private final T maximum;
    private final T inc;

    public NumberValue(String label, T value, T minimum, T maximum, T inc) {
        super(label, value);
        this.minimum = minimum;
        this.maximum = maximum;
        this.inc = inc;
    }

    public NumberValue(String label, T value, T minimum, T maximum, T inc, Value parentValueObject, String parentValue) {
        super(label, value, parentValueObject, parentValue);
        this.minimum = minimum;
        this.maximum = maximum;
        this.inc = inc;
    }

    public T getMinimum() {
        return this.minimum;
    }

    public T getMaximum() {
        return this.maximum;
    }

    public T getInc() {
        return this.inc;
    }

    @Override
    public T getValue() {
        return (T)((Number)this.value);
    }

    @Override
    public void setValue(T value) {
        this.value = NumberClamper.clamp(value, this.minimum, this.maximum);
    }

    public T getSliderValue() {
        return (T)((Number)this.value);
    }

    @Override
    public void setValue(String value) {
        try {
            this.setValue((T)NumberParser.parse(value, ((Number)this.value).getClass()));
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
    }
}

