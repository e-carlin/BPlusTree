import java.util.ArrayList;
import java.util.Arrays;


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

	public abstract ArrayList<Node<E>> getChildren();
	
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
	
	public int sizeOfValues(){
		return values.size();
	}
	
	public E getValue(int index){

		return values.get(index);
	}

	public void removeValue(int index) {

		values.remove(index);
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

