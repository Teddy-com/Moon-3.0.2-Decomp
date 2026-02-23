/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.text.WordUtils
 */
package me.moon.command.impl;

import me.moon.Moon;
import me.moon.command.Command;
import me.moon.utils.game.Printer;
import org.apache.commons.lang3.text.WordUtils;

public class HelpCommand
extends Command {
    public HelpCommand() {
        super("Help", new String[]{"h", "help"});
    }

    @Override
    public void onRun(String[] s) {
        Moon.INSTANCE.getCommandManager().getCommandMap().values().forEach(command -> Printer.print(WordUtils.capitalizeFully((String)command.getLabel())));
    }
}

