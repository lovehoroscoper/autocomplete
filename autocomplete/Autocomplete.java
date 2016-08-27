package autocomplete;

import java.io.*;
import java.util.*;


public class Autocomplete {
    
    public static void main(String[] args) {
        String[] terms = null;
        double[] weights = null;

        //file name is an input parameter
        File file = new File(args[0]);

        //read in the database
        try {
            Scanner in = new Scanner(file);

            int numberOfTerms = in.nextInt();
            terms = new String[numberOfTerms];
            weights = new double[numberOfTerms];

            /* Example Format of Input:
    		 * 4234 --> Net Size
    		 * 267323 Toronto --> Weight, followed by term
             */
            for (int i = 0; i < numberOfTerms; i++) {
                weights[i] = in.nextDouble();
                terms[i] = in.nextLine();
            }

        } catch (FileNotFoundException e) {
            System.out.println("Could not find file.");
        }

        //two autocomplete implementations
        TrieAutocomplete b = new TrieAutocomplete(terms, weights);
        //BruteAutocomplete b = new BruteAutocomplete(terms, weights);
        
        System.out.println("Read file! Enter search.");
        Scanner in = new Scanner(System.in);

        while (in.hasNext()) {
            String input = in.nextLine();
            
            //quit
            if (input.equals("quit")) {
                break;
            }

            String results[] = b.topMatches("\t" + input, 10);

            if (results != null){
                for (String indResult : results) {
                    System.out.println(indResult);
                }
            }
        }
    }
}
