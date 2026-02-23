/*
 * Decompiled with CFR 0.152.
 */
package me.moon.command.impl;

import me.moon.Moon;
import me.moon.command.Command;
import me.moon.utils.game.Printer;

public class FriendCommand
extends Command {
    public FriendCommand() {
        super("Friend", new String[]{"friends", "friend", "f"});
    }

    @Override
    public void onRun(String[] args) {
        if (args.length == 1) {
            Printer.print(".friend add <name>");
            return;
        }
        switch (args[1]) {
            case "add": 
            case "a": 
            case "Add": 
            case "Ad": 
            case "ad": {
                if (args.length <= 2) break;
                if (Moon.INSTANCE.getFriendManager().isFriend(args[2])) {
                    Printer.print(args[2] + " is already your friend.");
                    return;
                }
                if (args.length < 4) {
                    Moon.INSTANCE.getNotificationManager().addNotification("Added " + args[2] + " to your friends list without an alias.", 2000L);
                    Printer.print("Added " + args[2] + " to your friends list without an alias.");
                    Moon.INSTANCE.getFriendManager().addFriend(args[2]);
                    break;
                }
                Moon.INSTANCE.getNotificationManager().addNotification("Added " + args[2] + " to your friends list.", 2000L);
                Printer.print("Added " + args[2] + " to your friends list with the alias " + args[3] + ".");
                Moon.INSTANCE.getFriendManager().addFriendWithAlias(args[2], args[3]);
                break;
            }
            case "del": 
            case "delete": 
            case "d": 
            case "rem": 
            case "remove": 
            case "r": {
                if (args.length <= 2) break;
                if (!Moon.INSTANCE.getFriendManager().isFriend(args[2])) {
                    Printer.print(args[2] + " is not your friend.");
                    return;
                }
                if (!Moon.INSTANCE.getFriendManager().isFriend(args[2])) break;
                Printer.print("Removed " + args[2] + " from your friends list.");
                Moon.INSTANCE.getFriendManager().removeFriend(args[2]);
                break;
            }
            case "c": 
            case "clear": {
                if (Moon.INSTANCE.getFriendManager().getFriends().isEmpty()) {
                    Printer.print("Your friends list is already empty.");
                    return;
                }
                Printer.print("Your have cleared your friends list. Friends removed: " + Moon.INSTANCE.getFriendManager().getFriends().size());
                Moon.INSTANCE.getFriendManager().clearFriends();
                break;
            }
            case "list": 
            case "l": {
                if (Moon.INSTANCE.getFriendManager().getFriends().isEmpty()) {
                    Printer.print("Your friends list is empty.");
                    return;
                }
                Printer.print("Your current friends are: ");
                Moon.INSTANCE.getFriendManager().getFriends().forEach(friend -> Printer.print("Username: " + friend.getName() + (friend.getAlias() != null ? " - Alias: " + friend.getAlias() : "")));
            }
        }
    }
}

