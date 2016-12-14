import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

/**
 * Build a B+ tree from a structured file and outputs the results to a specified file
 * @author Evan Carlin
 */
public class BuildBPlusTree {
	public static void main(String[] args){
		boolean promptUser = true;

		while(promptUser){
			//Get user input and output files
			Scanner input = new Scanner(System.in);
			System.out.print("Input file path: ");
			File inputFile = new File(input.nextLine());
			System.out.print("Output file path: ");
			File outputFP = new File(input.nextLine());

			try {
				input = new Scanner(inputFile);
				int degree = Integer.parseInt(input.nextLine());
				System.out.println(degree);


			}catch(FileNotFoundException e){
				System.out.println("The input file could not be found");
			}







			//TODO: take in whether or not user wants to continue
			promptUser = false;
		}




//		final int DEGREE = 3;
//		BPlusTree<Integer> tree = new BPlusTree<Integer>(DEGREE);
//		System.out.println(tree);
// 		tree.insertValue(8);
//		tree.insertValue(5);
//		tree.insertValue(1);
//		System.out.println(tree);
//		tree.insertValue(7);
//		System.out.println(tree);
//		tree.insertValue(3);
//		tree.insertValue(12);
//		System.out.println(tree);
//		tree.insertValue(9);
//		System.out.println(tree.search(56));
//		System.out.println(tree.search(8));
//		System.out.println(tree);
//		tree.insertValue(6);
//		System.out.println(tree);
//		tree.insertValue(13);
//		tree.insertValue(14);
//		tree.insertValue(15);
//
//
//		System.out.println("\n");
//		System.out.println(tree);

	}
}
