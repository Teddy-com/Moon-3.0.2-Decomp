/*
 * Decompiled with CFR 0.152.
 */
package me.moon.gui.account.gui.components;

import me.moon.gui.account.gui.impl.GuiTextBox;
import net.minecraft.client.gui.FontRenderer;

public class GuiPasswordField
extends GuiTextBox {
    public GuiPasswordField(int componentId, FontRenderer fontrendererObj, int x, int y, int width, int height) {
        super(componentId, fontrendererObj, x, y, width, height);
    }

    @Override
    public void drawTextBox() {
        String text = this.getText();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); ++i) {
            builder.append("*");
        }
        this.setText(builder.toString());
        super.drawTextBox();
        this.setText(text);
    }
}

