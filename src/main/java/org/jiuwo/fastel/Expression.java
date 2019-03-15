package org.jiuwo.fastel;

import org.jiuwo.fastel.impl.ExpressionImpl;

/**
 * @author Steven Han
 */
public interface Expression {
    /**
     * 根据传入的变量上下文执行表达式
     *
     * @param context 上下文
     * @return 结果
     */
    Object evaluate(Object context);

    /**
     * 根据传入的变量上下文（键值数组），执行表达式
     *
     * @param keyValue 键值对（两个参数代表一个键值对）
     * @return 结果
     */
    Object evaluate(Object... keyValue);

    /**
     * EL 转化表达式
     *
     * @param el EL
     * @return 结果
     */
    ExpressionImpl parseExpression(String el);
}
