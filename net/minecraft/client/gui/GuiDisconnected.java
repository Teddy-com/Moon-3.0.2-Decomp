/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import me.moon.Moon;
import me.moon.gui.account.gui.GuiAltManager;
import me.moon.gui.account.gui.thread.AccountLoginThread;
import me.moon.gui.account.system.Account;
import me.moon.utils.thealtening.TheAltening;
import me.moon.utils.thealtening.domain.AlteningAlt;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;

public class GuiDisconnected
extends GuiScreen {
    public static boolean niggaButton = false;
    public static ServerData serverData;
    private String reason;
    private IChatComponent message;
    private List<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;

    public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, IChatComponent chatComp) {
        this.parentScreen = screen;
        this.reason = I18n.format(reasonLocalizationKey, new Object[0]);
        this.message = chatComp;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 103, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, 130, 20, "Go to AltManager"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 233, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, 130, 20, "Remove Alt"));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + 33, "Relog with new Alt (Normal)"));
        this.buttonList.add(new GuiButton(6, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + 57, "Relog with new Alt (TheAltening)"));
        this.buttonList.add(new GuiButton(7, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + 81, "Relog"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiAltManager());
        }
        if (button.id == 2 && GuiAltManager.currentAccount != null) {
            GuiAltManager.currentAccount.setBanned(true);
        }
        if (button.id == 4 && GuiAltManager.currentAccount != null) {
            if (GuiAltManager.loginThread != null) {
                GuiAltManager.loginThread = null;
            }
            Moon.INSTANCE.getAccountManager().getAccounts().remove(GuiAltManager.currentAccount);
            Moon.INSTANCE.getAccountManager().save();
        }
        if (button.id == 5) {
            if (Moon.INSTANCE.getAccountManager().getAccounts().isEmpty()) {
                return;
            }
            ArrayList<Account> registry = Moon.INSTANCE.getAccountManager().getAccounts();
            Random random = new Random();
            Account randomAlt = registry.get(random.nextInt(Moon.INSTANCE.getAccountManager().getAccounts().size()));
            String user2 = randomAlt.getName();
            String pass2 = randomAlt.getPassword();
            if (randomAlt.isBanned()) {
                return;
            }
            GuiAltManager.currentAccount = randomAlt;
            try {
                GuiAltManager.loginThread = new AccountLoginThread(user2, pass2);
                GuiAltManager.loginThread.start();
                if (serverData != null) {
                    this.mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), this.mc, serverData));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (button.id == 6) {
            if (Moon.INSTANCE.getAccountManager().getAlteningKey() == null) {
                return;
            }
            niggaButton = true;
            try {
                TheAltening theAltening = new TheAltening(Moon.INSTANCE.getAccountManager().getAlteningKey());
                AlteningAlt account = theAltening.generateAccount(theAltening.getUser());
                if (!Objects.requireNonNull(account).getToken().isEmpty()) {
                    GuiAltManager.loginThread = new AccountLoginThread(Objects.requireNonNull(account).getToken().replaceAll(" ", ""), "nig");
                    GuiAltManager.loginThread.start();
                }
                if (serverData != null) {
                    this.mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), this.mc, serverData));
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (button.id == 7 && serverData != null) {
            this.mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), this.mc, serverData));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.reason, this.width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 0xAAAAAA);
        int var4 = this.height / 2 - this.field_175353_i / 2;
        if (this.multilineMessage != null) {
            for (String var6 : this.multilineMessage) {
                this.drawCenteredString(this.fontRendererObj, var6, this.width / 2, var4, 0xFFFFFF);
                if (GuiAltManager.currentAccount != null) {
                    this.drawCenteredString(this.fontRendererObj, "Current Alt: " + this.mc.getSession().getUsername(), this.width / 2, 20, -1);
                }
                var4 += this.fontRendererObj.FONT_HEIGHT;
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

