
public class BPlusTree<E extends Comparable <E>>{
	
	public Node<E> root;
	public int degree;
	
	public BPlusTree(int degree){
		degree = degree;
		root = null;
	}
	
	public void insert(E value){
		//Tree is empty so we can just create a leaf node and insert the value
		if(this.isEmpty()){
			LeafNode<E> node = new LeafNode<E>(degree);
			root = node;
			root.insert(value);
			return;
		}
		
	}
	
	//TODO: complete
	private Node<E> findLeafFor(E value, Node<E> localRoot){
		//Either root is a leaf or we have reached a leaf so this node is where value should be placed
		if(localRoot instanceof LeafNode){
			return localRoot;
		}
		else{
			for(int i=0; i<localRoot.sizeOfValues(); i++){
				//Value should should go in leftmost subtree
				if(i==0 && value.compareTo(localRoot.getValue(i)) < 0){
					return findLeafFor(value, localRoot.getChildren().get(0));
				}
				//Value should go in one of internal pointers
				else if(i>0 && i<localRoot.sizeOfValues()-1){ //TODO: Test if it should really be -1
					
				}
			}
		}
	}
	
	private boolean rootIsLeaf(){
		return root instanceof LeafNode ? true : false;
		
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
