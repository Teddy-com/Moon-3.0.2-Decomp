/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals;

import me.moon.module.Module;
import me.moon.utils.value.impl.NumberValue;

public class ItemSize
extends Module {
    public NumberValue<Float> x = new NumberValue<Float>("X", Float.valueOf(0.0f), Float.valueOf(-2.0f), Float.valueOf(2.0f), Float.valueOf(0.1f));
    public NumberValue<Float> y = new NumberValue<Float>("Y", Float.valueOf(0.0f), Float.valueOf(-2.0f), Float.valueOf(2.0f), Float.valueOf(0.1f));
    public NumberValue<Float> scale = new NumberValue<Float>("Scale", Float.valueOf(2.0f), Float.valueOf(-4.0f), Float.valueOf(4.0f), Float.valueOf(0.1f));

    public ItemSize() {
        super("ItemSize", Module.Category.VISUALS, -1);
    }
}

