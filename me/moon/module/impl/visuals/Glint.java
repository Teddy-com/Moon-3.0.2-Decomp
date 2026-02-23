/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals;

import me.moon.module.Module;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.ColorValue;

public class Glint
extends Module {
    public static ColorValue colorValue = new ColorValue("Glint Color", -65536);
    public static BooleanValue booleanValue = new BooleanValue("Armor", false);

    public Glint() {
        super("Glint", Module.Category.VISUALS, -1);
    }
}

