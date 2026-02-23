/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;

public class NoBobbing
extends Module {
    public NoBobbing() {
        super("NoBobbing", Module.Category.VISUALS, new Color(10789534).getRGB());
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            this.mc.thePlayer.cameraYaw = 0.0f;
        }
    }
}

