/*
 * Decompiled with CFR 0.152.
 */
package me.moon.command.impl;

import me.moon.Moon;
import me.moon.command.Command;
import net.minecraft.client.gui.GuiScreen;

public class IGNCommand
extends Command {
    public IGNCommand() {
        super("IGN", new String[]{"ign", "name"});
    }

    @Override
    public void onRun(String[] s) {
        Moon.INSTANCE.getNotificationManager().addNotification("Copied your IGN in your clipboard!", 2000L);
        GuiScreen.setClipboardString(IGNCommand.mc.thePlayer.getName());
    }
}

