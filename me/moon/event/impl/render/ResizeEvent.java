/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.impl.render;

import me.moon.event.Event;

public class ResizeEvent
extends Event {
    private int width;
    private int height;
    private boolean pre;

    public ResizeEvent(int width, int height, boolean pre) {
        this.width = width;
        this.height = height;
        this.pre = pre;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public boolean isPre() {
        return this.pre;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}

