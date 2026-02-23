/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.hud.settingsscreen.impl;

import java.awt.Color;
import me.moon.Moon;
import me.moon.module.impl.visuals.hud.settingsscreen.Component;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.BooleanValue;
import net.minecraft.client.Minecraft;

public class BooleanComponent
extends Component {
    private BooleanValue booleanValue;

    public BooleanComponent(BooleanValue booleanValue, float posX, float posY) {
        super(booleanValue.getLabel(), posX, posY);
        this.booleanValue = booleanValue;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        boolean isHovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY(), 10.0, 10.0);
        RenderUtil.drawBorderedRect(this.getPosX(), (double)this.getPosY() - 0.5, 10.0, 10.0, 0.5, new Color(150, 150, 150, 255).getRGB(), isHovered ? new Color(-11184811).getRGB() : new Color(-12303292).getRGB());
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.booleanValue.getLabel(), this.getPosX() + 15.0f, this.getPosY() + 1.0f, -1);
        if (this.booleanValue.isEnabled()) {
            RenderUtil.drawCheckMark(this.getPosX() + 5.0f, this.getPosY() - 2.0f, 10, new Color(255, 255, 255, 255).getRGB());
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY(), 10.0, 10.0) && mouseButton == 0) {
            this.booleanValue.toggle();
            Moon.INSTANCE.getComponentManager().saveComps();
        }
    }
}

