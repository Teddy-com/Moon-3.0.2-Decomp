/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.event.impl.render.Render3DEvent;
import me.moon.module.Module;
import me.moon.module.impl.combat.AntiBot;
import me.moon.utils.render.GLUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class BowAimbot
extends Module {
    private EntityLivingBase target;
    private final List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
    private final BooleanValue players = new BooleanValue("Players", "Target Players", true);
    private final BooleanValue animals = new BooleanValue("Animals", "Target Animals", false);
    private final BooleanValue mobs = new BooleanValue("Mobs", "Target Mobs", false);
    private final BooleanValue invisibles = new BooleanValue("Invisibles", "Target Invisibles", false);
    private final EnumValue<SortMode> sortingmode = new EnumValue<SortMode>("SortMode", SortMode.FOV);
    public NumberValue<Float> range = new NumberValue<Float>("Range", Float.valueOf(70.0f), Float.valueOf(1.0f), Float.valueOf(150.0f), Float.valueOf(0.1f));

    public BowAimbot() {
        super("BowAimbot", Module.Category.COMBAT, new Color(15945286).getRGB());
        this.setDescription("Automatically aim at players while shooting a bow.");
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemBow && Mouse.isButtonDown((int)1)) {
            this.target = this.getBestTarget(event.getYaw());
            if (this.target != null && event.isPre()) {
                double v = 3.0;
                double g = 0.05f;
                float pitch = (float)(-Math.toDegrees(this.getLaunchAngle(this.target, 3.0, 0.05f)));
                if (Double.isNaN(pitch)) {
                    return;
                }
                Vec3 pos = BowAimbot.predictPos(this.target, 11.0f);
                double difX = pos.xCoord - this.mc.thePlayer.posX;
                double difZ = pos.zCoord - this.mc.thePlayer.posZ;
                float yaw = (float)(Math.atan2(difZ, difX) * 180.0 / Math.PI) - 90.0f;
                event.setYaw(yaw);
                event.setPitch(pitch);
            }
        } else {
            this.target = null;
        }
    }

    @Handler(value=Render3DEvent.class)
    public void onRender3D(Render3DEvent event) {
        if (this.mc.theWorld == null || this.target == null) {
            return;
        }
        if (this.target == null) {
            return;
        }
        Vec3 pos = BowAimbot.predictPos(this.target, 11.0f);
        this.drawEntityESP(pos.xCoord - this.mc.getRenderManager().renderPosX, pos.yCoord + 0.55 - this.mc.getRenderManager().renderPosY, pos.zCoord - this.mc.getRenderManager().renderPosZ, 0.4, 0.5, new Color(14890790));
    }

    private void drawEntityESP(double x, double y, double z, double height, double width, Color color) {
        GL11.glPushMatrix();
        GLUtil.setGLCap(3042, true);
        GLUtil.setGLCap(3553, false);
        GLUtil.setGLCap(2896, false);
        GLUtil.setGLCap(2929, false);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)1.8f);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtil.setGLCap(2848, true);
        GL11.glDepthMask((boolean)true);
        RenderUtil.BB(new AxisAlignedBB(x - width + 0.25, y + 0.1, z - width + 0.25, x + width - 0.25, y + height + 0.25, z + width - 0.25), new Color(color.getRed(), color.getGreen(), color.getBlue(), 120).getRGB());
        RenderUtil.OutlinedBB(new AxisAlignedBB(x - width + 0.25, y + 0.1, z - width + 0.25, x + width - 0.25, y + height + 0.25, z + width - 0.25), 1.0f, color.getRGB());
        GLUtil.revertAllCaps();
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    private static Vec3 lerp(Vec3 pos, Vec3 prev, float time) {
        double x = pos.xCoord + (pos.xCoord - prev.xCoord) * (double)time;
        double y = pos.yCoord + (pos.yCoord - prev.yCoord) * (double)time;
        double z = pos.zCoord + (pos.zCoord - prev.zCoord) * (double)time;
        return new Vec3(x, y, z);
    }

    public static Vec3 predictPos(Entity entity, float time) {
        return BowAimbot.lerp(new Vec3(entity.posX, entity.posY, entity.posZ), new Vec3(entity.prevPosX, entity.prevPosY, entity.prevPosZ), time);
    }

    private float getLaunchAngle(EntityLivingBase targetEntity, double v, double g) {
        double yDif = targetEntity.posY + (double)(targetEntity.getEyeHeight() / 2.0f) - (this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight());
        double xDif = targetEntity.posX - this.mc.thePlayer.posX;
        double zDif = targetEntity.posZ - this.mc.thePlayer.posZ;
        double xCoord = Math.sqrt(xDif * xDif + zDif * zDif);
        return this.theta(v, g, xCoord, yDif);
    }

    private float theta(double v, double g, double x, double y) {
        double yv = 2.0 * y * (v * v);
        double gx = g * (x * x);
        double g2 = g * (gx + yv);
        double insqrt = v * v * v * v - g2;
        double sqrt = Math.sqrt(insqrt);
        double numerator = v * v + sqrt;
        double numerator2 = v * v - sqrt;
        double atan1 = Math.atan2(numerator, g * x);
        double atan2 = Math.atan2(numerator2, g * x);
        return (float)Math.min(atan1, atan2);
    }

    private EntityLivingBase getBestTarget(float yaw) {
        this.targets.clear();
        for (Entity e : this.mc.theWorld.loadedEntityList) {
            EntityLivingBase ent;
            if (!(e instanceof EntityLivingBase) || !this.isValid(ent = (EntityLivingBase)e)) continue;
            this.targets.add(ent);
        }
        if (this.targets.isEmpty()) {
            return null;
        }
        this.sortTargets(yaw);
        return this.targets.get(0);
    }

    private void sortTargets(float yaw) {
        switch ((SortMode)((Object)this.sortingmode.getValue())) {
            case DISTANCE: {
                this.targets.sort(Comparator.comparingDouble(this.mc.thePlayer::getDistanceSqToEntity));
                break;
            }
            case HEALTH: {
                this.targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                break;
            }
            case FOV: {
                this.targets.sort(Comparator.comparingDouble(this::yawDist));
                break;
            }
            case CYCLE: {
                this.targets.sort(Comparator.comparingDouble(player -> this.yawDistCycle((EntityLivingBase)player, yaw)));
                break;
            }
            case ARMOR: {
                this.targets.sort(Comparator.comparingDouble(this::getArmorVal));
            }
        }
    }

    private boolean isValid(EntityLivingBase entity) {
        double d = ((Float)this.range.getValue()).floatValue();
        return !AntiBot.getBots().contains(entity) && this.mc.thePlayer.canEntityBeSeen(entity) && entity != null && this.mc.thePlayer != entity && (entity instanceof EntityPlayer && this.players.getValue() != false || entity instanceof EntityAnimal && this.animals.isEnabled() || (entity instanceof EntityMob || entity instanceof EntitySlime) && this.mobs.getValue() != false) && entity.getDistanceSqToEntity(this.mc.thePlayer) <= d * d && entity.isEntityAlive() && (!entity.isInvisible() || this.invisibles.getValue() != false) && !Moon.INSTANCE.getFriendManager().isFriend(entity.getName());
    }

    private double getArmorVal(EntityLivingBase ent) {
        if (ent instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)ent;
            double armorstrength = 0.0;
            for (int index = 3; index >= 0; --index) {
                ItemStack stack = player.inventory.armorInventory[index];
                if (stack == null) continue;
                armorstrength += this.getArmorStrength(stack);
            }
            return armorstrength;
        }
        return 0.0;
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

    private double yawDist(EntityLivingBase e) {
        Vec3 difference = e.getPositionVector().addVector(0.0, e.getEyeHeight() / 2.0f, 0.0).subtract(this.mc.thePlayer.getPositionVector().addVector(0.0, this.mc.thePlayer.getEyeHeight(), 0.0));
        double d = Math.abs((double)this.mc.thePlayer.rotationYaw - (Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0)) % 360.0;
        return d > 180.0 ? 360.0 - d : d;
    }

    private double yawDistCycle(EntityLivingBase e, float yaw) {
        Vec3 difference = e.getPositionVector().addVector(0.0, e.getEyeHeight() / 2.0f, 0.0).subtract(this.mc.thePlayer.getPositionVector().addVector(0.0, this.mc.thePlayer.getEyeHeight(), 0.0));
        double d = Math.abs((double)yaw - Math.atan2(difference.zCoord, difference.xCoord)) % 90.0;
        return d;
    }

    private static enum SortMode {
        FOV,
        DISTANCE,
        HEALTH,
        CYCLE,
        ARMOR;

    }
}

