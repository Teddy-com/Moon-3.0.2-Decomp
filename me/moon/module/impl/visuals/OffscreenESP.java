/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals;

import com.google.common.collect.Maps;
import java.awt.Color;
import java.util.Map;
import me.moon.event.Handler;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.event.impl.render.Render3DEvent;
import me.moon.module.Module;
import me.moon.module.impl.combat.AntiBot;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.ColorValue;
import me.moon.utils.value.impl.NumberValue;
import me.moon.utils.vector.Vec3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class OffscreenESP
extends Module {
    private int alpha;
    private final NumberValue<Float> size = new NumberValue<Float>("Size", Float.valueOf(10.0f), Float.valueOf(5.0f), Float.valueOf(25.0f), Float.valueOf(0.1f));
    private final NumberValue<Integer> radius = new NumberValue<Integer>("Radius", 45, 10, 200, 1);
    private final BooleanValue fade = new BooleanValue("Fade", false);
    private final EntityListener entityListener = new EntityListener();
    private final BooleanValue players = new BooleanValue("Players", true);
    private final BooleanValue animals = new BooleanValue("Animals", true);
    private final BooleanValue mobs = new BooleanValue("Mobs", false);
    private final BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private final BooleanValue passives = new BooleanValue("Passives", true);
    private final BooleanValue distance = new BooleanValue("Distance Mode", false);
    private final ColorValue colorValue = new ColorValue("Color", -1, (Value)this.distance, "false");

    public OffscreenESP() {
        super("OffScreenESP", Module.Category.VISUALS, new Color(7789567).getRGB());
        this.setDescription("OffScreen ESP.");
    }

    @Override
    public void onEnable() {
        this.alpha = 0;
    }

    @Handler(value=Render3DEvent.class)
    public void onRender3D(Render3DEvent event) {
        this.entityListener.render3d(event);
    }

    @Handler(value=Render2DEvent.class)
    public void onRender2D(Render2DEvent event) {
        this.alpha = this.fade.isEnabled() ? (int)Math.max(Math.round(60.0 + (Math.sin((double)System.currentTimeMillis() * 0.003) * -0.5 + 0.5) * 195.0), 60L) : 255;
        this.mc.theWorld.loadedEntityList.forEach(o -> {
            if (o instanceof EntityLivingBase && this.isValid((EntityLivingBase)o)) {
                EntityLivingBase entity = (EntityLivingBase)o;
                Vec3 pos = this.entityListener.getEntityLowerBounds().get(entity);
                if (pos != null && !this.isOnScreen(pos)) {
                    int x = Display.getWidth() / 2 / (this.mc.gameSettings.guiScale == 0 ? 1 : this.mc.gameSettings.guiScale);
                    int y = Display.getHeight() / 2 / (this.mc.gameSettings.guiScale == 0 ? 1 : this.mc.gameSettings.guiScale);
                    float yaw = this.getRotations(entity) - this.mc.thePlayer.rotationYaw;
                    GL11.glTranslatef((float)x, (float)y, (float)0.0f);
                    GL11.glRotatef((float)yaw, (float)0.0f, (float)0.0f, (float)1.0f);
                    GL11.glTranslatef((float)(-x), (float)(-y), (float)0.0f);
                    RenderUtil.drawTracerPointer(x, y - (Integer)this.radius.getValue(), ((Float)this.size.getValue()).floatValue(), 2.0f, 1.0f, this.distance.getValue() != false ? this.getColor(entity, this.alpha).getRGB() : new Color(this.colorValue.getColor().getRed(), this.colorValue.getColor().getGreen(), this.colorValue.getColor().getBlue(), this.alpha).getRGB());
                    GL11.glTranslatef((float)x, (float)y, (float)0.0f);
                    GL11.glRotatef((float)(-yaw), (float)0.0f, (float)0.0f, (float)1.0f);
                    GL11.glTranslatef((float)(-x), (float)(-y), (float)0.0f);
                }
            }
        });
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isOnScreen(Vec3 pos) {
        if (!(pos.xCoord > -1.0)) return false;
        if (!(pos.zCoord < 1.0)) return false;
        double d = pos.xCoord;
        int n = this.mc.gameSettings.guiScale == 0 ? 1 : this.mc.gameSettings.guiScale;
        if (!(d / (double)n >= 0.0)) return false;
        double d2 = pos.xCoord;
        int n2 = this.mc.gameSettings.guiScale == 0 ? 1 : this.mc.gameSettings.guiScale;
        if (!(d2 / (double)n2 <= (double)Display.getWidth())) return false;
        double d3 = pos.yCoord;
        int n3 = this.mc.gameSettings.guiScale == 0 ? 1 : this.mc.gameSettings.guiScale;
        if (!(d3 / (double)n3 >= 0.0)) return false;
        double d4 = pos.yCoord;
        int n4 = this.mc.gameSettings.guiScale == 0 ? 1 : this.mc.gameSettings.guiScale;
        if (!(d4 / (double)n4 <= (double)Display.getHeight())) return false;
        return true;
    }

    private boolean isValid(EntityLivingBase entity) {
        return !AntiBot.getBots().contains(entity) && entity != this.mc.thePlayer && this.isValidType(entity) && entity.getEntityId() != -1488 && entity.isEntityAlive() && (!entity.isInvisible() || this.invisibles.isEnabled());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return this.players.isEnabled() && entity instanceof EntityPlayer || this.mobs.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime) || this.passives.isEnabled() && (entity instanceof EntityVillager || entity instanceof EntityGolem) || this.animals.isEnabled() && entity instanceof EntityAnimal;
    }

    private float getRotations(EntityLivingBase ent) {
        double x = ent.posX - this.mc.thePlayer.posX;
        double z = ent.posZ - this.mc.thePlayer.posZ;
        return (float)(-(Math.atan2(x, z) * 180.0 / Math.PI));
    }

    private Color getColor(EntityLivingBase player, int alpha) {
        float f = this.mc.thePlayer.getDistanceToEntity(player);
        float f1 = 40.0f;
        float f2 = Math.max(0.0f, Math.min(f, f1) / f1);
        Color clr = new Color(Color.HSBtoRGB(f2 / 3.0f, 1.0f, 1.0f) | 0xFF000000);
        return new Color(clr.getRed(), clr.getGreen(), clr.getBlue(), alpha);
    }

    public class EntityListener {
        private final Map<Entity, Vec3> entityUpperBounds = Maps.newHashMap();
        private final Map<Entity, Vec3> entityLowerBounds = Maps.newHashMap();

        private void render3d(Render3DEvent event) {
            if (!this.entityUpperBounds.isEmpty()) {
                this.entityUpperBounds.clear();
            }
            if (!this.entityLowerBounds.isEmpty()) {
                this.entityLowerBounds.clear();
            }
            for (Entity e : ((OffscreenESP)OffscreenESP.this).mc.theWorld.loadedEntityList) {
                Vec3 bound = this.getEntityRenderPosition(e);
                bound.add(new Vec3(0.0, (double)e.height + 0.2, 0.0));
                Vec3 upperBounds = RenderUtil.to2D(bound.xCoord, bound.yCoord, bound.zCoord);
                Vec3 lowerBounds = RenderUtil.to2D(bound.xCoord, bound.yCoord - 2.0, bound.zCoord);
                if (upperBounds == null || lowerBounds == null) continue;
                this.entityUpperBounds.put(e, upperBounds);
                this.entityLowerBounds.put(e, lowerBounds);
            }
        }

        private Vec3 getEntityRenderPosition(Entity entity) {
            double partial = ((OffscreenESP)OffscreenESP.this).mc.timer.renderPartialTicks;
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partial - ((OffscreenESP)OffscreenESP.this).mc.getRenderManager().viewerPosX;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partial - ((OffscreenESP)OffscreenESP.this).mc.getRenderManager().viewerPosY;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partial - ((OffscreenESP)OffscreenESP.this).mc.getRenderManager().viewerPosZ;
            return new Vec3(x, y, z);
        }

        public Map<Entity, Vec3> getEntityLowerBounds() {
            return this.entityLowerBounds;
        }
    }
}

