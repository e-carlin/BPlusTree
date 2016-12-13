import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A general node. The node stores a list of values.
 * @author Evan Carlin
 */
public class Node<E extends Comparable<E>>{

	protected int degree; //The degree of the node
	protected ArrayList<E> values; //The list of values
	protected InternalNode<E> parent; //The parent of this node

	/**
	 * Constructs a new Node
	 * @param d the degree of the node
	 */
	public Node(int d){
		parent = null;
		degree = d;
		values = new ArrayList<>();
	}

	/**
	 * Inserts a value in sorted (ascending order)
	 * @param elemToInsert the element we wish to insert
	 */
	public void addValue(E elemToInsert){
		for(int i=0; i<values.size(); i++){
			//elemToInsert is bigger than this element so keep going
			if(elemToInsert.compareTo(values.get(i)) > 0){
				continue;
			}
			//elemToInsert is <= this element so addValue it here
			values.add(i, elemToInsert);
			return;
		}
		//All elements where < elemToInsert so append to end of list
		values.add(elemToInsert);
		return;
	}

	/**
	 * Adds a collection of values to the node in sorted order.
	 * @param valuesToAdd the values we wish to add
	 */
	public void addCollectionOfValues(ArrayList<E> valuesToAdd){

		for(E value : valuesToAdd){
			this.addValue(value);
		}
	}

	/**
	 * Gets the number of values this Node contains
	 * @return the number of values
	 */
	public int sizeOfValues(){

		return values.size();
	}

	/**
	 * Returns a value at a specific index
	 * @param index the index of the value
	 * @return the value at that index
	 */
	public E getValue(int index){

		return values.get(index);
	}

	/**
	 * Removes a value at a specific index
	 * @param index the index of the value we wish to remove
	 */
	public void removeValue(int index) {

		values.remove(index);
	}

	/**
	 * Gets a sublist of values
	 * @param from the start index (inclusive)
	 * @param to the end index (exclusive)
	 * @return the sublist of values
	 */
	public List<E> subListValues(int from, int to){

		return values.subList(from, to);
	}

	/**
	 * Removes a range of values from this node
	 * @param from the start index (inclusive)
	 * @param to the end index (exclusive)
	 */
	public void removeRangeOfValues(int from, int to){

		values.subList(from, to).clear();
	}

	/**
	 * Gets the parent of this Node
	 * @return the parent
	 */
	public InternalNode<E> getParent(){

		return parent;
	}

	/**
	 * Checks whether or not this node can contain any more values
	 * @return true if it can't false if it can contain more values
	 */
	public boolean isFull(){

		return values.size() >= degree-1;
	}

	/**
	 * Sets the parent of this node
	 * @param p the Node we wish to be our parent
	 */
	public void setParent(InternalNode<E> p){

		parent = p;
	}

	/**
	 * Get all of the values contained within this node
	 * @return a list of the values
	 */
	public ArrayList<E> getValues(){

		return values;
	}

	/**
	 * Returns a string of all of the values in this node
	 * @return all of the values
	 */
	@Override
	public String toString(){

		return Arrays.toString(values.toArray());
	}

}

