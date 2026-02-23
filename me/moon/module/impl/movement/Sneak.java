/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.value.impl.EnumValue;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class Sneak
extends Module {
    private final EnumValue<mode> modes = new EnumValue<mode>("Mode", mode.VANILLA);
    private boolean sneak = false;

    public Sneak() {
        super("Sneak", Module.Category.MOVEMENT, new Color(0, 255, 0).getRGB());
    }

    @Override
    public void onDisable() {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        this.sneak = false;
    }

    @Override
    public void onEnable() {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        if (this.modes.getValue() == mode.VANILLA) {
            this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
        }
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        if (event.isSending() && event.getPacket() instanceof C0BPacketEntityAction) {
            C0BPacketEntityAction packet = (C0BPacketEntityAction)event.getPacket();
            if (packet.getAction().equals((Object)C0BPacketEntityAction.Action.START_SNEAKING) && this.modes.getValue() == mode.VANILLA && !this.sneak) {
                this.sneak = true;
            } else if (packet.getAction().equals((Object)C0BPacketEntityAction.Action.START_SNEAKING) || packet.getAction().equals((Object)C0BPacketEntityAction.Action.STOP_SNEAKING)) {
                event.setCancelled(true);
            }
        }
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        this.setSuffix(((mode)((Object)this.modes.getValue())).getName());
        switch ((mode)((Object)this.modes.getValue())) {
            case NCP: {
                if (event.isPre()) {
                    this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                    break;
                }
                this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                break;
            }
        }
    }

    public static enum mode {
        VANILLA("Vanilla"),
        NCP("NCP"),
        DEV("DEV");

        private final String name;

        private mode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

