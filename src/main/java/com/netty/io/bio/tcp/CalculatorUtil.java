package com.netty.io.bio.tcp;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author Administrator
 */
public class CalculatorUtil {
    private final static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
    public static Object cal(String expression){
        try {
            return jse.eval(expression);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return null;
    }
}
