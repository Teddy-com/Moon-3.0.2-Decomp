/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.impl.render;

import me.moon.event.Event;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;

public class RenderEntityModelEvent
extends Event {
    private EntityLivingBase entityLivingBase;
    private ModelBase model;
    private float p_77036_2_;
    private float p_77036_3_;
    private float p_77036_4_;
    private float p_77036_5_;
    private float p_77036_6_;
    private float p_77036_7_;
    public boolean pre;

    public RenderEntityModelEvent(boolean pre, EntityLivingBase entityLivingBase, ModelBase model, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_) {
        this.entityLivingBase = entityLivingBase;
        this.model = model;
        this.p_77036_2_ = p_77036_2_;
        this.p_77036_3_ = p_77036_3_;
        this.p_77036_4_ = p_77036_4_;
        this.p_77036_5_ = p_77036_5_;
        this.p_77036_6_ = p_77036_6_;
        this.p_77036_7_ = p_77036_7_;
        this.pre = pre;
    }

    public float getP_77036_2_() {
        return this.p_77036_2_;
    }

    public float getP_77036_3_() {
        return this.p_77036_3_;
    }

    public float getP_77036_4_() {
        return this.p_77036_4_;
    }

    public float getP_77036_5_() {
        return this.p_77036_5_;
    }

    public float getP_77036_6_() {
        return this.p_77036_6_;
    }

    public float getP_77036_7_() {
        return this.p_77036_7_;
    }

    public ModelBase getModel() {
        return this.model;
    }

    public EntityLivingBase getEntityLivingBase() {
        return this.entityLivingBase;
    }

    public boolean isPre() {
        return this.pre;
    }
}

