/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.value.impl;

import java.util.Optional;
import me.moon.utils.value.Value;
import me.moon.utils.value.parse.BooleanParser;

public class BooleanValue
extends Value<Boolean> {
    private String description;

    public BooleanValue(String label, String description, Boolean value) {
        super(label, value);
        this.description = description;
    }

    public BooleanValue(String label, Boolean value) {
        super(label, value);
        this.description = label;
    }

    public BooleanValue(String label, String description, Boolean value, Value parentValueObject, String parentValue) {
        super(label, value, parentValueObject, parentValue);
        this.description = description;
    }

    public BooleanValue(String label, Boolean value, Value parentValueObject, String parentValue) {
        super(label, value, parentValueObject, parentValue);
        this.description = label;
    }

    @Override
    public void setValue(String input) {
        Optional<Boolean> result = BooleanParser.parse(input);
        result.ifPresent(aBoolean -> {
            this.value = aBoolean;
        });
    }

    @Override
    public Boolean getValue() {
        if (this.getParentValueObject() != null && !this.getParentValueObject().getValueAsString().equalsIgnoreCase(this.getParentValue())) {
            return false;
        }
        return (Boolean)this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public void toggle() {
        BooleanValue booleanValue = this;
        booleanValue.value = (Boolean)booleanValue.value ^ true;
    }

    public boolean isEnabled() {
        if (this.getParentValueObject() != null && !this.getParentValueObject().getValueAsString().equalsIgnoreCase(this.getParentValue())) {
            return false;
        }
        return (Boolean)this.value;
    }

    public boolean isEnabledNormal() {
        return (Boolean)this.value;
    }

    public void setEnabled(boolean enabled) {
        this.value = enabled;
    }
}

