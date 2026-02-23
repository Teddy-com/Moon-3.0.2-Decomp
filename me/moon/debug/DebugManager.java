/*
 * Decompiled with CFR 0.152.
 */
package me.moon.debug;

import java.awt.Color;
import java.util.ArrayList;
import me.moon.debug.Debug;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.AnimationUtil;
import net.minecraft.client.Minecraft;

public class DebugManager {
    private static ArrayList<Debug> debugs = new ArrayList();

    public static void renderDebugs() {
        float debugX = 75.0f;
        float debugY = 5.0f;
        for (Debug debug : debugs) {
            AnimationUtil debugAnimation = debug.animationUtil;
            if (System.currentTimeMillis() - debug.timeCreated > 1500L) {
                debug.alpha -= 8.0f / (float)Minecraft.getDebugFPS();
            }
            if (debug.alpha < 0.01f) {
                debugs.remove(debug);
                return;
            }
            debugAnimation.interpolate(150.0, debugY, 25.0f / (float)Minecraft.getDebugFPS());
            Fonts.moonSmaller.drawStringWithShadow(debug.message, debugX, (float)debugAnimation.getPosY(), new Color(1.0f, 1.0f, 1.0f, Math.max(debug.alpha, 0.0f)).getRGB());
            debugY += (float)(Fonts.moonSmaller.getHeight() + 2);
        }
    }

    public static void addDebug(String message) {
        debugs.add(new Debug(message, System.currentTimeMillis()));
    }
}

