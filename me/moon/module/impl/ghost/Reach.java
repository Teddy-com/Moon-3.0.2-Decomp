/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.ghost;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.render.ReachRenderEvent;
import me.moon.module.Module;
import me.moon.utils.value.impl.NumberValue;

public class Reach
extends Module {
    public static final NumberValue<Float> range = new NumberValue<Float>("Range", Float.valueOf(3.1f), Float.valueOf(3.0f), Float.valueOf(6.0f), Float.valueOf(0.01f));

    public Reach() {
        super("Reach", Module.Category.GHOST, new Color(10789534).getRGB());
        this.setDescription("Increase your reach");
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
    }

    @Handler(value=ReachRenderEvent.class)
    public void onReachRender(ReachRenderEvent event) {
        event.reach = Math.max(this.mc.playerController.getBlockReachDistance(), ((Float)range.getValue()).floatValue());
    }
}

