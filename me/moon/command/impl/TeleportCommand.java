/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.commons.lang3.math.NumberUtils
 */
package me.moon.command.impl;

import me.moon.Moon;
import me.moon.command.Command;
import me.moon.event.Handler;
import me.moon.event.impl.game.PacketEvent;
import me.moon.event.impl.game.TickEvent;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.utils.game.Printer;
import me.moon.utils.game.TimerUtil;
import me.moon.waypoint.Waypoint;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockSign;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class TeleportCommand
extends Command {
    private int x;
    private int y;
    private int z;
    private boolean onTop;
    private boolean isTeleporting;
    private boolean packet = true;
    private int moveUnder;
    private TimerUtil timerUtil = new TimerUtil();

    public TeleportCommand() {
        super("Teleport", new String[]{"teleport", "tp", "tele"});
    }

    @Override
    public void onRun(String[] args) {
        switch (args.length) {
            case 2: {
                if (args[1].toLowerCase().equals("stop")) {
                    if (this.onTop) {
                        this.stopTP();
                        Printer.print("Stopped.");
                        break;
                    }
                    Printer.print("Not running.");
                    break;
                }
                if (args[1].toLowerCase().equals("help")) {
                    Printer.print(".teleport stop/packet/xz/xyz/waypointname/playername/factionname");
                    break;
                }
                if (args[1].toLowerCase().equals("packet")) {
                    this.packet ^= true;
                    Printer.print("Packet set to " + this.packet);
                    break;
                }
                if (this.onTop) {
                    Printer.print("Already going.");
                    break;
                }
                for (Waypoint waypoint : Moon.INSTANCE.getWaypointManager().getWaypoints()) {
                    if (!waypoint.getLabel().toLowerCase().equals(args[1].toLowerCase()) || !TeleportCommand.mc.getCurrentServerData().serverIP.equals(waypoint.getServer()) || TeleportCommand.mc.thePlayer.dimension != waypoint.getDimension()) continue;
                    this.startTP(MathHelper.floor_double(waypoint.getX()), MathHelper.floor_double(waypoint.getY()), MathHelper.floor_double(waypoint.getZ()), true);
                    return;
                }
                for (EntityPlayer e : TeleportCommand.mc.theWorld.playerEntities) {
                    if (!e.getName().toLowerCase().equals(args[1].toLowerCase())) continue;
                    this.startTP(MathHelper.floor_double(e.posX), MathHelper.floor_double(e.posY), MathHelper.floor_double(e.posZ), true);
                    return;
                }
                Moon.INSTANCE.getEventBus().registerListener(this);
                TeleportCommand.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C01PacketChatMessage("/f who " + args[1]));
                break;
            }
            case 3: {
                if (this.onTop) {
                    Printer.print("Already going.");
                    break;
                }
                if (NumberUtils.isNumber((String)args[1]) && NumberUtils.isNumber((String)args[2])) {
                    if (!this.isUnderBlock() || this.packet) {
                        this.startTP(Integer.parseInt(args[1]), 255, Integer.parseInt(args[2]), true);
                        break;
                    }
                    Printer.print("You are under a block!");
                    break;
                }
                Printer.print("Invalid arguments.");
                Printer.print("try .teleport stop/packet/xz/xyz/waypointname/playername/factionname");
                break;
            }
            case 4: {
                if (this.onTop) {
                    Printer.print("Already going.");
                    break;
                }
                if (NumberUtils.isNumber((String)args[1]) && NumberUtils.isNumber((String)args[2]) && NumberUtils.isNumber((String)args[3])) {
                    if (!this.isUnderBlock() || this.packet) {
                        this.startTP(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), true);
                        break;
                    }
                    Printer.print("You are under a block!");
                    break;
                }
                Printer.print("Invalid arguments.");
                Printer.print("try .teleport stop/packet/xz/xyz/waypointname/playername/factionname");
                break;
            }
            default: {
                Printer.print("Invalid arguments.");
                Printer.print("try .teleport stop/packet/xz/xyz/waypointname/playername/factionname");
            }
        }
    }

    @Handler(value=UpdateEvent.class)
    public void onUpdate(UpdateEvent event) {
        if (this.onTop && !this.packet) {
            float angles = this.getRotationFromPosition(this.x, this.z);
            double distanceX = -3.0 * Math.sin(angles);
            double distanceZ = 3.0 * Math.cos(angles);
            if (TeleportCommand.mc.thePlayer.ticksExisted % 3 == 0) {
                if (TeleportCommand.mc.thePlayer.posY < 250.0) {
                    TeleportCommand.mc.thePlayer.motionY = 5.0;
                } else {
                    TeleportCommand.mc.thePlayer.motionY = 0.0;
                    this.isTeleporting = true;
                }
                if (TeleportCommand.mc.thePlayer.getDistanceSq(this.x, TeleportCommand.mc.thePlayer.posY, this.z) >= 32.0) {
                    if (this.isTeleporting) {
                        TeleportCommand.mc.thePlayer.motionX = distanceX;
                        TeleportCommand.mc.thePlayer.motionZ = distanceZ;
                    }
                } else {
                    TeleportCommand.mc.thePlayer.motionX = 0.0;
                    TeleportCommand.mc.thePlayer.motionZ = 0.0;
                    Printer.print("Finished you have arrived at x:" + (int)TeleportCommand.mc.thePlayer.posX + " z:" + (int)TeleportCommand.mc.thePlayer.posZ);
                    this.onTop = false;
                    this.isTeleporting = false;
                    TeleportCommand.mc.renderGlobal.loadRenderers();
                    Moon.INSTANCE.getEventBus().unregisterListener(this);
                }
            }
        }
    }

    @Handler(value=PacketEvent.class)
    public void onPacket(PacketEvent event) {
        if (this.packet && event.isSending()) {
            if (this.onTop) {
                if (event.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer packet = (C03PacketPlayer)event.getPacket();
                    if (!this.isTeleporting) {
                        packet.setY(this.y);
                        packet.setX(this.x);
                        packet.setZ(this.z);
                        TeleportCommand.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(this.x, this.y, this.z, true));
                        TeleportCommand.mc.thePlayer.setPosition(this.x, this.y, this.z);
                        this.isTeleporting = true;
                        this.moveUnder = 2;
                    }
                }
                if (this.timerUtil.hasReached(500L)) {
                    Printer.print("Finished you have arrived at x:" + this.x + " z:" + this.z);
                    this.onTop = false;
                    this.isTeleporting = false;
                    Moon.INSTANCE.getEventBus().unregisterListener(this);
                    this.timerUtil.reset();
                }
            }
        } else {
            if (event.getPacket() instanceof S08PacketPlayerPosLook && this.moveUnder == 2) {
                this.moveUnder = 1;
            }
            if (event.getPacket() instanceof S02PacketChat) {
                S02PacketChat packet = (S02PacketChat)event.getPacket();
                String text = packet.getChatComponent().getUnformattedText();
                if (text.contains("You cannot go past the border.")) {
                    event.setCancelled(true);
                }
                if (text.contains("Home: ")) {
                    if (text.contains("Not set")) {
                        this.stopTP();
                        Printer.print("Player or faction found but f home was not set.");
                        return;
                    }
                    try {
                        int x = Integer.parseInt(StringUtils.substringBetween((String)text, (String)"Home: ", (String)", "));
                        int z = Integer.parseInt(text.split(", ")[1]);
                        this.startTP(x, 255, z, false);
                    }
                    catch (Exception e) {
                        this.stopTP();
                    }
                } else if (text.contains(" not found.")) {
                    this.stopTP();
                    Printer.print("Player or faction not found.");
                }
            }
        }
    }

    @Handler(value=TickEvent.class)
    public void onTick(TickEvent event) {
        if (TeleportCommand.mc.thePlayer != null && this.moveUnder == 1 && this.packet && TeleportCommand.mc.thePlayer.getDistanceSq(this.x, TeleportCommand.mc.thePlayer.posY, this.z) > 1.0) {
            TeleportCommand.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(this.x, this.y, this.z, false));
            TeleportCommand.mc.thePlayer.sendQueue.addToSendQueueNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, true));
            this.moveUnder = 0;
        }
    }

    private void startTP(int x, int y, int z, boolean register) {
        if (this.onTop) {
            Printer.print("Already active!");
            return;
        }
        this.x = x;
        this.y = y;
        this.z = z;
        this.onTop = true;
        Printer.print("Teleporting to x:" + x + " y:" + y + " z:" + z + ".");
        if (register) {
            Moon.INSTANCE.getEventBus().registerListener(this);
        }
        this.timerUtil.reset();
    }

    private void stopTP() {
        this.z = 0;
        this.y = 0;
        this.x = 0;
        this.onTop = false;
        this.isTeleporting = false;
        Moon.INSTANCE.getEventBus().unregisterListener(this);
    }

    private boolean isUnderBlock() {
        for (int i = (int)(TeleportCommand.mc.thePlayer.posY + 2.0); i < 255; ++i) {
            BlockPos pos = new BlockPos(TeleportCommand.mc.thePlayer.posX, (double)i, TeleportCommand.mc.thePlayer.posZ);
            if (TeleportCommand.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir || TeleportCommand.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockFenceGate || TeleportCommand.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockSign || TeleportCommand.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockButton) continue;
            return true;
        }
        return false;
    }

    private float getRotationFromPosition(double x, double z) {
        double xDiff = x - TeleportCommand.mc.thePlayer.posX;
        double zDiff = z - TeleportCommand.mc.thePlayer.posZ;
        return (float)(Math.atan2(zDiff, xDiff) - 1.5707963267948966);
    }
}

