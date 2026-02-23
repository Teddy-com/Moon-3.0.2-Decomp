/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package me.moon.module.impl.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.input.SwingEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.module.impl.combat.AntiBot;
import me.moon.module.impl.combat.AutoApple;
import me.moon.utils.MathUtils;
import me.moon.utils.game.Printer;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.location.MoonLocation;
import me.moon.utils.pathfinding.Node;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.NumberValue;
import me.moon.utils.value.impl.RangedValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.apache.commons.lang3.StringUtils;

public class TPAura
extends Module {
    private final RangedValue<Integer> cps = new RangedValue<Integer>("CPS", 1, 20, 1, 7, 11);
    private final NumberValue<Float> range = new NumberValue<Float>("Range", Float.valueOf(50.0f), Float.valueOf(5.0f), Float.valueOf(100.0f), Float.valueOf(5.0f));
    private final EnumValue<mode> modes = new EnumValue<mode>("Mode", mode.TEST);
    private int cpsStage;
    private int nextCPS = 1;
    private float oldYaw;
    public float yaw;
    public float pitch;
    private double counter;
    private double stage;
    public static EntityLivingBase target;
    public static EntityLivingBase multiTarget;
    private final TimerUtil timerUtil = new TimerUtil();
    private List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();

    public TPAura() {
        super("TPAura", Module.Category.COMBAT, new Color(11472384).getRGB());
    }

    @Override
    public void onDisable() {
        target = null;
        this.mc.timer.timerSpeed = 1.0f;
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        long ping;
        this.setSuffix(StringUtils.capitalize((String)((mode)((Object)this.modes.getValue())).getName()));
        long l = ping = this.mc.getCurrentServerData() == null ? 0L : Math.min(50L, Math.max(this.mc.getCurrentServerData().pingToServer, 110L));
        if (AutoApple.doingStuff || this.mc.thePlayer.isSpectator()) {
            return;
        }
        if (this.mc.thePlayer != null && this.mc.thePlayer.deathTime == 1) {
            this.toggle();
        }
        if (event.isPre()) {
            ++this.cpsStage;
            event.setPitch(this.mc.thePlayer.rotationPitch);
        }
        target = null;
        switch ((mode)((Object)this.modes.getValue())) {
            case DEV: {
                target = this.getTarget();
                if (!event.isPre() || target == null) break;
                double x = this.mc.thePlayer.posX;
                double y = this.mc.thePlayer.posY;
                double z = this.mc.thePlayer.posZ;
                MoonLocation startLocation = new MoonLocation(this.mc.theWorld, x, y, z);
                MoonLocation moonLocation = new MoonLocation(this.mc.theWorld, TPAura.target.posX, TPAura.target.posY, TPAura.target.posZ);
                break;
            }
            case TEST: {
                target = this.getTarget();
                if (!event.isPre() || target == null || (this.counter += 1.0) % 20.0 != 0.0) break;
                MoonLocation startLocation = new MoonLocation(this.mc.theWorld, this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
                MoonLocation endLocation = new MoonLocation(this.mc.theWorld, TPAura.target.posX, TPAura.target.posY, TPAura.target.posZ);
                ArrayList<Node> openNodes = new ArrayList<Node>();
                ArrayList<Node> closedNodes = new ArrayList<Node>();
                openNodes.add(new Node(startLocation, startLocation, endLocation));
                for (int i = 0; i <= 2; ++i) {
                    Node current = Collections.min(openNodes, Comparator.comparing(Node::getF_COST));
                    openNodes.remove(current);
                    closedNodes.add(current);
                    if (current.getF_COST() <= 1.0) {
                        Printer.print("found path");
                        break;
                    }
                    double precision = 0.5;
                    for (int direction = 1; direction <= 4; ++direction) {
                        String toDebug;
                        String fromDebug;
                        Node neighbour = this.getNeighbourLocation(current.getLocation(), startLocation, endLocation, direction, precision);
                        if (closedNodes.contains(neighbour) || this.mc.theWorld.getBlockState(new BlockPos(neighbour.getLocation().getX(), neighbour.getLocation().getY(), neighbour.getLocation().getZ())).getBlock() != Blocks.air) continue;
                        if (neighbour.getF_COST() < current.getF_COST()) {
                            if (openNodes.contains(neighbour)) continue;
                            openNodes.add(neighbour);
                            fromDebug = String.format("From: XYZ: %.2f %.2f %.2f", current.getLocation().getX(), current.getLocation().getY(), current.getLocation().getZ());
                            toDebug = String.format("To: XYZ: %.2f %.2f %.2f", neighbour.getLocation().getX(), neighbour.getLocation().getY(), neighbour.getLocation().getZ());
                            String costs = String.format("neighbour: %.2f current: %.2f", neighbour.getF_COST(), current.getF_COST());
                            Printer.print(fromDebug + " " + toDebug + " " + costs);
                            this.mc.thePlayer.setPosition(neighbour.getLocation().getX(), neighbour.getLocation().getY(), neighbour.getLocation().getZ());
                            break;
                        }
                        fromDebug = String.format("Couldn't find From: XYZ: %.2f %.2f %.2f", current.getLocation().getX(), current.getLocation().getY(), current.getLocation().getZ());
                        toDebug = String.format("To: XYZ: %.2f %.2f %.2f", neighbour.getLocation().getX(), neighbour.getLocation().getY(), neighbour.getLocation().getZ());
                        String distances = String.format("nDist: %.2f cDist: %.2f", neighbour.getLocation().distance3D(endLocation), current.getLocation().distance3D(endLocation));
                        String debug = String.format("\u00a7cInvalid at %b nFCost: %.3f cFCost: %.3f", direction, neighbour.getF_COST(), current.getF_COST());
                        Printer.print(fromDebug + "\n" + toDebug + "\n" + debug + "\n" + distances);
                    }
                    if (i < 0) continue;
                }
                this.timerUtil.reset();
            }
        }
    }

    private Node getNeighbourLocation(MoonLocation location, MoonLocation startLocation, MoonLocation endLocation, int direction, double precision) {
        Node neighbour = new Node(location.clone(), startLocation.clone(), endLocation.clone());
        switch (direction) {
            case 1: {
                neighbour.getLocation().addToX(precision);
                break;
            }
            case 2: {
                neighbour.getLocation().removeToX(precision);
                break;
            }
            case 3: {
                neighbour.getLocation().addToZ(precision);
                break;
            }
            case 4: {
                neighbour.getLocation().removeToZ(precision);
            }
        }
        neighbour.updateCost(startLocation, endLocation);
        return neighbour;
    }

    private void attackEntity(Entity entity) {
        this.nextCPS = this.newgetCPS();
        this.cpsStage = 0;
        Moon.INSTANCE.getEventBus().fireEvent(new SwingEvent());
        this.mc.thePlayer.swingItem();
        this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
        float f1 = 0.0f;
        boolean flag = this.mc.thePlayer.fallDistance > 0.0f && !this.mc.thePlayer.onGround && !this.mc.thePlayer.isOnLadder() && !this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isPotionActive(Potion.blindness) && this.mc.thePlayer.ridingEntity == null && target instanceof EntityLivingBase;
        f1 = target != null ? EnchantmentHelper.func_152377_a(this.mc.thePlayer.getHeldItem(), target.getCreatureAttribute()) : EnchantmentHelper.func_152377_a(this.mc.thePlayer.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
        if (flag) {
            this.mc.thePlayer.onCriticalHit(target);
        }
        if (f1 > 0.0f) {
            this.mc.thePlayer.onEnchantmentCritical(target);
        }
    }

    private int newgetCPS() {
        double range = MathUtils.getRandomInRange(this.cps.getLeftValue(), this.cps.getRightValue());
        range = 20.0 / range;
        if (this.mc.thePlayer.ticksExisted % 3 != 0) {
            range += (double)Math.round(MathUtils.getRandomInRange(-1.15, 1.15));
        }
        if (this.mc.thePlayer.ticksExisted % 45 == 0) {
            range += (double)MathUtils.getRandomInRange(1, 3);
        }
        range = Math.round(Math.max(range, 1.0));
        int result = (int)range;
        return result;
    }

    private int getCPS() {
        return MathUtils.getRandomInRange(this.cps.getLeftValue(), this.cps.getRightValue());
    }

    private void swap(int slot, int hotbarNum) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, this.mc.thePlayer);
    }

    private EntityLivingBase getTarget() {
        this.targets.clear();
        double Dist = Double.MAX_VALUE;
        if (this.mc.theWorld != null) {
            for (Object object : this.mc.theWorld.loadedEntityList) {
                EntityLivingBase e;
                if (!(object instanceof EntityLivingBase) || !((double)this.mc.thePlayer.getDistanceToEntity(e = (EntityLivingBase)object) < Dist) || !this.isTargetable(e, this.mc.thePlayer, false)) continue;
                this.targets.add(e);
            }
        }
        if (this.targets.isEmpty()) {
            return null;
        }
        this.targets.sort(Comparator.comparingDouble(target -> this.yawDist((EntityLivingBase)target)));
        return this.targets.get(0);
    }

    private void lowerTicks() {
        this.mc.theWorld.getLoadedEntityList().forEach(e -> {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase living = (EntityLivingBase)e;
                if (living.auraticks > 0) {
                    --living.auraticks;
                }
            }
        });
    }

    private double yawDist(EntityLivingBase e) {
        if (e != null) {
            Vec3 difference = e.getPositionVector().addVector(0.0, e.getEyeHeight() / 2.0f, 0.0).subtract(this.mc.thePlayer.getPositionVector().addVector(0.0, this.mc.thePlayer.getEyeHeight(), 0.0));
            double d = Math.abs((double)this.mc.thePlayer.rotationYaw - (Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0)) % 360.0;
            return d > 180.0 ? 360.0 - d : d;
        }
        return 0.0;
    }

    public float[] getRotationsToEnt(Entity ent, EntityPlayerSP playerSP) {
        double differenceX = ent.posX - playerSP.posX;
        double differenceY = ent.posY + (double)ent.height - (playerSP.posY + (double)playerSP.height);
        double differenceZ = ent.posZ - playerSP.posZ;
        float rotationYaw = (float)(Math.atan2(differenceZ, differenceX) * 180.0 / Math.PI) - 90.0f;
        float rotationPitch = (float)(Math.atan2(differenceY, playerSP.getDistanceToEntity(ent)) * 180.0 / Math.PI);
        float finishedYaw = playerSP.rotationYaw + MathHelper.wrapAngleTo180_float(rotationYaw - playerSP.rotationYaw);
        float finishedPitch = playerSP.rotationPitch + MathHelper.wrapAngleTo180_float(rotationPitch - playerSP.rotationPitch);
        return new float[]{finishedYaw, -finishedPitch};
    }

    private boolean isTargetable(EntityLivingBase entity, EntityPlayerSP clientPlayer, boolean b) {
        boolean LOL = false;
        if (entity instanceof EntityWither && clientPlayer.getDistanceToEntity(entity) <= ((Float)this.range.getValue()).floatValue()) {
            EntityWither a = (EntityWither)entity;
            boolean teamChecks = false;
            EnumChatFormatting myCol = null;
            EnumChatFormatting enemyCol = null;
            if (a != null) {
                for (EnumChatFormatting col : EnumChatFormatting.values()) {
                    if (col == EnumChatFormatting.RESET) continue;
                    if (this.mc.thePlayer.getDisplayName().getFormattedText().contains(col.toString()) && myCol == null) {
                        myCol = col;
                    }
                    if (!a.getDisplayName().getFormattedText().contains(col.toString()) || enemyCol != null) continue;
                    enemyCol = col;
                }
                try {
                    if (myCol != null && enemyCol != null) {
                        teamChecks = myCol == enemyCol;
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        return entity.getUniqueID() != clientPlayer.getUniqueID() && entity.isEntityAlive() && !AntiBot.getBots().contains(entity) && !Moon.INSTANCE.getFriendManager().isFriend(entity.getName()) && !entity.isInvisible() && clientPlayer.getDistanceToEntity(entity) <= ((Float)this.range.getValue()).floatValue() && entity instanceof EntityPlayer;
    }

    private static enum mode {
        TEST("Test"),
        DEV("Dev");

        private final String name;

        private mode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

