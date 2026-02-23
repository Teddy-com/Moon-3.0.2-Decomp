/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.player.SlowdownEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.value.impl.EnumValue;

public class NoWeb
extends Module {
    private final EnumValue<Mode> mode = new EnumValue<Mode>("Mode", Mode.NORMAL);
    private boolean isInWeb = false;

    public NoWeb() {
        super("NoWeb", Module.Category.MOVEMENT, new Color(168, 166, 158).getRGB());
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        this.setSuffix(((Mode)((Object)this.mode.getValue())).getName());
        if (event.isPre()) {
            switch ((Mode)((Object)this.mode.getValue())) {
                case NORMAL: {
                    break;
                }
                case INTAVE: {
                    if (!this.isInWeb) break;
                    break;
                }
            }
            this.isInWeb = false;
        }
    }

    @Handler(value=SlowdownEvent.class)
    public void onSlowDown(SlowdownEvent event) {
        switch ((Mode)((Object)this.mode.getValue())) {
            case NORMAL: {
                switch (event.getType()) {
                    case Web: {
                        event.setCancelled(true);
                        break;
                    }
                    case SoulSand: {
                        event.setCancelled(true);
                    }
                }
                break;
            }
            case INTAVE: {
                switch (event.getType()) {
                    case Web: {
                        event.setMotionY(0.095);
                        this.isInWeb = true;
                    }
                }
                break;
            }
            case DEV: {
                switch (event.getType()) {
                    case Web: {
                        event.setMotionX(event.getMotionX() * 2.0);
                        event.setMotionZ(event.getMotionZ() * 2.0);
                        this.isInWeb = true;
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        this.isInWeb = false;
    }

    public static enum Mode {
        NORMAL("Normal"),
        INTAVE("Intave"),
        DEV("Dev");

        private final String name;

        private Mode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

