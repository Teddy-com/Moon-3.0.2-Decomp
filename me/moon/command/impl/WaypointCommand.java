/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.math.NumberUtils
 */
package me.moon.command.impl;

import me.moon.Moon;
import me.moon.command.Command;
import me.moon.utils.game.Printer;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.math.NumberUtils;

public class WaypointCommand
extends Command {
    public WaypointCommand() {
        super("Waypoint", new String[]{"waypoint", "wp", "way", "points"});
    }

    @Override
    public void onRun(String[] args) {
        if (Minecraft.getMinecraft().isSingleplayer()) {
            Printer.print("Waypoints cannot be used in singleplayer!");
            return;
        }
        if (args.length > 1) {
            switch (args[1]) {
                case "help": {
                    Printer.print("[Add] [Name] [X] [Y] [Z] / [Del] [Name]");
                    break;
                }
                case "reload": {
                    Moon.INSTANCE.getWaypointManager().getWaypoints().clear();
                    Moon.INSTANCE.getWaypointManager().load();
                    Printer.print("Waypoints reloaded!");
                    break;
                }
                case "clear": 
                case "reset": {
                    if (Moon.INSTANCE.getWaypointManager().getWaypoints().isEmpty()) {
                        Printer.print("Waypoints list is empty!");
                        break;
                    }
                    Moon.INSTANCE.getWaypointManager().getWaypoints().clear();
                    Printer.print("Cleared waypoints list!");
                    Moon.INSTANCE.getWaypointManager().save();
                    break;
                }
                case "list": {
                    if (Moon.INSTANCE.getWaypointManager().getWaypoints().isEmpty()) {
                        Printer.print("Waypoints list is empty!");
                        break;
                    }
                    Printer.print("Here is a list of all your waypoints:");
                    Moon.INSTANCE.getWaypointManager().getWaypoints().forEach(waypoint -> {
                        if (Minecraft.getMinecraft().getCurrentServerData().serverIP.equals(waypoint.getServer()) && Minecraft.getMinecraft().thePlayer.dimension == waypoint.getDimension()) {
                            Printer.print(waypoint.getLabel() + " (" + waypoint.getX() + ", " + waypoint.getY() + ", " + waypoint.getZ() + ", " + waypoint.getServer() + ")");
                        }
                    });
                    break;
                }
                case "add": {
                    if (args.length == 6) {
                        if (NumberUtils.isNumber((String)args[2]) || !NumberUtils.isNumber((String)args[3]) || !NumberUtils.isNumber((String)args[4]) || !NumberUtils.isNumber((String)args[5])) break;
                        if (!Moon.INSTANCE.getWaypointManager().isWaypoint(args[2], Minecraft.getMinecraft().getCurrentServerData().serverIP)) {
                            Printer.print("Waypoint " + args[2] + " (" + args[3] + ", " + args[4] + ", " + args[5] + ") has been added.");
                            Moon.INSTANCE.getWaypointManager().add(args[2], Double.valueOf(args[3]), Double.valueOf(args[4]), Double.valueOf(args[5]), Minecraft.getMinecraft().getCurrentServerData().serverIP, Minecraft.getMinecraft().thePlayer.dimension);
                            Moon.INSTANCE.getWaypointManager().save();
                            break;
                        }
                        Printer.print(args[2] + " is already a waypoint!");
                        break;
                    }
                    if (args.length == 3) {
                        if (!Moon.INSTANCE.getWaypointManager().isWaypoint(args[2], Minecraft.getMinecraft().getCurrentServerData().serverIP)) {
                            Printer.print("Waypoint " + args[2] + " (" + (int)Minecraft.getMinecraft().thePlayer.posX + ", " + (int)Minecraft.getMinecraft().thePlayer.posY + ", " + (int)Minecraft.getMinecraft().thePlayer.posZ + ") has been added.");
                            Moon.INSTANCE.getWaypointManager().add(args[2], (int)Minecraft.getMinecraft().thePlayer.posX, (int)Minecraft.getMinecraft().thePlayer.posY, (int)Minecraft.getMinecraft().thePlayer.posZ, Minecraft.getMinecraft().getCurrentServerData().serverIP, Minecraft.getMinecraft().thePlayer.dimension);
                            Moon.INSTANCE.getWaypointManager().save();
                            break;
                        }
                        Printer.print(args[2] + " is already a waypoint!");
                        break;
                    }
                    Printer.print("Either one of the needed variables is null or is a string/number!");
                    break;
                }
                case "del": 
                case "remove": {
                    if (Moon.INSTANCE.getWaypointManager().isWaypoint(args[2], Minecraft.getMinecraft().getCurrentServerData().serverIP)) {
                        Printer.print(args[2] + " has been removed from your waypoints!");
                        Moon.INSTANCE.getWaypointManager().remove(args[2], Minecraft.getMinecraft().getCurrentServerData().serverIP);
                        Moon.INSTANCE.getWaypointManager().save();
                        break;
                    }
                    Printer.print(args[2] + " is not a waypoint!");
                }
            }
        }
    }
}

