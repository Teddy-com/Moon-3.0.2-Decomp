/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package me.moon.module.impl.ghost;

import java.awt.Color;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.input.ClickMouseEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.MathUtils;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.value.impl.RangedValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Mouse;

public class Autoclicker
extends Module {
    private final RangedValue<Integer> cps = new RangedValue<Integer>("CPS", 1, 20, 1, 8, 12);
    private int nextCPS;
    private int num1Prob = 50;
    private int num2Prob = 89;
    private int num3Prob = 98;
    private final int attackKey;
    private final int blockKey;
    private double counter;
    private long rdmDelay;
    private final TimerUtil timer;

    public Autoclicker() {
        super("AutoClicker", Module.Category.GHOST, new Color(10789534).getRGB());
        this.attackKey = this.mc.gameSettings.keyBindAttack.getKeyCode();
        this.blockKey = this.mc.gameSettings.keyBindUseItem.getKeyCode();
        this.rdmDelay = 10000L;
        this.timer = new TimerUtil();
        this.setDescription("Clicks for you");
    }

    private int getCPS() {
        int chance = MathUtils.getRandomInRange(1, 100);
        return chance <= this.num1Prob ? 1 : (chance <= this.num2Prob ? 2 : (chance <= this.num3Prob ? 3 : (chance <= 99 ? MathUtils.getRandomInRange(4, 6) : MathUtils.getRandomInRange(5, 8))));
    }

    private void Click() {
        if (Mouse.isButtonDown((int)1)) {
            // empty if block
        }
        KeyBinding.setKeyBindState(this.attackKey, true);
        KeyBinding.setKeyBindState(this.attackKey, false);
        KeyBinding.onTick(this.attackKey);
    }

    private void clickMouse() {
        ClickMouseEvent event = new ClickMouseEvent();
        Moon.INSTANCE.getEventBus().fireEvent(event);
        if (this.mc.objectMouseOver == null) {
            System.out.print("Null returned as 'hitResult', this shouldn't happen!");
        } else {
            switch (this.mc.objectMouseOver.typeOfHit) {
                case ENTITY: 
                case MISS: {
                    Minecraft.leftClickCounter = 0;
                    this.Click();
                    break;
                }
            }
        }
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            if (this.timer.sleep(this.rdmDelay)) {
                int nextNum1Prob = MathUtils.getRandomInRange(28, 55);
                this.num1Prob = Math.abs(this.num1Prob - nextNum1Prob) > 5 ? nextNum1Prob : MathUtils.getRandomInRange(28, 55);
                int nextNum2Prob = MathUtils.getRandomInRange(82, 94);
                this.num2Prob = Math.abs(this.num2Prob - nextNum2Prob) > 4 ? nextNum2Prob : MathUtils.getRandomInRange(82, 94);
                this.num3Prob = MathUtils.getRandomInRange(97, 99);
                this.rdmDelay = MathUtils.getRandomInRange(6000, 23000);
            }
            if (this.mc.currentScreen instanceof GuiInventory || this.mc.thePlayer.capabilities.isCreativeMode || this.mc.thePlayer.openContainer != null && this.mc.thePlayer.openContainer.windowId != 0 || this.mc.thePlayer == null) {
                return;
            }
            if (Mouse.isButtonDown((int)0)) {
                this.counter += 1.0;
                if (this.counter >= (double)this.nextCPS) {
                    this.clickMouse();
                    this.nextCPS = this.getCPS();
                    this.counter = 0.0;
                } else if (Mouse.isButtonDown((int)1)) {
                    // empty if block
                }
            } else {
                this.counter = MathUtils.getRandomInRange(-2, 0);
            }
        }
    }
}

