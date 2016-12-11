
public class BPlusTree<E extends Comparable <E>>{
	
	public Node<E> root;
	public int degree;
	
	public BPlusTree(int degree){
		degree = degree;
		root = null;
	}
	
	public void insert(E value){
		//Tree is empty so we can just create a leaf node and insert the value
		if(this.isEmpty()){
			LeafNode<E> node = new LeafNode<E>(degree);
			root = node;
			root.insert(value);
			return;
		}

		//Tree is not empty so we must find the leaf where value should go
		LeafNode<E> insertIntoNode = findLeafFor(value, root);
		//The leaf node isn't full so we can just insert into it
		if(!insertIntoNode.isFull()){
			insertIntoNode.insert(value);
		}
		
	}


	//*************** PRIVATE HELPER METHODS ******************


	//TODO: complete and TEST
	/**
	 * Find the lead where value should be inserted
	 * @param value the value we wish to insert
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
		return root instanceof LeafNode ? true : false;
		
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
