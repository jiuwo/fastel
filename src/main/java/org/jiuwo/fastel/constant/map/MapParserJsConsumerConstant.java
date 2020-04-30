package org.jiuwo.fastel.constant.map;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.jiuwo.fastel.contract.enums.ExpressionEnum;
import org.jiuwo.fastel.util.function.JsExpressionFunction;

/**
 * @author Steven Han
 */
public class MapParserJsConsumerConstant {
    private static volatile Map<ExpressionEnum.JsToken, Function<JsExpressionFunction, Object>> mapParserJsConsumer = null;

    public static Map<ExpressionEnum.JsToken, Function<JsExpressionFunction, Object>> getInstance() {
        if (mapParserJsConsumer == null) {
            synchronized (Map.class) {
                if (mapParserJsConsumer == null) {
                    mapParserJsConsumer = mapParserJsConsumer();
                }
            }
        }
        return mapParserJsConsumer;
    }

    private static Map<ExpressionEnum.JsToken, Function<JsExpressionFunction, Object>> mapParserJsConsumer() {
        Map<ExpressionEnum.JsToken, Function<JsExpressionFunction, Object>> map = new HashMap<>();
        map.put(ExpressionEnum.JsToken.ENCODE_URI,
                JsExpressionFunction::enOrDeCodeURI);
        map.put(ExpressionEnum.JsToken.DECODE_URI,
                JsExpressionFunction::enOrDeCodeURI);
        map.put(ExpressionEnum.JsToken.ENCODE_URI_COMPONENT,
                JsExpressionFunction::enOrDeCodeURI);
        map.put(ExpressionEnum.JsToken.DECODE_URI_COMPONENT,
                JsExpressionFunction::enOrDeCodeURI);
        map.put(ExpressionEnum.JsToken.PARSE_INT,
                JsExpressionFunction::parseInt);
        map.put(ExpressionEnum.JsToken.PARSE_FLOAT,
                JsExpressionFunction::parseFloat);
        map.put(ExpressionEnum.JsToken.JSON_PARSE,
                JsExpressionFunction::parse);
        map.put(ExpressionEnum.JsToken.JSON_STRINGIFY,
                JsExpressionFunction::stringify);
        map.put(ExpressionEnum.JsToken.IS_FINITE,
                JsExpressionFunction::parseIsFinite);
        map.put(ExpressionEnum.JsToken.IS_NAN,
                JsExpressionFunction::parseIsNaN);
        map.put(ExpressionEnum.JsToken.MATH_ABS,
                JsExpressionFunction::abs);
        map.put(ExpressionEnum.JsToken.MATH_ACOS,
                JsExpressionFunction::acos);
        map.put(ExpressionEnum.JsToken.MATH_ASIN,
                JsExpressionFunction::asin);
        map.put(ExpressionEnum.JsToken.MATH_ATAN,
                JsExpressionFunction::atan);
        map.put(ExpressionEnum.JsToken.MATH_ATAN_2,
                JsExpressionFunction::atan2);
        map.put(ExpressionEnum.JsToken.MATH_CEIL,
                JsExpressionFunction::ceil);
        map.put(ExpressionEnum.JsToken.MATH_COS,
                JsExpressionFunction::cos);
        map.put(ExpressionEnum.JsToken.MATH_EXP,
                JsExpressionFunction::exp);
        map.put(ExpressionEnum.JsToken.MATH_FLOOR,
                JsExpressionFunction::floor);
        map.put(ExpressionEnum.JsToken.MATH_LOG,
                JsExpressionFunction::log);
        map.put(ExpressionEnum.JsToken.MATH_MAX,
                JsExpressionFunction::max);
        map.put(ExpressionEnum.JsToken.MATH_MIN,
                JsExpressionFunction::min);
        map.put(ExpressionEnum.JsToken.MATH_POW,
                JsExpressionFunction::pow);
        map.put(ExpressionEnum.JsToken.MATH_RANDOM,
                JsExpressionFunction::random);
        map.put(ExpressionEnum.JsToken.MATH_ROUND,
                JsExpressionFunction::round);
        map.put(ExpressionEnum.JsToken.MATH_SIN,
                JsExpressionFunction::sin);
        map.put(ExpressionEnum.JsToken.MATH_SQRT,
                JsExpressionFunction::sqrt);
        map.put(ExpressionEnum.JsToken.MATH_TAN,
                JsExpressionFunction::tan);
        return map;
    }
}
