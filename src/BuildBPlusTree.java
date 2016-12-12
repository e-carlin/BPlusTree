import java.util.*;


public class BuildBPlusTree {
	public static void main(String[] args){
		final int DEGREE = 5;
		BPlusTree<Integer> tree = new BPlusTree<Integer>(DEGREE);
		tree.insertValue(30);
		tree.insertValue(40);
		tree.insertValue(10);
		tree.insertValue(50);
		tree.insertValue(880);
		tree.insertValue(890);
		tree.insertValue(1);
		tree.insertValue(2);
		tree.insertValue(3);
		tree.insertValue(31);
		tree.insertValue(32);
		tree.insertValue(4);
		tree.insertValue(5);
		tree.insertValue(6);
		tree.insertValue(891);
//
// tree.insertValue(8);
//		tree.insertValue(5);
//		tree.insertValue(1);
//		tree.insertValue(7);
//		tree.insertValue(3);
//		tree.insertValue(12);

		System.out.println("\n");
		tree.printTree();


	}
}
