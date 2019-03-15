package org.jiuwo.fastel.impl;

import org.jiuwo.fastel.contract.enums.ExpressionEnum;
import org.jiuwo.fastel.exception.FastElException;

import static org.jiuwo.fastel.constant.CalculateConstant.BIT_ARGS;

/**
 * @author Steven Han
 */
public class ExpressionNode {

    private ExpressionEnum.Token token;
    private ExpressionNode left;
    private ExpressionNode right;
    private Object param;

    public ExpressionNode(ExpressionEnum.Token token, Object param) {
        this.token = token;
        this.param = param;
    }

    public ExpressionNode(String name) {
        this.token = ExpressionEnum.Token.getTokenByKey(name);
        if (this.token == null) {
            throw new FastElException("未知操作符：" + name);
        }
    }

    public static int getArgCount(ExpressionEnum.Token token) {
        if (token.getValue() < 0) {
            return 0;
        }
        int c = (token.getValue() & BIT_ARGS) >> 8;
        return c + 1;
    }

    public static boolean isPrefix(ExpressionEnum.Token token) {
        return getArgCount(token) == 1;
    }


    public ExpressionEnum.Token getToken() {
        return token;
    }


    public ExpressionNode getLeft() {
        return left;
    }

    public void setLeft(ExpressionNode left) {
        this.left = left;
    }


    public ExpressionNode getRight() {
        return right;
    }

    public void setRight(ExpressionNode right) {
        this.right = right;
    }


    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

}