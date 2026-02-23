/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.event.impl.render.Render3DEvent;
import me.moon.module.Module;
import me.moon.utils.MathUtils;
import me.moon.utils.game.CombatUtil;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.render.GLUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class Shooter
extends Module {
    private final NumberValue<Integer> distance = new NumberValue<Integer>("Delay", 150, 1, 300, 10);
    private final NumberValue<Integer> fov = new NumberValue<Integer>("FOV", 100, 10, 180, 5);
    private final BooleanValue targetesp = new BooleanValue("Target ESP", "Target ESP", true);
    private final BooleanValue teams = new BooleanValue("Teams", "Teams", false);
    private int clicks;
    private double counter;
    private double stage;
    private final List<C0EPacketClickWindow> queuedClicks = new ArrayList<C0EPacketClickWindow>();
    private final TimerUtil time = new TimerUtil();
    private EntityPlayer target;
    private final List<EntityPlayer> switchList = new ArrayList<EntityPlayer>();

    public Shooter() {
        super("Shooter", Module.Category.COMBAT, new Color(11472384).getRGB());
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Handler(value=Render2DEvent.class)
    public void onRender(Render2DEvent event) {
        ScaledResolution sr = event.getScaledResolution();
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.target = null;
        ArrayList<EntityPlayer> dynamicTargets = new ArrayList<EntityPlayer>();
        this.mc.theWorld.getLoadedEntityList().stream().filter(entity -> dynamicTargets.size() < 10 && entity instanceof EntityPlayer).filter(entity -> this.isValidTarget((EntityPlayer)entity)).forEach(potentialTarget -> dynamicTargets.add((EntityPlayer)potentialTarget));
        if (event.isPre()) {
            event.setYaw(this.mc.thePlayer.rotationYaw + (float)MathUtils.getRandomInRange(-180, 180));
            event.setPitch(MathUtils.getRandomInRange(80.0f, 90.0f));
            if (!dynamicTargets.isEmpty() && this.target == null) {
                dynamicTargets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                dynamicTargets.stream().limit(3L).collect(Collectors.toList()).sort(Comparator.comparingDouble(CombatUtil::yawDist));
                dynamicTargets.sort(Comparator.comparingDouble(target -> this.mc.thePlayer.getDistanceToEntity((Entity)target)));
                this.target = (EntityPlayer)dynamicTargets.get(0);
                if (event.isPre()) {
                    float[] rots = this.getRotationsToEnt(this.target, this.mc.thePlayer);
                    float yaw = rots[0];
                    float pitch = rots[1];
                    if (this.time.sleep(200L)) {
                        this.mc.getNetHandler().addToSendQueueNoEvents(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, yaw, pitch, false));
                        this.mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                        this.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.getHeldItem()));
                        this.mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                        this.mc.getNetHandler().addToSendQueueNoEvents(new C03PacketPlayer.C05PacketPlayerLook(this.mc.thePlayer.rotationYaw + (float)MathUtils.getRandomInRange(-180, 180), MathUtils.getRandomInRange(80.0f, 90.0f), false));
                    }
                }
            }
        }
    }

    @Handler(value=Render3DEvent.class)
    public void onRender3D(Render3DEvent event) {
        if (this.targetesp.isEnabled() && this.target != null) {
            double x = RenderUtil.interpolate(this.target.posX, this.target.lastTickPosX, event.getPartialTicks());
            double y = RenderUtil.interpolate(this.target.posY, this.target.lastTickPosY, event.getPartialTicks());
            double z = RenderUtil.interpolate(this.target.posZ, this.target.lastTickPosZ, event.getPartialTicks());
            this.drawEntityESP(x - this.mc.getRenderManager().renderPosX, y + (double)this.target.height + 0.1 - (double)this.target.height - this.mc.getRenderManager().renderPosY, z - this.mc.getRenderManager().renderPosZ, this.target.height, 0.65, new Color(this.target.hurtTime > 0 ? 14890790 : RenderUtil.getRainbow(4000, 0, 0.85f)));
        }
    }

    public float[] getRotationsToEnt(Entity ent, EntityPlayerSP playerSP) {
        double differenceX = ent.posX - playerSP.posX;
        double differenceY = ent.posY + (double)ent.height - 0.25 - (playerSP.posY + (double)playerSP.height);
        double differenceZ = ent.posZ - playerSP.posZ;
        float rotationYaw = (float)(Math.atan2(differenceZ, differenceX) * 180.0 / Math.PI) - 90.0f;
        float rotationPitch = (float)(Math.atan2(differenceY, playerSP.getDistanceToEntity(ent)) * 180.0 / Math.PI);
        float finishedYaw = playerSP.rotationYaw + MathHelper.wrapAngleTo180_float(rotationYaw - playerSP.rotationYaw);
        float p = MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(differenceY, Math.sqrt(differenceX * differenceX + differenceZ * differenceZ)))));
        return new float[]{finishedYaw, p};
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
        RenderUtil.BB(new AxisAlignedBB(x - width + 0.25, y, z - width + 0.25, x + width - 0.25, y + height, z + width - 0.25), new Color(color.getRed(), color.getGreen(), color.getBlue(), 120).getRGB());
        RenderUtil.OutlinedBB(new AxisAlignedBB(x - width + 0.25, y, z - width + 0.25, x + width - 0.25, y + height, z + width - 0.25), 1.0f, color.getRGB());
        GLUtil.revertAllCaps();
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public double getTargetWeight(EntityPlayer p) {
        double weight = -this.mc.thePlayer.getDistanceToEntity(p);
        if (p.lastTickPosX == p.posX && p.lastTickPosY == p.posY && p.lastTickPosZ == p.posZ) {
            weight += 6000.0;
        }
        for (EntityPlayer player : this.switchList) {
            if (player != p) continue;
            weight -= 6000.0;
        }
        return weight;
    }

    public boolean isValidTarget(EntityPlayer player) {
        return player.ticksExisted > 20 && player.getUniqueID() != this.mc.thePlayer.getUniqueID() && player.isEntityAlive() && !player.isInvisible() && this.isWithinFOV(player, this.mc.thePlayer.rotationYaw, ((Integer)this.fov.getValue()).intValue()) && (this.isOnEnemyTeam(player) || !this.teams.isEnabled()) && !Moon.INSTANCE.getFriendManager().isFriend(player.getName());
    }

    private boolean isWithinFOV(EntityLivingBase entity, float yaw, double fov) {
        float[] rotations = this.getRotationsToEnt(entity, this.mc.thePlayer);
        float yawDifference = this.getYawDifference(yaw % 360.0f, rotations[0]);
        float pitchDifference = Math.abs(this.mc.thePlayer.rotationPitch - rotations[1]);
        float total = Math.abs(yawDifference + (pitchDifference = (float)((double)pitchDifference * 1.5)));
        return (double)total < fov;
    }

    private float getYawDifference(float currentYaw, float neededYaw) {
        float yawDifference = neededYaw - currentYaw;
        if (yawDifference > 180.0f) {
            yawDifference = -(360.0f - neededYaw + currentYaw);
        } else if (yawDifference < -180.0f) {
            yawDifference = 360.0f - currentYaw + neededYaw;
        }
        return yawDifference;
    }

    public boolean isOnEnemyTeam(EntityPlayer target) {
        boolean teamChecks = false;
        ChatFormatting myCol = null;
        ChatFormatting enemyCol = null;
        if (target != null) {
            for (ChatFormatting col : ChatFormatting.values()) {
                if (col == ChatFormatting.RESET) continue;
                if (this.mc.thePlayer.getDisplayName().getFormattedText().contains(col.toString()) && myCol == null) {
                    myCol = col;
                }
                if (!target.getDisplayName().getFormattedText().contains(col.toString()) || enemyCol != null) continue;
                enemyCol = col;
            }
            try {
                if (myCol != null && enemyCol != null) {
                    teamChecks = myCol != enemyCol;
                } else if (this.mc.thePlayer.getTeam() != null) {
                    teamChecks = !this.mc.thePlayer.isOnSameTeam(target);
                } else if (this.mc.thePlayer.inventory.armorInventory[3].getItem() instanceof ItemBlock) {
                    teamChecks = !ItemStack.areItemStacksEqual(this.mc.thePlayer.inventory.armorInventory[3], target.inventory.armorInventory[3]);
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return teamChecks;
    }

    private Vec3 predict(EntityPlayer player) {
        int pingTicks = (int)Math.ceil((double)this.mc.getNetHandler().getPlayerInfo(this.mc.thePlayer.getUniqueID()).getResponseTime() / 50.0) + 1;
        return Shooter.predictPos(player, pingTicks);
    }

    private static Vec3 lerp(Vec3 pos, Vec3 prev, float time) {
        double x = pos.xCoord + (pos.xCoord - prev.xCoord) * (double)time;
        double y = pos.yCoord + (pos.yCoord - prev.yCoord) * (double)time;
        double z = pos.zCoord + (pos.zCoord - prev.zCoord) * (double)time;
        return new Vec3(x, y, z);
    }

    public static Vec3 predictPos(Entity entity, float time) {
        return Shooter.lerp(new Vec3(entity.posX, entity.posY, entity.posZ), new Vec3(entity.prevPosX, entity.prevPosY, entity.prevPosZ), time);
    }

    private boolean canPlayerSee(Vec3 vec3) {
        return this.mc.theWorld.rayTraceBlocks(new Vec3(this.mc.thePlayer.posX, this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight(), this.mc.thePlayer.posZ), new Vec3(vec3.xCoord, vec3.yCoord, vec3.zCoord), false, true, false) == null;
    }

    private boolean isInCircle(double x, double y, double radius, double posX, double posY) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        return (posX - (double)((float)sr.getScaledWidth() / 2.0f)) * (posX - (double)((float)sr.getScaledWidth() / 2.0f)) + (posY - (double)((float)sr.getScaledHeight() / 2.0f)) * (posY - (double)((float)sr.getScaledHeight() / 2.0f)) < (double)((int)radius) * radius;
    }
}

