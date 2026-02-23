/*
 * Decompiled with CFR 0.152.
 */
package me.moon.command.impl;

import me.moon.Moon;
import me.moon.command.Command;
import me.moon.utils.game.Printer;

public class ModulesCommand
extends Command {
    public ModulesCommand() {
        super("Modules", new String[]{"modules", "mods", "m"});
    }

    @Override
    public void onRun(String[] s) {
        StringBuilder mods = new StringBuilder("Modules (" + Moon.INSTANCE.getModuleManager().getModuleMap().values().size() + "): ");
        Moon.INSTANCE.getModuleManager().getModuleMap().values().forEach(mod -> mods.append(mod.isEnabled() ? "\u00a7a" : "\u00a7c").append(mod.getLabel()).append("\u00a7r, "));
        Printer.print(mods.toString().substring(0, mods.length() - 2));
    }
}

