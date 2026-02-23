/*
 * Decompiled with CFR 0.152.
 */
package me.moon.friend;

import java.io.File;
import java.util.ArrayList;
import me.moon.friend.Friend;
import me.moon.friend.FriendSaving;

public class FriendManager {
    private ArrayList<Friend> friends = new ArrayList();
    private FriendSaving friendSaving;

    public FriendManager(File dir) {
        this.friendSaving = new FriendSaving(dir);
        this.friendSaving.setup();
    }

    public FriendSaving getFriendSaving() {
        return this.friendSaving;
    }

    public ArrayList<Friend> getFriends() {
        return this.friends;
    }

    public void addFriend(String name) {
        this.friends.add(new Friend(name));
    }

    public void addFriendWithAlias(String name, String alias) {
        this.friends.add(new Friend(name, alias));
    }

    public Friend getFriend(String ign) {
        for (Friend friend : this.friends) {
            if (!friend.getName().equalsIgnoreCase(ign)) continue;
            return friend;
        }
        return null;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    public boolean isFriend(String ign) {
        return this.getFriend(ign) != null;
    }

    public void clearFriends() {
        this.friends.clear();
    }

    public void removeFriend(String name) {
        Friend f = this.getFriend(name);
        if (f != null) {
            this.friends.remove(f);
        }
    }
}

