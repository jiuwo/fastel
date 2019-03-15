package org.jiuwo.fastel.impl;

import java.util.function.Function;

import org.jiuwo.fastel.Executable;
import org.jiuwo.fastel.constant.map.MapParserJsConsumerConstant;
import org.jiuwo.fastel.contract.enums.ExpressionEnum;
import org.jiuwo.fastel.exception.FastElException;
import org.jiuwo.fastel.util.function.JsExpressionFunction;

/**
 * @author Steven Han
 */
public class OptimizeJsExpressionImpl implements Executable {

    private final ExpressionEnum.JsToken jsToken;

    public OptimizeJsExpressionImpl(ExpressionEnum.JsToken jsToken) {
        this.jsToken = jsToken;
    }


    @Override
    public Object invoke(Object obj, Object... args) {
        JsExpressionFunction jsExpressionFunction = new JsExpressionFunction(args, jsToken);
        Function<JsExpressionFunction, Object> consumer = MapParserJsConsumerConstant.getInstance().get(jsToken);
        if (consumer != null) {
            return consumer.apply(jsExpressionFunction);
        }
        throw new FastElException(String.format("还未识别的函数:{}%n{}", obj, args));
    }

}