/*
 * Decompiled with CFR 0.152.
 */
package me.moon.macro;

public class Macro {
    private String label;
    private String text;
    private int key;

    public Macro(String label, int key, String text) {
        this.label = label;
        this.text = text;
        this.key = key;
    }

    public String getLabel() {
        return this.label;
    }

    public String getText() {
        return this.text;
    }

    public int getKey() {
        return this.key;
    }
}

