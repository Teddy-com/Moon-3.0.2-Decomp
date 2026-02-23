/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.manager;

import java.lang.reflect.Method;

final class MethodAccessor {
    final Object object;
    final Method method;
    final boolean parameterZero;

    MethodAccessor(Object obj, Method mth) {
        this.object = obj;
        this.method = mth;
        if (!this.method.isAccessible()) {
            this.method.setAccessible(true);
        }
        this.parameterZero = this.method.getParameterCount() == 0;
    }
}

