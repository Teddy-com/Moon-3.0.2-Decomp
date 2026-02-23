/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.moon.module.impl.visuals;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import me.moon.Moon;
import me.moon.event.Handler;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.Module;
import me.moon.module.impl.visuals.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Secret
extends Module {
    public HashMap<Module, Integer> modColors = new HashMap();

    public Secret() {
        super("Secret", Module.Category.VISUALS, -1);
        this.setHidden(false);
    }

    @Handler(value=Render2DEvent.class)
    public void onRender2d(Render2DEvent event) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        this.mc.fontRendererObj.drawStringWithShadow("Moon 3.0 \u00a77" + dateTimeFormatter.format(LocalDateTime.now()), 2.0f, 2.0f, -1);
        this.mc.fontRendererObj.drawStringWithShadow("FPS: " + Minecraft.getDebugFPS(), 2.0f, 12.0f, -1);
        ScaledResolution sr = event.getScaledResolution();
        ArrayList<Module> modules = new ArrayList<Module>(Moon.INSTANCE.getModuleManager().getModuleMap().values());
        modules.sort(Comparator.comparingDouble(module -> -this.mc.fontRendererObj.getStringWidth(this.getModuleString((Module)module))));
        modules.removeIf(module -> !module.isEnabled());
        modules.removeIf(module -> module.isHidden());
        float yPosition = 2.0f;
        for (Module module2 : modules) {
            if (!this.modColors.containsKey(module2)) {
                this.modColors.put(module2, this.getRandomColor());
            }
            int color = this.modColors.get(module2);
            float xPosition = sr.getScaledWidth() - this.mc.fontRendererObj.getStringWidth(this.getModuleString(module2)) - 2;
            this.mc.fontRendererObj.drawStringWithShadow(this.getModuleString(module2), xPosition, yPosition, color);
            yPosition += (float)(this.mc.fontRendererObj.FONT_HEIGHT + 1);
            if (modules.indexOf(module2) != modules.size() - 1) continue;
            HUD.offset = yPosition;
        }
        this.mc.fontRendererObj.drawStringWithShadow(Math.round(this.mc.thePlayer.posX) + " " + Math.round(this.mc.thePlayer.posY) + " " + Math.round(this.mc.thePlayer.posZ), 2.0f, sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT - 2 - (this.mc.ingameGUI.getChatGUI().getChatOpen() ? 14 : 0), -1);
    }

    public String getModuleString(Module module) {
        StringBuilder moduleString = new StringBuilder(module.getRenderLabel() != null ? module.getRenderLabel() : module.getLabel());
        if (module.getSuffix() != null) {
            moduleString.append(ChatFormatting.GRAY).append(" - ").append(module.getSuffix());
        }
        return moduleString.toString();
    }

    public int getRandomColor() {
        return new Color((int)(Math.random() * 255.0), (int)(Math.random() * 255.0), (int)(Math.random() * 255.0), 255).getRGB();
    }
}

