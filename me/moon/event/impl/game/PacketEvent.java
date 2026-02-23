/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.impl.game;

import me.moon.event.Event;
import net.minecraft.network.Packet;

public class PacketEvent
extends Event {
    private boolean sending;
    private Packet packet;

    public PacketEvent(Packet packet, boolean sending) {
        this.packet = packet;
        this.sending = sending;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public boolean isSending() {
        return this.sending;
    }
}

