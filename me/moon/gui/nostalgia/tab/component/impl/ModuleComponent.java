/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.moon.gui.nostalgia.tab.component.impl;

import java.util.ArrayList;
import java.util.stream.Collectors;
import me.moon.gui.nostalgia.tab.component.Component;
import me.moon.gui.nostalgia.tab.component.impl.CategoryComponent;
import me.moon.gui.nostalgia.tab.component.impl.value.BooleanComponent;
import me.moon.gui.nostalgia.tab.component.impl.value.ColorComponent;
import me.moon.gui.nostalgia.tab.component.impl.value.EnumComponent;
import me.moon.gui.nostalgia.tab.component.impl.value.NumberComponent;
import me.moon.gui.nostalgia.tab.component.impl.value.RangedNumberComponent;
import me.moon.module.Module;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.ColorValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.FontValue;
import me.moon.utils.value.impl.NumberValue;
import me.moon.utils.value.impl.RangedValue;
import me.moon.utils.value.impl.StringValue;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

public class ModuleComponent
extends Component {
    private Module module;
    private CategoryComponent parentComponent;
    private ArrayList<Component> components = new ArrayList();
    private Value selectedValue;

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
                this.components.add(new BooleanComponent(this, (BooleanValue)value, this.getPosX() + this.getWidth() + 2.0f, y, this.module.getLongestValueInModule() + 15.0f, this.mc.fontRendererObj.FONT_HEIGHT + 2));
            } else if (value instanceof NumberValue) {
                this.components.add(new NumberComponent(this, (NumberValue)value, this.getPosX() + this.getWidth() + 2.0f, y, this.module.getLongestValueInModule() + 15.0f, this.mc.fontRendererObj.FONT_HEIGHT + 2));
            } else if (value instanceof EnumValue) {
                this.components.add(new EnumComponent(this, (EnumValue)value, this.getPosX() + this.getWidth() + 2.0f, y, this.module.getLongestValueInModule() + 15.0f, this.mc.fontRendererObj.FONT_HEIGHT + 2));
            } else if (value instanceof ColorValue) {
                this.components.add(new ColorComponent(this, (ColorValue)value, this.getPosX() + this.getWidth() + 2.0f, y, this.module.getLongestValueInModule() + 15.0f, this.mc.fontRendererObj.FONT_HEIGHT + 2));
            } else if (value instanceof RangedValue) {
                this.components.add(new RangedNumberComponent(this, (RangedValue)value, this.getPosX() + this.getWidth() + 2.0f, y, this.module.getLongestValueInModule() + 15.0f, this.mc.fontRendererObj.FONT_HEIGHT + 2));
            }
            if (value instanceof FontValue || value instanceof StringValue) continue;
            y += (float)(this.mc.fontRendererObj.FONT_HEIGHT + 2);
        }
        this.components.forEach(Component::init);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        for (Component component2 : this.components) {
            component2.setPosX(this.getPosX() + this.getWidth() + 2.0f);
            component2.setWidth(this.module.getLongestValueInModule() + 15.0f);
        }
        this.mc.fontRendererObj.drawStringWithShadow(this.getLabel(), this.getPosX() + 2.0f + (float)(this.parentComponent.getSelectedModule() == this.getModule() ? 8 : 0), this.getPosY() + 2.5f, this.module.isEnabled() ? -1 : -8355712);
        if (this.parentComponent.getMainTab().isExtendedValue() && this.parentComponent.getSelectedModule() == this.getModule()) {
            Keyboard.enableRepeatEvents((boolean)true);
            RenderUtil.drawBorderedRect(this.getPosX() + this.getWidth() + 2.0f, this.getPosY(), this.module.getLongestValueInModule() + 15.0f, (this.mc.fontRendererObj.FONT_HEIGHT + 2) * (int)this.module.getValues().stream().filter(value -> !(value instanceof FontValue) && !(value instanceof StringValue) && (value.getParentValueObject() == null || value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))).count() + 2, 0.5, -16777216, 0x60000000);
            this.components.stream().filter(component -> !component.isHidden()).forEach(component -> component.onDraw(scaledResolution));
            this.resetPositions();
        }
        if (this.parentComponent.getSelectedModule() == this.getModule()) {
            RenderUtil.drawCircle(this.getPosX() + 2.0f, this.getPosY() + 4.0f, 5.0f, RenderUtil.getRainbow(4500, -30, 1.0f));
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
        float y = this.getPosY();
        for (Component subComponent : this.components) {
            if (subComponent instanceof BooleanComponent) {
                BooleanComponent booleanComponent = (BooleanComponent)subComponent;
                if (booleanComponent.getBooleanValue().getParentValueObject() != null && !booleanComponent.getBooleanValue().getParentValueObject().getValueAsString().equalsIgnoreCase(booleanComponent.getBooleanValue().getParentValue())) {
                    booleanComponent.setHidden(true);
                    continue;
                }
                booleanComponent.setHidden(false);
                booleanComponent.setPosY(y);
                y += (float)(this.mc.fontRendererObj.FONT_HEIGHT + 2);
            }
            if (subComponent instanceof NumberComponent) {
                NumberComponent numberComponent = (NumberComponent)subComponent;
                if (numberComponent.getNumberValue().getParentValueObject() != null && !numberComponent.getNumberValue().getParentValueObject().getValueAsString().equalsIgnoreCase(numberComponent.getNumberValue().getParentValue())) {
                    numberComponent.setHidden(true);
                    continue;
                }
                numberComponent.setHidden(false);
                numberComponent.setPosY(y);
                y += (float)(this.mc.fontRendererObj.FONT_HEIGHT + 2);
            }
            if (subComponent instanceof ColorComponent) {
                ColorComponent colorComponent = (ColorComponent)subComponent;
                if (colorComponent.getColorValue().getParentValueObject() != null && !colorComponent.getColorValue().getParentValueObject().getValueAsString().equalsIgnoreCase(colorComponent.getColorValue().getParentValue())) {
                    colorComponent.setHidden(true);
                    continue;
                }
                colorComponent.setHidden(false);
                colorComponent.setPosY(y);
                y += (float)(this.mc.fontRendererObj.FONT_HEIGHT + 2);
            }
            if (subComponent instanceof RangedNumberComponent) {
                RangedNumberComponent rangedNumberComponent = (RangedNumberComponent)subComponent;
                if (rangedNumberComponent.getRangedValue().getParentValueObject() != null && !rangedNumberComponent.getRangedValue().getParentValueObject().getValueAsString().equalsIgnoreCase(rangedNumberComponent.getRangedValue().getParentValue())) {
                    rangedNumberComponent.setHidden(true);
                    continue;
                }
                rangedNumberComponent.setHidden(false);
                rangedNumberComponent.setPosY(y);
                y += (float)(this.mc.fontRendererObj.FONT_HEIGHT + 2);
            }
            if (!(subComponent instanceof EnumComponent)) continue;
            EnumComponent enumComponent = (EnumComponent)subComponent;
            if (enumComponent.getEnumValue().getParentValueObject() != null && !enumComponent.getEnumValue().getParentValueObject().getValueAsString().equalsIgnoreCase(enumComponent.getEnumValue().getParentValue())) {
                enumComponent.setHidden(true);
                continue;
            }
            enumComponent.setHidden(false);
            enumComponent.setPosY(y);
            y += (float)(this.mc.fontRendererObj.FONT_HEIGHT + 2);
        }
    }

    @Override
    public void setPosY(float posY) {
        super.setPosY(posY);
        float y = this.getPosY();
        for (Component component : this.components) {
            component.setPosY(y);
            y += (float)(this.mc.fontRendererObj.FONT_HEIGHT + 2);
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

