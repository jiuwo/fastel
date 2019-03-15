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
                (jsExpressionFunction) -> jsExpressionFunction.enOrDeCodeURI());
        map.put(ExpressionEnum.JsToken.DECODE_URI,
                (jsExpressionFunction) -> jsExpressionFunction.enOrDeCodeURI());
        map.put(ExpressionEnum.JsToken.ENCODE_URI_COMPONENT,
                (jsExpressionFunction) -> jsExpressionFunction.enOrDeCodeURI());
        map.put(ExpressionEnum.JsToken.DECODE_URI_COMPONENT,
                (jsExpressionFunction) -> jsExpressionFunction.enOrDeCodeURI());
        map.put(ExpressionEnum.JsToken.PARSE_INT,
                (jsExpressionFunction) -> jsExpressionFunction.parseInt());
        map.put(ExpressionEnum.JsToken.PARSE_FLOAT,
                (jsExpressionFunction) -> jsExpressionFunction.parseFloat());
        map.put(ExpressionEnum.JsToken.JSON_PARSE,
                (jsExpressionFunction) -> jsExpressionFunction.parse());
        map.put(ExpressionEnum.JsToken.JSON_STRINGIFY,
                (jsExpressionFunction) -> jsExpressionFunction.stringify());
        map.put(ExpressionEnum.JsToken.IS_FINITE,
                (jsExpressionFunction) -> jsExpressionFunction.parseIsFinite());
        map.put(ExpressionEnum.JsToken.IS_NAN,
                (jsExpressionFunction) -> jsExpressionFunction.parseIsNaN());
        map.put(ExpressionEnum.JsToken.MATH_ABS,
                (jsExpressionFunction) -> jsExpressionFunction.abs());
        map.put(ExpressionEnum.JsToken.MATH_ACOS,
                (jsExpressionFunction) -> jsExpressionFunction.acos());
        map.put(ExpressionEnum.JsToken.MATH_ASIN,
                (jsExpressionFunction) -> jsExpressionFunction.asin());
        map.put(ExpressionEnum.JsToken.MATH_ATAN,
                (jsExpressionFunction) -> jsExpressionFunction.atan());
        map.put(ExpressionEnum.JsToken.MATH_ATAN_2,
                (jsExpressionFunction) -> jsExpressionFunction.atan2());
        map.put(ExpressionEnum.JsToken.MATH_CEIL,
                (jsExpressionFunction) -> jsExpressionFunction.ceil());
        map.put(ExpressionEnum.JsToken.MATH_COS,
                (jsExpressionFunction) -> jsExpressionFunction.cos());
        map.put(ExpressionEnum.JsToken.MATH_EXP,
                (jsExpressionFunction) -> jsExpressionFunction.exp());
        map.put(ExpressionEnum.JsToken.MATH_FLOOR,
                (jsExpressionFunction) -> jsExpressionFunction.floor());
        map.put(ExpressionEnum.JsToken.MATH_LOG,
                (jsExpressionFunction) -> jsExpressionFunction.log());
        map.put(ExpressionEnum.JsToken.MATH_MAX,
                (jsExpressionFunction) -> jsExpressionFunction.max());
        map.put(ExpressionEnum.JsToken.MATH_MIN,
                (jsExpressionFunction) -> jsExpressionFunction.min());
        map.put(ExpressionEnum.JsToken.MATH_POW,
                (jsExpressionFunction) -> jsExpressionFunction.pow());
        map.put(ExpressionEnum.JsToken.MATH_RANDOM,
                (jsExpressionFunction) -> jsExpressionFunction.random());
        map.put(ExpressionEnum.JsToken.MATH_ROUND,
                (jsExpressionFunction) -> jsExpressionFunction.round());
        map.put(ExpressionEnum.JsToken.MATH_SIN,
                (jsExpressionFunction) -> jsExpressionFunction.sin());
        map.put(ExpressionEnum.JsToken.MATH_SQRT,
                (jsExpressionFunction) -> jsExpressionFunction.sqrt());
        map.put(ExpressionEnum.JsToken.MATH_TAN,
                (jsExpressionFunction) -> jsExpressionFunction.tan());
        return map;
    }
}
