
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
	 * Appends the node to the end of the children list
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
	public void addCollectionOfChildren(ArrayList<Node<E>> childrenToAdd){

		children.addAll(childrenToAdd);
	}


	public void removeRangeOfChildren(int from, int to){

		children.subList(from, to).clear();
	}

	public void setParentToBeThisNode(){
		for(Node<E> child : children){
			child.setParent(this);
		}
	}

	//TODO:Remove, this is just for test
	public void printChildrensParents(){
		for(Node<E> child : children){
			System.out.println("The child "+ child.getValues()+" parent is "+child.getParent());
		}
	}
}
