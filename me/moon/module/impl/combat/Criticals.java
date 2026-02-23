/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.lwjgl.input.Mouse
 */
package me.moon.module.impl.combat;

import java.awt.Color;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.MathUtils;
import me.moon.utils.value.impl.EnumValue;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Mouse;

public class Criticals
extends Module {
    public EnumValue<mode> criticalsMode = new EnumValue<mode>("Mode", mode.WATCHDOG);
    private float FallStack;

    public Criticals() {
        super("Criticals", Module.Category.COMBAT, new Color(10789534).getRGB());
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        this.setSuffix(StringUtils.capitalize((String)((mode)((Object)this.criticalsMode.getValue())).getName()));
        if (this.criticalsMode.getValue() == mode.CUBECRAFT && event.isPre() && !Mouse.isButtonDown((int)0) && this.mc.thePlayer.motionY < 0.0 && this.mc.thePlayer.onGround && !Moon.INSTANCE.getModuleManager().getModule("scaffold").isEnabled() && this.mc.thePlayer.onGround && !Moon.INSTANCE.getModuleManager().getModule("speed").isEnabled() && !Moon.INSTANCE.getModuleManager().getModule("flight").isEnabled() && !this.mc.gameSettings.keyBindJump.isKeyDown() && this.mc.thePlayer.fallDistance == 0.0f) {
            event.setOnGround(false);
            if (this.mc.thePlayer.ticksExisted % 3 == 0) {
                double value = 0.0624 + MathUtils.getRandomInRange(1.0E-8, 1.0E-7);
                event.setY(this.mc.thePlayer.posY + value);
            } else {
                event.setY(this.mc.thePlayer.posY + 1.0E-10);
            }
        }
        if (this.criticalsMode.getValue() == mode.NOGROUND && event.isPre()) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
            event.setCancelled(true);
        }
    }

    @Override
    public void onEnable() {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        this.FallStack = 0.0f;
    }

    private boolean isBlockUnder() {
        for (int i = (int)(this.mc.thePlayer.posY - 1.0); i > 0; --i) {
            BlockPos pos = new BlockPos(this.mc.thePlayer.posX, (double)i, this.mc.thePlayer.posZ);
            if (this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }

    public static enum mode {
        WATCHDOG("Watchdog"),
        NCP("NCP"),
        CUBECRAFT("Cubecraft"),
        AREA51("Area 51"),
        NOGROUND("No Ground");

        private String name;

        private mode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

