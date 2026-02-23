/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement.speed.modes;

import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.player.MotionEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.movement.Speed;
import me.moon.module.impl.movement.speed.SpeedMode;
import me.moon.utils.game.MoveUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class SpeedFaithful
extends SpeedMode {
    public SpeedFaithful() {
        super("Faithful", Speed.Mode.FAITHFUL);
    }

    @Override
    public void onPacket(PacketEvent event) {
        if (!event.isSending() && event.getPacket() instanceof S08PacketPlayerPosLook) {
            this.stage = 1;
        }
        if (event.isSending() && event.getPacket() instanceof C03PacketPlayer) {
            if (this.stage == 0 && (this.mc.thePlayer.isMoving() || Math.hypot(this.mc.thePlayer.motionX, this.mc.thePlayer.motionZ) > 0.0 || this.mc.thePlayer.getHealth() < this.mc.thePlayer.getMaxHealth())) {
                double xSpeed = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
                double zSpeed = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
                double precision = this.mc.thePlayer.isUsingItem() || this.mc.thePlayer.isBlocking() || this.mc.thePlayer.isSneaking() ? 0.035 : 0.125;
                for (int i = 0; i < 200; ++i) {
                    double addition = precision * (double)i;
                    double posX = xSpeed > 0.0 ? this.mc.thePlayer.prevPosX + addition : this.mc.thePlayer.prevPosX - addition;
                    posX = xSpeed > 0.0 ? Math.min(posX, this.mc.thePlayer.posX) : Math.max(posX, this.mc.thePlayer.posX);
                    double posZ = zSpeed > 0.0 ? this.mc.thePlayer.prevPosZ + addition : this.mc.thePlayer.prevPosZ - addition;
                    posZ = zSpeed > 0.0 ? Math.min(posZ, this.mc.thePlayer.posZ) : Math.max(posZ, this.mc.thePlayer.posZ);
                    this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(posX, this.mc.thePlayer.posY, posZ, true));
                    if (posX == this.mc.thePlayer.posX && posZ == this.mc.thePlayer.posZ && (i > 20 || this.mc.thePlayer.getHealth() >= this.mc.thePlayer.getMaxHealth())) break;
                }
            }
            C03PacketPlayer packet = (C03PacketPlayer)event.getPacket();
            packet.setOnGround(true);
            packet.setMoving(true);
        }
        super.onPacket(event);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            this.stage = Math.max(0, this.stage - 1);
        }
        super.onUpdate(event);
    }

    @Override
    public void onMotion(MotionEvent event) {
        MoveUtil.setMoveSpeed(event, 1.0);
        super.onMotion(event);
    }
}

