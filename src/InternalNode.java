import java.util.ArrayList;
import java.util.List;

/**
 * An internal node for a B+ Tree. The node has a list of values and a list of
 * children
 * @author Evan Carlin
 */
public class InternalNode<E extends Comparable<E>> extends Node<E>{

	//A list of pointers to children nodes
	private ArrayList<Node<E>> children;

	/**
	 * Constructs an InternalNode with a give degree
	 * @param degree the degree of the node
	 */
	public InternalNode(int degree) {

		super(degree);
		children = new ArrayList<>();
	}

	/**
	 * Returns all of the children of this node
	 * @return all of the children
	 */
	public ArrayList<Node<E>> getChildren(){

		return children;
	}

	/**
	 * Returns the child at a specific index
	 * @param index the index of the child we want
	 * @return the child at index
	 */
	public Node<E> getChild(int index){

		return children.get(index);
	}


	/**
	 * Adds a child in sorted order to the list of children
	 * @param nodeToAdd the node we want to add
	 */
	public void addChild(Node<E> nodeToAdd){

		for(int i=0; i<children.size(); i++){
			//If nodeToAdd is bigger than the child at this index keep going
			if(nodeToAdd.getValue(0).compareTo(this.getChild(i).getValue(0)) > 0){
				continue;
			}
			//nodeToAdd is <= this child
			children.add(i, nodeToAdd);
			return;
		}
		//All children were < nodeToAdd so append to end of list
		children.add(nodeToAdd);
	}

	/**
	 * The number of children elements
	 * @return the number
	 */
	public int sizeOfChildren(){

		return children.size();
	}

	/**
	 * Gets a sublist of children
	 * @param from the beginning index (inclusive)
	 * @param to the ending index (exclusive)
	 * @return the sublist of children
	 */
	public List<Node<E>> subListChildren(int from, int to){

		return children.subList(from, to);
	}

	/**
	 * Adds a list of children (in sorted order) to the chidlren list
	 * @param childrenToAdd the children pointers we wish to add
	 */
	public void addCollectionOfChildren(ArrayList<Node<E>> childrenToAdd){

		for(Node<E> child : childrenToAdd){
			this.addChild(child);
		}
	}

	/**
	 * Removes a range of children pointers
	 * @param from the start index (inclusive)
	 * @param to the end index (exclusive)
	 */
	public void removeRangeOfChildren(int from, int to){

		children.subList(from, to).clear();
	}

	/**
	 * Takes all of the children and sets their parent field to point to this node.
	 */
	public void setParentToBeThisNode(){
		for(Node<E> child : children){
			child.setParent(this);
		}
	}
}
