/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.game;

public enum Rotations {
    INSTANCE;

    Rotation currentRotation = new Rotation(Float.valueOf(0.0f), Float.valueOf(0.0f));
    Rotation previousRotation = new Rotation(Float.valueOf(0.0f), Float.valueOf(0.0f));

    public void setPreviousRotation(float yaw, float pitch) {
        this.previousRotation.setYaw(Float.valueOf(yaw));
        this.previousRotation.setPitch(Float.valueOf(pitch));
    }

    public void setCurrentRotation(float yaw, float pitch) {
        this.currentRotation.setYaw(Float.valueOf(yaw));
        this.currentRotation.setPitch(Float.valueOf(pitch));
    }

    public void setPreviousRotation(Rotation previousRotation) {
        this.previousRotation = previousRotation;
    }

    public void setCurrentRotation(Rotation currentRotation) {
        this.currentRotation = currentRotation;
    }

    public Rotation getCurrentRotation() {
        return this.currentRotation;
    }

    public Rotation getPreviousRotation() {
        return this.previousRotation;
    }

    public static class Rotation {
        public Float yaw;
        public Float pitch;

        public Rotation(Float yaw, Float pitch) {
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public void setPitch(Float pitch) {
            this.pitch = pitch;
        }

        public void setYaw(Float yaw) {
            this.yaw = yaw;
        }

        public Float getPitch() {
            return this.pitch;
        }

        public Float getYaw() {
            return this.yaw;
        }
    }
}

