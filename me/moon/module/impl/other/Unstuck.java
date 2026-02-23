/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.other;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.player.MotionEvent;
import me.moon.module.Module;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Unstuck
extends Module {
    private final NumberValue<Long> delay = new NumberValue<Long>("Delay", 500L, 50L, 1000L, 50L);
    private final BooleanValue automated = new BooleanValue("Automated", true);
    private int setbackCount;
    private final TimerUtil timer = new TimerUtil();

    public Unstuck() {
        super("Unstuck", Module.Category.OTHER, new Color(125, 125, 215).getRGB());
    }

    @Handler(value=PacketEvent.class)
    public void packetEvent(PacketEvent e) {
        if (e.isSending()) {
            if (e.getPacket() instanceof C03PacketPlayer && (this.isStuck() || !this.automated.isEnabled())) {
                e.setCancelled(true);
            }
        } else if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            this.setbackCount = !this.timer.hasReached((Long)this.delay.getValue()) ? ++this.setbackCount : 1;
            this.timer.reset();
        }
    }

    @Handler(value=MotionEvent.class)
    public void moveEvent(MotionEvent e) {
        if (this.isStuck() || !this.automated.isEnabled()) {
            this.mc.thePlayer.motionX = 0.0;
            e.setX(0.0);
            this.mc.thePlayer.motionY = 0.0;
            e.setY(0.0);
            this.mc.thePlayer.motionZ = 0.0;
            e.setZ(0.0);
        }
    }

    private boolean isStuck() {
        return this.setbackCount > 5 && !this.timer.hasReached((Long)this.delay.getValue());
    }
}

