/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package me.moon.gui.materialui.component.impl.subcomponents;

import java.awt.Color;
import java.util.ArrayList;
import me.moon.gui.materialui.component.Component;
import me.moon.gui.materialui.component.impl.CategoryComponent;
import me.moon.gui.materialui.component.impl.subcomponents.BooleanComponent;
import me.moon.gui.materialui.component.impl.subcomponents.ColorComponent;
import me.moon.gui.materialui.component.impl.subcomponents.EnumComponent;
import me.moon.gui.materialui.component.impl.subcomponents.NumberComponent;
import me.moon.gui.materialui.component.impl.subcomponents.RangedNumberComponent;
import me.moon.module.Module;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.ColorValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.NumberValue;
import me.moon.utils.value.impl.RangedValue;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class ModuleComponent
extends Component {
    private Module module;
    private ArrayList<Component> components = new ArrayList();
    private boolean extended;
    private CategoryComponent categoryComponent;
    private int scrollY;

    public ModuleComponent(CategoryComponent categoryComponent, Module module, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(module.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.module = module;
        this.categoryComponent = categoryComponent;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        if (!this.module.getValues().isEmpty()) {
            float valueOffsetY = 0.0f;
            for (Value value : this.module.getValues()) {
                if (value instanceof BooleanValue) {
                    this.components.add(new BooleanComponent((BooleanValue)value, this.getCategoryComponent().getPosX(), this.getCategoryComponent().getPosY() - 20.0f, 110.0f, valueOffsetY, 165.0f, 20.0f));
                    valueOffsetY += 20.0f;
                }
                if (value instanceof EnumValue) {
                    this.components.add(new EnumComponent((EnumValue)value, this.getCategoryComponent().getPosX(), this.getCategoryComponent().getPosY() - 20.0f, 110.0f, valueOffsetY, 165.0f, 20.0f));
                    valueOffsetY += 20.0f;
                }
                if (value instanceof NumberValue) {
                    this.components.add(new NumberComponent((NumberValue)value, this.getCategoryComponent().getPosX(), this.getCategoryComponent().getPosY() - 20.0f, 110.0f, valueOffsetY, 165.0f, 20.0f));
                    valueOffsetY += 20.0f;
                }
                if (value instanceof RangedValue) {
                    this.components.add(new RangedNumberComponent((RangedValue)value, this.getCategoryComponent().getPosX(), this.getCategoryComponent().getPosY() - 20.0f, 110.0f, valueOffsetY, 165.0f, 20.0f));
                    valueOffsetY += 20.0f;
                }
                if (!(value instanceof ColorValue)) continue;
                this.components.add(new ColorComponent((ColorValue)value, this.getCategoryComponent().getPosX(), this.getCategoryComponent().getPosY() - 20.0f, 110.0f, valueOffsetY, 165.0f, 100.0f));
                valueOffsetY += 100.0f;
            }
        }
        this.components.forEach(Component::initializeComponent);
    }

    @Override
    public void componentMoved(float movedX, float movedY) {
        super.componentMoved(movedX, movedY);
        if (this.isExtended()) {
            for (Component component : this.getComponents()) {
                component.componentMoved(this.getPosX(), this.getCategoryComponent().getPosY() - 20.0f);
            }
        }
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
        super.onDrawScreen(mouseX, mouseY, partialTicks);
        double scrollbarHeight = this.getCategoryComponent().getHeight() / (float)this.getComponentHeight() * this.getCategoryComponent().getHeight() + 78.0f;
        Fonts.sectioNormal.drawStringWithShadow(this.getLabel() + (Keyboard.isKeyDown((int)29) && this.module.getKeyBind() != 0 ? " [" + Keyboard.getKeyName((int)this.module.getKeyBind()) + "]" : ""), this.getPosX(), this.getPosY() - 1.0f, this.module.isEnabled() ? new Color(229, 229, 223, 255).getRGB() : new Color(167, 167, 161, 255).getRGB());
        if (!this.getComponents().isEmpty()) {
            Fonts.clickGuiFont.drawStringWithShadow("...", this.getPosX() + this.getWidth() - (float)Fonts.clickGuiFont.getStringWidth("..."), this.getPosY() - 2.0f, this.module.isEnabled() ? new Color(229, 229, 223, 255).getRGB() : new Color(167, 167, 161, 255).getRGB());
        }
        if (this.isExtended()) {
            if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getCategoryComponent().getPosX() + 155.0f, this.getCategoryComponent().getPosY(), 175.0, this.getCategoryComponent().getHeight()) && (float)this.getComponentHeight() >= this.getCategoryComponent().getHeight()) {
                int wheel = Mouse.getDWheel();
                if (wheel < 0) {
                    if (this.getScrollY() - 16 < -(this.getComponentHeight() - 251)) {
                        this.setScrollY(-(this.getComponentHeight() - 251));
                    } else {
                        this.setScrollY(this.getScrollY() - 16);
                    }
                } else if (wheel > 0) {
                    this.setScrollY(this.getScrollY() + 16);
                }
            }
            if (this.getScrollY() > 0) {
                this.setScrollY(0);
            }
            if ((float)this.getComponentHeight() >= this.getCategoryComponent().getHeight()) {
                if ((float)(this.getScrollY() - 6) < -((float)this.getComponentHeight() - this.getCategoryComponent().getHeight())) {
                    this.setScrollY((int)(-((float)this.getComponentHeight() - this.getCategoryComponent().getHeight())));
                }
            } else if (this.getScrollY() < 0) {
                this.setScrollY(0);
            }
            for (Component component : this.getComponents()) {
                if (component.isHidden()) continue;
                component.onDrawScreen(mouseX, mouseY, partialTicks);
                component.setOffsetY(component.getOriginalOffsetY() + (float)this.getScrollY());
                component.componentMoved(this.getPosX(), this.getCategoryComponent().getPosY() - 20.0f);
            }
            RenderUtil.drawRect(this.getCategoryComponent().getPosX() + 345.0f, this.getCategoryComponent().getPosY() - 26.0f, 2.0, this.getCategoryComponent().getHeight() + 26.0f, new Color(55, 55, 55, 255).getRGB());
            RenderUtil.drawRect(this.getCategoryComponent().getPosX() + 345.0f, (double)(this.getCategoryComponent().getPosY() - 26.0f) - ((double)(this.getCategoryComponent().getHeight() + 26.0f) - (scrollbarHeight - 4.0)) / (double)((float)this.getComponentHeight() - (this.getCategoryComponent().getHeight() + 26.0f)) * (double)this.getScrollY(), 2.0, scrollbarHeight, new Color(40, 40, 40, 255).getRGB());
            this.setupHeight();
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        if (button == 0 && MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY() - 4.0f, this.getWidth(), this.getHeight() - 8.0f)) {
            this.getModule().setEnabled(!this.getModule().isEnabled());
        }
        if (button == 1 && !this.getComponents().isEmpty() && MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY() - 4.0f, this.getWidth(), this.getHeight() - 8.0f)) {
            this.getCategoryComponent().getComponents().stream().filter(component -> component instanceof ModuleComponent && component != this).forEach(component -> ((ModuleComponent)component).setExtended(false));
            this.setExtended(!this.isExtended());
        }
        if (this.isExtended()) {
            for (Component component2 : this.getComponents()) {
                if (component2.isHidden()) continue;
                component2.onMouseClicked(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
        if (this.isExtended()) {
            for (Component component : this.getComponents()) {
                if (component.isHidden()) continue;
                component.onMouseReleased(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void onKeyTyped(char character, int keyCode) {
        super.onKeyTyped(character, keyCode);
        if (this.isExtended()) {
            for (Component component : this.getComponents()) {
                if (component.isHidden()) continue;
                component.onKeyTyped(character, keyCode);
            }
        }
    }

    public Module getModule() {
        return this.module;
    }

    public ArrayList<Component> getComponents() {
        return this.components;
    }

    public boolean isExtended() {
        return this.extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public CategoryComponent getCategoryComponent() {
        return this.categoryComponent;
    }

    public int getComponentHeight() {
        int h = 0;
        for (Component component : this.getComponents()) {
            if (component.isHidden()) continue;
            h = (int)((float)h + component.getHeight());
        }
        return h;
    }

    public void setupHeight() {
        int h = this.getScrollY();
        for (Component component : this.getComponents()) {
            if (component instanceof BooleanComponent) {
                BooleanComponent booleanComponent = (BooleanComponent)component;
                if (booleanComponent.getBooleanValue().getParentValueObject() != null && !booleanComponent.getBooleanValue().getParentValueObject().getValueAsString().equalsIgnoreCase(booleanComponent.getBooleanValue().getParentValue())) {
                    booleanComponent.setHidden(true);
                    continue;
                }
                booleanComponent.setHidden(false);
                component.setOffsetY(h);
                h = (int)((float)h + component.getHeight());
            }
            if (component instanceof NumberComponent) {
                NumberComponent numberComponent = (NumberComponent)component;
                if (numberComponent.getNumberValue().getParentValueObject() != null && !numberComponent.getNumberValue().getParentValueObject().getValueAsString().equalsIgnoreCase(numberComponent.getNumberValue().getParentValue())) {
                    numberComponent.setHidden(true);
                    continue;
                }
                numberComponent.setHidden(false);
                component.setOffsetY(h);
                h = (int)((float)h + component.getHeight());
            }
            if (component instanceof ColorComponent) {
                ColorComponent colorComponent = (ColorComponent)component;
                if (colorComponent.getColorValue().getParentValueObject() != null && !colorComponent.getColorValue().getParentValueObject().getValueAsString().equalsIgnoreCase(colorComponent.getColorValue().getParentValue())) {
                    colorComponent.setHidden(true);
                    continue;
                }
                colorComponent.setHidden(false);
                component.setOffsetY(h);
                h = (int)((float)h + component.getHeight());
            }
            if (component instanceof RangedNumberComponent) {
                RangedNumberComponent rangedNumberComponent = (RangedNumberComponent)component;
                if (rangedNumberComponent.getRangedValue().getParentValueObject() != null && !rangedNumberComponent.getRangedValue().getParentValueObject().getValueAsString().equalsIgnoreCase(rangedNumberComponent.getRangedValue().getParentValue())) {
                    rangedNumberComponent.setHidden(true);
                    continue;
                }
                rangedNumberComponent.setHidden(false);
                component.setOffsetY(h);
                h = (int)((float)h + component.getHeight());
            }
            if (!(component instanceof EnumComponent)) continue;
            EnumComponent enumComponent = (EnumComponent)component;
            if (enumComponent.getEnumValue().getParentValueObject() != null && !enumComponent.getEnumValue().getParentValueObject().getValueAsString().equalsIgnoreCase(enumComponent.getEnumValue().getParentValue())) {
                enumComponent.setHidden(true);
                continue;
            }
            enumComponent.setHidden(false);
            component.setOffsetY(h);
            h = (int)((float)h + component.getHeight());
        }
    }

    public int getScrollY() {
        return this.scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }
}

