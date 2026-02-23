/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package me.moon.gui.nostalgia.tab.component.impl.value;

import me.moon.Moon;
import me.moon.gui.nostalgia.tab.component.Component;
import me.moon.gui.nostalgia.tab.component.impl.ModuleComponent;
import me.moon.module.impl.visuals.HUD;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.EnumValue;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringUtils;

public class EnumComponent
extends Component {
    private EnumValue enumValue;
    private ModuleComponent parentComponent;

    public EnumComponent(ModuleComponent parentComponent, EnumValue enumValue, float posX, float posY, float width, float height) {
        super(enumValue.getLabel(), posX, posY, width, height);
        this.enumValue = enumValue;
        this.parentComponent = parentComponent;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        HUD hud = (HUD)Moon.INSTANCE.getModuleManager().getModule("hud");
        this.mc.fontRendererObj.drawStringWithShadow(this.getLabel() + ": " + StringUtils.capitalize((String)this.getEnumValue().getValue().toString().toLowerCase()), this.getPosX() + 2.0f + (float)(this.parentComponent.getSelectedValue() == this.getEnumValue() ? 8 : 0), this.getPosY() + 2.5f, this.parentComponent.getSelectedValue() == this.getEnumValue() ? -1 : -8355712);
        if (this.parentComponent.getSelectedValue() == this.getEnumValue()) {
            RenderUtil.drawCircle(this.getPosX() + 2.0f, this.getPosY() + 4.0f, 5.0f, RenderUtil.getRainbow(4500, -30, 1.0f));
        }
    }

    @Override
    public void onKeyPress(int key) {
        super.onKeyPress(key);
        if (this.parentComponent.getSelectedValue() != this.getEnumValue()) {
            return;
        }
        switch (key) {
            case 28: {
                this.parentComponent.getParentComponent().getMainTab().setExtendedValueDynamic(!this.parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic());
                break;
            }
            case 205: {
                if (!this.parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic() || this.parentComponent.getSelectedValue() != this.getEnumValue()) break;
                this.getEnumValue().increment();
                break;
            }
            case 203: {
                if (!this.parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic() || this.parentComponent.getSelectedValue() != this.getEnumValue()) break;
                this.getEnumValue().decrement();
                break;
            }
        }
    }

    public EnumValue getEnumValue() {
        return this.enumValue;
    }
}

