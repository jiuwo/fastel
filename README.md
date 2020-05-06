# FastEL 

#### 项目介绍

* FastEL 快速的表达式解析器

#### 使用说明

* 引用JAR包

    ``` xml
        <dependency>
            <groupId>org.jiuwo</groupId>
            <artifactId>fastel</artifactId>
            <version>1.0.6</version>
        </dependency>
    ```   
* 具体使用

    ``` java
        Expression expression = new ExpressionImpl("2*(3+5)");
        result = expression.evaluate();
        Assert.assertEquals(16L, result);
        
        result = expression
                 .parseExpression("parseInt('2')")
                 .evaluate();
        Assert.assertEquals(2, result);
        
        result = expression
                .parseExpression("Math.abs(-123)")
                .evaluate();
        Assert.assertEquals(123.0, result);
        
    ```   
* 更多用法参考项目中单元测试
