import java.util.ArrayList;
import java.util.Arrays;


public class BuildBPlusTree {
	public static void main(String[] args){
		final int DEGREE = 5;
//		BPlusTree<Integer> tree = new BPlusTree<Integer>(DEGREE);
//		System.out.println(tree);
//		tree.insert(3);
//		System.out.println(tree);
//		tree.insert(4);
//		tree.insert(1);
//		System.out.println(tree);
		Integer a =5;
		Integer b =4;
		System.out.println(a.compareTo(b) > 0);
		LeafNode<Integer> leaf = new LeafNode<>(4);
		leaf.insert(3);
		System.out.println(Arrays.toString(leaf.values.toArray()));
		leaf.insert(5);
		System.out.println(Arrays.toString(leaf.values.toArray()));
		leaf.insert(2);
		System.out.println(Arrays.toString(leaf.values.toArray()));
		leaf.insert(100);
		System.out.println(Arrays.toString(leaf.values.toArray()));
		leaf.insert(6);
		System.out.println(Arrays.toString(leaf.values.toArray()));


	}
}
