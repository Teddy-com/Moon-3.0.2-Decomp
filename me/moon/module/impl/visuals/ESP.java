/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  javax.vecmath.Vector4d
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.Map;
import javax.vecmath.Vector4d;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.event.impl.render.Render3DEvent;
import me.moon.module.Module;
import me.moon.module.impl.visuals.HUD;
import me.moon.utils.game.OutlineUtils;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.ColorValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import org.lwjgl.opengl.GL11;

public class ESP
extends Module {
    public final EnumValue<mode> modes = new EnumValue<mode>("Mode", mode.CSGO);
    private final BooleanValue players = new BooleanValue("Players", true);
    private final BooleanValue animals = new BooleanValue("Animals", true);
    private final BooleanValue mobs = new BooleanValue("Mobs", false);
    private final BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private final BooleanValue passives = new BooleanValue("Passives", true);
    private final BooleanValue box = new BooleanValue("Box", true);
    private static final BooleanValue health = new BooleanValue("Health", true);
    private final BooleanValue armor = new BooleanValue("Armor", true);
    private final BooleanValue filled = new BooleanValue("Filled", false);
    private final BooleanValue corner = new BooleanValue("Corner", false);
    public static final BooleanValue hudColored = new BooleanValue("Color Sync", false, (Value)health, "true");
    private final BooleanValue item = new BooleanValue("Show Item", true, (Value)this.modes, "CSGO");
    private final NumberValue<Float> thickness = new NumberValue<Float>("Thickness", Float.valueOf(1.5f), Float.valueOf(1.5f), Float.valueOf(5.0f), Float.valueOf(0.25f));
    private final NumberValue<Integer> transparency = new NumberValue<Integer>("Box Transparency", 155, 1, 255, 1);
    public final BooleanValue shader = new BooleanValue("Shader", true);
    public final BooleanValue blur = new BooleanValue("Blur", false, (Value)this.modes, "CSGO");
    public final ColorValue color = new ColorValue("Color", new Color(240, 177, 175).getRGB());

    public ESP() {
        super("ESP", Module.Category.VISUALS, new Color(175, 240, 238).getRGB());
    }

    @Handler(value=Render2DEvent.class)
    public void onRender2D(Render2DEvent event) {
        this.setSuffix(((mode)((Object)this.modes.getValue())).getName());
        this.mc.theWorld.loadedEntityList.forEach(entity -> {
            EntityLivingBase ent;
            if (entity instanceof EntityLivingBase && this.isValid(ent = (EntityLivingBase)entity) && RenderUtil.isInViewFrustrum(ent)) {
                GL11.glPushMatrix();
                Vector4d finalVec = ent.projectedVec;
                if (finalVec != null) {
                    Color clr = new Color(this.getColor(ent));
                    float xPos = (float)finalVec.x;
                    float yPos = (float)finalVec.y;
                    float eWidth = (float)finalVec.z - xPos;
                    float eHeight = (float)finalVec.w - yPos;
                    if (this.modes.getValue() == mode.CSGO) {
                        EntityPlayer player;
                        if (health.isEnabled()) {
                            RenderUtil.drawBar(entity, xPos - 3.0f - ((Float)this.thickness.getValue()).floatValue() / 2.0f, (float)((double)yPos - 1.0 - (double)(((Float)this.thickness.getValue()).floatValue() / 2.0f)), 1.5f, eHeight + 2.0f + ((Float)this.thickness.getValue()).floatValue(), (int)ent.getMaxHealth() >> 1, (int)ent.getHealth() >> 1, this.getHealthColor(ent), (float)(finalVec.w + 1.0 + (double)(((Float)this.thickness.getValue()).floatValue() / 2.0f)), (float)((double)yPos - 1.0 - (double)(((Float)this.thickness.getValue()).floatValue() / 2.0f)));
                        }
                        if (this.armor.isEnabled() && entity instanceof EntityPlayer) {
                            double armorstrength = 0.0;
                            EntityPlayer player2 = (EntityPlayer)entity;
                            for (int index = 3; index >= 0; --index) {
                                ItemStack stack = player2.inventory.armorInventory[index];
                                if (stack == null) continue;
                                armorstrength += this.getArmorStrength(stack);
                            }
                            if (armorstrength > 0.0) {
                                RenderUtil.drawBarA(entity, xPos + eWidth + 1.5f + ((Float)this.thickness.getValue()).floatValue() / 2.0f, (float)((double)yPos - 1.0 - (double)(((Float)this.thickness.getValue()).floatValue() / 2.0f)), 1.5f, eHeight + 2.0f + ((Float)this.thickness.getValue()).floatValue(), ent.getTotalArmorValue() >> 1, ent.getTotalArmorValue() >> 1, -10716417, (float)(finalVec.w + 1.0 + (double)(((Float)this.thickness.getValue()).floatValue() / 2.0f)), (float)((double)yPos - 1.0 - (double)(((Float)this.thickness.getValue()).floatValue() / 2.0f)));
                            }
                        }
                        if (this.filled.isEnabled()) {
                            RenderUtil.drawRect(xPos - 0.5f, yPos - 0.5f, eWidth + 1.0f, eHeight + 1.0f, new Color(0, 0, 0, 120).getRGB());
                        }
                        if (this.box.isEnabled()) {
                            if (this.corner.isEnabled()) {
                                RenderUtil.drawCornerRect((double)xPos - 1.0 - (double)(((Float)this.thickness.getValue()).floatValue() / 2.0f), (double)yPos - 1.0 - (double)(((Float)this.thickness.getValue()).floatValue() / 2.0f), eWidth + 2.0f + ((Float)this.thickness.getValue()).floatValue(), eHeight + 2.0f + ((Float)this.thickness.getValue()).floatValue(), ((Float)this.thickness.getValue()).floatValue(), -16777216, true, 0.5);
                                RenderUtil.drawCornerRect(xPos - 0.5f - ((Float)this.thickness.getValue()).floatValue() / 2.0f, yPos - 0.5f - ((Float)this.thickness.getValue()).floatValue() / 2.0f, eWidth + 1.0f + ((Float)this.thickness.getValue()).floatValue(), eHeight + 1.0f + ((Float)this.thickness.getValue()).floatValue(), ((Float)this.thickness.getValue()).floatValue() - 1.0f, hudColored.getValue() != false ? HUD.getColorHUD() : (Integer)this.color.getValue(), false, 0.0);
                            } else {
                                RenderUtil.drawBorderedRect(xPos - ((Float)this.thickness.getValue()).floatValue() / 2.0f, yPos - ((Float)this.thickness.getValue()).floatValue() / 2.0f, eWidth + ((Float)this.thickness.getValue()).floatValue(), eHeight + ((Float)this.thickness.getValue()).floatValue(), ((Float)this.thickness.getValue()).floatValue() - 1.0f, -16777216, 0);
                                RenderUtil.drawBorderedRect((double)xPos - 0.5 - (double)(((Float)this.thickness.getValue()).floatValue() / 2.0f), (double)yPos - 0.5 - (double)(((Float)this.thickness.getValue()).floatValue() / 2.0f), eWidth + 1.0f + ((Float)this.thickness.getValue()).floatValue(), eHeight + 1.0f + ((Float)this.thickness.getValue()).floatValue(), ((Float)this.thickness.getValue()).floatValue() - 1.0f, hudColored.getValue() != false ? HUD.getColorHUD() : (Integer)this.color.getValue(), 0);
                                RenderUtil.drawBorderedRect(xPos - 1.0f - ((Float)this.thickness.getValue()).floatValue() / 2.0f, yPos - 1.0f - ((Float)this.thickness.getValue()).floatValue() / 2.0f, eWidth + 2.0f + ((Float)this.thickness.getValue()).floatValue(), eHeight + 2.0f + ((Float)this.thickness.getValue()).floatValue(), 0.5, -16777216, 0);
                            }
                        }
                        if (entity instanceof EntityPlayer && (player = (EntityPlayer)entity).getHeldItem() != null && this.item.getValue().booleanValue()) {
                            ItemStack heldItem = player.getHeldItem();
                            GL11.glPushMatrix();
                            GL11.glScalef((float)0.5f, (float)0.5f, (float)1.0f);
                            GL11.glEnable((int)3008);
                            this.mc.fontRendererObjCustom.drawOutlinedString(heldItem.getDisplayName() + " \u00a77" + heldItem.stackSize + "x", Math.round((xPos + eWidth / 2.0f - (float)this.mc.fontRendererObjCustom.getStringWidth(heldItem.getDisplayName() + heldItem.stackSize + "x") / 4.0f) * 2.0f), Math.round((yPos + eHeight + 2.0f) * 2.0f), 1.0f, -1);
                            GL11.glPopMatrix();
                        }
                    }
                }
                GL11.glPopMatrix();
            }
        });
    }

    public void blur() {
        if (!this.isEnabled()) {
            return;
        }
        this.mc.theWorld.loadedEntityList.forEach(entity -> {
            EntityLivingBase ent;
            if (entity instanceof EntityLivingBase && this.isValid(ent = (EntityLivingBase)entity) && RenderUtil.isInViewFrustrum(ent) && this.blur.getValue().booleanValue()) {
                GL11.glPushMatrix();
                Vector4d finalVec = ent.projectedVec;
                if (finalVec != null) {
                    Color clr = new Color(this.getColor(ent));
                    float xPos = (float)finalVec.x;
                    float yPos = (float)finalVec.y;
                    float eWidth = (float)finalVec.z - xPos;
                    float eHeight = (float)finalVec.w - yPos;
                    if (this.modes.getValue() == mode.CSGO) {
                        RenderUtil.drawRect(xPos - 0.5f, yPos - 0.5f, eWidth + 1.0f, eHeight + 1.0f, -1);
                    }
                }
                GL11.glPopMatrix();
            }
        });
    }

    @Handler(value=Render3DEvent.class)
    public void onRender3D(Render3DEvent event) {
        if (this.modes.getValue() == mode.CYLINDER) {
            this.mc.theWorld.loadedEntityList.forEach(entity -> {
                if (entity instanceof EntityLivingBase && this.isValid((EntityLivingBase)entity) && RenderUtil.isInViewFrustrum(entity)) {
                    int i;
                    GL11.glPushMatrix();
                    GL11.glEnable((int)3042);
                    GL11.glBlendFunc((int)770, (int)771);
                    GL11.glDisable((int)3553);
                    GL11.glDisable((int)2929);
                    GL11.glDisable((int)3008);
                    GL11.glDisable((int)2884);
                    GL11.glEnable((int)2848);
                    double x = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, event.getPartialTicks()) - this.mc.getRenderManager().renderPosX;
                    double y = RenderUtil.interpolate(entity.posY, entity.lastTickPosY, event.getPartialTicks()) - this.mc.getRenderManager().renderPosY;
                    double z = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, event.getPartialTicks()) - this.mc.getRenderManager().renderPosZ;
                    GL11.glLineWidth((float)1.0f);
                    GL11.glColor4f((float)this.color.getColor().getRed(), (float)this.color.getColor().getGreen(), (float)this.color.getColor().getBlue(), (float)0.5f);
                    GL11.glBegin((int)6);
                    for (i = 0; i <= 360; i += 30) {
                        GL11.glVertex3d((double)(x + (double)entity.width * Math.cos((double)i * Math.PI / 180.0)), (double)y, (double)(z + (double)entity.width * Math.sin((double)i * Math.PI / 180.0)));
                    }
                    GL11.glEnd();
                    GL11.glDisable((int)2848);
                    GL11.glColor4f((float)this.color.getColor().getRed(), (float)this.color.getColor().getGreen(), (float)this.color.getColor().getBlue(), (float)0.3f);
                    GL11.glBegin((int)6);
                    for (i = 0; i <= 360; i += 30) {
                        GL11.glVertex3d((double)(x + (double)entity.width * Math.cos((double)i * Math.PI / 180.0)), (double)y, (double)(z + (double)entity.width * Math.sin((double)i * Math.PI / 180.0)));
                    }
                    GL11.glEnd();
                    GL11.glBegin((int)5);
                    for (i = 0; i <= 360; i += 30) {
                        GL11.glVertex3d((double)(x + (double)entity.width * Math.cos((double)i * Math.PI / 180.0)), (double)y, (double)(z + (double)entity.width * Math.sin((double)i * Math.PI / 180.0)));
                        GL11.glVertex3d((double)(x + (double)entity.width * Math.cos((double)i * Math.PI / 180.0)), (double)(y + (double)entity.height), (double)(z + (double)entity.width * Math.sin((double)i * Math.PI / 180.0)));
                    }
                    GL11.glEnd();
                    GL11.glBegin((int)6);
                    for (i = 0; i <= 360; i += 30) {
                        GL11.glVertex3d((double)(x + (double)entity.width * Math.cos((double)i * Math.PI / 180.0)), (double)(y + (double)entity.height), (double)(z + (double)entity.width * Math.sin((double)i * Math.PI / 180.0)));
                    }
                    GL11.glEnd();
                    GL11.glColor4f((float)this.color.getColor().getRed(), (float)this.color.getColor().getGreen(), (float)this.color.getColor().getBlue(), (float)0.5f);
                    GL11.glBegin((int)6);
                    for (i = 0; i <= 360; i += 30) {
                        GL11.glVertex3d((double)(x + (double)entity.width * Math.cos((double)i * Math.PI / 180.0)), (double)(y + (double)entity.height), (double)(z + (double)entity.width * Math.sin((double)i * Math.PI / 180.0)));
                    }
                    GL11.glEnd();
                    GL11.glEnable((int)3008);
                    GL11.glEnable((int)2929);
                    GL11.glEnable((int)3553);
                    GL11.glEnable((int)2884);
                    GL11.glDisable((int)2848);
                    GL11.glDisable((int)3042);
                    GL11.glPopMatrix();
                }
            });
        }
        if (this.modes.getValue() == mode.XAVUSDOPUS) {
            this.mc.theWorld.loadedEntityList.forEach(entity -> {
                EntityLivingBase ent;
                if (entity instanceof EntityLivingBase && this.isValid(ent = (EntityLivingBase)entity) && RenderUtil.isInViewFrustrum(ent)) {
                    this.setupFor2D(ent);
                    double w = 17.0;
                    double h = 30.0;
                    int width = 70;
                    int height = 120;
                    int x = -33;
                    int y = -108;
                    int hex = -1442840576;
                    float lineSize = 3.0f;
                    boolean border = false;
                    float borderWidth = 0.5f;
                    double thickness1 = 3.0;
                    if (this.box.isEnabled()) {
                        RenderUtil.drawRect(-33.0, -108.5, 17.0, 3.0, -1442840576);
                        double x1 = 20.0;
                        RenderUtil.drawRect(16.1, -108.5, 17.0, 3.0, -1442840576);
                        RenderUtil.drawRect(-33.0, 6.0, 17.0, 3.0, -1442840576);
                        RenderUtil.drawRect(16.1, 5.5, 17.0, 3.0, -1442840576);
                        RenderUtil.drawRect(-33.5, -108.0, 3.0, 30.0, -1442840576);
                        RenderUtil.drawRect(30.5, -108.0, 3.0, 30.0, -1442840576);
                        double y1 = -18.0;
                        RenderUtil.drawRect(-33.5, -21.5, 3.0, 30.0, -1442840576);
                        RenderUtil.drawRect(30.5, -21.5, 3.0, 30.0, -1442840576);
                        RenderUtil.drawCornerRect(-35.0, -110.0, 70.0, 120.0, 3.0, (Integer)this.color.getValue(), false, 0.0);
                    }
                    if (health.isEnabled()) {
                        RenderUtil.drawBar(entity, -40.0f - ((Float)this.thickness.getValue()).floatValue() / 2.0f, -111.0f, 3.0f, 122.0f, (int)ent.getMaxHealth() >> 1, (int)ent.getHealth() >> 1, this.getHealthColor(ent), 17.0, 30.0);
                    }
                    if (this.armor.isEnabled() && entity instanceof EntityPlayer) {
                        double armorstrength = 0.0;
                        EntityPlayer player = (EntityPlayer)entity;
                        for (int index = 3; index >= 0; --index) {
                            ItemStack stack = player.inventory.armorInventory[index];
                            if (stack == null) continue;
                            armorstrength += this.getArmorStrength(stack);
                        }
                        if (armorstrength > 0.0) {
                            RenderUtil.drawBarA(entity, 37.0f + ((Float)this.thickness.getValue()).floatValue() / 2.0f, -111.0f, 3.0f, 122.0f, 4.0f, (int)(Math.min(armorstrength, 40.0) / 10.0), -10716417, 17.0, 30.0);
                        }
                    }
                    this.disableFor2D();
                }
            });
        }
        if (this.modes.getValue() == mode.BOX) {
            this.mc.theWorld.loadedEntityList.forEach(entity -> {
                EntityLivingBase target;
                if (entity instanceof EntityLivingBase && this.isValid(target = (EntityLivingBase)entity) && RenderUtil.isInViewFrustrum(target)) {
                    double x = RenderUtil.interpolate(target.posX, target.lastTickPosX, event.getPartialTicks());
                    double y = RenderUtil.interpolate(target.posY, target.lastTickPosY, event.getPartialTicks());
                    double z = RenderUtil.interpolate(target.posZ, target.lastTickPosZ, event.getPartialTicks());
                    float width = target.width;
                    float height = target.height + 0.1f;
                    GL11.glPushMatrix();
                    GlStateManager.pushAttrib();
                    GL11.glDisable((int)3553);
                    GL11.glLineWidth((float)1.8f);
                    if (this.filled.getValue().booleanValue()) {
                        RenderUtil.BB(new AxisAlignedBB(x - this.mc.getRenderManager().renderPosX - (double)width + 0.25, y - this.mc.getRenderManager().renderPosY, z - this.mc.getRenderManager().renderPosZ - (double)width + 0.25, x - this.mc.getRenderManager().renderPosX + (double)width - 0.25, y - this.mc.getRenderManager().renderPosY + (double)height, z - this.mc.getRenderManager().renderPosZ + (double)width - 0.25), new Color(this.color.getColor().getRed(), this.color.getColor().getGreen(), this.color.getColor().getBlue(), (Integer)this.transparency.getValue()).getRGB());
                    }
                    if (this.corner.getValue().booleanValue()) {
                        RenderUtil.OutlinedBB(new AxisAlignedBB(x - this.mc.getRenderManager().renderPosX - (double)width + 0.25, y - this.mc.getRenderManager().renderPosY, z - this.mc.getRenderManager().renderPosZ - (double)width + 0.25, x - this.mc.getRenderManager().renderPosX + (double)width - 0.25, y - this.mc.getRenderManager().renderPosY + (double)height, z - this.mc.getRenderManager().renderPosZ + (double)width - 0.25), 1.0f, new Color(this.color.getColor().getRed(), this.color.getColor().getGreen(), this.color.getColor().getBlue(), 255).getRGB());
                    }
                    GL11.glEnable((int)3553);
                    GL11.glPopMatrix();
                    GL11.glPopAttrib();
                    GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                }
            });
        }
        if (this.modes.getValue() == mode.BOUNDINGBOX) {
            GL11.glPushMatrix();
            GL11.glDisable((int)3553);
            GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
            this.drawBoundingBoxOfEntitys(event);
            OutlineUtils.renderOne();
            this.drawBoundingBoxOfEntitys(event);
            OutlineUtils.renderTwo();
            this.drawBoundingBoxOfEntitys(event);
            OutlineUtils.renderThree();
            OutlineUtils.renderFour();
            this.drawBoundingBoxOfEntitys(event);
            OutlineUtils.renderFive();
            GL11.glEnable((int)3553);
            GlStateManager.resetColor();
            GL11.glPopMatrix();
        }
    }

    private void setupFor2D(EntityLivingBase entity) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((double)((float)entity.lastTickPosX) + (entity.posX - entity.lastTickPosX) * (double)this.mc.timer.renderPartialTicks - this.mc.getRenderManager().renderPosX, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)this.mc.timer.renderPartialTicks - this.mc.getRenderManager().renderPosY, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)this.mc.timer.renderPartialTicks - this.mc.getRenderManager().renderPosZ);
        GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-0.018f, -0.018f, 0.018f);
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
    }

    private void disableFor2D() {
        GlStateManager.enableDepth();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    public boolean isValid(EntityLivingBase entity) {
        return this.mc.thePlayer != entity && this.isValidType(entity) && entity instanceof EntityLivingBase && (!entity.isInvisible() || this.invisibles.isEnabled());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return this.players.isEnabled() && entity instanceof EntityPlayer || this.mobs.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime) || this.passives.isEnabled() && (entity instanceof EntityVillager || entity instanceof EntityGolem) || this.animals.isEnabled() && entity instanceof EntityAnimal;
    }

    private int getColor(EntityLivingBase ent) {
        if (Moon.INSTANCE.getFriendManager().isFriend(ent.getName())) {
            return new Color(122, 190, 255).getRGB();
        }
        if (ent.getName().equals(this.mc.thePlayer.getName())) {
            return new Color(-6684775).getRGB();
        }
        return (Integer)this.color.getValue();
    }

    private int getHealthColor(EntityLivingBase player) {
        return Color.HSBtoRGB(Math.max(0.0f, Math.min(player.getHealth(), player.getMaxHealth()) / player.getMaxHealth()) / 3.0f, 1.0f, 0.8f) | 0xFF000000;
    }

    private double getArmorStrength(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemArmor)) {
            return -1.0;
        }
        float damageReduction = ((ItemArmor)itemStack.getItem()).damageReduceAmount;
        Map<Integer, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        if (enchantments.containsKey(Enchantment.protection.effectId)) {
            int level = enchantments.get(Enchantment.protection.effectId);
            damageReduction += (float)Enchantment.protection.calcModifierDamage(level, DamageSource.generic);
        }
        return damageReduction;
    }

    public String getModuleString(Module module) {
        StringBuilder moduleString = new StringBuilder(module.getRenderLabel() != null ? module.getRenderLabel() : module.getLabel());
        if (module.getSuffix() != null) {
            moduleString.append(ChatFormatting.GRAY).append(" ").append(module.getSuffix());
        }
        return moduleString.toString();
    }

    public void drawBoundingBoxOfEntitys(Render3DEvent event) {
        this.mc.theWorld.loadedEntityList.forEach(entity -> {
            EntityLivingBase target;
            if (entity instanceof EntityLivingBase && this.isValid(target = (EntityLivingBase)entity) && RenderUtil.isInViewFrustrum(target)) {
                double x = RenderUtil.interpolate(target.posX, target.lastTickPosX, event.getPartialTicks());
                double y = RenderUtil.interpolate(target.posY, target.lastTickPosY, event.getPartialTicks());
                double z = RenderUtil.interpolate(target.posZ, target.lastTickPosZ, event.getPartialTicks());
                float width = target.width;
                float height = target.height + 0.1f;
                double x1 = x - this.mc.getRenderManager().renderPosX - (double)width + 0.25;
                double y1 = y - this.mc.getRenderManager().renderPosY;
                double z1 = z - this.mc.getRenderManager().renderPosZ - (double)width + 0.25;
                double x2 = x - this.mc.getRenderManager().renderPosX + (double)width - 0.25;
                double y2 = y - this.mc.getRenderManager().renderPosY + (double)height;
                double z2 = z - this.mc.getRenderManager().renderPosZ + (double)width - 0.25;
                RenderUtil.drawBoundingBox(new AxisAlignedBB(x1, y1, z1, x2, y2, z2));
            }
        });
    }

    public void drawEntitys() {
        GlStateManager.depthMask(false);
        GlStateManager.disableLighting();
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        this.mc.theWorld.loadedEntityList.forEach(entity -> {
            EntityLivingBase target;
            if (entity instanceof EntityLivingBase && this.isValid(target = (EntityLivingBase)entity) && RenderUtil.isInViewFrustrum(target)) {
                this.mc.getRenderManager().renderEntitySimple((Entity)entity, this.mc.timer.renderPartialTicks);
            }
        });
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
    }

    public static enum mode {
        XTASY("Xtasy"),
        CSGO("CSGO"),
        XAVUSDOPUS("Xavus Dopus"),
        BOX("Box"),
        BOUNDINGBOX("Bounding Box"),
        CYLINDER("Cylinder");

        private final String name;

        private mode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

