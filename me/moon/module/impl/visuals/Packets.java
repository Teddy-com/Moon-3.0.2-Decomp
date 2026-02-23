/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals;

import java.util.ArrayList;
import java.util.List;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.Module;
import net.minecraft.client.Minecraft;

public class Packets
extends Module {
    private final List<Long> playOut;
    private final List<Long> playIn;
    public int cpsCounterPosX;
    public int cpsCounterPosY;

    public Packets() {
        super("Packets Counter", Module.Category.VISUALS, 0);
        this.setHidden(true);
        this.playOut = new ArrayList<Long>();
        this.playIn = new ArrayList<Long>();
    }

    private int getOutPackets() {
        long time = System.currentTimeMillis();
        this.playOut.removeIf(o -> o + 1000L < time);
        return this.playOut.size();
    }

    private int getInPackets() {
        long time = System.currentTimeMillis();
        this.playIn.removeIf(o -> o + 1000L < time);
        return this.playIn.size();
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        if (event.isSending() && !event.isCancelled()) {
            this.playIn.add(System.currentTimeMillis());
        } else {
            this.playOut.add(System.currentTimeMillis());
        }
    }

    @Handler(value=Render2DEvent.class)
    public void onRender2D(Render2DEvent event) {
        this.cpsCounterPosX = 3;
        this.cpsCounterPosY = 50;
        int out = this.getOutPackets();
        int in = this.getInPackets();
        String textIn = in + " Packets/s";
        String textOut = out + " Packets/s";
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(textIn, this.cpsCounterPosX, this.cpsCounterPosY, 0xFFFFFF);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(textOut, this.cpsCounterPosX, this.cpsCounterPosY + 10, 0xFFFFFF);
    }
}

