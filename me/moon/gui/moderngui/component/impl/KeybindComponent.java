/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.moon.gui.moderngui.component.impl;

import me.moon.Moon;
import me.moon.gui.moderngui.component.Component;
import me.moon.module.Module;
import me.moon.module.impl.visuals.ClickGui;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.game.Printer;
import me.moon.utils.render.RenderUtil;
import org.lwjgl.input.Keyboard;

public class KeybindComponent
extends Component {
    private final Module module;
    private boolean binding;

    public KeybindComponent(Module module, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(module.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.module = module;
    }

    @Override
    public void init() {
    }

    @Override
    public void moved(float posX, float posY) {
        super.moved(posX, posY);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ClickGui clickGUI = (ClickGui)Moon.INSTANCE.getModuleManager().getModule("ClickGUI");
        RenderUtil.drawRect(this.getFinishedX(), this.getFinishedY(), this.getWidth(), this.getHeight(), clickGUI.getColor());
        RenderUtil.drawRect(this.getFinishedX() + 6.0f, this.getFinishedY() + this.getHeight() - 2.0f, this.getWidth() - 12.0f, 0.5, (Integer)clickGUI.color.getValue());
        Fonts.moonSmaller.drawString(this.isBinding() ? "Press a key..." : "Bind: " + Keyboard.getKeyName((int)this.getModule().getKeyBind()), this.getFinishedX() + 7.0f, this.getFinishedY() + 0.5f + this.getHeight() / 2.0f - (float)(Fonts.moonSmaller.getStringHeight(this.getLabel()) >> 1), -1);
    }

    @Override
    public void keyTyped(char character, int keyCode) {
        super.keyTyped(character, keyCode);
        if (this.isBinding()) {
            this.getModule().setKeyBind(keyCode == 1 || keyCode == 57 || keyCode == 211 ? 0 : keyCode);
            Printer.print("Bound " + this.getLabel() + " to " + Keyboard.getKeyName((int)this.getModule().getKeyBind()));
            this.setBinding(false);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + 5.0f, this.getFinishedY() + 1.0f, this.getWidth() - 10.0f, this.getHeight() - 2.0f);
        if (hovered && mouseButton == 0) {
            this.setBinding(!this.isBinding());
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
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
}

