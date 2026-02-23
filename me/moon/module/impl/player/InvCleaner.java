/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.module.impl.combat.AutoArmor;
import me.moon.utils.MathUtils;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class InvCleaner
extends Module {
    private final NumberValue<Integer> delay = new NumberValue<Integer>("Delay", 150, 50, 300, 10);
    private final NumberValue<Integer> openingDelay = new NumberValue<Integer>("Opening Delay", 700, 0, 3000, 1);
    private final BooleanValue toggle = new BooleanValue("Toggle", true);
    private final BooleanValue inventoryonly = new BooleanValue("Inventory Only", false);
    private final BooleanValue tools = new BooleanValue("Keep Tools", true);
    private final BooleanValue bow = new BooleanValue("Keep Bows", true);
    private final BooleanValue arrows = new BooleanValue("Keep Arrows", true);
    private final BooleanValue flintnsteel = new BooleanValue("Keep Flint n Steels", true);
    private final BooleanValue tnt = new BooleanValue("Keep TNT", true);
    private final BooleanValue buckets = new BooleanValue("Keep Buckets", true);
    private final BooleanValue boats = new BooleanValue("Keep Boats", true);
    private final BooleanValue shears = new BooleanValue("Keep Shears", true);
    private final BooleanValue customItems = new BooleanValue("Custom Items", true);
    private final TimerUtil timer = new TimerUtil();
    private final TimerUtil oTimer = new TimerUtil();
    private final String[] list = new String[]{"menu", "selector", "game", "gui", "server", "inventory", "play", "teleporter", "shop", "melee", "armor", "block", "castle", "mini", "warp", "teleport", "user", "team", "tool", "sure", "trade", "cancel", "accept", "soul", "book", "recipe", "profile", "tele", "port", "map", "kit", "select", "lobby", "vault", "lock", "quick", "travel", "cake", "war", "pvp", "Game Menu (Right Click)", "My Profile (Right Click)", "Shop (Right Click)", "Collectibles (Right Click)", "(Right Click)"};

    public InvCleaner() {
        super("InvCleaner", Module.Category.PLAYER, -14642170);
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            if (this.mc.thePlayer != null && this.mc.thePlayer.deathTime == 1) {
                this.oTimer.reset();
                this.toggle();
                return;
            }
            if (!(this.mc.currentScreen instanceof GuiInventory) && this.inventoryonly.isEnabled()) {
                this.oTimer.reset();
                return;
            }
            int random = Math.round(MathUtils.getRandomInRange(-75, 125));
            if (this.oTimer.hasReached(((Integer)this.openingDelay.getValue()).intValue()) && this.mc.thePlayer != null && (this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiInventory) && this.timer.sleep((Integer)this.delay.getValue() + random)) {
                for (int i = 9; i < 45; ++i) {
                    if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
                    String name = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getDisplayName();
                    ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if (!this.isBad(is) || this.doNotThrow(is) || !this.customItems.isEnabled() && is.hasDisplayName()) continue;
                    this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, i, 1, 4, this.mc.thePlayer);
                    this.timer.reset();
                    break;
                }
                if (!this.hasGoodies()) {
                    AutoArmor.canAutoArmor = true;
                    this.oTimer.reset();
                } else {
                    AutoArmor.canAutoArmor = false;
                }
            }
        }
    }

    private boolean hasGoodies() {
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.isBad(is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) || is == this.mc.thePlayer.getCurrentEquippedItem()) continue;
            return true;
        }
        return false;
    }

    private ItemStack bestSword() {
        ItemStack best = null;
        float swordDamage = 0.0f;
        for (int i = 9; i < 45; ++i) {
            float swordD;
            ItemStack is;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem() instanceof ItemSword) || !((swordD = this.getItemDamage(is)) >= swordDamage)) continue;
            swordDamage = swordD;
            best = is;
        }
        return best;
    }

    private boolean doNotThrow(ItemStack item) {
        return item.getDisplayName().contains("\u00a77(Right Click)");
    }

    private boolean isBad(ItemStack item) {
        return item != null && (item.getItem().getUnlocalizedName().contains("stick") || item.getItem() instanceof ItemEgg || item.getItem().getUnlocalizedName().contains("string") || item.getItem().getUnlocalizedName().contains("compass") || item.getItem().getUnlocalizedName().contains("feather") || item.getItem().getUnlocalizedName().equalsIgnoreCase("chest") && !item.getDisplayName().toLowerCase().contains("collect") || item.getItem().getUnlocalizedName().contains("snow") || item.getItem().getUnlocalizedName().contains("enchant") || item.getItem().getUnlocalizedName().contains("exp") || item.getItem().getUnlocalizedName().contains("anvil") || item.getItem().getUnlocalizedName().contains("torch") || item.getItem().getUnlocalizedName().contains("skull") || item.getItem().getUnlocalizedName().contains("seeds") || item.getItem().getUnlocalizedName().contains("leather") || item.getItem().getUnlocalizedName().contains("fishing") || item.getItem().getUnlocalizedName().contains("wheat") || item.getItem().getUnlocalizedName().contains("flower") || item.getItem().getUnlocalizedName().contains("record") || item.getItem().getUnlocalizedName().contains("note") || item.getItem().getUnlocalizedName().contains("sugar") || item.getItem().getUnlocalizedName().contains("wire") || item.getItem().getUnlocalizedName().contains("trip") || item.getItem().getUnlocalizedName().contains("slime") || item.getItem().getUnlocalizedName().contains("web") || !this.tools.isEnabled() && item.getItem() instanceof ItemPickaxe || item.getItem() instanceof ItemGlassBottle || item.getItem() instanceof ItemArmor && !this.getBest().contains(item) || item.getItem() instanceof ItemSword && item != this.bestSword() || item.getItem().getUnlocalizedName().contains("piston") || item.getItem().getUnlocalizedName().contains("potion") && this.isBadPotion(item) || !this.tools.isEnabled() || item.getItem() instanceof ItemPickaxe && this.tools.isEnabled() && item != this.bestPickaxe() || !this.tools.isEnabled() || item.getItem() instanceof ItemAxe && this.tools.isEnabled() && item != this.bestAxe() || !this.tools.isEnabled() || item.getItem() instanceof ItemSpade && this.tools.isEnabled() && item != this.bestShovel() || item.getItem().getUnlocalizedName().contains("flint") && !this.flintnsteel.isEnabled() && item != this.bestDurability("flint") || item.getItem().getUnlocalizedName().contains("shears") && !this.shears.isEnabled() && item != this.bestDurability("shears") || item.getItem().getUnlocalizedName().contains("arrow") && !this.arrows.isEnabled() || item.getItem().getUnlocalizedName().contains("tnt") && !this.tnt.isEnabled() || item.getItem().getUnlocalizedName().contains("bucket") && !this.buckets.isEnabled() || item.getItem().getUnlocalizedName().contains("boat") && !this.boats.isEnabled() || item.getItem() instanceof ItemBow && !this.bow.isEnabled() && item != this.bestBow());
    }

    private List<ItemStack> getBest() {
        ArrayList<ItemStack> best = new ArrayList<ItemStack>();
        for (int i = 0; i < 4; ++i) {
            ItemStack armorStack = null;
            for (ItemStack itemStack : this.mc.thePlayer.inventory.armorInventory) {
                if (itemStack == null || !(itemStack.getItem() instanceof ItemArmor)) continue;
                ItemArmor stackArmor = (ItemArmor)itemStack.getItem();
                if (stackArmor.armorType != i) continue;
                armorStack = itemStack;
            }
            double reduction = armorStack == null ? -1.0 : this.getArmorStrength(armorStack);
            ItemStack slotStack = this.findBestArmor(i);
            if (slotStack != null && this.getArmorStrength(slotStack) <= reduction) {
                slotStack = armorStack;
            }
            if (slotStack == null) continue;
            best.add(slotStack);
        }
        return best;
    }

    private ItemStack findBestArmor(int itemSlot) {
        ItemStack i = null;
        double maxReduction = 0.0;
        for (int slot = 0; slot < 36; ++slot) {
            double reduction;
            ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[slot];
            if (itemStack == null || (reduction = this.getArmorStrength(itemStack)) == -1.0) continue;
            ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
            if (itemArmor.armorType != itemSlot || reduction < maxReduction) continue;
            maxReduction = reduction;
            i = itemStack;
        }
        return i;
    }

    private double getArmorStrength(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemArmor)) {
            return -1.0;
        }
        float damageReduction = ((ItemArmor)itemStack.getItem()).damageReduceAmount;
        Map<Integer, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        if (enchantments.containsKey(Enchantment.protection.effectId)) {
            int level = enchantments.get(Enchantment.protection.effectId);
            damageReduction += (float)Enchantment.protection.calcModifierDamage(level, DamageSource.generic);
        }
        return damageReduction;
    }

    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (PotionEffect o : potion.getEffects(stack)) {
                    PotionEffect effect = o;
                    if (effect.getPotionID() != Potion.poison.getId() && effect.getPotionID() != Potion.harm.getId() && effect.getPotionID() != Potion.moveSlowdown.getId() && effect.getPotionID() != Potion.weakness.getId()) continue;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onDisable() {
        this.oTimer.reset();
        AutoArmor.canAutoArmor = true;
    }

    private float getItemDamage(ItemStack itemStack) {
        float damage = ((ItemSword)itemStack.getItem()).getDamageVsEntity();
        damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
        return damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.01f;
    }

    private ItemStack bestAxe() {
        ItemStack best = null;
        float toolEfficiency = 0.0f;
        for (int i = 9; i < 45; ++i) {
            float toolD;
            ItemStack is;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem() instanceof ItemAxe) || !((toolD = this.getBestAxe(is)) > toolEfficiency)) continue;
            toolEfficiency = toolD;
            best = is;
        }
        return best;
    }

    private float getBestAxe(ItemStack itemStack) {
        float efficiency = ((ItemAxe)itemStack.getItem()).getToolMaterial().getHarvestLevel();
        return efficiency;
    }

    private ItemStack bestPickaxe() {
        ItemStack best = null;
        float toolEfficiency = 0.0f;
        for (int i = 9; i < 45; ++i) {
            float toolD;
            ItemStack is;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem() instanceof ItemPickaxe) || !((toolD = this.getBestPickaxe(is)) > toolEfficiency)) continue;
            toolEfficiency = toolD;
            best = is;
        }
        return best;
    }

    private ItemStack bestShovel() {
        ItemStack best = null;
        float toolEfficiency = 0.0f;
        for (int i = 9; i < 45; ++i) {
            float toolD;
            ItemStack is;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem() instanceof ItemSpade) || !((toolD = this.getBestShovel(is)) > toolEfficiency)) continue;
            toolEfficiency = toolD;
            best = is;
        }
        return best;
    }

    private float getBestShovel(ItemStack itemStack) {
        float efficiency = ((ItemSpade)itemStack.getItem()).getToolMaterial().getHarvestLevel();
        return efficiency;
    }

    private float getBestPickaxe(ItemStack itemStack) {
        float efficiency = ((ItemPickaxe)itemStack.getItem()).getToolMaterial().getHarvestLevel();
        return efficiency;
    }

    private ItemStack bestDurability(String uncolorizedName) {
        ItemStack best = null;
        float toolDurability = 0.0f;
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !(is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem().getUnlocalizedName().contains(uncolorizedName)) continue;
            float toolD = is.getItemDamage();
            if (toolD < toolDurability) {
                toolDurability = toolD;
                best = is;
                continue;
            }
            if (toolD != 0.0f) continue;
            best = is;
            toolDurability = toolD;
        }
        return best;
    }

    private ItemStack bestBow() {
        ItemStack best = null;
        float toolDamage = 0.0f;
        for (int i = 9; i < 45; ++i) {
            float toolD;
            ItemStack is;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem() instanceof ItemBow) || !((toolD = this.getBestDamage(is)) > toolDamage)) continue;
            toolDamage = toolD;
            best = is;
        }
        return best;
    }

    private float getBestDamage(ItemStack itemStack) {
        float damage = 0.1f;
        return damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemStack) * 1.25f;
    }
}

