/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.moon.module.impl.player;

import java.awt.Color;
import java.util.Arrays;
import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import org.lwjgl.input.Keyboard;

public class InvWalk
extends Module {
    public InvWalk() {
        super("InvWalk", Module.Category.PLAYER, new Color(9699303).getRGB());
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (this.mc.currentScreen != null && event.isPre() && !(this.mc.currentScreen instanceof GuiChat)) {
            MovementInput cfr_ignored_0 = this.mc.thePlayer.movementInput;
            MovementInput.moveForward = 1.0f;
            if (Keyboard.isKeyDown((int)208)) {
                this.mc.thePlayer.rotationPitch += 2.0f;
            }
            if (Keyboard.isKeyDown((int)200)) {
                this.mc.thePlayer.rotationPitch -= 2.0f;
            }
            if (Keyboard.isKeyDown((int)205)) {
                this.mc.thePlayer.rotationYaw += 2.0f;
            }
            if (Keyboard.isKeyDown((int)203)) {
                this.mc.thePlayer.rotationYaw -= 2.0f;
            }
            KeyBinding[] keys = new KeyBinding[]{this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindBack, this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindRight, this.mc.gameSettings.keyBindJump};
            Arrays.stream(keys).forEach(key -> KeyBinding.setKeyBindState(key.getKeyCode(), Keyboard.isKeyDown((int)key.getKeyCode())));
        }
    }
}

