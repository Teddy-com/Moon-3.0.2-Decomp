/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.other;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.game.TickEvent;
import me.moon.module.Module;
import me.moon.utils.game.Printer;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.network.play.server.S02PacketChat;

public class AutoMatchJoin
extends Module {
    private final String[] nigga = new String[]{"1st Killer - ", "1st Place - ", "You died! Want to play again? Click here!", "Winner: ", " - Damage Dealt - ", "1st - ", "Winning Team - ", "Winners: ", "Winner: ", "Winning Team: ", " win the game!", "Top Seeker: ", "1st Place: ", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners - "};
    private final TimerUtil timer = new TimerUtil();
    public EnumValue<Type> type = new EnumValue<Type>("Type", Type.INSANE);
    public BooleanValue autoDetect = new BooleanValue("Auto Detect", true);
    public BooleanValue teams = new BooleanValue("Teams only mode", true);
    private final NumberValue<Long> delay = new NumberValue<Long>("Delay", 3000L, 500L, 10000L, 50L);
    private boolean allowedToSend;

    public AutoMatchJoin() {
        super("AutoMatchJoin", Module.Category.OTHER, new Color(16752555).getRGB());
    }

    @Override
    public void onEnable() {
        this.allowedToSend = false;
    }

    @Handler(value=TickEvent.class)
    public void onTick(TickEvent event) {
        if (this.mc.thePlayer != null && this.allowedToSend && this.timer.hasReached((Long)this.delay.getValue())) {
            switch ((Type)((Object)this.type.getValue())) {
                case NORMAL: {
                    this.mc.thePlayer.sendChatMessage(this.teams.isEnabled() ? "/play teams_normal" : "/play solo_normal");
                    this.allowedToSend = false;
                    break;
                }
                case INSANE: {
                    this.mc.thePlayer.sendChatMessage(this.teams.isEnabled() ? "/play teams_insane" : "/play solo_insane");
                    this.allowedToSend = false;
                }
            }
        }
        if (!this.allowedToSend) {
            this.timer.reset();
        }
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        if (this.mc.theWorld == null) {
            this.allowedToSend = false;
            return;
        }
        if (!event.isSending() && !this.allowedToSend && event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat)event.getPacket();
            for (String str : this.nigga) {
                if (!packet.getChatComponent().getUnformattedText().contains(str) || !GuiIngame.isInSkywars) continue;
                Printer.print("Automatically joining another game.");
                this.allowedToSend = true;
            }
        }
    }

    public static enum Type {
        NORMAL("Normal"),
        INSANE("Insane");

        private final String name;

        private Type(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

