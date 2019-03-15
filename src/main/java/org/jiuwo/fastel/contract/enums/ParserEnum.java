package org.jiuwo.fastel.contract.enums;

import java.util.Arrays;

import lombok.Getter;

/**
 * @author Steven Han
 */
public interface ParserEnum {

    @Getter
    enum CharEscape {
        /**
         * CharEscape
         */
        CHAR_B('\b', 'b'),
        CHAR_F('\f', 'f'),
        CHAR_N('\n', 'n'),
        CHAR_R('\r', 'r'),
        CHAR_T('\t', 't'),
        CHAR_V((char) 0xb, 'v'),
        CHAR_EMPTY(' ', ' '),
        CHAR_TWO_BACKS_SLASH('\\', '\\'),
        CHAR_SLASH('/', '/'),
        CHAR_QUOTATION('\'', '\''),
        CHAR_TWO_QUOTATION('"', '\"'),
        CHAR_U('u', 'u'),
        CHAR_X('x', 'x');


        private Character escape;
        private Character value;


        /**
         * 构造方法
         * int
         *
         * @param escape 转义后
         * @param value  转义前
         */
        CharEscape(Character escape, Character value) {
            this.escape = escape;
            this.value = value;
        }

        public static Character getEscape(Character value) {
            return Arrays.stream(ParserEnum.CharEscape.values())
                    .filter(p -> p.getValue().equals(value))
                    .map(ParserEnum.CharEscape::getEscape)
                    .findFirst()
                    .orElse(null);
        }

        public static Character getValue(Character escape) {
            return Arrays.stream(ParserEnum.CharEscape.values())
                    .filter(p -> p.getEscape().equals(escape))
                    .map(ParserEnum.CharEscape::getValue)
                    .findFirst()
                    .orElse(null);
        }

        public static CharEscape getCharEscape(Character escape) {
            return Arrays.stream(ParserEnum.CharEscape.values())
                    .filter(p -> p.getEscape().equals(escape))
                    .findFirst()
                    .orElse(null);
        }

    }

    /**
     * 转化状态
     */
    enum ParseStatus {
        /**
         * 开始
         */
        BEGIN,
        /**
         * 表达式
         */
        EXPRESSION,
        /**
         * 操作
         */
        OPERATOR
    }

}
