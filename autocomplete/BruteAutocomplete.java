package autocomplete;

import java.util.LinkedList;

public class BruteAutocomplete {

    private Word list[];

    public BruteAutocomplete(String[] terms, double[] weights) {        
        list = new Word[terms.length];
        for (int i = 0; i < terms.length; i++){
            list[i] = new Word(terms[i], weights[i]);
        }
    }

    public double weightOf(String term) {
        for (int i = 0; i < list.length; i++) {
            if (list[i].text.equals(term)) {
                return list[i].weight;
            }
        }
        return -1;
    }

    public String topMatch(String prefix) {
        String topTerm = "";
        double topWeight = -1;

        for (Word word : list) {
            if (word.text.startsWith(prefix) && (topWeight < word.weight)) {
                topTerm = word.text;
                topWeight = word.weight;
            }
        }
        return topTerm;
    }

    public String[] topMatches(String prefix, int k) {
        LinkedList<Word> topMatches = new LinkedList<>();

        /*WCA: If every string was a prefix --> O(N*k). It is subqudratic.
         *This implementation serves as a base case. It's a very simple,
         *brute-force implmentation. Not much thought put into efficiency.
         */
        
        for (int termNumber = 0; termNumber < list.length; termNumber++) {
            if (list[termNumber].text.startsWith(prefix)) {
                
                if (topMatches.size() <= k) {
                    int i;
                    for (i = 0; i < topMatches.size(); i++)
                        if (topMatches.get(i).weight < list[termNumber].weight)
                            break;
                    topMatches.add(i, list[termNumber]);
                } 
                
                while (topMatches.size() > k)
                    topMatches.removeLast();
                }
            }

            String array[] = new String[topMatches.size()];
            for (int i = 0;i < topMatches.size(); i++)
                array[i] = topMatches.get(i).text;
            return array;
        }
    }