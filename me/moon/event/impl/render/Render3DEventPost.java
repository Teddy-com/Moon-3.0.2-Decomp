/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.impl.render;

import me.moon.event.Event;

public class Render3DEventPost
extends Event {
    private float partialTicks;

    public Render3DEventPost(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

