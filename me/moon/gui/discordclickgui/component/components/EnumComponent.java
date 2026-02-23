/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  org.apache.commons.lang3.StringUtils
 */
package me.moon.gui.discordclickgui.component.components;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import me.moon.gui.discordclickgui.component.Component;
import me.moon.gui.discordclickgui.component.components.ModuleComponent;
import me.moon.utils.font.Fonts;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.EnumValue;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringUtils;

public class EnumComponent
extends Component {
    private EnumValue value;
    private ModuleComponent parent;
    private boolean isExtended;

    public EnumComponent(ModuleComponent parent, EnumValue value, float posX, float posY, float offsetX, float offsetY, float height) {
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
        Fonts.clickfont14.drawString(ChatFormatting.GRAY + this.getLabel(), this.getFinishedX(), this.getFinishedY(), new Color(229, 229, 223, 255).getRGB());
        if (this.isExtended()) {
            this.setHeight(27 + 15 * (this.value.getConstants().length - 1));
            RenderUtil.drawRoundedRect(this.getFinishedX() + 90.0f - 82.0f, this.getFinishedY() + 18.5f, 76.0, 15 * (this.value.getConstants().length - 1), 3.0, new Color(-13618633).brighter().getRGB());
            float enumY = this.getFinishedY() + 25.0f;
            for (Enum enoom : this.value.getConstants()) {
                if (enoom == this.value.getValue()) continue;
                Fonts.clickfont18.drawString(StringUtils.capitalize((String)enoom.name().toLowerCase()), this.getFinishedX() + 90.0f - 43.0f - (float)Fonts.clickfont18.getStringWidth(StringUtils.capitalize((String)enoom.name().toLowerCase())) / 2.0f, enumY, new Color(229, 229, 223, 255).getRGB());
                enumY += 15.0f;
            }
        } else {
            this.setHeight(27.0f);
        }
        RenderUtil.drawRoundedRect(this.getFinishedX() + 90.0f - 84.0f, this.getFinishedY() + 6.0f, 80.0, 15.0, 3.0, new Color(-14671323).brighter().getRGB());
        Fonts.clickfont18.drawString(StringUtils.capitalize((String)this.value.getValue().toString().toLowerCase()), this.getFinishedX() + 90.0f - 82.0f, this.getFinishedY() + 11.0f, new Color(229, 229, 223, 255).getRGB());
        RenderUtil.drawArrow(this.getFinishedX() + 90.0f - 16.0f, this.getFinishedY() + 11.0f, this.isExtended(), new Color(229, 229, 223, 255).getRGB());
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        if (button == 0 && MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + 90.0f - 84.0f, this.getFinishedY() + 4.5f, 80.0, 16.0)) {
            this.setExtended(!this.isExtended());
        }
        if (button == 0 && this.isExtended()) {
            float enumY = this.getFinishedY() + 25.0f;
            for (Enum enoom : this.value.getConstants()) {
                if (enoom == this.value.getValue()) continue;
                if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getFinishedX() + 90.0f - 80.0f, enumY - 4.0f, 72.0, 15.0)) {
                    this.value.setValue(enoom);
                    this.setExtended(false);
                }
                enumY += 15.0f;
            }
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

    public EnumValue getValue() {
        return this.value;
    }

    public boolean isExtended() {
        return this.isExtended;
    }

    public void setExtended(boolean extended) {
        this.isExtended = extended;
    }
}

