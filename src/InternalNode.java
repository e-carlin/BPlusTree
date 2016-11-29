
import java.util.ArrayList;

public class InternalNode<E extends Comparable<E>> extends Node<E>{

	private ArrayList<Node<E>> children;
	
	public InternalNode(int degree) {
		super(degree);
		children = new ArrayList<Node<E>>();
	}

	public ArrayList<Node<E>> getChildren(){
		return children;
	}
	
}
