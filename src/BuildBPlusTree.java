import java.util.*;


public class BuildBPlusTree {
	public static void main(String[] args){
		final int DEGREE = 5;
		BPlusTree<Integer> tree = new BPlusTree<Integer>(DEGREE);
		tree.insertValue(3);
		tree.insertValue(4);
		tree.insertValue(1);
		tree.insertValue(5);
		tree.insertValue(88);
		tree.insertValue(89);
		tree.insertValue(90);

		System.out.println("\n");
		tree.printTree();


	}
}
