/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.player;

import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.game.WorldLoadEvent;
import me.moon.module.Module;
import me.moon.utils.value.impl.BooleanValue;

public class AutoDisable
extends Module {
    private BooleanValue killaura = new BooleanValue("Disable Killaura", true);
    private BooleanValue invManager = new BooleanValue("Disable InvManager", true);
    private BooleanValue scaffold = new BooleanValue("Disable Scaffold", true);

    public AutoDisable() {
        super("AutoDisable", Module.Category.PLAYER, -1);
    }

    @Handler(value=WorldLoadEvent.class)
    public void onWorldLoad(WorldLoadEvent event) {
        if (Moon.INSTANCE.getModuleManager().getModule("Killaura").isEnabled() && this.killaura.getValue().booleanValue()) {
            Moon.INSTANCE.getModuleManager().getModule("Killaura").setEnabled(false);
            Moon.INSTANCE.getNotificationManager().addNotification("Auto disabled Killaura on world change!", 2000L);
        }
        if (Moon.INSTANCE.getModuleManager().getModule("InvManager").isEnabled() && this.invManager.getValue().booleanValue()) {
            Moon.INSTANCE.getModuleManager().getModule("InvManager").setEnabled(false);
            Moon.INSTANCE.getNotificationManager().addNotification("Auto disabled InvManager on world change!", 2000L);
        }
        if (Moon.INSTANCE.getModuleManager().getModule("Scaffold").isEnabled() && this.scaffold.getValue().booleanValue()) {
            Moon.INSTANCE.getModuleManager().getModule("Scaffold").setEnabled(false);
            Moon.INSTANCE.getNotificationManager().addNotification("Auto disabled Scaffold on world change!", 2000L);
        }
    }
}

