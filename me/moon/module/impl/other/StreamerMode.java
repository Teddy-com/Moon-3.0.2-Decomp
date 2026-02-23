/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.other;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.game.WorldLoadEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.module.impl.visuals.HUD;
import me.moon.utils.value.impl.BooleanValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class StreamerMode
extends Module {
    public static final BooleanValue hideNames = new BooleanValue("Hide Names", true);
    public static final BooleanValue hideScoreboardID = new BooleanValue("Hide Scoreboard ID", true);
    public static ArrayList<EntityLivingBase> playerCache = new ArrayList();

    public StreamerMode() {
        super("StreamerMode", Module.Category.OTHER, -65281);
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (playerCache.size() > 100) {
            playerCache.clear();
        }
        for (Entity entity : this.mc.theWorld.getLoadedEntityList()) {
            if (!(entity instanceof EntityOtherPlayerMP) || entity.isInvisible() || playerCache.contains(entity)) continue;
            playerCache.add((EntityLivingBase)entity);
        }
    }

    @Handler(value=WorldLoadEvent.class)
    public void onWorldLoad(WorldLoadEvent event) {
        playerCache.clear();
    }

    public static String replaceNames(String text) {
        Minecraft mc = Minecraft.getMinecraft();
        if (hideNames.isEnabled() && Moon.INSTANCE.getModuleManager().getModule("StreamerMode").isEnabled()) {
            try {
                if (mc.theWorld != null && mc.thePlayer != null) {
                    for (Entity entity : playerCache) {
                        if (entity.getName().isEmpty() || entity.getName().length() < 3) continue;
                        text = text.replace(entity.getName(), HUD.betterChat.getValue() != false ? "Censored" : "\u00a7k" + entity.getName() + "\u00a7f");
                    }
                    text = text.replaceAll(Minecraft.getMinecraft().thePlayer.getName(), Moon.INSTANCE.username);
                }
            }
            catch (ConcurrentModificationException concurrentModificationException) {
                // empty catch block
            }
        }
        return text;
    }
}

