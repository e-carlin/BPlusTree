import java.util.LinkedList;
import java.util.Queue;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * A class to construct a B+ Tree
 * The tree supports insertion, searching, and level order printing
 *
 * @author Evan Carlin
 */
public class BPlusTree<E extends Comparable<E>> {

    private Node<E> root;
    private int degree;

    /**
     * Construcst a B+ Tree with nodes of degree d
     *
     * @param d the degree of the nodes
     */
    public BPlusTree(int d) {
        if (d < 3) {
            throw new IllegalArgumentException("The degree specified is <4");
        } else {
            degree = d;
            root = null;
        }
    }

    /**
     * Inserts a value into the tree.
     *
     * @param value the value we wish to inset
     */
    public void insertValue(E value) {

        if(!this.search(value)) { //Only add values that aren't already in the tree
            //Tree is empty so we can just create a leaf node and addValue the value
            if (this.isEmpty()) {

                //Create the root and add the value to it
                root = new LeafNode<>(degree);
                root.addValue(value);
            } else {
                //Tree is not empty so we must find the leaf where value should go
                LeafNode<E> leafToInsertValueIn = findLeafFor(value);

                //The leaf node isn't full so we can just add the value into it!
                if (!leafToInsertValueIn.isFull()) {
                    leafToInsertValueIn.addValue(value);
                }

                // The node is full so we must split it and recurse
                else {
                    //First naively insert value into (now) overflown node
                    leafToInsertValueIn.addValue(value);
                    //Split the now overflown node
                    this.splitLeafNode(leafToInsertValueIn);
                }
            }
        }
    }

    /**
     * Prints the tree in level order
     * Adapted from Reddy's answer: http://stackoverflow.com/questions/2241513/java-printing-a-binary-tree-using-level-order-in-a-specific-format
     */
    public String toString() {
        String treeString = "******** PRINTING TREE ********\n";

        if (this.isEmpty()) {
            return treeString + "[] #\n";
        }

        Queue<Node<E>> currentLevel = new LinkedList<>(); //The current level we are examinig
        Queue<Node<E>> nextLevel = new LinkedList<>(); //The children of the current level

        currentLevel.add(this.root); //Begin by adding the root

        while (!currentLevel.isEmpty()) {
            Iterator<Node<E>> iter = currentLevel.iterator(); //Iterate over the nodes in the current level
            while (iter.hasNext()) {
                Node<E> currentNode = iter.next(); //Get the next node

                //Internal nodes are the only ones with children
                if (currentNode instanceof InternalNode) {
                    nextLevel.addAll(((InternalNode) currentNode).getChildren()); //Add the nodes children to the nextLevel queue
                }

                treeString += currentNode.getValues(); //print the current level
                if (iter.hasNext()) {
                    treeString += " # "; //Print a delimiter between nodes in the same level
                }
            }
            //Don't add a new lin
            if (iter.hasNext()) {
                treeString += " #";
            } else {
                treeString += " #\n";
            }

            currentLevel = nextLevel; //Jump to the next level
            nextLevel = new LinkedList<>(); //Reset

        }
        return treeString;
    }

    /**
     * Searches to see if the tree contains a specific value
     *
     * @param value the value we wish to see if the tree contains
     * @return true if the tree contains the value false if it doesn't
     */
    public boolean search(E value) {
        LeafNode<E> leafForValue = findLeafFor(value);
        if(leafForValue == null){ //No leaf exists so the tree is empty
            return false; //If the tree is empty then it doesn't contain the value we were looking for
        }
        return leafForValue.contains(value);

    }

    /*************** PRIVATE HELPER METHODS ******************/

    /**
     * Takes the result of a node splitting and updates the parent with the split information
     *
     * @param leftNode  the original node that overflowed (has smaller values so it to the "left")
     * @param rightNode the new node that took on the overflowed values and possibly pointers (has greater values so is to the "right")
     */
    private void insertInParent(Node<E> leftNode, Node<E> rightNode) {
        //The parent of the original node is null so we must be at the root of the tree
        if (leftNode.getParent() == null) {
            //create a new root
            this.root = new InternalNode<>(degree);

            //set left and right's parent to new root
            leftNode.setParent((InternalNode<E>) this.root);
            rightNode.setParent((InternalNode<E>) this.root);

            //Insert the smallest value of right split node into parent
            root.addValue(rightNode.getValue(0));

            //If right leaf is an internal node then the value it gives to its parent is removed from it
            if (rightNode instanceof InternalNode) {
                rightNode.removeValue(0);
            }

            //Set the roots children to the new split nodes
            ((InternalNode<E>) root).addChild(leftNode);
            ((InternalNode<E>) root).addChild(rightNode);
        }
        //Parent was not null so must insert into it
        else {
            //Get the parent
            InternalNode<E> parent = leftNode.getParent();
            //Set right's parent to be the same as left's parent
            rightNode.setParent(parent);

            //Parent has space so we can just insert into it
            if (!parent.isFull()) {
                //Add the value into the parent
                parent.addValue(rightNode.getValue(0));

                //If right leaf is an internal node then the value it gives to its parent is removed from it
                if (rightNode instanceof InternalNode) {
                    rightNode.removeValue(0);
                }

                //Insert the new child into the list of children in the parent
                parent.addChild(rightNode);
            }
            //Parent is full so we must split it and then insert into its parent
            else {
                //naively insert the value into it
                parent.addValue(rightNode.getValue(0));
                if (rightNode instanceof InternalNode) {
                    rightNode.removeValue(0);
                }
                //Naively insert the pointer into it
                parent.addChild(rightNode);
                //Split the now overflowing node
                this.splitInternalNode(parent);
            }
        }
    }

    /**
     * This method takes in an overflowing InternalNode, splits it,
     * and propagates the split upwards to its parent
     *
     * @param overflownNode the node that is overflowing
     */
    private void splitInternalNode(InternalNode<E> overflownNode) {
        //Copy over and delete values/pointers to new node
        InternalNode<E> rightNode = moveValuesAndPointersToNewInternalNode(overflownNode);
        this.insertInParent(overflownNode, rightNode);
    }

    /**
     * This method takes in an overflowing leaf node, splits it
     * and then propagates that split up to parent.
     *
     * @param overFlownLeaf the leaf that is now overflowing
     */
    private void splitLeafNode(LeafNode<E> overFlownLeaf) {

        //Copy second half of values to new leaf, remove values from original once copied
        LeafNode<E> rightLeaf = this.moveValuesToNewLeaf(overFlownLeaf);

        //TODO: the adjusting of these pointers should probably happen in move values to new leaf
        //Adjust pointers between nodes and next leaf
        rightLeaf.setNextLeaf(overFlownLeaf.getNextLeaf());
        overFlownLeaf.setNextLeaf(rightLeaf);
        //Insert new leaf in parent
        this.insertInParent(overFlownLeaf, rightLeaf);
    }

    /**
     * Copies half of the values and children pointers from an overflowing internal node to a new internal node.
     * Then deletes the copied values and children once they are transferred.
     *
     * @param overflownNode the old overflowing node
     * @return the new node containing half the values and pointers
     */
    private InternalNode<E> moveValuesAndPointersToNewInternalNode(InternalNode<E> overflownNode) {
        //Create new node
        InternalNode<E> rightNode = new InternalNode<>(degree);

        //Copy over half of the values
        rightNode.addCollectionOfValues(new ArrayList<>(overflownNode.subListValues((int) Math.ceil((degree - 1) / 2.0), overflownNode.sizeOfValues())));
        //Copy over half of the children pointers
        rightNode.addCollectionOfChildren(new ArrayList<>(overflownNode.subListChildren((int) Math.ceil((degree + 1) / 2.0), overflownNode.sizeOfChildren())));
        //set the child pointers to point to new node
        rightNode.setParentToBeThisNode();

        //Delete the copied values from the overflown leaf
        overflownNode.removeRangeOfValues((int) Math.ceil((degree - 1) / 2.0), overflownNode.sizeOfValues());
        //Delete the copied children pointers from overflown leaf
        overflownNode.removeRangeOfChildren((int) Math.ceil((degree + 1) / 2.0), overflownNode.sizeOfChildren());

        return rightNode;
    }

    /**
     * Copies half of of the values from the old leaf into a new leaf. Then deletes
     * the values copied from the original once they are transferred.
     *
     * @param overflownLeaf the old leaf containing the values to be copied
     * @return the newly created leaf containg the values
     */
    private LeafNode<E> moveValuesToNewLeaf(LeafNode<E> overflownLeaf) {
        //Create new leaf node
        LeafNode<E> rightLeaf = new LeafNode<>(degree);

        //Copy half of the original leaves values into it
        rightLeaf.addCollectionOfValues(new ArrayList<>(overflownLeaf.subListValues((int) Math.ceil((degree - 1) / 2.0), overflownLeaf.sizeOfValues())));

        //Delete the copied values from the overflown leaf
        overflownLeaf.removeRangeOfValues((int) Math.ceil((degree - 1) / 2.0), overflownLeaf.sizeOfValues());

        return rightLeaf;
    }

    /**
     * Starts the findLeafForHelper by starting the search at the root
     *
     * @param value the value we wish to find a leaf for
     * @return the leaf where the value should go
     */
    private LeafNode<E> findLeafFor(E value) {
        return findLeafForHelper(value, root);
    }

    /**
     * Find the leaf where value should be inserted
     *
     * @param value     the value we wish to find a leaf for
     * @param localRoot the local node we wish to examine
     * @return the leaf node where value should be inserted
     */
    private LeafNode<E> findLeafForHelper(E value, Node<E> localRoot) {
        if(localRoot != null) { //if localRoot is null then the tree was empty
            //We have reached a leaf so this node is where value should be placed (naive, the leaf could be full)
            if (localRoot instanceof LeafNode) {
                return (LeafNode<E>) localRoot;
            }
            //We still have some of the tree to traverse to get down to leaves
            else {
                //Look through this nodes values to find which pointer we should follow
                for (int i = 0; i < localRoot.sizeOfValues(); i++) {
                    //value < root.value[i] so we should recurse on this child
                    if (value.compareTo(localRoot.getValue(i)) < 0) {
                        return findLeafForHelper(value, ((InternalNode<E>) localRoot).getChild(i));
                    }
                }
            }
            return findLeafForHelper(value, ((InternalNode<E>) localRoot).getChild(((InternalNode<E>) localRoot).sizeOfChildren() - 1));
        }
        return null; //tree was empty so no leaf exists
    }

    /**
     * Returns wether or not the tree is empty
     * @return true if the tree is empty, false otherwise
     */
    private boolean isEmpty() {

        return root == null;
    }
}
