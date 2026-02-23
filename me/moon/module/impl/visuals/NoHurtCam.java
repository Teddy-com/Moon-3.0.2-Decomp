/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.render.HurtcamEvent;
import me.moon.module.Module;

public class NoHurtCam
extends Module {
    public NoHurtCam() {
        super("NoHurtCam", Module.Category.VISUALS, new Color(10789534).getRGB());
    }

    @Handler(value=HurtcamEvent.class)
    public void onHurtCam(HurtcamEvent event) {
        event.setCancelled(true);
    }
}

