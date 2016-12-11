import java.util.ArrayList;


public abstract class Node<E extends Comparable<E>>{
	
	//TODO: make private
	public int degree;
	public ArrayList<E> values;
	
	public Node(int d){
		degree = d;
		values = new ArrayList<E>();
	}
	
	public abstract ArrayList<Node<E>> getChildren();
	
	public void put(E elemToInsert){
		for(int i=0; i<values.size(); i++){
			//elemToInsert is bigger than this element so keep going
			if(elemToInsert.compareTo(values.get(i)) > 0){
				continue;
			}
			//elemToInsert is <= this element so put it here
			values.add(i, elemToInsert);
			return;
		}
		//All elements where < elemToInsert so append to end of list
		values.add(elemToInsert);
	}
	
	public int sizeOfValues(){
		return values.size();
	}
	
	public E getValue(int i){
		return values.get(i);
	}
	
	public String toString(){
		String toReturn = "";
		for(E elem : values){
			toReturn = toReturn +" "+elem;
		}
		
		return toReturn;
	}

	public boolean isFull(){
		return values.size() >= degree-1;
	}
}

