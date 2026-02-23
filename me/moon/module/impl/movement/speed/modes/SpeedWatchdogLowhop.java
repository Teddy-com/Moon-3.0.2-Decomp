/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement.speed.modes;

import me.moon.Moon;
import me.moon.event.impl.player.MotionEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.movement.Speed;
import me.moon.module.impl.movement.speed.SpeedMode;
import me.moon.utils.MathUtils;
import me.moon.utils.game.MoveUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;

public class SpeedWatchdogLowhop
extends SpeedMode {
    public SpeedWatchdogLowhop() {
        super("Watchdog LowHop", Speed.Mode.WATCHDOGLOWHOP);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            if (event.getY() % 0.015625 == 0.0) {
                event.setY(event.getY() + 5.3424E-4);
                event.setOnGround(false);
            }
            if (this.mc.thePlayer.motionY > 0.3) {
                event.setOnGround(true);
            }
        }
        super.onUpdate(event);
    }

    @Override
    public void onMotion(MotionEvent event) {
        if (!this.mc.gameSettings.keyBindJump.isKeyDown()) {
            double[] offsets = this.getOffsets(2.6f);
            double[] offsets2 = this.getOffsets(1.5f);
            if (this.mc.thePlayer.isMoving()) {
                float motionY = 0.42f;
                ++this.stage;
                if (this.mc.thePlayer.onGround) {
                    if (MathUtils.getRandomInRange(0, 3) > 2) {
                        this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C03PacketPlayer(true));
                    }
                    this.stage = 0;
                }
                switch (this.stage) {
                    case 0: {
                        if (this.mc.thePlayer.isPotionActive(Potion.jump)) {
                            motionY += (float)(this.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.101f;
                        }
                        if (this.mc.thePlayer.isCollidedHorizontally || !this.mc.theWorld.isAirBlock(new BlockPos(this.mc.thePlayer.posX + offsets[0], this.mc.thePlayer.posY, this.mc.thePlayer.posZ + offsets[1])) && !Moon.INSTANCE.getModuleManager().getModule("Scaffold").isEnabled() || this.mc.thePlayer.onGround && this.mc.theWorld.isAirBlock(new BlockPos(this.mc.thePlayer.posX + offsets2[0], this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ + offsets2[1])) && !Moon.INSTANCE.getModuleManager().getModule("Scaffold").isEnabled()) {
                            this.stageOG = 1;
                            this.mc.thePlayer.motionY = motionY;
                            event.setY(this.mc.thePlayer.motionY);
                        } else {
                            this.stageOG = 0;
                            event.setY(motionY);
                        }
                        if (this.stageOG == 1) {
                            if (!Moon.INSTANCE.getModuleManager().getModule("scaffold").isEnabled()) {
                                this.moveSpeed *= this.mc.thePlayer.isPotionActive(Potion.moveSpeed) ? (double)(this.mc.thePlayer.isPotionActive(Potion.jump) ? 1.925f : 2.14f) : (double)1.8f;
                                break;
                            }
                            this.moveSpeed *= (double)1.4f;
                            break;
                        }
                        this.moveSpeed *= 2.0;
                        break;
                    }
                    case 1: {
                        if (this.stageOG == 1) {
                            double boost = Moon.INSTANCE.getModuleManager().getModule("scaffold").isEnabled() ? 0.725 : (double)(this.mc.thePlayer.isPotionActive(Potion.moveSpeed) ? (this.mc.thePlayer.isPotionActive(Potion.jump) ? 0.905f : 0.73f) : 0.69f);
                            this.moveSpeed = this.lastDist - boost * (this.lastDist - this.getBaseMoveSpeed());
                            break;
                        }
                        this.moveSpeed = this.lastDist - (double)0.82f * (this.lastDist - this.getBaseMoveSpeed());
                        break;
                    }
                    case 2: {
                        if (this.stageOG != 0) break;
                        this.moveSpeed = this.lastDist - this.lastDist / 90.0;
                        event.setY(-0.0784000015258789);
                        break;
                    }
                    case 3: {
                        if (this.stageOG != 0) break;
                        this.moveSpeed = this.lastDist - this.lastDist / 50.0;
                        break;
                    }
                    default: {
                        this.moveSpeed = this.stageOG == 1 ? this.lastDist - this.lastDist / 159.0 : this.lastDist - this.lastDist / 5.0;
                    }
                }
                MoveUtil.setMoveSpeed(event, this.moveSpeed);
            } else {
                MoveUtil.setMoveSpeed(event, 0.0);
            }
        } else {
            switch (this.stage) {
                case 0: {
                    ++this.stage;
                    this.lastDist = 0.0;
                    break;
                }
                case 2: {
                    this.lastDist = 0.0;
                    float motionY = 0.4001f;
                    if (this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f || !this.mc.thePlayer.onGround) break;
                    if (this.mc.thePlayer.isPotionActive(Potion.jump)) {
                        motionY += (float)(this.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.101f;
                    }
                    this.mc.thePlayer.motionY = motionY;
                    event.setY(this.mc.thePlayer.motionY);
                    if (!Moon.INSTANCE.getModuleManager().getModule("scaffold").isEnabled()) {
                        this.moveSpeed *= this.mc.thePlayer.isPotionActive(Potion.moveSpeed) ? (double)(this.mc.thePlayer.isPotionActive(Potion.jump) ? 1.925f : 2.14f) : (double)1.8f;
                        break;
                    }
                    this.moveSpeed *= (double)1.4f;
                    break;
                }
                case 3: {
                    double boost = Moon.INSTANCE.getModuleManager().getModule("scaffold").isEnabled() ? 0.725 : (double)(this.mc.thePlayer.isPotionActive(Potion.moveSpeed) ? (this.mc.thePlayer.isPotionActive(Potion.jump) ? 0.905f : 0.73f) : 0.69f);
                    this.moveSpeed = this.lastDist - boost * (this.lastDist - this.getBaseMoveSpeed());
                    break;
                }
                default: {
                    ++this.stage;
                    if ((this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically) && this.stage > 0) {
                        this.stage = this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f ? 0 : 1;
                    }
                    this.moveSpeed = this.lastDist - this.lastDist / 159.9;
                }
            }
            this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
            MoveUtil.setMoveSpeed(event, this.moveSpeed);
            ++this.stage;
        }
        super.onMotion(event);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}

