package org.jiuwo.fastel.constant.map;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.jiuwo.fastel.contract.enums.ExpressionEnum;
import org.jiuwo.fastel.util.function.OperationStrategyFunction;

import static org.jiuwo.fastel.contract.enums.ExpressionEnum.Token.OP_GET;

/**
 * @author Steven Han
 */
public class MapStrategyLogicFunctionConstant {

    private static volatile Map<ExpressionEnum.Token, Function<OperationStrategyFunction, Object>> mapLogicFunction = null;

    public static Map<ExpressionEnum.Token, Function<OperationStrategyFunction, Object>> getInstance() {
        if (mapLogicFunction == null) {
            synchronized (Map.class) {
                if (mapLogicFunction == null) {
                    mapLogicFunction = mapLogicFunction();
                }
            }
        }
        return mapLogicFunction;
    }

    private static Map<ExpressionEnum.Token, Function<OperationStrategyFunction, Object>> mapLogicFunction() {
        Map<ExpressionEnum.Token, Function<OperationStrategyFunction, Object>> map = new HashMap<>();
        /**
         * MAP_LOGIC_FUNCTION
         */
        map.put(ExpressionEnum.Token.OP_GET_STATIC,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpGetStatic());
        map.put(OP_GET,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpGet());
        map.put(ExpressionEnum.Token.OP_AND,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpAnd());
        map.put(ExpressionEnum.Token.OP_OR,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpOr());
        map.put(ExpressionEnum.Token.OP_QUESTION_SELECT,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpQuestionSelect());
        map.put(ExpressionEnum.Token.OP_QUESTION,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpQuestion());
        return map;
    }
}