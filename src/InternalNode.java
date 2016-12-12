
import java.util.ArrayList;
import java.util.List;

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
	 * Adds the node at a specific index in the children list
	 * @param index the index we wish to place the child node at
	 * @param childNode the child node we wish to add to the list
	 */
	public void addChild(int index, Node<E> childNode){
		children.add(index, childNode);
	}

	/**
	 * Appends the node to the end of the children list
	 * @param childNode the node we want to add
	 */
	public void addChild(Node<E> childNode){
		children.add(childNode);
	}

	public int sizeOfChildren(){
		return children.size();
	}

	public List<Node<E>> subListChildren(int from, int to){
		return children.subList(from, to);
	}

	/**
	 * CAUTION: This naively adds elements to the node. It makes not effort to sort the values.
	 * This should work fine becasue they are being copied over in sorted order
	 * @param childrenToAdd the children pointers we wish to add
	 */
	public void putCollectionOfChildren(ArrayList<Node<E>> childrenToAdd){
		children.addAll(childrenToAdd);
	}
	
}
