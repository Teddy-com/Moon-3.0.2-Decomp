/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.impl.input;

import me.moon.event.Event;

public class KeyInputEvent
extends Event {
    private int key;

    public KeyInputEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
}

