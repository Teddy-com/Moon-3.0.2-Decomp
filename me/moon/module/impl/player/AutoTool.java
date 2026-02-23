/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.player;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.module.Module;
import me.moon.utils.value.impl.BooleanValue;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

public class AutoTool
extends Module {
    private final BooleanValue mouseCheck = new BooleanValue("MouseCheck", true);
    private final BooleanValue silent = new BooleanValue("Silent", false);

    public AutoTool() {
        super("AutoTool", Module.Category.PLAYER, new Color(8569493).getRGB());
        this.setDescription("Selects the best tool when mining.");
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        if (event.isSending() && event.getPacket() instanceof C07PacketPlayerDigging) {
            C07PacketPlayerDigging packetPlayerDigging = (C07PacketPlayerDigging)event.getPacket();
            switch (packetPlayerDigging.getStatus()) {
                case START_DESTROY_BLOCK: {
                    int toolSlot;
                    BlockPos blockPosHit;
                    if (this.mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK && this.mouseCheck.isEnabled() || this.mc.thePlayer.capabilities.isCreativeMode) break;
                    BlockPos blockPos = blockPosHit = this.mouseCheck.isEnabled() ? this.mc.objectMouseOver.getBlockPos() : packetPlayerDigging.getPosition();
                    if (blockPosHit == null && this.mouseCheck.isEnabled() || this.mc.thePlayer.inventory.currentItem == (toolSlot = this.getBestTool(blockPosHit))) break;
                    if (this.silent.isEnabled()) {
                        event.setCancelled(true);
                        this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C09PacketHeldItemChange(toolSlot));
                        this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(event.getPacket());
                        this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, packetPlayerDigging.getPosition(), packetPlayerDigging.getFacing()));
                        break;
                    }
                    this.mc.thePlayer.inventory.currentItem = this.getBestTool(blockPosHit);
                    this.mc.playerController.updateController();
                    break;
                }
                case ABORT_DESTROY_BLOCK: 
                case STOP_DESTROY_BLOCK: 
                case DROP_ITEM: 
                case DROP_ALL_ITEMS: {
                    if (!this.silent.isEnabled()) break;
                    this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
                }
            }
        }
    }

    private int getBestTool(BlockPos pos) {
        Block block = this.mc.theWorld.getBlockState(pos).getBlock();
        int slot = 0;
        float dmg = 0.1f;
        for (int index = 36; index < 45; ++index) {
            ItemStack itemStack = this.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (itemStack == null || block == null || !(itemStack.getItem().getStrVsBlock(itemStack, block) > dmg)) continue;
            slot = index - 36;
            dmg = itemStack.getItem().getStrVsBlock(itemStack, block);
        }
        if (dmg > 1.0f) {
            return slot;
        }
        return this.mc.thePlayer.inventory.currentItem;
    }
}

