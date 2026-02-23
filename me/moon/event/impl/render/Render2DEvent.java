/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.impl.render;

import me.moon.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class Render2DEvent
extends Event {
    private float partialTicks;
    private ScaledResolution scaledResolution;

    public Render2DEvent(float partialTicks, ScaledResolution scaledResolution) {
        this.partialTicks = partialTicks;
        this.scaledResolution = scaledResolution;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public ScaledResolution getScaledResolution() {
        return this.scaledResolution;
    }
}

