/*
 * Decompiled with CFR 0.152.
 */
package me.moon.module.impl.visuals;

import java.awt.Color;
import me.moon.event.Handler;
import me.moon.event.impl.render.CrosshairEvent;
import me.moon.event.impl.render.Render2DEvent;
import me.moon.module.Module;
import me.moon.utils.render.RenderUtil;
import me.moon.utils.value.impl.BooleanValue;
import me.moon.utils.value.impl.ColorValue;
import me.moon.utils.value.impl.NumberValue;

public class Crosshair
extends Module {
    private final BooleanValue dynamic = new BooleanValue("Dynamic", true);
    private final NumberValue<Double> width = new NumberValue<Double>("Width", 1.0, 0.5, 10.0, 0.5);
    private final NumberValue<Double> gap = new NumberValue<Double>("Gap", 3.0, 0.5, 10.0, 0.5);
    private final NumberValue<Double> length = new NumberValue<Double>("Length", 7.0, 0.5, 100.0, 0.5);
    private final NumberValue<Double> dynamicgap = new NumberValue<Double>("DynamicGap", 1.5, 0.5, 10.0, 0.5);
    private final ColorValue colorValue = new ColorValue("Color", new Color(255, 0, 0).getRGB());
    private final BooleanValue rainbow = new BooleanValue("Rainbow", false);
    private final BooleanValue staticRainbow = new BooleanValue("Static Rainbow", false);

    public Crosshair() {
        super("Crosshair", Module.Category.VISUALS, 0);
        this.setHidden(true);
    }

    @Handler(value=CrosshairEvent.class)
    public void onCrosshair(CrosshairEvent event) {
        event.setCancelled(true);
    }

    @Handler(value=Render2DEvent.class)
    public void onRender2D(Render2DEvent event) {
        Color rai = new Color(RenderUtil.getRainbow(6000, -15, 0.85f));
        int color = this.staticRainbow.isEnabled() ? this.color(2, 100) : (this.rainbow.isEnabled() ? new Color(rai.getRed(), rai.getGreen(), rai.getBlue(), new Color((Integer)this.colorValue.getValue()).getAlpha()).getRGB() : ((Integer)this.colorValue.getValue()).intValue());
        double middlex = event.getScaledResolution().getScaledWidth() >> 1;
        double middley = event.getScaledResolution().getScaledHeight() >> 1;
        RenderUtil.drawBordered(middlex - (Double)this.width.getValue(), middley - ((Double)this.gap.getValue() + (Double)this.length.getValue()) - (this.mc.thePlayer.isMoving() && this.dynamic.isEnabled() ? (Double)this.dynamicgap.getValue() : 0.0), middlex + (Double)this.width.getValue(), middley - (Double)this.gap.getValue() - (this.mc.thePlayer.isMoving() && this.dynamic.isEnabled() ? (Double)this.dynamicgap.getValue() : 0.0), 0.5, color, -16777216);
        RenderUtil.drawBordered(middlex - (Double)this.width.getValue(), middley + (Double)this.gap.getValue() + (this.mc.thePlayer.isMoving() && this.dynamic.isEnabled() ? (Double)this.dynamicgap.getValue() : 0.0), middlex + (Double)this.width.getValue(), middley + ((Double)this.gap.getValue() + (Double)this.length.getValue()) + (this.mc.thePlayer.isMoving() && this.dynamic.isEnabled() ? (Double)this.dynamicgap.getValue() : 0.0), 0.5, color, -16777216);
        RenderUtil.drawBordered(middlex - (Double)this.width.getValue(), middley - (Double)this.width.getValue(), middlex + (Double)this.width.getValue(), middley + (Double)this.width.getValue(), 0.5, color, -16777216);
        RenderUtil.drawBordered(middlex - ((Double)this.gap.getValue() + (Double)this.length.getValue()) - (this.mc.thePlayer.isMoving() && this.dynamic.isEnabled() ? (Double)this.dynamicgap.getValue() : 0.0), middley - (Double)this.width.getValue(), middlex - (Double)this.gap.getValue() - (this.mc.thePlayer.isMoving() && this.dynamic.isEnabled() ? (Double)this.dynamicgap.getValue() : 0.0), middley + (Double)this.width.getValue(), 0.5, color, -16777216);
        RenderUtil.drawBordered(middlex + (Double)this.gap.getValue() + (this.mc.thePlayer.isMoving() && this.dynamic.isEnabled() ? (Double)this.dynamicgap.getValue() : 0.0), middley - (Double)this.width.getValue(), middlex + ((Double)this.gap.getValue() + (Double)this.length.getValue()) + (this.mc.thePlayer.isMoving() && this.dynamic.isEnabled() ? (Double)this.dynamicgap.getValue() : 0.0), middley + (Double)this.width.getValue(), 0.5, color, -16777216);
    }

    public int color(int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(new Color((Integer)this.colorValue.getValue()).getRed(), new Color((Integer)this.colorValue.getValue()).getGreen(), new Color((Integer)this.colorValue.getValue()).getBlue(), hsb);
        float brightness = Math.abs((this.getOffset() + (float)index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.4f + 0.4f * brightness;
        hsb[2] = brightness % 1.0f;
        Color clr = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
        return new Color(clr.getRed(), clr.getGreen(), clr.getBlue(), new Color((Integer)this.colorValue.getValue()).getAlpha()).getRGB();
    }

    private float getOffset() {
        return (float)(System.currentTimeMillis() % 2000L) / 1000.0f;
    }
}

