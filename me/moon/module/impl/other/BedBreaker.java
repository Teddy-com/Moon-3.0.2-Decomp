/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.other;

import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.event.impl.render.Render3DEvent;
import me.moon.module.Module;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class BedBreaker
extends Module {
    private NumberValue<Float> reach = new NumberValue<Float>("Range", Float.valueOf(4.0f), Float.valueOf(0.0f), Float.valueOf(6.0f), Float.valueOf(0.1f));
    private BlockPos currBlockPos;
    private BlockPos renderBlockPos;

    public BedBreaker() {
        super("BedBreaker", Module.Category.OTHER, -1);
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            boolean found = false;
            int y = (int)(this.mc.thePlayer.posY - (double)((Float)this.reach.getValue()).floatValue());
            while ((double)y <= this.mc.thePlayer.posY + (double)((Float)this.reach.getValue()).floatValue()) {
                int x = (int)(this.mc.thePlayer.posX - (double)((Float)this.reach.getValue()).floatValue());
                while ((double)x <= this.mc.thePlayer.posX + (double)((Float)this.reach.getValue()).floatValue()) {
                    int z = (int)(this.mc.thePlayer.posZ - (double)((Float)this.reach.getValue()).floatValue());
                    while ((double)z <= this.mc.thePlayer.posZ + (double)((Float)this.reach.getValue()).floatValue()) {
                        BlockPos position = new BlockPos(x, y, z);
                        if (this.mc.theWorld.getBlockState(position).getBlock().equals(Blocks.bed)) {
                            if (this.currBlockPos != null) {
                                float[] rotations = Moon.INSTANCE.getRotationUtil().getFacingRotations((double)this.currBlockPos.getX(), (double)this.currBlockPos.getY() - 0.5, (double)this.currBlockPos.getZ());
                                event.setYaw(rotations[0]);
                                event.setPitch(rotations[1]);
                            }
                            if (PlayerControllerMP.blockHitDelay > 1) {
                                PlayerControllerMP.blockHitDelay = 1;
                            }
                            if (this.currBlockPos == null) {
                                this.currBlockPos = position;
                            }
                            found = true;
                        }
                        ++z;
                    }
                    ++x;
                }
                ++y;
            }
            if (!found) {
                this.currBlockPos = null;
                this.renderBlockPos = null;
            }
        } else if (this.currBlockPos != null && !event.isPre()) {
            EnumFacing direction = this.getFacingDirection(this.currBlockPos);
            if (direction != null) {
                this.mc.playerController.onPlayerDamageBlock(this.currBlockPos, direction);
            }
            this.mc.thePlayer.swingItem();
            this.renderBlockPos = this.currBlockPos;
        }
    }

    @Handler(value=Render3DEvent.class)
    public void onRender3d(Render3DEvent event) {
        if (this.renderBlockPos != null) {
            GL11.glPushMatrix();
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glDisable((int)3553);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            GL11.glColor4f((float)0.21f, (float)0.21f, (float)0.21f, (float)0.3f);
            RenderUtil.drawBoundingBox(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).offset((double)this.renderBlockPos.getX() - this.mc.getRenderManager().renderPosX, (double)this.renderBlockPos.getY() - this.mc.getRenderManager().renderPosY, (double)this.renderBlockPos.getZ() - this.mc.getRenderManager().renderPosZ));
            RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).offset((double)this.renderBlockPos.getX() - this.mc.getRenderManager().renderPosX, (double)this.renderBlockPos.getY() - this.mc.getRenderManager().renderPosY, (double)this.renderBlockPos.getZ() - this.mc.getRenderManager().renderPosZ));
            GL11.glLineWidth((float)1.0f);
            GL11.glDisable((int)2848);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDepthMask((boolean)true);
            GL11.glDisable((int)3042);
            GL11.glPopMatrix();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }

    private EnumFacing getFacingDirection(BlockPos pos) {
        EnumFacing direction = null;
        if (!this.mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isFullBlockSolid()) {
            direction = EnumFacing.UP;
        } else if (!this.mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isFullBlockSolid()) {
            direction = EnumFacing.DOWN;
        } else if (!this.mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isFullBlockSolid()) {
            direction = EnumFacing.EAST;
        } else if (!this.mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isFullBlockSolid()) {
            direction = EnumFacing.WEST;
        } else if (!this.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isFullBlockSolid()) {
            direction = EnumFacing.SOUTH;
        } else if (!this.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isFullBlockSolid()) {
            direction = EnumFacing.NORTH;
        }
        MovingObjectPosition rayResult = this.mc.theWorld.rayTraceBlocks(new Vec3(this.mc.thePlayer.posX, this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight(), this.mc.thePlayer.posZ), new Vec3((double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5));
        return rayResult != null && rayResult.getBlockPos() == pos ? rayResult.sideHit : direction;
    }
}

