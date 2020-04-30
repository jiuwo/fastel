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
                OperationStrategyFunction::evaluateOpNot);
        map.put(ExpressionEnum.Token.OP_POS,
                OperationStrategyFunction::evaluateOpPos);
        map.put(ExpressionEnum.Token.OP_NEG,
                OperationStrategyFunction::evaluateOpNeg);
        map.put(ExpressionEnum.Token.OP_ADD,
                OperationStrategyFunction::evaluateOpAdd);
        map.put(ExpressionEnum.Token.OP_SUB,
                OperationStrategyFunction::evaluateOpSub);
        map.put(ExpressionEnum.Token.OP_MUL,
                OperationStrategyFunction::evaluateOpMul);
        map.put(ExpressionEnum.Token.OP_DIV,
                OperationStrategyFunction::evaluateOpDiv);
        map.put(ExpressionEnum.Token.OP_MOD,
                OperationStrategyFunction::evaluateOpMod);
        map.put(ExpressionEnum.Token.OP_EQ_STRICT,
                OperationStrategyFunction::evaluateOpEqStrict);
        map.put(ExpressionEnum.Token.OP_EQ,
                OperationStrategyFunction::evaluateOpEq);
        map.put(ExpressionEnum.Token.OP_GT,
                OperationStrategyFunction::evaluateOpGlteq);
        map.put(ExpressionEnum.Token.OP_GTEQ,
                OperationStrategyFunction::evaluateOpGlteq);
        map.put(ExpressionEnum.Token.OP_LT,
                OperationStrategyFunction::evaluateOpGlteq);
        map.put(ExpressionEnum.Token.OP_LTEQ,
                OperationStrategyFunction::evaluateOpGlteq);
        map.put(ExpressionEnum.Token.OP_JOIN,
                OperationStrategyFunction::evaluateOpJoin);
        map.put(ExpressionEnum.Token.OP_PUT,
                OperationStrategyFunction::evaluateOpPut);
        map.put(ExpressionEnum.Token.OP_IN,
                OperationStrategyFunction::evaluateOpIn);
        map.put(ExpressionEnum.Token.DEFAULT,
                OperationStrategyFunction::evaluateOpDefault);
        map.put(ExpressionEnum.Token.OP_NE,
                OperationStrategyFunction::evaluateOpNe);
        map.put(ExpressionEnum.Token.OP_NE_STRICT,
                OperationStrategyFunction::evaluateOpNeStrict);
        return map;
    }
}
