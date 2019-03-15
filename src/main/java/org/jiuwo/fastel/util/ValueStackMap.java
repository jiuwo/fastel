package org.jiuwo.fastel.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Steven Han
 */
public class ValueStackMap implements Map<String, Object> {
    protected Object[] stack;

    public ValueStackMap(Object... stack) {
        this.stack = stack;
    }

    @Override
    public Object get(Object key) {
        int i = stack.length;
        while (i-- > 0) {
            Object context = stack[i];
            if (context instanceof Map<?, ?>) {
                Map<?, ?> contextMap = (Map<?, ?>) context;
                Object result = contextMap.get(key);
                if (result != null || contextMap.containsKey(key)) {
                    return result;
                }
            } else if (context != null) {
                Object result = ReflectUtil.getValue(context, key);
                Class<?> clazz = context.getClass();
                if (result != null
                        || ReflectUtil.getPropertyClass(clazz, key) != null) {
                    return result;
                }
                if (key instanceof String) {
                    return ExecutableUtil.getExecutable(clazz,
                            (String) key, -1);
                }
            }
        }
        return fallback(key);
    }

    protected Object fallback(Object key) {
        return null;
    }

    @Override
    public Object put(String key, Object value) {
        put(key, value, -1);
        return null;
    }

    public void put(Object key, Object value, int level) {
        if (level < 0) {
            level = level + stack.length;
        }
        ReflectUtil.setValue(stack[level], key, value);
    }

    @Override
    public void clear() {
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
    }

    @Override
    public Object remove(Object key) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Collection<Object> values() {
        return null;
    }

}

