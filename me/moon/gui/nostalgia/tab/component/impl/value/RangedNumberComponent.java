/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.moon.gui.nostalgia.tab.component.impl.value;

import me.moon.Moon;
import me.moon.gui.nostalgia.tab.component.Component;
import me.moon.gui.nostalgia.tab.component.impl.ModuleComponent;
import me.moon.module.impl.visuals.HUD;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.RangedValue;
import me.moon.utils.value.parse.NumberHelper;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

public class RangedNumberComponent
extends Component {
    private RangedValue rangedValue;
    private ModuleComponent parentComponent;

    public RangedNumberComponent(ModuleComponent parentComponent, RangedValue rangedValue, float posX, float posY, float width, float height) {
        super(rangedValue.getLabel(), posX, posY, width, height);
        this.rangedValue = rangedValue;
        this.parentComponent = parentComponent;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        HUD hud = (HUD)Moon.INSTANCE.getModuleManager().getModule("hud");
        this.mc.fontRendererObj.drawStringWithShadow(this.getLabel() + ": " + this.getRangedValue().getLeftValue() + " - " + this.getRangedValue().getRightValue(), this.getPosX() + 2.0f + (float)(this.parentComponent.getSelectedValue() == this.getRangedValue() ? 8 : 0), this.getPosY() + 2.5f, this.parentComponent.getSelectedValue() == this.getRangedValue() ? -1 : -8355712);
        if (this.parentComponent.getSelectedValue() == this.getRangedValue()) {
            RenderUtil.drawCircle(this.getPosX() + 2.0f, this.getPosY() + 4.0f, 5.0f, RenderUtil.getRainbow(4500, -30, 1.0f));
        }
    }

    @Override
    public void onKeyPress(int key) {
        super.onKeyPress(key);
        if (this.parentComponent.getSelectedValue() != this.getRangedValue()) {
            return;
        }
        switch (key) {
            case 28: {
                this.parentComponent.getParentComponent().getMainTab().setExtendedValueDynamic(!this.parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic());
                break;
            }
            case 205: {
                if (!this.parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic() || this.parentComponent.getSelectedValue() != this.getRangedValue()) break;
                NumberHelper.increcementRanged(this.getRangedValue(), Keyboard.isKeyDown((int)42));
                break;
            }
            case 203: {
                if (!this.parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic() || this.parentComponent.getSelectedValue() != this.getRangedValue()) break;
                NumberHelper.decrecementRanged(this.getRangedValue(), Keyboard.isKeyDown((int)42));
                break;
            }
        }
    }

    public RangedValue getRangedValue() {
        return this.rangedValue;
    }
}

