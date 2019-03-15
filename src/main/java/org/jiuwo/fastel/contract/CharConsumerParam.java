package org.jiuwo.fastel.contract;

import lombok.Data;

/**
 * @author Steven Han
 */
@Data
public class CharConsumerParam {
    private StringBuilder stringBuilder;
    private char c;
    private char cNext;
    private int currentIndex;
    private final String elValue;

    public CharConsumerParam(String elValue, StringBuilder stringBuilder, char c, char cNext, int currentIndex) {
        this.elValue = elValue;
        this.stringBuilder = stringBuilder;
        this.c = c;
        this.cNext = cNext;
        this.currentIndex = currentIndex;
    }

    public int plusCurrentIndex(int value) {
        this.currentIndex = this.currentIndex + value;
        return this.currentIndex;
    }
}
