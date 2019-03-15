package org.jiuwo.fastel.parser;

import org.jiuwo.fastel.contract.CharConsumerParam;
import org.jiuwo.fastel.contract.enums.ParserEnum;

/**
 * @author Steven Han
 */
public class CharConsumer {

    public void appendEscape(CharConsumerParam consumerParam) {
        consumerParam.getStringBuilder().append(ParserEnum.CharEscape.getEscape(consumerParam.getC()));
    }

    public void appendEmpty(CharConsumerParam consumerParam) {
        consumerParam.getStringBuilder().append(ParserEnum.CharEscape.CHAR_EMPTY.getEscape());
    }

    public void appendTwoBacksSlash(CharConsumerParam consumerParam) {
        consumerParam.getStringBuilder().append(ParserEnum.CharEscape.CHAR_TWO_BACKS_SLASH.getEscape());
    }

    public void appendSlash(CharConsumerParam consumerParam) {
        consumerParam.getStringBuilder().append(ParserEnum.CharEscape.CHAR_SLASH.getEscape());
    }

    public void appendQuotation(CharConsumerParam consumerParam) {
        consumerParam.getStringBuilder().append(ParserEnum.CharEscape.CHAR_QUOTATION.getEscape());
    }

    public void appendTwoQuotation(CharConsumerParam consumerParam) {
        consumerParam.getStringBuilder().append(ParserEnum.CharEscape.CHAR_TWO_QUOTATION.getEscape());
    }

    public void appendU(CharConsumerParam consumerParam) {
        consumerParam.getStringBuilder().append((char) Integer.parseInt(consumerParam.getElValue()
                .substring(consumerParam.getCurrentIndex(), consumerParam.plusCurrentIndex(4)), 16));
    }

    public void appendX(CharConsumerParam consumerParam) {
        consumerParam.getStringBuilder().append((char) Integer.parseInt(consumerParam.getElValue()
                .substring(consumerParam.getCurrentIndex(), consumerParam.plusCurrentIndex(2)), 16));
    }

    public void appendDefault(CharConsumerParam consumerParam) {
        consumerParam.getStringBuilder().append(consumerParam.getC());
        consumerParam.getStringBuilder().append(consumerParam.getCNext());
    }


}
