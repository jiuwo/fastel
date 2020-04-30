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
                CharConsumer::appendEscape);
        map.put(ParserEnum.CharEscape.CHAR_F.getValue(),
                CharConsumer::appendEscape);
        map.put(ParserEnum.CharEscape.CHAR_N.getValue(),
                CharConsumer::appendEscape);
        map.put(ParserEnum.CharEscape.CHAR_R.getValue(),
                CharConsumer::appendEscape);
        map.put(ParserEnum.CharEscape.CHAR_T.getValue(),
                CharConsumer::appendEscape);
        map.put(ParserEnum.CharEscape.CHAR_V.getValue(),
                CharConsumer::appendEscape);
        map.put(ParserEnum.CharEscape.CHAR_EMPTY.getValue(),
                CharConsumer::appendEmpty);
        map.put(ParserEnum.CharEscape.CHAR_TWO_BACKS_SLASH.getValue(),
                CharConsumer::appendTwoBacksSlash);
        map.put(ParserEnum.CharEscape.CHAR_SLASH.getValue(),
                CharConsumer::appendSlash);
        map.put(ParserEnum.CharEscape.CHAR_QUOTATION.getValue(),
                CharConsumer::appendQuotation);
        map.put(ParserEnum.CharEscape.CHAR_TWO_QUOTATION.getValue(),
                CharConsumer::appendTwoQuotation);
        map.put(ParserEnum.CharEscape.CHAR_U.getValue(),
                CharConsumer::appendU);
        map.put(ParserEnum.CharEscape.CHAR_X.getValue(),
                CharConsumer::appendX);
        return map;
    }
}
