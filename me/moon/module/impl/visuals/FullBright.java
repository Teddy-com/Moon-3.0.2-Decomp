/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals;

import java.awt.Color;
import me.moon.module.Module;

public class FullBright
extends Module {
    private float oldGamma;

    public FullBright() {
        super("FullBright", Module.Category.VISUALS, new Color(0xFFDF66).getRGB());
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.oldGamma = this.mc.gameSettings.gammaSetting;
        this.mc.gameSettings.gammaSetting = 2000.0f;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.gameSettings.gammaSetting = this.oldGamma;
    }
}

