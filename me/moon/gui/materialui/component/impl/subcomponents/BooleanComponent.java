/*
 * Decompiled with CFR 0.152.
 */
package me.moon.gui.materialui.component.impl.subcomponents;

import java.awt.Color;
import me.moon.gui.materialui.component.Component;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.BooleanValue;

public class BooleanComponent
extends Component {
    private BooleanValue booleanValue;
    private AnimationUtil util = new AnimationUtil(0.0, 0.0);

    public BooleanComponent(BooleanValue booleanValue, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(booleanValue.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.booleanValue = booleanValue;
    }

    @Override
    public void componentMoved(float movedX, float movedY) {
        super.componentMoved(movedX, movedY);
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
        super.onDrawScreen(mouseX, mouseY, partialTicks);
        Fonts.sectioNormal.drawStringWithShadow(this.getLabel(), this.getPosX(), this.getPosY() + 3.0f, new Color(229, 229, 223, 255).getRGB());
        RenderUtil.drawRect(this.getPosX() + this.getWidth() - 15.0f, this.getPosY(), 10.0, 10.0, this.booleanValue.isEnabled() ? -15305263 : new Color(25, 25, 25, 255).getRGB());
        if (this.booleanValue.isEnabled()) {
            RenderUtil.drawCheckMarkSectio(this.getPosX() + this.getWidth() - 14.0f, this.getPosY() - 2.0f, 14, -1);
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        if (button == 0 && MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX() + this.getWidth() - 15.0f, this.getPosY(), 10.0, 10.0)) {
            this.booleanValue.setEnabled(!this.booleanValue.isEnabled());
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

    public BooleanValue getBooleanValue() {
        return this.booleanValue;
    }
}

