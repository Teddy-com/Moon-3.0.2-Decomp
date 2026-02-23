/*
 * Decompiled with CFR 0.152.
 */
package me.moon.gui.othergui.component.impl;

import me.moon.Moon;
import me.moon.gui.othergui.component.Component;
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
        RenderUtil.drawRect(this.getFinishedX() + 1.0f, this.getFinishedY(), this.getWidth() - 2.0f, this.getHeight(), clickGUI.getColor());
        Fonts.astolfoFont.drawString(this.getLabel(), this.getFinishedX() + 5.0f, this.getFinishedY() + this.getHeight() / 2.0f - (float)Fonts.astolfoFont.getStringHeight(this.getLabel()) / 2.0f + 0.5f, -1);
        Fonts.astolfoFont.drawString(this.getEnumValue().getValue().toString(), this.getFinishedX() + this.getWidth() - 2.0f - (float)Fonts.astolfoFont.getStringWidth(this.getEnumValue().getValue().toString()), this.getFinishedY() + this.getHeight() / 2.0f - (float)Fonts.astolfoFont.getStringHeight(this.getLabel()) / 2.0f + 0.5f, -1);
    }

    @Override
    public void keyTyped(char character, int keyCode) {
        super.keyTyped(character, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX(), this.getFinishedY(), this.getWidth(), this.getHeight());
        if (hovered) {
            if (mouseButton == 0) {
                this.getEnumValue().increment();
            } else if (mouseButton == 1) {
                this.getEnumValue().decrement();
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

