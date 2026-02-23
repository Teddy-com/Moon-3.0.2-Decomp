/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.impl.render;

import me.moon.event.Event;

public class WorldRendererEvent
extends Event {
    double renderX;
    double renderY;
    double renderZ;
    float partialTicks;

    public double getRenderX() {
        return this.renderX;
    }

    public double getRenderY() {
        return this.renderY;
    }

    public double getRenderZ() {
        return this.renderZ;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public void setRenderY(double renderY) {
        this.renderY = renderY;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public void setRenderX(double renderX) {
        this.renderX = renderX;
    }

    public void setRenderZ(double renderZ) {
        this.renderZ = renderZ;
    }

    public WorldRendererEvent(Double renderX, double renderY, double renderZ, float partialTicks) {
        this.renderX = renderX;
        this.renderY = renderY;
        this.renderZ = renderZ;
        this.partialTicks = partialTicks;
    }
}

