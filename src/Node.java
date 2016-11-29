import java.util.ArrayList;


public abstract class Node<E extends Comparable<E>>{
	
	//TODO: make private
	public int degree;
	public ArrayList<E> values;
	
	public Node(int degree){
		degree = degree;
		values = new ArrayList<E>();
	}
	
	public abstract ArrayList<Node<E>> getChildren();
	
	public void insert(E elemToInsert){
		for(int i=0; i<values.size(); i++){
			//elemToInsert is bigger than this element so keep going
			if(values.get(i).compareTo(elemToInsert) <0){continue;}
	
			//elemToInsert is <= this element so insert it here
			values.add(i, elemToInsert);
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
}

