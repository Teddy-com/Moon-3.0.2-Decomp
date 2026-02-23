/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.utils.render;

import java.util.Random;
import me.moon.module.impl.visuals.ItemPhysics;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

public class ItemPhysic {
    private static long tick;
    public static double rotation;
    public static Random random;
    public static RenderItem renderItem;

    public static void doRender(Entity par1Entity, double x, double y, double z, float par8, float par9) {
        EntityItem item;
        ItemStack itemstack;
        tick = 0L;
        rotation = (double)(System.nanoTime() - tick) / 3000000.0 * (1.0 + (double)((Float)ItemPhysics.speed.getValue()).floatValue());
        if (!Minecraft.getMinecraft().inGameHasFocus) {
            rotation = 0.0;
        }
        if ((itemstack = (item = (EntityItem)par1Entity).getEntityItem()).getItem() != null) {
            random.setSeed(187L);
            boolean flag = false;
            if (TextureMap.locationBlocksTexture != null) {
                Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
                Minecraft.getMinecraft().getRenderManager().renderEngine.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
                flag = true;
            }
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(516, 0.1f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.pushMatrix();
            IBakedModel ibakedmodel = renderItem.getItemModelMesher().getItemModel(itemstack);
            int i = ItemPhysic.func_177077_a(item, x, y, z, par9, ibakedmodel);
            BlockPos pos = new BlockPos(item);
            if (item.rotationPitch > 360.0f) {
                item.rotationPitch = 0.0f;
            }
            if (!(Double.isNaN(item.posX) || Double.isNaN(item.posY) || Double.isNaN(item.posZ) || item.worldObj == null)) {
                if (item.onGround) {
                    if (item.rotationPitch != 0.0f && item.rotationPitch != 45.0f && item.rotationPitch != 120.0f && item.rotationPitch != 360.0f) {
                        float f;
                        double dist0 = ItemPhysic.formPositiv(item.rotationPitch);
                        double dist90 = ItemPhysic.formPositiv(item.rotationPitch - 45.0f);
                        double dist180 = ItemPhysic.formPositiv(item.rotationPitch - 120.0f);
                        double dist270 = ItemPhysic.formPositiv(item.rotationPitch - 360.0f);
                        if (dist0 <= dist90 && dist0 <= dist180 && dist0 <= dist270) {
                            item.rotationPitch = item.rotationPitch < 0.0f ? (float)((double)item.rotationPitch + rotation) : (float)((double)item.rotationPitch - rotation);
                            f = item.rotationPitch;
                        }
                        if (dist90 < dist0 && dist90 <= dist180 && dist90 <= dist270) {
                            item.rotationPitch = item.rotationPitch - 45.0f < 0.0f ? (float)((double)item.rotationPitch + rotation) : (float)((double)item.rotationPitch - rotation);
                            f = item.rotationPitch;
                        }
                        if (dist180 < dist90 && dist180 < dist0 && dist180 <= dist270) {
                            item.rotationPitch = item.rotationPitch - 120.0f < 0.0f ? (float)((double)item.rotationPitch + rotation) : (float)((double)item.rotationPitch - rotation);
                            f = item.rotationPitch;
                        }
                        if (dist270 < dist90 && dist270 < dist180 && dist270 < dist0) {
                            item.rotationPitch = item.rotationPitch - 360.0f < 0.0f ? (float)((double)item.rotationPitch + rotation) : (float)((double)item.rotationPitch - rotation);
                        }
                    }
                } else {
                    BlockPos posUp = new BlockPos(item);
                    posUp.add(0, 1, 0);
                    Material m1 = item.worldObj.getBlockState(posUp).getBlock().getMaterial();
                    Material m2 = item.worldObj.getBlockState(pos).getBlock().getMaterial();
                    boolean m3 = item.isInsideOfMaterial(Material.water);
                    boolean m4 = item.inWater;
                    item.rotationPitch = m3 | m1 == Material.water | m2 == Material.water | m4 ? (float)((double)item.rotationPitch + rotation / 2.0) : (float)((double)item.rotationPitch + rotation * 1.0);
                }
            }
            GL11.glRotatef((float)item.rotationYaw, (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glRotatef((float)(item.rotationPitch + 90.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            for (int j = 0; j < i; ++j) {
                if (ibakedmodel.isAmbientOcclusion()) {
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(0.5f, 0.5f, 0.5f);
                    renderItem.renderItem(itemstack, ibakedmodel);
                    GlStateManager.popMatrix();
                    continue;
                }
                GlStateManager.pushMatrix();
                if (j > 0) {
                    GlStateManager.translate(0.0f, 0.0f, 1.046875f * (float)j);
                }
                renderItem.renderItem(itemstack, ibakedmodel);
                GlStateManager.translate(0.0f, 0.0f, 1.046875f);
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            if (flag) {
                Minecraft.getMinecraft().getRenderManager().renderEngine.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
            }
        }
    }

    private static int func_177077_a(EntityItem item, double x, double y, double z, float p_177077_8_, IBakedModel p_177077_9_) {
        ItemStack itemstack = item.getEntityItem();
        Item item2 = itemstack.getItem();
        if (item2 == null) {
            return 0;
        }
        boolean flag = p_177077_9_.isAmbientOcclusion();
        int i = ItemPhysic.func_177078_a(itemstack);
        float f2 = 0.0f;
        GlStateManager.translate((float)x, (float)y + f2 + 0.25f, (float)z);
        float f3 = 0.0f;
        if (flag || Minecraft.getMinecraft().getRenderManager().renderEngine != null && Minecraft.getMinecraft().gameSettings.fancyGraphics) {
            GlStateManager.rotate(f3, 0.0f, 1.0f, 0.0f);
        }
        if (!flag) {
            f3 = -0.0f * (float)(i - 1) * 0.5f;
            float f4 = -0.0f * (float)(i - 1) * 0.5f;
            float f5 = -0.046875f * (float)(i - 1) * 0.5f;
            GlStateManager.translate(f3, f4, f5);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        return i;
    }

    private static double formPositiv(float rotationPitch) {
        if (rotationPitch > 0.0f) {
            return rotationPitch;
        }
        return -rotationPitch;
    }

    private static int func_177078_a(ItemStack stack) {
        if (stack.animationsToGo > 48) {
            return 5;
        }
        if (stack.animationsToGo > 32) {
            return 4;
        }
        if (stack.animationsToGo > 16) {
            return 3;
        }
        if (stack.animationsToGo > 1) {
            return 2;
        }
        return 1;
    }

    static {
        random = new Random();
        renderItem = Minecraft.getMinecraft().getRenderItem();
    }
}

