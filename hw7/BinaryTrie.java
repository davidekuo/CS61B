import java.io.Serializable;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.HashMap;

public class BinaryTrie implements Serializable {

    private Node root;
    protected Map<Character, BitSequence> lookupTable;

    private class Node implements Comparable<Node>, Serializable {
        private int frequency;
        private Node left, right;
        private char ch;
        private boolean exists;

        Node(char c, int f) {
            this.frequency = f;
            this.ch = c;
            this.exists = true;
        }

        Node(Node left, Node right) {
            this.frequency = left.frequency + right.frequency;
            this.left = left;
            this.right = right;
            this.exists = false;
        }

        /*  Required for Comparable - returns:
            - negative int if this node has lower frequency than other node
            - 0 if this node and other node have the same frequency
            - positive int if this node has higher frequency than other node
         */
        public int compareTo(Node other) {
            if (other == null) {
                throw new NullPointerException();
            }
            return this.frequency - other.frequency;
        }
    }

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        PriorityQueue<Node> frequencyPQ = new PriorityQueue<>();
        frequencyTable.forEach((c, i) -> frequencyPQ.add(new Node(c, i)));
        // head of PQ is the least element

        while (frequencyPQ.size() >= 2) {
            /*  For the purposes of this homework,
                the “less frequent” branch of your Huffman coding trie
                should always be the ‘0’ side, and the more common side
                should always be the ‘1’ side.
             */
            Node left = frequencyPQ.remove();
            Node right = frequencyPQ.remove();

            frequencyPQ.add(new Node(left, right));
        }

        // at this point should only have 1 node in frequencyPQ
        root = frequencyPQ.remove();

    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        BitSequence longestSequence = new BitSequence();
        Character symbol = null;
        Node ptr = root;

        for (int i = 0; i < querySequence.length(); i++) {
            int b = querySequence.bitAt(i);

            if (b == 0) {
                ptr = ptr.left;
            } else { // b == 1
                ptr = ptr.right;
            }

            if (ptr == null) { // fell off decoding trie
                break;
            }
            if (ptr.exists) {
                longestSequence = querySequence.firstNBits(i + 1);
                symbol = ptr.ch;
            }
        }

        return new Match(longestSequence, symbol);
    }

    public Map<Character, BitSequence> buildLookupTable() {
        lookupTable = new HashMap<>();
        buildLookupTableHelper(root, new BitSequence());
        return lookupTable;
    }

    private void buildLookupTableHelper(Node n, BitSequence s) {
        if (n == null) {
            return;
        }

        if (n.exists) {
            lookupTable.put(n.ch, s);
        }

        buildLookupTableHelper(n.left, s.appended(0));
        buildLookupTableHelper(n.right, s.appended(1));
    }

    public static void main(String[] args) {
        Map<Character, Integer> freqTable = new HashMap<Character, Integer>();
        freqTable.put('a', 1);
        freqTable.put('b', 2);
        freqTable.put('c', 4);
        freqTable.put('d', 5);
        freqTable.put('e', 6);
        BinaryTrie bt = new BinaryTrie(freqTable);

    }
}
