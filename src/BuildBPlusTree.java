import java.util.*;


public class BuildBPlusTree {
	public static void main(String[] args){
		final int DEGREE = 4;
		BPlusTree<Integer> tree = new BPlusTree<Integer>(DEGREE);
 		tree.insertValue(8);
		tree.insertValue(5);
		tree.insertValue(1);
		tree.insertValue(7);
		tree.insertValue(3);
		tree.insertValue(12);
		tree.insertValue(9);
		tree.insertValue(6);
		tree.insertValue(13);
		tree.insertValue(14);
		tree.insertValue(15);

//
		System.out.println("\n");
		tree.printTree();

	}
}
