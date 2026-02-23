/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.game;

import net.minecraft.client.Minecraft;

public class ServerUtils {
    public static boolean isOnHypixel() {
        String server;
        Minecraft mc = Minecraft.getMinecraft();
        return mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP != null && ((server = mc.getCurrentServerData().serverIP).endsWith(".hypixel.net") || server.endsWith(".hypixel.net:25565") || server.equals("hypixel.net") || server.equals("Hypixel.net") || server.equals("hypixel.net:25565") || server.equalsIgnoreCase("hypixel.net"));
    }
}

