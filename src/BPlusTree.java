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
				//Handle the splitting and propagation
                this.splitLeafNodeAndPropagate(originalLeaf);
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
	 * This method takes in an overflowing leaf nodes. Splits it
     * and then propagates that split up to parents
	 * @param overFlownLeaf the leaf that is now overflowing
	 */
	private void splitLeafNodeAndPropagate(LeafNode<E> overFlownLeaf){
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
			System.out.println("Propagating a leaf split");
		}

		//If the parent is null then originalLeaf is the root
		if(overFlownLeaf.getParent() == null) {
			if (DEBUG) {
				System.out.println("Parent is null so leaf was the root. Creating a new root...");
			}

			root = new InternalNode<E>(degree);
			root.putValue(rightLeaf.getValue(0));

			if (DEBUG) {
				System.out.println("New root is " + root + " should contain the smallest value of " + rightLeaf);
				System.out.println("Setting the roots children to be the leaves");
			}

			overFlownLeaf.setParent((InternalNode<E>) root);
			rightLeaf.setParent((InternalNode<E>) root);


			//*** Order is important here **** MUST set left then right because left contains smaller values the righ
			((InternalNode<E>)root).addChild(overFlownLeaf);
			((InternalNode<E>)root).addChild(rightLeaf);

			if (DEBUG) {
				//TODO: This wasn't here before refactoring no instance of check and no cast
			    if(root instanceof InternalNode) {
				    System.out.println("The roots children are " + Arrays.toString(((InternalNode)root).getChildren().toArray()));
                }
			}
		}
		else{
			//Parent was not null so we must insert into it
				if(DEBUG){
					System.out.println("Parent was not null so must insert into parent");
				}

				InternalNode<E> parent = overFlownLeaf.getParent();

				//Parent is NOT full so we can just insert into it and adjust pointers
				if(!parent.isFull()) {
					if (DEBUG) {
						System.out.println("Parent is NOT full so we can just insert into it");
					}

					int insertIndex = parent.putValue(rightLeaf.getValue(0));

					if (DEBUG) {
						System.out.println("The smallest value was " + rightLeaf.getValue(0) + " it was inserted at " + insertIndex
								+ " in " + parent);

					}

					//Insert the new child into the list of children in the parent
                    //The child is inserted at an index of +1 above the index
                    //where the key value for the node was inserted into the parent
                    //This is because there are 1 more entries in the children list
                    //than the values list
					parent.addChild(insertIndex+1, rightLeaf);

					if (DEBUG) {
						System.out.println("Parent now has the children "+Arrays.toString(parent.getChildren().toArray()));
                        System.out.println("Setting the right leaves parent node");
                    }

                    rightLeaf.setParent(parent);
				}

				else{
				    if(DEBUG){
                        System.out.println("Parent is full so we must do some internal node splitting work!");
                        System.out.println("Naively add to the parent and then pass off to the internal node split and propagate method");
                    }


                    //Naively add the value to the internal node
                    parent.putValue(rightLeaf.getValue(0));
				    //TODO: The add child logic isn't right. If the split node was not on the end then the pointer shouldn't be added to the end of the parent's children
                    //Naively add a pointer to the split node
                    parent.addChild(rightLeaf);
                    //Handle the now overflown node
                    splitInternalNodeAndPropagate(parent);
                }


		}
	}


	public void splitInternalNodeAndPropagate(InternalNode<E> overflownNode){
	    if(DEBUG){
            System.out.println("Copying over and deleting values and children from overflownNode to new rightNode");
        }

        //Copy over and delete values to new node
        InternalNode<E> rightNode = moveValuesAndPointersToNewInternalNode(overflownNode);

        //TODO: finish filling out this method
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
		rightNode.putCollectionOfValues(new ArrayList<>(overflownNode.subListValues((int)Math.ceil((degree-1)/2), overflownNode.sizeOfValues())));
		//Copy over half of the children pointers
        rightNode.putCollectionOfChildren(new ArrayList<>(overflownNode.subListChildren((int)Math.ceil((degree+1)/2), overflownNode.sizeOfChildren())));


        //TODO:Complete
        //Delete the copied values from the overflown leaf
        overflownNode.removeRange((int)Math.ceil((degree-1)/2), overflownNode.sizeOfValues());
        //Delete the copied children pointers from overflown leaf
//        overflownNode.removeRange();
        System.out.println(Arrays.toString(rightNode.getChildren().toArray()));
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
        rightLeaf.putCollectionOfValues(new ArrayList<E>(overflownLeaf.subListValues((int)Math.ceil((degree-1)/2), overflownLeaf.sizeOfValues())));
        //Delete the copied values from the overflown leaf
        overflownLeaf.removeRange((int)Math.ceil((degree-1)/2), overflownLeaf.sizeOfValues());

        return rightLeaf;
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



	public String toString(){
		if(this.isEmpty()){
			return "The tree is empty";
		}
		//TODO: Print more than the root...
		return root.toString();
	}
}
