/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals;

import me.moon.module.Module;
import me.moon.utils.value.impl.EnumValue;

public class IRC
extends Module {
    public EnumValue<Mode> mode = new EnumValue<Mode>("Mode", Mode.TABLIST);

    public IRC() {
        super("IRC", Module.Category.VISUALS, -1);
    }

    public static enum Mode {
        TABLIST("Tablist"),
        FONTRENDERER("FontRenderer");

        private final String name;

        private Mode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

