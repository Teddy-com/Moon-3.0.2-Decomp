/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.input.SwingEvent;
import me.moon.event.impl.player.StrafeEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.event.impl.render.Render3DEvent;
import me.moon.module.Module;
import me.moon.module.impl.combat.AntiBot;
import me.moon.module.impl.combat.AutoApple;
import me.moon.module.impl.combat.Criticals;
import me.moon.module.impl.movement.Scaffold;
import me.moon.module.impl.movement.Step;
import me.moon.module.impl.visuals.HUD;
import me.moon.utils.MathUtils;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.game.CombatUtil;
import me.moon.utils.game.Printer;
import me.moon.utils.game.Rotations;
import me.moon.utils.game.ServerUtils;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.render.GLUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.ColorValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.NumberValue;
import me.moon.utils.value.impl.RangedValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

public class KillAura
extends Module {
    public static EntityLivingBase target;
    public static EntityLivingBase multiTarget;
    public static EntityLivingBase dynamicTarget;
    private List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
    private final EnumValue<mode> modes = new EnumValue<mode>("Mode", mode.SINGLE);
    private final EnumValue<sortModes> sortMode = new EnumValue<sortModes>("Sort Mode", sortModes.FOV);
    private final RangedValue<Integer> cps = new RangedValue<Integer>("CPS", 1, 20, 1, 7, 11);
    public final NumberValue<Float> range = new NumberValue<Float>("Range", Float.valueOf(4.2f), Float.valueOf(1.0f), Float.valueOf(7.0f), Float.valueOf(0.1f));
    private final NumberValue<Float> blockRange = new NumberValue<Float>("Block Range", Float.valueOf(7.0f), Float.valueOf(1.0f), Float.valueOf(15.0f), Float.valueOf(0.1f));
    private final NumberValue<Integer> particlesMultiplier = new NumberValue<Integer>("Particles Multiplier", 3, 1, 5, 1);
    private final NumberValue<Integer> maxTargets = new NumberValue<Integer>("Max Targets", 3, 1, 5, 1);
    private final BooleanValue players = new BooleanValue("Players", "Target Players", true);
    private final BooleanValue animals = new BooleanValue("Animals", "Target Animals", false);
    private final BooleanValue monsters = new BooleanValue("Monsters", "Target Monsters", false);
    private final BooleanValue invisibles = new BooleanValue("Invisibles", "Target Invisibles", false);
    private final BooleanValue autoblock = new BooleanValue("AutoBlock", "Automatically Block", true);
    public final BooleanValue fakeAutoblock = new BooleanValue("Animate", "Always Animate", false);
    private final BooleanValue dynamic = new BooleanValue("Dynamic", "Dynamic Timing", true);
    private final BooleanValue fasterSwitching = new BooleanValue("Faster Switching", "Switches between targets faster", true);
    private final BooleanValue teams = new BooleanValue("Teams", "Teams Mode", false);
    private final NumberValue<Integer> SwitchSpeed = new NumberValue<Integer>("Switch Speed", 300, 100, 1000, 50, this.modes, "Switch");
    private final BooleanValue durability = new BooleanValue("Durability", "Tick Durability", false, this.modes, "Tick");
    private final NumberValue<Integer> multiTargets = new NumberValue<Integer>("Multi Targets", 3, 1, 10, 1, this.modes, "multi");
    private final NumberValue<Integer> multiFov = new NumberValue<Integer>("Multi FOV", 75, 1, 180, 1, this.modes, "multi");
    private final BooleanValue targetEsp = new BooleanValue("Target ESP", "Target ESP", true);
    private final BooleanValue rangeCircle = new BooleanValue("Range Circle", "Renders a circle of your range", false);
    private final EnumValue<targetESPMode> targetEspModes = new EnumValue<targetESPMode>("Target ESP Modes", targetESPMode.JELLO, (Value)this.targetEsp, "true");
    private final ColorValue targetESPColor = new ColorValue("Target ESP Color", -1, (Value)this.targetEsp, "true");
    private final TimerUtil timerUtil = new TimerUtil();
    private final TimerUtil switchTimer = new TimerUtil();
    private EntityLivingBase lastTarget;
    private final AnimationUtil transUtil = new AnimationUtil(0.0, 0.0);
    private long time;
    private float[] serverAngles = new float[2];
    private final float[] prevRotations = new float[2];
    private int switchI;
    private int cpsStage;
    private int nextCPS = 1;
    private int switchTicks;
    private float lastYaw;
    private float lastPitch;
    private float oldYaw;
    public float yaw;
    public float pitch;
    boolean upWards = false;

    public KillAura() {
        super("KillAura", Module.Category.COMBAT, new Color(11472384).getRGB());
    }

    @Override
    public void onDisable() {
        target = null;
        this.switchI = 0;
        this.mc.timer.timerSpeed = 1.0f;
        this.switchTicks = 0;
    }

    public boolean isAutoBlocking() {
        return this.autoblock.isEnabled() && this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword && target != null && this.fakeAutoblock.isEnabled() || this.fakeAutoblock.isEnabled() && target != null && this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
    }

    @Override
    public void onEnable() {
        if (this.mc.thePlayer != null) {
            this.lastYaw = this.mc.thePlayer.rotationYaw;
            this.lastPitch = this.mc.thePlayer.rotationPitch;
        }
    }

    @Handler(value=StrafeEvent.class)
    public void onStrafe(StrafeEvent event) {
        Scaffold scaffold = (Scaffold)Moon.INSTANCE.getModuleManager().getModule("Scaffold");
        if (scaffold.isEnabled() && !scaffold.killAura.isEnabled()) {
            return;
        }
        switch ((mode)((Object)this.modes.getValue())) {
            case AAC: 
            case INTAVE: {
                if (target == null || KillAura.target.isDead) break;
                float[] rots = this.getRotationsToEnt(target, this.mc.thePlayer);
                float sens = CombatUtil.getSensitivityMultiplier();
                float yaw = rots[0] + MathUtils.getRandomInRange(-0.55f, 0.55f);
                float pitch = rots[1] + MathUtils.getRandomInRange(-0.5f, 0.5f);
                float yawGCD = (float)Math.round(yaw / sens) * sens + 0.01f;
                float pitchGCD = MathHelper.clamp_float((float)Math.round(pitch / sens) * sens + 0.01f, -90.0f, 90.0f);
                if (Rotations.INSTANCE.getCurrentRotation() != null) break;
                Rotations.INSTANCE.setCurrentRotation(new Rotations.Rotation(Float.valueOf(yawGCD), Float.valueOf(pitchGCD)));
                break;
            }
        }
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        Scaffold scaffold = (Scaffold)Moon.INSTANCE.getModuleManager().getModule("Scaffold");
        if (scaffold.isEnabled() && !scaffold.killAura.isEnabled()) {
            return;
        }
        Criticals criticals = (Criticals)Moon.INSTANCE.getModuleManager().getModule("Criticals");
        this.setSuffix(StringUtils.capitalize((String)((mode)((Object)this.modes.getValue())).getName()));
        long ping = this.mc.getCurrentServerData() == null ? 0L : Math.min(50L, Math.max(this.mc.getCurrentServerData().pingToServer, 110L));
        int pingDelay = Math.round((float)ping / 10.0f);
        if (AutoApple.doingStuff || this.mc.thePlayer.isSpectator()) {
            return;
        }
        if (this.mc.thePlayer != null && this.mc.thePlayer.deathTime == 1) {
            this.toggle();
        }
        if (event.isPre()) {
            ++this.cpsStage;
        }
        if (this.modes.getValue() != mode.SINGLE) {
            target = null;
        }
        int attackKey = this.mc.gameSettings.keyBindAttack.getKeyCode();
        int blockKey = this.mc.gameSettings.keyBindUseItem.getKeyCode();
        switch ((mode)((Object)this.modes.getValue())) {
            case FAITHFUL: {
                target = this.getTarget();
                if (!event.isPre() || target == null || this.cpsStage < this.nextCPS) break;
                this.attackEntity(target, false, false);
                break;
            }
            case EXPERIMENTAL: {
                target = this.getTarget();
                if (event.isPre() || target == null) break;
                this.attackEntity(target, false, false);
                break;
            }
            case OLDAAC: {
                target = this.getTarget();
                if (event.isPre()) {
                    if (target != null) {
                        float[] rots = this.getRotationsToEnt(target, this.mc.thePlayer);
                        float sens = CombatUtil.getSensitivityMultiplier();
                        float yaw = rots[0] + MathUtils.getRandomInRange(-0.55f, 0.75f);
                        float pitch = rots[1] + MathUtils.getRandomInRange(-0.7f, 0.5f);
                        float yawGCD = (float)Math.round(yaw / sens) * sens + 0.01f;
                        float pitchGCD = MathHelper.clamp_float((float)Math.round(pitch / sens) * sens + 0.01f, -90.0f, 90.0f);
                        if (Rotations.INSTANCE.getCurrentRotation() == null) {
                            Rotations.INSTANCE.setCurrentRotation(new Rotations.Rotation(Float.valueOf(yawGCD), Float.valueOf(pitchGCD)));
                        }
                        if (!(this.cpsStage < this.nextCPS || KillAura.target.hurtResistantTime > 16 && this.canBlock() && this.autoblock.isEnabled())) {
                            this.nextCPS = this.getNewCps();
                            this.cpsStage = 0;
                            KeyBinding.setKeyBindState(attackKey, true);
                            KeyBinding.setKeyBindState(attackKey, false);
                            KeyBinding.onTick(attackKey);
                        } else if (this.canBlock() && this.autoblock.isEnabled()) {
                            boolean block = this.mc.thePlayer.ticksExisted % 3 != 0 && (KillAura.target.hurtResistantTime > 16 || this.cpsStage + 1 < this.nextCPS);
                            KeyBinding.setKeyBindState(blockKey, block);
                        }
                    } else {
                        if (this.canBlock() && this.autoblock.isEnabled()) {
                            KeyBinding.setKeyBindState(blockKey, false);
                        }
                        this.timerUtil.reset();
                    }
                }
            }
            case INTAVE: {
                target = this.getTarget();
                if (!event.isPre()) break;
                if (target != null) {
                    if (!(this.cpsStage < this.nextCPS || KillAura.target.hurtResistantTime > 16 && this.canBlock() && this.autoblock.isEnabled())) {
                        this.nextCPS = this.getNewCps();
                        this.cpsStage = 0;
                        KeyBinding.setKeyBindState(attackKey, true);
                        KeyBinding.setKeyBindState(attackKey, false);
                        KeyBinding.onTick(attackKey);
                        break;
                    }
                    if (!this.canBlock() || !this.autoblock.isEnabled()) break;
                    boolean block = this.mc.thePlayer.ticksExisted % 3 != 0 && (KillAura.target.hurtResistantTime > 16 || this.cpsStage + 1 < this.nextCPS);
                    KeyBinding.setKeyBindState(blockKey, block);
                    break;
                }
                if (this.canBlock() && this.autoblock.isEnabled()) {
                    KeyBinding.setKeyBindState(blockKey, false);
                }
                this.timerUtil.reset();
                break;
            }
            case AAC: {
                target = this.getTarget();
                if (!event.isPre()) break;
                if (target != null) {
                    if (this.dynamic.isEnabled()) {
                        if (KillAura.target.hurtResistantTime == 0) {
                            if (!this.timerUtil.sleep(ping * 3L)) break;
                            this.attackEntity(target, false, true);
                            break;
                        }
                        if (KillAura.target.hurtResistantTime > 9 + pingDelay) break;
                        this.attackEntity(target, false, true);
                        break;
                    }
                    if (this.cpsStage >= this.nextCPS) {
                        this.attackEntity(target, false, true);
                        break;
                    }
                    if (!this.canBlock() || !this.nearbyTargets(true)) break;
                    this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem());
                    break;
                }
                this.timerUtil.reset();
                break;
            }
            case CUBECRAFT: {
                target = this.getTarget();
                if (!event.isPre() || target == null) break;
                float[] rotations = this.getRotationsToEnt(target, this.mc.thePlayer);
                float sens = CombatUtil.getSensitivityMultiplier();
                float yaw = rotations[0] + MathUtils.getRandomInRange(-0.55f, 0.75f);
                float pitch = rotations[1] + MathUtils.getRandomInRange(-0.7f, 0.5f);
                float yawGCD = (float)Math.round(yaw / sens) * sens + 0.011111f;
                float pitchGCD = MathHelper.clamp_float((float)Math.round(pitch / sens) * sens + 0.011111f, -90.0f, 90.0f);
                event.setYaw(yawGCD);
                event.setPitch(pitchGCD);
                if (this.cpsStage >= this.nextCPS) {
                    this.attackEntity(target, false, false);
                    break;
                }
                if (!this.canBlock() || !this.nearbyTargets(true)) break;
                this.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
                break;
            }
            case SINGLE: {
                if (this.fasterSwitching.getValue().booleanValue()) {
                    if (target != null && this.mc.thePlayer.getDistanceToEntity(target) > ((Float)this.blockRange.getValue()).floatValue() || target != null && KillAura.target.isDead) {
                        target = this.getTarget();
                    } else if (target == null) {
                        target = this.getTarget();
                    }
                } else {
                    target = this.getTarget();
                }
                if (event.isPre()) {
                    if (target != null) {
                        float[] rots = this.getRotationsToEnt(target, this.mc.thePlayer);
                        event.setYaw(rots[0]);
                        event.setPitch(rots[1]);
                        break;
                    }
                    this.timerUtil.reset();
                    break;
                }
                if (target != null && this.mc.thePlayer.getDistanceToEntity(target) <= ((Float)this.range.getValue()).floatValue()) {
                    if (this.dynamic.isEnabled()) {
                        if (KillAura.target.hurtResistantTime == 0) {
                            if (this.timerUtil.sleep(ping * 4L)) {
                                this.attackEntity(target, false, true);
                            }
                        } else if (KillAura.target.hurtResistantTime <= 15) {
                            this.attackEntity(target, false, true);
                        }
                    } else if (this.cpsStage >= this.nextCPS) {
                        this.attackEntity(target, false, true);
                    }
                } else {
                    this.timerUtil.reset();
                }
                if (!this.canBlock() || !this.nearbyTargets(true)) break;
                this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem());
                break;
            }
            case TICK: {
                target = this.getTarget();
                if (event.isPre()) {
                    this.lowerTicks();
                    if (target == null) break;
                    float[] rots = this.getRotationsToEnt(target, this.mc.thePlayer);
                    event.setYaw(rots[0]);
                    event.setPitch(rots[1]);
                    break;
                }
                if (target != null && this.isValidTicks(target)) {
                    if (!this.durability.isEnabled()) {
                        this.mc.thePlayer.swingItem();
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity((Entity)target, C02PacketUseEntity.Action.ATTACK));
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity((Entity)target, C02PacketUseEntity.Action.ATTACK));
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity((Entity)target, C02PacketUseEntity.Action.ATTACK));
                        KillAura.target.auraticks = 10;
                    } else {
                        this.mc.thePlayer.swingItem();
                        this.swap(12, this.mc.thePlayer.inventory.currentItem);
                        this.attackEntity(target, false, true);
                        this.critical();
                        this.attackEntity(target, true, true);
                        this.swap(21, this.mc.thePlayer.inventory.currentItem);
                        this.attackEntity(target, false, true);
                        this.critical();
                        this.attackEntity(target, true, true);
                        this.swap(12, this.mc.thePlayer.inventory.currentItem);
                        this.attackEntity(target, false, true);
                        this.critical();
                        this.attackEntity(target, true, true);
                        ItemStack[] items = target.getInventory();
                        ItemStack helm = null;
                        if (items[3] != null) {
                            helm = items[3];
                        }
                        if (helm != null) {
                            float oldDura = helm.getMaxDamage();
                            float newDura = helm.getItemDamage();
                            float ey = oldDura - newDura;
                            float damagedone = oldDura - ey;
                            Printer.print("\u00a7f" + target.getName() + "\u00a77's Helmet Dura: \u00a7f" + ey + " (" + damagedone + ")");
                        }
                        KillAura.target.auraticks = 11;
                    }
                }
                if (!this.canBlock() || !this.nearbyTargets(true)) break;
                this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem());
                break;
            }
            case SWITCH: {
                target = this.getTarget();
                if (event.isPre()) {
                    ArrayList targs = new ArrayList();
                    this.mc.theWorld.getLoadedEntityList().stream().filter(entity -> entity instanceof EntityLivingBase).filter(entity -> this.isTargetable((EntityLivingBase)entity, this.mc.thePlayer, false)).forEach(potentialTarget -> {
                        if (targs.size() < (Integer)this.maxTargets.getValue()) {
                            targs.add((EntityLivingBase)potentialTarget);
                        }
                    });
                    if (this.switchTimer.sleep(((Integer)this.SwitchSpeed.getValue()).intValue()) && !targs.isEmpty()) {
                        this.switchI = this.switchI + 1 > targs.size() - 1 || targs.size() < 2 ? 0 : ++this.switchI;
                    }
                    if (!targs.isEmpty()) {
                        target = (EntityLivingBase)targs.get(Math.min(this.switchI, targs.size() - 1));
                    }
                    if (target != null && !this.isTargetable(target, this.mc.thePlayer, false)) {
                        target = null;
                    }
                    if (target != null && this.mc.thePlayer != null) {
                        float[] rots = this.getRotationsToEnt(target, this.mc.thePlayer);
                        float yaw = rots[0] + MathUtils.getRandomInRange(-1.8f, 1.8f);
                        float pitch = rots[1] + MathUtils.getRandomInRange(-3.8f, 3.8f);
                        float sens = CombatUtil.getSensitivityMultiplier();
                        float yawGCD = (float)Math.round(yaw / sens) * sens;
                        float pitchGCD = (float)Math.round(pitch / sens) * sens;
                        event.setYaw(yawGCD);
                        event.setPitch(pitchGCD);
                        if (this.dynamic.isEnabled()) {
                            if (KillAura.target.hurtResistantTime == 0) {
                                if (this.timerUtil.sleep(ping * 3L)) {
                                    this.attackEntity(target, false, true);
                                }
                            } else if (KillAura.target.hurtResistantTime <= 9 + pingDelay) {
                                this.attackEntity(target, false, true);
                            }
                        } else if (this.cpsStage >= this.nextCPS) {
                            this.attackEntity(target, false, true);
                        }
                    } else {
                        this.timerUtil.reset();
                    }
                }
                if (event.isPre() || !this.canBlock() || !this.nearbyTargets(true)) break;
                this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem());
                break;
            }
            case MULTINCP: {
                ArrayList<EntityLivingBase> dynamicTargets = new ArrayList<EntityLivingBase>();
                this.mc.theWorld.getLoadedEntityList().stream().filter(entity -> dynamicTargets.size() < 10 && entity instanceof EntityLivingBase).filter(entity -> this.isTargetable((EntityLivingBase)entity, this.mc.thePlayer, false) && entity.hurtResistantTime <= 16).forEach(potentialTarget -> dynamicTargets.add((EntityLivingBase)potentialTarget));
                if (event.isPre()) {
                    ++this.switchTicks;
                    if (!dynamicTargets.isEmpty() && dynamicTarget == null) {
                        dynamicTargets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                        dynamicTargets.stream().limit(3L).collect(Collectors.toList()).sort(Comparator.comparingDouble(CombatUtil::serverYawDist));
                        target = dynamicTarget = (EntityLivingBase)dynamicTargets.get(0);
                        this.switchTicks = 0;
                        this.cpsStage = 0;
                    }
                    if (this.mc.thePlayer != null && dynamicTarget != null) {
                        float[] rots = this.getRotationsToEnt(dynamicTarget, this.mc.thePlayer);
                        float yaw = CombatUtil.updateRotation(this.lastYaw, rots[0], 30.0f - MathUtils.getRandomInRange(0.1f, 2.5f));
                        event.setYaw(yaw);
                        event.setPitch(rots[1]);
                        this.lastYaw = event.getYaw();
                        this.lastPitch = event.getPitch();
                        target = dynamicTarget;
                    }
                } else {
                    if (dynamicTarget != null && this.mc.thePlayer != null) {
                        if (this.isTargetable(dynamicTarget, this.mc.thePlayer, false) && dynamicTarget.isEntityAlive()) {
                            if (this.dynamic.isEnabled()) {
                                if (KillAura.dynamicTarget.hurtResistantTime == 0) {
                                    if (this.timerUtil.sleep(ping * 3L)) {
                                        this.attackEntity(dynamicTarget, false, true);
                                        if (dynamicTargets.size() > 1) {
                                            dynamicTarget = null;
                                        }
                                    }
                                } else if (KillAura.dynamicTarget.hurtResistantTime <= 14) {
                                    this.attackEntity(dynamicTarget, false, true);
                                    if (dynamicTargets.size() > 1) {
                                        dynamicTarget = null;
                                    }
                                }
                            } else if (this.cpsStage >= this.nextCPS) {
                                this.attackEntity(dynamicTarget, false, true);
                                if (dynamicTargets.size() > 1) {
                                    dynamicTarget = null;
                                }
                            }
                        } else {
                            dynamicTarget = null;
                        }
                    }
                    if (this.canBlock() && this.nearbyTargets(true)) {
                        this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem());
                    }
                }
                target = dynamicTarget;
                break;
            }
            case MULTI: {
                target = this.findMostCrowdedEntity();
                if (event.isPre()) {
                    if (target != null) {
                        float[] rotations = this.getRotationsToEnt(target, this.mc.thePlayer);
                        event.setYaw(rotations[0]);
                        event.setPitch(rotations[1]);
                        this.oldYaw = event.getYaw();
                    }
                    if (target == null) break;
                    this.targets = this.getMultiTargets();
                    if (this.cpsStage < this.nextCPS + (this.targets.size() - 1) * pingDelay) break;
                    this.attackEntity(target, false, true);
                    this.targets.stream().filter(t -> t.hurtResistantTime <= 9 + pingDelay).forEach(t -> {
                        if (t != target) {
                            float[] rotations = this.getRotationsToEnt((Entity)t, this.mc.thePlayer);
                            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(rotations[0], rotations[1], event.isOnGround()));
                            this.attackEntity((EntityLivingBase)t, false, true);
                        }
                    });
                    break;
                }
                if (!this.canBlock() || !this.nearbyTargets(true)) break;
                this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem());
                break;
            }
            case SMOOTH: {
                target = this.getTarget();
                if (!event.isPre()) break;
                if (target != null) {
                    if (criticals.isEnabled() && this.mc.thePlayer.onGround && !this.isInsideBlock()) {
                        event.setY(event.getY() + 0.01);
                        event.setOnGround(false);
                    }
                    float[] dstAngle = this.getRotationsToEnt(target, this.mc.thePlayer);
                    float[] srcAngle = new float[]{this.serverAngles[0], this.serverAngles[1]};
                    this.serverAngles = this.smoothAngle(dstAngle, srcAngle);
                    event.setYaw(this.serverAngles[0]);
                    event.setPitch(this.serverAngles[1]);
                    if (!(this.getDistance(this.prevRotations) < 1.0f) || this.mc.thePlayer.isBlocking()) break;
                    if (this.dynamic.isEnabled()) {
                        if (KillAura.target.hurtResistantTime == 0) {
                            if (!this.timerUtil.sleep(ping * 3L)) break;
                            this.attackEntity(target, false, true);
                            break;
                        }
                        if (KillAura.target.hurtResistantTime > 9 + pingDelay) break;
                        this.attackEntity(target, false, true);
                        break;
                    }
                    if (this.cpsStage < this.nextCPS) break;
                    this.attackEntity(target, false, true);
                    break;
                }
                this.serverAngles[0] = this.mc.thePlayer.rotationYaw;
                this.serverAngles[1] = this.mc.thePlayer.rotationPitch;
                this.timerUtil.reset();
            }
        }
    }

    @Handler(value=Render3DEvent.class)
    public void onRender3d(Render3DEvent event) {
        Scaffold scaffold;
        if (this.rangeCircle.getValue().booleanValue()) {
            float xplayer = (float)RenderUtil.interpolate(this.mc.thePlayer.posX, this.mc.thePlayer.lastTickPosX, this.mc.timer.renderPartialTicks);
            float yplayer = (float)RenderUtil.interpolate(this.mc.thePlayer.posY, this.mc.thePlayer.lastTickPosY, this.mc.timer.renderPartialTicks);
            float zplayer = (float)RenderUtil.interpolate(this.mc.thePlayer.posZ, this.mc.thePlayer.lastTickPosZ, this.mc.timer.renderPartialTicks);
            GL11.glPushMatrix();
            GL11.glLineWidth((float)2.0f);
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.translate(-this.mc.getRenderManager().renderPosX, -this.mc.getRenderManager().renderPosY, -this.mc.getRenderManager().renderPosZ);
            Color clr = new Color(HUD.getColorHUD());
            GL11.glEnable((int)2848);
            GL11.glBegin((int)3);
            for (int i = 0; i <= 360; ++i) {
                GL11.glColor4f((float)((float)clr.getRed() / 255.0f), (float)((float)clr.getGreen() / 255.0f), (float)((float)clr.getBlue() / 255.0f), (float)((float)clr.getAlpha() / 255.0f));
                GL11.glVertex3d((double)((double)xplayer + (double)((Float)this.range.getValue()).floatValue() * Math.cos((double)i * Math.PI / 180.0)), (double)yplayer, (double)((double)zplayer + (double)((Float)this.range.getValue()).floatValue() * Math.sin((double)i * Math.PI / 180.0)));
            }
            GL11.glEnd();
            GL11.glDisable((int)2848);
            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GL11.glPopMatrix();
        }
        if ((scaffold = (Scaffold)Moon.INSTANCE.getModuleManager().getModule("Scaffold")).isEnabled() && !scaffold.killAura.isEnabled()) {
            return;
        }
        if (this.targetEsp.isEnabled()) {
            switch ((targetESPMode)((Object)this.targetEspModes.getValue())) {
                case JELLO: {
                    this.renderJelloCircle();
                    break;
                }
                case BOXY: {
                    if (this.modes.getValue() == mode.MULTI && target != null) {
                        this.targets.forEach(target2 -> {
                            if (this.isTargetable((EntityLivingBase)target2, this.mc.thePlayer, false)) {
                                double x = RenderUtil.interpolate(target2.posX, target2.lastTickPosX, event.getPartialTicks());
                                double y = RenderUtil.interpolate(target2.posY, target2.lastTickPosY, event.getPartialTicks());
                                double z = RenderUtil.interpolate(target2.posZ, target2.lastTickPosZ, event.getPartialTicks());
                                this.drawEntityESP(x - this.mc.getRenderManager().renderPosX, y + (double)target2.height + 0.1 - (double)target2.height - this.mc.getRenderManager().renderPosY, z - this.mc.getRenderManager().renderPosZ, target2.height, 0.65, new Color(target2.hurtTime > 0 ? 14890790 : RenderUtil.getRainbow(4000, 0, 0.85f)));
                            }
                        });
                        break;
                    }
                    if (target == null || KillAura.target.isDead) break;
                    double x = RenderUtil.interpolate(KillAura.target.posX, KillAura.target.lastTickPosX, event.getPartialTicks());
                    double y = RenderUtil.interpolate(KillAura.target.posY, KillAura.target.lastTickPosY, event.getPartialTicks());
                    double z = RenderUtil.interpolate(KillAura.target.posZ, KillAura.target.lastTickPosZ, event.getPartialTicks());
                    this.drawEntityESP(x - this.mc.getRenderManager().renderPosX, y + (double)KillAura.target.height + 0.1 - (double)KillAura.target.height - this.mc.getRenderManager().renderPosY, z - this.mc.getRenderManager().renderPosZ, KillAura.target.height, 0.65, new Color(KillAura.target.hurtTime > 0 ? 14890790 : RenderUtil.getRainbow(4000, 0, 0.85f)));
                }
            }
        }
        if (this.getTarget() != null) {
            this.lastTarget = this.getTarget();
        }
    }

    public void onShadering() {
        Scaffold scaffold = (Scaffold)Moon.INSTANCE.getModuleManager().getModule("Scaffold");
        if (scaffold.isEnabled() && !scaffold.killAura.isEnabled()) {
            return;
        }
        if (this.targetEsp.isEnabled()) {
            switch ((targetESPMode)((Object)this.targetEspModes.getValue())) {
                case JELLO: {
                    this.mc.entityRenderer.setupCameraTransform(this.mc.timer.renderPartialTicks, 0);
                    this.renderJelloCircle();
                    this.mc.entityRenderer.setupOverlayRendering();
                }
            }
        }
    }

    public void renderJelloCircle() {
        if (this.lastTarget != null) {
            int i2;
            this.transUtil.interpolate(target != null && !KillAura.target.isDead ? 100.0 : 0.0, this.transUtil.getPosY(), 8.0f / (float)Minecraft.getDebugFPS());
            float r = (float)this.targetESPColor.getColor().getRed() / 255.0f;
            float g = (float)this.targetESPColor.getColor().getGreen() / 255.0f;
            float b = (float)this.targetESPColor.getColor().getBlue() / 255.0f;
            float yPos = (float)(Math.sin((double)System.currentTimeMillis() * 0.0011 * Math.PI) * (double)(-this.lastTarget.height) / 2.0 + (double)(this.lastTarget.height / 2.0f));
            GL11.glPushMatrix();
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2929);
            GL11.glDisable((int)3008);
            GL11.glDisable((int)2884);
            GL11.glEnable((int)2848);
            double x = RenderUtil.interpolate(this.lastTarget.posX, this.lastTarget.lastTickPosX, this.mc.timer.renderPartialTicks) - this.mc.getRenderManager().renderPosX;
            double y = RenderUtil.interpolate(this.lastTarget.posY + (double)yPos, this.lastTarget.lastTickPosY + (double)yPos, this.mc.timer.renderPartialTicks) - this.mc.getRenderManager().renderPosY;
            double z = RenderUtil.interpolate(this.lastTarget.posZ, this.lastTarget.lastTickPosZ, this.mc.timer.renderPartialTicks) - this.mc.getRenderManager().renderPosZ;
            GL11.glShadeModel((int)7425);
            double x2 = RenderUtil.interpolate(this.lastTarget.posX, this.lastTarget.lastTickPosX, this.mc.timer.renderPartialTicks) - this.mc.getRenderManager().renderPosX;
            double y2 = RenderUtil.interpolate(this.lastTarget.posY + (double)yPos, this.lastTarget.lastTickPosY + (double)yPos, this.mc.timer.renderPartialTicks) - this.mc.getRenderManager().renderPosY;
            double z2 = RenderUtil.interpolate(this.lastTarget.posZ, this.lastTarget.lastTickPosZ, this.mc.timer.renderPartialTicks) - this.mc.getRenderManager().renderPosZ;
            GL11.glLineWidth((float)1.5f);
            GL11.glPushMatrix();
            GL11.glBegin((int)5);
            for (i2 = 0; i2 <= 360; ++i2) {
                GL11.glColor4f((float)r, (float)g, (float)b, (float)((float)Math.max(Math.max(this.transUtil.getPosX() - 50.0, 1.0E-20) / 100.0, 0.0)));
                GL11.glVertex3d((double)(x2 + (double)this.lastTarget.width * Math.cos((double)i2 * Math.PI / 180.0)), (double)y2, (double)(z2 + (double)this.lastTarget.width * Math.sin((double)i2 * Math.PI / 180.0)));
                GL11.glColor4f((float)r, (float)g, (float)b, (float)0.0f);
                GL11.glVertex3d((double)(x2 + (double)this.lastTarget.width * Math.cos((double)i2 * Math.PI / 180.0)), (double)(y2 - Math.cos((double)System.currentTimeMillis() * 0.0011 * Math.PI) * -0.4), (double)(z2 + (double)this.lastTarget.width * Math.sin((double)i2 * Math.PI / 180.0)));
                GlStateManager.resetColor();
            }
            GL11.glEnd();
            GL11.glLineWidth((float)1.0f);
            GL11.glBegin((int)2);
            for (i2 = 0; i2 <= 360; ++i2) {
                GL11.glColor4f((float)r, (float)g, (float)b, (float)((float)Math.max(Math.max(this.transUtil.getPosX() - 20.0, 1.0E-20) / 100.0, 0.0)));
                GL11.glVertex3d((double)(x + (double)this.lastTarget.width * Math.cos((double)i2 * Math.PI / 180.0)), (double)y, (double)(z + (double)this.lastTarget.width * Math.sin((double)i2 * Math.PI / 180.0)));
                GlStateManager.resetColor();
            }
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glShadeModel((int)7424);
            GL11.glEnable((int)3008);
            GL11.glEnable((int)2929);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2884);
            GL11.glDisable((int)2848);
            GL11.glDisable((int)3042);
            GL11.glPopMatrix();
        }
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        Scaffold scaffold = (Scaffold)Moon.INSTANCE.getModuleManager().getModule("Scaffold");
        if (scaffold.isEnabled() && !scaffold.killAura.isEnabled()) {
            return;
        }
        Criticals critical = (Criticals)Moon.INSTANCE.getModuleManager().getModule("criticals");
        if (event.isSending() && event.getPacket() instanceof C03PacketPlayer) {
            this.prevRotations[0] = ((C03PacketPlayer)event.getPacket()).getYaw();
            this.prevRotations[1] = ((C03PacketPlayer)event.getPacket()).getPitch();
            this.prevRotations[1] = ((C03PacketPlayer)event.getPacket()).getPitch();
        }
        if (event.isSending() && event.getPacket() instanceof C0APacketAnimation && (critical.isEnabled() && target != null || critical.isEnabled() && dynamicTarget != null)) {
            this.critical();
        }
    }

    private List<EntityLivingBase> getMultiTargets() {
        ArrayList<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
        int targets = 0;
        for (Entity entity : this.mc.theWorld.getLoadedEntityList()) {
            if (targets >= (Integer)this.multiTargets.getValue()) break;
            if (!(entity instanceof EntityLivingBase)) continue;
            EntityLivingBase living = (EntityLivingBase)entity;
            if (entities.size() >= (Integer)this.maxTargets.getValue() || living.hurtResistantTime > 15 || !this.isTargetable(living, this.mc.thePlayer, false) || !this.isWithinFOV(living, this.oldYaw, ((Integer)this.multiFov.getValue()).intValue()) && this.modes.getValue() == mode.MULTI) continue;
            entities.add(living);
            ++targets;
        }
        return entities;
    }

    private EntityLivingBase findMostCrowdedEntity() {
        ArrayList<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
        for (Entity entity : this.mc.theWorld.getLoadedEntityList()) {
            if (!(entity instanceof EntityLivingBase)) continue;
            entities.add((EntityLivingBase)entity);
        }
        EntityLivingBase best = null;
        int numBestEntities = -1;
        for (EntityLivingBase e : entities) {
            if (!this.isTargetable(e, this.mc.thePlayer, false)) continue;
            int closeEntities = 0;
            float yaw = this.getRotationsToEnt(e, this.mc.thePlayer)[0];
            for (EntityLivingBase e1 : entities) {
                if (!this.isTargetable(e1, this.mc.thePlayer, false) || !this.isWithinFOV(e1, yaw, ((Integer)this.multiFov.getValue()).intValue())) continue;
                ++closeEntities;
            }
            if (closeEntities <= numBestEntities) continue;
            numBestEntities = closeEntities;
            best = e;
        }
        return best;
    }

    private boolean isWithinFOV(EntityLivingBase entity, float yaw, double fov) {
        float[] rotations = this.getRotationsToEnt(entity, this.mc.thePlayer);
        float yawDifference = this.getYawDifference(yaw % 360.0f, rotations[0]);
        return (double)yawDifference < fov && (double)yawDifference > -fov;
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

    public boolean isWitherTeam(EntityLivingBase target) {
        if (!this.teams.isEnabled()) {
            return false;
        }
        boolean teamChecks = false;
        EnumChatFormatting myCol = null;
        EnumChatFormatting enemyCol = null;
        if (target != null) {
            for (EnumChatFormatting col : EnumChatFormatting.values()) {
                if (col == EnumChatFormatting.RESET) continue;
                if (this.mc.thePlayer.getDisplayName().getFormattedText().contains(col.toString()) && myCol == null) {
                    myCol = col;
                }
                if (!target.getDisplayName().getFormattedText().contains(col.toString()) || enemyCol != null) continue;
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
        return teamChecks;
    }

    public boolean isTeammate(EntityPlayer target) {
        boolean teamChecks = false;
        if (this.mc.thePlayer.getDistanceToEntity(target) <= ((Float)this.blockRange.getValue()).floatValue()) {
            if (!this.teams.isEnabled()) {
                return false;
            }
            EnumChatFormatting myCol = null;
            EnumChatFormatting enemyCol = null;
            for (EnumChatFormatting col : EnumChatFormatting.values()) {
                if (col == EnumChatFormatting.RESET) continue;
                if (this.mc.thePlayer.getDisplayName().getFormattedText().contains(col.toString()) && myCol == null) {
                    myCol = col;
                }
                if (!target.getDisplayName().getFormattedText().substring(0, 2).contains(col.toString()) || enemyCol != null) continue;
                enemyCol = col;
            }
            try {
                if (myCol != null && enemyCol != null) {
                    teamChecks = myCol == enemyCol;
                } else if (this.mc.thePlayer.getTeam() != null) {
                    teamChecks = this.mc.thePlayer.isOnSameTeam(target);
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

    private void drawFace(double x, double y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight, AbstractClientPlayer target) {
        try {
            ResourceLocation skin = target.getLocationSkin();
            this.mc.getTextureManager().bindTexture(skin);
            GL11.glEnable((int)3042);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            Gui.drawScaledCustomSizeModalRect((int)x, (int)y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
            GL11.glDisable((int)3042);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private int getHealthColor(EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0f, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0f, 1.0f, 0.75f) | 0xFF000000;
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

    private boolean nearbyTargets(boolean block) {
        for (Object e : this.mc.theWorld.loadedEntityList) {
            if (!(e instanceof EntityLivingBase) || !this.isTargetable((EntityLivingBase)e, this.mc.thePlayer, block)) continue;
            return true;
        }
        return false;
    }

    private boolean canBlock() {
        return this.autoblock.isEnabled() && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
    }

    private void critical() {
        Criticals critical = (Criticals)Moon.INSTANCE.getModuleManager().getModule("criticals");
        double[] ncpOffsets = new double[]{0.0625, 0.0};
        double[] watchdogOffsets = new double[]{0.0624f, 1.0E-4f, 0.0224f, 1.0E-4f};
        if (!ServerUtils.isOnHypixel()) {
            watchdogOffsets = ncpOffsets;
        }
        if (!(MathUtils.getBlockUnderPlayer(this.mc.thePlayer, 0.06) instanceof BlockStairs) && this.canCritical() && !(MathUtils.getBlockUnderPlayer(this.mc.thePlayer, 0.06) instanceof BlockSlab)) {
            if (critical.criticalsMode.getValue() == Criticals.mode.WATCHDOG) {
                double delay = 100.0;
                if (target != null && KillAura.target.hurtResistantTime == 0 || dynamicTarget != null && KillAura.dynamicTarget.hurtResistantTime == 0) {
                    delay = 425.0;
                }
                if ((target != null && KillAura.target.hurtResistantTime <= 13 || dynamicTarget != null && KillAura.dynamicTarget.hurtResistantTime <= 13) && Step.stepTicks > 2 && (double)(System.currentTimeMillis() - this.time) >= delay) {
                    for (double offset : watchdogOffsets) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + offset + (double)0.00113f, this.mc.thePlayer.posZ, false));
                    }
                    this.time = System.currentTimeMillis();
                }
            } else if (critical.criticalsMode.getValue() == Criticals.mode.NCP && this.canCritical() && KillAura.target.hurtResistantTime <= 13) {
                for (double offset : ncpOffsets) {
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + offset, this.mc.thePlayer.posZ, false));
                }
            }
        }
    }

    private float[] smoothAngle(float[] dst, float[] src) {
        float[] SmoothedAngle = new float[]{src[0] - dst[0], src[1] - dst[1]};
        SmoothedAngle = MathUtils.constrainAngle(SmoothedAngle);
        SmoothedAngle[0] = src[0] - SmoothedAngle[0] / 2.0f;
        SmoothedAngle[1] = src[1] - SmoothedAngle[1] / 25.0f * (float)MathUtils.getRandomInRange(3, 8);
        return SmoothedAngle;
    }

    private float getDistance(float[] original) {
        float yaw = MathHelper.wrapAngleTo180_float(this.serverAngles[0]) - MathHelper.wrapAngleTo180_float(original[0]);
        float pitch = MathHelper.wrapAngleTo180_float(this.serverAngles[1]) - MathHelper.wrapAngleTo180_float(original[1]);
        return (float)Math.sqrt(yaw * yaw + pitch * pitch);
    }

    private void attackEntity(EntityLivingBase entity, boolean dura, boolean unblock) {
        this.nextCPS = this.getNewCps();
        this.cpsStage = 0;
        Moon.INSTANCE.getEventBus().fireEvent(new SwingEvent());
        if (this.canBlock()) {
            this.mc.playerController.syncCurrentPlayItem();
            if (unblock) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        }
        if (dura) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
            this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity((Entity)entity, C02PacketUseEntity.Action.ATTACK));
        } else {
            this.mc.thePlayer.swingItem();
            this.mc.playerController.attackEntity(this.mc.thePlayer, entity);
        }
        for (int i = 0; i < (Integer)this.particlesMultiplier.getValue(); ++i) {
            boolean flag = this.mc.thePlayer.fallDistance > 0.0f && !this.mc.thePlayer.onGround && !this.mc.thePlayer.isOnLadder() && !this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isPotionActive(Potion.blindness) && this.mc.thePlayer.ridingEntity == null && entity instanceof EntityLivingBase;
            float f1 = entity != null ? EnchantmentHelper.func_152377_a(this.mc.thePlayer.getHeldItem(), entity.getCreatureAttribute()) : EnchantmentHelper.func_152377_a(this.mc.thePlayer.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
            if (flag) {
                this.mc.thePlayer.onCriticalHit(entity);
            }
            if (!(f1 > 0.0f)) continue;
            this.mc.thePlayer.onEnchantmentCritical(entity);
        }
    }

    private boolean canCritical() {
        return this.mc.thePlayer.onGround && !Moon.INSTANCE.getModuleManager().getModule("speed").isEnabled() && !this.mc.gameSettings.keyBindJump.isKeyDown() && this.mc.thePlayer.fallDistance == 0.0f;
    }

    private int getNewCps() {
        double range = MathUtils.getRandomInRange(this.cps.getLeftValue(), this.cps.getRightValue());
        range = 20.0 / range;
        if (this.mc.thePlayer.ticksExisted % 3 != 0) {
            range += (double)Math.round(MathUtils.getRandomInRange(-1.15, 1.15));
        }
        if (this.mc.thePlayer.ticksExisted % 45 == 0) {
            range += (double)MathUtils.getRandomInRange(1, 3);
        }
        range = Math.round(Math.max(range, 1.0));
        return (int)range;
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
                if (!(object instanceof EntityLivingBase) || !((double)this.mc.thePlayer.getDistanceToEntity(e = (EntityLivingBase)object) < Dist) || !(this.mc.thePlayer.getDistanceToEntity(e) < ((Float)this.range.getValue()).floatValue()) || !this.isTargetable(e, this.mc.thePlayer, false)) continue;
                this.targets.add(e);
            }
        }
        if (this.targets.isEmpty()) {
            return null;
        }
        switch ((sortModes)((Object)this.sortMode.getValue())) {
            case FOV: {
                this.targets.sort(Comparator.comparingDouble(this::yawDist));
                break;
            }
            case HEALTH: {
                this.targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                break;
            }
            case DISTANCE: {
                this.targets.sort(Comparator.comparingDouble(target -> this.mc.thePlayer.getDistanceToEntity((Entity)target)));
            }
        }
        return this.targets.get(0);
    }

    private boolean isValidTicks(EntityLivingBase target) {
        return target.auraticks == 0 && this.isTargetable(target, this.mc.thePlayer, false);
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

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isTargetable(EntityLivingBase entity, EntityPlayerSP clientPlayer, boolean b) {
        boolean isTeamMob = false;
        if (entity instanceof EntityWither) {
            float f = clientPlayer.getDistanceToEntity(entity);
            Float f2 = b ? (Float)this.blockRange.getValue() : (Float)this.range.getValue();
            if (f <= f2.floatValue() && this.monsters.isEnabled()) {
                EntityWither a = (EntityWither)entity;
                boolean teamChecks = false;
                EnumChatFormatting myCol = null;
                EnumChatFormatting enemyCol = null;
                for (EnumChatFormatting col : EnumChatFormatting.values()) {
                    if (col == EnumChatFormatting.RESET) continue;
                    if (this.mc.thePlayer.getDisplayName().getFormattedText().substring(0, 2).contains(col.toString()) && myCol == null) {
                        myCol = col;
                    }
                    if (!a.getDisplayName().getFormattedText().contains(col.toString()) || enemyCol != null) continue;
                    enemyCol = col;
                }
                try {
                    if (myCol != null && enemyCol != null) {
                        isTeamMob = teamChecks = myCol == enemyCol;
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        if (!this.teams.isEnabled()) {
            isTeamMob = false;
        }
        if (entity.getUniqueID() == clientPlayer.getUniqueID()) return false;
        if (entity instanceof EntityPlayer) {
            if (this.isTeammate((EntityPlayer)entity)) return false;
        }
        if (AntiBot.getBots().contains(entity)) return false;
        if (Moon.INSTANCE.getFriendManager().isFriend(entity.getName())) return false;
        if (entity.isInvisible()) {
            if (!this.invisibles.isEnabled()) return false;
        }
        if (!(clientPlayer.getDistanceToEntity(entity) <= (b ? (Float)this.blockRange.getValue() : (Float)this.range.getValue()).floatValue())) {
            if (this.modes.getValue() != mode.AAC) return false;
            float f = clientPlayer.getDistanceToEntity(entity);
            float f3 = b ? ((Float)this.blockRange.getValue()).floatValue() : 6.0f;
            if (!(f <= f3)) return false;
        }
        if (entity instanceof EntityPlayer) {
            if (this.players.isEnabled()) return true;
        }
        if ((entity instanceof EntityMob || entity instanceof EntityGolem || entity instanceof EntitySlime) && this.monsters.isEnabled()) {
            if (!isTeamMob) return true;
        }
        if (!(entity instanceof EntityVillager)) {
            if (!(entity instanceof EntityAnimal)) return false;
        }
        if (!this.animals.isEnabled()) return false;
        return true;
    }

    private boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().minY); y < MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                    Block block = this.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block == null || block instanceof BlockAir) continue;
                    AxisAlignedBB boundingBox = block.getCollisionBoundingBox(this.mc.theWorld, new BlockPos(x, y, z), this.mc.theWorld.getBlockState(new BlockPos(x, y, z)));
                    if (block instanceof BlockHopper) {
                        boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                    }
                    if (boundingBox == null || !this.mc.thePlayer.getEntityBoundingBox().intersectsWith(boundingBox)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 255.0f);
        GlStateManager.translate(posX, posY, 50.0f);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-((float)Math.atan(mouseY / 40.0f)) * 20.0f, 1.0f, 0.0f, 0.0f);
        ent.renderYawOffset = mouseX;
        ent.rotationYaw = mouseX;
        ent.rotationPitch = -mouseY;
        ent.rotationYawHead = mouseX;
        ent.prevRotationYawHead = mouseX;
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(ent, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    private static enum targetESPMode {
        JELLO("Jello"),
        BOXY("Boxy");

        private final String name;

        private targetESPMode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    private static enum mode {
        SINGLE("Single"),
        SWITCH("Switch"),
        TICK("Tick"),
        SMOOTH("Smooth"),
        CUBECRAFT("Cubecraft"),
        AAC("AAC"),
        INTAVE("Intave"),
        FAITHFUL("Faithful"),
        EXPERIMENTAL("Experimental"),
        MULTI("Multi"),
        MULTINCP("MultiNCP"),
        OLDAAC("Old AAC");

        private final String name;

        private mode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    private static enum sortModes {
        FOV("FoV"),
        HEALTH("Health"),
        DISTANCE("Distance");

        private final String name;

        private sortModes(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

