/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.player.MotionEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.MathUtils;
import me.moon.utils.game.MoveUtil;
import me.moon.utils.game.ServerUtils;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.potion.Potion;

public class LongJump
extends Module {
    private double moveSpeed;
    private double lastDist;
    private double startingY;
    private int stage = 1;
    private int level;
    private boolean hasDamaged;
    private final EnumValue<Mode> mode = new EnumValue<Mode>("Mode", Mode.WATCHDOG);
    private final NumberValue<Double> boostVal = new NumberValue<Double>("Boost", 3.0, 0.1, 3.0, 0.1, this.mode, "Vanilla");
    private final NumberValue<Float> hypixelBoost = new NumberValue<Float>("Watchdog Boost", Float.valueOf(0.6f), Float.valueOf(0.05f), Float.valueOf(3.0f), Float.valueOf(0.01f), this.mode, "Watchdog");
    private final BooleanValue damage = new BooleanValue("Damage", false, (Value)this.mode, "Watchdog");
    private final BooleanValue glide = new BooleanValue("Glide", false, (Value)this.mode, "Watchdog");
    private final NumberValue<Float> cubeMotion = new NumberValue<Float>("Cubecraft Boost", Float.valueOf(0.1f), Float.valueOf(0.0f), Float.valueOf(3.0f), Float.valueOf(0.1f), this.mode, "Cubecraft");
    private final NumberValue<Float> cubeSpeed = new NumberValue<Float>("Speed Addition", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(4.0f), Float.valueOf(0.1f), this.mode, "Cubecraft");

    public LongJump() {
        super("LongJump", Module.Category.MOVEMENT, new Color(163, 148, 255, 255).getRGB());
    }

    @Handler(value=MotionEvent.class)
    public void onMotion(MotionEvent event) {
        if (this.mc.thePlayer == null) {
            return;
        }
        if (this.mc.thePlayer.isOnLiquid() || this.mc.thePlayer.isInLiquid()) {
            return;
        }
        switch ((Mode)((Object)this.mode.getValue())) {
            case VANILLA: {
                MoveUtil.setMoveSpeed(event, (Double)this.boostVal.getValue());
                if (this.mc.thePlayer.isMoving()) {
                    if (!this.mc.thePlayer.onGround) break;
                    this.mc.thePlayer.motionY = 0.41;
                    event.setY(0.41);
                    break;
                }
                this.mc.thePlayer.motionX = 0.0;
                this.mc.thePlayer.motionZ = 0.0;
                break;
            }
            case WATCHDOG: {
                switch (this.stage) {
                    case 0: {
                        this.lastDist = 0.0;
                        this.startingY = this.mc.thePlayer.posY;
                        break;
                    }
                    case 1: {
                        this.lastDist = 0.0;
                        float motionY = 0.40001f;
                        if (this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f || !this.mc.thePlayer.onGround) break;
                        if (this.mc.thePlayer.isPotionActive(Potion.jump)) {
                            motionY += (float)(this.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.101f;
                        }
                        this.mc.thePlayer.motionY = motionY;
                        event.setY(this.mc.thePlayer.motionY);
                        this.moveSpeed *= 2.14;
                        break;
                    }
                    case 2: {
                        this.moveSpeed *= (double)((Float)this.hypixelBoost.getValue()).floatValue();
                        break;
                    }
                    case 3: {
                        double difference = 0.05 * (this.lastDist - this.getBaseMoveSpeed());
                        this.moveSpeed = this.lastDist - difference;
                    }
                    default: {
                        if (!(this.mc.thePlayer.isPotionActive(Potion.jump) || this.mc.thePlayer.isCollidedHorizontally || !this.glide.isEnabled() || this.stage != 7 && this.stage != 8 && this.stage != 10 && this.stage != 13 && this.stage != 16)) {
                            this.mc.thePlayer.motionY = 0.0;
                        }
                        if ((this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically) && this.stage > 0) {
                            this.toggle();
                        }
                        this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                    }
                }
                this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
                MoveUtil.setMoveSpeed(event, this.moveSpeed);
                ++this.stage;
                break;
            }
            case MINEPLEX: {
                double speed;
                if (this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f) break;
                ++this.level;
                if (this.mc.thePlayer.onGround) {
                    if (this.level > 7) {
                        this.toggle();
                        return;
                    }
                    this.mc.thePlayer.motionY = 0.42f;
                    event.setY(0.42f);
                    this.mc.thePlayer.motionY += 0.098;
                    speed = 0.0;
                } else {
                    if (this.mc.thePlayer.motionY >= 0.0) {
                        this.mc.thePlayer.motionY += 0.04;
                    }
                    if (this.mc.thePlayer.motionY < 0.0) {
                        this.mc.thePlayer.motionY = this.mc.thePlayer.motionY > -0.3 ? (this.mc.thePlayer.motionY += 0.0331) : (this.mc.thePlayer.motionY > -0.38 ? (this.mc.thePlayer.motionY += 0.031) : (this.mc.thePlayer.motionY += 0.01));
                    }
                    if ((speed = 0.815 - (double)this.level * 0.007) < 0.0) {
                        speed = 0.0;
                    }
                }
                MoveUtil.setMoveSpeed(event, speed);
                break;
            }
            case CUBECRAFT: {
                if (this.hasDamaged) {
                    switch (this.stage) {
                        case 0: {
                            this.lastDist = 0.0;
                            this.startingY = this.mc.thePlayer.posY;
                            break;
                        }
                        case 1: {
                            this.lastDist = 0.0;
                            float motionY = 0.40001f + ((Float)this.cubeMotion.getValue()).floatValue();
                            if (this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f || !this.mc.thePlayer.onGround) break;
                            if (this.mc.thePlayer.isPotionActive(Potion.jump)) {
                                motionY += (float)(this.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.101f;
                            }
                            this.mc.thePlayer.motionY = motionY;
                            event.setY(this.mc.thePlayer.motionY);
                            this.moveSpeed *= 2.0;
                            break;
                        }
                        case 2: {
                            this.moveSpeed *= (double)(4.0f + ((Float)this.cubeSpeed.getValue()).floatValue());
                            break;
                        }
                        case 3: {
                            double difference = 0.6963 * (this.lastDist - this.getBaseMoveSpeed());
                            this.moveSpeed = this.lastDist - difference;
                        }
                        default: {
                            if ((this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically) && this.stage > 0) {
                                this.toggle();
                            }
                            this.moveSpeed = this.lastDist - this.lastDist / 156.0;
                        }
                    }
                    this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
                    MoveUtil.setMoveSpeed(event, this.moveSpeed);
                    ++this.stage;
                    break;
                }
                this.stage = 0;
                break;
            }
        }
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (this.mc.thePlayer == null) {
            return;
        }
        this.setSuffix(((Mode)((Object)this.mode.getValue())).getName());
        if (this.mc.thePlayer.isOnLiquid() || this.mc.thePlayer.isInLiquid()) {
            return;
        }
        this.lastDist = Math.sqrt((this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX) * (this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX) + (this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ) * (this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ));
        if (this.lastDist > 5.0) {
            this.lastDist = 0.0;
        }
        switch ((Mode)((Object)this.mode.getValue())) {
            case REDESKY: {
                if (!event.isPre() || !this.mc.thePlayer.isMoving()) break;
                if (this.mc.thePlayer.onGround) {
                    if (this.stage == 0) {
                        this.mc.thePlayer.jump();
                        this.stage = 0;
                        break;
                    }
                    this.toggle();
                    break;
                }
                if (++this.stage > 5) break;
                MoveUtil.setSpeed(MoveUtil.getSpeed() + 0.25);
                this.mc.thePlayer.motionY += (double)0.07f;
                break;
            }
            case CUBECRAFT: {
                if (!event.isPre()) break;
                ++this.level;
                if (!this.mc.thePlayer.onGround) break;
                this.mc.thePlayer.cubeDamagePlayer();
                if (this.mc.thePlayer.hurtTime <= 0) break;
                this.hasDamaged = true;
                break;
            }
            case WATCHDOG: {
                if (!event.isPre()) break;
                if (this.level == 0) {
                    if (this.damage.getValue().booleanValue()) {
                        this.mc.thePlayer.damagePlayer();
                    }
                    this.level = 1;
                }
                if (!ServerUtils.isOnHypixel()) break;
                if (event.getY() % 0.015625 == 0.0) {
                    event.setY(event.getY() + 5.3424E-4);
                    event.setOnGround(false);
                }
                if (!(this.mc.thePlayer.motionY > 0.3)) break;
                event.setOnGround(true);
                break;
            }
        }
    }

    private double getBaseMoveSpeed() {
        double n = 0.2873;
        if (this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            n *= 1.0 + 0.2 * (double)(this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return n;
    }

    @Override
    public void onDisable() {
        this.mc.timer.timerSpeed = 1.0f;
        if (this.mc.thePlayer != null) {
            this.moveSpeed = this.getBaseMoveSpeed();
        }
        this.hasDamaged = false;
        this.lastDist = 0.0;
    }

    @Override
    public void onEnable() {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        this.stage = 0;
        this.level = 0;
        this.lastDist = 0.0;
        this.startingY = 0.0;
        this.hasDamaged = false;
        if (this.mode.getValue() == Mode.WATCHDOG) {
            PlayerCapabilities capabilities = new PlayerCapabilities();
            capabilities.isFlying = true;
            capabilities.allowFlying = true;
            capabilities.setFlySpeed(MathUtils.getRandomInRange(0.1f, 12.0f));
            this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C13PacketPlayerAbilities(capabilities));
        }
    }

    public static enum Mode {
        WATCHDOG("Watchdog"),
        MINEPLEX("Mineplex"),
        CUBECRAFT("Cubecraft"),
        REDESKY("Redesky"),
        VERUS("Verus"),
        VANILLA("Vanilla");

        private final String name;

        private Mode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

