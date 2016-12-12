import java.util.ArrayList;

/**
 * A leaf node
 * @author Evan Carlin
 */
public class LeafNode<E extends Comparable<E>> extends Node<E> {
	
	private LeafNode<E> nextLeaf;
	
	public LeafNode(int degree) {
		super(degree);
		
		nextLeaf = null;
	}

	/**
	 * Gets the next leaf node
	 * @return the next leaf node
	 */
	public LeafNode getNextLeaf(){

		return nextLeaf;
	}

	
	/**
	 * Sets the next leaf node
	 * @param leaf the node you wish to set as the next leaf
	 */
	public void setNextLeaf(LeafNode<E> leaf){

		nextLeaf = leaf;
	}

	//TODO: Think about this more is this really desired behavior??
	/**
	 * A leaf node has no children so return null
	 * TODO: see if this can be changed to null
	 * @return
	 */
	@Override
	public ArrayList<Node<E>> getChildren(){

		return new ArrayList<>();
	}

	public Node<E> getChild(int index){

		return null;
	}

	@Override
	public void addChild(int index, Node<E> childNode){}

	@Override
	public void addChild(Node<E> childNode){}
}
