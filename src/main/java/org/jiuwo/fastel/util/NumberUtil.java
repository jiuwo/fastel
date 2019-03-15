package org.jiuwo.fastel.util;

import org.jiuwo.fastel.constant.CharConstant;
import org.jiuwo.fastel.constant.SysConstant;
import org.jiuwo.fastel.contract.ParserParam;
import org.jiuwo.fastel.contract.enums.ExpressionEnum;

/**
 * @author Steven Han
 */
public class NumberUtil {

    private static final Class<?>[] NUMBER_CLASS = {Byte.class
            , Short.class
            , Integer.class
            , Long.class
            , Float.class
            , Double.class};

    public static final int getNumberType(Number a, Number b) {
        Class<?> aClass = a.getClass();
        Class<?> bClass = b.getClass();
        int i = NUMBER_CLASS.length;
        while (i-- > 0) {
            Class<?> c = NUMBER_CLASS[i];
            if (c == aClass || c == bClass) {
                return i;
            }
        }
        // double default
        return NUMBER_CLASS.length - 1;
    }

    /**
     * 加法运算
     *
     * @param a     左参数
     * @param b     右参数
     * @param token Token
     * @return 结果 0,1,-1,validReturn
     */
    public static boolean compare(Number a, Number b, ExpressionEnum.Token token) {

        double da = a.doubleValue(), db = b.doubleValue();
        if (Double.isNaN(da) || Double.isNaN(db)) {
            return token.equals(ExpressionEnum.Token.OP_NE) || token.equals(ExpressionEnum.Token.OP_NE_STRICT);
        }
        long i = Double.compare(da, db);
        boolean result;
        switch (token) {
            case OP_GT:
                result = i > 0;
                break;
            case OP_GTEQ:
                result = i >= 0;
                break;
            case OP_LT:
                result = i < 0;
                break;
            case VALUE_MAP:
            case OP_LTEQ:
                result = i <= 0;
                break;
            case OP_EQ:
            case OP_EQ_STRICT:
                result = i == 0;
                break;
            case OP_NE:
            case OP_NE_STRICT:
                result = i != 0;
                break;
            default:
                throw new IllegalStateException("无效比较类型:" + token);
        }
        return result;

    }

    /**
     * 加法运算
     *
     * @param a 左参数
     * @param b 右参数
     * @return 结果
     */
    public static Number plus(Number a, Number b) {
        switch (NumberUtil.getNumberType(a, b)) {
            case 0:
            case 1:
            case 2:
                return a.intValue() + b.intValue();
            case 3:
                return a.longValue() + b.longValue();
            case 4:
                return a.floatValue() + b.floatValue();
            default:
                return a.doubleValue() + b.doubleValue();
        }
    }

    /**
     * 减法运算
     *
     * @param a 左参数
     * @param b 右参数
     * @return 结果
     */
    public static Number subtract(Number a, Number b) {
        switch (NumberUtil.getNumberType(a, b)) {
            case 0:
            case 1:
            case 2:
                return a.intValue() - b.intValue();
            case 3:
                return a.longValue() - b.longValue();
            case 4:
                return a.floatValue() - b.floatValue();
            default:
                return a.doubleValue() - b.doubleValue();
        }
    }

    /**
     * 乘法运算
     *
     * @param a 左参数
     * @param b 右参数
     * @return 结果
     */
    public static Number multiply(Number a, Number b) {
        switch (NumberUtil.getNumberType(a, b)) {
            case 0:
            case 1:
            case 2:
                return a.intValue() * b.intValue();
            case 3:
                return a.longValue() * b.longValue();
            case 4:
                return a.floatValue() * b.floatValue();
            default:
                return a.doubleValue() * b.doubleValue();
        }
    }

    /**
     * 除法运算
     *
     * @param a 左参数
     * @param b 右参数
     * @return 结果
     */
    public static Number divide(Number a, Number b) {
        switch (NumberUtil.getNumberType(a, b)) {
            case 0:
            case 1:
            case 2:
            case 3:
                return a.doubleValue() / b.doubleValue();
            case 4:
                return a.floatValue() / b.floatValue();
            default:
                return a.doubleValue() / b.doubleValue();
        }
    }

    /**
     * 求模运算
     *
     * @param a 左参数
     * @param b 右参数
     * @return 结果
     */
    public static Number modulus(Number a, Number b) {
        switch (NumberUtil.getNumberType(a, b)) {
            case 0:
            case 1:
            case 2:
                return a.intValue() % b.intValue();
            case 3:
                return a.longValue() % b.longValue();
            case 4:
                return a.floatValue() % b.floatValue();
            default:
                return a.doubleValue() % b.doubleValue();
        }
    }

    public static Number parseZero(boolean neg, ParserParam parserParam) {
        if (parserParam.getCurrentIndex() < parserParam.getElSize()) {
            char c = parserParam.getElValue().charAt(parserParam.plusOne());
            if (c == CharConstant.CHAR_X || c == CharConstant.CHAR_X_UPPER) {
                long value = parseHex(parserParam);
                if (neg) {
                    value = -value;
                }
                return value;
            } else if (c > CharConstant.CHAR_NUMBER_ZERO && c <= CharConstant.CHAR_NUMBER_SEVEN) {
                parserParam.subtractOne();
                int value = parseOctal(parserParam);
                if (neg) {
                    value = -value;
                }
                return value;
            } else if (c == CharConstant.CHAR_DOT) {
                parserParam.subtractOne();
                return parseFloat(parserParam.getCurrentIndex() - 1, parserParam);
            } else {
                parserParam.subtractOne();
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * 当前值为 . 或者 E，e
     *
     * @param begin       开始
     * @param parserParam EL参数
     * @return 转换化的Float
     */
    public static Number parseFloat(final int begin, ParserParam parserParam) {
        boolean isFloatingPoint = false;
        char next = parserParam.getElValue().charAt(parserParam.getCurrentIndex());
        if (next == CharConstant.CHAR_DOT) {
            int p = parserParam.plusOne();
            seekDecimal(parserParam);
            // 复位
            if (parserParam.getCurrentIndex() == p) {
                parserParam.subtractOne();
                String ns = parserParam.getElValue().substring(begin, parserParam.getCurrentIndex());
                return Long.parseLong(ns);
            } else {
                isFloatingPoint = true;
                if (parserParam.getCurrentIndex() < parserParam.getElSize()) {
                    next = parserParam.getElValue().charAt(parserParam.getCurrentIndex());
                } else {
                    next = 0;
                }
            }
        }
        if (next == CharConstant.CHAR_E_UPPER || next == CharConstant.CHAR_E) {
            parserParam.plusOne();
            isFloatingPoint = true;
            seekNegative(parserParam);
            seekDecimal(parserParam);
        }
        String ns = parserParam.getElValue().substring(begin, parserParam.getCurrentIndex());
        if (isFloatingPoint) {
            return Double.parseDouble(ns);
        } else {
            return Long.parseLong(ns);
        }
    }

    /**
     * 转换16进制为Long
     *
     * @param parserParam EL参数
     * @return Long
     */
    private static long parseHex(ParserParam parserParam) {
        long longValue = 0;
        while (parserParam.getCurrentIndex() < parserParam.getElSize()) {
            char c = parserParam.getElValue().charAt(parserParam.plusOne());
            if (c >= CharConstant.CHAR_NUMBER_ZERO
                    && c <= CharConstant.CHAR_NUMBER_NINE) {
                longValue = (longValue << SysConstant.SYS_NUMBER_FOUR)
                        + (c - CharConstant.CHAR_NUMBER_ZERO);
            } else if (c >= CharConstant.CHAR_A_UPPER
                    && c <= CharConstant.CHAR_F_UPPER) {
                longValue = (longValue << SysConstant.SYS_NUMBER_FOUR)
                        + (c - CharConstant.CHAR_A_UPPER + SysConstant.SYS_NUMBER_TEN);
            } else if (c >= CharConstant.CHAR_A
                    && c <= CharConstant.CHAR_F) {
                longValue = (longValue << SysConstant.SYS_NUMBER_FOUR)
                        + (c - CharConstant.CHAR_A + SysConstant.SYS_NUMBER_TEN);
            } else {
                parserParam.subtractOne();
                break;
            }
        }
        return longValue;
    }

    private static int parseOctal(ParserParam parserParam) {
        int lvalue = 0;
        while (parserParam.getCurrentIndex() < parserParam.getElSize()) {
            char c = parserParam.getElValue().charAt(parserParam.plusOne());
            if (c >= CharConstant.CHAR_NUMBER_ZERO && c < CharConstant.CHAR_NUMBER_EIGHT) {
                lvalue = (lvalue << SysConstant.SYS_NUMBER_THREE) + (c - CharConstant.CHAR_NUMBER_ZERO);
            } else {
                parserParam.subtractOne();
                break;
            }
        }
        return lvalue;
    }

    private static void seekDecimal(ParserParam parserParam) {
        while (parserParam.getCurrentIndex() < parserParam.getElSize()) {
            char c = parserParam.getElValue().charAt(parserParam.plusOne());
            if (c >= CharConstant.CHAR_NUMBER_ZERO && c <= CharConstant.CHAR_NUMBER_NINE) {
            } else {
                parserParam.subtractOne();
                break;
            }
        }
    }

    private static void seekNegative(ParserParam parserParam) {
        char c = parserParam.getElValue().charAt(parserParam.plusOne());
        if (c == CharConstant.CHAR_HYPHEN || c == CharConstant.CHAR_PLUS) {

        } else {
            parserParam.subtractOne();
        }

    }

}
