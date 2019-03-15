package org.jiuwo.fastel.util;

import java.lang.reflect.Method;

import org.jiuwo.fastel.Executable;

/**
 * @author Steven Han
 */
public class MethodExecutable implements Executable {
    public Method[] methods;

    @Override
    public Object invoke(Object thiz, Object... args) throws Exception {
        nextMethod:
        for (Method method : methods) {
            Class<? extends Object>[] clazzs = method.getParameterTypes();
            if (clazzs.length == args.length) {
                for (int i = 0; i < clazzs.length; i++) {
                    Class<? extends Object> type = ReflectUtil.toWrapper(clazzs[i]);
                    Object value = args[i];
                    value = ParseUtil.parseToValue(value, type);
                    args[i] = value;
                    if (value != null) {
                        if (!type.isInstance(value)) {
                            continue nextMethod;
                        }
                    }
                }
            }
            return method.invoke(thiz, args);
        }
        return null;
    }


}
