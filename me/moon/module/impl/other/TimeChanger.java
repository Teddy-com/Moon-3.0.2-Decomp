/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.other;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.render.Render3DEvent;
import me.moon.module.Module;
import me.moon.utils.value.impl.NumberValue;

public class TimeChanger
extends Module {
    public static NumberValue<Long> time = new NumberValue<Long>("Time", 18400L, 0L, 24000L, 100L);

    public TimeChanger() {
        super("TimeChanger", Module.Category.OTHER, new Color(9280828).getRGB());
        this.setDescription("Change client-side time.");
    }

    @Handler(value=Render3DEvent.class)
    public void onRender3d(Render3DEvent event) {
        if (this.mc.theWorld == null) {
            return;
        }
        this.mc.theWorld.setWorldTime((Long)time.getValue());
    }
}

