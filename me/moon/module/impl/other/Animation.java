/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.other;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.NumberValue;

public class Animation
extends Module {
    public static EnumValue<mode> Mode = new EnumValue<mode>("Mode", mode.LUCKY);
    public BooleanValue gay = new BooleanValue("Item Pullup", false);
    public BooleanValue slowdown = new BooleanValue("Hit Slowdown", false);
    public NumberValue<Integer> slowdownStrength = new NumberValue<Integer>("Slowdown Strength", 5, 0, 15, 1);
    public NumberValue<Float> x = new NumberValue<Float>("X", Float.valueOf(0.0f), Float.valueOf(-2.0f), Float.valueOf(2.0f), Float.valueOf(0.1f));
    public NumberValue<Float> y = new NumberValue<Float>("Y", Float.valueOf(0.0f), Float.valueOf(-2.0f), Float.valueOf(2.0f), Float.valueOf(0.1f));
    public NumberValue<Float> scale = new NumberValue<Float>("Scale", Float.valueOf(2.0f), Float.valueOf(-4.0f), Float.valueOf(4.0f), Float.valueOf(0.1f));
    public static NumberValue<Integer> astolfoSpeed = new NumberValue<Integer>("Spin Speed", 1, 0, 20, 1);

    public Animation() {
        super("Animation", Module.Category.VISUALS, new Color(16731050).getRGB());
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (this.gay.getValue().booleanValue() && event.isPre() && this.mc.thePlayer.getHeldItem() != null) {
            this.mc.thePlayer.renderArmPitch -= 18.0f;
        }
    }

    public static enum mode {
        OLD("Old"),
        NORMAL("Normal"),
        HIDE("Hide"),
        SLIDE("Slide"),
        LUCKY("Lucky"),
        EXHIBITION("Exhibition"),
        OHARE("oHare"),
        WIZZARD("Wizzard"),
        LENNOX("Lennox"),
        Custom("Custom"),
        XIV("XIV"),
        SWANG("Swang"),
        SWANK("Swank"),
        ASTOLFOSPIN("Astolfo Spin"),
        ASTOLFOSPINNY("Astolfo Spinny");

        private final String name;

        private mode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

