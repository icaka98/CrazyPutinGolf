import java.util.*;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class MathParse{
    public static void main(String[] args) throws ScriptException {
        Map<String, String> values = new HashMap<String, String>();
        values.put("y^2", "Math.pow(2, 2)"); // problem where I can't replace around the exponent to close the bracket
        values.put("x^", "Math.pow(x, ");    // as denoted here
        values.put("sin", "Math.sin");
        values.put("cos", "Math.cos");
        values.put("z = ", "");
        values.put("pi", "Math.PI");
        String expression = "z = sin(pi) + y^2";
        System.out.println(expression);

        for (Map.Entry<String, String> entry : values.entrySet())
        expression = expression.replace(entry.getKey(), entry.getValue());
        System.out.println(expression);
        
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        System.out.println("ans = " + engine.eval(expression));
    } 
}
