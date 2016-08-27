package autocomplete;

import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Arrays;

public class TrieAutocomplete {

    private TrieNode root;

    public TrieAutocomplete(String[] terms, double[] weights) {
        //random insertion makes a better trie
        Integer randomInsertion[] = new Integer[terms.length];
        for (int i = 0; i < terms.length; i++) {
            randomInsertion[i] = i;
        }
        Collections.shuffle(Arrays.asList(randomInsertion));

        for (int i = 0; i < terms.length; i++) {
            add(terms[randomInsertion[i]], weights[randomInsertion[i]],
                    root, 0);
        }
    }

    private void add(String word, double weight, TrieNode root, int pos) {
        //empty tree
        if (root == null) {
            root = new TrieNode(word.charAt(pos), weight);
            this.root = root;
        }

        /* The idea is to find if the current char is already there. Traverse
         * left if char is smaller, right if larger (lexicographically). If a
         * null node is reached, make a new one. If it is indeed found, proceed
         * to its middle node and create any subsequent chars from the word. 
         * Ternary tries are great ways to do prefix searches. They prove to be
         * valuable with large data sets.
         */
        if (word.charAt(pos) < root.character) {
            if (root.maxWeight < weight) {
                root.maxWeight = weight;
            }

            if (root.left == null) {
                root.left = new TrieNode(word.charAt(pos), weight);
            }

            add(word, weight, root.left, pos);
        } else if (word.charAt(pos) > root.character) {
            if (root.maxWeight < weight) {
                root.maxWeight = weight;
            }

            if (root.right == null) {
                root.right = new TrieNode(word.charAt(pos), weight);
            }

            add(word, weight, root.right, pos);
        } else if (pos + 1 < word.length()) {
            pos++;

            if (root.maxWeight < weight) {
                root.maxWeight = weight;
            }

            if (root.middle == null) {
                root.middle = new TrieNode(word.charAt(pos), weight);
            }

            add(word, weight, root.middle, pos);
        } else {
            root.weightOfWord = weight;
            root.maxWeight = weight;
            root.word = word;
        }
    }

    public String[] topMatches(String prefix, int k) {

        TrieNode prefixLocation = prefixSearch(root, prefix, 0);
        PriorityQueue<TrieNode> nodeTraverser
                = new PriorityQueue<>(10 * k, new TrieNode.WeightOrder());
        LinkedList<Word> potentialList = new LinkedList<>();

        if (prefixLocation == null) {
            return null;
        }

        //if the prefix itself is a word
        if (prefixLocation.weightOfWord >= 0) {
            potentialList.add(new Word(prefixLocation.word,
                    prefixLocation.weightOfWord));
        }

        /* Look through the prefix subtree, with the help of a max priority 
         * queue. Stop once largest TrieNode weight <= kth largest node.
         */
        nodeTraverser.add(prefixLocation.middle);
        while (!nodeTraverser.isEmpty()) {

            TrieNode bestNode = nodeTraverser.poll();

            if (bestNode.left != null) {
                nodeTraverser.add(bestNode.left);
            }
            if (bestNode.middle != null) {
                nodeTraverser.add(bestNode.middle);
            }
            if (bestNode.right != null) {
                nodeTraverser.add(bestNode.right);
            }
            
            potentialListAdder:
            if (bestNode.weightOfWord >= 0) {
                
                //bypass known low-weight nodes
                if (potentialList.size() == k 
                        && potentialList.get(k-1).weight 
                        > bestNode.weightOfWord)
                    break potentialListAdder;

                int i = 0;
                for (i = 0; i < potentialList.size(); i++) {
                    if (potentialList.get(i).weight
                            < bestNode.weightOfWord) {
                        break;
                    }
                }

                potentialList.add(i, new Word(bestNode.word,
                        bestNode.weightOfWord));
                
                while (potentialList.size() > k) {
                    potentialList.removeLast();
                }
            }

            if (potentialList.size() >= k && nodeTraverser.peek().maxWeight
                    <= potentialList.get(k - 1).weight) {
                break;
            }
        }

        int constraint = k < potentialList.size() ? k : potentialList.size();
        String terms[] = new String[constraint];
        for (int i = 0; i < constraint; i++) {
            terms[i] = potentialList.get(i).text;
        }
        return terms;
    }

    private TrieNode prefixSearch(TrieNode root, String word, int pos) {
        if (root == null) {
            return null;
        }

        if (word.charAt(pos) < root.character) {
            return prefixSearch(root.left, word, pos);
        } else if (word.charAt(pos) > root.character) {
            return prefixSearch(root.right, word, pos);
        } else {
            pos++;
            if (pos < word.length()) {
                return prefixSearch(root.middle, word, pos);
            } else {
                return root;
            }
        }
    }
}
