package org.jiuwo.fastel.util;

/**
 * @author Steven Han
 */
public class CharUtil {

    /**
     * 全角unicode 开始
     */
    private static final char CHAR_SBC_START = 0xff01;
    /**
     * 全角unicode 结束
     */
    private static final char CHAR_SBC_END = 0xff5e;
    /**
     * 全解半角 unicode 差值
     */
    private static final char CHAR_SBC_DBC_DIFF = 0xfee0;

    /**
     * char 全角转半角
     *
     * @param c 需要转换的char
     * @return 结果
     */
    public static char sbc2Dbc(char c) {
        if (c >= CHAR_SBC_START && c <= CHAR_SBC_END) {
            c -= CHAR_SBC_DBC_DIFF;
        }
        return c;
    }
}
