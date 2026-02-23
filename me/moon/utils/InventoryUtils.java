/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils;

import java.util.ArrayList;
import me.moon.module.impl.player.InvManager;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class InventoryUtils {
    private final InvManager invManager;
    private final Minecraft mc;

    public InventoryUtils(InvManager inv) {
        this.invManager = inv;
        this.mc = Minecraft.getMinecraft();
    }

    public int getHealPotion() {
        for (int i = 0; i <= 35; ++i) {
            ItemPotion potion;
            ItemStack is;
            Item item;
            if (this.mc.thePlayer == null || this.mc.theWorld == null || this.mc.thePlayer.inventory.mainInventory[i] == null || !((item = (is = this.mc.thePlayer.inventory.mainInventory[i]).getItem()) instanceof ItemPotion) || (potion = (ItemPotion)item).getEffects(is) == null || !ItemPotion.isSplash(is.getItemDamage())) continue;
            for (PotionEffect effect : potion.getEffects(is)) {
                if (effect.getPotionID() != Potion.heal.id) continue;
                return i;
            }
        }
        return -1;
    }

    public int getSpeedPotion(boolean frogPotion) {
        int slot = -1;
        for (int i = 0; i <= 35; ++i) {
            ItemPotion potion;
            ItemStack is;
            Item item;
            if (this.mc.thePlayer == null || this.mc.theWorld == null || this.mc.thePlayer.inventory.mainInventory[i] == null || !((item = (is = this.mc.thePlayer.inventory.mainInventory[i]).getItem()) instanceof ItemPotion) || (potion = (ItemPotion)item).getEffects(is) == null || !ItemPotion.isSplash(is.getItemDamage())) continue;
            for (PotionEffect effect : potion.getEffects(is)) {
                if (effect.getPotionID() == Potion.moveSpeed.id) {
                    slot = i;
                    continue;
                }
                if (effect.getPotionID() != Potion.jump.id || frogPotion) continue;
                slot = -1;
                break;
            }
            if (slot != -1) break;
        }
        return slot;
    }

    public int getRegenPotion() {
        for (int i = 0; i <= 35; ++i) {
            ItemPotion potion;
            ItemStack is;
            Item item;
            if (this.mc.thePlayer == null || this.mc.theWorld == null || this.mc.thePlayer.inventory.mainInventory[i] == null || !((item = (is = this.mc.thePlayer.inventory.mainInventory[i]).getItem()) instanceof ItemPotion) || (potion = (ItemPotion)item).getEffects(is) == null) continue;
            for (PotionEffect effect : potion.getEffects(is)) {
                if (effect.getPotionID() != Potion.regeneration.id) continue;
                return i;
            }
        }
        return -1;
    }

    public int getResistancePotion() {
        for (int i = 0; i <= 35; ++i) {
            ItemPotion potion;
            ItemStack is;
            Item item;
            if (this.mc.thePlayer == null || this.mc.theWorld == null || this.mc.thePlayer.inventory.mainInventory[i] == null || !((item = (is = this.mc.thePlayer.inventory.mainInventory[i]).getItem()) instanceof ItemPotion) || (potion = (ItemPotion)item).getEffects(is) == null) continue;
            for (PotionEffect effect : potion.getEffects(is)) {
                if (effect.getPotionID() != Potion.resistance.id) continue;
                return i;
            }
        }
        return -1;
    }

    public final int getPickaxe() {
        double efficiency = Double.NaN;
        int slot = -1;
        for (Slot s : this.getSlots()) {
            ItemStack stack = s.getStack();
            if (stack == null || !(stack.getItem() instanceof ItemPickaxe)) continue;
            ItemPickaxe pickaxe = (ItemPickaxe)stack.getItem();
            double harvestLevel = (double)pickaxe.getToolMaterial().getHarvestLevel() + this.getEnchants(stack);
            int slotValue = 36 + (Integer)this.invManager.toolSlot.getValue();
            if (!Double.isNaN(efficiency) && !(harvestLevel > efficiency) && (harvestLevel != efficiency || s.slotNumber != slotValue)) continue;
            slot = s.slotNumber != slotValue ? s.slotNumber : -1;
            efficiency = harvestLevel;
        }
        return slot;
    }

    public final int getAxe() {
        double efficiency = Double.NaN;
        int slot = -1;
        for (Slot s : this.getSlots()) {
            ItemStack stack = s.getStack();
            if (stack == null || !(stack.getItem() instanceof ItemAxe)) continue;
            ItemAxe axe = (ItemAxe)stack.getItem();
            double harvestLevel = (double)axe.getToolMaterial().getHarvestLevel() + this.getEnchants(stack);
            int slotValue = 36 + (Integer)this.invManager.toolSlot.getValue() + 1;
            if (!Double.isNaN(efficiency) && !(harvestLevel > efficiency) && (harvestLevel != efficiency || s.slotNumber != slotValue)) continue;
            slot = s.slotNumber != slotValue ? s.slotNumber : -1;
            efficiency = harvestLevel;
        }
        return slot;
    }

    public final int getShovel() {
        double efficiency = Double.NaN;
        int slot = -1;
        for (Slot s : this.getSlots()) {
            ItemStack stack = s.getStack();
            if (stack == null || !(stack.getItem() instanceof ItemSpade)) continue;
            ItemSpade shovel = (ItemSpade)stack.getItem();
            double harvestLevel = (double)shovel.getToolMaterial().getHarvestLevel() + this.getEnchants(stack);
            int slotValue = 36 + (Integer)this.invManager.toolSlot.getValue() + 2;
            if (!Double.isNaN(efficiency) && !(harvestLevel > efficiency) && (harvestLevel != efficiency || s.slotNumber != slotValue)) continue;
            slot = s.slotNumber != slotValue ? s.slotNumber : -1;
            efficiency = harvestLevel;
        }
        return slot;
    }

    public final int getSword() {
        double damage = Double.NaN;
        int slot = -1;
        for (Slot s : this.getSlots()) {
            ItemStack stack = s.getStack();
            if (stack == null || !(stack.getItem() instanceof ItemSword)) continue;
            ItemSword sword = (ItemSword)stack.getItem();
            double dmg = (double)sword.getDamageVsEntity() + this.getEnchants(stack);
            if (!Double.isNaN(damage) && !(dmg > damage) && (dmg != damage || s.slotNumber != 36)) continue;
            slot = s.slotNumber != 36 ? s.slotNumber : -1;
            damage = dmg;
        }
        return slot;
    }

    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock) || !((ItemBlock)item).getBlock().isFullBlock()) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }

    public final int getCleaned() {
        for (Slot s : this.getSlots()) {
            if (!this.isTrash(s)) continue;
            return s.slotNumber;
        }
        return -1;
    }

    public final boolean isTrash(Slot slot) {
        ItemStack stack = slot.getStack();
        if (stack == null) {
            return false;
        }
        if (stack.hasDisplayName()) {
            return false;
        }
        Item item = stack.getItem();
        if (item instanceof ItemSword) {
            int best = this.getSword();
            return slot.slotNumber != best && (best != -1 || slot.slotNumber != 36);
        }
        if (this.invManager.keepTools.isEnabled()) {
            if (item instanceof ItemPickaxe) {
                int best = this.getPickaxe();
                int slotValue = 36 + (Integer)this.invManager.toolSlot.getValue();
                return slot.slotNumber != best && (best != -1 || slot.slotNumber != slotValue);
            }
            if (item instanceof ItemAxe) {
                int best = this.getAxe();
                int slotValue = 36 + (Integer)this.invManager.toolSlot.getValue() + 1;
                return slot.slotNumber != best && (best != -1 || slot.slotNumber != slotValue);
            }
            if (item instanceof ItemSpade) {
                int best = this.getShovel();
                int slotValue = 36 + (Integer)this.invManager.toolSlot.getValue() + 2;
                return slot.slotNumber != best && (best != -1 || slot.slotNumber != slotValue);
            }
        }
        if (item instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor)item;
            int best = this.getArmor(armor.armorType);
            return slot.slotNumber != best && (best != -1 || slot.slotNumber != armor.armorType + 5);
        }
        if (item instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion)item;
            for (PotionEffect effect : potion.getEffects(stack)) {
                if (effect == null || !Potion.potionTypes[effect.getPotionID()].isBadEffect()) continue;
                return true;
            }
            return false;
        }
        if (item instanceof ItemBlock && ((ItemBlock)item).getBlock().isFullBlock()) {
            int blockCount = this.getBlockCount();
            return blockCount > (Integer)this.invManager.blockLimit.getValue();
        }
        if (item instanceof ItemFood) {
            ItemFood food = (ItemFood)item;
            return false;
        }
        switch (Item.getIdFromItem(item)) {
            case 263: 
            case 264: 
            case 265: 
            case 266: 
            case 280: 
            case 325: 
            case 326: 
            case 327: 
            case 368: 
            case 369: 
            case 370: 
            case 378: 
            case 388: 
            case 399: {
                return false;
            }
        }
        return true;
    }

    public final int getArmor(int type) {
        double protection = Double.NaN;
        int slot = -1;
        for (Slot s : this.getSlots()) {
            ItemStack stack = s.getStack();
            if (stack == null || !(stack.getItem() instanceof ItemArmor)) continue;
            ItemArmor armor = (ItemArmor)stack.getItem();
            if (armor.armorType != type) continue;
            double prot = (double)armor.getArmorMaterial().getDamageReductionAmount(type) + this.getEnchants(stack);
            if (!Double.isNaN(protection) && !(prot > protection) && (prot != protection || s.slotNumber != type + 5)) continue;
            slot = s.slotNumber != type + 5 ? s.slotNumber : -1;
            protection = prot;
        }
        return slot;
    }

    private ArrayList<Slot> getSlots() {
        ArrayList<Slot> slots = new ArrayList<Slot>();
        slots.addAll(this.mc.thePlayer.inventoryContainer.inventorySlots);
        if (this.mc.thePlayer.openContainer != null) {
            slots.removeIf(s -> this.mc.thePlayer.openContainer.inventorySlots.contains(s));
            slots.addAll(this.mc.thePlayer.openContainer.inventorySlots);
        }
        return slots;
    }

    private double getEnchants(ItemStack stack) {
        if (stack == null || !stack.isItemEnchanted()) {
            return 0.0;
        }
        NBTTagList tags = stack.getEnchantmentTagList();
        double value = 0.0;
        block5: for (int i = 0; i < tags.tagCount(); ++i) {
            NBTTagCompound compound = tags.getCompoundTagAt(i);
            short lvl = compound.getShort("lvl");
            switch (compound.getShort("id")) {
                default: {
                    value += 0.2 * (double)lvl;
                    continue block5;
                }
                case 0: {
                    value += (double)lvl;
                    continue block5;
                }
                case 16: {
                    value += 1.25 * (double)lvl;
                    continue block5;
                }
                case 20: {
                    value += 0.5 * (double)lvl;
                }
            }
        }
        return value;
    }

    public final void swap(int hotbar, int slot) {
        this.swap(this.mc.thePlayer.inventoryContainer.windowId, hotbar, slot);
    }

    public final void swap(int windowId, int hotbar, int slot) {
        this.mc.playerController.windowClick(windowId, slot, hotbar, 2, this.mc.thePlayer);
    }

    public final void shift(int slot) {
        this.shift(this.mc.thePlayer.inventoryContainer.windowId, slot);
    }

    public final void shift(int windowId, int slot) {
        this.mc.playerController.windowClick(windowId, slot, 0, 1, this.mc.thePlayer);
    }

    public final void drop(int slot, boolean stack) {
        this.drop(this.mc.thePlayer.inventoryContainer.windowId, slot, stack);
    }

    public final void drop(int windowId, int slot, boolean stack) {
        this.mc.playerController.windowClick(windowId, slot, stack ? 1 : 0, 4, this.mc.thePlayer);
    }
}

