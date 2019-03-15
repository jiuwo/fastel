package org.jiuwo.fastel.util.function;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jiuwo.fastel.Executable;
import org.jiuwo.fastel.constant.CalculateConstant;
import org.jiuwo.fastel.constant.map.MapConstant;
import org.jiuwo.fastel.contract.enums.ExpressionEnum;
import org.jiuwo.fastel.exception.FastElException;
import org.jiuwo.fastel.impl.ExpressionNode;
import org.jiuwo.fastel.impl.OperationStrategyImpl;
import org.jiuwo.fastel.util.NumberUtil;
import org.jiuwo.fastel.util.ObjectUtil;
import org.jiuwo.fastel.util.ParseUtil;
import org.jiuwo.fastel.util.ReferenceUtil;
import org.jiuwo.fastel.util.ReflectUtil;
import org.jiuwo.fastel.util.ValueStackUtil;

/**
 * @author Steven Han
 */
public class OperationStrategyFunction {

    private final ExpressionNode node;
    private final Map<String, Object> vs;
    private final OperationStrategyImpl strategy;

    public OperationStrategyFunction(ExpressionNode node,
                                     Map<String, Object> vs,
                                     OperationStrategyImpl strategy) {
        this.node = node;
        this.vs = vs;
        this.strategy = strategy;
    }

    private Object getArgLeft() {
        return strategy.evaluate(node.getLeft(), vs);
    }

    private Object getArgRight() {
        if ((node.getToken().getValue() & CalculateConstant.BIT_ARGS) > 0) {
            return strategy.evaluate(node.getRight(), vs);
        }
        return null;
    }

    public Object evaluateValueVar() {
        Object key = node.getParam();
        return ValueStackUtil.getValueStack(vs, MapConstant.getInstance(), key);
    }

    public Object evaluateValueConstants() {
        return node.getParam();
    }

    public Object evaluateValueList() {
        return new ArrayList<>();
    }

    public Object evaluateValueMap() {
        return new LinkedHashMap<>();
    }

    public Object evaluateOpInvoke() {
        Object[] arguments;
        if (node.getToken().equals(ExpressionEnum.Token.OP_INVOKE)) {
            arguments = ((List<?>) strategy.evaluate(node.getRight(), vs)).toArray();
        } else {
            arguments = (Object[]) node.getParam();
        }
        ExpressionNode left = node.getLeft();
        ExpressionEnum.Token token2 = node.getToken();
        Object base;
        Object key;
        if (token2.equals(ExpressionEnum.Token.OP_GET)) {
            base = strategy.evaluate(left.getLeft(), vs);
            key = strategy.evaluate(left.getRight(), vs);
        } else if (token2 == ExpressionEnum.Token.OP_GET_STATIC) {
            base = strategy.evaluate(left.getLeft(), vs);
            key = left.getRight().getParam();
        } else {
            return ValueStackUtil.invoke(vs, strategy.evaluate(left, vs), arguments);
        }
        return ValueStackUtil.invoke(vs, new ReferenceUtil(base, key), arguments);
    }

    public Object evaluateOpGetStatic() {
        return ReflectUtil.getValue(this.getArgLeft(), node.getParam());
    }

    public Object evaluateOpGet() {
        Object argRight = strategy.evaluate(node.getRight(), vs);
        return ReflectUtil.getValue(this.getArgLeft(), argRight);
    }

    public Object evaluateOpAnd() {
        Object argLeft = this.getArgLeft();
        if (ParseUtil.parseToBoolean(argLeft)) {
            return strategy.evaluate(node.getRight(), vs);
        }
        return argLeft;
    }

    public Object evaluateOpOr() {
        Object argLeft = this.getArgLeft();
        if (ParseUtil.parseToBoolean(argLeft)) {
            return argLeft;
        }
        return strategy.evaluate(node.getRight(), vs);
    }

    public Object evaluateOpQuestionSelect() {
        ExpressionNode nodeLeft = (ExpressionNode) this.getArgLeft();
        if (ParseUtil.parseToBoolean(strategy.evaluate(nodeLeft.getLeft(), vs))) {
            return strategy.evaluate(nodeLeft.getRight(), vs);
        } else {
            return strategy.evaluate(node.getRight(), vs);
        }
    }

    public Object evaluateOpQuestion() {
        return this.node.getToken();
    }

    public Object evaluateOpNot() {
        return !ParseUtil.parseToBoolean(this.getArgLeft());
    }

    public Object evaluateOpPos() {
        return ParseUtil.parseToNumber(this.getArgLeft());
    }

    public Object evaluateOpNeg() {
        return NumberUtil.subtract(0, ParseUtil.parseToNumber(this.getArgLeft()));
    }

    public Object evaluateOpAdd() {
        Object primitiveLeft = ParseUtil.parseToPrimitive(this.getArgLeft(), String.class);
        Object primitiveRight = ParseUtil.parseToPrimitive(this.getArgRight(), String.class);
        if (primitiveLeft instanceof String || primitiveLeft instanceof Character) {
            return primitiveLeft + ParseUtil.parseToString(primitiveRight);
        } else if (primitiveRight instanceof String || primitiveRight instanceof Character) {
            return ParseUtil.parseToString(primitiveLeft) + primitiveRight;
        } else {
            return NumberUtil.plus(ParseUtil.parseToNumber(primitiveLeft), ParseUtil.parseToNumber(primitiveRight));
        }
    }

    public Object evaluateOpSub() {
        return NumberUtil.subtract(ParseUtil.parseToNumber(this.getArgLeft()), ParseUtil.parseToNumber(this.getArgRight()));
    }

    public Object evaluateOpMul() {
        return NumberUtil.multiply(ParseUtil.parseToNumber(this.getArgLeft()), ParseUtil.parseToNumber(this.getArgRight()));
    }

    public Object evaluateOpDiv() {
        return NumberUtil.divide(ParseUtil.parseToNumber(this.getArgLeft()), ParseUtil.parseToNumber(this.getArgRight()));
    }

    public Object evaluateOpMod() {
        return NumberUtil.modulus(ParseUtil.parseToNumber(this.getArgLeft()), ParseUtil.parseToNumber(this.getArgRight()));
    }

    public Object evaluateOpEqStrict() {
        return ObjectUtil.isEquals(this.getArgLeft(), this.getArgRight(), true);
    }

    public Object evaluateOpEq() {
        return ObjectUtil.isEquals(this.getArgLeft(), this.getArgRight(), true);
    }

    public Object evaluateOpNe() {
        return !ObjectUtil.isEquals(this.getArgLeft(), this.getArgRight(), false);
    }

    public Object evaluateOpNeStrict() {
        return !ObjectUtil.isEquals(this.getArgLeft(), this.getArgRight(), true);
    }

    public Object evaluateOpGlteq() {
        return ObjectUtil.compare(this.getArgLeft(), this.getArgRight(), this.node.getToken());
    }

    public Object evaluateOpJoin() {
        Object argLeft = getArgLeft();
        ((List) argLeft).add(getArgRight());
        return argLeft;
    }

    public Object evaluateOpPut() {
        Object argLeft = getArgLeft();
        ((Map) argLeft).put(this.node.getParam(), getArgRight());
        return argLeft;
    }

    public Object evaluateOpIn() {
        return ObjectUtil.in(getArgLeft(), getArgRight());
    }

    public Object evaluateOpDefault() {
        Object argLeft = getArgLeft();
        Object argRight = getArgRight();
        int numberLeft = ParseUtil.parseToNumber(argLeft).intValue();
        int numberRight = ParseUtil.parseToNumber(argRight).intValue();
        switch (this.node.getToken()) {
            case OP_BIT_AND:
                return numberLeft & numberRight;
            case OP_BIT_XOR:
                return numberLeft ^ numberRight;
            case OP_BIT_OR:
                return numberLeft | numberRight;
            case OP_LSH:
                return numberLeft << numberRight;
            case OP_RSH:
                return numberLeft >> numberRight;
            case OP_URSH:
                return numberLeft >>> numberRight;
            default:
                break;

        }

        Object impl = MapConstant.getInstance().get(node.getToken());
        if (impl != null) {
            Executable method = (Executable) impl;
            try {
                return method.invoke(null, argLeft, argRight);
            } catch (Exception e) {
                throw new FastElException("方法调用失败:" + argLeft, e);
            }
        }
        throw new FastElException("不支持的操作符" + node.getToken());
    }
}
