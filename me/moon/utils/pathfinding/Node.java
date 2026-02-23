/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.pathfinding;

import me.moon.utils.location.MoonLocation;

public class Node
implements Cloneable {
    private MoonLocation location;
    private double G_COST;
    private double H_COST;
    private double F_COST;

    public Node(MoonLocation location, MoonLocation startLocation, MoonLocation endLocation) {
        this.location = location;
        this.G_COST = location.distance3D(startLocation);
        this.H_COST = location.distance3D(endLocation);
        this.F_COST = this.G_COST + this.H_COST;
    }

    public void setLocation(MoonLocation location) {
        this.location = location;
    }

    public MoonLocation getLocation() {
        return this.location;
    }

    public void setG_COST(double gCost) {
        this.G_COST = gCost;
    }

    public double getG_COST() {
        return this.G_COST;
    }

    public void setH_COST(double hCost) {
        this.H_COST = hCost;
    }

    public double getH_COST() {
        return this.H_COST;
    }

    public void setF_COST(double fCost) {
        this.F_COST = fCost;
    }

    public double getF_COST() {
        return this.F_COST;
    }

    public void updateCost(MoonLocation startLocation, MoonLocation endLocation) {
        this.G_COST = this.location.distance3D(startLocation);
        this.H_COST = this.location.distance3D(endLocation);
        this.F_COST = this.G_COST + this.H_COST;
    }

    public Node clone() {
        try {
            return (Node)super.clone();
        }
        catch (CloneNotSupportedException var2) {
            throw new Error(var2);
        }
    }
}

