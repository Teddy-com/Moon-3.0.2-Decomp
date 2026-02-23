/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3d
 *  javax.vecmath.Vector4d
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import me.moon.event.Handler;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.event.impl.render.Render3DEvent;
import me.moon.module.Module;
import me.moon.utils.game.OutlineUtils;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.ColorValue;
import me.moon.utils.value.impl.EnumValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class ChestESP
extends Module {
    public ColorValue redstone = new ColorValue("Redstone", new Color(16397824).getRGB());
    public ColorValue normal = new ColorValue("Normal", new Color(42751).getRGB());
    public ColorValue ender = new ColorValue("Ender", new Color(11403519).getRGB());
    public EnumValue<Mode> mode = new EnumValue<Mode>("Mode", Mode.BOX);

    public ChestESP() {
        super("ChestESP", Module.Category.VISUALS, new Color(255, 188, 255, 255).getRGB());
    }

    @Handler(value=Render2DEvent.class)
    public void render2d(Render2DEvent event) {
        switch ((Mode)((Object)this.mode.getValue())) {
            case CSGO: {
                this.mc.theWorld.loadedTileEntityList.forEach(tileEntity -> {
                    if (tileEntity instanceof TileEntityChest) {
                        TileEntityChest chest = (TileEntityChest)tileEntity;
                        GL11.glPushMatrix();
                        double posX = RenderUtil.interpolate(chest.getPos().getX(), chest.getPos().getX(), event.getPartialTicks());
                        double posY = RenderUtil.interpolate(chest.getPos().getY(), chest.getPos().getY(), event.getPartialTicks());
                        double posZ = RenderUtil.interpolate(chest.getPos().getZ(), chest.getPos().getZ(), event.getPartialTicks());
                        AxisAlignedBB axisaligned = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.94, 0.875, 0.94).offset(posX, posY, posZ);
                        TileEntityChest adjacent = null;
                        if (((TileEntityChest)tileEntity).adjacentChestXNeg != null) {
                            adjacent = ((TileEntityChest)tileEntity).adjacentChestXNeg;
                        }
                        if (((TileEntityChest)tileEntity).adjacentChestXPos != null) {
                            adjacent = ((TileEntityChest)tileEntity).adjacentChestXPos;
                        }
                        if (((TileEntityChest)tileEntity).adjacentChestZNeg != null) {
                            adjacent = ((TileEntityChest)tileEntity).adjacentChestZNeg;
                        }
                        if (((TileEntityChest)tileEntity).adjacentChestZPos != null) {
                            adjacent = ((TileEntityChest)tileEntity).adjacentChestZPos;
                        }
                        if (adjacent != null) {
                            axisaligned = axisaligned.union(new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.94, 0.875, 0.94).offset(adjacent.getPos().getX(), adjacent.getPos().getY(), adjacent.getPos().getZ()));
                        }
                        List<Vector3d> vectors = Arrays.asList(new Vector3d(axisaligned.minX, axisaligned.minY, axisaligned.minZ), new Vector3d(axisaligned.minX, axisaligned.maxY, axisaligned.minZ), new Vector3d(axisaligned.maxX, axisaligned.minY, axisaligned.minZ), new Vector3d(axisaligned.maxX, axisaligned.maxY, axisaligned.minZ), new Vector3d(axisaligned.minX, axisaligned.minY, axisaligned.maxZ), new Vector3d(axisaligned.minX, axisaligned.maxY, axisaligned.maxZ), new Vector3d(axisaligned.maxX, axisaligned.minY, axisaligned.maxZ), new Vector3d(axisaligned.maxX, axisaligned.maxY, axisaligned.maxZ));
                        this.mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 2);
                        Vector4d finalVec = null;
                        for (Vector3d vector : vectors) {
                            vector = RenderUtil.project(vector.x - this.mc.getRenderManager().viewerPosX, vector.y - this.mc.getRenderManager().viewerPosY, vector.z - this.mc.getRenderManager().viewerPosZ);
                            if (vector == null || !(vector.z >= 0.0) || !(vector.z < 1.0)) continue;
                            if (finalVec == null) {
                                finalVec = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                            }
                            finalVec.x = Math.min(vector.x, finalVec.x);
                            finalVec.y = Math.min(vector.y, finalVec.y);
                            finalVec.z = Math.max(vector.x, finalVec.z);
                            finalVec.w = Math.max(vector.y, finalVec.w);
                        }
                        this.mc.entityRenderer.setupOverlayRendering();
                        if (finalVec != null && RenderUtil.isInViewFrustrum(axisaligned)) {
                            float xPos = (float)finalVec.x;
                            float yPos = (float)finalVec.y;
                            float eWidth = (float)finalVec.z - xPos;
                            float eHeight = (float)finalVec.w - yPos;
                            RenderUtil.drawBorderedRect(xPos - 0.75f, yPos - 0.75f, eWidth + 1.5f, eHeight + 1.5f, 0.5, -16777216, 0);
                            RenderUtil.drawBorderedRect((double)xPos - 0.5 - 0.75, (double)yPos - 0.5 - 0.75, eWidth + 1.0f + 1.5f, eHeight + 1.0f + 1.5f, 0.5, chest.getChestType() == 1 ? ((Integer)this.redstone.getValue()).intValue() : ((Integer)this.normal.getValue()).intValue(), 0);
                            RenderUtil.drawBorderedRect(xPos - 1.0f - 0.75f, yPos - 1.0f - 0.75f, eWidth + 2.0f + 1.5f, eHeight + 2.0f + 1.5f, 0.5, -16777216, 0);
                        }
                        GL11.glPopMatrix();
                    } else if (tileEntity instanceof TileEntityEnderChest) {
                        TileEntityEnderChest chest = (TileEntityEnderChest)tileEntity;
                        GL11.glPushMatrix();
                        double posX = RenderUtil.interpolate(chest.getPos().getX(), chest.getPos().getX(), event.getPartialTicks());
                        double posY = RenderUtil.interpolate(chest.getPos().getY(), chest.getPos().getY(), event.getPartialTicks());
                        double posZ = RenderUtil.interpolate(chest.getPos().getZ(), chest.getPos().getZ(), event.getPartialTicks());
                        AxisAlignedBB axisaligned = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.94, 0.875, 0.94).offset(posX, posY, posZ);
                        List<Vector3d> vectors = Arrays.asList(new Vector3d(axisaligned.minX, axisaligned.minY, axisaligned.minZ), new Vector3d(axisaligned.minX, axisaligned.maxY, axisaligned.minZ), new Vector3d(axisaligned.maxX, axisaligned.minY, axisaligned.minZ), new Vector3d(axisaligned.maxX, axisaligned.maxY, axisaligned.minZ), new Vector3d(axisaligned.minX, axisaligned.minY, axisaligned.maxZ), new Vector3d(axisaligned.minX, axisaligned.maxY, axisaligned.maxZ), new Vector3d(axisaligned.maxX, axisaligned.minY, axisaligned.maxZ), new Vector3d(axisaligned.maxX, axisaligned.maxY, axisaligned.maxZ));
                        this.mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 2);
                        Vector4d finalVec = null;
                        for (Vector3d vector : vectors) {
                            vector = RenderUtil.project(vector.x - this.mc.getRenderManager().viewerPosX, vector.y - this.mc.getRenderManager().viewerPosY, vector.z - this.mc.getRenderManager().viewerPosZ);
                            if (vector == null || !(vector.z >= 0.0) || !(vector.z < 1.0)) continue;
                            if (finalVec == null) {
                                finalVec = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                            }
                            finalVec.x = Math.min(vector.x, finalVec.x);
                            finalVec.y = Math.min(vector.y, finalVec.y);
                            finalVec.z = Math.max(vector.x, finalVec.z);
                            finalVec.w = Math.max(vector.y, finalVec.w);
                        }
                        this.mc.entityRenderer.setupOverlayRendering();
                        if (finalVec != null) {
                            float xPos = (float)finalVec.x;
                            float yPos = (float)finalVec.y;
                            float eWidth = (float)finalVec.z - xPos;
                            float eHeight = (float)finalVec.w - yPos;
                            RenderUtil.drawBorderedRect(xPos - 0.75f, yPos - 0.75f, eWidth + 1.5f, eHeight + 1.5f, 0.5, -16777216, 0);
                            RenderUtil.drawBorderedRect((double)xPos - 0.5 - 0.75, (double)yPos - 0.5 - 0.75, eWidth + 1.0f + 1.5f, eHeight + 1.0f + 1.5f, 0.5, (Integer)this.ender.getValue(), 0);
                            RenderUtil.drawBorderedRect(xPos - 1.0f - 0.75f, yPos - 1.0f - 0.75f, eWidth + 2.0f + 1.5f, eHeight + 2.0f + 1.5f, 0.5, -16777216, 0);
                        }
                        GL11.glPopMatrix();
                    }
                });
            }
        }
    }

    @Handler(value=Render3DEvent.class)
    public void render3d(Render3DEvent event) {
        switch ((Mode)((Object)this.mode.getValue())) {
            case BOX: {
                for (TileEntity tile : this.mc.theWorld.loadedTileEntityList) {
                    if (!(tile instanceof TileEntityChest)) continue;
                    double posX = (double)tile.getPos().getX() - this.mc.getRenderManager().renderPosX;
                    double posY = (double)tile.getPos().getY() - this.mc.getRenderManager().renderPosY;
                    double posZ = (double)tile.getPos().getZ() - this.mc.getRenderManager().renderPosZ;
                    if (tile instanceof TileEntityChest) {
                        AxisAlignedBB bb = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.94, 0.875, 0.94).offset(posX, posY, posZ);
                        TileEntityChest adjacent = null;
                        if (((TileEntityChest)tile).adjacentChestXNeg != null) {
                            adjacent = ((TileEntityChest)tile).adjacentChestXNeg;
                        }
                        if (((TileEntityChest)tile).adjacentChestXPos != null) {
                            adjacent = ((TileEntityChest)tile).adjacentChestXPos;
                        }
                        if (((TileEntityChest)tile).adjacentChestZNeg != null) {
                            adjacent = ((TileEntityChest)tile).adjacentChestZNeg;
                        }
                        if (((TileEntityChest)tile).adjacentChestZPos != null) {
                            adjacent = ((TileEntityChest)tile).adjacentChestZPos;
                        }
                        if (adjacent != null) {
                            bb = bb.union(new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.94, 0.875, 0.94).offset((double)adjacent.getPos().getX() - this.mc.getRenderManager().renderPosX, (double)adjacent.getPos().getY() - this.mc.getRenderManager().renderPosY, (double)adjacent.getPos().getZ() - this.mc.getRenderManager().renderPosZ));
                        }
                        Color normalColor = new Color((Integer)this.normal.getValue());
                        Color redColor = new Color((Integer)this.redstone.getValue());
                        if (((TileEntityChest)tile).getChestType() == 1) {
                            this.drawBlockESP(bb, redColor.getRed(), redColor.getGreen(), redColor.getBlue(), 255.0f, 1.0f);
                        } else {
                            this.drawBlockESP(bb, normalColor.getRed(), normalColor.getGreen(), normalColor.getBlue(), 255.0f, 1.0f);
                        }
                    }
                    if (!(tile instanceof TileEntityEnderChest)) continue;
                    this.drawBlockESP(new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.94, 0.875, 0.94).offset(posX, posY, posZ), this.ender.getColor().getRed(), this.ender.getColor().getGreen(), this.ender.getColor().getBlue(), 255.0f, 1.0f);
                }
                break;
            }
            case OUTLINE: {
                GlStateManager.pushAttrib();
                GL11.glPushMatrix();
                GL11.glDisable((int)3553);
                GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
                OutlineUtils.renderOne();
                this.drawChests(true);
                OutlineUtils.renderTwo();
                this.drawChests(true);
                OutlineUtils.renderThree();
                OutlineUtils.renderFour();
                this.drawChests(true);
                OutlineUtils.renderFive();
                GL11.glEnable((int)3553);
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                GL11.glPopMatrix();
                GL11.glPopAttrib();
            }
        }
    }

    private void drawBlockESP(AxisAlignedBB bb, float red, float green, float blue, float alpha, float width) {
        GL11.glPushMatrix();
        GL11.glEnable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4f((float)(red / 255.0f), (float)(green / 255.0f), (float)(blue / 255.0f), (float)0.3f);
        RenderUtil.drawBoundingBox(bb);
        GL11.glLineWidth((float)width);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public void drawChests(boolean shouldColor) {
        this.mc.theWorld.loadedTileEntityList.forEach(tileEntity -> {
            if (tileEntity instanceof TileEntityChest) {
                Color normalColor = new Color((Integer)this.normal.getValue());
                Color redColor = new Color((Integer)this.redstone.getValue());
                if (shouldColor) {
                    if (((TileEntityChest)tileEntity).getChestType() == 1) {
                        GL11.glColor3f((float)((float)redColor.getRed() / 255.0f), (float)((float)redColor.getGreen() / 255.0f), (float)((float)redColor.getBlue() / 255.0f));
                    } else {
                        GL11.glColor3f((float)((float)normalColor.getRed() / 255.0f), (float)((float)normalColor.getGreen() / 255.0f), (float)((float)normalColor.getBlue() / 255.0f));
                    }
                } else {
                    GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
                }
                TileEntityRendererDispatcher.instance.renderTileEntity((TileEntity)tileEntity, this.mc.timer.renderPartialTicks, -1);
            } else if (tileEntity instanceof TileEntityEnderChest) {
                if (shouldColor) {
                    GL11.glColor3f((float)((float)this.ender.getColor().getRed() / 255.0f), (float)((float)this.ender.getColor().getGreen() / 255.0f), (float)((float)this.ender.getColor().getBlue() / 255.0f));
                } else {
                    GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
                }
                TileEntityRendererDispatcher.instance.renderTileEntity((TileEntity)tileEntity, this.mc.timer.renderPartialTicks, -1);
            }
        });
    }

    public static enum Mode {
        BOX("Box"),
        OUTLINE("Outline"),
        SHADER("Shader"),
        CSGO("CSGO");

        private String name;

        private Mode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

