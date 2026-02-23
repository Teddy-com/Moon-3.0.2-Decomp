/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 */
package me.moon.waypoint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import me.moon.waypoint.Waypoint;

public class WaypointManager {
    private ArrayList<Waypoint> waypoints = new ArrayList();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private File waypointsFile;

    public WaypointManager(File dir) {
        this.waypointsFile = new File(dir + File.separator + "waypoints.json");
        this.load();
    }

    public void save() {
        if (this.waypointsFile == null) {
            return;
        }
        try {
            if (!this.waypointsFile.exists()) {
                this.waypointsFile.createNewFile();
            }
            PrintWriter printWriter = new PrintWriter(this.waypointsFile);
            printWriter.write(this.gson.toJson((JsonElement)this.toJson()));
            printWriter.close();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public void load() {
        if (!this.waypointsFile.exists()) {
            this.save();
            return;
        }
        try {
            JsonObject json = new JsonParser().parse((Reader)new FileReader(this.waypointsFile)).getAsJsonObject();
            this.fromJson(json);
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        this.getWaypoints().forEach(waypoint -> jsonArray.add((JsonElement)waypoint.toJson()));
        jsonObject.add("waypoints", (JsonElement)jsonArray);
        return jsonObject;
    }

    public void fromJson(JsonObject json) {
        JsonArray jsonArray = json.get("waypoints").getAsJsonArray();
        jsonArray.forEach(jsonElement -> {
            JsonObject jsonObject = (JsonObject)jsonElement;
            Waypoint waypoint = new Waypoint();
            waypoint.fromJson(jsonObject);
            this.getWaypoints().add(waypoint);
        });
    }

    private Waypoint getWaypoint(String label, String server) {
        for (Waypoint waypoint : this.waypoints) {
            if (!waypoint.getLabel().equalsIgnoreCase(label) || !waypoint.getServer().equals(server)) continue;
            return waypoint;
        }
        return null;
    }

    void setWaypoints(ArrayList<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public ArrayList<Waypoint> getWaypoints() {
        return this.waypoints;
    }

    public void add(String label, double x, double y, double z, String server, int dimension) {
        this.waypoints.add(new Waypoint(label, x, y, z, server, dimension));
    }

    public void remove(String label, String server) {
        Waypoint f = this.getWaypoint(label, server);
        if (f != null) {
            this.waypoints.remove(f);
        }
    }

    public boolean isWaypoint(String ign, String server) {
        return this.getWaypoint(ign, server) != null;
    }
}

