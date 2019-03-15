package org.jiuwo.fastel;

/**
 * @author Steven Han
 */
public interface Executable {

    /**
     * 接口调用
     *
     * @param obj  Object参数
     * @param args 数组参数
     * @return 结果
     * @throws Exception 异常
     */
    Object invoke(Object obj, Object... args) throws Exception;
}
