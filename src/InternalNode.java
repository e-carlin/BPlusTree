
import java.util.ArrayList;

public class InternalNode<E extends Comparable<E>> extends Node<E>{

	//A list of pointers to children nodes
	private ArrayList<Node<E>> children;

	public InternalNode(int degree) {

		super(degree);
		children = new ArrayList<Node<E>>();
	}

	public ArrayList<Node<E>> getChildren(){

		return children;
	}

	public Node<E> getChild(int index){

		return children.get(index);
	}

	/**
	 * Sets the node at a specific index in the children list
	 * @param index the index we wish to place the child node at
	 * @param childNode the child node we wish to add to the list
	 */
	public void setChild(int index, Node<E> childNode){
		children.set(index, childNode);
	}

	public void setChild(Node<E> childNode){
		children.add(childNode);
	}
	
}
