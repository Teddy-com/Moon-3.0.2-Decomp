/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package me.moon.module.impl.visuals.hud.settingsscreen.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import me.moon.Moon;
import me.moon.module.impl.visuals.hud.settingsscreen.Component;
import me.moon.utils.game.MouseUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.StringValue;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringUtils;

public class StringComponent
extends Component {
    private StringValue stringValue;
    private boolean editinig;
    private String content = "";

    public StringComponent(StringValue stringValue, float posX, float posY) {
        super(stringValue.getLabel(), posX, posY);
        this.stringValue = stringValue;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        boolean isHovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY(), Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.isEditinig() ? this.content : this.getComponentName() + ": " + StringUtils.capitalize((String)((String)this.stringValue.getValue()).toLowerCase())) + 4, 10.0);
        RenderUtil.drawRect(this.getPosX(), this.getPosY(), 150.0, 10.0, -15658735);
        SimpleDateFormat time = new SimpleDateFormat("hh:mm");
        Calendar calendar = Calendar.getInstance();
        Minecraft mc = Minecraft.getMinecraft();
        String transLatedString = ((String)this.stringValue.getValue()).replaceAll("%fps%", "" + Minecraft.getDebugFPS()).replaceAll("%version%", "3.0.0").replaceAll("%cn%", "Moon").replaceAll("%ping%", (mc.getCurrentServerData() == null ? 0L : mc.getCurrentServerData().pingToServer) + "").replaceAll("%time%", time.format(calendar.getTime())).replaceAll("%coords%", "\u00a77X\u00a77: \u00a7f" + Math.round(mc.thePlayer.posX) + " \u00a77Y: \u00a7f" + Math.round(mc.thePlayer.posY) + " \u00a77Z: \u00a7f" + Math.round(mc.thePlayer.posZ)).replaceAll("&", "\u00a7");
        String translatedContent = this.content.replaceAll("%fps%", "" + Minecraft.getDebugFPS()).replaceAll("%version%", "3.0.0").replaceAll("%cn%", "Moon").replaceAll("%ping%", (mc.getCurrentServerData() == null ? 0L : mc.getCurrentServerData().pingToServer) + "").replaceAll("%time%", time.format(calendar.getTime())).replaceAll("%coords%", "\u00a77X\u00a77: \u00a7f" + Math.round(mc.thePlayer.posX) + " \u00a77Y: \u00a7f" + Math.round(mc.thePlayer.posY) + " \u00a77Z: \u00a7f" + Math.round(mc.thePlayer.posZ)).replaceAll("&[0-9][a-z]", "\u00a7");
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.isEditinig() ? translatedContent + (this.isEditinig() ? "..." : "") : transLatedString, this.getPosX() + 2.0f, this.getPosY() + 1.0f, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, this.getPosX(), this.getPosY(), Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.getComponentName() + ": " + StringUtils.capitalize((String)((String)this.stringValue.getValue()).toLowerCase())) + 4, 10.0) && mouseButton == 0) {
            this.setEditinig(!this.isEditinig());
            if (this.isEditinig()) {
                this.content = (String)this.stringValue.getValue();
                Moon.INSTANCE.getComponentManager().saveComps();
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        super.keyTyped(typedChar, key);
        if (this.isEditinig()) {
            String specialChars = "/*!@#$%^&*()\"{}_[]|\\?/<>,.";
            if (key == 14) {
                if (this.content.length() > 1) {
                    this.content = this.content.substring(0, this.content.length() - 1);
                } else if (this.content.length() == 1) {
                    this.content = "";
                }
            } else if (key == 28) {
                this.stringValue.setValue(this.content);
                this.content = "";
                this.setEditinig(false);
            } else if ((Character.isLetterOrDigit(typedChar) || Character.isSpaceChar(typedChar) || specialChars.contains(Character.toString(typedChar))) && Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.content) < 230) {
                this.content = this.content + Character.toString(typedChar);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    private void setEditinig(boolean editinig) {
        this.editinig = editinig;
    }

    private boolean isEditinig() {
        return this.editinig;
    }

    private float getSpeed() {
        return (float)Math.sqrt(Minecraft.getMinecraft().thePlayer.motionX * Minecraft.getMinecraft().thePlayer.motionX + Minecraft.getMinecraft().thePlayer.motionZ * Minecraft.getMinecraft().thePlayer.motionZ + (Minecraft.getMinecraft().thePlayer.motionY > 0.0 ? Minecraft.getMinecraft().thePlayer.motionY * Minecraft.getMinecraft().thePlayer.motionY : 0.0));
    }
}

