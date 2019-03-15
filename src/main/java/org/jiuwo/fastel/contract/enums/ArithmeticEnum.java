package org.jiuwo.fastel.contract.enums;

/**
 * @author Steven Han
 * 四则运算枚举
 */
public interface ArithmeticEnum {

    /**
     * 四则运算枚举
     */
    enum Arithmetic {
        /**
         * 加法
         */
        PLUS,
        /**
         * 减法
         */
        SUBTRACT,
        /**
         * 乘法
         */
        MULTIPLY,
        /**
         * 除法
         */
        DIVIDE,
        /**
         * 取模
         */
        MODULUS
    }

    /**
     * 四则运算枚举
     */
    enum NumberType {
        /**
         * INT
         */
        INT,
        /**
         * LONG
         */
        LONG,
        /**
         * FLOAT
         */
        FLOAT,
        /**
         * DOUBLE
         */
        DOUBLE
    }
}
