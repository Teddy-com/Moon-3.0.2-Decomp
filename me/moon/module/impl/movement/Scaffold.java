/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.movement;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.player.JumpEvent;
import me.moon.event.impl.player.MotionEvent;
import me.moon.event.impl.player.SafewalkEvent;
import me.moon.event.impl.player.StrafeEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.Module;
import me.moon.utils.MathUtils;
import me.moon.utils.blockdata.BlockData;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.CombatUtil;
import me.moon.utils.game.MoveUtil;
import me.moon.utils.game.Printer;
import me.moon.utils.game.Rotations;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class Scaffold
extends Module {
    private final List<Block> invalid = Arrays.asList(Blocks.ladder, Blocks.anvil, Blocks.wooden_pressure_plate, Blocks.stone_slab, Blocks.wooden_slab, Blocks.stone_slab2, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.sapling, Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.chest, Blocks.anvil, Blocks.enchanting_table, Blocks.chest, Blocks.ender_chest, Blocks.gravel, Blocks.lever, Blocks.noteblock, Blocks.sand, Blocks.gravel, Blocks.stone_button, Blocks.wooden_button, Blocks.red_flower, Blocks.yellow_flower, Blocks.torch, Blocks.cocoa, Blocks.double_plant, Blocks.vine, Blocks.waterlily, Blocks.crafting_table, Blocks.enchanting_table, Blocks.acacia_fence, Blocks.birch_fence, Blocks.dark_oak_fence, Blocks.jungle_fence, Blocks.oak_fence, Blocks.acacia_fence_gate, Blocks.birch_fence_gate, Blocks.birch_fence_gate, Blocks.dark_oak_fence_gate, Blocks.jungle_fence_gate, Blocks.oak_fence_gate, Blocks.spruce_fence_gate, Blocks.spruce_fence, Blocks.cobblestone_wall, Blocks.dispenser, Blocks.dropper, Blocks.beacon, Blocks.bed, Blocks.acacia_door, Blocks.dark_oak_door, Blocks.birch_door, Blocks.iron_door, Blocks.jungle_door, Blocks.oak_door, Blocks.spruce_door, Blocks.trapdoor, Blocks.iron_trapdoor, Blocks.cake, Blocks.cauldron, Blocks.tnt);
    private final TimerUtil timerMotion = new TimerUtil();
    private BlockData blockData;
    private BlockData lastBlockData;
    private final EnumValue<mode> modes = new EnumValue<mode>("Mode", mode.NCP);
    public final BooleanValue killAura = new BooleanValue("KillAura", true);
    private final BooleanValue switchValue = new BooleanValue("Switch", true);
    public final BooleanValue watchdog = new BooleanValue("Watchdog", true);
    private final BooleanValue tower = new BooleanValue("Tower", true);
    private final NumberValue<Float> towerBoost = new NumberValue<Float>("Tower Boost", Float.valueOf(1.0f), Float.valueOf(1.0f), Float.valueOf(2.0f), Float.valueOf(0.1f), this.tower, "true");
    private final BooleanValue keepY = new BooleanValue("KeepY", true);
    public static final BooleanValue downwards = new BooleanValue("Downwards", true);
    private final MouseFilter yawMouseFilter = new MouseFilter();
    private final MouseFilter pitchMouseFilter = new MouseFilter();
    private float lastYaw;
    private float lastPitch;
    private int keepYPos;
    private int stage = 0;
    private int counter;
    private boolean shouldSneak;
    private boolean engage;
    private boolean shouldTurn;

    public Scaffold() {
        super("Scaffold", Module.Category.MOVEMENT, new Color(255, 155, 255, 255).getRGB());
        this.setDescription("Auto places blocks under you");
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        this.setSuffix(((mode)((Object)this.modes.getValue())).getName());
        int blockKey = this.mc.gameSettings.keyBindUseItem.getKeyCode();
        int sneakKey = this.mc.gameSettings.keyBindSneak.getKeyCode();
        if (event.isPre()) {
            ++this.stage;
            if (downwards.isEnabled() && this.mc.gameSettings.keyBindSneak.isKeyDown()) {
                if (this.mc.thePlayer.ticksExisted % 7 == 0) {
                    this.keepYPos = MathHelper.floor_double(this.mc.thePlayer.posY - 1.0);
                }
            } else if (this.keepY.isEnabled() && (!this.mc.thePlayer.isMoving() && this.mc.gameSettings.keyBindJump.isKeyDown() || this.mc.thePlayer.isCollidedVertically || this.mc.thePlayer.onGround)) {
                this.keepYPos = MathHelper.floor_double(this.mc.thePlayer.posY);
            } else if (!this.keepY.isEnabled()) {
                this.keepYPos = MathHelper.floor_double(this.mc.thePlayer.posY);
            }
        }
        switch ((mode)((Object)this.modes.getValue())) {
            case SNEAK: {
                if (!event.isPre()) break;
                Minecraft.rightClickDelayTimer = 0;
                KeyBinding.setKeyBindState(sneakKey, false);
                KeyBinding.setKeyBindState(blockKey, false);
                event.setPitch(82.5f);
                event.setYaw(this.mc.thePlayer.rotationYaw + 180.0f);
                this.blockData = null;
                BlockPos blockBelow = new BlockPos(this.mc.thePlayer.posX, (double)(this.keepYPos - 1), this.mc.thePlayer.posZ);
                if (this.mc.theWorld.getBlockState(blockBelow).getBlock().getMaterial().isReplaceable()) {
                    this.blockData = Moon.INSTANCE.getBlockData().getBlockData2(blockBelow);
                    if (this.blockData == null || this.blockData.position == null || this.blockData.face == null) break;
                    float[] rotations = Moon.INSTANCE.getRotationUtil().getLegitFacingRotations(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ());
                    event.setPitch(MathHelper.clamp_float(rotations[1], -90.0f, 90.0f));
                    event.setYaw(this.mc.thePlayer.rotationYaw + 180.0f + MathUtils.getRandomInRange(0.01f, 0.05f));
                    this.lastBlockData = this.blockData;
                    KeyBinding.setKeyBindState(sneakKey, true);
                    KeyBinding.setKeyBindState(blockKey, true);
                    break;
                }
                if (this.mc.thePlayer.ticksExisted % 2 != 0) break;
                KeyBinding.setKeyBindState(blockKey, true);
                break;
            }
            case SNEAK2: {
                if (!event.isPre()) break;
                Minecraft.rightClickDelayTimer = 0;
                KeyBinding.setKeyBindState(sneakKey, false);
                KeyBinding.setKeyBindState(blockKey, false);
                event.setPitch(82.5f);
                event.setYaw(this.mc.thePlayer.rotationYaw + 180.0f);
                this.blockData = null;
                BlockPos blockBelow = new BlockPos(this.mc.thePlayer.posX, (double)(this.keepYPos - 1), this.mc.thePlayer.posZ);
                if (this.mc.theWorld.getBlockState(blockBelow).getBlock().getMaterial().isReplaceable()) {
                    this.blockData = Moon.INSTANCE.getBlockData().getBlockData2(blockBelow);
                    if (this.blockData == null || this.blockData.position == null || this.blockData.face == null) break;
                    float[] rotations = Moon.INSTANCE.getRotationUtil().getLegitFacingRotations(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ());
                    event.setPitch(MathHelper.clamp_float(rotations[1], -90.0f, 90.0f));
                    event.setYaw(this.mc.thePlayer.rotationYaw + 180.0f + MathUtils.getRandomInRange(0.01f, 0.05f));
                    this.lastBlockData = this.blockData;
                    KeyBinding.setKeyBindState(sneakKey, true);
                    int delay = (int)Math.floor(MathUtils.getRandomInRange(1.0f, 3.4f));
                    if (this.counter > delay) {
                        KeyBinding.setKeyBindState(blockKey, true);
                    }
                    ++this.counter;
                    break;
                }
                this.counter = 0;
                if (this.mc.thePlayer.ticksExisted % 2 != 0) break;
                KeyBinding.setKeyBindState(blockKey, true);
                break;
            }
            case NCP: {
                if (event.isPre()) {
                    BlockPos blockBelow;
                    float speed = 0.4f;
                    if (this.lastBlockData != null && this.lastBlockData.position != null && this.lastBlockData.face != null) {
                        float[] rotations = Moon.INSTANCE.getRotationUtil().getLegitFacingRotations(this.lastBlockData.position.getX(), this.lastBlockData.position.getY(), this.lastBlockData.position.getZ());
                        float yaw = this.yawMouseFilter.smooth(rotations[0] + MathUtils.getRandomInRange(-1.0f, 5.0f), speed);
                        float pitch = MathHelper.clamp_float(this.pitchMouseFilter.smooth(rotations[1] + MathUtils.getRandomInRange(-1.2f, 3.5f), speed), -90.0f, 90.0f);
                        event.setPitch(pitch);
                        event.setYaw(yaw);
                    }
                    this.blockData = null;
                    if (!this.mc.thePlayer.isSneaking() && this.mc.theWorld.getBlockState(blockBelow = new BlockPos(this.mc.thePlayer.posX, (double)(this.keepYPos - 1), this.mc.thePlayer.posZ)).getBlock().getMaterial().isReplaceable()) {
                        this.blockData = Moon.INSTANCE.getBlockData().getBlockData2(blockBelow);
                        if (this.blockData != null && this.blockData.position != null && this.blockData.face != null) {
                            float[] rotations = Moon.INSTANCE.getRotationUtil().getLegitFacingRotations(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ());
                            float yaw = rotations[0];
                            float pitch = rotations[1];
                            this.lastYaw = yaw;
                            this.lastPitch = pitch;
                            event.setPitch(MathHelper.clamp_float(this.pitchMouseFilter.smooth(rotations[1] + MathUtils.getRandomInRange(-1.2f, 3.5f), speed), -90.0f, 90.0f));
                            event.setYaw(this.yawMouseFilter.smooth(rotations[0] + MathUtils.getRandomInRange(-1.0f, 5.0f), speed));
                            this.lastBlockData = this.blockData;
                        }
                    }
                }
                if (event.isPre() || this.blockData == null || this.modes.getValue() == mode.CUBECRAFT) break;
                if (this.getBlockCount() <= 0 || !this.switchValue.isEnabled() && this.mc.thePlayer.getCurrentEquippedItem() != null && !(this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock)) {
                    return;
                }
                int heldItem = this.mc.thePlayer.inventory.currentItem;
                int stackSize = 0;
                if (this.switchValue.isEnabled()) {
                    int i;
                    int stackWithHighestSting = 0;
                    for (i = 0; i < 9; ++i) {
                        if (this.mc.thePlayer.inventory.getStackInSlot(i) == null || this.mc.thePlayer.inventory.getStackInSlot((int)i).stackSize == 0 || !(this.mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) || this.invalid.contains(((ItemBlock)this.mc.thePlayer.inventory.getStackInSlot(i).getItem()).getBlock()) || stackSize >= this.mc.thePlayer.inventory.getStackInSlot((int)i).stackSize) continue;
                        stackSize = this.mc.thePlayer.inventory.getStackInSlot((int)i).stackSize;
                        stackWithHighestSting = i;
                    }
                    if (stackSize != 0) {
                        this.mc.thePlayer.inventory.currentItem = stackWithHighestSting;
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
                    } else {
                        for (i = 0; i < 45; ++i) {
                            if (this.mc.thePlayer.inventory.getStackInSlot(i) == null || this.mc.thePlayer.inventory.getStackInSlot((int)i).stackSize == 0 || !(this.mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) || this.invalid.contains(((ItemBlock)this.mc.thePlayer.inventory.getStackInSlot(i).getItem()).getBlock())) continue;
                            this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, i, 8, 2, this.mc.thePlayer);
                            int updateHotBar = 0;
                            int gaySize = 0;
                            for (int s = 0; s < 9; ++s) {
                                if (this.mc.thePlayer.inventory.getStackInSlot(s) == null || this.mc.thePlayer.inventory.getStackInSlot((int)s).stackSize == 0 || !(this.mc.thePlayer.inventory.getStackInSlot(s).getItem() instanceof ItemBlock) || this.invalid.contains(((ItemBlock)this.mc.thePlayer.inventory.getStackInSlot(s).getItem()).getBlock()) || gaySize >= this.mc.thePlayer.inventory.getStackInSlot((int)s).stackSize) continue;
                                gaySize = this.mc.thePlayer.inventory.getStackInSlot((int)s).stackSize;
                                updateHotBar = s;
                            }
                            if (gaySize == 0) break;
                            this.mc.thePlayer.inventory.currentItem = updateHotBar;
                            this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
                            break;
                        }
                    }
                }
                if (this.tower.isEnabled()) {
                    this.mc.timer.timerSpeed = 1.0f;
                    if (this.mc.gameSettings.keyBindJump.isKeyDown() && !this.mc.thePlayer.isPotionActive(Potion.jump)) {
                        if (!this.mc.thePlayer.isMoving()) {
                            this.mc.timer.timerSpeed = ((Float)this.towerBoost.getValue()).floatValue();
                            this.mc.thePlayer.motionY = 0.42f;
                            Minecraft.getMinecraft().thePlayer.motionZ = 0.0;
                            this.mc.thePlayer.motionX = 0.0;
                            if (this.timerMotion.hasReached(1700L)) {
                                this.mc.thePlayer.motionY = -0.2;
                                this.timerMotion.reset();
                            }
                        } else if (this.mc.thePlayer.onGround) {
                            this.mc.thePlayer.motionY = 0.42f;
                        } else if (this.mc.thePlayer.motionY < 0.17 && this.mc.thePlayer.motionY > 0.16) {
                            this.mc.thePlayer.motionY = -0.01f;
                        }
                    } else {
                        this.timerMotion.reset();
                    }
                }
                if (this.watchdog.getValue().booleanValue()) {
                    this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem(), this.blockData.position, this.blockData.face, new Vec3((double)this.blockData.position.getX() + (double)MathUtils.getRandom(100000000, 800000000) * 1.0E-9, (double)this.blockData.position.getY() + (double)MathUtils.getRandom(100000000, 800000000) * 1.0E-9, (double)this.blockData.position.getZ() + (double)MathUtils.getRandom(100000000, 800000000) * 1.0E-9));
                } else {
                    this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem(), this.blockData.position, this.blockData.face, new Vec3((double)this.blockData.position.getX() + Math.random(), (double)this.blockData.position.getY() + Math.random(), (double)this.blockData.position.getZ() + Math.random()));
                }
                this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                if (!this.switchValue.isEnabled()) break;
                this.mc.thePlayer.inventory.currentItem = heldItem;
                this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
                break;
            }
            case CUBECRAFT: {
                if (!event.isPre()) break;
                if (this.lastBlockData != null && this.lastBlockData.position != null && this.lastBlockData.face != null) {
                    float[] rotations = Moon.INSTANCE.getRotationUtil().getFacingRotations(this.lastBlockData.position.getX(), (double)this.lastBlockData.position.getY(), this.lastBlockData.position.getZ());
                    float yaw = rotations[0] + MathUtils.getRandomInRange(-1.0f, 5.0f);
                    float pitch = MathHelper.clamp_float(rotations[1] + MathUtils.getRandomInRange(-1.2f, 3.5f), -90.0f, 90.0f);
                    event.setPitch(pitch);
                    event.setYaw(yaw);
                }
                this.blockData = null;
                if (!this.mc.thePlayer.isSneaking()) {
                    double zAddition;
                    double speed = 0.5;
                    float f = this.mc.thePlayer.rotationYaw * ((float)Math.PI / 180);
                    double xAddition = (double)MathHelper.sin(f) * speed;
                    BlockPos blockBelow = new BlockPos(this.mc.thePlayer.posX - xAddition, (double)(this.keepYPos - 1), this.mc.thePlayer.posZ + (zAddition = (double)MathHelper.cos(f) * speed));
                    if (this.mc.theWorld.getBlockState(blockBelow).getBlock().getMaterial().isReplaceable()) {
                        this.blockData = Moon.INSTANCE.getBlockData().getBlockData2(blockBelow);
                        if (this.blockData != null && this.blockData.position != null && this.blockData.face != null) {
                            float[] rotations = Moon.INSTANCE.getRotationUtil().getFacingRotations(this.blockData.position.getX(), (double)this.blockData.position.getY(), this.blockData.position.getZ());
                            event.setPitch(MathHelper.clamp_float(rotations[1] + MathUtils.getRandomInRange(-1.2f, 1.2f), -90.0f, 90.0f));
                            event.setYaw(rotations[0] + MathUtils.getRandomInRange(-1.0f, 1.0f));
                            this.lastBlockData = this.blockData;
                        }
                    }
                }
                int oldHeldItem = this.mc.thePlayer.inventory.currentItem;
                if (this.blockData != null) {
                    if (this.getBlockCount() <= 0 || !this.switchValue.isEnabled() && this.mc.thePlayer.getCurrentEquippedItem() != null && !(this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock)) {
                        return;
                    }
                    if (this.switchValue.isEnabled()) {
                        for (int i = 0; i < 9; ++i) {
                            if (this.mc.thePlayer.inventory.getStackInSlot(i) == null || this.mc.thePlayer.inventory.getStackInSlot((int)i).stackSize == 0 || !(this.mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) || this.invalid.contains(((ItemBlock)this.mc.thePlayer.inventory.getStackInSlot(i).getItem()).getBlock())) continue;
                            if (i == this.counter) break;
                            Printer.print("switching from " + this.counter + " to " + i);
                            this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i));
                            this.counter = i;
                            return;
                        }
                    }
                    if (this.tower.isEnabled()) {
                        if (this.mc.gameSettings.keyBindJump.isKeyDown() && !this.mc.thePlayer.isMoving()) {
                            Minecraft.getMinecraft().thePlayer.motionZ = 0.0;
                            this.mc.thePlayer.motionX = 0.0;
                            if (this.timerMotion.sleep(150L)) {
                                this.mc.thePlayer.motionY = 0.42f;
                            }
                        } else {
                            this.timerMotion.reset();
                        }
                    }
                    if (this.stage >= 1) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                        this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem(), this.blockData.position, this.blockData.face, new Vec3((double)this.blockData.position.getX() + Math.random(), (double)this.blockData.position.getY() + Math.random(), (double)this.blockData.position.getZ() + Math.random()));
                        this.stage = 0;
                        if (this.switchValue.isEnabled()) {
                            // empty if block
                        }
                    }
                }
                this.mc.thePlayer.inventory.currentItem = oldHeldItem;
                break;
            }
            case AAC: {
                break;
            }
            case INTAVE: {
                if (!event.isPre()) break;
                Minecraft.rightClickDelayTimer = 0;
                KeyBinding.setKeyBindState(blockKey, false);
                event.setPitch(82.5f + MathUtils.getRandomInRange(-0.5f, 0.5f));
                event.setYaw(this.mc.thePlayer.rotationYaw + 180.0f);
                this.blockData = null;
                BlockPos blockBelow = new BlockPos(this.mc.thePlayer.posX, (double)(this.keepYPos - 1), this.mc.thePlayer.posZ);
                if (this.mc.theWorld.getBlockState(blockBelow).getBlock().getMaterial().isReplaceable()) {
                    this.blockData = Moon.INSTANCE.getBlockData().getBlockData2(blockBelow);
                    if (this.blockData == null || this.blockData.position == null || this.blockData.face == null) break;
                    int horizontalIndex = this.blockData.face.getHorizontalIndex();
                    double x = this.blockData.position.getX();
                    double y = this.blockData.position.getY();
                    double z = this.blockData.position.getZ();
                    switch (horizontalIndex) {
                        case 0: {
                            z += 0.5;
                            break;
                        }
                        case 1: {
                            x -= 0.5;
                            break;
                        }
                        case 2: {
                            z -= 0.5;
                            break;
                        }
                        case 3: {
                            x += 0.5;
                        }
                    }
                    float[] rotations = Moon.INSTANCE.getRotationUtil().getFacingRotations(x, y, z);
                    float sens = CombatUtil.getSensitivityMultiplier();
                    float yaw = rotations[0] + MathUtils.getRandomInRange(-0.55f, 0.55f);
                    float pitch = rotations[1] + MathUtils.getRandomInRange(-0.5f, 0.5f);
                    float yawGCD = (float)Math.round(yaw / sens) * sens + 0.01f;
                    float pitchGCD = MathHelper.clamp_float((float)Math.round(pitch / sens) * sens + 0.01f, -90.0f, 90.0f);
                    event.setPitch(pitchGCD);
                    event.setYaw(yawGCD);
                    KeyBinding.setKeyBindState(blockKey, true);
                    this.lastBlockData = this.blockData;
                    this.stage = 0;
                    break;
                }
                if (this.mc.thePlayer.ticksExisted % 2 != 0) break;
                KeyBinding.setKeyBindState(blockKey, true);
                break;
            }
            case DEV: {
                Minecraft.rightClickDelayTimer = 0;
                KeyBinding.setKeyBindState(blockKey, false);
                this.blockData = null;
                if (this.mc.thePlayer.isSneaking()) break;
                BlockPos blockBelow = new BlockPos(this.mc.thePlayer.posX, (double)(this.keepYPos - 1), this.mc.thePlayer.posZ);
                float yaw = this.mc.thePlayer.rotationYaw;
                if (!this.mc.theWorld.getBlockState(blockBelow).getBlock().getMaterial().isReplaceable()) break;
                this.blockData = Moon.INSTANCE.getBlockData().getBlockData2(blockBelow);
                if (this.blockData == null || this.blockData.face == null) break;
                int horizontalIndex = this.blockData.face.getHorizontalIndex();
                double x = this.blockData.position.getX();
                double z = this.blockData.position.getZ();
                switch (horizontalIndex) {
                    case 0: {
                        z += 0.5;
                        break;
                    }
                    case 1: {
                        x -= 0.5;
                        break;
                    }
                    case 2: {
                        z -= 0.5;
                        break;
                    }
                    case 3: {
                        x += 0.5;
                    }
                }
                if (this.blockData == null || this.blockData.position == null || this.blockData.face == null) break;
                float[] rotations = Moon.INSTANCE.getRotationUtil().getFacingRotations(x, (double)this.blockData.position.getY(), z);
                float pitch = MathHelper.clamp_float(rotations[1], -90.0f, 90.0f);
                yaw = rotations[0];
                event.setPitch(pitch);
                this.lastPitch = pitch;
                event.setYaw(yaw);
                this.lastYaw = yaw;
                this.engage = true;
                KeyBinding.setKeyBindState(blockKey, true);
                this.lastBlockData = this.blockData;
            }
        }
    }

    @Handler(value=MotionEvent.class)
    public void onMotion(MotionEvent event) {
        switch ((mode)((Object)this.modes.getValue())) {
            case NCP: {
                break;
            }
            case INTAVE: {
                if (!this.mc.thePlayer.onGround) break;
                break;
            }
            case CUBECRAFT: {
                if (!this.mc.thePlayer.onGround) break;
                MoveUtil.setMoveSpeed(event, 0.1924);
                if (this.stage > 1) break;
                break;
            }
        }
    }

    @Handler(value=StrafeEvent.class)
    public void onStrafe(StrafeEvent event) {
        if (this.mc.thePlayer.ticksExisted == this.counter) {
            // empty if block
        }
        switch ((mode)((Object)this.modes.getValue())) {
            case INTAVE: {
                break;
            }
            case NCP: {
                break;
            }
            case AAC: {
                event.setForward(-event.getForward());
                event.setStrafe(-event.getStrafe());
                ++this.stage;
                if (downwards.isEnabled() && this.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    if (this.mc.thePlayer.ticksExisted % 35 == 0) {
                        this.keepYPos = MathHelper.floor_double(this.mc.thePlayer.posY - 1.0);
                    }
                } else if (this.keepY.isEnabled()) {
                    if (!this.mc.thePlayer.isMoving() && this.mc.gameSettings.keyBindJump.isKeyDown() || this.mc.thePlayer.isCollidedVertically || this.mc.thePlayer.onGround) {
                        this.keepYPos = MathHelper.floor_double(this.mc.thePlayer.posY);
                    }
                } else {
                    this.keepYPos = MathHelper.floor_double(this.mc.thePlayer.posY);
                }
                this.blockData = null;
                if (!this.mc.thePlayer.isSneaking()) {
                    BlockPos blockBelow = new BlockPos(this.mc.thePlayer.posX, (double)(this.keepYPos - 1), this.mc.thePlayer.posZ);
                    this.blockData = Moon.INSTANCE.getBlockData().getBlockData2(blockBelow);
                    if (this.blockData != null && this.blockData.position != null && this.blockData.face != null) {
                        float[] rotations = Moon.INSTANCE.getRotationUtil().getFacingRotations(this.blockData.position.getX(), (double)this.blockData.position.getY(), this.blockData.position.getZ());
                        float yaw = this.mc.thePlayer.rotationYaw - 180.0f;
                        float pitch = rotations[1];
                        this.lastYaw = yaw;
                        this.lastPitch = pitch;
                        if (Math.abs(pitch) > 90.0f) {
                            pitch = 90.0f;
                        }
                        if (Rotations.INSTANCE.getCurrentRotation() == null) {
                            Rotations.INSTANCE.setCurrentRotation(new Rotations.Rotation(Float.valueOf(yaw), Float.valueOf(pitch)));
                        }
                    } else if (Rotations.INSTANCE.getCurrentRotation() == null) {
                        Rotations.INSTANCE.setCurrentRotation(new Rotations.Rotation(Float.valueOf(this.lastYaw), Float.valueOf(this.lastPitch)));
                    }
                }
                if (Rotations.INSTANCE.getCurrentRotation() == null || this.blockData == null) break;
                if (this.getBlockCount() <= 0 || !this.switchValue.isEnabled() && this.mc.thePlayer.getCurrentEquippedItem() != null && !(this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock)) {
                    return;
                }
                if (this.stage <= 0) break;
                this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem(), this.blockData.position, this.blockData.face, new Vec3((double)this.blockData.position.getX() + Math.random(), (double)this.blockData.position.getY() + Math.random(), (double)this.blockData.position.getZ() + Math.random()));
                this.stage = 0;
                this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                break;
            }
            case DEV: {
                if (Rotations.INSTANCE.getCurrentRotation() == null && this.engage) {
                    this.engage = false;
                    this.shouldTurn = true;
                    Rotations.INSTANCE.setCurrentRotation(new Rotations.Rotation(Float.valueOf(this.lastYaw), Float.valueOf(this.lastPitch)));
                }
                if (event.getForward() == 0.0f || !this.shouldTurn) break;
                event.setForward(0.0f);
                this.shouldTurn = false;
            }
        }
    }

    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock) || this.invalid.contains(((ItemBlock)item).getBlock())) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }

    @Handler(value=Render2DEvent.class)
    public void onRender2D(Render2DEvent event) {
        GL11.glPushMatrix();
        GlStateManager.disableAlpha();
        Fonts.moonThemeSF.drawStringWithShadow(this.getBlockCount() + " \u00a7fblocks", (float)((event.getScaledResolution().getScaledWidth() >> 1) + 1) - (float)Fonts.moonThemeSF.getStringWidth(Integer.toString(this.getBlockCount()) + " \u00a7fblocks") / 2.0f, (event.getScaledResolution().getScaledHeight() >> 1) + 24, this.getBlockColor(this.getBlockCount()));
        GL11.glPopMatrix();
    }

    @Handler(value=SafewalkEvent.class)
    public void onSafewalk(SafewalkEvent event) {
        if (this.mc.thePlayer != null) {
            event.setCancelled(!(this.blockData != null && downwards.isEnabled() && this.mc.gameSettings.keyBindSneak.isKeyDown() || downwards.isEnabled() && this.mc.gameSettings.keyBindSneak.isKeyDown() && this.lastBlockData != null && (double)this.lastBlockData.position.getY() <= Math.floor(this.mc.thePlayer.posY) - 1.0 || !(this.keepY.isEnabled() ? !this.mc.gameSettings.keyBindJump.isKeyDown() && this.mc.thePlayer.onGround : this.mc.thePlayer.onGround)));
        }
    }

    @Handler(value=JumpEvent.class)
    public void onKeyPress(JumpEvent event) {
    }

    private int getBlockColor(int count) {
        float maxBlocks = 64.0f;
        float hue = Math.max(0.0f, Math.min((float)count, maxBlocks) / maxBlocks);
        return Color.HSBtoRGB(hue / 3.0f, 1.0f, 1.0f) | 0xFF000000;
    }

    @Override
    public void onEnable() {
        this.mc.timer.timerSpeed = 1.0f;
        if (this.mc.theWorld != null) {
            this.timerMotion.reset();
            this.keepYPos = MathHelper.floor_double(this.mc.thePlayer.posY);
            this.lastBlockData = null;
        }
        this.counter = -1;
    }

    @Override
    public void onDisable() {
        int blockKey = this.mc.gameSettings.keyBindUseItem.getKeyCode();
        KeyBinding.setKeyBindState(blockKey, false);
        if (this.modes.getValue() == mode.CUBECRAFT && this.counter != this.mc.thePlayer.inventory.currentItem) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
        }
        if (this.tower.isEnabled() && this.modes.getValue() == mode.CUBECRAFT && this.mc.thePlayer.motionY > 0.0) {
            this.mc.thePlayer.motionY = 0.0;
        }
        this.mc.timer.timerSpeed = 1.0f;
    }

    public static enum mode {
        NCP("NCP"),
        CUBECRAFT("Cubecraft"),
        AAC("AAC"),
        INTAVE("Intave"),
        SNEAK("Sneak"),
        SNEAK2("Sneak 2"),
        DEV("Dev");

        private final String name;

        private mode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

