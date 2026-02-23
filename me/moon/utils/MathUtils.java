/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class MathUtils {
    private static Random rng = new Random();

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double getGravity() {
        return 0.0784000015258789;
    }

    public static Block getBlockUnderPlayer(EntityPlayer inPlayer, double height) {
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ)).getBlock();
    }

    public static double roundDouble(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static float roundFloat(float value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    public static double distance2D(double startX, double startZ, double endX, double endZ) {
        return Math.hypot(startX - endX, startZ - endZ);
    }

    public static double distance3D(Entity Player, Entity target) {
        float f = (float)(Player.posX - target.posX);
        float f1 = (float)(Player.posY - target.posY);
        float f2 = (float)(Player.posZ - target.posZ);
        return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
    }

    public static double distance3D(double startX, double startY, double startZ, double endX, double endY, double endZ) {
        float f = (float)(startX - endX);
        float f1 = (float)(startY - endY);
        float f2 = (float)(startZ - endZ);
        return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
    }

    public static double getRandomInRange(double min, double max) {
        double shifted;
        Random random = new Random();
        double range = max - min;
        double scaled = random.nextDouble() * range;
        if (scaled > max) {
            scaled = max;
        }
        if ((shifted = scaled + min) > max) {
            shifted = max;
        }
        return shifted;
    }

    public static double getHighestOffset(double max) {
        for (double i = 0.0; i < max; i += 0.01) {
            for (int offset : new int[]{-2, -1, 0, 1, 2}) {
                if (Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(Minecraft.getMinecraft().thePlayer.motionX * (double)offset, i, Minecraft.getMinecraft().thePlayer.motionZ * (double)offset)).size() <= 0) continue;
                return i - 0.01;
            }
        }
        return max;
    }

    public static float[] constrainAngle(float[] vector) {
        vector[0] = vector[0] % 360.0f;
        vector[1] = vector[1] % 360.0f;
        while (vector[0] <= -180.0f) {
            vector[0] = vector[0] + 360.0f;
        }
        while (vector[1] <= -180.0f) {
            vector[1] = vector[1] + 360.0f;
        }
        while (vector[0] > 180.0f) {
            vector[0] = vector[0] - 360.0f;
        }
        while (vector[1] > 180.0f) {
            vector[1] = vector[1] - 360.0f;
        }
        return vector;
    }

    public static float getRandomInRange(float min, float max) {
        Random random = new Random();
        float range = max - min;
        float scaled = random.nextFloat() * range;
        float shifted = scaled + min;
        return shifted;
    }

    public static int getRandomInRange(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt(max - min + 1) + min;
        return randomNum;
    }

    public double doRound(double d, int r) {
        String round = "#";
        for (int i = 0; i < r; ++i) {
            round = String.valueOf(round) + ".#";
        }
        DecimalFormat twoDForm = new DecimalFormat(round);
        return Double.valueOf(twoDForm.format(d));
    }

    public static int getMiddle(int i, int i2) {
        return (i + i2) / 2;
    }

    public static Random getRng() {
        return rng;
    }

    public static int getNumberFor(int start, int end) {
        if (end >= start) {
            return 0;
        }
        if (end - start < 0) {
            return 0;
        }
        return end - start;
    }

    public static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static float getRandom() {
        return rng.nextFloat();
    }

    public static int getRandom(int cap) {
        return rng.nextInt(cap);
    }

    public static int getRandom(int floor, int cap) {
        return floor + rng.nextInt(cap - floor + 1);
    }

    public static double getRandomf(double min, double max) {
        return min + rng.nextDouble() * (max - min + 1.0);
    }

    public static double getMiddleDouble(int i, int i2) {
        return (double)(i + i2) / 2.0;
    }

    public static double clamp(double value, double minimum, double maximum) {
        return value > maximum ? maximum : (value < minimum ? minimum : value);
    }

    public static double normalizeAngle(double angle) {
        return (angle + 360.0) % 360.0;
    }

    public static float normalizeAngle(float angle) {
        return (angle + 360.0f) % 360.0f;
    }

    public static float clamp(float input, float max) {
        return MathUtils.clamp(input, max, -max);
    }

    public static float clamp(float input, float max, float min) {
        if (input > max) {
            input = max;
        }
        if (input < min) {
            input = min;
        }
        return input;
    }
}

