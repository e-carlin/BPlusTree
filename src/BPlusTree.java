import java.util.*;

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
		    this.printTree();
			System.out.println("\nAdding "+value+" to tree");
		}

		//Tree is empty so we can just create a leaf node and addValue the value
		if(this.isEmpty()){
			if(DEBUG){
				System.out.println("Tree is empty so creating root and adding to it");
			}
			root = new LeafNode<>(degree);
			root.addValue(value);
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

			//The leaf node isn't full so we can just addValue into it!
			if(!originalLeaf.isFull()){
				if(DEBUG) {
					System.out.println("Leaf is not full so just adding to it");
				}
				originalLeaf.addValue(value);
			}

			// The node is full so we must split it and recurse
			else {
				if(DEBUG){
					System.out.println("Leaf is full so must do some splitting work!");
				}


				//First naively insert value into (now) overflown node
				originalLeaf.addValue(value);
				//Handle the splitting and propagation
                this.splitLeafNode(originalLeaf);
			}
		}

	}


	/**
	 * Prints the tree in level order
	 * Adapted from Reddy's answer: http://stackoverflow.com/questions/2241513/java-printing-a-binary-tree-using-level-order-in-a-specific-format
	 */
	public void printTree() {
		System.out.println("******** PRINTING TREE ********");

		if(this.isEmpty()){
            System.out.println("[]");
            return;
        }

		Queue<Node<E>> currentLevel = new LinkedList<>();
		Queue<Node<E>> nextLevel = new LinkedList<>();

		currentLevel.add(this.root);

		while (!currentLevel.isEmpty()) {
			Iterator<Node<E>> iter = currentLevel.iterator();
			while (iter.hasNext()) {
				Node<E> currentNode = iter.next();


                //Internal nodes are the only ones with children
                if(currentNode instanceof InternalNode){
                    InternalNode<E> n = (InternalNode)currentNode;
                    nextLevel.addAll(n.getChildren());
                }

				System.out.print(currentNode.getValues());
				if(iter.hasNext()){
					System.out.print(" # ");
				}
			}
			System.out.println();
			currentLevel = nextLevel;
			nextLevel = new LinkedList<>();

		}

	}

	/*************** PRIVATE HELPER METHODS ******************/

	/**
	 * Takes the result of a node splitting and inserts the two new nodes
	 * values and pointers into their parent
	 * @param leftLeaf the left leaf (this is the original leaf that overflowed)
	 * @param rightLeaf the right leaf (this is the new leaf that took on the overflowed values and possibly pointers)
	 */
	private void insertInParent(Node<E> leftLeaf, Node<E> rightLeaf){
		//The parent of the original leaf is null so we must be at the root of the tree
		if(leftLeaf.getParent() == null){
			if(DEBUG){
				System.out.println("Parent is null so creating a new root and updating values/pointers");
			}

			//create a new root
			this.root = new InternalNode<>(degree);

			//set left and right's parent to new root
			leftLeaf.setParent((InternalNode<E>) this.root);
			rightLeaf.setParent((InternalNode<E>)this.root);

			//Insert the smallest value of right split node into parent
			root.addValue(rightLeaf.getValue(0));

			//If right leaf is an internal node then the value it gives to its parent is removed from it
			if(rightLeaf instanceof InternalNode){
				rightLeaf.removeValue(0);
			}

			((InternalNode<E>)root).addChild(leftLeaf);
			((InternalNode<E>)root).addChild(rightLeaf);

			if (DEBUG) {
				System.out.println("New root is created and pointers are updated");
				System.out.println("The root is "+root);
				System.out.println("The roots children are " + Arrays.toString(((InternalNode)root).getChildren().toArray()));
				}
		}

		//Parent was not null so must insert into it
		else{
			if(DEBUG){
				System.out.println("Parent is not null so must insert into parent");
			}

			//Get the parent
			InternalNode<E> parent = leftLeaf.getParent();
			//Set right's parent to be the same as left's parent
			//TODO: Is this necessary?? Do some checking to see if it is ever null when we get to here; It probably is necessary
			rightLeaf.setParent(parent);

			//Parent has space so we can just insert into it
			if(!parent.isFull()) {
				if (DEBUG) {
					System.out.println("Parent is NOT full so we can just insert into it");
				}

				int insertIndex = parent.addValue(rightLeaf.getValue(0));

				if (DEBUG) {
					System.out.println("The smallest value was " + rightLeaf.getValue(0) + " it was inserted at " + insertIndex
							+ " in " + parent);

				}

				//Insert the new child into the list of children in the parent
				//The child is inserted at an index of +1 above the index
				//where the key value for the node was inserted into the parent
				//This is because there are 1 more entries in the children list
				//than the values list
				parent.addChild(rightLeaf);

				if (DEBUG) {
					System.out.println("Parent now has the children "+Arrays.toString(parent.getChildren().toArray()));
				}

			}
			//Parent is full so we must split it and then insert in its parent
			else {
				if (DEBUG) {
					System.out.println("Parent is full so we must do some internal node splitting work!");
					System.out.println("Naively add to the parent and then pass off to the slitInternalNode method");
				}

				//TODO: Finish this in tandem with the splitInternalNode method
				//Pretty much I think we can naively add call the split internal node method and then
				//it will propagate the split up

				//naively insert the value into it
				parent.addValue(rightLeaf.getValue(0));
				if(rightLeaf instanceof InternalNode){
					if(DEBUG) {
						System.out.println("Right leaf is an internal node so we added its smalles value into parent and removed it from itself");
					}
					rightLeaf.removeValue(0);
				}
				//Naively insert the pointer into it
				//Call split internal node
			}
		}
	}

	/**
	 * This method takes in an overflowing InternalNode. Splits it,
	 * and propagates the split updwards to parents
	 * @param overflownNode
	 */
	private void splitInternalNode(InternalNode<E> overflownNode){
	    if(DEBUG){
            System.out.println("Copying over and deleting values and children from overflownNode to new rightNode");
        }
        //Copy over and delete values to new node
        InternalNode<E> rightNode = moveValuesAndPointersToNewInternalNode(overflownNode);

        //TODO: finish filling out this method
    }

	/**
	 * This method takes in an overflowing leaf node. Splits it
	 * and then propagates that split up to parents
	 * @param overFlownLeaf the leaf that is now overflowing
	 */
	private void splitLeafNode(LeafNode<E> overFlownLeaf){
		if(DEBUG){
			System.out.println("Copying over and deleting values from overFlownLeaf to new rightLeaf");
			System.out.println("Adjusting leaf nodes next pointers");
		}

		//Copy second half of values to new leaf, remove values from original once copied
		LeafNode<E> rightLeaf = this.moveValuesToNewLeaf(overFlownLeaf);

		//Adjust pointers between nodes and next leaf
		rightLeaf.setNextLeaf(overFlownLeaf.getNextLeaf());
		overFlownLeaf.setNextLeaf(rightLeaf);

		if(DEBUG){
			System.out.println("Leaf is split now inserting split in parent");
		}

		this.insertInParent(overFlownLeaf, rightLeaf);
	}

    /**
     * Copies half of the values and chilren pointers from an overflowing internal node to a new internal node.
     * Then deletes the copied values and children once they are transferred.
     * @param overflownNode the old overflowing node
     * @return the new node containing half the values and pointers
     */
    public InternalNode<E> moveValuesAndPointersToNewInternalNode(InternalNode<E> overflownNode){
		//Create new node
		InternalNode<E> rightNode = new InternalNode<E>(degree);

		//Copy over half of the values
		rightNode.addCollectionOfValues(new ArrayList<>(overflownNode.subListValues((int)Math.ceil((degree-1)/2), overflownNode.sizeOfValues())));
		//Copy over half of the children pointers
        rightNode.addCollectionOfChildren(new ArrayList<>(overflownNode.subListChildren((int)Math.ceil((degree+1)/2), overflownNode.sizeOfChildren())));




        //TODO:Complete
        //Delete the copied values from the overflown leaf
        overflownNode.removeRangeOfValues((int)Math.ceil((degree-1)/2), overflownNode.sizeOfValues());
        //Delete the copied children pointers from overflown leaf
        overflownNode.removeRangeOfChildren((int)Math.ceil((degree+1)/2), overflownNode.sizeOfChildren());

		System.out.println((Arrays.toString(rightNode.getValues().toArray())));
		System.out.println(Arrays.toString(rightNode.getChildren().toArray()));

		System.out.println();
		System.out.println((Arrays.toString(overflownNode.getValues().toArray())));
		System.out.println(Arrays.toString(overflownNode.getChildren().toArray()));


		return rightNode;
    }

    /**
     * Copies half of of the values from the old leaf into a new leaf. Then deletes
     * the values copied from the original once they are transferred.
     * @param overflownLeaf the old leaf containing the values to be copied
     * @return the newly created leaf containg the values
     */
    private LeafNode<E> moveValuesToNewLeaf(LeafNode<E> overflownLeaf){
        //Create new leaf node
        LeafNode<E> rightLeaf = new LeafNode<E>(degree);

        //Copy half of the original leaves values into it
        rightLeaf.addCollectionOfValues(new ArrayList<E>(overflownLeaf.subListValues((int)Math.ceil((degree-1)/2), overflownLeaf.sizeOfValues())));
        //Delete the copied values from the overflown leaf
        overflownLeaf.removeRangeOfValues((int)Math.ceil((degree-1)/2), overflownLeaf.sizeOfValues());

        return rightLeaf;
    }

	//TODO: complete and TEST
	/**
	 * Find the lead where value should be inserted
	 * @param value the value we wish to addValue
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
					return findLeafFor(value, ((InternalNode<E>)localRoot).getChildren().get(0));
				}

				//Value should go in rightmost pointers subtree
				//TODO: The check after the && is extraneous we will only ever get to here in the loop if it true
				else if(i==localRoot.sizeOfValues()-1 && value.compareTo(localRoot.getValue(i)) >= 0){
					return findLeafFor(value, ((InternalNode<E>)localRoot).getChildren().get(i+1));
				}

				//Value should go in one of the internal pointers subtree
				else if(value.compareTo(localRoot.getValue(i)) >= 0 && value.compareTo(localRoot.getValue(i+1)) < 0) {
					// i <= value < i+1; so it value goes in the i+1 childs subtree
					return findLeafFor(value, ((InternalNode<E>)localRoot).getChildren().get(i + 1));
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
}
