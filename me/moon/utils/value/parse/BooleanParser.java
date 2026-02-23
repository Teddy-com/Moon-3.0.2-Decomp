/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.value.parse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class BooleanParser {
    private static final Map<String, Boolean> PARSE = new HashMap<String, Boolean>(){
        {
            this.put("enable", true);
            this.put("true", true);
            this.put("on", true);
            this.put("1", true);
            this.put("disable", false);
            this.put("false", false);
            this.put("off", false);
            this.put("0", false);
        }
    };

    public static Optional<Boolean> parse(String input) {
        return Optional.ofNullable(PARSE.get(input.toLowerCase()));
    }
}

