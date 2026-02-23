/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.impl.render;

import me.moon.event.Event;
import net.minecraft.entity.Entity;

public class NameplateEvent
extends Event {
    private Entity entity;

    public NameplateEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}

