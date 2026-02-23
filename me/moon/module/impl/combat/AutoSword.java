/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.combat;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class AutoSword
extends Module {
    private final NumberValue<Integer> delay = new NumberValue<Integer>("Delay", 100, 1, 2000, 1);
    private final TimerUtil timer = new TimerUtil();
    public static NumberValue<Integer> slot = new NumberValue<Integer>("Slot", 0, 0, 8, 1);
    private final BooleanValue inventoryonly = new BooleanValue("Inventory Only", false);
    private final BooleanValue customItems = new BooleanValue("Custom Items", true);
    private final String[] list = new String[]{"menu", "selector", "game", "gui", "server", "inventory", "play", "teleporter", "shop", "melee", "armor", "block", "castle", "mini", "warp", "teleport", "user", "team", "tool", "sure", "trade", "cancel", "accept", "soul", "book", "recipe", "profile", "tele", "port", "map", "kit", "select", "lobby", "vault", "lock", "quick", "travel", "cake", "war", "pvp", "Game Menu (Right Click)", "My Profile (Right Click)", "Shop (Right Click)", "Collectibles (Right Click)", "(Right Click)"};

    public AutoSword() {
        super("AutoSword", Module.Category.COMBAT, new Color(153, 204, 255, 255).getRGB());
        this.setDescription("Automatically equips the strongest sword");
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (event.isPre() || !this.timer.hasReached(((Integer)this.delay.getValue()).intValue()) || this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiInventory) || !(this.mc.currentScreen instanceof GuiInventory) && this.inventoryonly.isEnabled()) {
            return;
        }
        int best = -1;
        float swordDamage = 0.0f;
        for (int i = 9; i < 45; ++i) {
            float swordD;
            ItemStack is;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem() instanceof ItemSword) || !((swordD = this.getItemDamage(is)) >= swordDamage) || this.doNotEquip(is) || !this.customItems.isEnabled() && is.hasDisplayName()) continue;
            swordDamage = swordD;
            best = i;
        }
        ItemStack current = this.mc.thePlayer.inventoryContainer.getSlot(36 + (Integer)slot.getValue()).getStack();
        if (best != -1 || current == null || current.getItem() instanceof ItemSword && swordDamage > this.getItemDamage(current)) {
            float dmg;
            float f = dmg = current != null && current.getItem() != null && current.getItem() instanceof ItemSword ? this.getItemDamage(current) : -83474.0f;
            if (best != 36 + (Integer)slot.getValue() && best != -1 && swordDamage > dmg) {
                this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, best, (Integer)slot.getValue(), 2, this.mc.thePlayer);
                this.timer.reset();
            }
        }
    }

    private boolean doNotEquip(ItemStack item) {
        return item.getDisplayName().contains("\u00a77(Right Click)");
    }

    private float getItemDamage(ItemStack itemStack) {
        float damage = ((ItemSword)itemStack.getItem()).getDamageVsEntity();
        damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
        return damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.01f;
    }
}

