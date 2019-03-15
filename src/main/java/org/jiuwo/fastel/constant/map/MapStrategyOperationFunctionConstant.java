package org.jiuwo.fastel.constant.map;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.jiuwo.fastel.contract.enums.ExpressionEnum;
import org.jiuwo.fastel.util.function.OperationStrategyFunction;

/**
 * @author Steven Han
 */
public class MapStrategyOperationFunctionConstant {

    private static volatile Map<ExpressionEnum.Token, Function<OperationStrategyFunction, Object>> mapOperationFunction = null;

    public static Map<ExpressionEnum.Token, Function<OperationStrategyFunction, Object>> getInstance() {
        if (mapOperationFunction == null) {
            synchronized (Map.class) {
                if (mapOperationFunction == null) {
                    mapOperationFunction = mapOperationFunction();
                }
            }
        }
        return mapOperationFunction;
    }

    private static Map<ExpressionEnum.Token, Function<OperationStrategyFunction, Object>> mapOperationFunction() {
        Map<ExpressionEnum.Token, Function<OperationStrategyFunction, Object>> map = new HashMap<>();
        /**
         * map
         */
        map.put(ExpressionEnum.Token.OP_NOT,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpNot());
        map.put(ExpressionEnum.Token.OP_POS,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpPos());
        map.put(ExpressionEnum.Token.OP_NEG,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpNeg());
        map.put(ExpressionEnum.Token.OP_ADD,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpAdd());
        map.put(ExpressionEnum.Token.OP_SUB,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpSub());
        map.put(ExpressionEnum.Token.OP_MUL,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpMul());
        map.put(ExpressionEnum.Token.OP_DIV,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpDiv());
        map.put(ExpressionEnum.Token.OP_MOD,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpMod());
        map.put(ExpressionEnum.Token.OP_EQ_STRICT,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpEqStrict());
        map.put(ExpressionEnum.Token.OP_EQ,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpEq());
        map.put(ExpressionEnum.Token.OP_GT,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpGlteq());
        map.put(ExpressionEnum.Token.OP_GTEQ,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpGlteq());
        map.put(ExpressionEnum.Token.OP_LT,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpGlteq());
        map.put(ExpressionEnum.Token.OP_LTEQ,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpGlteq());
        map.put(ExpressionEnum.Token.OP_JOIN,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpJoin());
        map.put(ExpressionEnum.Token.OP_PUT,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpPut());
        map.put(ExpressionEnum.Token.OP_IN,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpIn());
        map.put(ExpressionEnum.Token.DEFAULT,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpDefault());
        map.put(ExpressionEnum.Token.OP_NE,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpNe());
        map.put(ExpressionEnum.Token.OP_NE_STRICT,
                operationStrategyFunction -> operationStrategyFunction.evaluateOpNeStrict());
        return map;
    }
}
