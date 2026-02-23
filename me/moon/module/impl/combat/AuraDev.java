/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.player.MotionEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.MathUtils;
import me.moon.utils.game.CombatUtil;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;

public class AuraDev
extends Module {
    private final NumberValue<Integer> cps = new NumberValue<Integer>("CPS", 12, 1, 20, 1);
    public final NumberValue<Float> range = new NumberValue<Float>("Range", Float.valueOf(4.2f), Float.valueOf(1.0f), Float.valueOf(7.0f), Float.valueOf(0.1f));
    private final NumberValue<Float> turnSpeed = new NumberValue<Float>("Max Turn Speed", Float.valueOf(30.0f), Float.valueOf(1.0f), Float.valueOf(180.0f), Float.valueOf(1.0f));
    private final NumberValue<Integer> particlesMultiplier = new NumberValue<Integer>("Particles Multiplier", 3, 1, 5, 1);
    private final EnumValue<targetingModes> targetingMode = new EnumValue<targetingModes>("Targeting Mode", targetingModes.DYNAMIC);
    public final EnumValue<sortModes> sortMode = new EnumValue<sortModes>("Sort Mode", sortModes.FOV, this.targetingMode, "SORT");
    private final EnumValue<rotationModes> rotationMode = new EnumValue<rotationModes>("Rotation Mode", rotationModes.BASIC);
    private final EnumValue<attackEvents> attackEvent = new EnumValue<attackEvents>("Attack Event", attackEvents.PRE);
    private final EnumValue<clickPatterns> clickPattern = new EnumValue<clickPatterns>("Click Pattern", clickPatterns.BASIC);
    private final BooleanValue unblockOnAttack = new BooleanValue("Unblock On Attack", true);
    private final EnumValue<attackMethods> attackMethod = new EnumValue<attackMethods>("Attack Method", attackMethods.ARTIFICIAL);
    private final BooleanValue autoBlock = new BooleanValue("AutoBlock", true);
    public final NumberValue<Float> blockRange = new NumberValue<Float>("Block Range", Float.valueOf(7.0f), Float.valueOf(1.0f), Float.valueOf(15.0f), Float.valueOf(0.1f), this.autoBlock, "true");
    public final EnumValue<autoBlockModes> autoBlockMode = new EnumValue<autoBlockModes>("AutoBlock Mode", autoBlockModes.PRE, (Value)this.autoBlock, "true");
    public final BooleanValue teams = new BooleanValue("Teams", "Teams Mode", false);
    public final BooleanValue players = new BooleanValue("Players", "Target Players", true);
    public final BooleanValue animals = new BooleanValue("Animals", "Target Animals", false);
    public final BooleanValue monsters = new BooleanValue("Monsters", "Target Monsters", false);
    public final BooleanValue invisibles = new BooleanValue("Invisibles", "Target Invisibles", false);
    private EntityLivingBase target;
    private float[] lastRotation = new float[]{0.0f, 0.0f};
    private int currentAttackDelay;
    private int attackDelay;
    private final CombatUtil combatUtil = new CombatUtil();

    public AuraDev() {
        super("AuraDev", Module.Category.COMBAT, new Color(0, 255, 25, 255).getRGB());
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        boolean syncBlockEvent;
        switch ((targetingModes)((Object)this.targetingMode.getValue())) {
            case DYNAMIC: {
                ArrayList<EntityLivingBase> dynamicTargets = new ArrayList<EntityLivingBase>();
                this.mc.theWorld.getLoadedEntityList().stream().filter(entity -> dynamicTargets.size() < 10 && entity instanceof EntityLivingBase).filter(entity -> this.combatUtil.isTargetable(this, (EntityLivingBase)entity, this.mc.thePlayer, false) && entity.hurtResistantTime <= 16).forEach(potentialTarget -> dynamicTargets.add((EntityLivingBase)potentialTarget));
                if (dynamicTargets.isEmpty()) break;
                dynamicTargets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                dynamicTargets.stream().limit(3L).collect(Collectors.toList()).sort(Comparator.comparingDouble(CombatUtil::serverYawDist));
                this.target = (EntityLivingBase)dynamicTargets.get(0);
                break;
            }
            case SORT: {
                this.target = this.getTarget();
            }
        }
        if (this.target != null) {
            boolean syncAttackEvent;
            if (event.isPre()) {
                float[] rotation = this.getRotation(this.target);
                float randomizedTurnSpeed = Math.max(0.0f, ((Float)this.turnSpeed.getValue()).floatValue() - MathUtils.getRandomInRange(0.1f, 2.5f));
                float yaw = CombatUtil.updateRotation(this.lastRotation[0], rotation[0], randomizedTurnSpeed);
                float pitch = rotation[1];
                switch ((rotationModes)((Object)this.rotationMode.getValue())) {
                    case BASIC: {
                        event.setYaw(yaw);
                        event.setPitch(pitch);
                        break;
                    }
                    case ADVANCED: {
                        float aimingIndex = CombatUtil.getSensitivityMultiplier();
                        yaw += MathUtils.getRandomInRange(-1.0f, 1.0f);
                        pitch += MathUtils.getRandomInRange(-1.0f, 1.0f);
                        yaw -= yaw % aimingIndex;
                        pitch -= pitch % aimingIndex;
                        pitch = MathHelper.clamp_float(pitch, -90.0f, 90.0f);
                        event.setYaw(yaw);
                        event.setPitch(pitch);
                        break;
                    }
                }
                this.lastRotation = new float[]{yaw, pitch};
            }
            boolean bl = syncAttackEvent = event.isPre() && this.attackEvent.getValue() == attackEvents.PRE || !event.isPre() && this.attackEvent.getValue() == attackEvents.POST;
            if (syncAttackEvent && ++this.currentAttackDelay >= this.attackDelay) {
                this.generateNextDelay((clickPatterns)((Object)this.clickPattern.getValue()));
                switch ((attackMethods)((Object)this.attackMethod.getValue())) {
                    case ARTIFICIAL: {
                        this.combatUtil.attackEntity(this.target, this.canBlock() && this.unblockOnAttack.isEnabled(), (Integer)this.particlesMultiplier.getValue());
                        break;
                    }
                    case MOUSE_CLICK: {
                        this.mouseClick();
                    }
                }
                if (!this.combatUtil.isTargetable(this, this.target, this.mc.thePlayer, false) || !this.target.isEntityAlive()) {
                    this.target = null;
                }
            }
        }
        boolean bl = syncBlockEvent = event.isPre() && this.autoBlockMode.getValue() == autoBlockModes.PRE || !event.isPre() && this.autoBlockMode.getValue() == autoBlockModes.POST;
        if (syncBlockEvent && this.canBlock() && this.combatUtil.nearbyTargets(this, true)) {
            this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem());
        }
    }

    @Handler(value=MotionEvent.class)
    public void onMotion(MotionEvent event) {
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
    }

    private boolean canBlock() {
        return this.autoBlock.isEnabled() && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
    }

    private void mouseClick() {
        int attackKey = this.mc.gameSettings.keyBindAttack.getKeyCode();
        KeyBinding.setKeyBindState(attackKey, true);
        KeyBinding.setKeyBindState(attackKey, false);
        KeyBinding.onTick(attackKey);
    }

    private void generateNextDelay(clickPatterns clickPattern) {
        switch (clickPattern) {
            case BASIC: {
                break;
            }
        }
        this.currentAttackDelay = 0;
        this.attackDelay = 2;
    }

    public float[] getRotation(Entity target) {
        double differenceX = target.posX - this.mc.thePlayer.posX;
        double differenceY = target.posY + (double)target.height - 0.25 - (this.mc.thePlayer.posY + (double)this.mc.thePlayer.height);
        double differenceZ = target.posZ - this.mc.thePlayer.posZ;
        float rotationYaw = (float)(Math.atan2(differenceZ, differenceX) * 180.0 / Math.PI) - 90.0f;
        float rotationPitch = MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(differenceY, Math.sqrt(differenceX * differenceX + differenceZ * differenceZ)))));
        float finishedYaw = this.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(rotationYaw - this.mc.thePlayer.rotationYaw);
        float finishedPitch = MathHelper.clamp_float(rotationPitch, -90.0f, 90.0f);
        return new float[]{finishedYaw, finishedPitch};
    }

    private EntityLivingBase getTarget() {
        ArrayList<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
        double Dist = Double.MAX_VALUE;
        if (this.mc.theWorld != null) {
            for (Object object : this.mc.theWorld.loadedEntityList) {
                EntityLivingBase e;
                if (!(object instanceof EntityLivingBase) || !((double)this.mc.thePlayer.getDistanceToEntity(e = (EntityLivingBase)object) < Dist) || !(this.mc.thePlayer.getDistanceToEntity(e) < ((Float)this.range.getValue()).floatValue()) || !this.combatUtil.isTargetable(this, e, this.mc.thePlayer, false)) continue;
                targets.add(e);
            }
        }
        if (targets.isEmpty()) {
            return null;
        }
        switch ((sortModes)((Object)this.sortMode.getValue())) {
            case FOV: {
                targets.sort(Comparator.comparingDouble(this.combatUtil::yawDistance));
                break;
            }
            case HEALTH: {
                targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                break;
            }
            case DISTANCE: {
                targets.sort(Comparator.comparingDouble(target -> this.mc.thePlayer.getDistanceToEntity((Entity)target)));
            }
        }
        return (EntityLivingBase)targets.get(0);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        this.target = null;
        this.lastRotation = new float[]{this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch};
        super.onEnable();
    }

    private static enum autoBlockModes {
        PRE("Pre"),
        POST("Post"),
        FAKE("Fake");

        private final String name;

        private autoBlockModes(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    private static enum attackMethods {
        ARTIFICIAL("Artificial"),
        MOUSE_CLICK("MouseClick");

        private final String name;

        private attackMethods(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    private static enum clickPatterns {
        BASIC("Basic"),
        ADVANCED("Advanced");

        private final String name;

        private clickPatterns(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    private static enum attackEvents {
        PRE("Pre"),
        POST("Post");

        private final String name;

        private attackEvents(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    private static enum rotationModes {
        BASIC("Basic"),
        ADVANCED("Advanced"),
        NONE("None");

        private final String name;

        private rotationModes(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    private static enum sortModes {
        FOV("Fov"),
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

    private static enum targetingModes {
        DYNAMIC("Dynamic"),
        SORT("Sort");

        private final String name;

        private targetingModes(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

