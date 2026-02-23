/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.other;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.module.Module;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate
extends Module {
    public NoRotate() {
        super("NoRotate", Module.Category.OTHER, new Color(10327960).getRGB());
        this.setDescription("Cancel ncp rotation flags.");
    }

    @Handler(value=PacketEvent.class)
    public void handle(PacketEvent event) {
        if (!event.isSending() && event.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.getPacket();
            if (this.mc.thePlayer != null && this.mc.theWorld != null && this.mc.thePlayer.rotationYaw != -180.0f && this.mc.thePlayer.rotationPitch != 0.0f) {
                packet.yaw = packet.field_179835_f.contains((Object)S08PacketPlayerPosLook.EnumFlags.X_ROT) ? 0.0f : this.mc.thePlayer.rotationYaw;
                packet.pitch = packet.field_179835_f.contains((Object)S08PacketPlayerPosLook.EnumFlags.Y_ROT) ? 0.0f : this.mc.thePlayer.rotationPitch;
            }
        }
    }
}

