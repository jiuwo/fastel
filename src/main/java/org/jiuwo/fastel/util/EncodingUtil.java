package org.jiuwo.fastel.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Steven Han
 */
public class EncodingUtil {

    /**
     * //-_.!~*'() URL Encoder
     */
    private static final Pattern URL_ENCODE_SPLIT = Pattern.compile("[;/?:@&=+$,#]");
    private static final Pattern URL_DECODE_SPLIT = Pattern.compile("\\+|%3B|%2F|%3F|%3A|%40|%26|%3D|%2B|%24|%2C|%23");

    public static Object enOrDecode(boolean isEncode, boolean split, final String value, String charset)
            throws UnsupportedEncodingException {
        if (split) {
            Matcher matcher = (isEncode ? URL_ENCODE_SPLIT : URL_DECODE_SPLIT).matcher(value);
            StringBuilder buf = new StringBuilder();
            int end = 0;
            while (matcher.find()) {
                int start = matcher.start();
                if (start >= end) {
                    buf.append(processPart(isEncode, value.substring(end, start), charset));
                }
                buf.append(value.substring(start, end = matcher.end()));
            }
            buf.append(processPart(isEncode, value.substring(end), charset));
            return buf.toString();
        } else {
            return processPart(isEncode, value, charset);
        }
    }

    private static Object processPart(boolean encode, final String text, String charset)
            throws UnsupportedEncodingException {
        return encode ? URLEncoder.encode(text, charset) : URLDecoder.decode(
                text, charset);
    }
}
