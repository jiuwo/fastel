package org.jiuwo.fastel.constant.map;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.jiuwo.fastel.contract.CharConsumerParam;
import org.jiuwo.fastel.contract.enums.ParserEnum;
import org.jiuwo.fastel.parser.CharConsumer;

/**
 * @author Steven Han
 */
public class MapParserStringConsumerConstant {
    private static volatile Map<Character, BiConsumer<CharConsumer, CharConsumerParam>> mapCharConsumer = null;

    public static Map<Character, BiConsumer<CharConsumer, CharConsumerParam>> getInstance() {
        if (mapCharConsumer == null) {
            synchronized (Map.class) {
                if (mapCharConsumer == null) {
                    mapCharConsumer = mapCharConsumer();
                }
            }
        }
        return mapCharConsumer;
    }

    private static Map<Character, BiConsumer<CharConsumer, CharConsumerParam>> mapCharConsumer() {
        Map<Character, BiConsumer<CharConsumer, CharConsumerParam>> map = new HashMap<>();
        map.put(ParserEnum.CharEscape.CHAR_B.getValue(),
                (charConsumer, consumerParam) -> charConsumer.appendEscape(consumerParam));
        map.put(ParserEnum.CharEscape.CHAR_F.getValue(),
                (charConsumer, consumerParam) -> charConsumer.appendEscape(consumerParam));
        map.put(ParserEnum.CharEscape.CHAR_N.getValue(),
                (charConsumer, consumerParam) -> charConsumer.appendEscape(consumerParam));
        map.put(ParserEnum.CharEscape.CHAR_R.getValue(),
                (charConsumer, consumerParam) -> charConsumer.appendEscape(consumerParam));
        map.put(ParserEnum.CharEscape.CHAR_T.getValue(),
                (charConsumer, consumerParam) -> charConsumer.appendEscape(consumerParam));
        map.put(ParserEnum.CharEscape.CHAR_V.getValue(),
                (charConsumer, consumerParam) -> charConsumer.appendEscape(consumerParam));
        map.put(ParserEnum.CharEscape.CHAR_EMPTY.getValue(),
                (charConsumer, consumerParam) -> charConsumer.appendEmpty(consumerParam));
        map.put(ParserEnum.CharEscape.CHAR_TWO_BACKS_SLASH.getValue(),
                (charConsumer, consumerParam) -> charConsumer.appendTwoBacksSlash(consumerParam));
        map.put(ParserEnum.CharEscape.CHAR_SLASH.getValue(),
                (charConsumer, consumerParam) -> charConsumer.appendSlash(consumerParam));
        map.put(ParserEnum.CharEscape.CHAR_QUOTATION.getValue(),
                (charConsumer, consumerParam) -> charConsumer.appendQuotation(consumerParam));
        map.put(ParserEnum.CharEscape.CHAR_TWO_QUOTATION.getValue(),
                (charConsumer, consumerParam) -> charConsumer.appendTwoQuotation(consumerParam));
        map.put(ParserEnum.CharEscape.CHAR_U.getValue(),
                (charConsumer, consumerParam) -> charConsumer.appendU(consumerParam));
        map.put(ParserEnum.CharEscape.CHAR_X.getValue(),
                (charConsumer, consumerParam) -> charConsumer.appendX(consumerParam));
        return map;
    }
}
