/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.impl.player;

import me.moon.event.Event;
import net.minecraft.entity.Entity;

public class StepEvent
extends Event {
    private Entity entity;
    private float height;
    private boolean pre;

    public StepEvent(Entity entity, boolean pre) {
        this.entity = entity;
        this.height = entity.stepHeight;
        this.pre = pre;
    }

    public StepEvent(Entity entity) {
        this.entity = entity;
        this.height = entity.stepHeight;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isPre() {
        return this.pre;
    }
}

