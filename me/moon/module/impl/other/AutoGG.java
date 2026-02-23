/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.other;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.module.Module;
import me.moon.utils.value.impl.StringValue;
import net.minecraft.network.play.server.S02PacketChat;

public class AutoGG
extends Module {
    private final String[] text = new String[]{"1st Killer - ", "1st Place - ", "Winner: ", " - Damage Dealt - ", "1st - ", "Winning Team - ", "Winners: ", "Winner: ", "Winning Team: ", " win the game!", "Top Seeker: ", "1st Place: ", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners - "};
    private StringValue message = new StringValue("Message", "gg, get good get moon");

    public AutoGG() {
        super("AutoGG", Module.Category.OTHER, new Color(11468766).getRGB());
        this.setDescription("Automatically ggs to be gay");
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        if (!event.isSending() && event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat)event.getPacket();
            for (String str : this.text) {
                if (!packet.getChatComponent().getUnformattedText().contains(str)) continue;
                this.mc.thePlayer.sendChatMessage("/achat gg");
            }
        }
    }
}

