/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package me.moon.gui.account.system;

import com.google.gson.JsonObject;

public class Account {
    private String email;
    private String password;
    private String name;
    private boolean banned;

    public Account(String email, String password) {
        this.email = this.name = email;
        this.password = password;
    }

    public Account() {
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBanned() {
        return this.banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", this.email);
        jsonObject.addProperty("password", this.password);
        jsonObject.addProperty("name", this.name);
        jsonObject.addProperty("banned", Boolean.valueOf(this.banned));
        return jsonObject;
    }

    public void fromJson(JsonObject json) {
        this.email = json.get("email").getAsString();
        this.password = json.get("password").getAsString();
        this.name = json.get("name").getAsString();
        this.banned = json.get("banned").getAsBoolean();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean equals(Object obj) {
        return obj instanceof Account && this.email.equalsIgnoreCase(((Account)obj).email);
    }
}

