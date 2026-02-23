/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.impl.render;

import me.moon.event.Event;

public class Render3DEvent
extends Event {
    private float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

