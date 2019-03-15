package org.jiuwo.fastel.impl;

import java.util.Map;
import java.util.function.Function;

import org.jiuwo.fastel.OperationStrategy;
import org.jiuwo.fastel.constant.map.MapStrategyLogicFunctionConstant;
import org.jiuwo.fastel.constant.map.MapStrategyOperationFunctionConstant;
import org.jiuwo.fastel.constant.map.MapStrategyStaticFunctionConstant;
import org.jiuwo.fastel.contract.enums.ExpressionEnum;
import org.jiuwo.fastel.util.function.OperationStrategyFunction;

/**
 * @author Steven Han
 */
public class OperationStrategyImpl implements OperationStrategy {

    @Override
    public Object evaluate(ExpressionNode node, Map<String, Object> vs) {
        OperationStrategyFunction operationStrategyFunction = new OperationStrategyFunction(node, vs, this);
        Function<OperationStrategyFunction, Object> staticFunction =
                MapStrategyStaticFunctionConstant.getInstance().get(node.getToken());
        if (staticFunction != null) {
            return staticFunction.apply(operationStrategyFunction);
        }

        Function<OperationStrategyFunction, Object> logicFunction =
                MapStrategyLogicFunctionConstant.getInstance().get(node.getToken());
        if (logicFunction != null) {
            return logicFunction.apply(operationStrategyFunction);
        }

        Function<OperationStrategyFunction, Object> operationFunction =
                MapStrategyOperationFunctionConstant.getInstance().get(node.getToken());
        if (operationFunction != null) {
            return operationFunction.apply(operationStrategyFunction);
        }

        return MapStrategyOperationFunctionConstant.getInstance()
                .get(ExpressionEnum.Token.DEFAULT)
                .apply(operationStrategyFunction);

    }

}