import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Iterator;

public class BPlusTree<E extends Comparable <E>>{

	private final boolean DEBUG = true;
	private Node<E> root;
	private int degree;

	public BPlusTree(int d){
		degree = d;
		root = null;
	}

	public void insertValue(E value){

		if(DEBUG){
			System.out.println("Adding "+value+" to tree");
		}

		//Tree is empty so we can just create a leaf node and putValue the value
		if(this.isEmpty()){
			if(DEBUG){
				System.out.println("Tree is empty so creating root and adding to it");
			}
			root = new LeafNode<>(degree);
			root.putValue(value);
		}
		else{
			if(DEBUG){
				System.out.println("Tree is not empty");
			}
			//Tree is not empty so we must find the leaf where value should go
			LeafNode<E> originalLeaf = findLeafFor(value, root);

			if(DEBUG){
				System.out.println("Found leaf node for "+value+" it is "+ originalLeaf);
			}

			//The leaf node isn't full so we can just putValue into it!
			if(!originalLeaf.isFull()){
				if(DEBUG) {
					System.out.println("Leaf is not full so just adding to it");
				}
				originalLeaf.putValue(value);
			}

			// The node is full so we must split it and recurse
			else {
				if(DEBUG){
					System.out.println("Leaf is full so must do some splitting work!");
				}


				//First naively insert value into (now) overflown node
				originalLeaf.putValue(value);


				if( DEBUG){
					System.out.println("Naively added value to leaf. Leaf is now "+originalLeaf);
					System.out.println("Copying/deleting half of values from original leaf");
				}

				//Copy second half of values to new leaf, remove values from original once copied
				LeafNode<E> rightLeaf = this.moveValuesToNewLeaf(originalLeaf);

				//Adjust pointers between nodes and next leaf
				rightLeaf.setNextLeaf(originalLeaf.getNextLeaf());
				originalLeaf.setNextLeaf(rightLeaf);

				if(DEBUG){
					System.out.println("The left leaf is "+originalLeaf);
					System.out.println("The right leaf is "+rightLeaf);
					System.out.println("Now going to propagate the split...");
				}

				//Let the parent know that it has a new child
				this.propagateLeafNodeSplit(originalLeaf,  rightLeaf);
			}
		}

	}


	/**
	 * Prints the tree in level order
	 * Adapted from Reddy's answer: http://stackoverflow.com/questions/2241513/java-printing-a-binary-tree-using-level-order-in-a-specific-format
	 */
	public void printTree() {
		System.out.println("******** PRINTING TREE ********");

		Queue<Node<E>> currentLevel = new LinkedList<>();
		Queue<Node<E>> nextLevel = new LinkedList<>();

		currentLevel.add(this.root);

		while (!currentLevel.isEmpty()) {
			System.out.println("Current level isn't empty");
			Iterator<Node<E>> iter = currentLevel.iterator();
			while (iter.hasNext()) {
				Node<E> currentNode = iter.next();

				System.out.println("Current node is "+currentNode);

				nextLevel.addAll(currentNode.getChildren());

				System.out.print(currentNode.getValues() + " ");
			}
			System.out.println();
			currentLevel = nextLevel;
			nextLevel = new LinkedList<>();

		}

	}

	/*************** PRIVATE HELPER METHODS ******************/

	/**
	 * Copies half of of the values from the old leaf into a new leaf. Then deletes
	 * the values from the original once they are copied.
	 * @param originalLeaf the old leaf containing the values to be copied
	 * @return the newly created leaf containg the values
	 */
	private LeafNode<E> moveValuesToNewLeaf(LeafNode<E> originalLeaf){
		//Create new leaf node
		LeafNode<E> rightLeaf = new LeafNode<E>(degree);

		//Copy half of the original leaves values into it
		rightLeaf.putCollectionOfValues(new ArrayList<E>(originalLeaf.subList((int)Math.ceil((degree-1)/2), originalLeaf.sizeOfValues())));

		//Delete the copied values from the original leaf
		originalLeaf.removeRange((int)Math.ceil((degree-1)/2), originalLeaf.sizeOfValues());

		return rightLeaf;
	}

	/**
	 * This method handles the splitting of a leaf node. It adjusts the parents pointers,
	 * inserts the smallest value from the new right node into the parent, and then calls
	 * another method if the parent now needs to be split
	 * @param originalLeaf the original leaf that was split (now is the left leaf)
	 * @param newLeaf the new leaf that was created (the next of the original leaf)
	 */
	private void propagateLeafNodeSplit(LeafNode<E> originalLeaf, LeafNode<E> newLeaf){
		if(DEBUG){
			System.out.println("Propagating a leaf split");
		}

		//If the parent is null then original leaf is the root
		if(originalLeaf.getParent() == null){
			if(DEBUG){
				System.out.println("Parent is null so leaf was the root. Creating a new root...");
			}

			root = new InternalNode<E>(degree);
			root.putValue(newLeaf.getValue(0));
			//TODO: Handle pointers
		}

		//The original leaf was not the root **/

		//The parent is NOT full so we can just insert into it and adjust pointers
		else if(!originalLeaf.getParent().isFull()) {
			//TODO: Complete
		}

		// The parent is full so we must split it and propagate the split
		else{
			//TODO: split the node; propagate split into parent
		}

	}


	//TODO: complete and TEST
	/**
	 * Find the lead where value should be inserted
	 * @param value the value we wish to putValue
	 * @param localRoot the local node we wish to examine
	 * @return the leaf node where value should be inserted
	 */
	private LeafNode<E> findLeafFor(E value, Node<E> localRoot){
		//We have reached a leaf so this node is where value should be placed (naive, the leaf could be full)
		if(localRoot instanceof LeafNode){
			return (LeafNode)localRoot;
		}
		//We still have some of the tree to traverse to get down to leaves
		else{
			//Look through this nodes values to find which pointer we should follow
			for(int i=0; i<localRoot.sizeOfValues(); i++){
				//Value should should go in leftmost subtree
				if(i ==0 && value.compareTo(localRoot.getValue(i)) < 0){
					return findLeafFor(value, localRoot.getChildren().get(0));
				}

				//Value should go in rightmost pointers subtree
				//TODO: The check after the && is extraneous we will only ever get to here in the loop if it true
				else if(i==localRoot.sizeOfValues()-1 && value.compareTo(localRoot.getValue(i)) >= 0){
					return findLeafFor(value, localRoot.getChildren().get(i+1));
				}

				//Value should go in one of the internal pointers subtree
				else if(value.compareTo(localRoot.getValue(i)) >= 0 && value.compareTo(localRoot.getValue(i+1)) < 0) {
					// i <= value < i+1; so it value goes in the i+1 childs subtree
					return findLeafFor(value, localRoot.getChildren().get(i + 1));
				}
			}
		}
		//This should never be reached
		return null;
	}

	private boolean rootIsLeaf(){
		return root instanceof LeafNode;
	}

	private boolean isEmpty(){
		return root == null;
	}



	public String toString(){
		if(this.isEmpty()){
			return "The tree is empty";
		}
		//TODO: Print more than the root...
		return root.toString();
	}
}
