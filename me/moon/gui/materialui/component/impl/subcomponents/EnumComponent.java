/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package me.moon.gui.materialui.component.impl.subcomponents;

import java.awt.Color;
import me.moon.gui.materialui.component.Component;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.EnumValue;
import org.apache.commons.lang3.StringUtils;

public class EnumComponent
extends Component {
    private EnumValue enumValue;
    private boolean extended;

    public EnumComponent(EnumValue enumValue, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(enumValue.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.enumValue = enumValue;
    }

    @Override
    public void componentMoved(float movedX, float movedY) {
        super.componentMoved(movedX, movedY);
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
        super.onDrawScreen(mouseX, mouseY, partialTicks);
        Fonts.sectioNormal.drawStringWithShadow(this.getLabel(), this.getPosX(), this.getPosY() + 1.0f, new Color(229, 229, 223, 255).getRGB());
        if (this.isExtended()) {
            this.setHeight(20 + 15 * (this.enumValue.getConstants().length - 1));
            RenderUtil.drawRect(this.getPosX() + this.getWidth() - 82.0f, this.getPosY() + 13.5f, 76.0, 15 * (this.enumValue.getConstants().length - 1), new Color(25, 25, 25, 255).getRGB());
            RenderUtil.drawBorderedRect(this.getPosX() + this.getWidth() - 82.0f, this.getPosY() + 12.5f, 76.0, 1 + 15 * (this.enumValue.getConstants().length - 1), 0.5, new Color(-15305263).getRGB(), new Color(25, 25, 25, 255).getRGB());
            float enumY = this.getPosY() + 20.0f;
            for (Enum enoom : this.enumValue.getConstants()) {
                if (enoom == this.enumValue.getValue()) continue;
                Fonts.sectioNormal.drawStringWithShadow(StringUtils.capitalize((String)enoom.name().toLowerCase()), this.getPosX() + this.getWidth() - 44.0f - (float)(Fonts.clickGuiFont.getStringWidth(StringUtils.capitalize((String)enoom.name().toLowerCase())) / 2), enumY, new Color(229, 229, 223, 255).getRGB());
                enumY += 15.0f;
            }
        } else {
            this.setHeight(20.0f);
        }
        RenderUtil.drawRoundedRect(this.getPosX() + this.getWidth() - 84.0f, this.getPosY() - 1.5f, 80.0, 15.0, 0.0, new Color(25, 25, 25, 255).getRGB());
        RenderUtil.drawBorderedRect(this.getPosX() + this.getWidth() - 84.0f, this.getPosY() - 1.5f, 80.0, 15.0, 0.5, new Color(-15305263).getRGB(), new Color(25, 25, 25, 255).getRGB());
        if (this.isExtended()) {
            RenderUtil.drawRect((double)(this.getPosX() + this.getWidth()) - 81.5, this.getPosY() - 2.5f + 15.0f, 75.0, 1.0, new Color(25, 25, 25, 255).getRGB());
        }
        Fonts.sectioNormal.drawStringWithShadow(StringUtils.capitalize((String)this.enumValue.getValue().toString().toLowerCase()), this.getPosX() + this.getWidth() - 82.0f, this.getPosY() + 4.0f, new Color(229, 229, 223, 255).getRGB());
        RenderUtil.drawArrow(this.getPosX() + this.getWidth() - 14.0f, this.getPosY() + 4.0f, this.isExtended(), new Color(229, 229, 223, 255).getRGB());
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        if (button == 0 && MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX() + this.getWidth() - 84.0f, this.getPosY() - 1.5f, 80.0, 15.0)) {
            this.setExtended(!this.isExtended());
        }
        if (button == 0 && this.isExtended()) {
            float enumY = this.getPosY() + 20.0f;
            for (Enum enoom : this.enumValue.getConstants()) {
                if (enoom == this.enumValue.getValue()) continue;
                if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX() + this.getWidth() - 80.0f, enumY - 4.0f, 72.0, 15.0)) {
                    this.enumValue.setValue(enoom);
                    this.setExtended(false);
                }
                enumY += 15.0f;
            }
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void onKeyTyped(char character, int keyCode) {
        super.onKeyTyped(character, keyCode);
    }

    public boolean isExtended() {
        return this.extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public EnumValue getEnumValue() {
        return this.enumValue;
    }
}

