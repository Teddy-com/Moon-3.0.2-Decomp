/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.value;

public abstract class Value<O> {
    protected O value;
    private O defaultValue;
    private String label;
    private Value parentValueObject;
    private String parentValue;

    public Value(String label, O value) {
        this.value = value;
        this.defaultValue = value;
        this.label = label;
    }

    public Value(String label, O value, Value parentValueObject, String parentValue) {
        this.value = value;
        this.defaultValue = value;
        this.label = label;
        this.parentValueObject = parentValueObject;
        this.parentValue = parentValue;
    }

    public Value getParentValueObject() {
        return this.parentValueObject;
    }

    public void setParentValueObject(Value parentValueObject) {
        this.parentValueObject = parentValueObject;
    }

    public String getParentValue() {
        return this.parentValue;
    }

    public void setParentValue(String parentValue) {
        this.parentValue = parentValue;
    }

    public O getValue() {
        return this.value;
    }

    public O getDefaultValue() {
        return this.defaultValue;
    }

    public String getLabel() {
        return this.label;
    }

    public void setValue(O value) {
        this.value = value;
    }

    public String getValueAsString() {
        return String.valueOf(this.value);
    }

    public abstract void setValue(String var1);
}

