/*
 * Decompiled with CFR 0.152.
 */
package me.moon.gui.nostalgia.tab.component.impl.value;

import me.moon.Moon;
import me.moon.gui.nostalgia.tab.component.Component;
import me.moon.gui.nostalgia.tab.component.impl.ModuleComponent;
import me.moon.module.impl.visuals.HUD;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.NumberValue;
import me.moon.utils.value.parse.NumberHelper;
import net.minecraft.client.gui.ScaledResolution;

public class NumberComponent
extends Component {
    private NumberValue numberValue;
    private ModuleComponent parentComponent;

    public NumberComponent(ModuleComponent parentComponent, NumberValue numberValue, float posX, float posY, float width, float height) {
        super(numberValue.getLabel(), posX, posY, width, height);
        this.numberValue = numberValue;
        this.parentComponent = parentComponent;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        HUD hud = (HUD)Moon.INSTANCE.getModuleManager().getModule("hud");
        this.mc.fontRendererObj.drawStringWithShadow(this.getLabel() + ": " + this.getNumberValue().getValue(), this.getPosX() + 2.0f + (float)(this.parentComponent.getSelectedValue() == this.getNumberValue() ? 8 : 0), this.getPosY() + 2.5f, this.parentComponent.getSelectedValue() == this.getNumberValue() ? -1 : -8355712);
        if (this.parentComponent.getSelectedValue() == this.getNumberValue()) {
            RenderUtil.drawCircle(this.getPosX() + 2.0f, this.getPosY() + 4.0f, 5.0f, RenderUtil.getRainbow(4500, -30, 1.0f));
        }
    }

    @Override
    public void onKeyPress(int key) {
        super.onKeyPress(key);
        if (this.parentComponent.getSelectedValue() != this.getNumberValue()) {
            return;
        }
        switch (key) {
            case 28: {
                this.parentComponent.getParentComponent().getMainTab().setExtendedValueDynamic(!this.parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic());
                break;
            }
            case 205: {
                if (!this.parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic() || this.parentComponent.getSelectedValue() != this.getNumberValue()) break;
                NumberHelper.increment(this.getNumberValue());
                break;
            }
            case 203: {
                if (!this.parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic() || this.parentComponent.getSelectedValue() != this.getNumberValue()) break;
                NumberHelper.decrecement(this.getNumberValue());
                break;
            }
        }
    }

    public NumberValue getNumberValue() {
        return this.numberValue;
    }
}

