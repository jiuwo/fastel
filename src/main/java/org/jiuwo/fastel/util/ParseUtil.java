package org.jiuwo.fastel.util;

import java.util.Date;

import org.jiuwo.fastel.constant.CharConstant;
import org.jiuwo.fastel.constant.SysConstant;

/**
 * @author Steven Han
 */
public class ParseUtil {

    private static Number parseToNumber(String text, int radix) {
        try {
            return Integer.parseInt(text, radix);
        } catch (Exception e) {
            return Long.parseLong(text, radix);
        }
    }

    /**
     * @param value 值
     * @return 结果
     */
    public static boolean parseToBoolean(Object value) {
        if (value == null) {
            return false;
        } else if (value instanceof Number) {
            float f = ((Number) value).floatValue();
            return f != 0 && !Float.isNaN(f);
        } else if (value instanceof String) {
            return ((String) value).length() > 0;
        } else if (value instanceof Boolean) {
            return (Boolean) value;
        } else {
            return true;
        }
    }

    /**
     * @param value 值
     * @return 结果
     */
    public static Number parseToNumber(Object value) {
        value = parseToPrimitive(value, String.class);
        if (value == null) {
            return 0;
        } else if (value instanceof Boolean) {
            return ((Boolean) value) ? 1 : 0;
        } else if (value instanceof Number) {
            return (Number) value;
        } else {
            String text = (String) value;
            try {
                if (text.indexOf(CharConstant.CHAR_DOT) >= 0) {
                    return Float.parseFloat(text);
                }
                if (text.length() > 1) {
                    char c1 = text.charAt(0);
                    char c2 = text.charAt(1);
                    if (c1 == CharConstant.CHAR_PLUS || c1 == CharConstant.CHAR_HYPHEN) {
                        c1 = c2;
                        if (text.length() > SysConstant.SYS_NUMBER_TWO) {
                            c2 = text.charAt(2);
                        }
                    }
                    if (c1 == CharConstant.CHAR_NUMBER_ZERO) {
                        if (c2 == CharConstant.CHAR_X || c2 == CharConstant.CHAR_X_UPPER) {
                            return parseToNumber(text.substring(2), 16);
                        }
                        return parseToNumber(text, 10);
                    } else if (text.indexOf(CharConstant.CHAR_E_UPPER) > 0 || text.indexOf(CharConstant.CHAR_E) > 0) {
                        return Float.parseFloat(text);
                    }
                }
                return parseToNumber(text, 10);
            } catch (NumberFormatException ex) {
                return Double.NaN;
            }
        }
    }

    /**
     * @param value        值
     * @param expectedType 类型
     * @return null|Number|Boolean|String结果
     */
    public static Object parseToPrimitive(Object value, Class<?> expectedType) {
        boolean toString;
        if (expectedType == Number.class) {
            toString = false;
        } else if (expectedType == String.class) {
            toString = true;
        } else if (expectedType == null) {
            toString = !(value instanceof Date);
        } else {
            throw new IllegalArgumentException(
                    "expectedType 只能是 Number或者String");
        }
        if (value == null) {
            return null;
        } else if (value instanceof Boolean) {
            return value;
        } else if (value instanceof Number) {
            return value;
        } else if (value instanceof String) {
            return value;
        }

        if (toString) {
            return String.valueOf(value);
        } else {
            if (value instanceof Date) {
                return new Long(((Date) value).getTime());
            } else {
                return String.valueOf(value);
            }
        }
    }

    public static Object parseToValue(Object value, Class<?> type) {
        if (type == String.class) {
            return value == null ? null : value.toString();
        } else if (type == Character.class) {
            if (value == null) {
                return (char) 0;
            }
            value = parseToPrimitive(value, String.class);
            if (value instanceof Number) {
                return (char) ((Number) value).intValue();
            }
            String text = (String) value;
            if (text.length() > 0) {
                return text.charAt(0);
            } else {
                return 0;
            }
        }
        type = ReflectUtil.toWrapper(type);

        if (Number.class.isAssignableFrom(type)) {
            Number n = parseToNumber(value);
            return ReflectUtil.toValue(n, type);
        }

        //Boolean
        if (type == Boolean.class) {
            return parseToBoolean(parseToPrimitive(value, type));
        }
        return value;
    }

    public static String parseToString(Object value) {
        value = parseToPrimitive(value, String.class);
        if (value instanceof Number) {
            return parseToString((Number) value, 10);
        }
        return String.valueOf(value);
    }

    static String parseToString(Number thiz, int radix) {
        if (radix <= 0 || radix > Character.MAX_RADIX) {
            radix = 10;
        }
        if (thiz instanceof Double || thiz instanceof Float) {
            return floatToString(thiz.doubleValue(), radix);
        }
        return Long.toString(thiz.longValue(), radix);
    }

    private static String floatToString(double d, int base) {
        if (Double.isNaN(d)) {
            return "NaN";
        } else if (Double.isInfinite(d)) {
            return (d > 0.0) ? "Infinity" : "-Infinity";
        } else if (d == 0) {
            // ALERT: should it distinguish -0.0 from +0.0 ?
            return "0";
        }
        if (base == SysConstant.SYS_NUMBER_TEN) {
            String result = Double.toString(d);
            if (result.endsWith(SysConstant.SYS_STRING_DOT_ZERO)) {
                result = result.substring(0, result.length() - 2);
            }
            return result;
        } else {
            return Long.toString((long) d, base);
        }
    }
}
