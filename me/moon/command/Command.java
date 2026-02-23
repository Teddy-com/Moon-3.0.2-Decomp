/*
 * Decompiled with CFR 0.152.
 */
package me.moon.command;

import net.minecraft.client.Minecraft;

public class Command {
    private String label;
    private String[] handles;
    protected static final Minecraft mc = Minecraft.getMinecraft();

    public Command(String label, String[] handles) {
        this.label = label;
        this.handles = handles;
    }

    public String[] getHandles() {
        return this.handles;
    }

    public void onRun(String[] s) {
    }

    public String getLabel() {
        return this.label;
    }
}

