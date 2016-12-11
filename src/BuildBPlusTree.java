import java.util.Arrays;


public class BuildBPlusTree {
	public static void main(String[] args){
		final int DEGREE = 5;
//		BPlusTree<Integer> tree = new BPlusTree<Integer>(DEGREE);
//		System.out.println(tree);
//		tree.put(3);
//		System.out.println(tree);
//		tree.put(4);
//		tree.put(1);
//		System.out.println(tree);
		Integer a =5;
		Integer b =4;
		System.out.println(a.compareTo(b) > 0);
		LeafNode<Integer> leaf = new LeafNode<>(4);
		leaf.put(3);
		System.out.println(Arrays.toString(leaf.values.toArray()));
		leaf.put(5);
		System.out.println(Arrays.toString(leaf.values.toArray()));
		leaf.put(2);
		System.out.println(Arrays.toString(leaf.values.toArray()));
		leaf.put(100);
		System.out.println(Arrays.toString(leaf.values.toArray()));
		leaf.put(6);
		System.out.println(Arrays.toString(leaf.values.toArray()));


	}
}
