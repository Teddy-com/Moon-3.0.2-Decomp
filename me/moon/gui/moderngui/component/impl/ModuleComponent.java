/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.moon.gui.moderngui.component.impl;

import java.awt.Color;
import java.util.ArrayList;
import me.moon.Moon;
import me.moon.gui.moderngui.ModernClickGui;
import me.moon.gui.moderngui.component.Component;
import me.moon.gui.moderngui.component.impl.BooleanComponent;
import me.moon.gui.moderngui.component.impl.ColorComponent;
import me.moon.gui.moderngui.component.impl.EnumComponent;
import me.moon.gui.moderngui.component.impl.KeybindComponent;
import me.moon.gui.moderngui.component.impl.NumberComponent;
import me.moon.gui.moderngui.component.impl.RangedNumberComponent;
import me.moon.gui.moderngui.frame.impl.CategoryFrame;
import me.moon.module.Module;
import me.moon.module.impl.visuals.ClickGui;
import me.moon.utils.MathUtils;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.game.Printer;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.ColorValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.NumberValue;
import me.moon.utils.value.impl.RangedValue;
import org.lwjgl.input.Keyboard;

public class ModuleComponent
extends Component {
    private final Module module;
    private final ArrayList<Component> components = new ArrayList();
    private final CategoryFrame frame;
    private boolean hasToForce = true;

    public ModuleComponent(Module module, float posX, float posY, float offsetX, float offsetY, float width, float height, CategoryFrame categoryFrame) {
        super(module.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.module = module;
        this.frame = categoryFrame;
    }

    @Override
    public void init() {
        float offY = this.getHeight() + 2.0f;
        for (Value value : this.getModule().getValues()) {
            if (value instanceof BooleanValue) {
                this.getComponents().add(new BooleanComponent((BooleanValue)value, this.getFinishedX(), this.getFinishedY(), 0.0f, offY, this.getWidth(), 14.0f));
                offY += 14.0f;
            }
            if (value instanceof NumberValue) {
                this.getComponents().add(new NumberComponent((NumberValue)value, this.getFinishedX(), this.getFinishedY(), 0.0f, offY, this.getWidth(), 16.0f));
                offY += 16.0f;
            }
            if (value instanceof EnumValue) {
                this.getComponents().add(new EnumComponent((EnumValue)value, this.getFinishedX(), this.getFinishedY(), 0.0f, offY, this.getWidth(), 20.0f));
                offY += 20.0f;
            }
            if (value instanceof RangedValue) {
                this.getComponents().add(new RangedNumberComponent((RangedValue)value, this.getFinishedX(), this.getFinishedY(), 0.0f, offY, this.getWidth(), 16.0f));
                offY += 16.0f;
            }
            if (!(value instanceof ColorValue)) continue;
            this.getComponents().add(new ColorComponent((ColorValue)value, this.getFinishedX(), this.getFinishedY(), 0.0f, offY, this.getWidth(), 100.0f));
            offY += 100.0f;
        }
        this.getComponents().add(new KeybindComponent(this.getModule(), this.getFinishedX(), this.getFinishedY(), 0.0f, offY, this.getWidth(), 14.0f));
        this.getComponents().forEach(Component::init);
    }

    public void setupHeight() {
        int h = (int)this.getHeight();
        for (Component component : this.getComponents()) {
            if (component instanceof BooleanComponent) {
                BooleanComponent booleanComponent = (BooleanComponent)component;
                if (booleanComponent.getBooleanValue().getParentValueObject() != null && !booleanComponent.getBooleanValue().getParentValueObject().getValueAsString().equalsIgnoreCase(booleanComponent.getBooleanValue().getParentValue())) {
                    booleanComponent.setHidden(true);
                    continue;
                }
                booleanComponent.setHidden(false);
                component.setOffsetY(h);
                component.setFinishedY(h);
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
                component.setFinishedY(h);
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
                component.setFinishedY(h);
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
                component.setFinishedY(h);
                h = (int)((float)h + component.getHeight());
            }
            if (component instanceof EnumComponent) {
                EnumComponent enumComponent = (EnumComponent)component;
                if (enumComponent.getEnumValue().getParentValueObject() != null && !enumComponent.getEnumValue().getParentValueObject().getValueAsString().equalsIgnoreCase(enumComponent.getEnumValue().getParentValue())) {
                    enumComponent.setHidden(true);
                    continue;
                }
                enumComponent.setHidden(false);
                component.setOffsetY(h);
                component.setFinishedY(h);
                h = (int)((float)h + component.getHeight());
            }
            if (!(component instanceof KeybindComponent)) continue;
            component.setOffsetY(h);
            component.setFinishedY(h);
        }
    }

    @Override
    public void moved(float posX, float posY) {
        super.moved(posX, posY);
        this.getComponents().forEach(component -> component.moved(this.getFinishedX(), this.getFinishedY()));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ClickGui clickGUI = (ClickGui)Moon.INSTANCE.getModuleManager().getModule("ClickGUI");
        boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX(), this.getFinishedY(), this.getWidth(), this.getHeight() - 0.5f);
        if (this.isExtended()) {
            ModernClickGui.extendedFrame = this.frame;
            this.frame.extendedComponent = this;
            this.frame.setWidth(150.0f);
            float scrolling = MathUtils.clamp(this.frame.getScrollY(), -this.getCurrentHeight() + 271.0f, 0.0f);
            if (this.getCurrentHeight() > 268.0f) {
                scrolling = MathUtils.clamp(this.frame.getScrollY(), 0.0f, -this.getCurrentHeight() + 271.0f);
            }
            if (this.getCurrentHeight() < 268.0f) {
                scrolling = 0.0f;
            }
            float finalScrolling = scrolling;
            this.getComponents().forEach(component -> component.moved(this.getFinishedX(), this.getFinishedY() + finalScrolling));
            this.frame.maxScroll = scrolling;
            this.frame.notAnimated = Math.min(this.getCurrentHeight() + this.getHeight(), 288.0f);
        } else {
            this.frame.notAnimated = 288.0f;
            this.hasToForce = false;
        }
        for (Component component2 : this.getComponents()) {
            component2.setWidth(this.getWidth());
            if (!this.isExtended() || component2.getHidden().booleanValue()) continue;
            component2.drawScreen(mouseX, mouseY, partialTicks);
        }
        this.setupHeight();
        RenderUtil.drawRect(this.getFinishedX(), this.getFinishedY(), this.getWidth(), this.getHeight(), this.module.isEnabled() ? (hovered ? new Color((Integer)clickGUI.color.getValue()).darker().getRGB() : ((Integer)clickGUI.color.getValue()).intValue()) : (hovered ? new Color(37, 37, 37).brighter().getRGB() : new Color(37, 37, 37).getRGB()));
        Fonts.moon.drawCenteredString(this.getLabel() + (Keyboard.isKeyDown((int)29) && this.module.getKeyBind() != 0 && !this.isExtended() ? " [" + Keyboard.getKeyName((int)this.module.getKeyBind()) + "]" : "") + (this.isExtended() ? " Settings" : ""), this.getFinishedX() + this.getWidth() / 2.0f, this.getFinishedY() + 1.0f + this.getHeight() / 2.0f - (float)Fonts.moon.getStringHeight(this.getLabel()) / 2.0f, hovered ? -1 : -2236963);
    }

    @Override
    public void keyTyped(char character, int keyCode) {
        super.keyTyped(character, keyCode);
        if (this.isExtended()) {
            this.getComponents().forEach(component -> component.keyTyped(character, keyCode));
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX(), this.isExtended() ? this.frame.getPosY() + this.frame.getHeight() - 1.0f : this.getFinishedY(), this.getWidth(), this.getHeight() - 0.5f);
        boolean hovered2 = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX(), this.getFinishedY() + this.getHeight(), this.getWidth(), this.frame.bottomHeight - 20.0f);
        if (hovered) {
            switch (mouseButton) {
                case 0: {
                    if (Keyboard.isKeyDown((int)42)) {
                        this.getModule().setHidden(!this.getModule().isHidden());
                        Printer.print("Set " + this.getModule().getLabel() + " to " + (this.getModule().isHidden() ? "hidden in the arraylist!" : "shown in the arraylist!"));
                        break;
                    }
                    this.getModule().setEnabled(!this.getModule().isEnabled());
                    break;
                }
                case 1: {
                    if (!this.getComponents().isEmpty()) {
                        this.setExtended(!this.isExtended());
                    }
                    if (!this.isExtended()) {
                        ModernClickGui.extendedFrame = null;
                        this.frame.setWidth(110.0f);
                        this.frame.setScrollY((int)this.frame.scrollBefore);
                        this.frame.transUtil.setPosY(this.frame.scrollBefore);
                        this.hasToForce = false;
                        for (Component component2 : this.frame.getComponents()) {
                            component2.forceAnimation();
                        }
                        this.forceAnimation();
                    } else {
                        this.hasToForce = true;
                        this.forceAnimation();
                        this.getComponents().forEach(Component::forceAnimation);
                        this.frame.scrollBefore = this.frame.getScrollY();
                        this.frame.setScrollY(0);
                        this.frame.transUtil.setPosY(0.0);
                        this.hasToForce = true;
                    }
                    this.forceAnimation();
                    break;
                }
            }
        }
        if (this.isExtended()) {
            this.getComponents().forEach(component -> {
                if (hovered2) {
                    component.mouseClicked(mouseX, mouseY, mouseButton);
                }
            });
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public float getCurrentHeight() {
        float cHeight = 0.0f;
        for (Component component : this.getComponents()) {
            if (component.getHidden().booleanValue()) continue;
            cHeight += component.getHeight();
        }
        return cHeight;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        if (this.isExtended()) {
            this.getComponents().forEach(component -> component.mouseReleased(mouseX, mouseY, mouseButton));
        }
    }

    public Module getModule() {
        return this.module;
    }

    public ArrayList<Component> getComponents() {
        return this.components;
    }
}

