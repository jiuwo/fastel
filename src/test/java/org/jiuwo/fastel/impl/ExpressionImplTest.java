package org.jiuwo.fastel.impl;

import org.jiuwo.fastel.Expression;
import org.jiuwo.fastel.constant.CalculateConstant;
import org.jiuwo.fastel.constant.CharConstant;
import org.jiuwo.fastel.constant.SysConstant;
import org.jiuwo.fastel.contract.enums.ExpressionEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author Steven Han
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ExpressionEnum.class, SysConstant.class, CalculateConstant.class, CharConstant.class})
public class ExpressionImplTest {

    ExpressionImpl expression;
    Object result;
    String json;
    String jsonEscape;
    String jsonSimple;

    @Before
    public void setUp() {
        json = "{\"returncode\":0,\"message\":\"success\",\"result\":\"\\uD83D\\uDE00\"}";
        jsonEscape = "\"{\\\"result\\\":\\\"\uD83D\uDE00\\\",\\\"returncode\\\":0,\\\"message\\\":\\\"success\\\"}\"";
        jsonSimple = "{\"returncode\":0,\"message\":\"查询成功!\",\"result\":{\"sk\":{\"temp\":\"21\",\"wind_direction\":\"西风\",\"wind_strength\":\"2级\",\"humidity\":\"4%\",\"time\":\"14:25\"},\"today\":{\"city\":\"天津\",\"date_y\":\"2014年03月21日\",\"week\":\"星期五\",\"temperature\":\"8℃~20℃\",\"weather\":\"晴转霾\",\"weather_id\":{\"fa\":\"00\",\"fb\":\"53\"},\"wind\":\"西南风微风\",\"dressing_index\":\"较冷\",\"dressing_advice\":\"建议着大衣、呢外套加毛衣、卫衣等服装。\",\"uv_index\":\"中等\",\"comfort_index\":\"\",\"wash_index\":\"较适宜\",\"travel_index\":\"适宜\",\"exercise_index\":\"较适宜\",\"drying_index\":\"\"},\"future\":[{\"temperature\":\"28℃~36℃\",\"weather\":\"晴转多云\",\"weather_id\":{\"fa\":\"00\",\"fb\":\"01\"},\"wind\":\"南风3-4级\",\"week\":\"星期一\",\"date\":\"20140804\"},{\"temperature\":\"28℃~36℃\",\"weather\":\"晴转多云\",\"weather_id\":{\"fa\":\"00\",\"fb\":\"01\"},\"wind\":\"东南风3-4级\",\"week\":\"星期二\",\"date\":\"20140805\"},{\"temperature\":\"27℃~35℃\",\"weather\":\"晴转多云\",\"weather_id\":{\"fa\":\"00\",\"fb\":\"01\"},\"wind\":\"东南风3-4级\",\"week\":\"星期三\",\"date\":\"20140806\"},{\"temperature\":\"27℃~34℃\",\"weather\":\"多云\",\"weather_id\":{\"fa\":\"01\",\"fb\":\"01\"},\"wind\":\"东南风3-4级\",\"week\":\"星期四\",\"date\":\"20140807\"},{\"temperature\":\"27℃~33℃\",\"weather\":\"多云\",\"weather_id\":{\"fa\":\"01\",\"fb\":\"01\"},\"wind\":\"东北风4-5级\",\"week\":\"星期五\",\"date\":\"20140808\"},{\"temperature\":\"26℃~33℃\",\"weather\":\"多云\",\"weather_id\":{\"fa\":\"01\",\"fb\":\"01\"},\"wind\":\"北风4-5级\",\"week\":\"星期六\",\"date\":\"20140809\"},{\"temperature\":\"26℃~33℃\",\"weather\":\"多云\",\"weather_id\":{\"fa\":\"01\",\"fb\":\"01\"},\"wind\":\"北风4-5级\",\"week\":\"星期日\",\"date\":\"20140810\"}]},\"error_code\":0}";
        expression = new ExpressionImpl();
    }

    @Test
    public void evaluateTest() {
        result = expression
                .parseExpression("9/2")
                .evaluate();
        Assert.assertEquals(4.5, result);

        result = expression
                .parseExpression("8/2")
                .evaluate();
        Assert.assertEquals(4.0, result);

        result = expression
                .parseExpression("5-2")
                .evaluate();
        Assert.assertEquals(3L, result);

        result = expression
                .parseExpression("5+-2")
                .evaluate();
        Assert.assertEquals(3L, result);

        expression = new ExpressionImpl("2*3");
        result = expression.evaluate();
        Assert.assertEquals(6L, result);

        expression = new ExpressionImpl("2*(3+5)");
        result = expression.evaluate();
        Assert.assertEquals(16L, result);

        expression = new ExpressionImpl("2*(3+5)");
        result = expression.evaluate();
        Assert.assertEquals(16L, result);

        result = expression
                .parseExpression("'Hello World'")
                .evaluate();
        Assert.assertEquals("Hello World", result);

        result = expression
                .parseExpression("2")
                .evaluate();
        Assert.assertEquals(2L, result);

        expression = new ExpressionImpl("0x06+0x02");
        result = expression.evaluate();
        Assert.assertEquals(8L, result);

    }

    @Test
    public void evaluate_JS_PARSW_Test() {
        result = expression
                .parseExpression("parseInt('2')")
                .evaluate();
        Assert.assertEquals(2, result);

        result = expression
                .parseExpression("parseFloat('1.230')")
                .evaluate();
        Assert.assertEquals(1.23f, result);

        result = expression
                .parseExpression("parseFloat('1.230')")
                .evaluate();
        Assert.assertEquals(1.23f, result);

    }

    @Test
    public void evaluate_JS_JSON_Test() {
        result = expression
                .parseExpression("JSON.parse('" + json + "')")
                .evaluate();
        Assert.assertEquals(JSON.parse(json), result);

        result = expression
                .parseExpression("JSON.stringify('" + JSON.parse(json) + "')")
                .evaluate();
        Assert.assertEquals(jsonEscape, result);
    }

    @Test
    public void evaluate_JS_EPI_Test() {
        result = expression
                .parseExpression("Math.E")
                .evaluate();
        Assert.assertEquals(Math.E, result);

        result = expression
                .parseExpression("Math.PI")
                .evaluate();
        Assert.assertEquals(Math.PI, result);
    }

    @Test
    public void evaluate_JS_L_Test() {
        result = expression
                .parseExpression("Math.LOG10E")
                .evaluate();
        Assert.assertEquals(1 / Math.log(10), result);

        result = expression
                .parseExpression("Math.LN10")
                .evaluate();
        Assert.assertEquals(Math.log(10), result);

        result = expression
                .parseExpression("Math.LN2")
                .evaluate();
        Assert.assertEquals(Math.log(2), result);

        result = expression
                .parseExpression("Math.LOG2E")
                .evaluate();
        Assert.assertEquals(1 / Math.log(2), result);
    }

    @Test
    public void evaluate_JS_SQRT1_2_Test() {
        result = expression
                .parseExpression("Math.SQRT1_2")
                .evaluate();
        Assert.assertEquals(Math.sqrt(0.5), result);
    }

    @Test
    public void evaluate_JS_ISFINITE_Test() {
        result = expression
                .parseExpression("isFinite(5)")
                .evaluate();
        Assert.assertEquals(true, result);

        result = expression
                .parseExpression("isFinite('fastel')")
                .evaluate();
        Assert.assertEquals(false, result);
    }

    @Test
    public void evaluate_JS_ISNAN_Test() {
        result = expression
                .parseExpression("isNaN('fastel')")
                .evaluate();
        Assert.assertEquals(true, result);

        result = expression
                .parseExpression("isNaN(123)")
                .evaluate();
        Assert.assertEquals(false, result);
    }

    @Test
    public void evaluate_JS_ABS_Test() {
        result = expression
                .parseExpression("Math.abs(123)")
                .evaluate();
        Assert.assertEquals(123.0, result);

        result = expression
                .parseExpression("Math.abs(-123)")
                .evaluate();
        Assert.assertEquals(123.0, result);
    }

    @Test
    public void evaluate_JS_ACOS_Test() {
        result = expression
                .parseExpression("Math.acos(0.64)")
                .evaluate();
        Assert.assertEquals(Math.acos(0.64), result);

        result = expression
                .parseExpression("Math.acos(1)")
                .evaluate();
        Assert.assertEquals(Math.acos(1), result);

        result = expression
                .parseExpression("Math.acos(2)")
                .evaluate();
        Assert.assertEquals(Math.acos(2), result);
    }

    @Test
    public void evaluate_JS_ASIN_Test() {
        result = expression
                .parseExpression("Math.asin(0.64)")
                .evaluate();
        Assert.assertEquals(Math.asin(0.64), result);

        result = expression
                .parseExpression("Math.asin(1)")
                .evaluate();
        Assert.assertEquals(Math.asin(1), result);

        result = expression
                .parseExpression("Math.asin(2)")
                .evaluate();
        Assert.assertEquals(Math.asin(2), result);
    }

    @Test
    public void evaluate_JS_ATAN_Test() {
        result = expression
                .parseExpression("Math.atan(0.64)")
                .evaluate();
        Assert.assertEquals(Math.atan(0.64), result);

        result = expression
                .parseExpression("Math.atan(5)")
                .evaluate();
        Assert.assertEquals(Math.atan(5), result);

        result = expression
                .parseExpression("Math.atan(-10)")
                .evaluate();
        Assert.assertEquals(Math.atan(-10), result);
    }

    @Test
    public void evaluate_JS_ATAN2_Test() {
        result = expression
                .parseExpression("Math.atan2(0.50,0.50)")
                .evaluate();
        Assert.assertEquals(Math.atan2(0.50, 0.50), result);

        result = expression
                .parseExpression("Math.atan2(-0.50,-0.50)")
                .evaluate();
        Assert.assertEquals(Math.atan2(-0.50, -0.50), result);

        result = expression
                .parseExpression("Math.atan2(10,20)")
                .evaluate();
        Assert.assertEquals(Math.atan2(10, 20), result);
    }

    @Test
    public void evaluate_JS_CEIL_Test() {
        result = expression
                .parseExpression("Math.ceil(0.64)")
                .evaluate();
        Assert.assertEquals(Math.ceil(0.64), result);

        result = expression
                .parseExpression("Math.ceil(-5.9)")
                .evaluate();
        Assert.assertEquals(Math.ceil(-5.9), result);

        result = expression
                .parseExpression("Math.ceil(-5.1)")
                .evaluate();
        Assert.assertEquals(Math.ceil(-5.1), result);
    }

    @Test
    public void evaluate_JS_COS_Test() {
        result = expression
                .parseExpression("Math.cos(0.64)")
                .evaluate();
        Assert.assertEquals(Math.cos(0.64), result);

        result = expression
                .parseExpression("Math.cos(-5.9)")
                .evaluate();
        Assert.assertEquals(Math.cos(-5.9), result);

        result = expression
                .parseExpression("Math.cos(-5.1)")
                .evaluate();
        Assert.assertEquals(Math.cos(-5.1), result);
    }

    @Test
    public void evaluate_JS_EXP_Test() {
        result = expression
                .parseExpression("Math.exp(1)")
                .evaluate();
        Assert.assertEquals(Math.exp(1), result);

        result = expression
                .parseExpression("Math.exp(-1)")
                .evaluate();
        Assert.assertEquals(Math.exp(-1), result);

        result = expression
                .parseExpression("Math.exp(10)")
                .evaluate();
        Assert.assertEquals(Math.exp(10), result);
    }

    @Test
    public void evaluate_JS_FLOOR_Test() {
        result = expression
                .parseExpression("Math.floor(0.60)")
                .evaluate();
        Assert.assertEquals(Math.floor(0.60), result);

        result = expression
                .parseExpression("Math.floor(5)")
                .evaluate();
        Assert.assertEquals(Math.floor(5), result);

        result = expression
                .parseExpression("Math.floor(-5.1)")
                .evaluate();
        Assert.assertEquals(Math.floor(-5.1), result);
    }

    @Test
    public void evaluate_JS_LOG_Test() {
        result = expression
                .parseExpression("Math.log(2.7183)")
                .evaluate();
        Assert.assertEquals(Math.log(2.7183), result);

        result = expression
                .parseExpression("Math.log(2)")
                .evaluate();
        Assert.assertEquals(Math.log(2), result);

        result = expression
                .parseExpression("Math.log(0)")
                .evaluate();
        Assert.assertEquals(Math.log(0), result);

        result = expression
                .parseExpression("Math.log(-1)")
                .evaluate();
        Assert.assertEquals(Math.log(-1), result);
    }

    @Test
    public void evaluate_JS_MAX_Test() {
        result = expression
                .parseExpression("Math.max(5,7)")
                .evaluate();
        Assert.assertEquals(Math.max(5, 7), (long) result);

        result = expression
                .parseExpression("Math.max(-3,-5)")
                .evaluate();
        Assert.assertEquals(Math.max(-3, -5), (long) result);

        result = expression
                .parseExpression("Math.max(7.25,7.30)")
                .evaluate();
        Assert.assertEquals(Math.max(7.25, 7.30), result);
    }

    @Test
    public void evaluate_JS_MIN_Test() {
        result = expression
                .parseExpression("Math.min(5,7)")
                .evaluate();
        Assert.assertEquals(Math.min(5, 7), (long) result);

        result = expression
                .parseExpression("Math.min(-3,-5)")
                .evaluate();
        Assert.assertEquals(Math.min(-3, -5), (long) result);

        result = expression
                .parseExpression("Math.min(7.25,7.30)")
                .evaluate();
        Assert.assertEquals(Math.min(7.25, 7.30), result);
    }

    @Test
    public void evaluate_JS_POW_Test() {
        result = expression
                .parseExpression("Math.pow(0,0)")
                .evaluate();
        Assert.assertEquals(Math.pow(0, 0), result);

        result = expression
                .parseExpression("Math.pow(0,1)")
                .evaluate();
        Assert.assertEquals(Math.pow(0, 1), result);

        result = expression
                .parseExpression("Math.pow(2,4)")
                .evaluate();
        Assert.assertEquals(Math.pow(2, 4), result);
    }

    @Test
    public void evaluate_JS_RANDOM_Test() {
        result = expression
                .parseExpression("Math.random()")
                .evaluate();
        Assert.assertEquals(true, (double) result >= 0 && (double) result <= 1);

    }

    @Test
    public void evaluate_JS_ROUND_Test() {
        result = expression
                .parseExpression("Math.round(0.60)")
                .evaluate();
        Assert.assertEquals(Math.round(0.60), result);

        result = expression
                .parseExpression("Math.round(0.49)")
                .evaluate();
        Assert.assertEquals(Math.round(0.49), result);

        result = expression
                .parseExpression("Math.round(-4.60)")
                .evaluate();
        Assert.assertEquals(Math.round(-4.60), result);
    }

    @Test
    public void evaluate_JS_SIN_Test() {
        result = expression
                .parseExpression("Math.sin(0.60)")
                .evaluate();
        Assert.assertEquals(Math.sin(0.60), result);

        result = expression
                .parseExpression("Math.sin(0.49)")
                .evaluate();
        Assert.assertEquals(Math.sin(0.49), result);

        result = expression
                .parseExpression("Math.sin(-4.60)")
                .evaluate();
        Assert.assertEquals(Math.sin(-4.60), result);
    }

    @Test
    public void evaluate_JS_SQRT_Test() {
        result = expression
                .parseExpression("Math.sqrt(0.64)")
                .evaluate();
        Assert.assertEquals(Math.sqrt(0.64), result);

        result = expression
                .parseExpression("Math.sqrt(0)")
                .evaluate();
        Assert.assertEquals(Math.sqrt(0), result);

        result = expression
                .parseExpression("Math.sqrt(9)")
                .evaluate();
        Assert.assertEquals(Math.sqrt(9), result);
    }

    @Test
    public void evaluate_JS_TAN_Test() {
        result = expression
                .parseExpression("Math.tan(0.50)")
                .evaluate();
        Assert.assertEquals(Math.tan(0.50), result);

        result = expression
                .parseExpression("Math.tan(-0.50)")
                .evaluate();
        Assert.assertEquals(Math.tan(-0.50), result);

        result = expression
                .parseExpression("Math.tan(10)")
                .evaluate();
        Assert.assertEquals(Math.tan(10), result);
    }

    @Test
    public void evaluate_JSON_Test() {
        JSONObject obj = JSON.parseObject(json);
        JSONObject objSimple = JSON.parseObject(jsonSimple);

        Expression el = new ExpressionImpl("returncode==0 && message=='success'");
        result = el.evaluate(obj);
        Assert.assertEquals(true, result);

        result = expression
                .parseExpression("returncode>0")
                .evaluate(obj);
        Assert.assertEquals(false, result);

        result = expression
                .parseExpression("returncode==0")
                .evaluate(objSimple);
        Assert.assertEquals(true, result);

        result = expression
                .parseExpression("returncode")
                .evaluate(objSimple);
        Assert.assertEquals(0, result);

        result = expression
                .parseExpression("result.sk.temp")
                .evaluate(objSimple);
        Assert.assertEquals("21", result);

        result = expression
                .parseExpression("result.sk.temp")
                .evaluate(objSimple);
        Assert.assertEquals("21", result);

        result = expression
                .parseExpression("result.future.length")
                .evaluate(objSimple);
        Assert.assertEquals(7, result);

    }

    @Test
    public void evaluate_console() {
        result = expression
                .parseExpression("parseInt('0x01')")
                .evaluate();
        System.out.println(result);

        result = expression
                .parseExpression("parseFloat('0x01')")
                .evaluate();
        System.out.println(result);
    }

}
