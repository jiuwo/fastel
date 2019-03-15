package org.jiuwo.fastel.contract.enums;

import java.util.Arrays;

import org.jiuwo.fastel.constant.SysConstant;
import lombok.Getter;

import static org.jiuwo.fastel.constant.SysConstant.SYS_NULL;

/**
 * @author Steven Han
 */
public interface ExpressionEnum {

    /**
     * Token
     */
    @Getter
    enum Token {

        /**
         * value
         */
        VALUE_CONSTANTS("value", -0x01),
        /**
         * var
         */
        VALUE_VAR("var", -0x02),
        /**
         * []
         */
        VALUE_LIST("[]", -0x03),
        /**
         * {}
         */
        VALUE_MAP("{}", -0x04),

        /**
         * lambda
         */
        VALUE_LAMBDA("null", -0x05),

        /**
         * default
         */
        DEFAULT("null", -0x09),

        //九：（最高级别的运算符号）

        /**
         * .
         */
        OP_GET(".", 0 << 16 | 0 << 12 | 1 << 8 | 8 << 4 | 0),

        /**
         * ()
         */
        OP_INVOKE("()", 0 << 16 | 0 << 12 | 1 << 8 | 8 << 4 | 1),

        //八

        /**
         * !
         */
        OP_NOT("!", 0 << 16 | 0 << 12 | 0 << 8 | 7 << 4 | 0),

        /**
         * ~
         */
        OP_BIT_NOT("~", 0 << 16 | 0 << 12 | 0 << 8 | 7 << 4 | 1),

        /**
         * +
         */
        OP_POS("+", 0 << 16 | 0 << 12 | 0 << 8 | 7 << 4 | 2),

        /**
         * -
         */
        OP_NEG("-", 0 << 16 | 0 << 12 | 0 << 8 | 7 << 4 | 3),

        //七：
        /**
         * *
         */
        OP_MUL("*", 0 << 16 | 0 << 12 | 1 << 8 | 6 << 4 | 0),

        /**
         * /
         */
        OP_DIV("/", 0 << 16 | 0 << 12 | 1 << 8 | 6 << 4 | 1),

        /**
         * %
         */
        OP_MOD("%", 0 << 16 | 0 << 12 | 1 << 8 | 6 << 4 | 2),

        //六：
        //与正负符号共享了字面值

        /**
         * +
         */
        OP_ADD("+", 0 << 16 | 0 << 12 | 1 << 8 | 5 << 4 | 0),

        /**
         * -
         */
        OP_SUB("-", 0 << 16 | 0 << 12 | 1 << 8 | 5 << 4 | 1),

        //五:移位

        /**
         * 左移位
         */
        OP_LSH("<<", 0 << 16 | 0 << 12 | 1 << 8 | 4 << 4 | 0),

        /**
         * 右移位
         */
        OP_RSH(">>", 0 << 16 | 0 << 12 | 1 << 8 | 4 << 4 | 1),

        /**
         * 无符号右移
         */
        OP_URSH(">>>", 0 << 16 | 0 << 12 | 1 << 8 | 4 << 4 | 2),

        //四:比较

        /**
         * 小于
         */
        OP_LT("<", 0 << 16 | 1 << 12 | 1 << 8 | 3 << 4 | 0),

        /**
         * 大于
         */
        OP_GT(">", 0 << 16 | 1 << 12 | 1 << 8 | 3 << 4 | 1),

        /**
         * 小于等于
         */
        OP_LTEQ("<=", 0 << 16 | 1 << 12 | 1 << 8 | 3 << 4 | 2),

        /**
         * 大于等于
         */
        OP_GTEQ(">=", 0 << 16 | 1 << 12 | 1 << 8 | 3 << 4 | 3),

        /**
         * in
         */
        OP_IN(" in ", 1 << 16 | 1 << 12 | 1 << 8 | 3 << 4 | 0),

        //四:等不等比较

        /**
         * ==
         */
        OP_EQ("==", 0 << 16 | 0 << 12 | 1 << 8 | 3 << 4 | 0),

        /**
         * !=
         */
        OP_NE("!=", 0 << 16 | 0 << 12 | 1 << 8 | 3 << 4 | 1),

        /**
         * ===
         */
        OP_EQ_STRICT("===", 0 << 16 | 0 << 12 | 1 << 8 | 3 << 4 | 2),

        /**
         * !==
         */
        OP_NE_STRICT("!==", 0 << 16 | 0 << 12 | 1 << 8 | 3 << 4 | 30),

        //三:按位与或

        /**
         * AND符
         */
        OP_BIT_AND("&", 0 << 16 | 4 << 12 | 1 << 8 | 2 << 4 | 0),

        /**
         * ^
         */
        OP_BIT_XOR("^", 0 << 16 | 3 << 12 | 1 << 8 | 2 << 4 | 0),

        /**
         * |
         */
        OP_BIT_OR("|", 0 << 16 | 2 << 12 | 1 << 8 | 2 << 4 | 0),

        //三:与或

        /**
         * 双AND符
         */
        OP_AND("&&", 0 << 16 | 1 << 12 | 1 << 8 | 2 << 4 | 0),

        /**
         * ||
         */
        OP_OR("||", 0 << 16 | 0 << 12 | 1 << 8 | 2 << 4 | 0),

        //二：
        //?;

        /**
         * ?
         */
        OP_QUESTION("?", 0 << 16 | 0 << 12 | 1 << 8 | 1 << 4 | 0),

        //:;

        /**
         * :
         */
        OP_QUESTION_SELECT(":", 0 << 16 | 0 << 12 | 1 << 8 | 1 << 4 | 1),

        //一：
        //与Map Join 共享字面量（map join 会忽略）

        /**
         * ,
         */
        OP_JOIN(",", 0 << 16 | 0 << 12 | 1 << 8 | 0 << 4 | 0),

        //与三元运算符共享字面值

        /**
         * :
         */
        OP_PUT(":", 0 << 16 | 0 << 12 | 1 << 8 | 0 << 4 | 1),

        /**
         * (
         */
        BRACKET_BEGIN("(", -0xFFFE),

        /**
         * )
         */
        BRACKET_END(")", -0xFFFF),

        /**
         * OP_GET_STATIC
         */
        OP_GET_STATIC("null", 0 << 16 | 0 << 12 | 0 << 8 | 8 << 4 | 0),

        /**
         * OP_INVOKE_WITH_STATIC_PARAM
         */
        OP_INVOKE_WITH_STATIC_PARAM("null", 0 << 16 | 0 << 12 | 0 << 8 | 8 << 4 | 1);

        private String key;
        private int value;

        /**
         * 构造方法
         *
         * @param value 值
         */
        Token(String key, int value) {
            this.key = key;
            this.value = value;
        }

        public static ExpressionEnum.Token getTokenByValue(int value) {
            return Arrays.stream(Token.values())
                    .filter(p -> p.getValue() == value)
                    .findFirst()
                    .orElse(null);
        }

        public static ExpressionEnum.Token getTokenByKey(String key) {
            return Arrays.stream(Token.values())
                    .filter(p -> p.getKey().equals(key))
                    .findFirst()
                    .orElse(null);
        }

        public static boolean existsTokenByKey(String key) {
            return getTokenByKey(key) != null;
        }

        public static boolean existsTokenByValue(int value) {
            return getTokenByValue(value) != null;
        }

        public static int getValue(String key) {
            return Arrays.stream(Token.values())
                    .filter(p -> !p.getKey().equals(SYS_NULL) && p.getKey().equals(key))
                    .map(ExpressionEnum.Token::getValue)
                    .findFirst()
                    .orElse(0);
        }

        public static String getKey(int value) {
            return Arrays.stream(Token.values())
                    .filter(p -> !p.getKey().equals(SYS_NULL) && p.getValue() == value)
                    .map(ExpressionEnum.Token::getKey)
                    .findFirst()
                    .orElse(null);
        }

    }

    @Getter
    enum JsToken {


        /**
         * value
         */
        MATH_ABS("abs", null),
        MATH_ACOS("acos", null),
        MATH_ASIN("asin", null),
        MATH_ATAN("atan", null),
        MATH_CEIL("ceil", null),
        MATH_COS("cos", null),
        MATH_EXP("exp", null),
        MATH_FLOOR("floor", null),
        MATH_LOG("log", null),
        MATH_ROUND("round", null),
        MATH_SIN("sin", null),
        MATH_SQRT("sqrt", null),
        MATH_TAN("tan", null),
        MATH_RANDOM("random", null),
        MATH_MIN("min", null),
        MATH_MAX("max", null),
        MATH_POW("pow", null),
        MATH_ATAN_2("atan2", null),
        MATH_E("E", SysConstant.MATH_E),
        MATH_PI("PI", SysConstant.MATH_PI),
        MATH_LN10("LN10", SysConstant.LN10),
        MATH_LN2("LN2", SysConstant.LN2),
        MATH_LOG2E("LOG2E", 1 / SysConstant.LN2),
        MATH_LOG10E("LOG10E", 1 / SysConstant.LN10),
        MATH_SQRT1_2("SQRT1_2", SysConstant.SQRT1_2),
        MATH_SQRT2("SQRT2", SysConstant.SQRT2),
        MATH("Math", null),
        JSON_PARSE("parse", null),
        JSON_STRINGIFY("stringify", null),
        JSON("JSON", null),
        IS_FINITE("isFinite", null),
        IS_NAN("isNaN", null),
        PARSE_INT("parseInt", null),
        PARSE_FLOAT("parseFloat", null),
        ENCODE_URI("encodeURI", 1 << 8 | 1 << 4),
        DECODE_URI("decodeURI", 0 << 8 | 1 << 4),
        ENCODE_URI_COMPONENT("encodeURIComponent", 1 << 8 | 0 << 4),
        DECODE_URI_COMPONENT("decodeURIComponent", 0 << 8 | 0 << 4),
        INFINITY("Infinity", null),
        NAN("NaN", null),
        DEFAULT("default", null);

        private String key;
        private Object value;

        /**
         * 构造方法
         *
         * @param value 值
         */
        JsToken(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public static ExpressionEnum.JsToken getJsTokenByValue(Object value) {
            return Arrays.stream(JsToken.values())
                    .filter(p -> p.getValue().equals(value))
                    .findFirst()
                    .orElse(null);
        }

        public static ExpressionEnum.JsToken getJsTokenByKey(String key) {
            return Arrays.stream(JsToken.values())
                    .filter(p -> p.getKey().equals(key))
                    .findFirst()
                    .orElse(null);
        }

        public static Object getValue(String key) {
            return Arrays.stream(JsToken.values())
                    .filter(p -> !p.getKey().equals(SYS_NULL) && p.getKey().equals(key))
                    .map(ExpressionEnum.JsToken::getValue)
                    .findFirst()
                    .orElse(null);
        }

    }


}
