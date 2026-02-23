/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.impl.player;

import me.moon.event.Event;

public class PushEvent
extends Event {
    private boolean pre;

    public PushEvent(boolean pre) {
        this.pre = pre;
    }

    public boolean isPre() {
        return this.pre;
    }
}

