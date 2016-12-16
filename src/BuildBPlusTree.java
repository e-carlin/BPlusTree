import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.File;

/**
 * Build a B+ tree from a structured file and outputs the results to a specified file
 *
 * @author Evan Carlin
 */
public class BuildBPlusTree {
    public static void main(String[] args) {

        boolean promptUser = true;

        while (promptUser) {
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
                try {
                    tree = new BPlusTree(degree);
                    System.out.println("Building the tree and printing to the output file...");

                    while (input.hasNext()) { //Read the input file
                        String line = input.nextLine();
                        switch (line.substring(0, 1)) {
                            case "p":
                                outPutFW.write(tree.toString());
                                break;

                            case "i":
                                tree.insertValue(Integer.parseInt(line.substring(2)));
                                break;

                            case "s":
                                if (tree.search(Integer.parseInt(line.substring(2)))) {
                                    outPutFW.write(line.substring(2) + " FOUND\n");
                                } else {
                                    outPutFW.write(line.substring(2) + " NOT FOUND\n");
                                }
                                break;
                        }
                    }

                    outPutFW.flush();
                    outPutFW.close();


                } catch (IllegalArgumentException e) { //The degree <3
                    System.out.println(e);
                    System.out.println("Exiting...");
                    System.exit(1);
                }
            } catch (java.io.IOException e) {
                System.out.println("There was a problem with the given files.");
                System.out.println("Exiting...");
                System.exit(1);
            }


            Scanner input = new Scanner(System.in);
            System.out.print("Would you like to continue [Y or N]? ");
            String answer = input.nextLine();
            if (answer.toLowerCase().trim().equals("n")) {
                promptUser = false;
                System.out.println("Exiting...");
                System.exit(0);
            }
        }
    }
}
