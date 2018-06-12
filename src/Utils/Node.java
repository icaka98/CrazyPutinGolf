package Utils;

/**
 * @author Mathieu Coenegracht
 * Node class that makes up the expression tree
 */
public class Node {
    public String value;
    public Node left, right;

    /**
     * Constructor for the node class
     * @param val String to be stored in the node
     */
    public Node(String val){
        this.value = val;
        this.left = this.right = null;
    }
}
