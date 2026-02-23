/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.value.impl.EnumValue;

public class FastLadder
extends Module {
    private final EnumValue<mode> modes = new EnumValue<mode>("Mode", mode.NORMAL);

    public FastLadder() {
        super("FastLadder", Module.Category.MOVEMENT, new Color(168, 166, 158).getRGB());
        this.setDescription("Climb Faster!");
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            this.setSuffix(((mode)((Object)this.modes.getValue())).getName());
            switch ((mode)((Object)this.modes.getValue())) {
                case NORMAL: {
                    if (this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f || !this.mc.thePlayer.isOnLadder() || !this.mc.thePlayer.isCollidedHorizontally) break;
                    this.mc.thePlayer.motionY = 0.75;
                    break;
                }
                case AAC: {
                    if (this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f || !this.mc.thePlayer.isOnLadder() || !this.mc.thePlayer.isCollidedHorizontally) break;
                    this.mc.thePlayer.motionY *= 1.3;
                    break;
                }
                case SPARTAN: {
                    break;
                }
                case CUBECRAFT: {
                    if (this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f || !this.mc.thePlayer.isOnLadder() || !this.mc.thePlayer.isCollidedHorizontally) break;
                    this.mc.thePlayer.motionY *= 2.3;
                }
            }
        }
    }

    public static enum mode {
        NORMAL("Normal"),
        AAC("AAC"),
        SPARTAN("Spartan"),
        CUBECRAFT("Cubecraft");

        private final String name;

        private mode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

