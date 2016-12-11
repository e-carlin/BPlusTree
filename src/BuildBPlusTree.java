import java.util.Arrays;


public class BuildBPlusTree {
	public static void main(String[] args){
		final int DEGREE = 5;
		BPlusTree<Integer> tree = new BPlusTree<Integer>(DEGREE);
		System.out.println(tree);
		tree.insertValue(3);
		System.out.println(tree);
		tree.insertValue(4);
		tree.insertValue(1);
		System.out.println(tree);

	}
}
