/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals;

import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.Module;
import me.moon.utils.render.BloomUtils;

public class Bloom
extends Module {
    public Bloom() {
        super("Bloom", Module.Category.VISUALS, -1);
    }

    @Handler(value=Render2DEvent.class)
    public void onRender2d(Render2DEvent event) {
        if (!Moon.INSTANCE.getModuleManager().getModule("Glow").isEnabled()) {
            BloomUtils.callBloom();
        }
    }
}

