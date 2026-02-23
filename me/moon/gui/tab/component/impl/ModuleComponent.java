/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.moon.gui.tab.component.impl;

import java.util.ArrayList;
import java.util.stream.Collectors;
import me.moon.Moon;
import me.moon.gui.tab.component.Component;
import me.moon.gui.tab.component.impl.CategoryComponent;
import me.moon.gui.tab.component.impl.value.BooleanComponent;
import me.moon.gui.tab.component.impl.value.ColorComponent;
import me.moon.gui.tab.component.impl.value.EnumComponent;
import me.moon.gui.tab.component.impl.value.NumberComponent;
import me.moon.gui.tab.component.impl.value.RangedNumberComponent;
import me.moon.module.Module;
import me.moon.module.impl.visuals.HUD;
import me.moon.utils.font.FontUtil;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.ColorValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.FontValue;
import me.moon.utils.value.impl.NumberValue;
import me.moon.utils.value.impl.RangedValue;
import me.moon.utils.value.impl.StringValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

public class ModuleComponent
extends Component {
    private Module module;
    private CategoryComponent parentComponent;
    private ArrayList<Component> components = new ArrayList();
    private Value selectedValue;
    private AnimationUtil animationUtil = new AnimationUtil(0.0, 0.0);
    float lolX;
    float lolY;
    float lolWidth;
    float lolHeight;

    public ModuleComponent(CategoryComponent parentComponent, Module module, float posX, float posY, float width, float height) {
        super(module.getLabel(), posX, posY, width, height);
        this.module = module;
        this.parentComponent = parentComponent;
        this.selectedValue = module.getValues().isEmpty() ? null : module.getValues().get(0);
    }

    @Override
    public void init() {
        float y = this.getPosY();
        for (Value value : this.module.getValues()) {
            if (value instanceof BooleanValue) {
                this.components.add(new BooleanComponent(this, (BooleanValue)value, this.getPosX() + this.getWidth() + 2.0f, y, this.module.getLongestValueInModule() + 15.0f, FontUtil.getStringHeight("COMBAT") + 2.0f));
            } else if (value instanceof NumberValue) {
                this.components.add(new NumberComponent(this, (NumberValue)value, this.getPosX() + this.getWidth() + 2.0f, y, this.module.getLongestValueInModule() + 15.0f, FontUtil.getStringHeight("COMBAT") + 2.0f));
            } else if (value instanceof EnumValue) {
                this.components.add(new EnumComponent(this, (EnumValue)value, this.getPosX() + this.getWidth() + 2.0f, y, this.module.getLongestValueInModule() + 15.0f, FontUtil.getStringHeight("COMBAT") + 2.0f));
            } else if (value instanceof ColorValue) {
                this.components.add(new ColorComponent(this, (ColorValue)value, this.getPosX() + this.getWidth() + 2.0f, y, this.module.getLongestValueInModule() + 15.0f, FontUtil.getStringHeight("COMBAT") + 2.0f));
            } else if (value instanceof RangedValue) {
                this.components.add(new RangedNumberComponent(this, (RangedValue)value, this.getPosX() + this.getWidth() + 2.0f, y, this.module.getLongestValueInModule() + 15.0f, FontUtil.getStringHeight("COMBAT") + 2.0f));
            }
            if (value instanceof FontValue || value instanceof StringValue) continue;
            y += FontUtil.getStringHeight("COMBAT") + 2.0f;
        }
        this.components.forEach(Component::init);
        this.animationUtil.setPosX(this.getPosX() + (float)(this.parentComponent.getSelectedModule() == this.module ? 5 : 2) - 10.0f);
    }

    @Override
    public void blur() {
        if (this.parentComponent.getMainTab().isExtendedValue() && this.parentComponent.getSelectedModule() == this.getModule()) {
            RenderUtil.drawRect(this.lolX, this.lolY, this.lolWidth, this.lolHeight, -1155456735);
        }
        super.blur();
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        for (Component component2 : this.components) {
            component2.setPosX(this.getPosX() + this.getWidth() + 2.0f);
            component2.setWidth(this.module.getLongestValueInModule() + 15.0f);
        }
        for (Component subComponent : this.components) {
            if (subComponent instanceof BooleanComponent && this.selectedValue == ((BooleanComponent)subComponent).getBooleanValue()) {
                this.animationUtil.interpolate(this.getPosX() + (float)(this.parentComponent.getSelectedModule() == this.module ? 5 : 2) - 10.0f, subComponent.getPosY(), 14.0f / (float)Minecraft.getDebugFPS());
            }
            if (subComponent instanceof NumberComponent && this.selectedValue == ((NumberComponent)subComponent).getNumberValue()) {
                this.animationUtil.interpolate(this.getPosX() + (float)(this.parentComponent.getSelectedModule() == this.module ? 5 : 2) - 10.0f, subComponent.getPosY(), 14.0f / (float)Minecraft.getDebugFPS());
            }
            if (subComponent instanceof ColorComponent && this.selectedValue == ((ColorComponent)subComponent).getColorValue()) {
                this.animationUtil.interpolate(this.getPosX() + (float)(this.parentComponent.getSelectedModule() == this.module ? 5 : 2) - 10.0f, subComponent.getPosY() + 0.5f, 14.0f / (float)Minecraft.getDebugFPS());
            }
            if (subComponent instanceof RangedNumberComponent && this.selectedValue == ((RangedNumberComponent)subComponent).getRangedValue()) {
                this.animationUtil.interpolate(this.getPosX() + (float)(this.parentComponent.getSelectedModule() == this.module ? 5 : 2) - 10.0f, subComponent.getPosY(), 14.0f / (float)Minecraft.getDebugFPS());
            }
            if (!(subComponent instanceof EnumComponent) || this.selectedValue != ((EnumComponent)subComponent).getEnumValue()) continue;
            this.animationUtil.interpolate(this.getPosX() + (float)(this.parentComponent.getSelectedModule() == this.module ? 5 : 2) - 10.0f, subComponent.getPosY(), 14.0f / (float)Minecraft.getDebugFPS());
        }
        FontUtil.drawShadowedString(this.getLabel(), (float)this.animationUtil.getPosX(), this.getPosY() + (FontUtil.getHeight() + 3.0f) / 2.0f - FontUtil.getHeight() / 2.0f + 1.5f, this.module.isEnabled() ? -1 : -8355712);
        if (this.parentComponent.getMainTab().isExtendedValue() && this.parentComponent.getSelectedModule() == this.getModule()) {
            Keyboard.enableRepeatEvents((boolean)true);
            this.lolX = this.getPosX() + Moon.INSTANCE.getModuleManager().getLongestModInCategory(this.module.getCategory()) + 12.0f - 8.0f;
            this.lolY = this.getParentComponent().getPosY();
            this.lolWidth = this.module.getLongestValueInModule() + 15.0f;
            this.lolHeight = (FontUtil.getHeight() + 3.0f) * (float)((int)this.module.getValues().stream().filter(value -> !(value instanceof FontValue) && !(value instanceof StringValue) && (value.getParentValueObject() == null || value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))).count());
            RenderUtil.drawRect(this.getPosX() + Moon.INSTANCE.getModuleManager().getLongestModInCategory(this.module.getCategory()) + 12.0f - 8.0f, this.getParentComponent().getPosY(), this.module.getLongestValueInModule() + 15.0f, (FontUtil.getHeight() + 3.0f) * (float)((int)this.module.getValues().stream().filter(value -> !(value instanceof FontValue) && !(value instanceof StringValue) && (value.getParentValueObject() == null || value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))).count()), -1155456735);
            RenderUtil.drawRect(this.getPosX() + Moon.INSTANCE.getModuleManager().getLongestModInCategory(this.module.getCategory()) + 12.0f - 8.0f, this.animationUtil.getPosY(), this.module.getLongestValueInModule() + 15.0f, FontUtil.getHeight() + 3.0f, HUD.getColorHUD());
            this.components.forEach(component -> component.setPosX(this.getPosX() + Moon.INSTANCE.getModuleManager().getLongestModInCategory(this.module.getCategory()) + 24.0f));
            this.components.stream().filter(component -> !component.isHidden()).forEach(component -> component.onDraw(scaledResolution));
            this.resetPositions();
        } else {
            this.lolX = -1000.0f;
            this.lolY = -1000.0f;
            this.lolWidth = 0.0f;
            this.lolHeight = 0.0f;
        }
    }

    @Override
    public void onKeyPress(int key) {
        if (this.parentComponent.getMainTab().isExtendedValue() && this.parentComponent.getSelectedModule() == this.getModule()) {
            this.components.stream().filter(component -> !component.isHidden()).forEach(component -> component.onKeyPress(key));
            switch (key) {
                case 208: {
                    if (!this.getParentComponent().getMainTab().isExtendedValue() || this.getParentComponent().getMainTab().isExtendedValueDynamic()) break;
                    this.setSelectedValue((Value)this.module.getValues().stream().filter(value -> !(value instanceof FontValue) && !(value instanceof StringValue) && (value.getParentValueObject() == null || value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))).collect(Collectors.toList()).get((this.module.getValues().stream().filter(value -> !(value instanceof FontValue) && !(value instanceof StringValue) && (value.getParentValueObject() == null || value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))).collect(Collectors.toList()).indexOf(this.getSelectedValue()) + 1) % (int)this.module.getValues().stream().filter(value -> !(value instanceof FontValue) && !(value instanceof StringValue) && (value.getParentValueObject() == null || value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))).count()));
                    break;
                }
                case 200: {
                    if (!this.getParentComponent().getMainTab().isExtendedValue() || this.getParentComponent().getMainTab().isExtendedValueDynamic()) break;
                    this.setSelectedValue((Value)this.module.getValues().stream().filter(value -> !(value instanceof FontValue) && !(value instanceof StringValue) && (value.getParentValueObject() == null || value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))).collect(Collectors.toList()).get(this.module.getValues().stream().filter(value -> !(value instanceof FontValue) && !(value instanceof StringValue) && (value.getParentValueObject() == null || value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))).collect(Collectors.toList()).indexOf(this.getSelectedValue()) - 1 < 0 ? (int)this.module.getValues().stream().filter(value -> !(value instanceof FontValue) && !(value instanceof StringValue) && (value.getParentValueObject() == null || value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))).count() - 1 : this.module.getValues().stream().filter(value -> !(value instanceof FontValue) && !(value instanceof StringValue) && (value.getParentValueObject() == null || value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))).collect(Collectors.toList()).indexOf(this.getSelectedValue()) - 1));
                    break;
                }
            }
        }
    }

    private void resetPositions() {
        float y = this.getParentComponent().getPosY();
        for (Component subComponent : this.components) {
            if (subComponent instanceof BooleanComponent) {
                BooleanComponent booleanComponent = (BooleanComponent)subComponent;
                if (booleanComponent.getBooleanValue().getParentValueObject() != null && !booleanComponent.getBooleanValue().getParentValueObject().getValueAsString().equalsIgnoreCase(booleanComponent.getBooleanValue().getParentValue())) {
                    booleanComponent.setHidden(true);
                    continue;
                }
                booleanComponent.setHidden(false);
                booleanComponent.setPosY(y);
                booleanComponent.setHeight(FontUtil.getHeight() + 3.0f);
                y += FontUtil.getHeight() + 3.0f;
            }
            if (subComponent instanceof NumberComponent) {
                NumberComponent numberComponent = (NumberComponent)subComponent;
                if (numberComponent.getNumberValue().getParentValueObject() != null && !numberComponent.getNumberValue().getParentValueObject().getValueAsString().equalsIgnoreCase(numberComponent.getNumberValue().getParentValue())) {
                    numberComponent.setHidden(true);
                    continue;
                }
                numberComponent.setHidden(false);
                numberComponent.setPosY(y);
                numberComponent.setHeight(FontUtil.getHeight() + 3.0f);
                y += FontUtil.getHeight() + 3.0f;
            }
            if (subComponent instanceof ColorComponent) {
                ColorComponent colorComponent = (ColorComponent)subComponent;
                if (colorComponent.getColorValue().getParentValueObject() != null && !colorComponent.getColorValue().getParentValueObject().getValueAsString().equalsIgnoreCase(colorComponent.getColorValue().getParentValue())) {
                    colorComponent.setHidden(true);
                    continue;
                }
                colorComponent.setHidden(false);
                colorComponent.setPosY(y);
                colorComponent.setHeight(FontUtil.getHeight() + 3.0f);
                y += FontUtil.getHeight() + 3.0f;
            }
            if (subComponent instanceof RangedNumberComponent) {
                RangedNumberComponent rangedNumberComponent = (RangedNumberComponent)subComponent;
                if (rangedNumberComponent.getRangedValue().getParentValueObject() != null && !rangedNumberComponent.getRangedValue().getParentValueObject().getValueAsString().equalsIgnoreCase(rangedNumberComponent.getRangedValue().getParentValue())) {
                    rangedNumberComponent.setHidden(true);
                    continue;
                }
                rangedNumberComponent.setHidden(false);
                rangedNumberComponent.setPosY(y);
                rangedNumberComponent.setHeight(FontUtil.getHeight() + 3.0f);
                y += FontUtil.getHeight() + 3.0f;
            }
            if (!(subComponent instanceof EnumComponent)) continue;
            EnumComponent enumComponent = (EnumComponent)subComponent;
            if (enumComponent.getEnumValue().getParentValueObject() != null && !enumComponent.getEnumValue().getParentValueObject().getValueAsString().equalsIgnoreCase(enumComponent.getEnumValue().getParentValue())) {
                enumComponent.setHidden(true);
                continue;
            }
            enumComponent.setHidden(false);
            enumComponent.setPosY(y);
            enumComponent.setHeight(FontUtil.getHeight() + 3.0f);
            y += FontUtil.getHeight() + 3.0f;
        }
    }

    @Override
    public void setPosY(float posY) {
        super.setPosY(posY);
        float y = this.getParentComponent().getPosY();
        for (Component component : this.components) {
            if (component.isHidden()) continue;
            component.setPosY(y);
            y += FontUtil.getHeight() + 3.0f;
        }
    }

    public Module getModule() {
        return this.module;
    }

    public CategoryComponent getParentComponent() {
        return this.parentComponent;
    }

    public Value getSelectedValue() {
        return this.selectedValue;
    }

    public void setSelectedValue(Value selectedValue) {
        this.selectedValue = selectedValue;
    }
}

