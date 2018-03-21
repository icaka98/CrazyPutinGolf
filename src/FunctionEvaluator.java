import java.util.Queue;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Arrays;

public class FunctionEvaluator{

    public static void main(String[] args) {
        System.out.println(eval("( 1.0 + sin ( 3.14 ) ) * 3.0"));
    }
    private static Double eval(String s){
        String[] str = s.split("\\s+");
        Stack<String> ops = new Stack<>();//operations stack
        Stack<Double> vals = new Stack<>();//value stack
        Queue<String> fun = new LinkedList<>();//Queue that holds the different parts of the expression
        fun.addAll(Arrays.asList(str));

        while(!fun.isEmpty()){
            String token = fun.remove();
            switch (token) {
                case "(":
                    break;
                case "+":
                case "-":
                case "*":
                case "/":
                case "^":
                case "sqrt":
                case "sin":
                case "cos":
                case "tan":
                    ops.push(token); //push operation to stack
                    break;
                case ")":
                    vals.push(evalOp(ops, vals)); //evaluate operation within brackets and push it to value stack
                    break;
                default:
                    vals.push(Double.parseDouble(token)); // push value to stack
            }
        }
        return evalOp(ops, vals); //evaluate if there is another operation and return the answer
    }

    private static Double evalOp(Stack<String> ops, Stack<Double> vals){
        Double v = vals.pop();

        if(!ops.isEmpty()) {
            String op = ops.pop();

            switch (op) {
                case "+":
                    v = vals.pop() + v;
                    break;
                case "-":
                    v = vals.pop() - v;
                    break;
                case "*":
                    v = vals.pop() * v;
                    break;
                case "/":
                    v = vals.pop() / v;
                    break;
                case "^":
                    v = Math.pow(vals.pop(), v);
                    break;
                case "sqrt":
                    v = Math.sqrt(v);
                    break;
                case "sin":
                    v = Math.sin(v);
                    break;
                case "cos":
                    v = Math.cos(v);
                    break;
                case "tan":
                    v = Math.tan(v);
                    break;
            }
        }
        return v;
    }
}
