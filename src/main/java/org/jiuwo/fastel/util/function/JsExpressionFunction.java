package org.jiuwo.fastel.util.function;

import java.io.UnsupportedEncodingException;

import org.jiuwo.fastel.constant.CalculateConstant;
import org.jiuwo.fastel.contract.enums.ExpressionEnum;
import org.jiuwo.fastel.exception.FastElException;
import org.jiuwo.fastel.util.EncodingUtil;
import org.jiuwo.fastel.util.ObjectUtil;
import org.jiuwo.fastel.util.ParseUtil;
import com.alibaba.fastjson.JSON;

/**
 * @author Steven Han
 */
public class JsExpressionFunction {

    private final Number xNull;
    private final Number xNaN;
    private final Number yNaN;
    private final ExpressionEnum.JsToken jsToken;
    private final Object[] args;

    public JsExpressionFunction(Object[] args, ExpressionEnum.JsToken jsToken) {
        this.args = args;
        this.xNull = ObjectUtil.getNumberArg(args, 0, null);
        this.xNaN = ObjectUtil.getNumberArg(args, 0, Double.NaN);
        if (args.length > 1) {
            this.yNaN = ObjectUtil.getNumberArg(args, 1, Double.NaN);
        } else {
            this.yNaN = 0;
        }
        this.jsToken = jsToken;
    }

    /**
     * 是否NaN 如果是返回true
     *
     * @return 结果
     */
    public final boolean parseIsNaN() {
        if (xNull == null) {
            return false;
        }
        return Double.isNaN(xNull.doubleValue());
    }

    public final boolean parseIsFinite() {
        if (xNull == null) {
            return true;
        }
        return !Double.isNaN(xNull.doubleValue()) && !Double.isInfinite(xNull.doubleValue());
    }

    /**
     * 转为Int
     *
     * @return Int结果
     */
    public final int parseInt() {
        return Integer.parseInt(this.xNaN.toString(), this.yNaN.intValue() == 0 ? 10 : this.yNaN.intValue());
    }

    public float parseFloat() {
        return this.xNaN.floatValue();
    }

    /**
     * URL Encoder
     *
     * @return 结果
     **/
    public final Object enOrDeCodeURI() {
        try {
            String text = String.valueOf(ObjectUtil.getArg(args, 0, "null"));
            final String charset = String.valueOf(ObjectUtil.getArg(args, 1, "utf-8"));
            Object result = EncodingUtil.enOrDecode(1 == ((int) jsToken.getValue() & CalculateConstant.BIT_PRIORITY_SUB) >> 8,
                    1 == ((int) jsToken.getValue() & CalculateConstant.BIT_PRIORITY) >> 4,
                    text,
                    charset);
            return result;
        } catch (UnsupportedEncodingException ex) {
            throw new FastElException(ex);
        }
    }

    /**
     * min() 方法可返回指定的数字中带有最低值的数字。
     *
     * @return 结果
     */
    public final Number min() {
        if (Math.min(xNaN.doubleValue(), yNaN.doubleValue()) == xNaN.doubleValue()) {
            return xNaN;
        }
        return yNaN;
    }

    /**
     * max() 方法可返回两个指定的数中带有较大的值的那个数。
     *
     * @return 结果
     */
    public final Number max() {
        if (Math.max(xNaN.doubleValue(), yNaN.doubleValue()) == xNaN.doubleValue()) {
            return xNaN;
        }
        return yNaN;
    }

    /**
     * 返回数的绝对值。
     *
     * @return 结果
     */
    public final double abs() {
        return Math.abs(this.xNaN.doubleValue());
    }

    /**
     * 返回数的反余弦值。
     *
     * @return 结果
     */
    public final double acos() {
        return Math.acos(this.xNaN.doubleValue());
    }

    /**
     * 返回数的反正弦值。
     *
     * @return 结果
     */
    public final double asin() {
        return Math.asin(this.xNaN.doubleValue());
    }

    /**
     * 以介于 -PI/2 与 PI/2 弧度之间的数值来返回 x 的反正切值。
     *
     * @return 结果
     */
    public final double atan() {
        return Math.atan(this.xNaN.doubleValue());
    }

    /**
     * 返回从 x 轴到点 (x,y) 的角度（介于 -PI/2 与 PI/2 弧度之间）。
     *
     * @return 结果
     */
    public final double atan2() {
        return Math.atan2(this.xNaN.doubleValue(), this.yNaN.doubleValue());
    }

    /**
     * 对数进行上舍入。
     *
     * @return 结果
     */
    public final double ceil() {
        return Math.ceil(this.xNaN.doubleValue());
    }

    /**
     * 返回数的余弦。
     *
     * @return 结果
     */
    public final double cos() {
        return Math.cos(this.xNaN.doubleValue());
    }

    /**
     * 返回 e 的指数。
     *
     * @return 结果
     */
    public final double exp() {
        return Math.exp(this.xNaN.doubleValue());
    }

    /**
     * floor() 方法可对一个数进行下舍入。
     *
     * @return 结果
     */
    public final double floor() {
        return Math.floor(this.xNaN.doubleValue());
    }

    /**
     * log() 方法可返回一个数的自然对数。
     *
     * @return 结果
     */
    public final double log() {
        return Math.log(this.xNaN.doubleValue());
    }

    /**
     * pow() 方法可返回 x 的 y 次幂的值。
     *
     * @return 结果
     */
    public final double pow() {
        return Math.pow(this.xNaN.doubleValue(), this.yNaN.doubleValue());
    }

    /**
     * random() 方法可返回介于 0 ~ 1 之间的一个随机数。
     *
     * @return 结果
     */
    public final double random() {
        return Math.random();
    }

    /**
     * round() 方法可把一个数字舍入为最接近的整数。
     *
     * @return 结果
     */
    public final long round() {
        return Math.round(this.xNaN.doubleValue());
    }

    /**
     * sin() 方法可返回一个数字的正弦。
     *
     * @return 结果
     */
    public final double sin() {
        return Math.sin(this.xNaN.doubleValue());
    }

    /**
     * sqrt() 方法可返回一个数的平方根。
     *
     * @return 结果
     */
    public final double sqrt() {
        return Math.sqrt(this.xNaN.doubleValue());
    }

    /**
     * tan() 方法可返回一个表示某个角的正切的数字
     *
     * @return 结果
     */
    public final double tan() {
        return Math.tan(this.xNaN.doubleValue());
    }

    /**
     * String 转 JSON
     *
     * @return 结果
     */
    public final Object parse() {
        Object value = ObjectUtil.getStringArg(this.args, 0, null);
        Object result = JSON.parse(ParseUtil.parseToPrimitive(value, String.class).toString());
        return result;
    }

    /**
     * 对象转JSON
     *
     * @return 结果
     */
    public final Object stringify() {
        Object value = ObjectUtil.getArg(this.args, 0, null);
        Object result = JSON.toJSONString(value);
        return result;
    }


}
