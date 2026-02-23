/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.moon.gui.tab.component.impl.value;

import me.moon.Moon;
import me.moon.gui.tab.component.Component;
import me.moon.gui.tab.component.impl.ModuleComponent;
import me.moon.module.impl.visuals.HUD;
import me.moon.utils.font.FontUtil;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.value.impl.RangedValue;
import me.moon.utils.value.parse.NumberHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

public class RangedNumberComponent
extends Component {
    private RangedValue rangedValue;
    private ModuleComponent parentComponent;
    private AnimationUtil animationUtil = new AnimationUtil(0.0, 0.0);

    public RangedNumberComponent(ModuleComponent parentComponent, RangedValue rangedValue, float posX, float posY, float width, float height) {
        super(rangedValue.getLabel(), posX, posY, width, height);
        this.rangedValue = rangedValue;
        this.parentComponent = parentComponent;
        this.animationUtil.setPosX(posX - 18.0f);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        HUD hud = (HUD)Moon.INSTANCE.getModuleManager().getModule("hud");
        this.animationUtil.interpolate(this.getPosX() + (float)(this.parentComponent.getSelectedValue() == this.getRangedValue() ? 5 : 2) - 20.0f, 0.0, 14.0f / (float)Minecraft.getDebugFPS());
        FontUtil.drawShadowedString(this.getLabel() + ": " + this.getRangedValue().getLeftValue() + " - " + this.getRangedValue().getRightValue(), (float)this.animationUtil.getPosX(), this.getPosY() + 2.5f, -1);
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

