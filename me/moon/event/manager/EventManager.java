/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.manager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.moon.event.Handler;
import me.moon.event.manager.IManager;
import me.moon.event.manager.MethodAccessor;

public class EventManager<T>
implements IManager<T> {
    private final Map<Class<?>, List<MethodAccessor>> registeredListeners = new HashMap();

    @Override
    public void registerListener(Object listener) {
        Method[] methods = listener.getClass().getDeclaredMethods();
        Map<Class<?>, List<MethodAccessor>> tempListeners = this.registeredListeners;
        for (int i = 0; i < methods.length; ++i) {
            Method method = methods[i];
            Handler handler = method.getAnnotation(Handler.class);
            if (handler == null) continue;
            Class<?>[] classParameters = method.getParameterTypes();
            int parameterLength = classParameters.length;
            Class<?> handlerClass = handler.value();
            if (parameterLength > 1 || parameterLength == 1 && handlerClass != classParameters[0]) continue;
            MethodAccessor tempAccessor = new MethodAccessor(listener, method);
            if (tempListeners.containsKey(handlerClass)) {
                tempListeners.get(handlerClass).add(tempAccessor);
                continue;
            }
            EventManager.accessorCache[0] = tempAccessor;
            tempListeners.put(handlerClass, new ArrayList<MethodAccessor>(Arrays.asList(accessorCache)));
        }
    }

    @Override
    public void unregisterListener(Object listener) {
        for (List<MethodAccessor> accessors : this.registeredListeners.values()) {
            accessors.removeIf(methodAccessor -> methodAccessor.object == listener);
        }
    }

    @Override
    public void fireEvent(T event) {
        List<MethodAccessor> methodAccessors = this.registeredListeners.get(event.getClass());
        if (methodAccessors != null) {
            for (int i = 0; i < methodAccessors.size(); ++i) {
                try {
                    MethodAccessor methodAccessor = methodAccessors.get(i);
                    Method method = methodAccessor.method;
                    Object object = methodAccessor.object;
                    if (methodAccessor.parameterZero) {
                        method.invoke(object, new Object[0]);
                        continue;
                    }
                    method.invoke(object, event);
                    continue;
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
}

