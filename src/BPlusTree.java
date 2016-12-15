import java.util.LinkedList;
import java.util.Queue;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class to construct a B+ Tree
 * The tree supports insertion, searching, and level order printing
 * @author Evan Carlin
 * @param <E>
 */
public class BPlusTree<E extends Comparable <E>>{

	private final boolean DEBUG = false; //For testing purposes

	private Node<E> root;
	private int degree;

	/**
	 * Construcst a B+ Tree with nodes of degree d
	 * @param d the degree of the nodes
	 */
	public BPlusTree(int d){
		if(d <3){
			throw new IllegalArgumentException("The degree specified is <4");
		}
		else {
			degree = d;
			root = null;
		}
	}

	/**
	 * Inserts a value into the tree.
	 * @param value the value we wish to inset
	 */
	public void insertValue(E value){

		if(DEBUG){
			System.out.println(this.toString());
			System.out.println("\nAdding "+value+" to tree");
		}

		//Tree is empty so we can just create a leaf node and addValue the value
		if(this.isEmpty()){
			if(DEBUG){
				System.out.println("Tree is empty so creating root and adding to it");
			}
			//Create the root and add the value to it
			root = new LeafNode<>(degree);
			root.addValue(value);
		}
		else{
			//Tree is not empty so we must find the leaf where value should go
			LeafNode<E> originalLeaf = findLeafFor(value);

			if(DEBUG){
				System.out.println("Tree is not empty");
				System.out.println("Found leaf node for "+value+" is "+ originalLeaf);
				System.out.println("Leaf's parent is "+originalLeaf.getParent());
			}

			//The leaf node isn't full so we can just add the value into it!
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
				//Split the now overflown node
                this.splitLeafNode(originalLeaf);
			}
		}
	}

	/**
	 * Prints the tree in level order
	 * Adapted from Reddy's answer: http://stackoverflow.com/questions/2241513/java-printing-a-binary-tree-using-level-order-in-a-specific-format
	 */
	public String toString() {
		String treeString = "******** PRINTING TREE ********\n";

		if(this.isEmpty()){
            return treeString+"[] #";
        }

		Queue<Node<E>> currentLevel = new LinkedList<>(); //The current level we are examinig
		Queue<Node<E>> nextLevel = new LinkedList<>(); //The children of the current level

		currentLevel.add(this.root); //Begin by adding the root

		while (!currentLevel.isEmpty()) {
			Iterator<Node<E>> iter = currentLevel.iterator(); //Iterate over the nodes in the current level
			while (iter.hasNext()) {
				Node<E> currentNode = iter.next(); //Get the next node

                //Internal nodes are the only ones with children
                if(currentNode instanceof InternalNode){
                    nextLevel.addAll(((InternalNode)currentNode).getChildren()); //Add the nodes children to the nextLevel queue
                }

				treeString += currentNode.getValues(); //print the current level
				if(iter.hasNext()){
					treeString +=" # "; //Print a delimiter between nodes in the same level
				}
			}
			//Don't add a new lin
			if(iter.hasNext()){
				treeString += " #";
			}
			else {
				treeString += " #\n";
			}

			currentLevel = nextLevel; //Jump to the next level
			nextLevel = new LinkedList<>(); //Reset

		}
		return treeString; //Remove last \n
	}

	/**
	 * Searches to see if the tree contains a specific value
	 * @param value the value we wish to see if the tree contains
	 * @return true if the tree contains the value false if it doesn't
	 */
	public boolean search(E value){
		LeafNode<E> leafForValue = findLeafFor(value);
		return leafForValue.contains(value);

	}

	/*************** PRIVATE HELPER METHODS ******************/

	/**
	 * Takes the result of a node splitting and updates the parent with the split information
	 * @param leftNode he original node that overflowed (has smaller values so it to the "left")
	 * @param rightNode the new node that took on the overflowed values and possibly pointers (has greater values so is to the "right")
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

			//Set the roots children to the new split nodes
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
				parent.addValue(rightNode.getValue(0));

				//If right leaf is an internal node then the value it gives to its parent is removed from it
				if(rightNode instanceof InternalNode){
					rightNode.removeValue(0);
				}

				//Insert the new child into the list of children in the parent
				parent.addChild(rightNode);
			}
			//Parent is full so we must split it and then insert into its parent
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
					System.out.println(this.toString());
				}
				//Split the now overflowing node
				this.splitInternalNode(parent);
			}
		}
	}

	/**
	 * This method takes in an overflowing InternalNode. Splits it,
	 * and propagates the split upwards to its parent
	 * @param overflownNode the node that is overflowing
	 */
	private void splitInternalNode(InternalNode<E> overflownNode){
	    if(DEBUG){
            System.out.println("Copying over and deleting values and children from overflownNode to new rightNode");
        }
        //Copy over and delete values/pointers to new node
        InternalNode<E> rightNode = moveValuesAndPointersToNewInternalNode(overflownNode);

		if(DEBUG){
			System.out.println("Leaf is split now inserting split in parent");
		}

		this.insertInParent(overflownNode, rightNode);
    }

	/**
	 * This method takes in an overflowing leaf node. Splits it
	 * and then propagates that split up to parent.
	 * @param overFlownLeaf the leaf that is now overflowing
	 */
	private void splitLeafNode(LeafNode<E> overFlownLeaf){
		if(DEBUG){
			System.out.println("Copying over and deleting values from overFlownLeaf to new rightLeaf");
			System.out.println("Adjusting leaf nodes next pointers");
		}

		//Copy second half of values to new leaf, remove values from original once copied
		LeafNode<E> rightLeaf = this.moveValuesToNewLeaf(overFlownLeaf);

		//TODO: the adjusting of these pointers should probably happen in move values to new leaf
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
    private InternalNode<E> moveValuesAndPointersToNewInternalNode(InternalNode<E> overflownNode){
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

	/**
	 * Starts the findLeafForHelper by starting the search at the root
	 * @param value the value we wish to find a leaf for
	 * @return the leaf where the value should go
	 */
	private LeafNode<E> findLeafFor(E value){
    	return findLeafForHelper(value, root);
	}

    //TODO: Refactor to remove null return
	/**
	 * Find the lead where value should be inserted
	 * @param value the value we wish to addValue
	 * @param localRoot the local node we wish to examine
	 * @return the leaf node where value should be inserted
	 */
	private LeafNode<E> findLeafForHelper(E value, Node<E> localRoot){
		//We have reached a leaf so this node is where value should be placed (naive, the leaf could be full)
		if(localRoot instanceof LeafNode){
			return (LeafNode)localRoot;
		}
		//We still have some of the tree to traverse to get down to leaves
		else{
			//Look through this nodes values to find which pointer we should follow
			for(int i=0; i<localRoot.sizeOfValues(); i++){
				//value < root.value[i] so we should recurse on this child
				if(value.compareTo(localRoot.getValue(i)) < 0){
					return findLeafForHelper(value, ((InternalNode<E>)localRoot).getChild(i));
				}
			}
		}
		return findLeafForHelper(value, ((InternalNode<E>)localRoot).getChild(((InternalNode<E>)localRoot).sizeOfChildren()-1));
	}



	private boolean isEmpty(){

		return root == null;
	}
}
