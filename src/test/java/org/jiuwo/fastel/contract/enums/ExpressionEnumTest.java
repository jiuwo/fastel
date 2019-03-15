package org.jiuwo.fastel.contract.enums;

import org.jiuwo.fastel.constant.CalculateConstant;
import org.jiuwo.fastel.constant.CharConstant;
import org.jiuwo.fastel.constant.SysConstant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ExpressionEnum.class, SysConstant.class, CalculateConstant.class, CharConstant.class})
public class ExpressionEnumTest {

    @Before
    public void setUp() {

    }

    @Test
    public void getValueTest() {
        int tokenValue = ExpressionEnum.Token.getValue(ExpressionEnum.Token.OP_JOIN.getKey());
        Assert.assertEquals(ExpressionEnum.Token.OP_JOIN.getValue(), tokenValue);

        String tokenKey = ExpressionEnum.Token.getKey(tokenValue);
        Assert.assertEquals(ExpressionEnum.Token.OP_JOIN.getKey(), tokenKey);

        ExpressionEnum.Token token = ExpressionEnum.Token.getTokenByValue(tokenValue);
        Assert.assertEquals(ExpressionEnum.Token.OP_JOIN, token);

        Assert.assertEquals(ExpressionEnum.Token.OP_QUESTION_SELECT.getKey(), ExpressionEnum.Token.OP_PUT.getKey());

    }
}
