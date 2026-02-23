/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals;

import java.awt.Color;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.render.Render3DEvent;
import me.moon.module.Module;
import me.moon.module.impl.combat.AntiBot;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class Tracers
extends Module {
    private final BooleanValue players = new BooleanValue("Players", true);
    private final BooleanValue animals = new BooleanValue("Animals", true);
    private final BooleanValue mobs = new BooleanValue("Mobs", false);
    private final BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private final BooleanValue passives = new BooleanValue("Passives", true);
    private final NumberValue<Float> width = new NumberValue<Float>("Width", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(10.0f), Float.valueOf(1.0f));

    public Tracers() {
        super("Tracers", Module.Category.VISUALS, new Color(-9310611).getRGB());
        this.setDescription("Lines to players");
        this.setHidden(true);
    }

    @Handler(value=Render3DEvent.class)
    public void onRender3D(Render3DEvent event) {
        for (Entity entity : this.mc.theWorld.getLoadedEntityList()) {
            if (!(entity instanceof EntityLivingBase) || !this.isValid((EntityLivingBase)entity)) continue;
            this.trace(entity, ((Float)this.width.getValue()).floatValue(), Moon.INSTANCE.getFriendManager().isFriend(entity.getName()) ? new Color(32, 128, 255) : new Color(255, 255, 255), Minecraft.getMinecraft().timer.renderPartialTicks);
        }
    }

    private boolean isValid(EntityLivingBase entity) {
        return !AntiBot.getBots().contains(entity) && this.mc.thePlayer != entity && entity.getEntityId() != -1488 && this.isValidType(entity) && entity.isEntityAlive() && (!entity.isInvisible() || this.invisibles.isEnabled());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return this.players.isEnabled() && entity instanceof EntityPlayer || this.mobs.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime) || this.passives.isEnabled() && (entity instanceof EntityVillager || entity instanceof EntityGolem) || this.animals.isEnabled() && entity instanceof IAnimals;
    }

    private void trace(Entity entity, float width, Color color, float partialTicks) {
        float r = 0.003921569f * (float)color.getRed();
        float g = 0.003921569f * (float)color.getGreen();
        float b = 0.003921569f * (float)color.getBlue();
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        this.mc.entityRenderer.orientCamera(partialTicks);
        GL11.glDisable((int)2929);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        double x = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, partialTicks) - this.mc.getRenderManager().viewerPosX;
        double y = RenderUtil.interpolate(entity.posY, entity.lastTickPosY, partialTicks) - this.mc.getRenderManager().viewerPosY;
        double z = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks) - this.mc.getRenderManager().viewerPosZ;
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)3);
        GL11.glColor3d((double)r, (double)g, (double)b);
        GL11.glVertex3d((double)x, (double)y, (double)z);
        GL11.glVertex3d((double)0.0, (double)this.mc.thePlayer.getEyeHeight(), (double)0.0);
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glPopMatrix();
    }
}

