/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.combat;

import java.awt.Color;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor
extends Module {
    private final BooleanValue drop = new BooleanValue("Drop", "Drop Worse Armor", true);
    private final NumberValue<Integer> delay = new NumberValue<Integer>("Delay", 250, 1, 3000, 1);
    private final NumberValue<Integer> openingDelay = new NumberValue<Integer>("Opening Delay", 250, 0, 3000, 1);
    private final BooleanValue delayBetweenActions = new BooleanValue("Delay between actions", true);
    private final BooleanValue inventoryonly = new BooleanValue("Inventory Only", false);
    private final TimerUtil timer = new TimerUtil();
    private TimerUtil KEKWHD = new TimerUtil();
    private TimerUtil cringeTimer = new TimerUtil();
    public static boolean canAutoArmor = false;

    public AutoArmor() {
        super("AutoArmor", Module.Category.COMBAT, new Color(180, 180, 190, 255).getRGB());
        this.setDescription("Automatically equips armor");
    }

    @Override
    public void onDisable() {
        canAutoArmor = false;
        super.onDisable();
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        this.setSuffix(((Integer)this.delay.getValue()).toString());
        if (!Moon.INSTANCE.getModuleManager().getModule("InvCleaner").isEnabled()) {
            canAutoArmor = true;
        }
        if (!(this.mc.currentScreen instanceof GuiInventory) && this.inventoryonly.isEnabled()) {
            this.KEKWHD.reset();
            this.cringeTimer.reset();
            return;
        }
        if (!canAutoArmor) {
            this.timer.reset();
            this.KEKWHD.reset();
            this.cringeTimer.reset();
            return;
        }
        if (!this.KEKWHD.hasReached(((Integer)this.openingDelay.getValue()).intValue())) {
            return;
        }
        if (this.mc.thePlayer != null && (this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiInventory)) {
            int slotID = -1;
            double maxProt = -1.0;
            int switchArmor = -1;
            for (int i = 9; i < 45; ++i) {
                double d;
                ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack == null || !this.canEquip(stack) && (!this.betterCheck(stack) || this.canEquip(stack))) continue;
                if (this.betterCheck(stack) && switchArmor == -1) {
                    switchArmor = this.betterSwap(stack);
                }
                double protValue = this.getProtectionValue(stack);
                if (d < maxProt) continue;
                slotID = i;
                maxProt = protValue;
            }
            if (slotID != -1) {
                if (this.timer.sleep(((Integer)this.delay.getValue()).intValue())) {
                    if (switchArmor != -1) {
                        if (this.drop.isEnabled()) {
                            this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, 4 + switchArmor, 1, 4, this.mc.thePlayer);
                        } else {
                            this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, 4 + switchArmor, 1, 1, this.mc.thePlayer);
                        }
                    }
                    if (this.cringeTimer.hasReached(50L) || !this.delayBetweenActions.getValue().booleanValue()) {
                        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slotID, 1, 1, this.mc.thePlayer);
                        this.cringeTimer.reset();
                        this.timer.reset();
                    }
                }
            } else {
                this.timer.reset();
            }
        }
    }

    private boolean betterCheck(ItemStack stack) {
        if (this.mc.thePlayer.getEquipmentInSlot(4) != null && Item.getIdFromItem(this.mc.thePlayer.getEquipmentInSlot(4).getItem()) == 397) {
            return true;
        }
        if (stack.getItem() instanceof ItemArmor) {
            if (this.mc.thePlayer.getEquipmentInSlot(1) != null && stack.getUnlocalizedName().contains("boots") && this.getProtectionValue(stack) + (double)((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(this.mc.thePlayer.getEquipmentInSlot(1)) + (double)((ItemArmor)this.mc.thePlayer.getEquipmentInSlot((int)1).getItem()).damageReduceAmount) {
                return true;
            }
            if (this.mc.thePlayer.getEquipmentInSlot(2) != null && stack.getUnlocalizedName().contains("leggings") && this.getProtectionValue(stack) + (double)((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(this.mc.thePlayer.getEquipmentInSlot(2)) + (double)((ItemArmor)this.mc.thePlayer.getEquipmentInSlot((int)2).getItem()).damageReduceAmount) {
                return true;
            }
            if (this.mc.thePlayer.getEquipmentInSlot(3) != null && stack.getUnlocalizedName().contains("chestplate") && this.getProtectionValue(stack) + (double)((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(this.mc.thePlayer.getEquipmentInSlot(3)) + (double)((ItemArmor)this.mc.thePlayer.getEquipmentInSlot((int)3).getItem()).damageReduceAmount) {
                return true;
            }
            return this.mc.thePlayer.getEquipmentInSlot(4) != null && stack.getUnlocalizedName().contains("helmet") && this.getProtectionValue(stack) + (double)((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(this.mc.thePlayer.getEquipmentInSlot(4)) + (double)((ItemArmor)this.mc.thePlayer.getEquipmentInSlot((int)4).getItem()).damageReduceAmount;
        }
        return false;
    }

    private int betterSwap(ItemStack stack) {
        if (this.mc.thePlayer.getEquipmentInSlot(4) != null && Item.getIdFromItem(this.mc.thePlayer.getEquipmentInSlot(4).getItem()) == 397) {
            return 1;
        }
        if (stack.getItem() instanceof ItemArmor) {
            if (this.mc.thePlayer.getEquipmentInSlot(1) != null && stack.getUnlocalizedName().contains("boots") && this.getProtectionValue(stack) + (double)((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(this.mc.thePlayer.getEquipmentInSlot(1)) + (double)((ItemArmor)this.mc.thePlayer.getEquipmentInSlot((int)1).getItem()).damageReduceAmount) {
                return 4;
            }
            if (this.mc.thePlayer.getEquipmentInSlot(2) != null && stack.getUnlocalizedName().contains("leggings") && this.getProtectionValue(stack) + (double)((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(this.mc.thePlayer.getEquipmentInSlot(2)) + (double)((ItemArmor)this.mc.thePlayer.getEquipmentInSlot((int)2).getItem()).damageReduceAmount) {
                return 3;
            }
            if (this.mc.thePlayer.getEquipmentInSlot(3) != null && stack.getUnlocalizedName().contains("chestplate") && this.getProtectionValue(stack) + (double)((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(this.mc.thePlayer.getEquipmentInSlot(3)) + (double)((ItemArmor)this.mc.thePlayer.getEquipmentInSlot((int)3).getItem()).damageReduceAmount) {
                return 2;
            }
            if (this.mc.thePlayer.getEquipmentInSlot(4) != null && stack.getUnlocalizedName().contains("helmet") && this.getProtectionValue(stack) + (double)((ItemArmor)stack.getItem()).damageReduceAmount > this.getProtectionValue(this.mc.thePlayer.getEquipmentInSlot(4)) + (double)((ItemArmor)this.mc.thePlayer.getEquipmentInSlot((int)4).getItem()).damageReduceAmount) {
                return 1;
            }
        }
        return -1;
    }

    private boolean canEquip(ItemStack stack) {
        return this.mc.thePlayer.getEquipmentInSlot(1) == null && stack.getUnlocalizedName().contains("boots") || this.mc.thePlayer.getEquipmentInSlot(2) == null && stack.getUnlocalizedName().contains("leggings") || this.mc.thePlayer.getEquipmentInSlot(3) == null && stack.getUnlocalizedName().contains("chestplate") || this.mc.thePlayer.getEquipmentInSlot(4) == null && stack.getUnlocalizedName().contains("helmet");
    }

    private double getProtectionValue(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemArmor)) {
            return 0.0;
        }
        return (double)((ItemArmor)stack.getItem()).damageReduceAmount + (double)((100 - ((ItemArmor)stack.getItem()).damageReduceAmount * 4) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 4) * 0.0075;
    }
}

