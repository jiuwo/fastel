package org.jiuwo.fastel.util;

/**
 * @author Steven Han
 */
public class StringUtil {

    /**
     * 字符串中的全解符号转换为半角
     *
     * @param value 需要转换的字符串
     * @return 结果
     */
    public static String sbc2Dbc(String value) {
        char[] cs = value.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            cs[i] = CharUtil.sbc2Dbc(cs[i]);
        }
        return new String(cs);
    }
}
