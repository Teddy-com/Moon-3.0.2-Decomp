/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.hud.editscreen.buttons.impl;

import java.util.Objects;
import me.moon.Moon;
import me.moon.module.impl.visuals.hud.editscreen.buttons.AddButton;
import me.moon.module.impl.visuals.hud.impl.ScoreboardComponent;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ScoreboardAddButton
extends AddButton {
    public ScoreboardAddButton(float posY) {
        super("Add Scoreboard", posY);
    }

    @Override
    public void onDraw(int mouseX, int mouseY, ScaledResolution sr) {
        boolean isHovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY(), 96.0, 20.0);
        if (this.getPosX() == 0.0f) {
            return;
        }
        RenderUtil.drawRect(this.getPosX(), this.getPosY(), 96.0, 20.0, isHovered ? -16761970 : -15374912);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.getDisplayString(), this.getPosX() + 49.0f - (float)Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.getDisplayString()) / 2.0f, this.getPosY() + 10.0f - (float)Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 2.0f, -1);
    }

    @Override
    public void onRun() {
        int ok = Objects.requireNonNull(Moon.INSTANCE.getComponentManager().getDirectory().list()).length + 1;
        Moon.INSTANCE.getComponentManager().initComp(new ScoreboardComponent("Scoreboard" + ok));
    }
}

