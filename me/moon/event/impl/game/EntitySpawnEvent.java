/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.impl.game;

import me.moon.event.Event;
import net.minecraft.entity.EntityLivingBase;

public class EntitySpawnEvent
extends Event {
    public EntityLivingBase spawnedEntity;

    public EntitySpawnEvent(EntityLivingBase spawnedEntity) {
        this.spawnedEntity = spawnedEntity;
    }

    public EntityLivingBase getSpawnedEntity() {
        return this.spawnedEntity;
    }
}

