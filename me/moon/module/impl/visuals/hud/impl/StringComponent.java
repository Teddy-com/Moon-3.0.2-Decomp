/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals.hud.impl;

import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.impl.visuals.hud.Component;
import me.moon.utils.font.MCFontRenderer;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.FontValue;
import me.moon.utils.value.impl.StringValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class StringComponent
extends Component {
    public final BooleanValue useFont = new BooleanValue("Use font", true);
    public final FontValue fontValue = new FontValue("Font", new MCFontRenderer(new Font("Verdana", 0, 18), true, false));
    public final StringValue watermarkString = new StringValue("Watermark String", "Moon");

    public StringComponent(String name) {
        super(name, 2.0, 2.0);
        this.initValues(this.useFont, this.fontValue, this.watermarkString);
    }

    @Override
    public void onCompRender(ScaledResolution sr) {
        SimpleDateFormat time = new SimpleDateFormat("hh:mm");
        Calendar calendar = Calendar.getInstance();
        String transLatedString = ((String)this.watermarkString.getValue()).replaceAll("%fps%", "" + Minecraft.getDebugFPS()).replaceAll("%version%", "3.0.0").replaceAll("%cn%", "Moon").replaceAll("%ping%", (this.mc.getCurrentServerData() == null ? 0L : this.mc.getCurrentServerData().pingToServer) + "").replaceAll("%time%", time.format(calendar.getTime())).replaceAll("%coords%", "\u00a77X\u00a77: \u00a7f" + Math.round(this.mc.thePlayer.posX) + " \u00a77Y: \u00a7f" + Math.round(this.mc.thePlayer.posY) + " \u00a77Z: \u00a7f" + Math.round(this.mc.thePlayer.posZ)).replaceAll("&", "\u00a7");
        if (this.useFont.getValue().booleanValue()) {
            ((MCFontRenderer)this.fontValue.getValue()).drawStringWithShadow(transLatedString, this.getX(), this.getY(), -1);
        } else {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(transLatedString, (float)this.getX(), (float)this.getY(), -1);
        }
    }

    @Override
    public void onCompUpdate(UpdateEvent event) {
    }

    @Override
    public void onInit() {
    }

    private float getSpeed() {
        return (float)Math.sqrt((Minecraft.getMinecraft().thePlayer.posX - Minecraft.getMinecraft().thePlayer.prevPosX) * (Minecraft.getMinecraft().thePlayer.posX - Minecraft.getMinecraft().thePlayer.prevPosX) + (Minecraft.getMinecraft().thePlayer.posZ - Minecraft.getMinecraft().thePlayer.prevPosZ) * (Minecraft.getMinecraft().thePlayer.posZ - Minecraft.getMinecraft().thePlayer.prevPosZ));
    }
}

