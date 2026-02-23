/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package me.moon.module.impl.visuals.hud.settingsscreen.impl;

import java.awt.Color;
import me.moon.Moon;
import me.moon.module.impl.visuals.hud.settingsscreen.Component;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.EnumValue;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringUtils;

public class EnumComponent
extends Component {
    private EnumValue enumValue;

    public EnumComponent(EnumValue enumValue, float posX, float posY) {
        super(enumValue.getLabel(), posX, posY);
        this.enumValue = enumValue;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        boolean isHovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY(), Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.getComponentName() + ": " + StringUtils.capitalize((String)this.enumValue.getValue().toString().toLowerCase())) + 4, 10.0);
        RenderUtil.drawBorderedRect(this.getPosX(), this.getPosY(), 100.0, 12.0, 0.5, -10461088, isHovered ? new Color(-11184811).getRGB() : new Color(-12303292).getRGB());
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.enumValue.getValue().toString(), this.getPosX() + 50.0f - (float)(Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.enumValue.getValue().toString()) / 2), this.getPosY() + 3.0f, -1);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.enumValue.getLabel().toString(), this.getPosX() + 105.0f, this.getPosY() + 3.0f, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean isHovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY(), 100.0, 10.0);
        if (isHovered) {
            if (mouseButton == 0) {
                this.enumValue.increment();
            } else if (mouseButton == 1) {
                this.enumValue.decrement();
            }
            Moon.INSTANCE.getComponentManager().saveComps();
        }
    }
}

