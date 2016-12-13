import java.util.*;


public class BuildBPlusTree {
	public static void main(String[] args){
		final int DEGREE = 3;
		BPlusTree<Integer> tree = new BPlusTree<Integer>(DEGREE);
		tree.printTree();
 		tree.insertValue(8);
		tree.insertValue(5);
		tree.insertValue(1);
		tree.printTree();
		tree.insertValue(7);
		tree.printTree();
		tree.insertValue(3);
		tree.insertValue(12);
		tree.printTree();
		tree.insertValue(9);
		System.out.println(tree.search(56));
		System.out.println(tree.search(8));
		tree.printTree();
		tree.insertValue(6);
		tree.printTree();
		tree.insertValue(13);
		tree.insertValue(14);
		tree.insertValue(15);

//
		System.out.println("\n");
		tree.printTree();

	}
}
