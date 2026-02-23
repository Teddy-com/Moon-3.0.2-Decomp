/*
 * Decompiled with CFR 0.152.
 */
package me.moon.command.impl;

import java.io.IOException;
import me.moon.Moon;
import me.moon.command.Command;
import me.moon.config.Config;
import me.moon.gui.ConfigGUI;
import me.moon.utils.game.Printer;
import net.minecraft.client.Minecraft;

public class ConfigCommand
extends Command {
    public ConfigCommand() {
        super("Config", new String[]{"c", "config", "configs"});
    }

    @Override
    public void onRun(String[] s) {
        if (s.length == 1) {
            Printer.print("config create/save configname ((keys) if you want to save with keybinds) - saves a config.");
            Printer.print("config override configname ((keys) if you want to override with keybinds) - overrides existing configs.");
            Printer.print("config delete/remove configname - removes a config.");
            return;
        }
        switch (s[1]) {
            case "menu": {
                Minecraft.getMinecraft().displayGuiScreen(new ConfigGUI());
                Printer.print("Opened config menu!");
                break;
            }
            case "list": {
                if (!Moon.INSTANCE.getConfigManager().getConfigs().isEmpty()) {
                    Printer.print("Current Configs:");
                    Moon.INSTANCE.getConfigManager().getConfigs().forEach(cfg -> Printer.print(cfg.getName()));
                    break;
                }
                Printer.print("You have no saved configs!");
                break;
            }
            case "help": {
                Printer.print("config list - shows all configs.");
                Printer.print("config create/save configname ((keys) if you want to save with keybinds) - saves a config.");
                Printer.print("config override configname ((keys) if you want to override with keybinds) - overrides existing configs.");
                Printer.print("config load configname ((keys) if you want to override with keybinds) - loads a config.");
                Printer.print("config delete/remove configname - removes a config.");
                break;
            }
            case "create": 
            case "save": {
                if (s.length <= 2) break;
                if (!Moon.INSTANCE.getConfigManager().isConfig(s[2])) {
                    Moon.INSTANCE.getConfigManager().saveConfig(s[2], s.length > 3 && s[3].equalsIgnoreCase("keys"));
                    Moon.INSTANCE.getConfigManager().getConfigs().add(new Config(s[2]));
                    Printer.print("Created a config named " + s[2] + (s.length > 3 && s[3].equalsIgnoreCase("keys") ? " with keys included" : "") + "!");
                    Moon.INSTANCE.getNotificationManager().addNotification("Created a config named " + s[2] + (s.length > 3 && s[3].equalsIgnoreCase("keys") ? " with keys included" : "") + "!", 2000L);
                    break;
                }
                Printer.print(s[2] + " is already a saved config!");
                Moon.INSTANCE.getNotificationManager().addNotification(s[2] + " is already a saved config!", 2000L);
                break;
            }
            case "delete": 
            case "remove": {
                if (s.length <= 2) break;
                if (Moon.INSTANCE.getConfigManager().isConfig(s[2])) {
                    Moon.INSTANCE.getConfigManager().deleteConfig(s[2]);
                    Printer.print("Deleted the config named " + s[2] + "!");
                    Moon.INSTANCE.getNotificationManager().addNotification("Deleted the config named " + s[2] + "!", 2000L);
                    break;
                }
                Printer.print(s[2] + " is not a saved config!");
                Moon.INSTANCE.getNotificationManager().addNotification(s[2] + " is not a saved config!", 2000L);
                break;
            }
            case "reload": {
                Moon.INSTANCE.getConfigManager().getConfigs().clear();
                Moon.INSTANCE.getConfigManager().load();
                Printer.print("Reloaded all saved configs. Current number of configs: " + Moon.INSTANCE.getConfigManager().getConfigs().size() + "!");
                Moon.INSTANCE.getNotificationManager().addNotification("Reloaded all saved configs. Current number of configs: " + Moon.INSTANCE.getConfigManager().getConfigs().size() + "!", 2000L);
                break;
            }
            case "clear": {
                try {
                    if (!Moon.INSTANCE.getConfigManager().getConfigs().isEmpty()) {
                        Moon.INSTANCE.getConfigManager().clear();
                        Moon.INSTANCE.getConfigManager().getConfigs().clear();
                        Printer.print("Cleared all saved configs!");
                        Moon.INSTANCE.getNotificationManager().addNotification("Cleared all saved configs!", 2000L);
                        break;
                    }
                    Printer.print("You have no saved configs!");
                    Moon.INSTANCE.getNotificationManager().addNotification("You have no saved configs!", 2000L);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "override": {
                if (s.length <= 2 || s[2] == null) break;
                if (Moon.INSTANCE.getConfigManager().isConfig(s[2])) {
                    Moon.INSTANCE.getConfigManager().saveConfig(s[2], s.length > 3 && s[3].equalsIgnoreCase("keys"));
                    Printer.print("Overrode the config named " + s[2] + (s.length > 3 && s[3].equalsIgnoreCase("keys") ? " with keys included" : "") + "!");
                    Moon.INSTANCE.getNotificationManager().addNotification("Overrode the config named " + s[2] + (s.length > 3 && s[3].equalsIgnoreCase("keys") ? " with keys included" : "") + "!", 2000L);
                    break;
                }
                Printer.print(s[2] + " is not a saved config!");
                Moon.INSTANCE.getNotificationManager().addNotification(s[2] + " is not a saved config!", 2000L);
                break;
            }
            case "load": {
                if (s.length <= 2) break;
                if (Moon.INSTANCE.getConfigManager().isConfig(s[2])) {
                    Moon.INSTANCE.getConfigManager().loadConfig(s[2], s.length > 3 && s[3].equalsIgnoreCase("keys"));
                    Printer.print("Loaded the config named " + s[2] + (s.length > 3 && s[3].equalsIgnoreCase("keys") ? " with keys included" : "") + "!");
                    Moon.INSTANCE.getNotificationManager().addNotification("Loaded the config named " + s[2] + (s.length > 3 && s[3].equalsIgnoreCase("keys") ? " with keys included" : "") + "!", 2000L);
                    break;
                }
                Printer.print(s[2] + " is not a saved config!");
                Moon.INSTANCE.getNotificationManager().addNotification(s[2] + " is not a saved config!", 2000L);
            }
        }
    }
}

