/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.movement;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.render.Render3DEvent;
import me.moon.module.Module;
import me.moon.module.impl.combat.AntiBot;
import me.moon.module.impl.combat.KillAura;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class TargetStrafe
extends Module {
    public EntityLivingBase target;
    private List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
    public static NumberValue<Float> distance = new NumberValue<Float>("Distance", Float.valueOf(3.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), Float.valueOf(0.1f));
    public static NumberValue<Float> angle = new NumberValue<Float>("Angle", Float.valueOf(20.0f), Float.valueOf(10.0f), Float.valueOf(90.0f), Float.valueOf(5.0f));
    private final BooleanValue players = new BooleanValue("Players", "Target Players", true);
    private final BooleanValue animals = new BooleanValue("Animals", "Target Animals", false);
    private final BooleanValue monsters = new BooleanValue("Monsters", "Target Monsters", false);
    private final BooleanValue invisibles = new BooleanValue("Invisibles", "Target Invisibles", false);
    public static BooleanValue spaceOnly = new BooleanValue("Press Space only", "Press Space only", false);
    public static BooleanValue render = new BooleanValue("Render Circle", "Renders the Circle", false);

    public TargetStrafe() {
        super("TargetStrafe", Module.Category.MOVEMENT, -5370194);
    }

    @Override
    public void onEnable() {
        if (this.mc.thePlayer == null) {
            return;
        }
    }

    @Override
    public void onDisable() {
        if (this.mc.thePlayer == null) {
            return;
        }
        this.mc.timer.timerSpeed = 1.0f;
    }

    @Handler(value=Render3DEvent.class)
    public void onRender3D(Render3DEvent event) {
        if (this.mc.theWorld == null || this.mc.thePlayer == null) {
            return;
        }
        for (Entity entity : this.mc.theWorld.getLoadedEntityList()) {
            if (entity != KillAura.target) continue;
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            if (entityLivingBase.isDead || entityLivingBase == this.mc.thePlayer || !this.isTargetable(entityLivingBase, this.mc.thePlayer) || !(entity.getDistanceToEntity(this.mc.thePlayer) < 5.0f) || !render.getValue().booleanValue()) continue;
            this.drawCirle(entity, 3.0f, this.target != null && entity == this.target ? new Color(255, 72, 67) : new Color(255, 255, 255), event.getPartialTicks());
        }
    }

    private void drawCirle(Entity entity, float width, Color color, float partialTicks) {
        int i;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glEnable((int)2848);
        double x = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, partialTicks) - this.mc.getRenderManager().renderPosX;
        double y = RenderUtil.interpolate(entity.posY, entity.lastTickPosY, partialTicks) - this.mc.getRenderManager().renderPosY;
        double z = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks) - this.mc.getRenderManager().renderPosZ;
        GL11.glLineWidth((float)4.0f);
        float r = 0.003921569f * (float)color.getRed();
        float g = 0.003921569f * (float)color.getGreen();
        float b = 0.003921569f * (float)color.getBlue();
        GL11.glColor3f((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glBegin((int)2);
        for (i = 0; i <= 360; ++i) {
            GL11.glVertex3d((double)(x + (double)((Float)distance.getValue()).floatValue() * Math.cos((double)i * Math.PI / 180.0)), (double)y, (double)(z + (double)((Float)distance.getValue()).floatValue() * Math.sin((double)i * Math.PI / 180.0)));
        }
        GL11.glEnd();
        GL11.glLineWidth((float)2.0f);
        GL11.glColor3f((float)r, (float)g, (float)b);
        GL11.glBegin((int)2);
        for (i = 0; i <= 360; ++i) {
            GL11.glVertex3d((double)(x + (double)((Float)distance.getValue()).floatValue() * Math.cos((double)i * Math.PI / 180.0)), (double)y, (double)(z + (double)((Float)distance.getValue()).floatValue() * Math.sin((double)i * Math.PI / 180.0)));
        }
        GL11.glEnd();
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    private boolean isTargetable(EntityLivingBase entity, EntityPlayerSP clientPlayer) {
        return entity.getUniqueID() != clientPlayer.getUniqueID() && entity.isEntityAlive() && !AntiBot.getBots().contains(entity) && !Moon.INSTANCE.getFriendManager().isFriend(entity.getName()) && (!entity.isInvisible() || this.invisibles.isEnabled()) && (entity instanceof EntityPlayer && this.players.isEnabled() || (entity instanceof EntityMob || entity instanceof EntityGolem) && this.monsters.isEnabled() || entity instanceof IAnimals && this.animals.isEnabled());
    }

    private static enum sortmode {
        FOV,
        HEALTH,
        DISTANCE;

    }
}

