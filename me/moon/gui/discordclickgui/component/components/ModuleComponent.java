/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package me.moon.gui.discordclickgui.component.components;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import me.moon.gui.discordclickgui.component.Component;
import me.moon.gui.discordclickgui.component.components.BooleanComponent;
import me.moon.gui.discordclickgui.component.components.ColorComponent;
import me.moon.gui.discordclickgui.component.components.EnumComponent;
import me.moon.gui.discordclickgui.component.components.NumberComponent;
import me.moon.gui.discordclickgui.component.components.RangedNumberComponent;
import me.moon.gui.discordclickgui.frame.frames.DiscordFrame;
import me.moon.module.Module;
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
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ModuleComponent
extends Component {
    private Module module;
    private boolean binding;
    private boolean extended;
    private DiscordFrame frame;
    private ArrayList<Component> components = new ArrayList();
    private int scrollY;

    public ModuleComponent(DiscordFrame frame, Module module, float posX, float posY, float offsetX, float offsetY, float height) {
        super(module.getLabel(), posX, posY, offsetX, offsetY, height);
        this.module = module;
        this.frame = frame;
    }

    @Override
    public void init() {
        super.init();
        if (this.getModule().getValues().isEmpty()) {
            return;
        }
        float y = 34.0f;
        for (Value value : this.getModule().getValues()) {
            if (value instanceof BooleanValue) {
                this.getComponents().add(new BooleanComponent(this, (BooleanValue)value, this.getFinishedX(), this.frame.getPosY(), 96.0f, y, 20.0f));
                y += 20.0f;
            }
            if (value instanceof NumberValue) {
                this.getComponents().add(new NumberComponent(this, (NumberValue)value, this.getFinishedX(), this.frame.getPosY(), 96.0f, y, 20.0f));
                y += 20.0f;
            }
            if (value instanceof RangedValue) {
                this.getComponents().add(new RangedNumberComponent(this, (RangedValue)value, this.getFinishedX(), this.frame.getPosY(), 96.0f, y, 20.0f));
                y += 20.0f;
            }
            if (value instanceof ColorValue) {
                this.getComponents().add(new ColorComponent(this, (ColorValue)value, this.getFinishedX(), this.frame.getPosY(), 96.0f, y, 90.0f));
                y += 90.0f;
            }
            if (!(value instanceof EnumValue)) continue;
            this.getComponents().add(new EnumComponent(this, (EnumValue)value, this.getFinishedX(), this.frame.getPosY(), 96.0f, y, 17 + ((EnumValue)value).getConstants().length * 20));
            y += (float)(17 + ((EnumValue)value).getConstants().length * 20);
        }
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, ScaledResolution scaledResolution) {
        super.onDrawScreen(mouseX, mouseY, scaledResolution);
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() - 2.0f, this.getFinishedY() - 5.0f, 94.0, 14.0)) {
            RenderUtil.drawRect(this.getFinishedX() - 2.0f, this.getFinishedY() - 5.0f, 93.33, 14.0, -13355204);
            if (!this.components.isEmpty()) {
                GL11.glPushMatrix();
                GL11.glScalef((float)0.05f, (float)0.05f, (float)0.05f);
                GlStateManager.enableBlend();
                RenderUtil.drawImage(new ResourceLocation("textures/client/Gear.png"), (this.getFinishedX() + 80.0f) * 20.0f, (this.getFinishedY() - 2.5f) * 20.0f, 200, 200);
                GL11.glPopMatrix();
            }
        }
        Fonts.clickfont18.drawString(this.isBinding() ? "Press a key..." : (this.components.isEmpty() ? ChatFormatting.DARK_GRAY + "# " : "") + (this.getModule().isEnabled() ? ChatFormatting.WHITE : ChatFormatting.DARK_GRAY) + this.getLabel() + (Keyboard.isKeyDown((int)29) && this.module.getKeyBind() != 0 ? " [" + Keyboard.getKeyName((int)this.module.getKeyBind()) + "]" : ""), this.getFinishedX() + (float)(this.components.isEmpty() ? 0 : 10), this.getFinishedY() - 0.5f, -11645101);
        if (!this.components.isEmpty()) {
            RenderUtil.drawImage(new ResourceLocation("textures/client/Megaphone.png"), this.getFinishedX(), this.getFinishedY() - 1.5f, 8, 8);
        }
        if (this.isExtended()) {
            Fonts.clickfont18.drawString(this.getLabel() + "'s Values", this.getFinishedX() + 96.0f, this.getFrame().getPosY() + 24.0f - (float)Fonts.clickfont18.getStringHeight(this.getLabel() + "'s Values"), -1);
            if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + 94.0f, this.getFrame().getPosY() + 30.0f, this.getFrame().getWidth() / 2.0f + 8.0f, 255.0) && this.getHeightXD() >= 255) {
                int wheel = Mouse.getDWheel();
                if (wheel < 0) {
                    if (this.getScrollY() - 12 < -(this.getHeightXD() - 251)) {
                        this.setScrollY(-(this.getHeightXD() - 251));
                    } else {
                        this.setScrollY(this.getScrollY() - 12);
                    }
                } else if (wheel > 0) {
                    this.setScrollY(this.getScrollY() + 12);
                }
            }
            if (this.getScrollY() > 0) {
                this.setScrollY(0);
            }
            this.components.stream().filter(component -> !component.isHidden()).forEach(component -> {
                GL11.glPushMatrix();
                component.onDrawScreen(mouseX, mouseY, scaledResolution);
                GL11.glPopMatrix();
                component.setOffsetY(component.getBaseOffsetY() + (float)this.getScrollY() + 35.0f);
                component.updatePosition(this.getFinishedX(), this.frame.getPosY());
            });
        }
        this.getComponentHeight();
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        switch (button) {
            case 0: {
                if (!MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() - 2.0f, this.getFinishedY() - 5.0f, 93.33, 14.0) || !MouseUtil.mouseWithinBounds(mouseX, mouseY, this.frame.getPosX() + 35.0f, this.frame.getPosY() + 30.0f, 100.33, 255.0)) break;
                this.getModule().setEnabled(!this.getModule().isEnabled());
                break;
            }
            case 1: {
                if (this.getComponents().isEmpty() || !MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() - 2.0f, this.getFinishedY() - 5.0f, 93.33, 14.0)) break;
                this.setExtended(!this.isExtended());
                this.frame.getComponents().stream().filter(component -> component instanceof ModuleComponent && component != this && ((ModuleComponent)component).getModule().getCategory() == this.getModule().getCategory()).forEach(component -> ((ModuleComponent)component).setExtended(false));
                this.setScrollY(0);
                break;
            }
            case 2: {
                if (!MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() - 2.0f, this.getFinishedY() - 5.0f, 93.33, 14.0)) break;
                this.setBinding(!this.isBinding());
                break;
            }
        }
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + 94.0f, this.getFrame().getPosY() + 30.0f, this.getFrame().getWidth() / 2.0f + 8.0f, 254.0) && this.isExtended()) {
            this.components.stream().filter(component -> !component.isHidden()).forEach(component -> component.onMouseClicked(mouseX, mouseY, button));
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
        if (this.isExtended()) {
            this.components.stream().filter(component -> !component.isHidden()).forEach(component -> component.onMouseReleased(mouseX, mouseY, button));
        }
    }

    @Override
    public void onKeyTyped(char keyChar, int key) {
        super.onKeyTyped(keyChar, key);
        if (this.isBinding()) {
            this.getModule().setKeyBind(key == 14 ? 0 : key);
            Printer.print("Set " + this.getModule().getLabel() + "'s keybind to " + Keyboard.getKeyName((int)(key == 14 ? 0 : key)) + "!");
            this.setBinding(false);
        }
        if (this.isExtended()) {
            this.components.stream().filter(component -> !component.isHidden()).forEach(component -> component.onKeyTyped(keyChar, key));
        }
    }

    @Override
    public void updatePosition(float posX, float posY) {
        super.updatePosition(posX, posY);
        this.components.stream().filter(component -> !component.isHidden()).forEach(component -> component.updatePosition(this.getFinishedX(), this.frame.getPosY()));
    }

    public Module getModule() {
        return this.module;
    }

    public boolean isBinding() {
        return this.binding;
    }

    public void setBinding(boolean binding) {
        this.binding = binding;
    }

    public boolean isExtended() {
        return this.extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public DiscordFrame getFrame() {
        return this.frame;
    }

    public ArrayList<Component> getComponents() {
        return this.components;
    }

    public int getScrollY() {
        return this.scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }

    public int getComponentHeight() {
        int h = 0;
        for (Component component : this.getComponents()) {
            if (component instanceof BooleanComponent) {
                BooleanComponent booleanComponent = (BooleanComponent)component;
                if (booleanComponent.getValue().getParentValueObject() != null && !booleanComponent.getValue().getParentValueObject().getValueAsString().equalsIgnoreCase(booleanComponent.getValue().getParentValue())) {
                    booleanComponent.setHidden(true);
                    continue;
                }
                booleanComponent.setHidden(false);
                component.setBaseOffsetY(h);
                component.setFinishedY(h);
                h = (int)((float)h + component.getHeight());
            }
            if (component instanceof NumberComponent) {
                NumberComponent numberComponent = (NumberComponent)component;
                if (numberComponent.getValue().getParentValueObject() != null && !numberComponent.getValue().getParentValueObject().getValueAsString().equalsIgnoreCase(numberComponent.getValue().getParentValue())) {
                    numberComponent.setHidden(true);
                    continue;
                }
                numberComponent.setHidden(false);
                component.setBaseOffsetY(h);
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
                component.setBaseOffsetY(h);
                component.setFinishedY(h);
                h = (int)((float)h + component.getHeight());
            }
            if (component instanceof RangedNumberComponent) {
                RangedNumberComponent rangedNumberComponent = (RangedNumberComponent)component;
                if (rangedNumberComponent.getValue().getParentValueObject() != null && !rangedNumberComponent.getValue().getParentValueObject().getValueAsString().equalsIgnoreCase(rangedNumberComponent.getValue().getParentValue())) {
                    rangedNumberComponent.setHidden(true);
                    continue;
                }
                rangedNumberComponent.setHidden(false);
                component.setBaseOffsetY(h);
                component.setFinishedY(h);
                h = (int)((float)h + component.getHeight());
            }
            if (!(component instanceof EnumComponent)) continue;
            EnumComponent enumComponent = (EnumComponent)component;
            if (enumComponent.getValue().getParentValueObject() != null && !enumComponent.getValue().getParentValueObject().getValueAsString().equalsIgnoreCase(enumComponent.getValue().getParentValue())) {
                enumComponent.setHidden(true);
                continue;
            }
            enumComponent.setHidden(false);
            component.setBaseOffsetY(h);
            component.setFinishedY(h);
            h = (int)((float)h + component.getHeight());
        }
        return h;
    }

    public int getHeightXD() {
        int h = 0;
        for (Component component : this.getComponents()) {
            if (component.isHidden()) continue;
            h = (int)((float)h + component.getHeight());
        }
        return h;
    }
}

