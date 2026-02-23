/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector2f
 *  org.apache.commons.lang3.StringUtils
 */
package me.moon.module.impl.movement;

import java.awt.Color;
import java.util.ArrayList;
import javax.vecmath.Vector2f;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.player.EventCollideUnderPlayer;
import me.moon.event.impl.player.MotionEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.Module;
import me.moon.module.impl.exploit.Disabler;
import me.moon.module.impl.visuals.HUD;
import me.moon.utils.MathUtils;
import me.moon.utils.game.MoveUtil;
import me.moon.utils.game.ServerUtils;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.NumberValue;
import me.moon.utils.vector.Vec3f;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.StringUtils;

public class Flight
extends Module {
    private final TimerUtil timer = new TimerUtil();
    private final EnumValue<Modes> mode = new EnumValue<Modes>("Mode", Modes.WATCHDOG);
    private final EnumValue<verusModes> verusMode = new EnumValue<verusModes>("Verus Mode", verusModes.NORMAL, this.mode, "Verus");
    private final EnumValue<BoostModes> boostModes = new EnumValue<BoostModes>("Watchdog Mode", BoostModes.DAMAGE, this.mode, "Watchdog");
    private final BooleanValue viewbob = new BooleanValue("ViewBob", true);
    private final BooleanValue boost = new BooleanValue("Boost", true, (Value)this.mode, "Watchdog");
    private final BooleanValue extra = new BooleanValue("Extra Boost", true, (Value)this.mode, "Watchdog");
    private final BooleanValue safe = new BooleanValue("Safe Damage", true, (Value)this.mode, "Watchdog");
    private final BooleanValue blink = new BooleanValue("Blink", true, (Value)this.mode, "Watchdog");
    private final BooleanValue blinkIndicator = new BooleanValue("Blink Indicator", true, (Value)this.blink, "true");
    private final NumberValue<Integer> blinkLength = new NumberValue<Integer>("Blink Length", 45, 0, 300, 1, this.blink, "true");
    private final BooleanValue dropDown = new BooleanValue("Drop Fly", true, (Value)this.mode, "Watchdog");
    private final BooleanValue damageBoost = new BooleanValue("DamageBoost", true, (Value)this.mode, "Watchdog");
    private final NumberValue<Float> flySpeed = new NumberValue<Float>("Watchdog Speed", Float.valueOf(2.0f), Float.valueOf(0.2f), Float.valueOf(10.0f), Float.valueOf(0.1f), this.mode, "Watchdog");
    private final NumberValue<Float> timerSpeed = new NumberValue<Float>("Timer Speed", Float.valueOf(2.5f), Float.valueOf(1.0f), Float.valueOf(4.0f), Float.valueOf(0.1f), this.mode, "Watchdog");
    private final NumberValue<Integer> timerLength = new NumberValue<Integer>("Timer Length", 500, 100, 1500, 50, this.mode, "Watchdog");
    private final NumberValue<Float> vanSpeed = new NumberValue<Float>("Fly Speed", Float.valueOf(2.0f), Float.valueOf(0.2f), Float.valueOf(8.0f), Float.valueOf(0.1f), this.mode, "Vanilla");
    private final NumberValue<Float> mineSpeed = new NumberValue<Float>("Max Speed", Float.valueOf(2.0f), Float.valueOf(0.2f), Float.valueOf(8.0f), Float.valueOf(0.1f), this.mode, "Mineplex");
    private final NumberValue<Float> longJumpTimer = new NumberValue<Float>("Mineplex Timer", Float.valueOf(0.6f), Float.valueOf(0.1f), Float.valueOf(1.0f), Float.valueOf(0.05f), this.mode, "Mineplex");
    private final NumberValue<Double> mineplexAcceleration = new NumberValue<Double>("Mineplex Accel", 0.085, 0.005, 0.25, 0.01, this.mode, "Mineplex");
    private double posY;
    private double moveSpeed;
    private double preStage;
    private double postStage;
    private double lastDist;
    private int level;
    private int counter;
    public boolean hasDamaged;
    public boolean goodToGo;
    public boolean vanillaFly;
    public float rotYaw = 0.0f;
    public float rotPitch = 0.0f;
    private final ArrayList<Packet> packets = new ArrayList();
    private boolean isEnabled;
    private boolean hasSwitched;
    private int elapsed;
    protected S27PacketExplosion velocity;

    public Flight() {
        super("Flight", Module.Category.MOVEMENT, new Color(33, 120, 255, 255).getRGB());
        this.setDescription("Zoom around like an epic gamer.");
    }

    @Override
    public void onDisable() {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        this.mc.timer.timerSpeed = 1.0f;
        this.moveSpeed = this.getBaseMoveSpeed();
        this.lastDist = 0.0;
        this.counter = 0;
        this.goodToGo = false;
        this.hasDamaged = false;
        this.preStage = 0.0;
        this.isEnabled = false;
        this.postStage = 0.0;
        this.packets.forEach(this.mc.thePlayer.sendQueue.getNetworkManager()::sendPacket);
        this.packets.clear();
        if (this.mode.getValue() == Modes.MINEPLEX) {
            int heldItem = this.mc.thePlayer.inventory.currentItem;
            this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(heldItem));
        }
        this.vanillaFly = false;
        super.onDisable();
    }

    @Handler(value=EventCollideUnderPlayer.class)
    public void onCollision(EventCollideUnderPlayer event) {
        if (this.mode.getValue() == Modes.COLLISION || this.mode.getValue() == Modes.VERUS && (this.verusMode.getValue() == verusModes.FLOAT || this.verusMode.getValue() == verusModes.DEV)) {
            event.getList().add(new AxisAlignedBB(this.mc.thePlayer.posX, Math.floor(this.mc.thePlayer.posY), this.mc.thePlayer.posZ));
        }
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (this.mc.thePlayer == null) {
            return;
        }
        this.setSuffix(StringUtils.capitalize((String)((Modes)((Object)this.mode.getValue())).getName()));
        if (!event.isPre()) {
            double xDist = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
            double zDist = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
        if (event.isPre() && this.viewbob.getValue().booleanValue() && this.mc.thePlayer.isMoving()) {
            this.mc.thePlayer.cameraYaw = 0.081f;
        }
        block0 : switch ((Modes)((Object)this.mode.getValue())) {
            case WATCHDOG: {
                boolean bl = this.vanillaFly = this.dropDown.isEnabled() && this.mc.thePlayer.fallDistance >= 3.0f;
                if (this.extra.isEnabled()) {
                    this.mc.timer.timerSpeed = !this.timer.hasReached(((Integer)this.timerLength.getValue()).intValue()) && this.timer.hasReached(10L) && this.goodToGo ? ((Float)this.timerSpeed.getValue()).floatValue() : 1.0f;
                    if (this.mc.thePlayer.hurtResistantTime == 19 && this.level < 20) {
                        this.timer.reset();
                    }
                }
                if (!event.isPre()) break;
                if (!this.boost.isEnabled()) {
                    ++this.level;
                }
                double motionY = 0.42f + (this.mc.thePlayer.isPotionActive(Potion.jump) ? (float)(this.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f : 0.0f);
                if (this.level > 3) {
                    if (this.level % 3 == 0) {
                        this.postStage += 1.0E-13 + MathUtils.getRandomInRange(1.0E-14, 1.0E-13);
                    }
                    event.setY(this.mc.thePlayer.posY + this.postStage);
                }
                if (ServerUtils.isOnHypixel()) {
                    event.setOnGround(true);
                }
                this.mc.thePlayer.motionY = 0.0;
                if (this.boostModes.getValue() == BoostModes.DAMAGE && this.boost.isEnabled() && !this.hasDamaged) {
                    this.mc.thePlayer.damagePlayer();
                    this.hasDamaged = true;
                    event.setY(this.mc.thePlayer.posY);
                }
                if (this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f || this.boost.isEnabled() || !this.mc.thePlayer.onGround) break;
                this.mc.thePlayer.motionY = motionY;
                break;
            }
            case VANILLA: {
                if (this.mc.gameSettings.keyBindJump.isKeyDown()) {
                    this.mc.thePlayer.motionY = ((Float)this.vanSpeed.getValue()).floatValue() / 2.0f;
                    break;
                }
                if (this.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    this.mc.thePlayer.motionY = -((Float)this.vanSpeed.getValue()).floatValue() / 2.0f;
                    break;
                }
                this.mc.thePlayer.motionY = 0.0;
                break;
            }
            case STATICGRAVITY: {
                if (this.mc.thePlayer.onGround || !(this.mc.thePlayer.fallDistance > 0.0f)) break;
                this.mc.thePlayer.motionY = -0.0784000015258789;
                event.setOnGround(false);
                break;
            }
            case CHUNKLOAD: {
                if (!this.mc.thePlayer.onGround && this.mc.thePlayer.fallDistance > 0.0f) {
                    this.mc.thePlayer.motionY = -0.09800000190734864;
                    event.setOnGround(false);
                }
                if (!this.mc.gameSettings.keyBindJump.isKeyDown() || this.mc.thePlayer.ticksExisted % 50 != 0) break;
                break;
            }
            case MINEPLEX: {
                if (event.isPre()) {
                    int heldItem = this.mc.thePlayer.inventory.currentItem;
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(heldItem));
                    break;
                }
                for (int i = 0; i < 9; ++i) {
                    if (this.mc.thePlayer.inventory.getStackInSlot(i) != null) continue;
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i));
                    break block0;
                }
                break;
            }
            case EXPERIMENTAL: {
                if (!event.isPre()) break;
                this.mc.timer.timerSpeed = 1.0f;
                if (this.mc.thePlayer.motionY <= -0.5) {
                    // empty if block
                }
                if (!((this.postStage += 1.0) >= 10.0) && ++this.counter > 1) break;
                this.postStage = 0.0;
                float yaw = (float)Math.toRadians(this.mc.thePlayer.rotationYaw);
                double horizontal = 4.0;
                double vertical = 1.5;
                if (this.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    vertical = 8.0;
                    horizontal = 0.0;
                }
                Vector2f vector = new Vector2f((float)(-Math.sin(yaw) * horizontal), (float)(Math.cos(yaw) * horizontal));
                event.setCancelled(true);
                this.mc.getNetHandler().addToSendQueueNoEvents(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX + (double)vector.x, this.mc.thePlayer.posY + vertical, this.mc.thePlayer.posZ + (double)vector.y, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, false));
                this.mc.getNetHandler().addToSendQueueNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + (double)vector.x, this.mc.thePlayer.posY + 100.0, this.mc.thePlayer.posZ + (double)vector.y, false));
                break;
            }
            case REDESKY: {
                if (!event.isPre()) break;
                this.mc.thePlayer.motionY = 0.0;
                if (this.mc.thePlayer.ticksExisted % 10 != 0) break;
                float yaw = (float)Math.toRadians(this.mc.thePlayer.rotationYaw);
                Vector2f vector = new Vector2f((float)(-Math.sin(yaw) * 9.6), (float)(Math.cos(yaw) * 9.6));
                double motion = 8.248135998590946;
                this.mc.timer.timerSpeed = 1.0f;
                for (int i = 0; i <= 5; ++i) {
                    this.mc.getNetHandler().addToSendQueueNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + (double)(vector.x * (float)i), this.mc.thePlayer.posY + 2.6 * (double)i, this.mc.thePlayer.posZ + (double)(vector.y * (float)i), true));
                }
                this.mc.getNetHandler().addToSendQueueNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + (double)vector.x, this.mc.thePlayer.posY + 100.0, this.mc.thePlayer.posZ + (double)vector.y, true));
                break;
            }
            case VERUS: {
                double y;
                double x;
                switch ((verusModes)((Object)this.verusMode.getValue())) {
                    case NORMAL: {
                        if (!event.isPre()) break;
                        event.setOnGround(this.mc.thePlayer.ticksExisted % 2 == 0);
                        if (!this.mc.thePlayer.onGround) {
                            this.mc.thePlayer.onGround = true;
                            this.mc.thePlayer.motionY = 0.0;
                            x = this.mc.thePlayer.posX;
                            y = this.mc.thePlayer.posY;
                            double z = this.mc.thePlayer.posZ;
                            if (this.mc.thePlayer.ticksExisted % 2 == 0) {
                                y -= 0.005;
                                y -= y % 0.015625;
                            } else {
                                y += 0.02;
                            }
                            this.mc.thePlayer.setPosition(x, y, z);
                            break;
                        }
                        this.mc.thePlayer.motionY = 0.42f;
                        break;
                    }
                    case FLOAT: {
                        if (!event.isPre()) break;
                        if (this.mc.thePlayer.isMoving() && !this.mc.gameSettings.keyBindJump.isKeyDown()) {
                            ++this.counter;
                            if (this.mc.thePlayer.onGround) {
                                this.mc.thePlayer.motionY = 0.42f;
                                this.level = 1;
                                break;
                            }
                            if (++this.level > 10) break;
                            this.mc.thePlayer.motionY = 0.0;
                            event.setOnGround(true);
                            break;
                        }
                        this.counter = 0;
                        break;
                    }
                    case BASIC: {
                        if (!this.mc.thePlayer.isMoving()) {
                            MoveUtil.setSpeed(0.03);
                        }
                        if (this.mc.thePlayer.onGround) {
                            this.mc.thePlayer.motionY = 0.42f;
                            break;
                        }
                        this.mc.thePlayer.motionY = 0.02 + MathUtils.getRandomInRange(1.0E-11, 1.0E-10);
                        break;
                    }
                    case DEV: {
                        if (!this.mc.gameSettings.keyBindJump.isKeyDown() || this.mc.thePlayer.onGround || !(this.mc.thePlayer.motionY < 0.3) || this.mc.thePlayer.ticksExisted % 5 != 0) break;
                        this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0, this.mc.thePlayer.posZ);
                    }
                }
                break;
            }
            case SPARTAN: {
                this.mc.timer.timerSpeed = 0.95f;
                double x = this.mc.thePlayer.posX;
                double y = this.mc.thePlayer.posY;
                double z = this.mc.thePlayer.posZ;
                this.mc.thePlayer.motionY = 0.0;
                double speed = 0.311f;
                float f = this.mc.thePlayer.rotationYaw * ((float)Math.PI / 180);
                double xAddition = (double)MathHelper.sin(f) * speed;
                double zAddition = (double)MathHelper.cos(f) * speed;
                event.setCancelled(true);
                if (++this.counter < 21) break;
                this.counter = 0;
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(x -= xAddition, y -= 0.5, z += zAddition, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, false));
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y - 11111.0, z, false));
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 11111.0, z, false));
                for (int i = 0; i < 20; ++i) {
                }
                break;
            }
        }
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        S27PacketExplosion packet;
        if (!event.isSending() && event.getPacket() instanceof S27PacketExplosion && (Math.abs((packet = (S27PacketExplosion)event.getPacket()).func_149149_c()) > 0.0f || packet.func_149144_d() > 0.0f || Math.abs(packet.func_149147_e()) > 0.0f)) {
            this.velocity = packet;
        }
        if (this.mode.getValue() == Modes.WATCHDOG && this.blink.isEnabled()) {
            if (this.mc.thePlayer == null || !event.isSending()) {
                return;
            }
            if (this.mc.thePlayer.ticksExisted % (Integer)this.blinkLength.getValue() != 0) {
                if (!this.isEnabled) {
                    this.isEnabled = true;
                    if (this.mc.theWorld == null) {
                        return;
                    }
                    this.packets.clear();
                }
            } else {
                this.elapsed = 0;
                this.isEnabled = false;
                if (this.mc.theWorld == null) {
                    return;
                }
                this.packets.forEach(this.mc.thePlayer.sendQueue.getNetworkManager()::sendPacket);
                this.packets.clear();
            }
            if (this.isEnabled) {
                ++this.elapsed;
                this.packets.add(event.getPacket());
                event.setCancelled(true);
            }
        }
    }

    @Handler(value=Render2DEvent.class)
    public void onRender(Render2DEvent event) {
        if (this.blink.isEnabled() && this.mode.getValue() == Modes.WATCHDOG && this.blinkIndicator.isEnabled()) {
            ScaledResolution sr = event.getScaledResolution();
            RenderUtil.drawRect((float)sr.getScaledWidth() / 2.0f - 49.5f, (float)sr.getScaledHeight() / 2.0f + 40.5f, 99.0 / ((double)((Integer)this.blinkLength.getValue()).intValue() / Math.max((double)(this.mc.thePlayer.ticksExisted % (Integer)this.blinkLength.getValue()), 1.0E-14)), 4.0, HUD.getColorHUD());
            RenderUtil.drawBorderedRect((float)sr.getScaledWidth() / 2.0f - 50.0f, (float)sr.getScaledHeight() / 2.0f + 40.0f, 100.0, 5.0, 0.5, -14606047, -2011094751);
        }
    }

    @Handler(value=MotionEvent.class)
    public void onMotion(MotionEvent event) {
        switch ((Modes)((Object)this.mode.getValue())) {
            case SPARTAN: {
                MoveUtil.setMoveSpeed(event, 0.0);
                break;
            }
            case VERUS: {
                switch ((verusModes)((Object)this.verusMode.getValue())) {
                    case DEV: {
                        break;
                    }
                    case BASIC: {
                        if (!this.mc.thePlayer.isMoving()) break;
                        MoveUtil.setMoveSpeed(event, 0.359);
                        break;
                    }
                    case NORMAL: {
                        double speed = this.mc.thePlayer.ticksExisted % 2 != 0 ? 0.612 : 0.34;
                        MoveUtil.setMoveSpeed(event, speed);
                        break;
                    }
                    case FLOAT: {
                        if (this.counter <= 10) break;
                        double speed = this.level <= 8 && this.level > 0 ? 0.28 + (double)this.level * 0.124 : (!this.mc.thePlayer.onGround && this.level <= 9 ? 0.135 : (this.level == 0 ? 0.612 : 0.28));
                        MoveUtil.setMoveSpeed(event, speed);
                    }
                }
                break;
            }
            case EXPERIMENTAL: {
                break;
            }
            case REDESKY: {
                MoveUtil.setMoveSpeed(event, 0.0);
                break;
            }
            case VANILLA: {
                MoveUtil.setMoveSpeed(event, ((Float)this.vanSpeed.getValue()).floatValue());
                break;
            }
            case WATCHDOG: {
                if (this.vanillaFly) {
                    MoveUtil.setMoveSpeed(event, (double)((Float)this.flySpeed.getValue()).floatValue() * 1.5);
                    return;
                }
                if (this.boost.isEnabled()) {
                    double motionY = this.mc.thePlayer.isPotionActive(Potion.jump) ? 0.8125 : (double)0.42f;
                    switch ((BoostModes)((Object)this.boostModes.getValue())) {
                        case NORMAL: 
                        case DAMAGE: {
                            if (this.mc.thePlayer.isMoving()) {
                                if (this.level != 1) {
                                    if (this.level == 2) {
                                        ++this.level;
                                        this.moveSpeed *= (double)((Float)this.flySpeed.getValue()).floatValue() - (this.mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.3 : 0.0);
                                    } else if (this.level == 3) {
                                        ++this.level;
                                        this.moveSpeed = this.lastDist - 0.05;
                                    } else {
                                        ++this.level;
                                        this.moveSpeed = this.lastDist - this.lastDist / 159.9;
                                        if (this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically) {
                                            this.level = 1;
                                        }
                                    }
                                } else if (!this.goodToGo && (this.mc.thePlayer.hurtResistantTime == 19 && this.safe.isEnabled() || !this.safe.isEnabled())) {
                                    this.goodToGo = true;
                                    this.mc.thePlayer.motionY = motionY;
                                    event.setY(this.mc.thePlayer.motionY);
                                    this.level = 2;
                                    double boost = 1.6 - (this.mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.11 : 0.0);
                                    this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
                                    this.moveSpeed = boost * this.getBaseMoveSpeed();
                                }
                            } else {
                                this.moveSpeed = 0.0;
                            }
                            this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
                            if (!this.goodToGo && this.safe.isEnabled()) {
                                this.moveSpeed = 0.0;
                            }
                            MoveUtil.setMoveSpeed(event, (float)this.moveSpeed);
                        }
                    }
                    break;
                }
                MoveUtil.setMoveSpeed(event, this.getBaseMoveSpeed());
                break;
            }
            case MINEPLEX: {
                Vec3f hitVec = new Vec3f(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ)).add(0.5f, 0.5f, 0.5f).add(new Vec3f(EnumFacing.DOWN.getDirectionVec()).scale(0.5f));
                this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ), EnumFacing.DOWN.getIndex(), null, hitVec.x, hitVec.y, hitVec.z));
                double acceleration = (Double)this.mineplexAcceleration.getValue();
                double daCoolSpeed = 0.5 + this.preStage * acceleration;
                double maxSpeed = ((Float)this.mineSpeed.getValue()).floatValue();
                if (daCoolSpeed >= maxSpeed) {
                    this.postStage += 1.0;
                    double slowdown = 0.0355;
                    daCoolSpeed = (maxSpeed - 0.01) * Math.pow(0.98f, this.postStage);
                    if (this.mc.thePlayer.onGround) {
                        if (this.postStage == 1.0) {
                            this.mc.thePlayer.motionY = 0.42f;
                            event.setY(0.42f);
                            this.mc.thePlayer.motionY += 0.098;
                            daCoolSpeed = 0.0;
                            this.mc.timer.timerSpeed = ((Float)this.longJumpTimer.getValue()).floatValue();
                        } else {
                            this.toggle();
                        }
                    }
                } else {
                    this.preStage += 1.0;
                }
                if (!this.mc.thePlayer.onGround) {
                    if (this.mc.thePlayer.motionY >= 0.0) {
                        this.mc.thePlayer.motionY += 0.04;
                    }
                    if (this.mc.thePlayer.motionY < 0.0) {
                        this.mc.thePlayer.motionY = this.mc.thePlayer.motionY > -0.3 ? (this.mc.thePlayer.motionY += 0.0331) : (this.mc.thePlayer.motionY > -0.38 ? (this.mc.thePlayer.motionY += 0.031) : (this.mc.thePlayer.motionY += 0.01));
                    }
                }
                if (this.mc.thePlayer.isCollidedHorizontally) {
                    this.toggle();
                }
                daCoolSpeed = this.mc.thePlayer.ticksExisted % 2 == 0 && this.postStage == 0.0 && daCoolSpeed <= maxSpeed ? -daCoolSpeed : daCoolSpeed;
                MoveUtil.setMoveSpeed(event, daCoolSpeed);
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
    public void onEnable() {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        this.velocity = null;
        this.posY = this.mc.thePlayer.posY;
        this.level = 0;
        this.lastDist = 0.0;
        this.packets.clear();
        this.timer.reset();
        if (this.mode.getValue() == Modes.VERUS) {
            boolean isDisabler;
            boolean bl = isDisabler = Moon.INSTANCE.getModuleManager().getModule("Disabler").isEnabled() && Disabler.mode.getValue() == Disabler.Mode.VERUS_SEMI || Moon.INSTANCE.getModuleManager().getModule("Disabler").isEnabled() && Disabler.mode.getValue() == Disabler.Mode.FAITHFUL3;
            if (!isDisabler) {
                Moon.INSTANCE.getNotificationManager().addNotification("Enable Verus disabler to make the fly faster.", 4000L);
            }
        }
        if (this.mode.getValue() == Modes.REDESKY) {
            this.rotYaw = this.mc.thePlayer.rotationYaw;
            this.rotPitch = this.mc.thePlayer.rotationPitch;
        }
        if (this.mode.getValue() == Modes.WATCHDOG && this.mc.thePlayer.fallDistance >= 3.0f && this.dropDown.getValue().booleanValue()) {
            Moon.INSTANCE.getNotificationManager().addNotification("Activated Exploit Fly!", 2000L);
        }
        super.onEnable();
    }

    public static enum BoostModes {
        NORMAL("Normal"),
        DAMAGE("Damage");

        private final String name;

        private BoostModes(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    private static enum verusModes {
        NORMAL("Ground"),
        FLOAT("Hop"),
        BASIC("FastGround"),
        DEV("Dev");

        private final String name;

        private verusModes(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum Modes {
        WATCHDOG("Watchdog"),
        MINEPLEX("Mineplex"),
        VANILLA("Vanilla"),
        CHUNKLOAD("ChunkLoad"),
        STATICGRAVITY("Static Gravity"),
        EXPERIMENTAL("Experimental"),
        COLLISION("Collision"),
        VERUS("Verus"),
        SPARTAN("Spartan"),
        REDESKY("Redesky");

        private final String name;

        private Modes(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

