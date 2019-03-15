package org.jiuwo.fastel.util;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import org.jiuwo.fastel.constant.SysConstant;
import org.jiuwo.fastel.contract.enums.ExpressionEnum;

/**
 * @author Steven Han
 */
public class ObjectUtil {

    /**
     * 有指定参数则返回指定参数(包括null),无则返回defaultValue
     *
     * @param args         参数
     * @param index        索引
     * @param defaultValue 默认值
     * @return 数字
     */
    public static Number getNumberArg(Object[] args, int index, Number defaultValue) {
        Object value = getArg(args, index, defaultValue);
        return value == null ? null : ParseUtil.parseToNumber(value);
    }

    public static Object getArg(Object[] args, int index, Object defaultValue) {
        if (index >= 0 && index < args.length) {
            return args[index];
        } else {
            return defaultValue;
        }
    }

    public static String getStringArg(Object[] args, int index, String defaultValue) {
        Object value = getArg(args, index, defaultValue);
        return value == null ? null : ParseUtil.parseToString(value);
    }

    public static boolean isEquals(Object arg1, Object arg2, boolean strict) {
        if (arg1 == null || arg2 == null) {
            return arg1 == arg2;
        }
        if (arg1 instanceof Number && arg2 instanceof Number) {
            return NumberUtil.compare((Number) arg1, (Number) arg2, ExpressionEnum.Token.OP_EQ);
        } else if (arg1.equals(arg2)) {
            return true;
        }
        if (strict) {
            if (arg1 instanceof String && arg2 instanceof String) {
                return false;
            }
            if (arg1 instanceof Boolean && arg2 instanceof Boolean) {
                return false;
            }
        }
        arg1 = ParseUtil.parseToPrimitive(arg1, Number.class);
        arg2 = ParseUtil.parseToPrimitive(arg2, Number.class);
        if (arg1 instanceof String && arg2 instanceof String) {
            return arg1.equals(arg2);
        }
        Number n1 = ParseUtil.parseToNumber(arg1);
        Number n2 = ParseUtil.parseToNumber(arg2);
        return NumberUtil.compare(n1, n2, ExpressionEnum.Token.OP_EQ);
    }

    /**
     * @param arg1  参数1
     * @param arg2  参数2
     * @param token 类型
     * @return 结果
     */
    public static boolean compare(Object arg1, Object arg2, ExpressionEnum.Token token) {
        if (arg1 == null) {
            if (arg2 == null) {
                return token.equals(ExpressionEnum.Token.OP_GTEQ) || token.equals(ExpressionEnum.Token.OP_LTEQ);
            }
        } else if (arg1 instanceof Number && arg2 instanceof Number) {
            return NumberUtil.compare((Number) arg1, (Number) arg2, token);
        } else if (arg1.equals(arg2)) {
            return token.equals(ExpressionEnum.Token.OP_GTEQ) || token.equals(ExpressionEnum.Token.OP_LTEQ);
        }
        arg1 = ParseUtil.parseToPrimitive(arg1, Number.class);
        arg2 = ParseUtil.parseToPrimitive(arg2, Number.class);
        if (arg1 instanceof String && arg2 instanceof String) {
            return NumberUtil.compare(((String) arg1).compareTo((String) arg2), 0, token);
        }
        Number n1 = ParseUtil.parseToNumber(arg1);
        Number n2 = ParseUtil.parseToNumber(arg2);
        return NumberUtil.compare(n1, n2, token);
    }

    public static boolean in(Object key, Object object) {
        int len = -1;
        Class<?> clazz = object.getClass();
        if (object instanceof List<?>) {
            len = ((List<?>) object).size();
        } else if (clazz.isArray()) {
            len = Array.getLength(object);
        }
        if (len >= 0) {
            if (SysConstant.SYS_LENGTH.equals(key)) {
                return true;
            }
            Number n = ParseUtil.parseToNumber(key);
            int i = n.intValue();
            if (i >= 0 && i <= len) {
                return i == n.floatValue();
            }
            return false;
        }
        String skey = ParseUtil.parseToString(key);
        if (object instanceof Map<?, ?>) {
            return ((Map<?, ?>) object).containsKey(skey);
        }

        return ReflectUtil.getPropertyClass(clazz, skey) != null;
    }
}
