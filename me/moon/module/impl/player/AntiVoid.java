/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.player;

import java.awt.Color;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class AntiVoid
extends Module {
    private NumberValue<Float> fallDistance = new NumberValue<Float>("Distance until Fall", Float.valueOf(5.0f), Float.valueOf(3.0f), Float.valueOf(20.0f), Float.valueOf(0.5f));
    private BooleanValue highJump = new BooleanValue("HighJump", false);
    private float startY = 0.0f;
    private float dist = 0.0f;
    private boolean justJumped = false;

    public AntiVoid() {
        super("AntiVoid", Module.Category.PLAYER, new Color(223, 233, 233).getRGB());
        this.setDescription("Flags you back when falling into the void");
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        boolean blockUnder = false;
        for (int i = (int)Math.ceil(this.mc.thePlayer.posY); i >= 0; --i) {
            if (this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, (double)i, this.mc.thePlayer.posZ)).getBlock() == Blocks.air) continue;
            blockUnder = true;
        }
        if (event.isPre()) {
            if (this.mc.thePlayer.fallDistance > ((Float)this.fallDistance.getValue()).floatValue()) {
                if (this.mc.thePlayer.posY < 0.0) {
                    this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C03PacketPlayer(true));
                    event.setY(event.getY() + (double)this.mc.thePlayer.fallDistance);
                } else if (!this.highJump.isEnabled() && !blockUnder) {
                    event.setY(event.getY() + (double)this.mc.thePlayer.fallDistance);
                }
            }
            if (this.highJump.isEnabled() && (double)(this.startY + this.dist) > this.mc.thePlayer.posY && !blockUnder) {
                this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C03PacketPlayer(true));
                if (this.mc.thePlayer.hurtTime > 0) {
                    this.mc.thePlayer.motionY = (double)((Float)this.fallDistance.getValue()).floatValue() * 0.282;
                    this.justJumped = true;
                }
            }
            if (this.highJump.isEnabled() && !((double)(this.startY + 3.5f + this.dist) > this.mc.thePlayer.posY) && !blockUnder && !Moon.INSTANCE.getModuleManager().getModule("Flight").isEnabled()) {
                this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C03PacketPlayer(true));
            }
            if (this.mc.thePlayer.onGround && blockUnder) {
                this.startY = (float)this.mc.thePlayer.posY;
            }
            this.dist = -((Float)this.fallDistance.getValue()).floatValue();
            if (!this.mc.thePlayer.onGround && this.justJumped) {
                this.justJumped = false;
            }
        }
    }
}

