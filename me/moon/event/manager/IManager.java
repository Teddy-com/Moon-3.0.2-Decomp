/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.manager;

import me.moon.event.manager.MethodAccessor;

public interface IManager<T> {
    public static final MethodAccessor[] accessorCache = new MethodAccessor[1];

    public void registerListener(Object var1);

    public void unregisterListener(Object var1);

    public void fireEvent(T var1);
}

