package org.jiuwo.fastel.constant.map;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.jiuwo.fastel.contract.enums.ExpressionEnum;
import org.jiuwo.fastel.util.function.OperationStrategyFunction;

import static org.jiuwo.fastel.contract.enums.ExpressionEnum.Token.OP_INVOKE;

/**
 * @author Steven Han
 */
public class MapStrategyStaticFunctionConstant {

    private static volatile Map<ExpressionEnum.Token, Function<OperationStrategyFunction, Object>> mapStaticFunction = null;

    public static Map<ExpressionEnum.Token, Function<OperationStrategyFunction, Object>> getInstance() {
        if (mapStaticFunction == null) {
            synchronized (Map.class) {
                if (mapStaticFunction == null) {
                    mapStaticFunction = mapStaticFunction();
                }
            }
        }
        return mapStaticFunction;
    }

    private static Map<ExpressionEnum.Token, Function<OperationStrategyFunction, Object>> mapStaticFunction() {
        Map<ExpressionEnum.Token, Function<OperationStrategyFunction, Object>> map = new HashMap<>();
        /**
         * MAP_STATIC_FUNCTION
         */
        map.put(ExpressionEnum.Token.VALUE_VAR,
                operationStrategyFunction -> operationStrategyFunction.evaluateValueVar());
        map.put(ExpressionEnum.Token.VALUE_CONSTANTS,
                operationStrategyFunction -> operationStrategyFunction.evaluateValueConstants());
        map.put(ExpressionEnum.Token.VALUE_LIST,
                operationStrategyFunction -> operationStrategyFunction.evaluateValueList());
        map.put(ExpressionEnum.Token.VALUE_MAP,
                operationStrategyFunction -> operationStrategyFunction.evaluateValueMap());
        map.put(OP_INVOKE,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpInvoke());
        map.put(ExpressionEnum.Token.OP_INVOKE_WITH_STATIC_PARAM,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpInvoke());
        return map;
    }
}
