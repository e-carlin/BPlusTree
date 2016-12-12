import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Node<E extends Comparable<E>>{

	//TODO: make private
	private int degree;
	private ArrayList<E> values;
	private InternalNode<E> parent;
	
	public Node(int d){
		parent = null;
		degree = d;
		values = new ArrayList<>();
	}

	/**
	 * Inserts a value in sorted (ascending order)
	 * @param elemToInsert the element we wish to insert
	 * @return the index where the element was inserted
	 */
	public int putValue(E elemToInsert){
		for(int i=0; i<values.size(); i++){
			//elemToInsert is bigger than this element so keep going
			if(elemToInsert.compareTo(values.get(i)) > 0){
				continue;
			}
			//elemToInsert is <= this element so putValue it here
			values.add(i, elemToInsert);
			return i;
		}
		//All elements where < elemToInsert so append to end of list
		values.add(elemToInsert);
		return values.size()-1;
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

	public List<E> subListValues(int from, int to){

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



	//TODO: Remove, this is just for debugging, I don't think it is know probably need to keep
	public ArrayList<E> getValues(){
		return values;
	}
}

