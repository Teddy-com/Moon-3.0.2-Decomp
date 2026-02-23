/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.movement;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.player.SlowdownEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.EnumValue;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlowdown
extends Module {
    private final EnumValue<Modes> modes = new EnumValue<Modes>("Modes", Modes.VANILLA);
    private final BooleanValue items = new BooleanValue("Items", true);
    private final BooleanValue sprint = new BooleanValue("Sprint", true);
    private final BooleanValue soulSand = new BooleanValue("SoulSand", true);
    private boolean a = false;

    public NoSlowdown() {
        super("NoSlowdown", Module.Category.MOVEMENT, new Color(168, 166, 158).getRGB());
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (this.modes.getValue() == Modes.AAC) {
            this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
        }
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        if (this.modes.getValue() == Modes.NCP && event.getPacket() instanceof C03PacketPlayer && !this.a && this.mc.thePlayer.isMoving() && this.mc.thePlayer.isBlocking()) {
            event.setCancelled(true);
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            this.a = true;
            this.mc.thePlayer.sendQueue.addToSendQueueNoEvents(event.getPacket());
            this.a = false;
            this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.getCurrentEquippedItem()));
        }
    }

    @Handler(value=SlowdownEvent.class)
    public void onSlowDown(SlowdownEvent event) {
        switch (event.getType()) {
            case Item: {
                if (this.items.getValue().booleanValue()) {
                    event.setCancelled(true);
                }
                if (!this.mc.gameSettings.keyBindSprint.isKeyDown()) break;
                this.mc.thePlayer.setSprinting(true);
                break;
            }
            case Sprinting: {
                if (!this.sprint.getValue().booleanValue()) break;
                event.setCancelled(true);
                break;
            }
            case SoulSand: {
                if (!this.soulSand.getValue().booleanValue()) break;
                event.setCancelled(true);
            }
        }
    }

    private static enum Modes {
        NCP("NCP"),
        AAC("AAC"),
        VANILLA("Vanilla");

        private final String mode;

        private Modes(String mode2) {
            this.mode = mode2;
        }

        public String getMode() {
            return this.mode;
        }
    }
}

