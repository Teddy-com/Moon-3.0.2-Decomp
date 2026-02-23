/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement;

import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.NumberValue;

public class Timer
extends Module {
    private final BooleanValue randomized = new BooleanValue("Tick delay", true);
    private final NumberValue<Float> tickTime = new NumberValue<Float>("Delay", Float.valueOf(1.0f), Float.valueOf(1.0f), Float.valueOf(40.0f), Float.valueOf(1.0f), this.randomized, "TRUE");
    private final NumberValue<Float> speed = new NumberValue<Float>("Timer Speed", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(10.0f), Float.valueOf(0.05f));

    public Timer() {
        super("Timer", Module.Category.MOVEMENT, -5370194);
    }

    @Override
    public void onEnable() {
        this.mc.timer.timerSpeed = ((Float)this.speed.getValue()).floatValue();
    }

    @Override
    public void onDisable() {
        if (this.mc.thePlayer == null) {
            return;
        }
        this.mc.timer.timerSpeed = 1.0f;
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        this.mc.timer.timerSpeed = (float)this.mc.thePlayer.ticksExisted % ((Float)this.tickTime.getValue()).floatValue() == 0.0f || !this.randomized.isEnabled() ? ((Float)this.speed.getValue()).floatValue() : 1.0f;
    }
}

