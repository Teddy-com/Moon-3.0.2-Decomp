/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.moon.utils.game;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.moon.Moon;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;

public class Printer {
    public static void print(String message) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation(ChatFormatting.AQUA + "Moon 3.0" + ChatFormatting.GRAY + ChatFormatting.GRAY + " \u00bb " + ChatFormatting.WHITE + message, new Object[0]));
        }
    }

    public static void printIRC(String message) {
        if (Minecraft.getMinecraft().thePlayer != null && Moon.INSTANCE.getModuleManager().getModule("IRC").isEnabled()) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation(ChatFormatting.AQUA + "IRC" + ChatFormatting.GRAY + " \u00bb \u00a7f" + message, new Object[0]));
        }
    }
}

