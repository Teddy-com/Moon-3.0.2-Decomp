/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3d
 */
package me.moon.module.impl.other;

import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Vector3d;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.input.ClickMouseEvent;
import me.moon.event.impl.player.MotionEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.event.impl.render.ReachRenderEvent;
import me.moon.module.Module;
import me.moon.utils.game.MoveUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Teleport
extends Module {
    private boolean teleporting;
    private BlockPos teleportPosition;
    private boolean hasJumped;
    private boolean teleportSoon;
    private int y = 0;

    public Teleport() {
        super("Teleport", Module.Category.OTHER, -1);
    }

    @Handler(value=ReachRenderEvent.class)
    public void onReachRender(ReachRenderEvent event) {
        event.reach = 10000.0;
    }

    @Handler(value=ClickMouseEvent.class)
    public void onClickMouseEvent(ClickMouseEvent event) {
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.getBlockPos() != null && this.teleportPosition == null && this.mc.thePlayer.isSneaking()) {
            this.teleportPosition = this.mc.objectMouseOver.getBlockPos();
            this.teleporting = true;
            this.teleportSoon = false;
            this.y = (int)this.mc.thePlayer.posY;
            Moon.INSTANCE.getNotificationManager().addNotification("Teleporting to " + this.teleportPosition.getX() + " " + this.teleportPosition.getY() + " " + this.teleportPosition.getZ() + " in 5 seconds!", 5000L);
        }
    }

    @Override
    public void onEnable() {
        this.teleporting = false;
        this.teleportPosition = null;
        this.hasJumped = false;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.teleporting = false;
        this.teleportPosition = null;
        this.hasJumped = false;
        this.teleportSoon = false;
        super.onDisable();
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook && this.teleportPosition != null && Math.round(((S08PacketPlayerPosLook)event.getPacket()).y) == (long)this.y) {
            this.teleportSoon = true;
        }
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (event.isPre() && this.teleporting) {
            if (!this.hasJumped) {
                this.mc.thePlayer.jump();
                this.hasJumped = true;
            }
            if (this.mc.thePlayer.motionY < 0.0) {
                this.mc.thePlayer.motionY = 0.0;
            }
            if (this.teleportSoon) {
                event.setCancelled(true);
                this.teleport(this.teleportPosition);
            }
        }
    }

    @Handler(value=MotionEvent.class)
    public void onMotion(MotionEvent event) {
        if (this.teleporting) {
            MoveUtil.setMoveSpeed(event, 0.0);
        }
    }

    private void teleport(BlockPos blockPos) {
        if ((double)blockPos.getY() > this.mc.thePlayer.posY - 1.0) {
            for (Vector3d pathPoint : this.findPath(this.mc.thePlayer.posX, (float)blockPos.getY() + 1.42f, this.mc.thePlayer.posZ, 8.0)) {
                this.mc.thePlayer.setPositionAndUpdate(this.mc.thePlayer.posX, pathPoint.y, this.mc.thePlayer.posZ);
                this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, pathPoint.y, this.mc.thePlayer.posZ, false));
            }
            for (Vector3d pathPoint : this.findPath(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 8.0)) {
                float x = Math.round(pathPoint.x);
                float z = Math.round(pathPoint.z);
                this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(x, (float)blockPos.getY() + 1.42f, z, false));
                this.mc.thePlayer.setPositionAndUpdate(x, (float)blockPos.getY() + 1.42f, z);
            }
        } else if ((double)blockPos.getY() < this.mc.thePlayer.posY - 1.0) {
            for (Vector3d pathPoint : this.findPath(blockPos.getX(), this.mc.thePlayer.posY, blockPos.getZ(), 8.0)) {
                float x = Math.round(pathPoint.x);
                float z = Math.round(pathPoint.z);
                this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(x, this.mc.thePlayer.posY, z, false));
                this.mc.thePlayer.setPositionAndUpdate(x, this.mc.thePlayer.posY, z);
            }
            for (Vector3d pathPoint : this.findPath(this.mc.thePlayer.posX, (float)blockPos.getY() + 1.42f, this.mc.thePlayer.posZ, 8.0)) {
                this.mc.thePlayer.setPositionAndUpdate(this.mc.thePlayer.posX, pathPoint.y, this.mc.thePlayer.posZ);
                this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, pathPoint.y, this.mc.thePlayer.posZ, false));
            }
        } else {
            for (Vector3d pathPoint : this.findPath(blockPos.getX(), (float)blockPos.getY() + 1.42f, blockPos.getZ(), 8.0)) {
                float x = Math.round(pathPoint.x);
                float z = Math.round(pathPoint.z);
                this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(x, pathPoint.y, z, false));
                this.mc.thePlayer.setPositionAndUpdate(x, pathPoint.y, z);
            }
        }
        Moon.INSTANCE.getNotificationManager().addNotification("Teleported to " + this.teleportPosition.getX() + " " + this.teleportPosition.getY() + " " + this.teleportPosition.getZ(), 1000L);
        this.hasJumped = false;
        this.teleporting = false;
        this.teleportPosition = null;
        this.teleportSoon = false;
    }

    private List<Vector3d> findPath(double tpX, double tpY, double tpZ, double offset) {
        ArrayList<Vector3d> positions = new ArrayList<Vector3d>();
        float yaw = (float)(Math.atan2(tpZ - this.mc.thePlayer.posZ, tpX - this.mc.thePlayer.posX) * 180.0 / Math.PI - 90.0);
        double steps = this.getDistance(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, tpX, tpY, tpZ) / offset;
        double curY = this.mc.thePlayer.posY;
        for (double d = offset; d < this.getDistance(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, tpX, tpY, tpZ); d += offset) {
            positions.add(new Vector3d(this.mc.thePlayer.posX - Math.sin(Math.toRadians(yaw)) * d, curY -= (this.mc.thePlayer.posY - tpY) / steps, this.mc.thePlayer.posZ + Math.cos(Math.toRadians(yaw)) * d));
        }
        positions.add(new Vector3d(tpX, tpY, tpZ));
        return positions;
    }

    private double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double xDiff = x1 - x2;
        double yDiff = y1 - y2;
        double zDiff = z1 - z2;
        return MathHelper.sqrt_double(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }
}

