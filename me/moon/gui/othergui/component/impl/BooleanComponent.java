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
import me.moon.utils.value.impl.BooleanValue;
import net.minecraft.client.renderer.GlStateManager;

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
        RenderUtil.drawRect(this.getFinishedX() + 1.0f, this.getFinishedY(), this.getWidth() - 2.0f, this.getHeight(), clickGUI.getColor());
        GlStateManager.resetColor();
        Fonts.astolfoFont.drawString(this.getLabel(), this.getFinishedX() + 5.0f, this.getFinishedY() + this.getHeight() / 2.0f - (float)Fonts.astolfoFont.getStringHeight(this.getLabel()) / 2.0f + 0.5f, -1);
        RenderUtil.drawRect(this.getFinishedX() + this.getWidth() - 20.0f, this.getFinishedY() + 3.0f, 16.0, this.getHeight() - 6.0f, -15460588);
        RenderUtil.drawRect(this.getFinishedX() + this.getWidth() - 20.0f + (float)(this.booleanValue.isEnabled() ? 8 : 0), this.getFinishedY() + 3.0f, 8.0, this.getHeight() - 6.0f, this.booleanValue.isEnabled() ? (Integer)clickGUI.color.getValue() : -13487566);
    }

    @Override
    public void keyTyped(char character, int keyCode) {
        super.keyTyped(character, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + this.getWidth() - 20.0f, this.getFinishedY() + 3.0f, 16.0, this.getHeight() - 6.0f);
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

