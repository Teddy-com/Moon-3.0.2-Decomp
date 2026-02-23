/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.player;

import me.moon.event.Handler;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.module.impl.combat.KillAura;
import me.moon.utils.InventoryUtils;
import me.moon.utils.MathUtils;
import me.moon.utils.game.Printer;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;

public class InvManager
extends Module {
    private final EnumValue<inventoryModes> inventoryMode = new EnumValue<inventoryModes>("Inventory Mode", inventoryModes.NORMAL);
    public final NumberValue<Integer> delay = new NumberValue<Integer>("Delay", 150, 1, 300, 50);
    public final NumberValue<Integer> randomization = new NumberValue<Integer>("Delay Randomization", 50, 0, 500, 50);
    public final BooleanValue inventoryCleaner = new BooleanValue("Clean Inventory", true);
    public final NumberValue<Integer> blockLimit = new NumberValue<Integer>("Block Limit", 256, 64, 1024, 64, this.inventoryCleaner, "true");
    public final BooleanValue keepTools = new BooleanValue("Keep Tools", true, (Value)this.inventoryCleaner, "true");
    public final BooleanValue hotbarTools = new BooleanValue("Hotbar Tools", true, (Value)this.inventoryCleaner, "true");
    public final BooleanValue autoArmor = new BooleanValue("Auto Armor", true);
    public final BooleanValue autoSword = new BooleanValue("Auto Sword", true);
    private final BooleanValue autoPotion = new BooleanValue("Auto Potion", true);
    public final NumberValue<Integer> potionDelay = new NumberValue<Integer>("Potion Delay", 500, 50, 2500, 50, this.autoPotion, "true");
    private final BooleanValue healPotion = new BooleanValue("Heal Potion", true, (Value)this.autoPotion, "true");
    private final NumberValue<Integer> health = new NumberValue<Integer>("Health", 15, 1, 20, 1, this.autoPotion, "true");
    private final BooleanValue regenPotion = new BooleanValue("Regen Potion", true, (Value)this.autoPotion, "true");
    private final BooleanValue speedPotion = new BooleanValue("Speed Potion", true, (Value)this.autoPotion, "true");
    private final BooleanValue resistancePotion = new BooleanValue("Resistance Potion", true, (Value)this.autoPotion, "true");
    private final BooleanValue frogPotion = new BooleanValue("Frog Potion", true, (Value)this.autoPotion, "true");
    public final NumberValue<Integer> toolSlot = new NumberValue<Integer>("Tools slot", 1, 1, 6, 1, this.hotbarTools, "true");
    private final InventoryUtils inventoryUtils;
    private Integer queuedPotion;
    private final TimerUtil delayTimer = new TimerUtil();
    private final TimerUtil potionDelayTimer = new TimerUtil();
    private long nextDelay;

    public InvManager() {
        super("InvManager", Module.Category.PLAYER, -14642170);
        this.inventoryUtils = new InventoryUtils(this);
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        if (this.mc.currentScreen == null && this.inventoryMode.getValue() == inventoryModes.OPEN) {
            return;
        }
        if (event.isPre()) {
            this.setSuffix(this.delay.getValueAsString());
            if (this.queuedPotion != null) {
                Printer.print("throwing potion at: " + this.queuedPotion);
                int playerSlot = this.mc.thePlayer.inventory.currentItem;
                this.heldItem(this.queuedPotion);
                this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.getHeldItem()));
                this.heldItem(playerSlot);
                this.queuedPotion = null;
                return;
            }
            if (this.delayTimer.sleep(this.nextDelay)) {
                int cleaned;
                boolean emptyTargets;
                int sword;
                this.nextDelay = this.generateRandomDelay();
                if (this.autoArmor.isEnabled()) {
                    for (int i = 0; i < 4; ++i) {
                        int armor = this.inventoryUtils.getArmor(i);
                        if (armor == -1) {
                            continue;
                        }
                        if (this.mc.thePlayer.inventoryContainer.getSlot(i + 5).getStack() != null) {
                            if (this.mc.thePlayer.inventory.getFirstEmptyStack() == -1) break;
                            this.inventoryUtils.shift(i + 5);
                            return;
                        }
                        this.inventoryUtils.shift(armor);
                        return;
                    }
                }
                if (this.autoSword.isEnabled() && (sword = this.inventoryUtils.getSword()) != -1) {
                    this.inventoryUtils.swap(0, sword);
                    return;
                }
                if (this.keepTools.isEnabled() && this.hotbarTools.isEnabled()) {
                    int pickaxe = this.inventoryUtils.getPickaxe();
                    if (pickaxe != -1) {
                        this.inventoryUtils.swap((Integer)this.toolSlot.getValue(), pickaxe);
                        return;
                    }
                    int axe = this.inventoryUtils.getAxe();
                    if (axe != -1) {
                        this.inventoryUtils.swap((Integer)this.toolSlot.getValue() + 1, axe);
                        return;
                    }
                    int shovel = this.inventoryUtils.getShovel();
                    if (shovel != -1) {
                        this.inventoryUtils.swap((Integer)this.toolSlot.getValue() + 2, shovel);
                        return;
                    }
                }
                boolean bl = emptyTargets = KillAura.target == null && KillAura.dynamicTarget == null && KillAura.multiTarget == null;
                if (this.autoPotion.isEnabled() && this.potionDelayTimer.hasReached(((Integer)this.potionDelay.getValue()).intValue()) && emptyTargets && this.mc.thePlayer.onGround) {
                    int healPotionSlot = this.inventoryUtils.getHealPotion();
                    int regenPotionSlot = this.inventoryUtils.getRegenPotion();
                    int speedPotionSlot = this.inventoryUtils.getSpeedPotion(this.frogPotion.isEnabled());
                    int resistancePotionSlot = this.inventoryUtils.getResistancePotion();
                    if (this.healPotion.isEnabled() && healPotionSlot != -1 && this.mc.thePlayer.getHealth() <= (float)((Integer)this.health.getValue()).intValue()) {
                        this.queuedPotion = healPotionSlot;
                    } else if (this.regenPotion.isEnabled() && regenPotionSlot != -1 && !this.mc.thePlayer.isPotionActive(Potion.regeneration)) {
                        this.queuedPotion = regenPotionSlot;
                    } else if (this.speedPotion.isEnabled() && speedPotionSlot != -1 && !this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                        this.queuedPotion = speedPotionSlot;
                    } else if (this.resistancePotion.isEnabled() && resistancePotionSlot != -1 && !this.mc.thePlayer.isPotionActive(Potion.resistance)) {
                        this.queuedPotion = resistancePotionSlot;
                    }
                    if (this.queuedPotion != null) {
                        if (this.queuedPotion > 8) {
                            this.inventoryUtils.swap(8, this.queuedPotion);
                            this.queuedPotion = 8;
                        }
                        Printer.print("Looking the ground..");
                        event.setPitch(90.0f);
                        this.potionDelayTimer.reset();
                        return;
                    }
                }
                if (this.inventoryCleaner.isEnabled() && (cleaned = this.inventoryUtils.getCleaned()) != -1) {
                    this.inventoryUtils.drop(cleaned, true);
                }
            }
        }
    }

    private void heldItem(int slot) {
        this.mc.thePlayer.inventory.currentItem = slot;
        this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
    }

    private void openInventory() {
        this.mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
    }

    private void closeInventory() {
        this.mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(this.mc.thePlayer.inventoryContainer.windowId));
    }

    private long generateRandomDelay() {
        int random = MathUtils.getRandomInRange(0, (Integer)this.randomization.getValue()) + 25;
        random -= random % 50;
        return (Integer)this.delay.getValue() + random;
    }

    @Override
    public void onDisable() {
        this.queuedPotion = null;
    }

    @Override
    public void onEnable() {
        this.nextDelay = this.generateRandomDelay();
    }

    public static enum inventoryModes {
        NORMAL("Normal"),
        OPEN("OpenOnly");

        private final String name;

        private inventoryModes(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

