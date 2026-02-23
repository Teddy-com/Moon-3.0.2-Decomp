/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.value.impl;

import me.moon.utils.value.Value;

public class EnumValue<T extends Enum<T>>
extends Value<T> {
    private final T[] constants;

    public EnumValue(String name, T value) {
        super(name, value);
        this.constants = this.extractConstantsFromEnumValue((Enum)value);
    }

    public EnumValue(String name, T value, Value parentValueObject, String parentValue) {
        super(name, value, parentValueObject, parentValue);
        this.constants = this.extractConstantsFromEnumValue((Enum)value);
    }

    public T[] extractConstantsFromEnumValue(T value) {
        return (Enum[])((Enum)value).getDeclaringClass().getEnumConstants();
    }

    public String getFixedValue() {
        return ((Enum)this.getValue()).toString();
    }

    public T[] getConstants() {
        return this.constants;
    }

    public Object[] getValues(EnumValue property) {
        return property.getConstants();
    }

    public void increment() {
        Enum currentValue = (Enum)this.getValue();
        for (T constant : this.constants) {
            if (constant != currentValue) continue;
            int ordinal = ((Enum)constant).ordinal();
            T newValue = ordinal == this.constants.length - 1 ? this.constants[0] : this.constants[ordinal + 1];
            this.setValue(newValue);
            return;
        }
    }

    public void decrement() {
        Enum currentValue = (Enum)this.getValue();
        for (T constant : this.constants) {
            if (constant != currentValue) continue;
            int ordinal = ((Enum)constant).ordinal();
            T newValue = ordinal == 0 ? this.constants[this.constants.length - 1] : this.constants[ordinal - 1];
            this.setValue(newValue);
            return;
        }
    }

    @Override
    public void setValue(String string) {
        for (T constant : this.constants) {
            if (!((Enum)constant).name().equalsIgnoreCase(string)) continue;
            this.setValue(constant);
        }
    }
}

