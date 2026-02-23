/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.value.impl;

import me.moon.utils.value.Value;
import me.moon.utils.value.clamper.NumberClamper;
import me.moon.utils.value.parse.NumberParser;

public class RangedValue<T extends Number>
extends Value<T> {
    private T minimum;
    private T maximum;
    private T inc;
    private T leftValue;
    private T rightValue;

    public RangedValue(String label, T minimum, T maximum, T inc, T leftValue, T rightValue) {
        super(label, null);
        this.minimum = minimum;
        this.maximum = maximum;
        this.inc = inc;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    public RangedValue(String label, T minimum, T maximum, T inc, T leftVal, T rightVal, Value parentValueObject, String parentValue) {
        super(label, null, parentValueObject, parentValue);
        this.minimum = minimum;
        this.maximum = maximum;
        this.inc = inc;
        this.leftValue = leftVal;
        this.rightValue = rightVal;
    }

    @Override
    public T getValue() {
        return (T)((Number)this.value);
    }

    @Override
    public void setValue(T value) {
        this.value = NumberClamper.clamp(value, this.minimum, this.maximum);
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

    public T getMinimum() {
        return this.minimum;
    }

    public T getMaximum() {
        return this.maximum;
    }

    public T getLeftValue() {
        return this.leftValue;
    }

    public T getRightValue() {
        return this.rightValue;
    }

    public T getInc() {
        return this.inc;
    }

    public void setInc(T inc) {
        this.inc = inc;
    }

    public void setLeftValue(T val) {
        this.leftValue = NumberClamper.clamp(val, this.minimum, this.maximum);
    }

    public void setRightValue(T rightValue) {
        this.rightValue = NumberClamper.clamp(rightValue, this.minimum, this.maximum);
    }
}

