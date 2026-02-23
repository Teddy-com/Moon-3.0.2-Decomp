/*
 * Decompiled with CFR 0.152.
 */
package me.moon.command.impl;

import me.moon.Moon;
import me.moon.command.Command;
import me.moon.module.Module;
import me.moon.utils.game.Printer;

public class ToggleCommand
extends Command {
    public ToggleCommand() {
        super("Toggle", new String[]{"t", "toggle"});
    }

    @Override
    public void onRun(String[] s) {
        if (s.length <= 1) {
            Printer.print("Not enough args.");
            return;
        }
        for (Module m : Moon.INSTANCE.getModuleManager().getModuleMap().values()) {
            if (!m.getLabel().toLowerCase().equals(s[1])) continue;
            m.toggle();
            Printer.print("Toggled " + m.getLabel());
            Moon.INSTANCE.getNotificationManager().addNotification("Toggled " + m.getLabel(), 3000L);
            break;
        }
    }
}

