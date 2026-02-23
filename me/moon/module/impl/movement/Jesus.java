/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package me.moon.module.impl.movement;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.player.BoundingBoxEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.MathUtils;
import me.moon.utils.game.MoveUtil;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.value.impl.EnumValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.StringUtils;

public class Jesus
extends Module {
    private final EnumValue<jesusMode> modes = new EnumValue<jesusMode>("Mode", jesusMode.VANILLA);
    private boolean wasWater;
    private final TimerUtil timer = new TimerUtil();

    public Jesus() {
        super("Jesus", Module.Category.MOVEMENT, new Color(142, 248, 255).getRGB());
        this.setDescription("Walking on water like like jesus.");
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (this.mc.thePlayer != null) {
            this.setSuffix(StringUtils.capitalize((String)((jesusMode)((Object)this.modes.getValue())).getName()));
            switch ((jesusMode)((Object)this.modes.getValue())) {
                case DEV: {
                    if (!event.isPre() || !this.isInLiquid(0.55) || this.mc.thePlayer.isSneaking()) break;
                    double speed = 0.2 - MathUtils.getRandomInRange(1.0E-5, 9.0E-4);
                    MoveUtil.setSpeed(speed);
                    event.setOnGround(false);
                    if (!(this.mc.thePlayer.motionY <= 0.0)) break;
                    this.mc.thePlayer.motionY = 0.0;
                    this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0E-12, this.mc.thePlayer.posZ);
                    break;
                }
                case VANILLA: {
                    if (!event.isPre()) break;
                    if (this.isOnLiquid()) {
                        event.setOnGround(false);
                    }
                    if (!event.isPre() || this.mc.thePlayer.isBurning() && this.isOnWater()) {
                        return;
                    }
                    if (!this.isInLiquid() || this.mc.gameSettings.keyBindSneak.isKeyDown() || this.mc.gameSettings.keyBindJump.isKeyDown() || !(this.mc.thePlayer.fallDistance < 3.0f)) break;
                    this.mc.thePlayer.motionY = 0.1;
                    break;
                }
                case NCP: {
                    if (!event.isPre() || this.mc.thePlayer.isBurning() && this.isOnWater()) {
                        return;
                    }
                    if (!this.isInLiquid() || this.mc.gameSettings.keyBindSneak.isKeyDown() || this.mc.gameSettings.keyBindJump.isKeyDown() || !(this.mc.thePlayer.fallDistance < 3.0f)) break;
                    this.mc.thePlayer.motionY = 0.1;
                    break;
                }
                case DOLPHIN: {
                    if (!event.isPre()) break;
                    if (this.mc.thePlayer.isSneaking()) {
                        return;
                    }
                    if (this.mc.thePlayer.onGround || this.mc.thePlayer.isOnLadder()) {
                        this.wasWater = false;
                    }
                    if (this.wasWater && this.mc.thePlayer.motionY > 0.0) {
                        if (this.mc.thePlayer.motionY < 0.03) {
                            this.mc.thePlayer.motionY *= 1.2;
                            this.mc.thePlayer.motionY += 0.067;
                        } else if (this.mc.thePlayer.motionY < 0.05) {
                            this.mc.thePlayer.motionY *= 1.2;
                            this.mc.thePlayer.motionY += 0.06;
                        } else if (this.mc.thePlayer.motionY < 0.07) {
                            this.mc.thePlayer.motionY *= 1.2;
                            this.mc.thePlayer.motionY += 0.057;
                        } else if (this.mc.thePlayer.motionY < 0.11) {
                            this.mc.thePlayer.motionY *= 1.2;
                            this.mc.thePlayer.motionY += 0.0534;
                        } else {
                            this.mc.thePlayer.motionY += 0.0517;
                        }
                    }
                    if (this.wasWater && this.mc.thePlayer.motionY < 0.0 && this.mc.thePlayer.motionY > -0.3) {
                        this.mc.thePlayer.motionY += 0.04;
                    }
                    if (!this.isOnLiquid()) {
                        return;
                    }
                    if ((double)MathHelper.ceiling_double_int(this.mc.thePlayer.posY) != this.mc.thePlayer.posY + 1.0E-6) {
                        return;
                    }
                    this.mc.thePlayer.motionY = 0.5;
                    this.setSpeed(this.getSpeed() * 1.35);
                    this.wasWater = true;
                }
            }
        }
    }

    @Handler(value=BoundingBoxEvent.class)
    public void onBoundingBox(BoundingBoxEvent event) {
        if (this.mc.thePlayer != null) {
            Block block = this.mc.theWorld.getBlockState(event.getBlockPos()).getBlock();
            switch ((jesusMode)((Object)this.modes.getValue())) {
                case DEV: {
                    if (this.mc.theWorld == null || this.mc.thePlayer.fallDistance > 3.0f || this.mc.thePlayer.isBurning() && this.isOnWater()) {
                        return;
                    }
                    if (block instanceof BlockLiquid && !this.mc.thePlayer.isSneaking()) break;
                    return;
                }
                case VANILLA: {
                    if (this.mc.theWorld == null || this.mc.thePlayer.fallDistance > 3.0f || this.mc.thePlayer.isBurning() && this.isOnWater()) {
                        return;
                    }
                    if (!(block instanceof BlockLiquid) || this.isInLiquid() || this.mc.thePlayer.isSneaking()) {
                        return;
                    }
                    event.setBoundingBox(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).contract(0.0, 0.0, 0.0).offset(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ()));
                    break;
                }
                case DOLPHIN: {
                    if (!(event.getBlock() instanceof BlockLiquid) || this.isInLiquid() || this.mc.thePlayer.isSneaking() || this.mc.thePlayer.isBurning() && this.isOnWater()) {
                        return;
                    }
                    event.setBoundingBox(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).contract(0.0, 1.0E-6, 0.0).offset(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ()));
                    break;
                }
                case NCP: {
                    if (this.mc.theWorld == null || this.mc.thePlayer.fallDistance > 3.0f || this.mc.thePlayer.isBurning() && this.isOnWater()) {
                        return;
                    }
                    if (!(block instanceof BlockLiquid) || this.isInLiquid() || this.mc.thePlayer.isSneaking()) {
                        return;
                    }
                    event.setBoundingBox(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).contract(0.0, 2.000111E-12, 0.0).offset(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ()));
                }
            }
        }
    }

    @Handler(value=PacketEvent.class)
    public void onPacketSent(PacketEvent event) {
        if (this.mc.thePlayer != null) {
            switch ((jesusMode)((Object)this.modes.getValue())) {
                case NCP: {
                    if (!(event.getPacket() instanceof C03PacketPlayer) || !event.isSending() || this.isInLiquid() || !this.isOnLiquid() || this.mc.thePlayer.isSneaking() || this.mc.thePlayer.fallDistance > 3.0f || this.mc.thePlayer.isBurning() && this.isOnWater()) {
                        return;
                    }
                    C03PacketPlayer packet = (C03PacketPlayer)event.getPacket();
                    if (event.getPacket() instanceof C03PacketPlayer && !this.mc.thePlayer.isMoving()) {
                        event.setCancelled(true);
                    }
                    if (this.mc.thePlayer.isSprinting() && this.mc.thePlayer.isInLava() && this.isOnLiquid()) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                    }
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    packet.setY(packet.getPositionY() + (this.mc.thePlayer.ticksExisted % 2 == 0 ? 2.000111E-12 : 0.0));
                    packet.setOnGround(this.mc.thePlayer.ticksExisted % 2 != 0);
                }
            }
        }
    }

    private boolean isOnLiquid() {
        double y = this.mc.thePlayer.posY - 0.015625;
        for (int x = MathHelper.floor_double(this.mc.thePlayer.posX); x < MathHelper.ceiling_double_int(this.mc.thePlayer.posX); ++x) {
            for (int z = MathHelper.floor_double(this.mc.thePlayer.posZ); z < MathHelper.ceiling_double_int(this.mc.thePlayer.posZ); ++z) {
                BlockPos pos = new BlockPos(x, MathHelper.floor_double(y), z);
                if (!(this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    private boolean isOnWater() {
        double y = this.mc.thePlayer.posY - 0.03;
        for (int x = MathHelper.floor_double(this.mc.thePlayer.posX); x < MathHelper.ceiling_double_int(this.mc.thePlayer.posX); ++x) {
            for (int z = MathHelper.floor_double(this.mc.thePlayer.posZ); z < MathHelper.ceiling_double_int(this.mc.thePlayer.posZ); ++z) {
                BlockPos pos = new BlockPos(x, MathHelper.floor_double(y), z);
                if (!(this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockLiquid) || this.mc.theWorld.getBlockState(pos).getBlock().getMaterial() != Material.water) continue;
                return true;
            }
        }
        return false;
    }

    private boolean isInLiquid() {
        double y = this.mc.thePlayer.posY + 0.01;
        for (int x = MathHelper.floor_double(this.mc.thePlayer.posX); x < MathHelper.ceiling_double_int(this.mc.thePlayer.posX); ++x) {
            for (int z = MathHelper.floor_double(this.mc.thePlayer.posZ); z < MathHelper.ceiling_double_int(this.mc.thePlayer.posZ); ++z) {
                BlockPos pos = new BlockPos(x, (int)y, z);
                if (!(this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    private double getSpeed() {
        return Math.sqrt(this.mc.thePlayer.motionX * this.mc.thePlayer.motionX + this.mc.thePlayer.motionZ * this.mc.thePlayer.motionZ);
    }

    private void setSpeed(double speed) {
        this.mc.thePlayer.motionX = -(Math.sin(this.getDirection()) * speed);
        this.mc.thePlayer.motionZ = Math.cos(this.getDirection()) * speed;
    }

    private boolean isBlockUnder() {
        for (int i = (int)(this.mc.thePlayer.posY - 1.0); i > 0; --i) {
            BlockPos pos = new BlockPos(this.mc.thePlayer.posX, (double)i, this.mc.thePlayer.posZ);
            if (this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }

    private float getDirection() {
        float direction = this.mc.thePlayer.rotationYaw;
        if (this.mc.thePlayer.moveForward < 0.0f) {
            direction += 180.0f;
        }
        float forward = this.mc.thePlayer.moveForward < 0.0f ? -0.5f : (this.mc.thePlayer.moveForward > 0.0f ? 0.5f : 1.0f);
        if (this.mc.thePlayer.moveStrafing > 0.0f) {
            direction -= 90.0f * forward;
        } else if (this.mc.thePlayer.moveStrafing < 0.0f) {
            direction += 90.0f * forward;
        }
        return direction *= (float)Math.PI / 180;
    }

    @Override
    public void onEnable() {
        this.timer.reset();
    }

    @Override
    public void onDisable() {
        this.wasWater = false;
    }

    private boolean isInLiquid(double value) {
        double y = this.mc.thePlayer.posY + value;
        for (int x = MathHelper.floor_double(this.mc.thePlayer.posX); x < MathHelper.ceiling_double_int(this.mc.thePlayer.posX); ++x) {
            for (int z = MathHelper.floor_double(this.mc.thePlayer.posZ); z < MathHelper.ceiling_double_int(this.mc.thePlayer.posZ); ++z) {
                BlockPos pos = new BlockPos(x, (int)y, z);
                if (!(this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    public static enum Mode {
        SOLID,
        BOUNCE;

    }

    public static enum jesusMode {
        VANILLA("Vanilla"),
        NCP("NCP"),
        DOLPHIN("Dolphin"),
        DEV("Dev");

        private final String name;

        private jesusMode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

