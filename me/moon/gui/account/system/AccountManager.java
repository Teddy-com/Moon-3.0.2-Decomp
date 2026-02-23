/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 */
package me.moon.gui.account.system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import me.moon.gui.account.system.Account;

public class AccountManager {
    private final ArrayList<Account> accounts = new ArrayList();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final File altsFile;
    private String alteningKey;
    private String lastAlteningAlt;
    private Account lastAlt;

    public AccountManager(File parent) {
        this.altsFile = new File(parent.toString() + File.separator + "alts.json");
        this.load();
    }

    public void save() {
        if (this.altsFile == null) {
            return;
        }
        try {
            if (!this.altsFile.exists() && !this.altsFile.createNewFile()) {
                throw new Exception("Failed to create alts.json");
            }
            PrintWriter printWriter = new PrintWriter(this.altsFile);
            printWriter.write(this.gson.toJson((JsonElement)this.toJson()));
            printWriter.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        if (!this.altsFile.exists()) {
            this.save();
            return;
        }
        try {
            this.fromJson(new JsonParser().parse((Reader)new FileReader(this.altsFile)).getAsJsonObject());
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        this.getAccounts().forEach(account -> jsonArray.add((JsonElement)account.toJson()));
        if (this.alteningKey != null) {
            jsonObject.addProperty("altening", this.alteningKey);
        }
        if (this.lastAlteningAlt != null) {
            jsonObject.addProperty("alteningAlt", this.lastAlteningAlt);
        }
        if (this.lastAlt != null) {
            jsonObject.add("lastalt", (JsonElement)this.lastAlt.toJson());
        }
        jsonObject.add("accounts", (JsonElement)jsonArray);
        return jsonObject;
    }

    public void fromJson(JsonObject json) {
        if (json.has("altening")) {
            this.alteningKey = json.get("altening").getAsString();
        }
        if (json.has("alteningAlt")) {
            this.lastAlteningAlt = json.get("alteningAlt").getAsString();
        }
        if (json.has("lastalt")) {
            Account account = new Account();
            account.fromJson(json.get("lastalt").getAsJsonObject());
            this.lastAlt = account;
        }
        json.get("accounts").getAsJsonArray().forEach(jsonElement -> {
            JsonObject jsonObject = (JsonObject)jsonElement;
            Account account = new Account();
            account.fromJson(jsonObject);
            if (this.getAccounts().contains(account)) {
                return;
            }
            this.getAccounts().add(account);
        });
    }

    public void remove(String username) {
        this.getAccounts().removeIf(account -> account.getName().equalsIgnoreCase(username));
    }

    public Account getAccountByEmail(String email) {
        for (Account account : this.getAccounts()) {
            if (!account.getEmail().equalsIgnoreCase(email)) continue;
            return account;
        }
        return null;
    }

    public String getLastAlteningAlt() {
        return this.lastAlteningAlt;
    }

    public void setLastAlteningAlt(String lastAlteningAlt) {
        this.lastAlteningAlt = lastAlteningAlt;
    }

    public String getAlteningKey() {
        return this.alteningKey;
    }

    public void setAlteningKey(String alteningKey) {
        this.alteningKey = alteningKey;
    }

    public Account getLastAlt() {
        return this.lastAlt;
    }

    public void setLastAlt(Account lastAlt) {
        this.lastAlt = lastAlt;
    }

    public ArrayList<Account> getAccounts() {
        return this.accounts;
    }
}

