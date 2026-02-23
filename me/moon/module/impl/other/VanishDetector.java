/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.other;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.game.TickEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.game.Printer;
import net.minecraft.network.play.server.S38PacketPlayerListItem;

public class VanishDetector
extends Module {
    private final Set<UUID> vanished = new HashSet<UUID>();
    private final HashMap<UUID, String> uuids = new HashMap();

    public VanishDetector() {
        super("VanishDetector", Module.Category.OTHER, new Color(11927534).getRGB());
        this.setDescription("no vanish niggas");
    }

    @Handler(value=TickEvent.class)
    public void onTick(TickEvent event) {
        if (this.mc.getNetHandler() != null) {
            this.mc.getNetHandler().getRealPlayerInfoMap().values().forEach(info -> {
                if (info.getGameProfile().getName() != null) {
                    this.uuids.put(info.getGameProfile().getId(), info.getGameProfile().getName());
                }
            });
        }
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        S38PacketPlayerListItem packet;
        if (event.getPacket() instanceof S38PacketPlayerListItem && (packet = (S38PacketPlayerListItem)event.getPacket()).func_179768_b() == S38PacketPlayerListItem.Action.UPDATE_LATENCY) {
            packet.func_179767_a().forEach(data -> {
                if (this.mc.getNetHandler().getPlayerInfo(data.getProfile().getId()) == null) {
                    if (!this.vanished.contains(data.getProfile().getId())) {
                        Printer.print(this.getName(data.getProfile().getId()) + " is now vanished.");
                    }
                    this.vanished.add(data.getProfile().getId());
                }
            });
        }
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        this.vanished.forEach(uuid -> {
            if (this.mc.getNetHandler().getPlayerInfo((UUID)uuid) != null) {
                Printer.print(this.getName((UUID)uuid) + " is no longer vanished.");
            }
            this.vanished.remove(uuid);
        });
    }

    public String getName(UUID uuid) {
        if (this.uuids.containsKey(uuid)) {
            return this.uuids.get(uuid);
        }
        return "undefined - " + uuid.toString();
    }
}

