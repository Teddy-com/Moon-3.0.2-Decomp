/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.player;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.value.impl.EnumValue;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class NoFall
extends Module {
    private final EnumValue<Modes> mode = new EnumValue<Modes>("Mode", Modes.EDIT);

    public NoFall() {
        super("NoFall", Module.Category.PLAYER, new Color(15060464).getRGB());
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        switch ((Modes)((Object)this.mode.getValue())) {
            case EDIT: {
                if (!event.isPre() || !((double)this.mc.thePlayer.fallDistance >= 2.5)) break;
                event.setOnGround(true);
                break;
            }
            case WATCHDOG: {
                if (!event.isPre() || !((double)this.mc.thePlayer.fallDistance >= 2.4)) break;
                if (!this.isBlockUnder()) {
                    return;
                }
                this.mc.thePlayer.fallDistance = 0.0f;
                this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C03PacketPlayer(true));
                break;
            }
            case ROUNDING: {
                if (!event.isPre() || !((double)this.mc.thePlayer.fallDistance >= 2.5)) break;
                this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, (double)Math.round(this.mc.thePlayer.posY / 0.015625) * 0.015625, this.mc.thePlayer.posZ);
                event.setOnGround(true);
                break;
            }
            case SPARTAN: {
                if (!event.isPre()) break;
                if ((double)this.mc.thePlayer.fallDistance >= 2.1) {
                    BlockPos pos = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 3.0, this.mc.thePlayer.posZ);
                    if (this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) break;
                    this.mc.thePlayer.motionY = 0.0;
                    this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, (double)Math.round(this.mc.thePlayer.posY / 0.015625) * 0.015625, this.mc.thePlayer.posZ);
                    event.setOnGround(true);
                    break;
                }
                this.mc.timer.timerSpeed = 1.0f;
            }
        }
    }

    private boolean isBlockUnder() {
        for (int i = (int)(this.mc.thePlayer.posY - 1.0); i > 0; --i) {
            BlockPos pos = new BlockPos(this.mc.thePlayer.posX, (double)i, this.mc.thePlayer.posZ);
            if (this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }

    public static enum Modes {
        EDIT("Edit"),
        WATCHDOG("Watchdog"),
        ROUNDING("Rounding"),
        SPARTAN("Spartan");

        private final String name;

        private Modes(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

