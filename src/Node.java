import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class Node<E extends Comparable<E>>{

	//TODO: make private
	private int degree;
	private ArrayList<E> values;
	private InternalNode<E> parent;
	
	public Node(int d){
		parent = null;
		degree = d;
		values = new ArrayList<E>();
	}

	/**
	 * Gets all of the children nodes
	 * @return a list of all of the children
	 */
	public abstract ArrayList<Node<E>> getChildren();

	/**
	 * Gets a child at a specific index
	 * @param index the index of the child
	 * @return the child node
	 */
	public abstract Node<E> getChild(int index);
	
	public void putValue(E elemToInsert){
		for(int i=0; i<values.size(); i++){
			//elemToInsert is bigger than this element so keep going
			if(elemToInsert.compareTo(values.get(i)) > 0){
				continue;
			}
			//elemToInsert is <= this element so putValue it here
			values.add(i, elemToInsert);
			return;
		}
		//All elements where < elemToInsert so append to end of list
		values.add(elemToInsert);
	}

	/**
	 * CAUTION: This naively adds elements to the node. It makes not effort to sort the values
	 * @param valuesToAdd the values we wish to add
	 */
	public void putCollectionOfValues(ArrayList<E> valuesToAdd){
		values.addAll(valuesToAdd);
	}

	public int sizeOfValues(){
		return values.size();
	}

	public E getValue(int index){

		return values.get(index);
	}

	public void removeValue(int index) {

		values.remove(index);
	}

	public List<E> subList(int from, int to){
		return values.subList(from, to);
	}

	public void removeRange(int from, int to){
		values.subList(from, to).clear();
	}

	public InternalNode<E> getParent(){
		return parent;
	}

	public String toString(){
		return Arrays.toString(values.toArray());
	}

	public boolean isFull(){
		return values.size() >= degree-1;
	}

	public void setParent(InternalNode<E> p){
		parent = p;
	}


	//TODO: Remove, this is just for debugging
	public ArrayList<E> getValues(){
		return values;
	}
}

