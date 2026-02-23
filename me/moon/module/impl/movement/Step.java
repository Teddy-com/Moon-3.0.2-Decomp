/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement;

import java.awt.Color;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.player.StepEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.MathUtils;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Step
extends Module {
    public EnumValue<modes> mode = new EnumValue<modes>("Mode", modes.VANILLA);
    private final BooleanValue smooth = new BooleanValue("Smooth", true, (Value)this.mode, "NCP");
    private final NumberValue<Float> height = new NumberValue<Float>("Height", Float.valueOf(1.0f), Float.valueOf(1.0f), Float.valueOf(9.0f), Float.valueOf(0.5f));
    private final NumberValue<Integer> delay = new NumberValue<Integer>("Delay", 300, 0, 2000, 1);
    private final TimerUtil time = new TimerUtil();
    private boolean hasStep;
    public static int stepTicks;
    private double stage;
    private double level;

    public Step() {
        super("Step", Module.Category.MOVEMENT, new Color(102, 255, 51, 255).getRGB());
        this.setDescription("Automatically step up blocks");
    }

    @Override
    public void onDisable() {
        if (this.mc.thePlayer != null) {
            this.mc.thePlayer.stepHeight = 0.6f;
        }
        this.mc.timer.timerSpeed = 1.0f;
    }

    @Handler(value=StepEvent.class)
    public void onStep(StepEvent event) {
        double rHeight;
        boolean canStep;
        if (this.mc.theWorld == null || this.mc.thePlayer == null) {
            return;
        }
        if (!this.mc.thePlayer.isInLiquid() && event.isPre()) {
            if (this.mode.getValue() != modes.GROUND && this.mode.getValue() != modes.DEV && this.mc.thePlayer.isCollidedVertically && !this.mc.gameSettings.keyBindJump.isKeyDown() && !this.mc.thePlayer.isOnLadder() && this.time.hasReached(((Integer)this.delay.getValue()).intValue()) && this.mc.thePlayer.onGround && !Moon.INSTANCE.getModuleManager().getModule("speed").isEnabled() && !Moon.INSTANCE.getModuleManager().getModule("flight").isEnabled() && !Moon.INSTANCE.getModuleManager().getModule("longjump").isEnabled()) {
                event.setHeight(((Float)this.height.getValue()).floatValue());
            } else {
                event.setHeight(0.6f);
            }
        }
        boolean bl = canStep = (rHeight = this.mc.thePlayer.getEntityBoundingBox().minY - this.mc.thePlayer.posY) > 0.6;
        if (canStep) {
            stepTicks = 0;
            this.hasStep = true;
            switch ((modes)((Object)this.mode.getValue())) {
                case MINEPLEX: {
                    this.mineplexStep(rHeight);
                    break;
                }
                case SPARTAN: {
                    this.spartanStep(rHeight);
                    break;
                }
                case NCP: {
                    this.ncpStep(rHeight);
                    break;
                }
            }
            this.time.reset();
        }
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            ++stepTicks;
        }
        this.setSuffix(((modes)((Object)this.mode.getValue())).getName());
        if (this.mode.getValue() == modes.VANILLA && event.isPre()) {
            this.mc.thePlayer.stepHeight = ((Float)this.height.getValue()).floatValue();
        } else if (this.time.hasReached(65L) && this.hasStep && event.isPre() && this.mode.getValue() != modes.VANILLA && this.mode.getValue() != modes.DEV) {
            this.mc.timer.timerSpeed = 1.0f;
            this.hasStep = false;
        }
        if (this.mode.getValue() == modes.GROUND && event.isPre()) {
            if (this.mc.thePlayer.isCollidedHorizontally && !this.mc.gameSettings.keyBindJump.isKeyDown() && !this.mc.thePlayer.isOnLadder()) {
                if (this.mc.thePlayer.isCollidedVertically && this.mc.thePlayer.onGround && this.time.hasReached(((Integer)this.delay.getValue()).intValue())) {
                    this.mc.thePlayer.jump();
                } else if (this.mc.thePlayer.motionY < -0.18) {
                    this.mc.thePlayer.onGround = true;
                }
            } else {
                this.time.reset();
            }
        }
        if (event.isPre() && this.mode.getValue() == modes.DEV) {
            if (this.level >= 1.0 && !this.mc.gameSettings.keyBindJump.isKeyDown() && !this.mc.thePlayer.isOnLadder()) {
                this.stage += 1.0;
                switch ((int)this.stage) {
                    case 1: {
                        if (!this.mc.thePlayer.isCollidedVertically) {
                            this.stage = 0.0;
                            this.level = 0.0;
                            break;
                        }
                    }
                    case 2: {
                        this.mc.thePlayer.motionY = 0.6;
                        event.setOnGround(true);
                        break;
                    }
                    case 4: {
                        this.mc.thePlayer.motionY = 0.42f;
                        event.setOnGround(true);
                        break;
                    }
                    default: {
                        if (this.mc.thePlayer.posY % 0.015625 == 0.0) {
                            event.setOnGround(true);
                            this.stage = 0.0;
                        }
                        this.mc.thePlayer.onGround = true;
                        break;
                    }
                }
            } else {
                this.stage = 0.0;
            }
            this.level = this.mc.thePlayer.isCollidedHorizontally ? 1.0 : 0.0;
        }
    }

    private void mineplexStep(double height) {
        if (height <= 15.0) {
            double lastPosY = 0.0;
            float packetsAmount = 0.0f;
            for (int i = 0; i < 50; ++i) {
                double value = (double)0.42f - (MathUtils.getGravity() - 0.038) * (double)i;
                if (lastPosY + 0.175 > lastPosY + value) {
                    i = -1;
                }
                if (!((lastPosY += (value = (double)0.42f - (MathUtils.getGravity() - 0.038) * (double)i)) <= height + 0.01)) break;
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + lastPosY, this.mc.thePlayer.posZ, true));
                packetsAmount += 1.0f;
                if (lastPosY > height) break;
            }
            this.mc.timer.timerSpeed = Math.max(0.05f, Math.min(1.0f, 2.0f / packetsAmount));
        }
    }

    private void devStep(double height) {
        double value = 0.5;
        int i = 0;
        while ((double)i < height * 2.0) {
            if (value * (double)i <= height + 0.001) {
                // empty if block
            }
            ++i;
        }
    }

    private void spartanStep(double height) {
        double value = 0.5;
        double packetsAmount = 0.0;
        int i = 1;
        while ((double)i < height * 2.0) {
            double packetsHeight = value * (double)i;
            if (packetsHeight <= height + 0.001) {
                packetsAmount += 1.0;
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + packetsHeight, this.mc.thePlayer.posZ, true));
            }
            ++i;
        }
    }

    private void ncpStep(double height) {
        double posX = this.mc.thePlayer.posX;
        double posZ = this.mc.thePlayer.posZ;
        double y = this.mc.thePlayer.posY;
        if (height < 1.0) {
            double[] heights;
            if (this.smooth.getValue().booleanValue()) {
                this.mc.timer.timerSpeed = 0.31f;
            }
            for (double off : heights = new double[]{0.425, 0.821, 0.699, 0.599, 1.022}) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, true));
            }
        } else if (height < 1.1) {
            double[] motions;
            if (this.smooth.getValue().booleanValue()) {
                this.mc.timer.timerSpeed = 0.4f;
            }
            for (double off : motions = new double[]{0.42f, 0.75}) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, true));
            }
        } else if (height < 1.6) {
            double[] heights;
            if (this.smooth.getValue().booleanValue()) {
                this.mc.timer.timerSpeed = 0.35f;
            }
            for (double off : heights = new double[]{0.425, 0.75, 1.0, 1.16, 1.23, 1.2}) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, true));
            }
        } else if (height < 2.1) {
            double[] heights;
            if (this.smooth.getValue().booleanValue()) {
                this.mc.timer.timerSpeed = 0.25f;
            }
            for (double off : heights = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869}) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, true));
            }
        } else {
            double[] heights;
            if (this.smooth.getValue().booleanValue()) {
                this.mc.timer.timerSpeed = 0.21f;
            }
            for (double off : heights = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907}) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, true));
            }
        }
    }

    public static enum modes {
        VANILLA("Vanilla"),
        NCP("NCP"),
        SPARTAN("Spartan"),
        VERUS("Verus"),
        MINEPLEX("Mineplex"),
        GROUND("Ground"),
        DEV("Dev");

        private final String name;

        private modes(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

