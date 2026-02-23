/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.impl.input;

import me.moon.event.Event;

public class MouseEvent
extends Event {
    private int button;

    public MouseEvent(int button) {
        this.button = button;
    }

    public int getButton() {
        return this.button;
    }

    public void setButton(int button) {
        this.button = button;
    }
}

