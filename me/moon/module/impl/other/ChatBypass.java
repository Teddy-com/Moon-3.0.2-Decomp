/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.other;

import java.awt.Color;
import java.util.ArrayList;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.module.Module;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class ChatBypass
extends Module {
    public ChatBypass() {
        super("ChatBypass", Module.Category.OTHER, new Color(255, 255, 0, 255).getRGB());
        this.setDescription("Bypass chat restrictions on servers");
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        if (event.isSending() && event.getPacket() instanceof C01PacketChatMessage) {
            C01PacketChatMessage p = (C01PacketChatMessage)event.getPacket();
            StringBuilder finalmsg = new StringBuilder();
            ArrayList<String> splitshit = new ArrayList<String>();
            String[] msg = p.getMessage().split(" ");
            for (String value : msg) {
                char[] characters;
                for (char character : characters = value.toCharArray()) {
                    splitshit.add(character + "\u061d");
                }
                splitshit.add(" ");
            }
            for (String s : splitshit) {
                finalmsg.append(s);
            }
            if (!p.getMessage().startsWith("/")) {
                p.setMessage(finalmsg.toString());
                splitshit.clear();
            }
        }
    }
}

