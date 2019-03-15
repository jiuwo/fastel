package org.jiuwo.fastel;

import java.util.Map;

import org.jiuwo.fastel.impl.ExpressionNode;


/**
 * 三元运算符，需要转化为二元表示
 * 值类型运算符，创建或者从vs中获取值
 *
 * @author Steven Han
 */
public interface OperationStrategy {

    /**
     * 运算符转化
     *
     * @param node 操作符对象
     * @param vs   运算变量表
     * @return 运算结果
     */
    Object evaluate(ExpressionNode node, Map<String, Object> vs);

}
