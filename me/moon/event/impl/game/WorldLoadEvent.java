/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.impl.game;

import me.moon.event.Event;
import net.minecraft.client.multiplayer.WorldClient;

public class WorldLoadEvent
extends Event {
    private WorldClient worldClient;

    public WorldLoadEvent(WorldClient worldClient) {
        this.worldClient = worldClient;
    }

    public WorldClient getWorldClient() {
        return this.worldClient;
    }
}

