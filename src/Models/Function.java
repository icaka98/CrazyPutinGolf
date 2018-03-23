package Models;

import Utils.Node;

import java.util.Queue;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Function{
    private Map<String,Double> vars = new HashMap<>();
    private Node z;

    public Function(String s){
        z = parse(s); // parse z and return the root
        inOrder(z);
        vars.put("pi", Math.PI);
    }

    private Node parse(String s){ //parse string and create Expression Tree of function

        String[] str = s.split("\\s+");//create tokens
        Stack<Node> operators = new Stack<>();//operations stack
        Stack<Node> operands = new Stack<>();//value stack
        Queue<String> q = new LinkedList<>();//Queue that holds the different parts of the expression
        q.addAll(Arrays.asList(str));//add all tokens to queue

        while(!q.isEmpty()){
            String token = q.remove(); //get token
            if(token.equals("(")) continue;//if token is a "(" ignore
            if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("^") || token.equals("sqrt") || token.equals("sin") || token.equals("cos") || token.equals("tan")){
                Node t = new Node(token);//create new node that stores the operator
                operators.push(t); //push operator node to stack
            }
            else if (token.equals(")")){
                operands.push(makeTree(operators, operands)); //create subtree of operation within brackets and push it to value stack
            }
            else {
                Node t = new Node(token); // create node that stores an operand
                operands.push(t); // push node to stack
            }
        }
        //System.out.println(operators.size());
        //System.out.println(operands.size());
        return makeTree(operators, operands); //evaluate if there is another operation and return the answer
    }

    private Node makeTree(Stack<Node> operators, Stack<Node> operands){ //Create the tree
        Node v = operands.pop();

        if(!operators.isEmpty()) { // create subtree of operation if there are still operators on the stack
            Node op = operators.pop();

            if(op.value.equals("+") || op.value.equals("-") || op.value.equals("*") || op.value.equals("/") || op.value.equals("^")){
                op.left = operands.pop(); op.right = v;

            }else if(op.value.equals("sqrt")){
                op.right = v;
            }else if(op.value.equals("sin")){
                op.right = v;
            }else if(op.value.equals("cos")){
                op.right = v;
            }else if(op.value.equals("tan")){
                op.right = v;
            }
            return op;//return subtree
        }
        return v; //return operand
    }

    private void inOrder(Node r){ //inorder traversel of tree
        if (r.left != null) inOrder(r.left);
        System.out.print("[" +r.value +"]");
        if (r.right != null) inOrder(r.right);
    }

    public double solve(double x, double y){ //solve equation for x and y values
        vars.put("x", x);
        vars.put("y", y);
        //System.out.println(vars.containsKey("pi"));
        return eval(z);
    }

    private double eval(Node r){ // recursively solve the subtrees
        if(r == null){
            return 0.0;
        }
        else if(r.left == null && r.right == null){//r is a leaf node
            if(vars.containsKey(r.value)){//return value corresponding to key
                return vars.get(r.value);
            }
            else{//key is not in vars, parse double
                return Double.parseDouble(r.value);
            }
        }
        else{//perform operations on the subtrees
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
                return Math.sin(r_val);
            }
            if (r.value.equals("cos")){
                return Math.cos(r_val);
            }
            if (r.value.equals("tan")){
                return Math.tan(r_val);
            }
            return Math.pow(l_val, r_val);
        }
    }

    public static void main(String[] args) {
        String height = "( ( 0.1 * x ) + ( 0.03 * ( x ^ 2 ) ) + ( 0.2 * y ) )";
        Function f = new Function(height);
        System.out.println("\n" +f.solve(1.0, 2.0));
        System.out.println(f.solve(2.0, 3.0));
    }
}