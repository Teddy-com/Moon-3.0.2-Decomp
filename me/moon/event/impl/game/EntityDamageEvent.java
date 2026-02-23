/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.impl.game;

import me.moon.event.Event;
import net.minecraft.entity.EntityLivingBase;

public class EntityDamageEvent
extends Event {
    private EntityLivingBase entity;
    private float damageAmount;

    public EntityDamageEvent(EntityLivingBase entity, float amount) {
        this.entity = entity;
        this.damageAmount = amount;
    }

    public float getDamageAmount() {
        return this.damageAmount;
    }

    public EntityLivingBase getEntity() {
        return this.entity;
    }
}

