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
import me.moon.utils.value.impl.BooleanValue;

public class BooleanComponent
extends Component {
    private final BooleanValue booleanValue;

    public BooleanComponent(BooleanValue booleanValue, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(booleanValue.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.booleanValue = booleanValue;
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
        RenderUtil.drawCircle(this.getFinishedX() + this.getWidth() - 16.5f, this.getFinishedY() + 1.5f, 11.0f, this.booleanValue.isEnabled() ? ((Integer)clickGUI.color.getValue()).intValue() : new Color(25, 25, 25).getRGB());
        if (this.booleanValue.isEnabled()) {
            RenderUtil.drawCheckmark1(this.getFinishedX() + this.getWidth() - 15.0f, this.getFinishedY() + 6.0f, new Color(35, 35, 35).getRGB());
        }
        Fonts.moonSmaller.drawString(this.getLabel(), this.getFinishedX() + 5.0f, this.getFinishedY() + this.getHeight() / 2.0f - (float)Fonts.moonSmaller.getStringHeight(this.getLabel()) / 2.0f + 1.5f, -1);
    }

    @Override
    public void keyTyped(char character, int keyCode) {
        super.keyTyped(character, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + this.getWidth() - 14.0f, this.getFinishedY() + 1.0f, 12.0, this.getHeight() - 2.0f);
        if (hovered && mouseButton == 0) {
            this.getBooleanValue().setValue(this.getBooleanValue().getValue() == false);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    public BooleanValue getBooleanValue() {
        return this.booleanValue;
    }
}

