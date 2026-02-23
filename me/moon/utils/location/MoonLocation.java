/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.location;

import net.minecraft.world.World;

public class MoonLocation
implements Cloneable {
    private World world;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    public MoonLocation(World world, double x, double y, double z) {
        this(world, x, y, z, 0.0f, 0.0f);
    }

    public MoonLocation(World world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return this.world;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return this.x;
    }

    public void addToX(double addition) {
        this.x += addition;
    }

    public void removeToX(double reduction) {
        this.x -= reduction;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return this.y;
    }

    public void addToY(double addition) {
        this.y += addition;
    }

    public void removeToY(double reduction) {
        this.y -= reduction;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getZ() {
        return this.z;
    }

    public void addToZ(double addition) {
        this.z += addition;
    }

    public void removeToZ(double reduction) {
        this.z -= reduction;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getPitch() {
        return this.pitch;
    }

    public MoonLocation add(MoonLocation vec) {
        if (vec != null && vec.getWorld() == this.getWorld()) {
            this.x += vec.x;
            this.y += vec.y;
            this.z += vec.z;
            return this;
        }
        throw new IllegalArgumentException("Cannot add Locations of differing worlds");
    }

    public double distance2D(MoonLocation location) {
        return Math.sqrt(Math.pow(this.x - location.x, 2.0) + Math.pow(this.z - location.z, 2.0));
    }

    public double distance3D(MoonLocation location) {
        return Math.sqrt(Math.pow(this.x - location.x, 2.0) + Math.pow(this.y - location.y, 2.0) + Math.pow(this.z - location.z, 2.0));
    }

    public MoonLocation add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public MoonLocation subtract(MoonLocation vec) {
        if (vec != null && vec.getWorld() == this.getWorld()) {
            this.x -= vec.x;
            this.y -= vec.y;
            this.z -= vec.z;
            return this;
        }
        throw new IllegalArgumentException("Cannot add Locations of differing worlds");
    }

    public MoonLocation subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public MoonLocation multiply(double m) {
        this.x *= m;
        this.y *= m;
        this.z *= m;
        return this;
    }

    public MoonLocation zero() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        return this;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        MoonLocation other = (MoonLocation)obj;
        if (!(this.world == other.world || this.world != null && this.world.equals(other.world))) {
            return false;
        }
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z)) {
            return false;
        }
        if (Float.floatToIntBits(this.pitch) != Float.floatToIntBits(other.pitch)) {
            return false;
        }
        return Float.floatToIntBits(this.yaw) == Float.floatToIntBits(other.yaw);
    }

    public String toString() {
        return "Location{world=" + this.world + ",x=" + this.x + ",y=" + this.y + ",z=" + this.z + ",pitch=" + this.pitch + ",yaw=" + this.yaw + '}';
    }

    public MoonLocation clone() {
        try {
            return (MoonLocation)super.clone();
        }
        catch (CloneNotSupportedException var2) {
            throw new Error(var2);
        }
    }
}

