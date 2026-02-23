/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.moon.gui.account.gui.components;

import me.moon.Moon;
import me.moon.gui.account.gui.GuiAltManager;
import me.moon.gui.account.gui.components.GuiSlotCustom;
import me.moon.gui.account.system.Account;
import me.moon.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiAccountList
extends GuiSlotCustom {
    public int selected = -1;

    public GuiAccountList(GuiAltManager parent) {
        super(Minecraft.getMinecraft(), parent.width, parent.height, 32, parent.height - 56, 36);
    }

    @Override
    public int getSize() {
        return Moon.INSTANCE.getAccountManager().getAccounts().size();
    }

    @Override
    public void elementClicked(int i, boolean b, int i1, int i2) {
        this.selected = i;
        if (b) {
            GuiAltManager.INSTANCE.login(this.getAccount(i));
        }
    }

    @Override
    protected boolean isSelected(int i) {
        return i == this.selected;
    }

    @Override
    protected void drawBackground() {
    }

    @Override
    protected void drawSlot(int i, int i1, int i2, int i3, int i4, int i5) {
        Account account = this.getAccount(i);
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int x = i1 + 2;
        if (i2 >= this.height || i2 <= 0) {
            return;
        }
        GL11.glTranslated((double)x, (double)i2, (double)0.0);
        if (account.getEmail().contains("@alt.com")) {
            RenderUtil.drawImage(new ResourceLocation("textures/client/altening.png"), 2.0f, 5.0f, 20, 22);
        } else {
            RenderUtil.drawImage(new ResourceLocation("textures/client/mojang.png"), 2.0f, 5.0f, 20, 20);
        }
        fontRenderer.drawStringWithShadow(account.getName(), 112 - fontRenderer.getStringWidth(account.getName()) / 2, 6.0f, -1);
        if (!account.getName().equalsIgnoreCase(account.getEmail())) {
            fontRenderer.drawStringWithShadow(account.getEmail().contains("@alt.com") ? "\u00a77***********@alt.com" : "\u00a77" + account.getEmail(), 112 - fontRenderer.getStringWidth(account.getEmail().contains("@alt.com") ? "***********@alt.com" : account.getEmail()) / 2, 6 + fontRenderer.FONT_HEIGHT + 2, -1);
        }
        GL11.glTranslated((double)(-x), (double)(-i2), (double)0.0);
    }

    public Account getAccount(int i) {
        return Moon.INSTANCE.getAccountManager().getAccounts().get(i);
    }

    private void drawFace(String name, int x, int y, int w, int h) {
        try {
            GL11.glEnable((int)3042);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(name), name).loadTexture(Minecraft.getMinecraft().getResourceManager());
            Minecraft.getMinecraft().getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(name));
            Gui.drawModalRectWithCustomSizedTexture(x, y, 24.0f, 24.0f, 24, 24, 192.0f, 192.0f);
            GL11.glDisable((int)3042);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void removeSelected() {
        if (this.selected == -1) {
            return;
        }
        Moon.INSTANCE.getAccountManager().getAccounts().remove(this.getAccount(this.selected--));
        Moon.INSTANCE.getAccountManager().save();
    }

    public Account getSelectedAccount() {
        return this.getAccount(this.selected);
    }
}

