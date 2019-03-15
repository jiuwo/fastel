package org.jiuwo.fastel.util;

import java.util.Collections;
import java.util.Map;

import org.jiuwo.fastel.Executable;
import org.jiuwo.fastel.exception.FastElException;

/**
 * @author Steven Han
 */
public class ValueStackUtil {

    public static <T> T wrapAsContext(Object context) {
        Map<String, Object> valueStack;
        if (context instanceof Map) {
            valueStack = (Map<String, Object>) context;
        } else if (context == null) {
            valueStack = new ValueStackMap(Collections.emptyMap());
        } else {
            valueStack = new ValueStackMap(context);
        }
        return (T) valueStack;
    }

    public static Object getValueStack(Map<String, Object> vs,
                                       Map<Object, Object> jsNodeList,
                                       Object key) {
        Object o = vs.get(key);
        if (o == null) {
            return jsNodeList.get(key);
        }
        return o;
    }

    public static Object invoke(Map<String, Object> vs, Object arg1, Object[] arguments) {
        try {
            Object thiz;
            Executable executable = null;
            if (arg1 instanceof ReferenceUtil) {
                ReferenceUtil pv = (ReferenceUtil) arg1;
                thiz = pv.getBase();
                Object name = pv.getName();
                executable = ExecutableUtil.getExecutable(thiz, String.valueOf(name), arguments);
                if (executable == null) {
                    arg1 = pv.getValue();
                } else {
                    return executable.invoke(thiz, arguments);
                }
            } else {
                thiz = vs;
            }
            if (executable == null) {
                if (arg1 instanceof Executable) {
                    executable = (Executable) arg1;
                } else if ((arg1 instanceof java.lang.reflect.Method)) {
                    executable = ExecutableUtil.createProxy((java.lang.reflect.Method) arg1);
                } else {
                    throw new FastElException("对象不是有效函数:" + arg1);
                }
            }
            return executable.invoke(thiz, arguments);
        } catch (Exception e) {
            throw new FastElException("方法调用失败:" + arg1, e);
        }
    }
}
