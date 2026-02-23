/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals;

import me.moon.module.Module;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.NumberValue;

public class Blur
extends Module {
    public NumberValue<Integer> radius = new NumberValue<Integer>("Radius", 20, 1, 200, 1);
    public static final BooleanValue toasterBlur = new BooleanValue("Toaster Blur", false);
    public static final BooleanValue blurChat = new BooleanValue("Blur Chat", false);
    public static final BooleanValue blurScoreboard = new BooleanValue("Blur Scoreboard", false);
    public static final BooleanValue blurTabGUI = new BooleanValue("Blur TabGUI", false);

    public Blur() {
        super("Blur", Module.Category.VISUALS, -1);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }
}

