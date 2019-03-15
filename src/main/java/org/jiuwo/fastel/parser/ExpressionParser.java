package org.jiuwo.fastel.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.BiConsumer;

import org.jiuwo.fastel.constant.CharConstant;
import org.jiuwo.fastel.constant.SysConstant;
import org.jiuwo.fastel.constant.map.MapParserStringConsumerConstant;
import org.jiuwo.fastel.contract.CharConsumerParam;
import org.jiuwo.fastel.contract.ParserParam;
import org.jiuwo.fastel.contract.enums.ExpressionEnum;
import org.jiuwo.fastel.contract.enums.ParserEnum;
import org.jiuwo.fastel.exception.FastElException;
import org.jiuwo.fastel.impl.ExpressionNode;
import org.jiuwo.fastel.util.CharUtil;
import org.jiuwo.fastel.util.ExecutableUtil;
import org.jiuwo.fastel.util.NumberUtil;
import org.jiuwo.fastel.util.StringUtil;
import org.jiuwo.fastel.util.TreeUtil;

import static org.jiuwo.fastel.contract.enums.ExpressionEnum.Token.*;

/**
 * @author Steven Han
 */
public class ExpressionParser extends AbstractParser {

    private final static ExpressionNode NODE_TRUE = new ExpressionNode(VALUE_CONSTANTS, Boolean.TRUE);
    private final static ExpressionNode NODE_FALSE = new ExpressionNode(VALUE_CONSTANTS, Boolean.FALSE);
    private final static ExpressionNode NODE_NULL = new ExpressionNode(VALUE_CONSTANTS, null);

    protected ArrayList<ExpressionNode> expressionNodeArrayList = new ArrayList<>();
    private ParserEnum.ParseStatus status = ParserEnum.ParseStatus.BEGIN;
    private ExpressionEnum.Token previousType = null;
    private Map<String, ExpressionEnum.Token> aliasMap = Collections.emptyMap();
    private int depth;

    public ExpressionParser(String elValue) {
        super(elValue);
    }

    public ExpressionNode parseEL() {
        while (currentIndex < elSize) {
            skipSpace();
            char c = Character.toLowerCase(elValue.charAt(currentIndex));
            if (c == CharConstant.CHAR_TWO_QUOTATION || c == CharConstant.CHAR_QUOTATION) {
                String text = findString();
                addKeyOrObject(text, false);
            } else if (c >= CharConstant.CHAR_NUMBER_ZERO && c <= CharConstant.CHAR_NUMBER_NINE) {
                Number number = findNumber();
                addKeyOrObject(number, false);
            } else if (Character.isJavaIdentifierStart(c)) {
                String id = findId();
                addId(id);
            } else {
                String op = findOperator();
                addOperator(op);
            }
        }

        return getExpressionNode();
    }

    private ExpressionNode getExpressionNode() {
        if (depth != 0) {
            this.buildFail("表达式括弧不匹配", null);
        }
        TreeUtil.prepareSelect(expressionNodeArrayList);
        LinkedList<ExpressionNode> stack = new LinkedList<>();
        try {
            TreeUtil.toTree(TreeUtil.right(this.expressionNodeArrayList), stack);
        } catch (Exception e) {
            this.buildFail("逆波兰式树型化异常", e);
        }
        if (stack.size() != 1) {
            this.buildFail("表达式语法错误", null);
        }

        return stack.getFirst();
    }

    private void addKeyOrObject(Object object, boolean isVar) {
        if (isEqualsChar(CharConstant.CHAR_COLON) && ExecutableUtil.isMapMethod(expressionNodeArrayList)) {
            addNode(new ExpressionNode(OP_PUT, object));
            this.currentIndex++;
        } else if (isVar) {
            addNode(new ExpressionNode(VALUE_VAR, object));
        } else {
            addNode(new ExpressionNode(VALUE_CONSTANTS, object));
        }
    }

    private void addNode(ExpressionNode expressionNode) {
        ExpressionEnum.Token token = expressionNode.getToken();
        //TODO: 疑问注释：invoke 在invoke解析时处理纠正歧异
        if (token.equals(BRACKET_BEGIN) || token.getValue() < 0) {
            replacePrevious();
        }
        if (token == VALUE_VAR) {
            Object id = expressionNode.getParam();
            ExpressionEnum.Token op = aliasMap.get(id);
            if (op == null && SysConstant.SYS_IN.equals(id)) {
                op = OP_IN;
            }
            if (op != null && isArgCountAndParseStatus(op)) {
                expressionNode = new ExpressionNode(op, null);
            }
        }

        switch (expressionNode.getToken()) {
            case BRACKET_BEGIN:
                depth++;
                status = ParserEnum.ParseStatus.BEGIN;
                break;
            case BRACKET_END:
                depth--;
                if (depth < 0) {
                    buildFail("括弧异常", null);
                }
                break;
            case VALUE_CONSTANTS:
            case VALUE_VAR:
            case VALUE_LIST:
            case VALUE_MAP:
                status = ParserEnum.ParseStatus.EXPRESSION;
                break;
            default:
                status = ParserEnum.ParseStatus.OPERATOR;
                break;
        }
        previousType = token;
        expressionNodeArrayList.add(expressionNode);
    }

    private boolean isArgCountAndParseStatus(ExpressionEnum.Token op) {
        int argCount = ExpressionNode.getArgCount(op);
        return argCount == SysConstant.SYS_NUMBER_TWO
                && status.equals(ParserEnum.ParseStatus.EXPRESSION)
                || argCount == SysConstant.SYS_NUMBER_ONE
                && !status.equals(ParserEnum.ParseStatus.EXPRESSION);
    }


    protected String findString() {
        char quoteChar = elValue.charAt(currentIndex++);
        StringBuilder stringBuilder = new StringBuilder();
        while (currentIndex < elSize) {
            char c = elValue.charAt(currentIndex++);
            switch (c) {
                case CharConstant.CHAR_BACKSLASH:
                    findStringBackslash(stringBuilder, c);
                    break;
                case CharConstant.CHAR_TWO_QUOTATION:
                case CharConstant.CHAR_QUOTATION:
                    if (c == quoteChar) {
                        return (stringBuilder.toString());
                    }
                    stringBuilder.append(c);
                    break;
                case CharConstant.CHAR_ESCAPE_R:
                case CharConstant.CHAR_ESCAPE_N:
                    throw buildError("JSON 标准字符串不能换行");
                default:
                    stringBuilder.append(c);
                    break;
            }
        }
        throw buildError("未结束字符串");
    }

    private void findStringBackslash(StringBuilder stringBuilder, char c) {
        char cNext = elValue.charAt(currentIndex++);
        CharConsumer charConsumer = new CharConsumer();
        CharConsumerParam consumerParam = new CharConsumerParam(this.elValue, stringBuilder, c, cNext, this.currentIndex);
        BiConsumer<CharConsumer, CharConsumerParam> consumer = MapParserStringConsumerConstant.getInstance().get(cNext);
        if (consumer == null) {
            charConsumer.appendDefault(consumerParam);
        } else {
            consumer.accept(charConsumer, consumerParam);
            this.currentIndex = consumerParam.getCurrentIndex();
        }
    }

    protected FastElException buildError(String msg) {
        return new FastElException(String.format("语法错误:%s%n%s@%s", msg, elValue, currentIndex));
    }

    protected boolean isEqualsChar(int value) {
        if (value > 0 && currentIndex < elSize) {
            if (value == elValue.charAt(currentIndex)) {
                return true;
            }
        }
        return false;
    }

    private void replacePrevious() {
        int last = expressionNodeArrayList.size() - 1;
        if (previousType == VALUE_VAR && last >= 0) {
            ExpressionNode lt = expressionNodeArrayList.get(last);
            ExpressionEnum.Token op = aliasMap.get(lt.getParam());
            if (op != null) {
                expressionNodeArrayList.set(last, new ExpressionNode(op, null));
                status = ParserEnum.ParseStatus.OPERATOR;
                previousType = op;
            }
        }
    }

    private void addId(String id) {
        switch (id) {
            case SysConstant.SYS_TRUE:
                addNode(NODE_TRUE);
                break;
            case SysConstant.SYS_FALSE:
                addNode(NODE_FALSE);
                break;
            case SysConstant.SYS_NULL:
                addNode(NODE_NULL);
                break;
            default:
                skipSpace();
                if (previousType == OP_GET) {
                    addNode(new ExpressionNode(VALUE_CONSTANTS, id));
                } else {
                    addKeyOrObject(id, true);
                }
                break;
        }
    }

    /**
     * 还是改成JDK自己的parser？
     *
     * @return 结果
     */
    protected Number findNumber() {
        // 10进制优化
        final int begin = currentIndex;
        boolean nag = false;
        char c = elValue.charAt(currentIndex++);
        if (c == CharConstant.CHAR_PLUS) {
            c = elValue.charAt(currentIndex++);
        } else if (c == CharConstant.CHAR_HYPHEN) {
            nag = true;
            c = elValue.charAt(currentIndex++);
        }

        if (c == CharConstant.CHAR_NUMBER_ZERO) {
            ParserParam parserParam = new ParserParam(this.elValue, this.currentIndex);
            Number result = NumberUtil.parseZero(nag, parserParam);
            currentIndex = parserParam.getCurrentIndex();
            return result;
        } else {
            long numberValue = c - CharConstant.CHAR_NUMBER_ZERO;
            while (currentIndex < elSize) {
                c = elValue.charAt(currentIndex++);
                if (c >= CharConstant.CHAR_NUMBER_ZERO && c <= CharConstant.CHAR_NUMBER_NINE) {
                    numberValue = numberValue * 10 + (c - CharConstant.CHAR_NUMBER_ZERO);
                } else {
                    if (c == CharConstant.CHAR_DOT || c == CharConstant.CHAR_E_UPPER) {
                        currentIndex--;
                        ParserParam parserParam = new ParserParam(this.elValue, this.currentIndex);
                        Number result = NumberUtil.parseFloat(begin, parserParam);
                        this.currentIndex = parserParam.getCurrentIndex();
                        return result;
                    } else {
                        currentIndex--;
                        break;
                    }
                }
            }
            return nag ? -numberValue : numberValue;
        }
    }

    protected String findId() {
        int idIndex = currentIndex;
        if (Character.isJavaIdentifierPart(elValue.charAt(idIndex++))) {
            while (idIndex < elSize) {
                if (!Character.isJavaIdentifierPart(elValue.charAt(idIndex))) {
                    break;
                }
                idIndex++;
            }
            return (elValue.substring(currentIndex, currentIndex = idIndex));
        }
        throw buildError("无效id");

    }

    private String findOperator() {
        int end = currentIndex + 1;
        final char c = CharUtil.sbc2Dbc(elValue.charAt(currentIndex));
        final char next = elValue.length() > end ? CharUtil.sbc2Dbc(elValue.charAt(end)) : 0;

        switch (c) {
            case CharConstant.CHAR_COMMA:
            case CharConstant.CHAR_COLON:
            case CharConstant.CHAR_BRACKET_LEFT:
            case CharConstant.CHAR_BRACKET_RIGHT:
            case CharConstant.CHAR_BRACE_LEFT:
            case CharConstant.CHAR_BRACE_RIGHT:
            case CharConstant.CHAR_PARENTHESIS_LEFT:
            case CharConstant.CHAR_PARENTHESIS_RIGHT:
            case CharConstant.CHAR_DOT:
            case CharConstant.CHAR_QUESTION_MARK:
            case CharConstant.CHAR_PLUS:
            case CharConstant.CHAR_HYPHEN:
            case CharConstant.CHAR_NOT:
            case CharConstant.CHAR_XOR:
            case CharConstant.CHAR_STAR:
            case CharConstant.CHAR_SLASH:
            case CharConstant.CHAR_PERCENT:
                break;
            case CharConstant.CHAR_EQUAL:
                end = findOperatorEqual(end, next);
                break;
            case CharConstant.CHAR_EXCLAMATION_POINT:
                end = findOperatorExclamationPoint(end, next);
                break;
            case CharConstant.CHAR_GT:
            case CharConstant.CHAR_LT:
                end = findOperatorGtLt(end, c, next);
                break;
            case CharConstant.CHAR_AND:
            case CharConstant.CHAR_VERTICAL:
                if ((c == next)) {
                    end++;
                }
                break;
            default:
                return null;
        }

        return StringUtil.sbc2Dbc(elValue.substring(currentIndex, currentIndex = end));
    }

    private int findOperatorGtLt(int end, char c, char next) {
        if (next == CharConstant.CHAR_EQUAL) {
            end++;
        } else if (next == c) {
            end++;
            if (elValue.length() > end && CharUtil.sbc2Dbc(elValue.charAt(end)) == c) {
                end++;
            }

        }
        return end;
    }

    private int findOperatorEqual(int end, char next) {
        if (next != CharConstant.CHAR_EQUAL) {
            this.buildFail("不支持赋值操作:", null);
        }
        end++;
        return end;
    }

    private int findOperatorExclamationPoint(int end, char next) {
        if (next == CharConstant.CHAR_EQUAL) {
            end++;
            if (elValue.length() > end && CharUtil.sbc2Dbc(elValue.charAt(end)) == CharConstant.CHAR_EQUAL) {
                end++;
                this.buildFail("不支持=== 和!==操作符，请使用==,!=", null);
            }
        }
        return end;
    }

    private void addOperator(String op) {
        if (op == null) {
            this.buildFail("未知操作符:null", null);
        }
        if (op.length() == 1) {
            switch (op.charAt(0)) {
                case CharConstant.CHAR_PARENTHESIS_LEFT:
                    addOperatorParenthesisLeft();
                    break;
                case CharConstant.CHAR_BRACKET_LEFT:
                    addOperatorBracketLeft();
                    break;
                case CharConstant.CHAR_BRACE_LEFT:
                    addMap();
                    break;
                case CharConstant.CHAR_BRACE_RIGHT:
                case CharConstant.CHAR_BRACKET_RIGHT:
                case CharConstant.CHAR_PARENTHESIS_RIGHT:
                    addNode(new ExpressionNode(BRACKET_END, null));
                    break;
                case CharConstant.CHAR_PLUS:
                    addNode(new ExpressionNode(
                            status == ParserEnum.ParseStatus.EXPRESSION ? OP_ADD : OP_POS,
                            null));
                    break;
                case CharConstant.CHAR_HYPHEN:
                    addNode(new ExpressionNode(
                            status == ParserEnum.ParseStatus.EXPRESSION ? OP_SUB : OP_NEG,
                            null));
                    break;
                case CharConstant.CHAR_COLON:
                    addNode(new ExpressionNode(OP_QUESTION_SELECT, null));
                    break;
                case CharConstant.CHAR_COMMA:
                    addOperatorComma();
                    break;
                case CharConstant.CHAR_SLASH:
                    addOperatorSlash(op);
                    break;
                default:
                    addNode(new ExpressionNode(op));
                    break;
            }
        } else {
            addNode(new ExpressionNode(op));
        }
    }

    private void addOperatorSlash(String op) {
        char next = elValue.charAt(currentIndex);
        if (next == CharConstant.CHAR_SLASH || next == CharConstant.CHAR_STAR) {
            currentIndex--;
            skipComment();
            return;
        } else if (this.status != ParserEnum.ParseStatus.EXPRESSION) {
            int end = findRegExp(this.elValue, this.currentIndex);
            if (end > 0) {
                String regexp = this.elValue.substring(this.currentIndex - 1,
                        end);
                Map<String, Object> value = new HashMap<>(2);
                value.put(SysConstant.SYS_CLASS, SysConstant.SYS_REGEXP);
                value.put(SysConstant.SYS_LITERAL, regexp);
                this.addNode(new ExpressionNode(VALUE_CONSTANTS, value));
                this.currentIndex = end;
                return;
            }
        }
        addNode(new ExpressionNode(op));
    }

    private void addOperatorComma() {
        if (ExecutableUtil.isMapMethod(expressionNodeArrayList)) {
            status = ParserEnum.ParseStatus.OPERATOR;
        } else {
            addNode(new ExpressionNode(OP_JOIN, null));
        }
    }

    private void addOperatorBracketLeft() {
        if (status == ParserEnum.ParseStatus.EXPRESSION) {
            addNode(new ExpressionNode(OP_GET, null));
            addNode(new ExpressionNode(BRACKET_BEGIN, null));
        } else {
            addList();
        }
    }

    /**
     * 左小括号
     */
    private void addOperatorParenthesisLeft() {
        replacePrevious();
        if (status == ParserEnum.ParseStatus.EXPRESSION) {
            addNode(new ExpressionNode(OP_INVOKE, null));
            if (isEqualsChar(CharConstant.CHAR_PARENTHESIS_RIGHT)) {
                addNode(new ExpressionNode(VALUE_CONSTANTS,
                        Collections.EMPTY_LIST));
                currentIndex++;
            } else {
                addList();
            }

        } else {
            addNode(new ExpressionNode(BRACKET_BEGIN, null));
        }
    }

    private void addList() {
        addNode(new ExpressionNode(BRACKET_BEGIN, null));
        addNode(new ExpressionNode(VALUE_LIST, null));
        if (!isEqualsChar(CharConstant.CHAR_BRACKET_RIGHT)) {
            addNode(new ExpressionNode(OP_JOIN, null));
        }
    }

    private void addMap() {
        addNode(new ExpressionNode(BRACKET_BEGIN, null));
        addNode(new ExpressionNode(VALUE_MAP, null));
    }

    int findRegExp(String text, int start) {
        int depth = 0;
        int end = text.length();
        char c;
        while (start < end) {
            c = text.charAt(start++);
            switch (c) {
                case CharConstant.CHAR_BRACKET_LEFT:
                    depth = 1;
                    break;
                case CharConstant.CHAR_BRACKET_RIGHT:
                    depth = 0;
                    break;
                case CharConstant.CHAR_BACKSLASH:
                    start++;
                    break;
                case CharConstant.CHAR_SLASH:
                    if (depth == 0) {
                        while (start < end) {
                            c = text.charAt(start++);
                            switch (c) {
                                case CharConstant.CHAR_G:
                                case CharConstant.CHAR_I:
                                case CharConstant.CHAR_M:
                                    break;
                                default:
                                    return start - 1;
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return -1;
    }


    private void buildFail(String msg, Throwable throwable) {
        String message = String.format("%s%n@%s%n%s%n----%n%s",
                msg,
                currentIndex,
                elValue.substring(currentIndex),
                elValue);
        if (throwable == null) {
            throw new FastElException(message);
        }
        throw new FastElException(message, throwable);
    }

    /**
     * 空格不处理，遇到空格当前索引前进
     */
    private void skipSpace() {
        while (currentIndex < elSize) {
            if (!Character.isWhitespace(elValue.charAt(currentIndex))) {
                break;
            }
            currentIndex++;
        }
    }

    /**
     * 注释不处理，遇到注释当前过引前进
     */
    protected void skipComment() {
        while (true) {
            skipSpace();
            if (currentIndex < elSize && elValue.charAt(currentIndex) == CharConstant.CHAR_SLASH) {
                currentIndex++;
                char next = elValue.charAt(currentIndex++);
                if (next == CharConstant.CHAR_SLASH) {
                    skipCommentNextSlash();
                } else if (next == CharConstant.CHAR_STAR) {
                    skipCommentNextStar();
                }
            } else {
                break;
            }
        }
    }

    private void skipCommentNextStar() {
        int cend = currentIndex + 1;
        while (true) {
            cend = this.elValue.indexOf(CharConstant.CHAR_SLASH, cend);
            if (cend > 0) {
                if (this.elValue.charAt(cend - 1) == CharConstant.CHAR_STAR) {
                    currentIndex = cend + 1;
                    break;
                } else {
                    cend++;
                }
            } else {
                throw buildError("未结束注释");
            }
        }
    }

    private void skipCommentNextSlash() {
        int end1 = this.elValue.indexOf(CharConstant.CHAR_ESCAPE_N, currentIndex);
        int end2 = this.elValue.indexOf(CharConstant.CHAR_ESCAPE_R, currentIndex);
        int cend = Math.min(end1, end2);
        if (cend < 0) {
            cend = Math.max(end1, end2);
        }
        if (cend > 0) {
            currentIndex = cend;
        } else {
            currentIndex = this.elSize;
        }
    }


}
