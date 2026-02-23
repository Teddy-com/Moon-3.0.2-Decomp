/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement;

import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;

public class Sprint
extends Module {
    public Sprint() {
        super("Sprint", Module.Category.MOVEMENT, -5588054);
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (this.mc.thePlayer == null) {
            return;
        }
        this.mc.thePlayer.setSprinting(this.canSprint());
    }

    private boolean canSprint() {
        return this.mc.thePlayer.getFoodStats().getFoodLevel() > 7 && (this.mc.gameSettings.keyBindForward.isKeyDown() | this.mc.gameSettings.keyBindBack.isKeyDown() || this.mc.gameSettings.keyBindLeft.isKeyDown() || this.mc.gameSettings.keyBindRight.isKeyDown());
    }
}

