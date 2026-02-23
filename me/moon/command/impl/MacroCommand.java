/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.moon.command.impl;

import java.io.IOException;
import me.moon.Moon;
import me.moon.command.Command;
import me.moon.utils.game.Printer;
import org.lwjgl.input.Keyboard;

public class MacroCommand
extends Command {
    public MacroCommand() {
        super("Macros", new String[]{"macros", "mac", "macro"});
    }

    @Override
    public void onRun(String[] args) {
        if (args.length > 1) {
            switch (args[1].toLowerCase()) {
                case "list": {
                    if (Moon.INSTANCE.getMacroManager().getMacros().isEmpty()) {
                        Printer.print("Your macro list is empty.");
                        return;
                    }
                    Printer.print("Your macros are:");
                    Moon.INSTANCE.getMacroManager().getMacros().values().forEach(macro -> Printer.print("Label: " + macro.getLabel() + ", Keybind: " + Keyboard.getKeyName((int)macro.getKey()) + ", Text: " + macro.getText() + "."));
                    break;
                }
                case "reload": {
                    Moon.INSTANCE.getMacroManager().clearMacros();
                    Moon.INSTANCE.getMacroManager().load();
                    Printer.print("Reloaded macros.");
                    break;
                }
                case "remove": 
                case "delete": {
                    if (args.length < 3) {
                        Printer.print("Invalid args.");
                        return;
                    }
                    if (Moon.INSTANCE.getMacroManager().isMacro(args[2])) {
                        Moon.INSTANCE.getMacroManager().removeMacroByLabel(args[2]);
                        Printer.print("Removed a macro named " + args[2] + ".");
                        if (Moon.INSTANCE.getMacroManager().getMacroFile().exists()) {
                            Moon.INSTANCE.getMacroManager().save();
                            break;
                        }
                        try {
                            Moon.INSTANCE.getMacroManager().getMacroFile().createNewFile();
                            Moon.INSTANCE.getMacroManager().save();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    Printer.print(args[2] + " is not a macro.");
                    break;
                }
                case "clear": {
                    if (Moon.INSTANCE.getMacroManager().getMacros().isEmpty()) {
                        Printer.print("Your macro list is empty.");
                        return;
                    }
                    Printer.print("Cleared all macros.");
                    Moon.INSTANCE.getMacroManager().clearMacros();
                    if (Moon.INSTANCE.getMacroManager().getMacroFile().exists()) {
                        Moon.INSTANCE.getMacroManager().save();
                        break;
                    }
                    try {
                        Moon.INSTANCE.getMacroManager().getMacroFile().createNewFile();
                        Moon.INSTANCE.getMacroManager().save();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "add": 
                case "create": {
                    if (args.length < 5) {
                        Printer.print("Invalid args.");
                        return;
                    }
                    int keyCode = Keyboard.getKeyIndex((String)args[3].toUpperCase());
                    if (keyCode != -1 && !Keyboard.getKeyName((int)keyCode).equals("NONE")) {
                        if (Moon.INSTANCE.getMacroManager().getMacroByKey(keyCode) != null) {
                            Printer.print("There is already a macro bound to that key.");
                            return;
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 4; i < args.length; ++i) {
                            stringBuilder.append(args[i]);
                            if (i == args.length - 1) continue;
                            stringBuilder.append(" ");
                        }
                        Moon.INSTANCE.getMacroManager().addMacro(args[2], keyCode, stringBuilder.toString());
                        Printer.print("Bound a macro named " + args[2] + " to the key " + Keyboard.getKeyName((int)keyCode) + ".");
                        if (Moon.INSTANCE.getMacroManager().getMacroFile().exists()) {
                            Moon.INSTANCE.getMacroManager().save();
                            break;
                        }
                        try {
                            Moon.INSTANCE.getMacroManager().getMacroFile().createNewFile();
                            Moon.INSTANCE.getMacroManager().save();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    Printer.print("That is not a valid key code.");
                }
            }
        }
    }
}

