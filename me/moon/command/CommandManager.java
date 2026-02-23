/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.FileUtils
 *  org.apache.commons.io.IOCase
 *  org.apache.commons.io.filefilter.IOFileFilter
 *  org.apache.commons.io.filefilter.WildcardFileFilter
 *  org.lwjgl.input.Keyboard
 */
package me.moon.command;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import me.moon.Moon;
import me.moon.command.Command;
import me.moon.module.Module;
import me.moon.utils.MathUtils;
import me.moon.utils.font.MCFontRenderer;
import me.moon.utils.game.Printer;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.FontValue;
import me.moon.utils.value.impl.StringValue;
import net.minecraft.client.gui.Gui;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.lwjgl.input.Keyboard;

public class CommandManager {
    public static Map<String, Command> map = new HashMap<String, Command>();

    public void initialize() {
    }

    public void register(Class<? extends Command> commandClass) {
        try {
            Command createdCommand = commandClass.newInstance();
            map.put(createdCommand.getLabel().toLowerCase(), createdCommand);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dispatch(String s) {
        Module m;
        String[] command = s.split(" ");
        if (command.length > 1 && Gui.protectionIsPassed && (m = Moon.INSTANCE.getModuleManager().getModule(command[0])) != null) {
            if (command[1].equalsIgnoreCase("help")) {
                if (!m.getValues().isEmpty()) {
                    Printer.print(m.getLabel() + "'s available properties are:");
                    for (Value value : m.getValues()) {
                        if (value.getParentValueObject() != null && !value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue())) continue;
                        if (value instanceof FontValue) {
                            FontValue fontValue = (FontValue)value;
                            Printer.print(fontValue.getLabel() + ": " + ((MCFontRenderer)fontValue.getValue()).getFont().getName());
                        } else {
                            Printer.print(value.getLabel() + ": " + value.getValue());
                        }
                        if (!(value instanceof EnumValue)) continue;
                        Printer.print(value.getLabel() + "'s available modes are:");
                        StringBuilder modes2 = new StringBuilder();
                        for (Enum vals : ((EnumValue)value).getConstants()) {
                            modes2.append(vals).append(" ");
                        }
                        Printer.print(modes2.toString());
                    }
                } else {
                    Printer.print("This cheat has no properties.");
                }
                Printer.print(m.getLabel() + " is a " + (m.isHidden() ? "hidden " : "shown ") + "module.");
                Printer.print(m.getLabel() + " is bound to " + Keyboard.getKeyName((int)m.getKeyBind()) + ".");
                return;
            }
            if (command.length > 2) {
                if (command[1].equalsIgnoreCase("hidden")) {
                    m.setHidden(Boolean.parseBoolean(command[2].toLowerCase()));
                    Printer.print("Set " + m.getLabel() + " hidden status to " + m.isHidden() + ".");
                    return;
                }
                for (Value value : m.getValues()) {
                    if (value.getParentValueObject() != null && !value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()) || !value.getLabel().replace(" ", "").toLowerCase().equals(command[1].toLowerCase())) continue;
                    if (value instanceof StringValue) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 2; i < command.length; ++i) {
                            stringBuilder.append(command[i]);
                            if (i == command.length - 1) continue;
                            stringBuilder.append(" ");
                        }
                        value.setValue(stringBuilder.toString());
                        Printer.print("Set " + command[0] + " " + value.getLabel() + " to " + value.getValue() + ".");
                        continue;
                    }
                    if (value instanceof FontValue) {
                        if (command.length >= 7) {
                            ArrayList<String> fonts = new ArrayList<String>();
                            for (String font : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
                                fonts.add(font.toLowerCase());
                            }
                            File fontDirectory = new File(System.getenv("LOCALAPPDATA") + "\\Microsoft\\Windows\\Fonts");
                            for (File file : FileUtils.listFiles((File)fontDirectory, (IOFileFilter)new WildcardFileFilter("*.ttf", IOCase.INSENSITIVE), null)) {
                                try {
                                    Font font = Font.createFont(0, file);
                                    if (fonts.contains(font.getName().toLowerCase())) continue;
                                    GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
                                    fonts.add(font.getName().toLowerCase());
                                    Printer.print("Detected and added font " + font.getName());
                                }
                                catch (FontFormatException | IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (fonts.contains(command[2].replace("_", " ").toLowerCase())) {
                                try {
                                    FontValue fontValue = (FontValue)value;
                                    int style = (int)MathUtils.clamp(Integer.parseInt(command[3]), 2.0f, 0.0f);
                                    int size = Integer.parseInt(command[4]);
                                    boolean aa = Boolean.parseBoolean(command[5]);
                                    boolean fractionalmetrics = Boolean.parseBoolean(command[6]);
                                    MCFontRenderer mcFontRenderer = new MCFontRenderer(new Font(command[2].replace("_", " "), style, size), aa, fractionalmetrics);
                                    fontValue.setValue(mcFontRenderer);
                                    Printer.print("Set " + command[0] + " " + value.getLabel() + " to " + ((MCFontRenderer)fontValue.getValue()).getFont().getName() + ", style " + ((MCFontRenderer)fontValue.getValue()).getFont().getStyle() + ", size " + ((MCFontRenderer)fontValue.getValue()).getFont().getSize() + ", antialiasing " + aa + ", fractionalmetrics " + fractionalmetrics + ".");
                                }
                                catch (Exception e) {
                                    Printer.print("Invalid Font 1!");
                                }
                                continue;
                            }
                            Printer.print("Invalid Font 2!");
                            continue;
                        }
                        Printer.print("Arguments are (Name, Style (0 for plain, 1 for bold, 2 for italics), size, AntiAliasing (true or false), FractionalMetrics (true or false))");
                        continue;
                    }
                    value.setValue(command[2]);
                    Printer.print("Set " + command[0] + " " + value.getLabel() + " to " + value.getValue() + ".");
                }
            }
        }
        Moon.INSTANCE.getCommandManager().getCommandMap().values().forEach(c -> {
            for (String handle : c.getHandles()) {
                if (!handle.toLowerCase().equals(command[0])) continue;
                c.onRun(command);
            }
        });
    }

    public Map<String, Command> getCommandMap() {
        return map;
    }
}

