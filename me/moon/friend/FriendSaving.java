/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.reflect.TypeToken
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 */
package me.moon.friend;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import me.moon.Moon;
import me.moon.friend.Friend;

public class FriendSaving {
    private static File friendFile;
    private static final Gson GSON;

    public FriendSaving(File dir) {
        friendFile = new File(dir + File.separator + "friends.json");
    }

    public void setup() {
        try {
            if (!friendFile.exists()) {
                friendFile.createNewFile();
                return;
            }
            this.loadFile();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public void loadFile() {
        if (!friendFile.exists()) {
            return;
        }
        try (FileReader inFile = new FileReader(friendFile);){
            Moon.INSTANCE.getFriendManager().setFriends((ArrayList)GSON.fromJson((Reader)inFile, new TypeToken<ArrayList<Friend>>(){}.getType()));
            if (Moon.INSTANCE.getFriendManager().getFriends() == null) {
                Moon.INSTANCE.getFriendManager().setFriends(new ArrayList<Friend>());
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void saveFile() {
        if (friendFile.exists()) {
            try (PrintWriter writer = new PrintWriter(friendFile);){
                writer.print(GSON.toJson(Moon.INSTANCE.getFriendManager().getFriends()));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }
}

