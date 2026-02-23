/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.other;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.module.Module;
import me.moon.utils.MathUtils;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Detector
extends Module {
    private final BooleanValue autodisable = new BooleanValue("Autodisable", true);
    private final BooleanValue unblock = new BooleanValue("Unblock", true);
    private final BooleanValue chat = new BooleanValue("Chat", true);
    private final BooleanValue strict = new BooleanValue("Strict", true, (Value)this.chat, "true");
    private final BooleanValue suspect = new BooleanValue("CheatingWarning", true, (Value)this.chat, "true");
    private final BooleanValue autoNoU = new BooleanValue("AutoNoU", true, (Value)this.chat, "true");
    private final List<String> hackMatches = Arrays.asList("hack", "report", "cheat", "aura", "speed", "record", "what client", "ban", "bhop", "bunny hop", "hax");
    private final List<String> noUMatches = Arrays.asList("gay", "fag", "stupid", "retard", "idiot", "skid", "loser", "cheater", "retard", "kys", "neck your self", "delete self", "cunt", "suck", "kill yourself");

    public Detector() {
        super("Detector", Module.Category.OTHER, new Color(10968674).getRGB());
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        if (!event.isSending()) {
            if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                if (this.unblock.getValue().booleanValue()) {
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                }
                if (this.autodisable.getValue().booleanValue() && (Moon.INSTANCE.getModuleManager().getModule("speed").isEnabled() || Moon.INSTANCE.getModuleManager().getModule("flight").isEnabled() || Moon.INSTANCE.getModuleManager().getModule("longjump").isEnabled())) {
                    Moon.INSTANCE.getModuleManager().getModule("speed").setEnabled(false);
                    Moon.INSTANCE.getModuleManager().getModule("flight").setEnabled(false);
                    Moon.INSTANCE.getModuleManager().getModule("longjump").setEnabled(false);
                    Moon.INSTANCE.getNotificationManager().addNotification("Lag back/Teleport! turned off some mods", 2500L);
                }
            } else if (this.chat.getValue().booleanValue() && event.getPacket() instanceof S02PacketChat) {
                S02PacketChat chatPacket = (S02PacketChat)event.getPacket();
                String message = chatPacket.getChatComponent().getUnformattedText();
                if (message.contains("[WATCHDOG CHEAT DETECTION]") || message.contains("[WATCHDOG ANNOUNCEMENT]")) {
                    this.mc.thePlayer.sendChatMessage("Thanks Watchdog!");
                }
                if (message.toLowerCase().contains(this.mc.getSession().getUsername().toLowerCase()) || !this.strict.getValue().booleanValue()) {
                    if (this.autoNoU.isEnabled()) {
                        for (String noUMatch : this.noUMatches) {
                            if (!message.contains(noUMatch) || message.contains(this.mc.thePlayer.getName())) continue;
                            this.mc.thePlayer.sendChatMessage("no u " + MathUtils.getRandomInRange(100000.0, 1.0E8));
                            Moon.INSTANCE.getNotificationManager().addNotification("Detected a Salty Noob, auto no u", 2500L);
                            break;
                        }
                        for (String noUMatch : this.hackMatches) {
                            if (!message.contains(noUMatch) || message.contains(this.mc.thePlayer.getName())) continue;
                            this.mc.thePlayer.sendChatMessage("no u " + MathUtils.getRandomInRange(100000.0, 1.0E8));
                            Moon.INSTANCE.getNotificationManager().addNotification("Detected a Salty Noob, auto no u", 2500L);
                            break;
                        }
                    }
                    if (this.suspect.isEnabled()) {
                        for (String hackMatch : this.hackMatches) {
                            if (!message.contains(hackMatch)) continue;
                            Moon.INSTANCE.getNotificationManager().addNotification("Suspected of cheating! turn it down buddy!", 2500L);
                            break;
                        }
                    }
                }
                if (message.equalsIgnoreCase("ground items will be removed in ")) {
                    Moon.INSTANCE.getNotificationManager().addNotification("Ground items will be gone in " + message.split("be removed in ")[1], 2500L);
                }
            }
        }
    }
}

