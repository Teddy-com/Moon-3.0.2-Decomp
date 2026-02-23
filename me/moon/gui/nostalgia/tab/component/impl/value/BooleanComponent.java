/*
 * Decompiled with CFR 0.152.
 */
package me.moon.gui.nostalgia.tab.component.impl.value;

import me.moon.Moon;
import me.moon.gui.nostalgia.tab.component.Component;
import me.moon.gui.nostalgia.tab.component.impl.ModuleComponent;
import me.moon.module.impl.visuals.HUD;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.BooleanValue;
import net.minecraft.client.gui.ScaledResolution;

public class BooleanComponent
extends Component {
    private BooleanValue booleanValue;
    private ModuleComponent parentComponent;

    public BooleanComponent(ModuleComponent parentComponent, BooleanValue booleanValue, float posX, float posY, float width, float height) {
        super(booleanValue.getLabel(), posX, posY, width, height);
        this.booleanValue = booleanValue;
        this.parentComponent = parentComponent;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        HUD hud = (HUD)Moon.INSTANCE.getModuleManager().getModule("hud");
        this.mc.fontRendererObj.drawStringWithShadow(this.getLabel() + ": " + this.getBooleanValue().getValue(), this.getPosX() + 2.0f + (float)(this.parentComponent.getSelectedValue() == this.getBooleanValue() ? 8 : 0), this.getPosY() + 2.5f, this.parentComponent.getSelectedValue() == this.getBooleanValue() ? -1 : -8355712);
        if (this.parentComponent.getSelectedValue() == this.getBooleanValue()) {
            RenderUtil.drawCircle(this.getPosX() + 2.0f, this.getPosY() + 4.0f, 5.0f, RenderUtil.getRainbow(4500, -30, 1.0f));
        }
    }

    @Override
    public void onKeyPress(int key) {
        super.onKeyPress(key);
        if (this.parentComponent.getSelectedValue() != this.booleanValue) {
            return;
        }
        if (key == 28) {
            this.booleanValue.setEnabled(!this.booleanValue.isEnabled());
        }
    }

    public BooleanValue getBooleanValue() {
        return this.booleanValue;
    }
}

