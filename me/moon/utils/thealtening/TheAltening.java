/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 */
package me.moon.utils.thealtening;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import me.moon.utils.thealtening.domain.AlteningAlt;
import me.moon.utils.thealtening.domain.User;
import me.moon.utils.thealtening.utils.Utilities;

public final class TheAltening {
    private final String apiKey;
    private final String website = "http://api.thealtening.com/v1/";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public TheAltening(String apiKey) {
        this.apiKey = apiKey;
    }

    public User getUser() throws IOException {
        URLConnection licenseEndpoint = new URL(this.attach(this.website + "license")).openConnection();
        String userInfo = new String(Utilities.getInstance().readAllBytes(licenseEndpoint.getInputStream()));
        return (User)this.gson.fromJson(userInfo, User.class);
    }

    public AlteningAlt generateAccount(User user) throws IOException {
        URLConnection generateEndpoint = new URL(this.attach(this.website + "generate")).openConnection();
        String accountInfo = new String(Utilities.getInstance().readAllBytes(generateEndpoint.getInputStream()));
        if (user.isPremium()) {
            return (AlteningAlt)this.gson.fromJson(accountInfo, AlteningAlt.class);
        }
        return null;
    }

    public boolean favoriteAccount(AlteningAlt account) throws IOException {
        URLConnection favoriteAccount = new URL(this.attachAccount(this.website + "favorite", account)).openConnection();
        String info = new String(Utilities.getInstance().readAllBytes(favoriteAccount.getInputStream()));
        return info.isEmpty();
    }

    public boolean privateAccount(AlteningAlt account) throws IOException {
        URLConnection privateAccount = new URL(this.attachAccount(this.website + "private", account)).openConnection();
        String info = new String(Utilities.getInstance().readAllBytes(privateAccount.getInputStream()));
        return info.isEmpty();
    }

    private String attach(String website) {
        return website + "?token=" + this.apiKey;
    }

    private String attachAccount(String website, AlteningAlt account) {
        return website + "?token=" + this.apiKey + "&acctoken=" + account.getToken();
    }
}

