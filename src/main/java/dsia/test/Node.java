package dsia.test;

public class Node {
    // keep these​​​​​​‌​​​‌​‌‌‌​​‌​‌​‌​​​​‌​‌‌‌ fields
    Node left, right;
	int value;

    public Node find(int v) {
    	Node retour = null;
        if(value == v) {
        	retour = this;
        } else {
        	if(v > value && left != null) {
        		retour = left.find(v);
        	} else if(v < value && right != null) {
        		retour = right.find(v);
        	}
        }
        return retour;
    }

    public static void main(String[] args) {
    	Node n = new Node().find(8);
    	System.out.println("find returns " + n.value); // 8


	}
    
}
