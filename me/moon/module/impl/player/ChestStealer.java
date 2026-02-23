/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.game.WorldLoadEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.MathUtils;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.NumberValue;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

public class ChestStealer
extends Module {
    private TileEntityChest tileEntity;
    private final List<TileEntityChest> openedChests = new ArrayList<TileEntityChest>();
    private final NumberValue<Integer> delay = new NumberValue<Integer>("Delay", 100, 0, 1000, 1);
    public static final BooleanValue miss = new BooleanValue("Miss", true);
    private final BooleanValue aura = new BooleanValue("ChestAura", false);
    private final NumberValue<Float> range = new NumberValue<Float>("AuraRange", Float.valueOf(4.0f), Float.valueOf(0.0f), Float.valueOf(5.0f), Float.valueOf(0.1f), this.aura, "true");
    public static final BooleanValue customInventory = new BooleanValue("Custom Inventorys", false);
    public static final BooleanValue silent = new BooleanValue("Silent", false);
    public static final BooleanValue renderGui = new BooleanValue("Render Gui", false, (Value)silent, "true");
    private final TimerUtil timerUtil = new TimerUtil();
    private final TimerUtil FakeMiss = new TimerUtil();
    private final TimerUtil closedtimer = new TimerUtil();
    public static final String[] blacklist = new String[]{"menu", "selector", "game", "gui", "server", "inventory", "play", "teleporter", "shop", "melee", "armor", "block", "castle", "mini", "warp", "teleport", "user", "team", "tool", "sure", "trade", "cancel", "accept", "soul", "book", "recipe", "profile", "tele", "port", "map", "kit", "select", "lobby", "vault", "lock", "quick", "travel", "cake", "war", "pvp", "choose", "collectibles", "modos", "jogo", "clique"};
    public static final String[] whitelist = new String[]{"    gui"};

    public ChestStealer() {
        super("ChestStealer", Module.Category.PLAYER, new Color(255, 75, 170).getRGB());
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            this.setSuffix(((Integer)this.delay.getValue()).toString());
            if (this.aura.isEnabled()) {
                this.tileEntity = this.getClosestChest();
            }
            if (this.mc.currentScreen instanceof GuiChest) {
                if (this.tileEntity != null) {
                    this.openedChests.add(this.tileEntity);
                }
                GuiChest chest = (GuiChest)this.mc.currentScreen;
                if (!customInventory.getValue().booleanValue() && chest.upperChestInventory.hasCustomName()) {
                    return;
                }
                String name = chest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase();
                for (String badName : blacklist) {
                    for (String whiteName : whitelist) {
                        if (!name.contains(badName) || name.contains(whiteName)) continue;
                        return;
                    }
                }
                int rows = chest.getInventoryRows() * 9;
                for (int i = 0; i < rows; ++i) {
                    Slot slot = chest.inventorySlots.getSlot(i);
                    if (!slot.getHasStack()) continue;
                    if (this.timerUtil.hasReached((Integer)this.delay.getValue() + MathUtils.getRandomInRange(10, 110))) {
                        this.mc.playerController.windowClick(chest.inventorySlots.windowId, slot.slotNumber, 0, 1, this.mc.thePlayer);
                        this.timerUtil.reset();
                        continue;
                    }
                    if (!this.FakeMiss.hasReached(400L) || !miss.isEnabled()) continue;
                    this.FakeMiss.reset();
                    this.mc.playerController.windowClick(chest.inventorySlots.windowId, MathUtils.getRandomInRange(1, 15), 0, 1, this.mc.thePlayer);
                }
                if (!this.hasItems(chest) || this.isInventoryFull()) {
                    if (this.closedtimer.hasReached(MathUtils.getRandomInRange(75, 150))) {
                        this.mc.thePlayer.closeScreen();
                        Moon.INSTANCE.getNotificationManager().addNotification("Closed Chest!", 1000L);
                    }
                } else {
                    this.closedtimer.reset();
                }
            } else {
                this.FakeMiss.reset();
                if (this.aura.isEnabled() && this.tileEntity != null) {
                    float[] rots = this.calculateLookAt((float)this.tileEntity.getPos().getX() + 0.5f, this.tileEntity.getPos().getY() - 1, (float)this.tileEntity.getPos().getZ() + 0.5f, this.mc.thePlayer);
                    event.setPitch(rots[1]);
                    event.setYaw(rots[0]);
                }
            }
        } else if (this.tileEntity != null && !(this.mc.currentScreen instanceof GuiChest)) {
            this.mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(this.tileEntity.getPos(), 1, null, 0.0f, 0.0f, 0.0f));
        }
        if (this.mc.currentScreen == null) {
            this.closedtimer.reset();
        }
    }

    @Handler(value=WorldLoadEvent.class)
    public void onWorldLoad(WorldLoadEvent event) {
        this.openedChests.clear();
    }

    private boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack != null) continue;
            return false;
        }
        return true;
    }

    private TileEntityChest getClosestChest() {
        TileEntityChest entity = null;
        double maxDist = ((Float)this.range.getValue()).floatValue() * ((Float)this.range.getValue()).floatValue();
        for (TileEntity tileEntity : this.mc.theWorld.loadedTileEntityList) {
            if (!(tileEntity instanceof TileEntityChest) || this.openedChests.contains(tileEntity) || !(this.mc.thePlayer.getDistanceSq(tileEntity.getPos()) < maxDist)) continue;
            entity = (TileEntityChest)tileEntity;
            maxDist = this.mc.thePlayer.getDistanceSq(entity.getPos());
        }
        return entity;
    }

    private float[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;
        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        double pitch = Math.asin(diry /= len);
        double yaw = Math.atan2(dirz /= len, dirx /= len);
        pitch = pitch * 180.0 / Math.PI;
        yaw = yaw * 180.0 / Math.PI;
        return new float[]{(float)(yaw += 90.0), (float)pitch};
    }

    private boolean hasItems(GuiChest chest) {
        int items = 0;
        int rows = chest.getInventoryRows() * 9;
        for (int i = 0; i < rows; ++i) {
            Slot slot = chest.inventorySlots.getSlot(i);
            if (!slot.getHasStack()) continue;
            ++items;
        }
        return items > 0;
    }
}

