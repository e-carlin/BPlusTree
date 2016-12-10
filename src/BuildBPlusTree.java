
public class BuildBPlusTree {
	public static void main(String[] args){
		final int DEGREE = 5;
		BPlusTree<Integer> tree = new BPlusTree<Integer>(DEGREE);
		System.out.println(tree);
		tree.insert(3);
		System.out.println(tree);
		tree.insert(4);
		tree.insert(1);
		System.out.println(tree);
	}
}
