import java.io.FileNotFoundException;
import java.io.FileWriter;
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
			//Build the tree
			try {
				//Get user input and output files
				Scanner input = new Scanner(System.in);
				System.out.print("Input file path: ");
				File inputFile = new File(input.nextLine());
				System.out.print("Output file path: ");
				FileWriter outPutFW = new FileWriter(new File(input.nextLine()));
				input = new Scanner(inputFile);

				//First line of the file is the degree
				int degree = Integer.parseInt(input.nextLine());
				BPlusTree tree;
				try{
					tree = new BPlusTree(degree);
					System.out.println("Building the tree and printing to the output file...");

					while(input.hasNext()){ //Read the input file
						String line = input.nextLine();
						switch (line.substring(0,1)){
							case "p":
								outPutFW.write(tree.toString());
								break;

							case "i":
								tree.insertValue(Integer.parseInt(line.substring(2)));
								break;

							case "s":
								if(tree.search(Integer.parseInt(line.substring(2)))){
									outPutFW.write(line.substring(2)+" FOUND\n");
								}
								else{
									outPutFW.write(line.substring(2)+" NOT FOUND\n");
								}
								break;
						}
					}

					outPutFW.flush();
					outPutFW.close();



				}
				catch (IllegalArgumentException e){ //The degree <3
					System.out.println(e);
					System.out.println("Exiting...");
					System.exit(1);
				}
			}
			catch(Exception e){
				System.out.println("The input file could not be found");
				System.out.println("Exiting...");
				System.exit(1);
			}




// /Users/evan/Dev/classes/cs455/BPlusTree/input.txt


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
