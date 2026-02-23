/*
 * Decompiled with CFR 0.152.
 */
package me.moon.gui.moderngui.component.impl;

import java.awt.Color;
import me.moon.Moon;
import me.moon.gui.moderngui.component.Component;
import me.moon.module.impl.visuals.ClickGui;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.EnumValue;

public class EnumComponent
extends Component {
    private final EnumValue enumValue;

    public EnumComponent(EnumValue enumValue, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(enumValue.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.enumValue = enumValue;
    }

    @Override
    public void init() {
    }

    @Override
    public void moved(float posX, float posY) {
        super.moved(posX, posY);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ClickGui clickGUI = (ClickGui)Moon.INSTANCE.getModuleManager().getModule("ClickGUI");
        RenderUtil.drawRect(this.getFinishedX(), this.getFinishedY(), this.getWidth(), this.getHeight(), clickGUI.getColor());
        Fonts.moon.drawString(this.getLabel(), this.getFinishedX() + 5.0f, this.getFinishedY() + 8.0f, -1);
        float impWidth = Fonts.moonSmaller.getStringWidth(this.enumValue.getValue().toString());
        for (Enum enoom : this.enumValue.getConstants()) {
            if (!((float)Fonts.moonSmaller.getStringWidth(enoom.name()) > impWidth)) continue;
            impWidth = Fonts.moonSmaller.getStringWidth(enoom.name());
        }
        RenderUtil.drawRect(this.getFinishedX() + this.getWidth() - 22.0f - impWidth, this.getFinishedY() + 3.0f, 20.0f + impWidth, 14.9f, new Color(33, 33, 33).getRGB());
        if (this.isExtended()) {
            RenderUtil.drawRect(this.getFinishedX() + this.getWidth() - 20.0f - impWidth, this.getFinishedY() + 16.0f, 16.0f + impWidth, 1.0, new Color(37, 37, 37).getRGB());
        }
        Fonts.moonSmaller.drawString(this.enumValue.getValue().toString(), this.getFinishedX() + this.getWidth() - 20.0f - impWidth, this.getFinishedY() + 9.0f, -1);
        RenderUtil.drawArrowS(this.getFinishedX() + this.getWidth() - 12.0f, this.getFinishedY() + 7.5f, this.isExtended(), -1);
        if (this.isExtended()) {
            this.setHeight(this.enumValue.getConstants().length * 15 + 14);
            float enumY = this.getFinishedY() + 25.0f;
            for (Enum enoom : this.enumValue.getConstants()) {
                if (enoom == this.enumValue.getValue()) continue;
                RenderUtil.drawRect(this.getFinishedX() + this.getWidth() - 22.0f - impWidth, enumY - 7.0f, 20.0f + impWidth, 15.0, new Color(33, 33, 33).getRGB());
                Fonts.moonSmaller.drawString(enoom.name(), this.getFinishedX() + this.getWidth() - 20.0f - impWidth, enumY, new Color(229, 229, 223, 255).getRGB());
                enumY += 15.0f;
            }
        } else {
            this.setHeight(20.0f);
        }
    }

    @Override
    public void keyTyped(char character, int keyCode) {
        super.keyTyped(character, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        float impWidth = Fonts.moonSmaller.getStringWidth(this.enumValue.getValue().toString());
        for (Enum enoom : this.enumValue.getConstants()) {
            if (!((float)Fonts.moonSmaller.getStringWidth(enoom.name()) > impWidth)) continue;
            impWidth = Fonts.moonSmaller.getStringWidth(enoom.name());
        }
        boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + this.getWidth() - 22.0f - impWidth, this.getFinishedY() + 3.0f, 20.0f + impWidth, 14.9f);
        if (hovered) {
            this.setExtended(!this.isExtended());
        }
        if (this.isExtended()) {
            float enumY = this.getFinishedY() + 25.0f;
            for (Enum enoom : this.enumValue.getConstants()) {
                if (enoom == this.enumValue.getValue()) continue;
                if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + this.getWidth() - 22.0f - impWidth, enumY - 4.0f, 20.0f + impWidth, 15.0)) {
                    this.enumValue.setValue(enoom);
                    this.setExtended(false);
                }
                enumY += 15.0f;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    public EnumValue getEnumValue() {
        return this.enumValue;
    }
}

