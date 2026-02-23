/*
 * Decompiled with CFR 0.152.
 */
package me.moon.gui.config;

public class ConfigObject {
    private String creator;
    private String configName;
    private String lastUpdateDate;

    public ConfigObject(String creator, String configName, String lastUpdateDate) {
        this.creator = creator;
        this.configName = configName;
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getConfigName() {
        return this.configName;
    }

    public String getCreator() {
        return this.creator;
    }

    public String getLastUpdateDate() {
        return this.lastUpdateDate;
    }
}

