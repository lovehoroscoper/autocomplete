package autocomplete;
import java.util.Comparator;

public class TrieNode {
    char character;
    double weightOfWord = -1;
    double maxWeight = -1;
    String word;
    
    TrieNode left;
    TrieNode middle;
    TrieNode right;
    
    public TrieNode(char character, double maxWeight){
        this.character = character;
        this.maxWeight = maxWeight;
    }
    
    public static class WeightOrder implements Comparator<TrieNode> {
            public int compare(TrieNode v, TrieNode w) {
            //want maximum-oriented priority queue
            return (int) (w.maxWeight - v.maxWeight);
	}
    }
}
