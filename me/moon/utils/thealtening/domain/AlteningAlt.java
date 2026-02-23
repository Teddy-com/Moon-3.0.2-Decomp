/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package me.moon.utils.thealtening.domain;

import com.google.gson.annotations.SerializedName;

public class AlteningAlt {
    @SerializedName(value="token")
    private String token;
    @SerializedName(value="username")
    private String username;
    @SerializedName(value="expires")
    private String expiryDate;
    @SerializedName(value="limit")
    private boolean isLimitReached;
    @SerializedName(value="skin")
    private String skinHash;

    public String getToken() {
        return this.token;
    }

    public String getUsername() {
        return this.username;
    }

    public String getExpiryDate() {
        return this.expiryDate;
    }

    public boolean isLimitReached() {
        return this.isLimitReached;
    }

    public String getSkinHash() {
        return this.skinHash;
    }
}

