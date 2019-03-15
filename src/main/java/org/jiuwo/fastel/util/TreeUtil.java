package org.jiuwo.fastel.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jiuwo.fastel.constant.CalculateConstant;
import org.jiuwo.fastel.contract.enums.ExpressionEnum;
import org.jiuwo.fastel.impl.ExpressionNode;

import static org.jiuwo.fastel.contract.enums.ExpressionEnum.Token.BRACKET_BEGIN;
import static org.jiuwo.fastel.contract.enums.ExpressionEnum.Token.BRACKET_END;
import static org.jiuwo.fastel.contract.enums.ExpressionEnum.Token.OP_QUESTION;
import static org.jiuwo.fastel.contract.enums.ExpressionEnum.Token.OP_QUESTION_SELECT;

/**
 * @author Steven Han
 */
public class TreeUtil {

    public static void prepareSelect(ArrayList<ExpressionNode> expressionNodeArrayList) {
        int nodeArraySize = expressionNodeArrayList.size();
        while (nodeArraySize-- > 0) {
            ExpressionEnum.Token token1 = expressionNodeArrayList.get(nodeArraySize).getToken();
            if (token1 == OP_QUESTION) {
                // (a?b
                int pos = TreeUtil.getSelectRange(nodeArraySize, -1, -1, expressionNodeArrayList);
                expressionNodeArrayList.add(pos + 1, new ExpressionNode(BRACKET_BEGIN, null));
                nodeArraySize++;
            } else if (token1 == OP_QUESTION_SELECT) {
                int end = expressionNodeArrayList.size();
                int pos = TreeUtil.getSelectRange(nodeArraySize, 1, end, expressionNodeArrayList);
                expressionNodeArrayList.add(pos, new ExpressionNode(BRACKET_END, null));
            }
        }
    }

    public static void toTree(List<ExpressionNode> nodes, LinkedList<ExpressionNode> stack) {
        for (final ExpressionNode item : nodes) {
            ExpressionEnum.Token token = item.getToken();
            switch (token) {
                case VALUE_CONSTANTS:
                case VALUE_VAR:
                case VALUE_LIST:
                case VALUE_MAP:
                    stack.addFirst(item);
                    break;
                default:// OP
                    if ((token.getValue() & CalculateConstant.BIT_ARGS) > 0) {
                        // 两个操作数
                        ExpressionNode right = stack.removeFirst();
                        ExpressionNode left = stack.removeFirst();
                        item.setLeft(left);
                        item.setRight(right);
                        stack.addFirst(item);
                    } else {
                        // 一个操作树
                        ExpressionNode arg1 = stack.removeFirst();
                        item.setLeft(arg1);
                        stack.addFirst(item);
                    }
            }
        }
    }

    /**
     * 将中序表达式转换为右序表达式
     *
     * @param nodes 结点
     * @return 结果
     */
    public static List<ExpressionNode> right(List<ExpressionNode> nodes) {
        LinkedList<List<ExpressionNode>> rightStack = new LinkedList<>();
        // 存储右序表达式
        rightStack.addFirst(new ArrayList<>());

        LinkedList<ExpressionNode> buffer = new LinkedList<>();

        for (final ExpressionNode item : nodes) {
            if (item.getToken().getValue() > 0 || item.getToken().getValue() <= BRACKET_BEGIN.getValue()) {
                if (buffer.isEmpty()) {
                    buffer.addFirst(item);
                } else if (item.getToken().equals(BRACKET_BEGIN)) {
                    // ("(")
                    buffer.addFirst(item);
                } else if (item.getToken().equals(BRACKET_END)) {
                    // .equals(")"))
                    while (true) {
                        ExpressionNode operator = buffer.removeFirst();
                        if (operator.getToken().equals(BRACKET_BEGIN)) {
                            break;
                        }
                        addRightNode(rightStack, operator);
                    }
                } else {
                    while (!buffer.isEmpty()
                            && rightEnd(item, buffer.getFirst())) {
                        ExpressionNode operator = buffer.removeFirst();
                        addRightNode(rightStack, operator);
                    }
                    buffer.addFirst(item);
                }
            } else {// lazy begin value exp
                addRightNode(rightStack, item);
            }
        }
        while (!buffer.isEmpty()) {
            ExpressionNode operator = buffer.removeFirst();
            addRightNode(rightStack, operator);
        }
        return rightStack.getFirst();
    }

    public static void addRightNode(LinkedList<List<ExpressionNode>> rightStack,
                                    ExpressionNode node) {
        List<ExpressionNode> list = rightStack.getFirst();
        list.add(node);
    }

    public static boolean rightEnd(ExpressionNode node, ExpressionNode left) {
        ExpressionEnum.Token leftToken = left.getToken();
        ExpressionEnum.Token token = node.getToken();
        int leftPriority = TreeUtil.getPriority(leftToken);
        int priority = TreeUtil.getPriority(token);

        if (leftPriority == priority && ExpressionNode.isPrefix(token)) {
            return false;
        }

        return priority <= leftPriority;
    }

    public static int getSelectRange(int p2, int inc, int end, ArrayList<ExpressionNode> expressionNodeArrayList) {
        int dep = 0;
        while ((p2 += inc) != end) {
            ExpressionEnum.Token token2 = expressionNodeArrayList.get(p2).getToken();
            if (token2.getValue() > 0) {
                // op
                if (token2 == BRACKET_BEGIN) {
                    dep += inc;
                } else if (token2 == BRACKET_END) {
                    dep -= inc;
                } else {
                    if (dep == 0 && getPriority(token2) <= getPriority(OP_QUESTION)) {
                        return p2;
                    }
                }
                if (dep < 0) {
                    return p2;
                }
            }
        }
        return inc > 0 ? end : -1;
    }


    private static int getPriority(ExpressionEnum.Token token) {
        switch (token) {
            case BRACKET_BEGIN:
            case BRACKET_END:
                return Integer.MIN_VALUE;
            default:
                //TODO: 需要重新计算移位
                return (token.getValue() & CalculateConstant.BIT_PRIORITY) << 4
                        | (token.getValue() & CalculateConstant.BIT_PRIORITY_SUB) >> 8;
        }
    }
}
