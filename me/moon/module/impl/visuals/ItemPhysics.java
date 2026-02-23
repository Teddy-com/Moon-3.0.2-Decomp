/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals;

import me.moon.module.Module;
import me.moon.utils.value.impl.NumberValue;

public class ItemPhysics
extends Module {
    public static final NumberValue<Float> speed = new NumberValue<Float>("Speed", Float.valueOf(0.5f), Float.valueOf(0.0f), Float.valueOf(20.0f), Float.valueOf(0.1f));

    public ItemPhysics() {
        super("ItemPhysics", Module.Category.VISUALS, -1);
    }
}

