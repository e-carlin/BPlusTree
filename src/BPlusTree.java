import java.util.*;

public class BPlusTree<E extends Comparable <E>>{

	private final boolean DEBUG = false;
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
				System.out.println("Found leaf node for "+value+" is "+ originalLeaf);
				System.out.println("Leaf's parent is "+originalLeaf.getParent());
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
	 * values and  pointers into their parent
	 * @param leftNode the left node (this is the original node that overflowed)
	 * @param rightNode the node leaf (this is the new node that took on the overflowed values and possibly pointers)
	 */
	private void insertInParent(Node<E> leftNode, Node<E> rightNode){
		//The parent of the original node is null so we must be at the root of the tree
		if(leftNode.getParent() == null){
			if(DEBUG){
				System.out.println("Parent is null so creating a new root and updating values/pointers");
			}

			//create a new root
			this.root = new InternalNode<>(degree);

			//set left and right's parent to new root
			leftNode.setParent((InternalNode<E>) this.root);
			rightNode.setParent((InternalNode<E>)this.root);

			//Insert the smallest value of right split node into parent
			root.addValue(rightNode.getValue(0));

			//If right leaf is an internal node then the value it gives to its parent is removed from it
			if(rightNode instanceof InternalNode){
				rightNode.removeValue(0);
			}

			((InternalNode<E>)root).addChild(leftNode);
			((InternalNode<E>)root).addChild(rightNode);

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
			InternalNode<E> parent = leftNode.getParent();
			//Set right's parent to be the same as left's parent
			rightNode.setParent(parent);

			//Parent has space so we can just insert into it
			if(!parent.isFull()) {
				if (DEBUG) {
					System.out.println("Parent is NOT full so we can just insert into it");
				}

				//Add the value into the parent
				int insertIndex = parent.addValue(rightNode.getValue(0));

				//If right leaf is an internal node then the value it gives to its parent is removed from it
				if(rightNode instanceof InternalNode){
					rightNode.removeValue(0);
				}

				//Insert the new child into the list of children in the parent
				parent.addChild(rightNode);

				if (DEBUG) {
					System.out.println("Parent is now "+parent);

				}
			}
			//Parent is full so we must split it and then new split in its parent
			else {
				if (DEBUG) {
					System.out.println("Parent is full so we must do some internal node splitting work!");
				}

				//naively insert the value into it
				parent.addValue(rightNode.getValue(0));
				if(rightNode instanceof InternalNode){
					if(DEBUG) {
						System.out.println("Right leaf is an internal node so we removed smallest value from it");
					}
					rightNode.removeValue(0);
				}
				//Naively insert the pointer into it
				parent.addChild(rightNode);
				if(DEBUG) {
					System.out.println("We naively added the values and pointers to the now overflowing parent");
					this.printTree();
				}
				//Call split internal node
				//TODO: Call split inernal node and then it will split and call insert in parent
				this.splitInternalNode(parent);
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
        //Copy over and delete values/pointers to new node
        InternalNode<E> rightNode = moveValuesAndPointersToNewInternalNode(overflownNode);
		if(DEBUG){
			System.out.println("Left node is ");
			System.out.println(overflownNode);
			System.out.println("Children are "+overflownNode.getChildren());
			System.out.println("Right node is");
			System.out.println(rightNode);
			System.out.println("Children are "+rightNode.getChildren());
			System.out.println("Node is split now inserting in parent");

		}
		//TODO: Insert into parent
		this.insertInParent(overflownNode, rightNode);
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
		rightNode.addCollectionOfValues(new ArrayList<>(overflownNode.subListValues((int)Math.ceil((degree-1)/2.0), overflownNode.sizeOfValues())));
		//Copy over half of the children pointers
        rightNode.addCollectionOfChildren(new ArrayList<>(overflownNode.subListChildren((int)Math.ceil((degree+1)/2.0), overflownNode.sizeOfChildren())));
        //set the child pointers to point to new node
		rightNode.setParentToBeThisNode();

        //Delete the copied values from the overflown leaf
        overflownNode.removeRangeOfValues((int)Math.ceil((degree-1)/2.0), overflownNode.sizeOfValues());
        //Delete the copied children pointers from overflown leaf
        overflownNode.removeRangeOfChildren((int)Math.ceil((degree+1)/2.0), overflownNode.sizeOfChildren());

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
        rightLeaf.addCollectionOfValues(new ArrayList<E>(overflownLeaf.subListValues((int)Math.ceil((degree-1)/2.0), overflownLeaf.sizeOfValues())));

		//Delete the copied values from the overflown leaf
        overflownLeaf.removeRangeOfValues((int)Math.ceil((degree-1)/2.0), overflownLeaf.sizeOfValues());

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
