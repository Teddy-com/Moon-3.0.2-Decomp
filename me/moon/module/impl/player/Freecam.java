/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.player;

import java.awt.Color;
import java.util.Objects;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.player.BoundingBoxEvent;
import me.moon.event.impl.player.MotionEvent;
import me.moon.event.impl.player.PushEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.game.MoveUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovementInput;

public class Freecam
extends Module {
    private double x;
    private double y;
    private double z;
    private double yaw;
    private double pitch;

    public Freecam() {
        super("Freecam", Module.Category.PLAYER, new Color(0, 160, 255).getRGB());
        this.setDescription("Spawn nigga and be a creep");
    }

    @Override
    public void onEnable() {
        if (Objects.nonNull(this.mc.theWorld)) {
            this.x = this.mc.thePlayer.posX;
            this.y = this.mc.thePlayer.posY;
            this.z = this.mc.thePlayer.posZ;
            this.yaw = this.mc.thePlayer.rotationYaw;
            this.pitch = this.mc.thePlayer.rotationPitch;
            EntityOtherPlayerMP entityOtherPlayerMP = new EntityOtherPlayerMP(this.mc.theWorld, this.mc.thePlayer.getGameProfile());
            entityOtherPlayerMP.inventory = this.mc.thePlayer.inventory;
            entityOtherPlayerMP.inventoryContainer = this.mc.thePlayer.inventoryContainer;
            entityOtherPlayerMP.setPositionAndRotation(this.x, this.mc.thePlayer.getEntityBoundingBox().minY, this.z, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch);
            entityOtherPlayerMP.rotationYawHead = this.mc.thePlayer.rotationYawHead;
            entityOtherPlayerMP.setSneaking(this.mc.thePlayer.isSneaking());
            this.mc.theWorld.addEntityToWorld(-6969, entityOtherPlayerMP);
        }
    }

    @Override
    public void onDisable() {
        if (Objects.nonNull(this.mc.theWorld)) {
            this.mc.thePlayer.jumpMovementFactor = 0.02f;
            this.mc.thePlayer.setPosition(this.x, this.y, this.z);
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.01, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
            this.mc.thePlayer.noClip = false;
            this.mc.theWorld.removeEntityFromWorld(-6969);
            this.mc.thePlayer.motionY = 0.0;
            this.mc.thePlayer.rotationPitch = (float)this.pitch;
            this.mc.thePlayer.rotationYaw = (float)this.yaw;
            this.pitch = 0.0;
            this.yaw = 0.0;
        }
        this.mc.renderGlobal.loadRenderers();
        super.onDisable();
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        this.mc.thePlayer.setVelocity(0.0, 0.0, 0.0);
        this.mc.thePlayer.jumpMovementFactor = 1.0f;
        if (this.mc.currentScreen == null) {
            if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindJump)) {
                this.mc.thePlayer.motionY += 1.0;
            }
            if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindSneak)) {
                this.mc.thePlayer.motionY -= 1.0;
            }
        }
        this.mc.thePlayer.noClip = true;
        this.mc.thePlayer.renderArmPitch = 5000.0f;
    }

    @Handler(value=MotionEvent.class)
    public void onMotion(MotionEvent event) {
        MoveUtil.setMoveSpeed(event, 1.0);
        if (!GameSettings.isKeyDown(this.mc.gameSettings.keyBindSneak) && !GameSettings.isKeyDown(this.mc.gameSettings.keyBindJump)) {
            event.setY(2.0 * (double)(-(this.mc.thePlayer.rotationPitch / 180.0f)) * (double)((int)MovementInput.moveForward));
        }
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        if (event.isSending()) {
            event.setCancelled(true);
        }
    }

    @Handler(value=BoundingBoxEvent.class)
    public void onBB(BoundingBoxEvent event) {
        event.setBoundingBox(null);
    }

    @Handler(value=PushEvent.class)
    public void onPush(PushEvent event) {
        event.setCancelled(true);
    }
}

