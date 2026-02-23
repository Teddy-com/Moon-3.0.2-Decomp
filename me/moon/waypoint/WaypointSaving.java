/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.reflect.TypeToken
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 */
package me.moon.waypoint;

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
import me.moon.waypoint.Waypoint;

public class WaypointSaving {
    private static File waypointFile;
    private static final Gson GSON;

    WaypointSaving(File dir) {
        waypointFile = new File(dir + File.separator + "waypoints.json");
    }

    public void setup() {
        try {
            if (!waypointFile.exists()) {
                waypointFile.createNewFile();
                this.saveFile();
                return;
            }
            this.loadFile();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public void loadFile() {
        try (FileReader inFile = new FileReader(waypointFile);){
            Moon.INSTANCE.getWaypointManager().setWaypoints((ArrayList)GSON.fromJson((Reader)inFile, new TypeToken<ArrayList<Waypoint>>(){}.getType()));
            if (Moon.INSTANCE.getWaypointManager().getWaypoints() == null) {
                Moon.INSTANCE.getWaypointManager().setWaypoints(new ArrayList<Waypoint>());
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void saveFile() {
        if (waypointFile.exists()) {
            try (PrintWriter writer = new PrintWriter(waypointFile);){
                writer.print(GSON.toJson(Moon.INSTANCE.getWaypointManager().getWaypoints()));
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

