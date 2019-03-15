package org.jiuwo.fastel.contract;

import org.jiuwo.fastel.parser.AbstractParser;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Steven Han
 */
@Setter
@Getter
public class ParserParam extends AbstractParser {

    public ParserParam(String elValue, int currentIndex) {
        super(elValue, currentIndex);
    }

    public int plusOne() {
        return this.currentIndex++;
    }

    public int subtractOne() {
        return this.currentIndex--;
    }

}
