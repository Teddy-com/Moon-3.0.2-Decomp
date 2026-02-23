/*
 * Decompiled with CFR 0.152.
 */
package me.moon.gui.changelog;

import me.moon.gui.changelog.ChangeType;

public class ChangeComponent {
    public String change;
    public ChangeType changeType = ChangeType.ADDED;

    public void setChange(String change) {
        this.change = change;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public ChangeType getChangeType() {
        return this.changeType;
    }

    public String getChange() {
        return this.change;
    }

    public String getFormattedText() {
        switch (this.changeType) {
            case ADDED: {
                return "\u00a7a" + this.change;
            }
            case FIXED: {
                return "\u00a76" + this.change;
            }
            case REMOVED: {
                return "\u00a7c" + this.change;
            }
            case INFORMATION: {
                return "\u00a79" + this.change;
            }
        }
        return this.change;
    }
}

