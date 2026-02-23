/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.moon.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import me.moon.Moon;
import me.moon.module.impl.visuals.HUD;
import me.moon.utils.MathUtils;
import me.moon.utils.font.Fonts;
import me.moon.utils.font.MCFontRenderer;
import me.moon.utils.game.AnimationUtil;
import me.moon.utils.game.TimerUtil;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.Value;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.ColorValue;
import me.moon.utils.value.impl.EnumValue;
import me.moon.utils.value.impl.FontValue;
import me.moon.utils.value.impl.NumberValue;
import me.moon.utils.value.impl.RangedValue;
import net.minecraft.client.Minecraft;

public class Module {
    private final String label;
    private String renderLabel;
    private String suffix;
    private String description;
    private boolean enabled;
    private boolean hidden;
    private int color;
    private int keyBind;
    private final Category category;
    protected final Minecraft mc = Minecraft.getMinecraft();
    private final List<Value> values = new ArrayList<Value>();
    private final AnimationUtil transUtil = new AnimationUtil(0.0, 0.0);
    private final AnimationUtil animationUtil2 = new AnimationUtil(0.0, 0.0);
    private final AnimationUtil animationUtil3 = new AnimationUtil(0.0, 0.0);
    private final AnimationUtil customAnimation = new AnimationUtil(0.0, 0.0);
    private final TimerUtil timerUtil = new TimerUtil();
    public float offset = 0.0f;
    private boolean shouldAnimate;

    public Module(String label, Category category, int color) {
        this.label = label;
        this.category = category;
        this.color = color;
    }

    public Value findValue(String term) {
        for (Value value : this.values) {
            if (!value.getLabel().equalsIgnoreCase(term)) continue;
            return value;
        }
        return null;
    }

    public List<Value> getValues() {
        return this.values;
    }

    public float getLongestValueInModule() {
        float width = RenderUtil.getStringWidth(this.getValues().get(0).getLabel() + ": " + this.getValues().get(0).getValue(), false, Fonts.arialFont);
        for (Value value : this.getValues()) {
            if (value instanceof NumberValue) {
                StringBuilder stringBuilder = new StringBuilder();
                if (RenderUtil.getStringWidth(stringBuilder.append(value.getLabel()).append(": ").append(((NumberValue)value).getMaximum()).toString(), false, Fonts.arialFont) > width) {
                    width = RenderUtil.getStringWidth(value.getLabel() + ": " + ((NumberValue)value).getMaximum(), false, Fonts.arialFont);
                } else {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    if (RenderUtil.getStringWidth(stringBuilder2.append(value.getLabel()).append(": ").append(((NumberValue)value).getMinimum()).toString(), false, Fonts.arialFont) > width) {
                        width = RenderUtil.getStringWidth(value.getLabel() + ": " + ((NumberValue)value).getMinimum(), false, Fonts.arialFont);
                    }
                }
            }
            if (value instanceof RangedValue) {
                StringBuilder stringBuilder = new StringBuilder();
                if (RenderUtil.getStringWidth(stringBuilder.append(value.getLabel()).append(": ").append(((RangedValue)value).getMaximum()).append(" - ").append(((RangedValue)value).getMaximum()).toString(), false, Fonts.arialFont) > width) {
                    width = RenderUtil.getStringWidth(value.getLabel() + ": " + ((RangedValue)value).getMaximum() + " - " + ((RangedValue)value).getMaximum(), false, Fonts.arialFont);
                    continue;
                }
                StringBuilder stringBuilder3 = new StringBuilder();
                if (!(RenderUtil.getStringWidth(stringBuilder3.append(value.getLabel()).append(": ").append(((RangedValue)value).getMaximum()).append(" - ").append(((RangedValue)value).getMaximum()).toString(), false, Fonts.arialFont) > width)) continue;
                width = RenderUtil.getStringWidth(value.getLabel() + ": " + ((RangedValue)value).getMaximum() + " - " + ((RangedValue)value).getMaximum(), false, Fonts.arialFont);
                continue;
            }
            if (value instanceof EnumValue) {
                for (Enum enoom : ((EnumValue)value).getConstants()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (!(RenderUtil.getStringWidth(stringBuilder.append(value.getLabel()).append(": ").append(enoom.name()).toString(), false, Fonts.arialFont) > width)) continue;
                    width = RenderUtil.getStringWidth(value.getLabel() + ": " + enoom.name(), false, Fonts.arialFont);
                }
                continue;
            }
            if (!(value instanceof BooleanValue)) continue;
            StringBuilder stringBuilder = new StringBuilder();
            if (!(RenderUtil.getStringWidth(stringBuilder.append(value.getLabel()).append(": ").append(value.getValue()).toString(), false, Fonts.arialFont) > width)) continue;
            width = RenderUtil.getStringWidth(value.getLabel() + ": " + value.getValue(), false, Fonts.arialFont);
        }
        return width;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setRenderLabel(String renderLabel) {
        this.renderLabel = renderLabel;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (Float.isNaN(this.offset)) {
            this.offset = 0.0f;
        }
        if (enabled) {
            Moon.INSTANCE.eventBus.registerListener(this);
            this.shouldAnimate = true;
            this.onEnable();
            if (this.mc.thePlayer != null) {
                StringBuilder moduleString = new StringBuilder(this.getRenderLabel() != null ? this.getRenderLabel() : this.getLabel());
                if (this.getSuffix() != null) {
                    moduleString.append(HUD.arrayListModes.getValue() == HUD.ArrayListMode.FLUX ? ChatFormatting.WHITE : ChatFormatting.GRAY).append(" ").append(this.suffix);
                }
                this.offset += (float)(Fonts.astolfoFont.getStringWidth(moduleString.toString()) + 10);
            }
        } else {
            Moon.INSTANCE.eventBus.unregisterListener(this);
            this.onDisable();
            if (this.mc.thePlayer != null) {
                StringBuilder moduleString = new StringBuilder(this.getRenderLabel() != null ? this.getRenderLabel() : this.getLabel());
                if (this.getSuffix() != null) {
                    moduleString.append(HUD.arrayListModes.getValue() == HUD.ArrayListMode.FLUX ? ChatFormatting.WHITE : ChatFormatting.GRAY).append(" ").append(this.suffix);
                }
                this.offset -= (float)(Fonts.astolfoFont.getStringWidth(moduleString.toString()) + 10);
            }
        }
    }

    public void toggle() {
        this.setEnabled(!this.isEnabled());
    }

    public String getLabel() {
        return this.label;
    }

    public String getRenderLabel() {
        return this.renderLabel;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public int getColor() {
        return this.color;
    }

    public int getKeyBind() {
        return this.keyBind;
    }

    public Category getCategory() {
        return this.category;
    }

    public void save(JsonObject directory, boolean key) {
        if (key) {
            directory.addProperty("key", (Number)this.getKeyBind());
        }
        directory.addProperty("enabled", Boolean.valueOf(this.isEnabled()));
        directory.addProperty("hidden", Boolean.valueOf(this.isHidden()));
        this.values.forEach(val -> {
            if (val instanceof RangedValue) {
                directory.addProperty(val.getLabel(), ((RangedValue)val).getLeftValue() + "-" + ((RangedValue)val).getRightValue());
            } else if (val instanceof FontValue) {
                directory.addProperty(val.getLabel(), ((MCFontRenderer)((FontValue)val).getValue()).getFont().getName().replace(" ", "_") + "-" + ((MCFontRenderer)((FontValue)val).getValue()).getFont().getStyle() + "-" + ((MCFontRenderer)((FontValue)val).getValue()).getFont().getSize() + "-" + ((MCFontRenderer)((FontValue)val).getValue()).isAntiAlias() + "-" + ((MCFontRenderer)((FontValue)val).getValue()).isFractionalMetrics());
            } else {
                directory.addProperty(val.getLabel(), val.getValue().toString());
            }
        });
    }

    public void load(JsonObject directory, boolean key) {
        directory.entrySet().forEach(data -> {
            switch ((String)data.getKey()) {
                case "name": {
                    return;
                }
                case "key": {
                    if (key) {
                        this.setKeyBind(((JsonElement)data.getValue()).getAsInt());
                    }
                    return;
                }
                case "enabled": {
                    if (!(this.isEnabled() && ((JsonElement)data.getValue()).getAsBoolean() || !this.isEnabled() && !((JsonElement)data.getValue()).getAsBoolean())) {
                        this.setEnabled(((JsonElement)data.getValue()).getAsBoolean());
                    }
                    return;
                }
                case "hidden": {
                    this.setHidden(((JsonElement)data.getValue()).getAsBoolean());
                    return;
                }
            }
            Value val = this.findValue((String)data.getKey());
            if (val != null) {
                if (val instanceof RangedValue) {
                    String[] strings = ((JsonElement)data.getValue()).getAsString().split("-");
                    if (((RangedValue)val).getInc() instanceof Float) {
                        ((RangedValue)val).setLeftValue(Float.valueOf(Float.parseFloat(strings[0])));
                        ((RangedValue)val).setRightValue(Float.valueOf(Float.parseFloat(strings[1])));
                    }
                    if (((RangedValue)val).getInc() instanceof Integer) {
                        ((RangedValue)val).setLeftValue(Integer.parseInt(strings[0]));
                        ((RangedValue)val).setRightValue(Integer.parseInt(strings[1]));
                    }
                    if (((RangedValue)val).getInc() instanceof Double) {
                        ((RangedValue)val).setLeftValue(Double.parseDouble(strings[0]));
                        ((RangedValue)val).setRightValue(Double.parseDouble(strings[1]));
                    }
                    if (((RangedValue)val).getInc() instanceof Byte) {
                        ((RangedValue)val).setLeftValue(Byte.parseByte(strings[0]));
                        ((RangedValue)val).setRightValue(Byte.parseByte(strings[1]));
                    }
                    if (((RangedValue)val).getInc() instanceof Short) {
                        ((RangedValue)val).setLeftValue(Short.parseShort(strings[0]));
                        ((RangedValue)val).setRightValue(Short.parseShort(strings[1]));
                    }
                    if (((RangedValue)val).getInc() instanceof Long) {
                        ((RangedValue)val).setLeftValue(Long.parseLong(strings[0]));
                        ((RangedValue)val).setRightValue(Long.parseLong(strings[1]));
                    }
                } else if (val instanceof FontValue) {
                    FontValue hudFont = (FontValue)val;
                    String[] strings = ((JsonElement)data.getValue()).getAsString().split("-");
                    int style = (int)MathUtils.clamp(Integer.parseInt(strings[1]), 2.0f, 0.0f);
                    int size = Integer.parseInt(strings[2]);
                    boolean aa = Boolean.parseBoolean(strings[3]);
                    boolean fractionalmetrics = Boolean.parseBoolean(strings[4]);
                    MCFontRenderer mcFontRenderer = new MCFontRenderer(new Font(strings[0].replace("_", " "), style, size), aa, fractionalmetrics);
                    hudFont.setValue(mcFontRenderer);
                } else if (val instanceof ColorValue) {
                    val.setValue(((JsonElement)data.getValue()).getAsInt());
                } else {
                    val.setValue(((JsonElement)data.getValue()).getAsString());
                }
            }
        });
    }

    public AnimationUtil getTransUtil() {
        return this.transUtil;
    }

    public boolean isShouldAnimate() {
        return this.shouldAnimate;
    }

    public void setShouldAnimate(boolean shouldAnimate) {
        this.shouldAnimate = shouldAnimate;
    }

    public AnimationUtil getAnimationUtil2() {
        return this.animationUtil2;
    }

    public AnimationUtil getAnimationUtil3() {
        return this.animationUtil3;
    }

    public AnimationUtil getCustomAnimation() {
        return this.customAnimation;
    }

    public TimerUtil getTimerUtil() {
        return this.timerUtil;
    }

    public static enum Category {
        COMBAT("a", "Combat"),
        MOVEMENT("b", "Movement"),
        PLAYER("c", "Player"),
        EXPLOITS("d", "Exploits"),
        OTHER("e", "Other"),
        GHOST("f", "Ghost"),
        VISUALS("g", "Visuals");

        private final String character;
        private final String name;
        public int alpha = 100;

        private Category(String character, String name) {
            this.character = character;
            this.name = name;
        }

        public String getCharacter() {
            return this.character;
        }

        public String getName() {
            return this.name;
        }

        public int getAlpha() {
            return this.alpha;
        }
    }
}

