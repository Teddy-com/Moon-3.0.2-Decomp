/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.impl.game;

import me.moon.event.Event;

public class ChatEvent
extends Event {
    private String msg;

    public ChatEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }
}

