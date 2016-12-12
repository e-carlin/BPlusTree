
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

	public void setChild(int index, Node<E> node){
		//If we really just want to append this node to the list of children
		if(children.size() == index){
			children.add(node);
		}
		else {
			children.set(index, node);
		}
	}
	
}
