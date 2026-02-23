/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package me.moon.module.impl.combat;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.game.Printer;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import org.lwjgl.input.Mouse;

public class AutoApple
extends Module {
    private boolean eatingApple;
    private int switched = -1;
    public static boolean doingStuff = false;
    private final TimerUtil timer = new TimerUtil();
    private final BooleanValue eatHeads = new BooleanValue("Eat heads", "Heal width Heads", true);
    private final BooleanValue eatApples = new BooleanValue("Eat apples", "Heal width apples", true);
    private final NumberValue<Integer> health = new NumberValue<Integer>("Health", 10, 1, 20, 1);
    private final NumberValue<Integer> delay = new NumberValue<Integer>("Delay", 750, 100, 2000, 25);

    public AutoApple() {
        super("AutoApple", Module.Category.COMBAT, new Color(255, 255, 0, 255).getRGB());
        this.setDescription("Automatically eats golden apples / heads when at low health");
    }

    @Override
    public void onEnable() {
        doingStuff = false;
        this.eatingApple = false;
        this.switched = -1;
        this.timer.reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        doingStuff = false;
        if (this.eatingApple) {
            this.repairItemPress();
            this.repairItemSwitch();
        }
        super.onDisable();
    }

    private void repairItemPress() {
        KeyBinding keyBindUseItem;
        if (this.mc.gameSettings != null && (keyBindUseItem = this.mc.gameSettings.keyBindUseItem) != null) {
            keyBindUseItem.pressed = false;
        }
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (this.mc.thePlayer == null) {
            return;
        }
        InventoryPlayer inventory = this.mc.thePlayer.inventory;
        if (inventory == null) {
            return;
        }
        doingStuff = false;
        if (!Mouse.isButtonDown((int)0) && !Mouse.isButtonDown((int)1)) {
            KeyBinding useItem = this.mc.gameSettings.keyBindUseItem;
            if (!this.timer.hasReached(((Integer)this.delay.getValue()).intValue())) {
                this.eatingApple = false;
                this.repairItemPress();
                this.repairItemSwitch();
                return;
            }
            if (this.mc.thePlayer.capabilities.isCreativeMode || this.mc.thePlayer.getHealth() >= (float)((Integer)this.health.getValue()).intValue()) {
                this.timer.reset();
                if (this.eatingApple) {
                    this.eatingApple = false;
                    this.repairItemPress();
                    this.repairItemSwitch();
                }
                return;
            }
            for (int i = 0; i < 2; ++i) {
                int slot;
                boolean doEatHeads;
                boolean bl = doEatHeads = i != 0;
                if (doEatHeads) {
                    if (!this.eatHeads.isEnabled()) {
                        continue;
                    }
                } else if (!this.eatApples.isEnabled()) {
                    this.eatingApple = false;
                    this.repairItemPress();
                    this.repairItemSwitch();
                    continue;
                }
                if ((slot = doEatHeads ? this.getItemFromHotbar(397) : this.getItemFromHotbar(322)) == -1) continue;
                int tempSlot = inventory.currentItem;
                doingStuff = true;
                if (doEatHeads) {
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(inventory.getCurrentItem()));
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(tempSlot));
                    this.timer.reset();
                } else {
                    inventory.currentItem = slot;
                    useItem.pressed = true;
                    if (this.eatingApple) continue;
                    this.eatingApple = true;
                    this.switched = tempSlot;
                }
                Printer.print(String.format("Automatically ate a %s", doEatHeads ? "player head" : "golden apple"));
            }
        }
    }

    private void repairItemSwitch() {
        EntityPlayerSP p = this.mc.thePlayer;
        if (p == null) {
            return;
        }
        InventoryPlayer inventory = p.inventory;
        if (inventory == null) {
            return;
        }
        int switched = this.switched;
        if (switched == -1) {
            return;
        }
        inventory.currentItem = switched;
        this.switched = switched = -1;
    }

    private int getItemFromHotbar(int id) {
        for (int i = 0; i < 9; ++i) {
            ItemStack is;
            Item item;
            if (this.mc.thePlayer.inventory.mainInventory[i] == null || Item.getIdFromItem(item = (is = this.mc.thePlayer.inventory.mainInventory[i]).getItem()) != id) continue;
            if (id == 397 && !is.getDisplayName().contains("\u00a76")) {
                return -1;
            }
            return i;
        }
        return -1;
    }
}

