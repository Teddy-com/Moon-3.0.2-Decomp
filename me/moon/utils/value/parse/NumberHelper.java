/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.value.parse;

import me.moon.utils.MathUtils;
import me.moon.utils.value.impl.NumberValue;
import me.moon.utils.value.impl.RangedValue;

public class NumberHelper {
    public static void decrecement(NumberValue value) {
        if (value.getValue() instanceof Integer) {
            int inc = (Integer)value.getValue();
            inc -= ((Number)value.getInc()).intValue();
            if ((inc = (int)MathUtils.round(inc, 1)) >= ((Number)value.getMinimum()).intValue()) {
                value.setValue(inc);
            }
        } else if (value.getValue() instanceof Double) {
            double inc = (Double)value.getValue();
            inc -= ((Number)value.getInc()).doubleValue();
            if (!((inc = MathUtils.round(inc, 1)) < ((Number)value.getMinimum()).doubleValue())) {
                value.setValue(inc);
            }
        } else if (value.getValue() instanceof Float) {
            float inc = ((Float)value.getValue()).floatValue();
            inc -= ((Number)value.getInc()).floatValue();
            if (!((inc = (float)MathUtils.round(inc, 1)) < ((Number)value.getMinimum()).floatValue())) {
                value.setValue(Float.valueOf(inc));
            }
        } else if (value.getValue() instanceof Long) {
            long inc = (Long)value.getValue();
            inc -= ((Number)value.getInc()).longValue();
            if ((inc = (long)MathUtils.round(inc, 1)) >= ((Number)value.getMinimum()).longValue()) {
                value.setValue(inc);
            }
        }
    }

    public static void increment(NumberValue value) {
        if (value.getValue() instanceof Integer) {
            int inc = (Integer)value.getValue();
            inc = (int)MathUtils.round(inc + ((Number)value.getInc()).intValue(), 1);
            value.setValue(Math.min(inc, ((Number)value.getMaximum()).intValue()));
        } else if (value.getValue() instanceof Double) {
            double inc = (Double)value.getValue();
            inc = MathUtils.round(inc + ((Number)value.getInc()).doubleValue(), 1);
            value.setValue(Math.min(inc, ((Number)value.getMaximum()).doubleValue()));
        } else if (value.getValue() instanceof Float) {
            float inc = ((Float)value.getValue()).floatValue();
            inc = (float)MathUtils.round(inc + ((Number)value.getInc()).floatValue(), 1);
            value.setValue(Float.valueOf(Math.min(inc, ((Number)value.getMaximum()).floatValue())));
        } else if (value.getValue() instanceof Long) {
            long inc = (Long)value.getValue();
            inc = (long)MathUtils.round(inc + ((Number)value.getInc()).longValue(), 1);
            value.setValue(Math.min(inc, ((Number)value.getMaximum()).longValue()));
        }
    }

    public static void decrecementRanged(RangedValue value, boolean left) {
        if (left) {
            if (value.getLeftValue() instanceof Integer) {
                int inc = (Integer)value.getLeftValue();
                inc -= ((Number)value.getInc()).intValue();
                if ((inc = (int)MathUtils.round(inc, 1)) >= ((Number)value.getMinimum()).intValue()) {
                    value.setLeftValue(inc);
                }
            } else if (value.getLeftValue() instanceof Double) {
                double inc = (Double)value.getLeftValue();
                inc -= ((Number)value.getInc()).doubleValue();
                if (!((inc = MathUtils.round(inc, 1)) < ((Number)value.getMinimum()).doubleValue())) {
                    value.setLeftValue(inc);
                }
            } else if (value.getLeftValue() instanceof Float) {
                float inc = ((Float)value.getLeftValue()).floatValue();
                inc -= ((Number)value.getInc()).floatValue();
                if (!((inc = (float)MathUtils.round(inc, 1)) < ((Number)value.getMinimum()).floatValue())) {
                    value.setLeftValue(Float.valueOf(inc));
                }
            } else if (value.getLeftValue() instanceof Long) {
                long inc = (Long)value.getLeftValue();
                inc -= ((Number)value.getInc()).longValue();
                if ((inc = (long)MathUtils.round(inc, 1)) >= ((Number)value.getMinimum()).longValue()) {
                    value.setLeftValue(inc);
                }
            }
        } else if (value.getRightValue() instanceof Integer) {
            int inc = (Integer)value.getRightValue();
            inc -= ((Number)value.getInc()).intValue();
            if ((inc = (int)MathUtils.round(inc, 1)) >= ((Number)value.getLeftValue()).intValue()) {
                value.setRightValue(inc);
            }
        } else if (value.getRightValue() instanceof Double) {
            double inc = (Double)value.getRightValue();
            inc -= ((Number)value.getInc()).doubleValue();
            if (!((inc = MathUtils.round(inc, 1)) < ((Number)value.getLeftValue()).doubleValue())) {
                value.setRightValue(inc);
            }
        } else if (value.getRightValue() instanceof Float) {
            float inc = ((Float)value.getRightValue()).floatValue();
            inc -= ((Number)value.getInc()).floatValue();
            if (!((inc = (float)MathUtils.round(inc, 1)) < ((Number)value.getLeftValue()).floatValue())) {
                value.setRightValue(Float.valueOf(inc));
            }
        } else if (value.getRightValue() instanceof Long) {
            long inc = (Long)value.getRightValue();
            inc -= ((Number)value.getInc()).longValue();
            if ((inc = (long)MathUtils.round(inc, 1)) >= ((Number)value.getLeftValue()).longValue()) {
                value.setRightValue(inc);
            }
        }
    }

    public static void increcementRanged(RangedValue value, boolean left) {
        if (left) {
            if (value.getLeftValue() instanceof Integer) {
                int inc = (Integer)value.getLeftValue();
                inc += ((Number)value.getInc()).intValue();
                if ((inc = (int)MathUtils.round(inc, 1)) <= ((Number)value.getRightValue()).intValue()) {
                    value.setLeftValue(inc);
                }
            } else if (value.getLeftValue() instanceof Double) {
                double inc = (Double)value.getLeftValue();
                inc += ((Number)value.getInc()).doubleValue();
                if (!((inc = MathUtils.round(inc, 1)) > ((Number)value.getRightValue()).doubleValue())) {
                    value.setLeftValue(inc);
                }
            } else if (value.getLeftValue() instanceof Float) {
                float inc = ((Float)value.getLeftValue()).floatValue();
                inc += ((Number)value.getInc()).floatValue();
                if (!((inc = (float)MathUtils.round(inc, 1)) > ((Number)value.getRightValue()).floatValue())) {
                    value.setLeftValue(Float.valueOf(inc));
                }
            } else if (value.getLeftValue() instanceof Long) {
                long inc = (Long)value.getLeftValue();
                inc += ((Number)value.getInc()).longValue();
                if ((inc = (long)MathUtils.round(inc, 1)) <= ((Number)value.getRightValue()).longValue()) {
                    value.setLeftValue(inc);
                }
            }
        } else if (value.getRightValue() instanceof Integer) {
            int inc = (Integer)value.getRightValue();
            inc += ((Number)value.getInc()).intValue();
            if ((inc = (int)MathUtils.round(inc, 1)) <= ((Number)value.getMaximum()).intValue()) {
                value.setRightValue(inc);
            }
        } else if (value.getRightValue() instanceof Double) {
            double inc = (Double)value.getRightValue();
            inc += ((Number)value.getInc()).doubleValue();
            if (!((inc = MathUtils.round(inc, 1)) > ((Number)value.getMaximum()).doubleValue())) {
                value.setRightValue(inc);
            }
        } else if (value.getRightValue() instanceof Float) {
            float inc = ((Float)value.getRightValue()).floatValue();
            inc += ((Number)value.getInc()).floatValue();
            if (!((inc = (float)MathUtils.round(inc, 1)) > ((Number)value.getMaximum()).floatValue())) {
                value.setRightValue(Float.valueOf(inc));
            }
        } else if (value.getRightValue() instanceof Long) {
            long inc = (Long)value.getRightValue();
            inc += ((Number)value.getInc()).longValue();
            if ((inc = (long)MathUtils.round(inc, 1)) <= ((Number)value.getMaximum()).longValue()) {
                value.setRightValue(inc);
            }
        }
    }
}

