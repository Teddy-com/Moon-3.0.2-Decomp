/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import me.moon.Moon;
import me.moon.module.impl.movement.Scaffold;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;

public class MovementInputFromOptions
extends MovementInput {
    private final GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings gameSettingsIn) {
        this.gameSettings = gameSettingsIn;
    }

    @Override
    public void updatePlayerMoveState() {
        moveStrafe = 0.0f;
        moveForward = 0.0f;
        if (this.gameSettings.keyBindForward.isKeyDown()) {
            moveForward += 1.0f;
        }
        if (this.gameSettings.keyBindBack.isKeyDown()) {
            moveForward -= 1.0f;
        }
        if (this.gameSettings.keyBindLeft.isKeyDown()) {
            moveStrafe += 1.0f;
        }
        if (this.gameSettings.keyBindRight.isKeyDown()) {
            moveStrafe -= 1.0f;
        }
        this.jump = this.gameSettings.keyBindJump.isKeyDown();
        Scaffold scaffold = (Scaffold)Moon.INSTANCE.getModuleManager().getModule("scaffold");
        boolean bl = this.sneak = this.gameSettings.keyBindSneak.pressed && (!scaffold.isEnabled() || !Scaffold.downwards.isEnabled());
        if (this.sneak) {
            moveStrafe = (float)((double)moveStrafe * 0.3);
            moveForward = (float)((double)moveForward * 0.3);
        }
    }
}

