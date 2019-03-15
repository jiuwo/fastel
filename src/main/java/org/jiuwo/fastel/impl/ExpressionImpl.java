package org.jiuwo.fastel.impl;

import java.util.HashMap;
import java.util.Map;

import org.jiuwo.fastel.Expression;
import org.jiuwo.fastel.OperationStrategy;
import org.jiuwo.fastel.parser.ExpressionParser;
import org.jiuwo.fastel.util.ValueStackUtil;

/**
 * @author Steven Han
 */
public class ExpressionImpl implements Expression {

    protected OperationStrategy strategy;

    protected ExpressionNode expressionNode;


    public ExpressionImpl() {

    }

    public ExpressionImpl(String el) {
        createExpression(el);
    }

    @Override
    public ExpressionImpl parseExpression(String el) {
        createExpression(el);
        return this;
    }

    private void createExpression(String el) {
        ExpressionParser ep = new ExpressionParser(el);
        this.expressionNode = ep.parseEL();
        this.strategy = new OperationStrategyImpl();
    }

    @Override
    public Object evaluate(Object context) {
        Map<String, Object> contextMap = ValueStackUtil.wrapAsContext(context);
        Object result = strategy.evaluate(expressionNode, contextMap);
        return result;
    }

    @Override
    public Object evaluate(Object... context) {
        if (context == null || context.length == 0) {
            return evaluate((Object) null);
        } else if (context.length == 1) {
            return evaluate(context[0]);
        } else if ((context.length & 1) == 1) {
            throw new IllegalArgumentException("参数必须是偶数个数");
        }
        HashMap<Object, Object> map = new HashMap<>(context.length);
        for (int i = 0; i < context.length; i++) {
            map.put(context[i], context[++i]);
        }
        return evaluate(map);
    }

}
