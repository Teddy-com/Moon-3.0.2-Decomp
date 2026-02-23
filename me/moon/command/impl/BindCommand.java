/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.moon.command.impl;

import java.util.Objects;
import me.moon.Moon;
import me.moon.command.Command;
import me.moon.module.Module;
import me.moon.utils.game.Printer;
import org.lwjgl.input.Keyboard;

public class BindCommand
extends Command {
    public BindCommand() {
        super("Bind", new String[]{"bind", "b"});
    }

    @Override
    public void onRun(String[] args) {
        if (args.length == 2 && args[1].toLowerCase().equals("resetall")) {
            Moon.INSTANCE.getModuleManager().getModuleMap().values().forEach(m -> m.setKeyBind(0));
            Moon.INSTANCE.getNotificationManager().addNotification("Reset all keybinds.", 2000L);
            return;
        }
        if (args.length == 3) {
            String moduleName = args[1];
            Module module = Moon.INSTANCE.getModuleManager().getModule(moduleName);
            if (module != null) {
                int keyCode = Keyboard.getKeyIndex((String)args[2].toUpperCase());
                if (keyCode != -1) {
                    module.setKeyBind(keyCode);
                    Printer.print(module.getLabel() + " is now bound to \"" + Keyboard.getKeyName((int)keyCode) + "\".");
                    Moon.INSTANCE.getNotificationManager().addNotification((Objects.nonNull(module.getRenderLabel()) ? module.getRenderLabel() : module.getLabel()) + " is now bound to \"" + Keyboard.getKeyName((int)keyCode) + "\".", 2000L);
                } else {
                    Printer.print("That is not a valid key code.");
                }
            } else {
                Moon.INSTANCE.getNotificationManager().addNotification("That module does not exist.", 2000L);
                Printer.print("That module does not exist.");
                Printer.print("Type \"modules\" for a list of all modules.");
            }
        } else {
            Printer.print("Invalid arguments.");
            Printer.print("Usage: \"bind [module] [key]\"");
        }
    }
}

