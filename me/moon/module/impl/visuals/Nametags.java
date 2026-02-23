/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector4d
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals;

import java.awt.Color;
import javax.vecmath.Vector4d;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.render.NameplateEvent;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.Module;
import me.moon.module.impl.other.StreamerMode;
import me.moon.module.impl.visuals.HUD;
import me.moon.module.impl.visuals.IRC;
import me.moon.utils.MathUtils;
import me.moon.utils.font.Fonts;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.BooleanValue;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class Nametags
extends Module {
    private final BooleanValue players = new BooleanValue("Players", true);
    private final BooleanValue animals = new BooleanValue("Animals", true);
    private final BooleanValue mobs = new BooleanValue("Mobs", false);
    private final BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private final BooleanValue passives = new BooleanValue("Passives", true);
    private final BooleanValue font = new BooleanValue("Font", true);
    private final BooleanValue background = new BooleanValue("Background", true);
    private final BooleanValue distance = new BooleanValue("Distance", false);
    private final BooleanValue health = new BooleanValue("Health", true);
    private final BooleanValue healthRect = new BooleanValue("Health Rect", true);
    private final BooleanValue outlinedText = new BooleanValue("Outlined Text", false);
    private final BooleanValue hudColored = new BooleanValue("Color Sync", true);
    private final BooleanValue dandelion = new BooleanValue("Dandelion", false);

    public Nametags() {
        super("Nametags", Module.Category.VISUALS, new Color(240, 177, 175).getRGB());
    }

    @Handler(value=Render2DEvent.class)
    public void onRender2D(Render2DEvent event) {
        GlStateManager.bindTexture(0);
        this.mc.theWorld.loadedEntityList.forEach(entity -> {
            EntityLivingBase ent;
            if (entity instanceof EntityLivingBase && this.isValid(ent = (EntityLivingBase)entity) && entity.getUniqueID() != this.mc.thePlayer.getUniqueID() && RenderUtil.isInViewFrustrum(ent)) {
                GL11.glPushMatrix();
                Vector4d position = ent.projectedVec;
                this.mc.entityRenderer.setupOverlayRendering();
                if (position != null) {
                    float textHeight;
                    String healthText;
                    float xPos = (float)position.x;
                    float yPos = (float)position.y;
                    float width = (float)position.z - xPos;
                    FontRenderer fontRenderer = this.dandelion.getValue() != false ? this.mc.fontRendererObjCustom : this.mc.fontRendererObj;
                    String nameText = "\u00a77" + (this.distance.getValue() != false ? Math.round(this.mc.thePlayer.getDistanceToEntity(ent)) + "m\u00a7f " : (Moon.INSTANCE.getFriendManager().isFriend(ent.getName()) ? "\u00a7b" : "\u00a7f")) + ent.getName();
                    if (this.hudColored.getValue().booleanValue()) {
                        String string = nameText = this.distance.getValue() != false ? Math.round(this.mc.thePlayer.getDistanceToEntity(ent)) + "m " + ent.getName() : ent.getName();
                    }
                    String string = this.health.getValue() != false ? " " + (!Float.isNaN(ent.getHealth()) ? Float.valueOf(MathUtils.roundFloat(ent.getHealth(), 1)) : "") : (healthText = "NaN");
                    if (Moon.INSTANCE.getModuleManager().getModule("StreamerMode").isEnabled() && StreamerMode.hideNames.getValue().booleanValue()) {
                        nameText = this.font.getValue() != false ? (this.hudColored.getValue() != false ? "" : "\u00a77") + (this.distance.getValue() != false ? Math.round(this.mc.thePlayer.getDistanceToEntity(ent)) + "m " + (this.hudColored.getValue() != false ? "" : "\u00a7f") + "Protected" : (this.hudColored.getValue() != false ? "" : "\u00a7f") + "Protected") : (this.hudColored.getValue() != false ? "" : "\u00a77") + (this.distance.getValue() != false ? Math.round(this.mc.thePlayer.getDistanceToEntity(ent)) + "m " + (this.hudColored.getValue() != false ? "" : "\u00a7f") + "\u00a7k" + ent.getName() + "\u00a7f" : (this.hudColored.getValue() != false ? "" : "\u00a7f") + "\u00a7k" + ent.getName() + "\u00a7f");
                    }
                    IRC irc = (IRC)Moon.INSTANCE.getModuleManager().getModule("IRC");
                    float f = textHeight = this.font.getValue() != false ? (float)Fonts.moonSmall.getHeight() : (float)fontRenderer.FONT_HEIGHT;
                    float textWidth = this.font.getValue() != false ? (float)Fonts.moonSmall.getStringWidth(nameText + (this.health.isEnabled() ? healthText : "")) : (float)fontRenderer.getStringWidth(nameText + (this.health.isEnabled() ? healthText : ""));
                    float nameTextWidth = this.font.getValue() != false ? (float)Fonts.moonSmall.getStringWidth(nameText) : (float)fontRenderer.getStringWidth(nameText);
                    float nameTagX = (xPos + width / 2.0f - textWidth / 4.0f) / 0.5f;
                    float nameTagY = (yPos - textHeight + 1.0f + (this.background.getValue() == false && this.healthRect.getValue() == false ? 1.0f : 0.0f)) / 0.5f;
                    if (this.font.getValue().booleanValue()) {
                        nameTagX = xPos + width / 2.0f - textWidth / 2.0f;
                        nameTagY = yPos - textHeight - 5.0f + (this.background.getValue() == false && this.healthRect.getValue() == false ? 1.0f : 0.0f);
                    }
                    GlStateManager.pushMatrix();
                    if (!this.font.getValue().booleanValue()) {
                        GlStateManager.scale(0.5f, 0.5f, 1.0f);
                    }
                    if (this.background.getValue().booleanValue()) {
                        RenderUtil.drawRect(nameTagX - 2.0f, nameTagY - 2.0f, textWidth + 4.0f, textHeight + (float)(this.healthRect.getValue() != false && !Float.isNaN(ent.getHealth()) ? 5 : 4), 0x67000000);
                    }
                    if (this.healthRect.getValue().booleanValue() && !Float.isNaN(ent.getHealth())) {
                        RenderUtil.drawRect(nameTagX - 2.0f, nameTagY + (this.font.getValue() != false ? 2.5f : 2.0f) + textHeight, (textWidth + 4.0f) * (ent.getHealth() / ent.getMaxHealth()), this.font.getValue() != false ? 0.5 : 1.0, this.getHealthColor(ent));
                    }
                    if (this.font.getValue().booleanValue()) {
                        if (this.outlinedText.getValue().booleanValue()) {
                            Fonts.moonSmall.drawOutlinedString(nameText, nameTagX, nameTagY + 1.0f, this.hudColored.getValue() != false ? HUD.getColorHUD() : -1);
                            if (this.health.getValue().booleanValue()) {
                                Fonts.moonSmall.drawOutlinedString(healthText, nameTagX + nameTextWidth + 0.5f, nameTagY + 1.0f, this.getHealthColor(ent));
                            }
                        } else {
                            Fonts.moonSmall.drawStringWithShadow(nameText, nameTagX, nameTagY + 1.0f, this.hudColored.getValue() != false ? HUD.getColorHUD() : -1);
                            if (this.health.getValue().booleanValue()) {
                                Fonts.moonSmall.drawStringWithShadow(healthText, nameTagX + nameTextWidth + 0.5f, nameTagY + 1.0f, this.getHealthColor(ent));
                            }
                        }
                    } else if (this.outlinedText.getValue().booleanValue()) {
                        fontRenderer.drawOutlinedString(nameText, nameTagX, nameTagY + 1.0f, 1.0f, this.hudColored.getValue() != false ? HUD.getColorHUD() : -1);
                        if (this.health.getValue().booleanValue()) {
                            fontRenderer.drawOutlinedString(healthText, nameTagX + nameTextWidth, nameTagY + 1.0f, 1.0f, this.getHealthColor(ent));
                        }
                    } else {
                        fontRenderer.drawStringWithShadow(nameText, nameTagX, nameTagY + 1.0f, this.hudColored.getValue() != false ? HUD.getColorHUD() : -1);
                        if (this.health.getValue().booleanValue()) {
                            fontRenderer.drawStringWithShadow(healthText, nameTagX + nameTextWidth, nameTagY + 1.0f, this.getHealthColor(ent));
                        }
                    }
                    GlStateManager.popMatrix();
                }
                GL11.glPopMatrix();
                GlStateManager.resetColor();
            }
        });
    }

    @Handler(value=NameplateEvent.class)
    public void onNamePlate(NameplateEvent event) {
        event.setCancelled(event.getEntity() instanceof EntityLivingBase && this.isValid((EntityLivingBase)event.getEntity()) || event.getEntity() instanceof EntityOtherPlayerMP);
    }

    public boolean isValid(EntityLivingBase entity) {
        return this.mc.thePlayer != entity && entity.getEntityId() != -1488 && this.isValidType(entity) && entity instanceof EntityLivingBase && (!entity.isInvisible() || this.invisibles.isEnabled());
    }

    public boolean isValidType(EntityLivingBase entity) {
        return this.players.isEnabled() && entity instanceof EntityPlayer || this.mobs.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime) || this.passives.isEnabled() && (entity instanceof EntityVillager || entity instanceof EntityGolem) || this.animals.isEnabled() && entity instanceof EntityAnimal;
    }

    private String healthCode(EntityLivingBase entityLivingBase) {
        if (entityLivingBase.getMaxHealth() / 1.5f <= entityLivingBase.getHealth()) {
            return "\u00a7a";
        }
        if (entityLivingBase.getMaxHealth() / 2.0f <= entityLivingBase.getHealth()) {
            return "\u00a7e";
        }
        if (entityLivingBase.getMaxHealth() / 3.0f <= entityLivingBase.getHealth()) {
            return "\u00a76";
        }
        if (entityLivingBase.getMaxHealth() / 4.0f <= entityLivingBase.getHealth()) {
            return "\u00a7c";
        }
        return "\u00a74";
    }

    private int getHealthColor(EntityLivingBase player) {
        return Color.HSBtoRGB(Math.max(0.0f, Math.min(player.getHealth(), player.getMaxHealth()) / player.getMaxHealth()) / 3.0f, 1.0f, 0.8f) | 0xFF000000;
    }
}

