/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.game;

import me.moon.Moon;
import me.moon.event.impl.player.MotionEvent;
import me.moon.module.impl.combat.KillAura;
import me.moon.module.impl.movement.Speed;
import me.moon.module.impl.movement.TargetStrafe;
import me.moon.utils.MathUtils;
import me.moon.utils.game.ServerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class MoveUtil {
    private static Minecraft mc = Minecraft.getMinecraft();
    private static int voidTicks;

    public static void setJumpSpeed(float multiplier) {
        if (MoveUtil.mc.thePlayer.isSprinting()) {
            float f = MoveUtil.mc.thePlayer.rotationYaw * ((float)Math.PI / 180);
            float speed = 0.2f * multiplier;
            MoveUtil.mc.thePlayer.motionX -= (double)(MathHelper.sin(f) * speed);
            MoveUtil.mc.thePlayer.motionZ += (double)(MathHelper.cos(f) * speed);
        }
        MoveUtil.mc.thePlayer.isAirBorne = true;
    }

    public static void setSpeed2(double speed) {
        Minecraft mc = Minecraft.getMinecraft();
        MovementInput movementInput = mc.thePlayer.movementInput;
        float forward = MovementInput.moveForward;
        float strafe = MovementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward == 0.0f && strafe == 0.0f) {
            mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionZ = 0.0;
        } else if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
                strafe = 0.0f;
            } else if (strafe <= -1.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
                strafe = 0.0f;
            }
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        mc.thePlayer.motionX = (double)forward * speed * mx + (double)strafe * speed * mz;
        mc.thePlayer.motionZ = (double)forward * speed * mz - (double)strafe * speed * mz;
    }

    protected static boolean inVoid() {
        for (int i = (int)Math.ceil(MoveUtil.mc.thePlayer.posY); i >= 0; --i) {
            if (MoveUtil.mc.theWorld.getBlockState(new BlockPos(MoveUtil.mc.thePlayer.posX, (double)i, MoveUtil.mc.thePlayer.posZ)).getBlock() == Blocks.air) continue;
            return false;
        }
        return true;
    }

    protected static float findYaw(EntityLivingBase target, EntityPlayerSP player) {
        float yaw = MoveUtil.getRotationsToEnt(player, target);
        double direction = Math.toRadians(yaw += Speed.strafeDirection ? ((Float)TargetStrafe.angle.getValue()).floatValue() : -((Float)TargetStrafe.angle.getValue()).floatValue());
        double x = target.posX;
        double z = target.posZ;
        return MoveUtil.getRotationFromPosition(x += (double)(-((Float)TargetStrafe.distance.getValue()).floatValue() * MathHelper.sin((float)direction)), z += (double)(((Float)TargetStrafe.distance.getValue()).floatValue() * MathHelper.cos((float)direction)));
    }

    protected static float getRotationFromPosition(double x, double z) {
        double xDiff = x - MoveUtil.mc.thePlayer.posX;
        double zDiff = z - MoveUtil.mc.thePlayer.posZ;
        return (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
    }

    protected static float getRotationsToEnt(EntityLivingBase target, EntityLivingBase attacker) {
        double differenceX = target.posX - attacker.posX;
        double differenceZ = target.posZ - attacker.posZ;
        float rotationYaw = (float)(Math.atan2(differenceZ, differenceX) * 180.0 / Math.PI) - 90.0f;
        float finishedYaw = attacker.rotationYaw + MathHelper.wrapAngleTo180_float(rotationYaw - attacker.rotationYaw);
        return finishedYaw;
    }

    public static void setMoveSpeed(MotionEvent event, double speed) {
        float yaw;
        boolean shouldStrafe;
        ++voidTicks;
        if (KillAura.target != null) {
            if (MoveUtil.mc.thePlayer.isCollidedHorizontally) {
                boolean bl = Speed.strafeDirection = !Speed.strafeDirection;
            }
            if (MoveUtil.inVoid() && voidTicks > 4 && !Moon.INSTANCE.getModuleManager().getModule("Flight").isEnabled()) {
                voidTicks = 0;
                Speed.strafeDirection = !Speed.strafeDirection;
            }
        }
        boolean bl = shouldStrafe = Moon.INSTANCE.getModuleManager().getModule("targetstrafe").isEnabled() && KillAura.target != null && (MoveUtil.mc.gameSettings.keyBindJump.isKeyDown() || !TargetStrafe.spaceOnly.isEnabled());
        double forward = shouldStrafe ? (double)(Math.abs(MovementInput.moveForward) > 0.0f || Math.abs(MovementInput.moveStrafe) > 0.0f ? 1 : 0) : (double)MovementInput.moveForward;
        double strafe = shouldStrafe ? 0.0 : (double)MovementInput.moveStrafe;
        float f = yaw = shouldStrafe ? MoveUtil.findYaw(KillAura.target, MoveUtil.mc.thePlayer) : MoveUtil.mc.thePlayer.rotationYaw;
        if (ServerUtils.isOnHypixel()) {
            yaw = (float)((double)yaw + MathUtils.getRandomInRange(-2.5, 2.5));
        }
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw)));
            event.setZ(forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw)));
        }
    }

    public static void setSpeedMove(MotionEvent event, float speed) {
        float direction = MoveUtil.getDirection(MoveUtil.mc.thePlayer.rotationYaw);
        event.setX(-Math.sin(direction) * (double)speed);
        event.setZ(Math.cos(direction) * (double)speed);
    }

    public static float getDirection(float yaw) {
        double forward;
        double moveForward = MoveUtil.mc.thePlayer.moveForward;
        double moveStrafing = MoveUtil.mc.thePlayer.moveStrafing;
        yaw += MathUtils.getRandomInRange(-2.5f, 2.5f);
        if (moveForward < 0.0) {
            yaw += 180.0f;
            forward = -0.5;
        } else {
            forward = moveForward > 0.0 ? 0.5 : 1.0;
        }
        if (moveStrafing < 0.0) {
            yaw = (float)((double)yaw + 90.0 * forward);
        } else if (moveStrafing > 0.0) {
            yaw = (float)((double)yaw - 90.0 * forward);
        }
        return (float)((double)yaw * (Math.PI / 180));
    }

    public static float getDirection() {
        float yaw = MoveUtil.mc.thePlayer.rotationYaw;
        float forward = MoveUtil.mc.thePlayer.moveForward;
        float strafe = MoveUtil.mc.thePlayer.moveStrafing;
        yaw += (float)(forward < 0.0f ? 180 : 0);
        if (strafe < 0.0f) {
            yaw += (float)(forward < 0.0f ? -45 : (forward == 0.0f ? 90 : 45));
        }
        if (strafe > 0.0f) {
            yaw -= (float)(forward < 0.0f ? -45 : (forward == 0.0f ? 90 : 45));
        }
        return (float)((double)yaw * (Math.PI / 180));
    }

    public static double square(double in) {
        return in * in;
    }

    public static double getSpeed() {
        return Math.hypot(MoveUtil.mc.thePlayer.motionX, MoveUtil.mc.thePlayer.motionZ);
    }

    public static void setSpeed(double speed) {
        MoveUtil.mc.thePlayer.motionX = (double)(-MathHelper.sin(MoveUtil.getDirection())) * speed;
        MoveUtil.mc.thePlayer.motionZ = (double)MathHelper.cos(MoveUtil.getDirection()) * speed;
    }
}

