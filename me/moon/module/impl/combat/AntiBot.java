/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package me.moon.module.impl.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.EnumValue;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import org.apache.commons.lang3.StringUtils;

public class AntiBot
extends Module {
    private static final ArrayList<Entity> bots = new ArrayList();
    private final EnumValue<mode> modes = new EnumValue<mode>("Mode", mode.WATCHDOG);
    private final BooleanValue remove = new BooleanValue("Remove Bots", "Remove Bots From World", false);
    private final Map<Integer, Double> distanceMap = new HashMap<Integer, Double>();
    private final Set<Integer> swingSet = new HashSet<Integer>();

    public AntiBot() {
        super("AntiBot", Module.Category.COMBAT, new Color(153, 204, 255, 255).getRGB());
        this.setDescription("Add bots to a list of poop heads");
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (this.mc.theWorld == null) {
            return;
        }
        this.setSuffix(StringUtils.capitalize((String)((mode)((Object)this.modes.getValue())).getName()));
        switch ((mode)((Object)this.modes.getValue())) {
            case WATCHDOG: {
                if (!event.isPre()) break;
                if (this.mc.thePlayer.ticksExisted % 600 == 0) {
                    bots.clear();
                }
                for (Entity entity : this.mc.theWorld.getLoadedEntityList()) {
                    if (!(entity instanceof EntityPlayer) || entity == this.mc.thePlayer) continue;
                    if (this.isEntityBot(entity)) {
                        if (this.remove.isEnabled()) {
                            this.mc.theWorld.removeEntity(entity);
                        }
                        bots.add(entity);
                        continue;
                    }
                    bots.remove(entity);
                }
                break;
            }
            case MINEPLEX: {
                if (!event.isPre()) break;
                for (Entity e : this.mc.theWorld.getLoadedEntityList()) {
                    if (!(e instanceof EntityPlayer)) continue;
                    if (e.posY > this.mc.thePlayer.posY + 1.0 && e.isInvisible() && e.ticksExisted > 1) {
                        double[] abovePlayerValues;
                        double yDistance = ((EntityPlayer)e).posY - this.mc.thePlayer.posY;
                        for (double value : abovePlayerValues = new double[]{4.0, 1.333333333, 2.666666666, 3.111111111}) {
                            if (!(Math.abs(yDistance - value) <= 1.0E-5)) continue;
                            if (this.remove.isEnabled()) {
                                this.mc.theWorld.removeEntity(e);
                            }
                            bots.add(e);
                            break;
                        }
                    }
                    if (e.ticksExisted < 2 && ((EntityPlayer)e).getHealth() < 20.0f && ((EntityPlayer)e).getHealth() > 0.0f && e == this.mc.thePlayer) continue;
                }
                break;
            }
        }
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        Packet<INetHandlerPlayClient> packet;
        if (this.mc.theWorld == null) {
            return;
        }
        if (event.getPacket() instanceof S0CPacketSpawnPlayer) {
            packet = (S0CPacketSpawnPlayer)event.getPacket();
            double x = ((S0CPacketSpawnPlayer)packet).getX();
            double y = ((S0CPacketSpawnPlayer)packet).getY();
            double z = ((S0CPacketSpawnPlayer)packet).getZ();
            double d = this.mc.thePlayer.getDistance(x, y, z);
            this.distanceMap.put(((S0CPacketSpawnPlayer)packet).getEntityID(), d);
        }
        if (event.getPacket() instanceof S0BPacketAnimation) {
            packet = (S0BPacketAnimation)event.getPacket();
            if (((S0BPacketAnimation)packet).getAnimationType() != 0) {
                return;
            }
            this.swingSet.add(((S0BPacketAnimation)packet).getEntityID());
        }
    }

    private boolean isEntityBot(Entity entity) {
        int ping;
        NetworkPlayerInfo networkPlayerInfo = this.mc.getNetHandler().getPlayerInfo(entity.getUniqueID());
        try {
            ping = networkPlayerInfo.getResponseTime();
        }
        catch (NullPointerException ex) {
            return false;
        }
        if (ping == 0) {
            return false;
        }
        return ping > 1;
    }

    private boolean isOnTab(Entity entity) {
        return this.mc.getNetHandler().getPlayerInfoMap().stream().anyMatch(info -> info.getGameProfile().getName().equalsIgnoreCase(entity.getName()));
    }

    @Override
    public void onEnable() {
        bots.clear();
    }

    @Override
    public void onDisable() {
        bots.clear();
        super.onDisable();
    }

    public static List<Entity> getBots() {
        return bots;
    }

    private static enum mode {
        WATCHDOG("Watchdog"),
        MINEPLEX("Mineplex");

        private String name;

        private mode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

