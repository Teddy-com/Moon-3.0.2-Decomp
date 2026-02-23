/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.UserAuthentication
 *  com.mojang.authlib.exceptions.AuthenticationException
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 */
package me.moon.gui.account.gui.thread;

import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import java.util.UUID;
import me.moon.Moon;
import me.moon.gui.account.gui.GuiAltManager;
import me.moon.gui.account.gui.impl.GuiAlteningLogin;
import me.moon.gui.account.system.Account;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.util.Session;

public class AccountLoginThread
extends Thread {
    private final Account account;
    private String status = "Waiting for login...";

    public AccountLoginThread(Account account) {
        this.account = account;
    }

    public AccountLoginThread(String email, String password) {
        this.account = new Account(email, password);
    }

    @Override
    public void run() {
        this.status = "Logging in...";
        if (Minecraft.getMinecraft().currentScreen instanceof GuiAlteningLogin || this.account.getEmail().contains("@alt.com") || Minecraft.getMinecraft().currentScreen instanceof GuiDisconnected && GuiDisconnected.niggaButton) {
            Moon.INSTANCE.switchToTheAltening();
        } else {
            Moon.INSTANCE.switchToMojang();
        }
        YggdrasilAuthenticationService yService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
        UserAuthentication userAuth = yService.createUserAuthentication(Agent.MINECRAFT);
        if (userAuth == null) {
            this.status = "\u00a74Unknown error.";
            return;
        }
        try {
            String email = this.account.getEmail();
            userAuth.setUsername(email);
            userAuth.setPassword(this.account.getPassword());
            userAuth.logIn();
            Session session = new Session(userAuth.getSelectedProfile().getName(), userAuth.getSelectedProfile().getId().toString(), userAuth.getAuthenticatedToken(), email.contains("@") ? "mojang" : "legacy");
            Minecraft.getMinecraft().setSession(session);
            this.account.setName(session.getUsername());
            if (!(Minecraft.getMinecraft().currentScreen instanceof GuiAlteningLogin) && !(Minecraft.getMinecraft().currentScreen instanceof GuiDisconnected)) {
                Moon.INSTANCE.getAccountManager().setLastAlt(this.account);
            }
            Moon.INSTANCE.getAccountManager().save();
            GuiAltManager.currentAccount = this.account;
            this.status = String.format("\u00a7aLogged in as %s.", this.account.getName());
        }
        catch (AuthenticationException exception) {
            this.status = "\u00a74Login failed.";
        }
        catch (NullPointerException exception) {
            this.status = "\u00a74Unknown error.";
        }
    }

    public String getStatus() {
        return this.status;
    }
}

