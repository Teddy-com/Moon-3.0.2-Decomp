/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package me.moon.utils.thealtening.domain;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName(value="username")
    private String username;
    @SerializedName(value="premium")
    private boolean premium;
    @SerializedName(value="premium_name")
    private String premiumName;
    @SerializedName(value="expires")
    private String expiryDate;

    public String getUsername() {
        return this.username;
    }

    public boolean isPremium() {
        return this.premium;
    }

    public String getPremiumName() {
        return this.premiumName;
    }

    public String getExpiryDate() {
        return this.expiryDate;
    }
}

