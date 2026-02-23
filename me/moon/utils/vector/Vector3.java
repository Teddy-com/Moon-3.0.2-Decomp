/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.vector;

public class Vector3<T extends Number> {
    public T x;
    public T y;
    public T z;

    public Vector3(T x, T y, T z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3() {
    }

    public T getX() {
        return this.x;
    }

    public Vector3 setX(T x) {
        this.x = x;
        return this;
    }

    public T getY() {
        return this.y;
    }

    public Vector3 setY(T y) {
        this.y = y;
        return this;
    }

    public T getZ() {
        return this.z;
    }

    public Vector3 setZ(T z) {
        this.z = z;
        return this;
    }
}

