/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package me.moon.waypoint;

import com.google.gson.JsonObject;

public class Waypoint {
    private String label;
    private String server;
    private double x;
    private double y;
    private double z;
    private int dimension;

    Waypoint(String label, double x, double y, double z, String server, int dimension) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.z = z;
        this.server = server;
        this.dimension = dimension;
    }

    public Waypoint() {
    }

    public String getLabel() {
        return this.label;
    }

    public String getServer() {
        return this.server;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public int getDimension() {
        return this.dimension;
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("label", this.label);
        jsonObject.addProperty("server", this.server);
        jsonObject.addProperty("x", (Number)this.x);
        jsonObject.addProperty("y", (Number)this.y);
        jsonObject.addProperty("z", (Number)this.z);
        jsonObject.addProperty("dimension", (Number)this.dimension);
        return jsonObject;
    }

    public void fromJson(JsonObject json) {
        this.label = json.get("label").getAsString();
        this.server = json.get("server").getAsString();
        this.x = json.get("x").getAsInt();
        this.y = json.get("y").getAsInt();
        this.z = json.get("z").getAsInt();
        this.dimension = json.get("dimension").getAsInt();
    }
}

