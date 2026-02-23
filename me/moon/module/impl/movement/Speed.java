/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement;

import java.awt.Color;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.module.impl.movement.speed.SpeedManager;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.NumberValue;

public class Speed
extends Module {
    public static final EnumValue<Mode> mode = new EnumValue<Mode>("Mode", Mode.WATCHDOG);
    public static final NumberValue<Double> startingSpeed = new NumberValue<Double>("Starting Speed", 0.6, 0.5, 1.0, 0.05, mode, "Watchdog");
    public static final NumberValue<Double> damageBoost = new NumberValue<Double>("Damage boost", 1.0, 0.0, 2.0, 0.05, mode, "Watchdog");
    public static final BooleanValue damageJump = new BooleanValue("Damage Jump", "Damage Jump", false, mode, "Watchdog");
    public static final EnumValue<damageBoostModes> damageBoostMode = new EnumValue<damageBoostModes>("Boost Mode", damageBoostModes.SET, mode, "Watchdog");
    public static final EnumValue<verusModes> verusMode = new EnumValue<verusModes>("Verus Mode", verusModes.HOP, mode, "Verus");
    public static final NumberValue<Double> vanillaSpeed = new NumberValue<Double>("Vanilla Speed", 1.2, 0.1, 5.0, 0.1, mode, "Vanilla");
    public static final NumberValue<Double> bSpeed = new NumberValue<Double>("Bhop Speed", 1.2, 0.1, 5.0, 0.1, mode, "BHop");
    public static final BooleanValue strafeBypass = new BooleanValue("Strafe Bypass", "Strafe Bypass", true, mode, "WatchdogCustom");
    public static final NumberValue<Float> jumpValue = new NumberValue<Float>("Jump Y-Value", Float.valueOf(0.42f), Float.valueOf(0.3f), Float.valueOf(0.43f), Float.valueOf(0.01f), mode, "WatchdogCustom");
    public static final NumberValue<Double> groundFriction = new NumberValue<Double>("Ground Friction", 0.66, 0.5, 1.0, 0.01, mode, "WatchdogCustom");
    public static final NumberValue<Double> groundBoost = new NumberValue<Double>("Ground Boost", 2.14, 1.4, 2.14, 0.01, mode, "WatchdogCustom");
    public static boolean strafeDirection;
    private final SpeedManager speedManager = new SpeedManager();

    public Speed() {
        super("Speed", Module.Category.MOVEMENT, new Color(0, 255, 0, 255).getRGB());
        this.setDescription("Zoomie zoom");
        this.speedManager.init();
    }

    @Override
    public void onEnable() {
        Moon.INSTANCE.getEventBus().registerListener(this.speedManager);
        this.speedManager.onEnable();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        Moon.INSTANCE.getEventBus().unregisterListener(this.speedManager);
        this.speedManager.onDisable();
        super.onDisable();
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        this.setSuffix(((Mode)((Object)mode.getValue())).getName());
    }

    public static enum damageBoostModes {
        SET("Set"),
        ADDITION("Addition");

        private final String name;

        private damageBoostModes(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum verusModes {
        HOP("Hop"),
        GROUND("Ground"),
        FASTGROUND("FastGround"),
        FLOAT("Float"),
        DEV("Dev");

        private final String name;

        private verusModes(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum Mode {
        WATCHDOG("Watchdog"),
        WATCHDOGLOWHOP("Watchdog LowHop"),
        WATCHDOGCUSTOM("Watchdog Custom"),
        WATCHDOGGROUND("Watchdog Ground"),
        WATCHDOGFAST("Watchdog Fast"),
        WATCHDOGNEW("Watchdog New"),
        MINEPLEX("Mineplex"),
        NCP("NCP"),
        FAITHFUL("Faithful"),
        VANILLA("Vanilla"),
        BHOP("BHop"),
        REDESKY("Redesky"),
        STRAFE("Strafe"),
        VERUS("Verus"),
        SPARTAN("Spartan"),
        HIVE("Hive");

        private final String name;

        private Mode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

