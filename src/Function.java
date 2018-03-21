import java.util.Queue;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Function{

    public static Map<String,Double> vars = new HashMap<>();
    private Node z;

    public Function(String s){
        z = parse(s); // parse z and return the root
        inOrder(z);
        vars.put("pi", Math.PI);
    }
    public Node parse(String s){

        String[] str = s.split("\\s+");
        Stack<Node> operators = new Stack<>();//operations stack
        Stack<Node> operands = new Stack<>();//value stack
        Queue<String> q = new LinkedList<>();//Queue that holds the different parts of the expression
        q.addAll(Arrays.asList(str));

        while(!q.isEmpty()){
            String token = q.remove();
            if(token.equals("(")) continue;
            if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("^") || token.equals("sqrt") || token.equals("sin") || token.equals("cos") || token.equals("tan")){
                Node t = new Node(token);
                operators.push(t); //push operation to stack
            }
            else if (token.equals(")")){
                operands.push(makeTree(operators, operands)); //evaluate operation within brackets and push it to value stack
            }
            else {
                Node t = new Node(token);
                operands.push(t); // push node to stack
            }
        }
        System.out.println(operators.size());
        System.out.println(operands.size());
        return makeTree(operators, operands); //evaluate if there is another operation and return the answer
    }

    public Node makeTree(Stack<Node> operators, Stack<Node> operands){
        Node v = operands.pop();

        if(!operators.isEmpty()) {
            Node op = operators.pop();

            if(op.value.equals("+") || op.value.equals("-") || op.value.equals("*") || op.value.equals("/") || op.value.equals("^")){
                op.left = operands.pop(); op.right = v;

            }else if(op.value.equals("sqrt")){
                op.left = v;
            }else if(op.value.equals("sin")){
                op.left = v;
            }else if(op.value.equals("cos")){
                op.left = v;
            }else if(op.value.equals("tan")){
                op.left = v;
            }
            return op;
        }
        return v;
    }
    public void inOrder(Node r){
        if (r.left != null) inOrder(r.left);
        System.out.print("[" +r.value +"]");
        if (r.right != null) inOrder(r.right);
    }

    public double solve(double x, double y){
        vars.put("x", x);
        vars.put("y", y);
        System.out.println(vars.containsKey("pi"));
        return eval(z);
    }

    private double eval(Node r){
        if(r == null){
            return 0.0;
        }
        else if(r.left == null && r.right == null){
            if(vars.containsKey(r.value)){
                return vars.get(r.value);
            }
            else{
                return Double.parseDouble(r.value);
            }
        }
        else{
            double l_val = eval(r.left);
            double r_val = eval(r.right);
            if (r.value.equals("+")){
                return l_val + r_val;
            }
            if (r.value.equals("-")){
                return l_val - r_val;
            }
            if (r.value.equals("*")){
                return l_val * r_val;
            }
            if (r.value.equals("/")){
                return l_val / r_val;
            }
            if (r.value.equals("sin")){
                return Math.sin(l_val);
            }
            if (r.value.equals("cos")){
                return Math.cos(l_val);
            }
            if (r.value.equals("tan")){
                return Math.tan(l_val);
            }
            return Math.pow(l_val, r_val);
        }
    }

    public static void main(String[] args) {
        String height = "( tan ( y ) ) + ( ( x ^ 2 ) * 3.0 )";
        Function f = new Function(height);
        System.out.println("\n" +f.solve(10, Math.PI/2));
        System.out.println(f.solve(1, Math.PI));
    }
}