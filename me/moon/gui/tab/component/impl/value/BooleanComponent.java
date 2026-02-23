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
import me.moon.utils.value.impl.BooleanValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class BooleanComponent
extends Component {
    private BooleanValue booleanValue;
    private ModuleComponent parentComponent;
    private AnimationUtil animationUtil = new AnimationUtil(0.0, 0.0);

    public BooleanComponent(ModuleComponent parentComponent, BooleanValue booleanValue, float posX, float posY, float width, float height) {
        super(booleanValue.getLabel(), posX, posY, width, height);
        this.booleanValue = booleanValue;
        this.parentComponent = parentComponent;
        this.animationUtil.setPosX(posX - 18.0f);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        HUD hud = (HUD)Moon.INSTANCE.getModuleManager().getModule("hud");
        this.animationUtil.interpolate(this.getPosX() + (float)(this.parentComponent.getSelectedValue() == this.getBooleanValue() ? 5 : 2) - 20.0f, 0.0, 14.0f / (float)Minecraft.getDebugFPS());
        FontUtil.drawShadowedString(this.getLabel() + ": " + this.getBooleanValue().getValue(), (float)this.animationUtil.getPosX(), this.getPosY() + 2.5f, -1);
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

