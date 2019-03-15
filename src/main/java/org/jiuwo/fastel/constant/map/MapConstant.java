package org.jiuwo.fastel.constant.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jiuwo.fastel.contract.enums.ExpressionEnum;
import org.jiuwo.fastel.impl.OptimizeJsExpressionImpl;

/**
 * @author Steven Han
 */
public class MapConstant {

    private static String[] mathArgs = {
            "abs", "acos", "asin", "atan", "ceil", "asin", "cos", "exp", "floor", "log", "round",
            "sin", "sqrt", "tan", "random", "min", "max", "pow", "atan2"};

    private static volatile Map<Object, Object> jsNodeList = null;

    public static Map<Object, Object> getInstance() {
        if (jsNodeList == null) {
            synchronized (Map.class) {
                if (jsNodeList == null) {
                    jsNodeList = MapConstant.jsNodeList();
                }
            }
        }
        return jsNodeList;
    }

    private static Map<String, Object> mathList() {
        Map<String, Object> map = new HashMap<>(8 + mathArgs.length);
        double ln10 = Math.log(10);
        double ln2 = Math.log(2);
        /**
         * 算术常量 e，即自然对数的底数（约等于2.718）。
         */
        map.put("E", Math.E);
        /**
         * 圆周率（约等于3.14159）
         */
        map.put("PI", Math.PI);
        /**
         * 10 的自然对数（约等于2.302）
         */
        map.put("LN10", ln10);
        /**
         * 2 的自然对数（约等于0.693
         */
        map.put("LN2", ln2);
        /**
         * 以 2 为底的 e 的对数（约等于 1.414）
         */
        map.put("LOG2E", 1 / ln2);
        /**
         * 以 10 为底的 e 的对数（约等于0.434
         */
        map.put("LOG10E", 1 / ln10);
        /**
         * 返回 2 的平方根的倒数（约等于 0.707）
         */
        map.put("SQRT1_2", Math.sqrt(0.5));
        /**
         * 2 的平方根（约等于 1.414）
         */
        map.put("SQRT2", Math.sqrt(2));
        for (int i = 0; i < mathArgs.length; i++) {
            map.put(mathArgs[i], new OptimizeJsExpressionImpl(ExpressionEnum.JsToken.getJsTokenByKey(mathArgs[i])));
        }
        return map;
    }

    private static Map<String, Object> jsonList() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("parse", new OptimizeJsExpressionImpl(ExpressionEnum.JsToken.JSON_PARSE));
        map.put("stringify", new OptimizeJsExpressionImpl(ExpressionEnum.JsToken.JSON_STRINGIFY));
        return map;
    }

    private static Map<Object, Object> jsNodeList() {
        Map<Object, Object> map = new HashMap<>(12);
        /**
         * Math 0+
         */
        map.put("Math", MapConstant.mathList());

        /**
         * JSON = {"parse","stringify"};//100+
         */
        map.put("JSON", Collections.unmodifiableMap(MapConstant.jsonList()));

        /**
         * isFinite,isNaN 200+
         */
        map.put("isFinite", new OptimizeJsExpressionImpl(ExpressionEnum.JsToken.IS_FINITE));
        map.put("isNaN", new OptimizeJsExpressionImpl(ExpressionEnum.JsToken.IS_NAN));

        /**
         * parseInt,parseFloat 300+
         */
        map.put("parseInt", new OptimizeJsExpressionImpl(ExpressionEnum.JsToken.PARSE_INT));
        map.put("parseFloat", new OptimizeJsExpressionImpl(ExpressionEnum.JsToken.PARSE_FLOAT));

        /**
         * encode,decode uri 400+
         */
        map.put("encodeURI", new OptimizeJsExpressionImpl(ExpressionEnum.JsToken.ENCODE_URI));
        map.put("decodeURI", new OptimizeJsExpressionImpl(ExpressionEnum.JsToken.DECODE_URI));
        map.put("encodeURIComponent", new OptimizeJsExpressionImpl(ExpressionEnum.JsToken.ENCODE_URI_COMPONENT));
        map.put("decodeURIComponent", new OptimizeJsExpressionImpl(ExpressionEnum.JsToken.DECODE_URI_COMPONENT));

        /**
         * Others
         */
        map.put("Infinity", Double.POSITIVE_INFINITY);
        map.put("NaN", Double.NaN);
        return map;
    }

}
