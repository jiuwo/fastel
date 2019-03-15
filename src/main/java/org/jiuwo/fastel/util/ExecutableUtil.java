package org.jiuwo.fastel.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jiuwo.fastel.Executable;
import org.jiuwo.fastel.contract.enums.ExpressionEnum;
import org.jiuwo.fastel.impl.ExpressionNode;

import static org.jiuwo.fastel.contract.enums.ExpressionEnum.Token.BRACKET_BEGIN;
import static org.jiuwo.fastel.contract.enums.ExpressionEnum.Token.BRACKET_END;
import static org.jiuwo.fastel.contract.enums.ExpressionEnum.Token.OP_JOIN;
import static org.jiuwo.fastel.contract.enums.ExpressionEnum.Token.OP_PUT;
import static org.jiuwo.fastel.contract.enums.ExpressionEnum.Token.VALUE_MAP;

/**
 * @author Steven Han
 */
public class ExecutableUtil {

    private static Map<String, Executable> cachedExecutableMap = new ConcurrentHashMap<>();
    private static Map<Class<? extends Object>, Map<String, Executable>> classMethodMap = new ConcurrentHashMap<>();

    public static Executable getExecutable(final Class<? extends Object> clazz,
                                           final String name,
                                           int length) {
        String key = clazz.getName() + '.' + length + name;
        Executable result = cachedExecutableMap.get(key);
        if (result == null && !cachedExecutableMap.containsKey(key)) {
            ArrayList<Method> methods = new ArrayList<Method>();
            for (Method method : clazz.getMethods()) {
                if (method.getName().equals(name)
                        && (length < 0 || method.getParameterTypes().length == length)) {
                    methods.add(method);
                }
            }
            if (methods.size() > 0) {
                result = createProxy(methods
                        .toArray(new Method[methods.size()]));
                cachedExecutableMap.put(key, result);
            }
        }
        return result;
    }

    public static Executable createProxy(final Method... methods) {
        for (Method method : methods) {
            try {
                method.setAccessible(true);
            } catch (Exception e) {
            }
        }
        MethodExecutable inv = new MethodExecutable();
        inv.methods = methods;
        return inv;
    }

    public static Executable getExecutable(Object base,
                                           String name,
                                           Object[] args) {
        Map<String, Executable> mm = requireMethodMap(base.getClass());
        Executable executable = mm.get(name);
        if (executable == null && name instanceof String) {
            executable = getExecutable(base.getClass(), name,
                    args.length);
            if (executable == null && base instanceof Class<?>) {
                executable = getExecutable((Class<?>) base, name,
                        args.length);
            }
        }
        return executable;
    }

    private static Map<String, Executable> requireMethodMap(
            Class<? extends Object> clazz) {
        Map<String, Executable> methodMap = classMethodMap.get(clazz);
        if (methodMap == null) {
            methodMap = new HashMap<String, Executable>();
            {
                Class<?>[] interfaces = clazz.getInterfaces();
                for (Class<?> clazz2 : interfaces) {
                    Map<String, Executable> m2 = requireMethodMap(clazz2);
                    methodMap.putAll(m2);
                }
            }
            Class<? extends Object> clazz2 = clazz.getSuperclass();
            if (clazz2 != clazz) {
                if (clazz2 == Object.class && clazz.isArray()
                        && clazz != Object[].class) {
                    clazz2 = Object[].class;
                }
                if (clazz2 != null) {
                    Map<String, Executable> m2 = requireMethodMap(clazz2);
                    methodMap.putAll(m2);
                }
            }
        }
        return methodMap;
    }

    public static boolean isMapMethod(ArrayList<ExpressionNode> expressionNodeArrayList) {
        int i = expressionNodeArrayList.size() - 1;
        int depth = 0;
        for (; i >= 0; i--) {
            ExpressionNode expressionNode = expressionNodeArrayList.get(i);
            ExpressionEnum.Token token = expressionNode.getToken();
            if (depth == 0) {
                if (token.equals(OP_PUT) || token.equals(VALUE_MAP)) {
                    return true;
                } else if (token.equals(OP_JOIN)) {
                    return false;
                }
            }
            if (token.equals(BRACKET_BEGIN)) {
                depth--;
            } else if (token.equals(BRACKET_END)) {
                depth++;
            }
        }
        return false;
    }
}
