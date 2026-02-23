/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.moon.gui.discordclickgui.component.components;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.moon.gui.discordclickgui.component.Component;
import me.moon.gui.discordclickgui.component.components.ModuleComponent;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.BooleanValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class BooleanComponent
extends Component {
    private BooleanValue value;
    private ModuleComponent parent;

    public BooleanComponent(ModuleComponent parent, BooleanValue value, float posX, float posY, float offsetX, float offsetY, float height) {
        super(value.getLabel(), posX, posY, offsetX, offsetY, height);
        this.value = value;
        this.parent = parent;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, ScaledResolution scaledResolution) {
        super.onDrawScreen(mouseX, mouseY, scaledResolution);
        Fonts.clickfont14.drawString(ChatFormatting.GRAY + this.getLabel(), this.getFinishedX(), this.getFinishedY() - 0.5f, -11645101);
        Fonts.clickfont18.drawString(ChatFormatting.WHITE + this.value.getDescription(), this.getFinishedX(), this.getFinishedY() + 8.0f, -11645101);
        RenderUtil.drawImage(new ResourceLocation(this.value.isEnabled() ? "textures/client/Enabled.png" : "textures/client/Disabled.png"), this.getFinishedX() + 116.0f, this.getFinishedY() + 6.0f, 21, 12);
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + 116.0f, this.getFinishedY() + 6.0f, 21.0, 12.0) && button == 0) {
            this.value.setEnabled(!this.value.isEnabled());
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void onKeyTyped(char keyChar, int key) {
        super.onKeyTyped(keyChar, key);
    }

    public BooleanValue getValue() {
        return this.value;
    }
}

