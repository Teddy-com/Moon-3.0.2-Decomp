/*
 * Decompiled with CFR 0.152.
 */
package me.moon.gui.tab.component.impl.value;

import me.moon.Moon;
import me.moon.gui.tab.component.Component;
import me.moon.gui.tab.component.impl.ModuleComponent;
import me.moon.module.impl.visuals.HUD;
import me.moon.utils.font.FontUtil;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.value.impl.EnumValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class EnumComponent
extends Component {
    private EnumValue enumValue;
    private ModuleComponent parentComponent;
    private AnimationUtil animationUtil = new AnimationUtil(0.0, 0.0);

    public EnumComponent(ModuleComponent parentComponent, EnumValue enumValue, float posX, float posY, float width, float height) {
        super(enumValue.getLabel(), posX, posY, width, height);
        this.enumValue = enumValue;
        this.parentComponent = parentComponent;
        this.animationUtil.setPosX(posX - 18.0f);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        HUD hud = (HUD)Moon.INSTANCE.getModuleManager().getModule("hud");
        this.animationUtil.interpolate(this.getPosX() + (float)(this.parentComponent.getSelectedValue() == this.getEnumValue() ? 5 : 2) - 20.0f, 0.0, 14.0f / (float)Minecraft.getDebugFPS());
        FontUtil.drawShadowedString(this.getLabel() + ": " + this.getEnumValue().getValue().toString(), (float)this.animationUtil.getPosX(), this.getPosY() + 2.5f, -1);
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

