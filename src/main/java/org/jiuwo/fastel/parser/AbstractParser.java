package org.jiuwo.fastel.parser;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Steven Han
 */
@Getter
@Setter
public abstract class AbstractParser {
    /**
     * 表达式的值
     */
    protected final String elValue;
    /**
     * 当前索引
     */
    protected int currentIndex = 0;
    /**
     * 表达式长度
     */
    protected final int elSize;
    /**
     * 要去掉的空白
     */
    private final String ufeff = "\uFEFF";

    public AbstractParser(String elValue) {
        this.elValue = getElValue(elValue);
        this.elSize = this.elValue.length();
    }

    public AbstractParser(String elValue, int currentIndex) {
        this.elValue = getElValue(elValue);
        this.elSize = this.elValue.length();
        this.currentIndex = currentIndex;
    }

    private String getElValue(String elValue) {
        if (elValue.trim().startsWith(ufeff)) {
            elValue = elValue.trim().substring(1);
        }
        return elValue;
    }

}
