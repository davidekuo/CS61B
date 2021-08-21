import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HuffmanEncoder {

    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> frequencyTable = new HashMap<>();
        for (int i = 0; i < inputSymbols.length; i++) {
            char ch = inputSymbols[i];
            if (frequencyTable.containsKey(ch)) {
                int freq = frequencyTable.get(ch);
                frequencyTable.put(ch, freq + 1);
            } else {
                frequencyTable.put(ch, 1);
            }
        }
        return frequencyTable;
    }

    public static void main(String[] args) {
        char[] input = FileUtils.readFile(args[0]);
        Map<Character, Integer> frequencyTable = HuffmanEncoder.buildFrequencyTable(input);

        BinaryTrie bt = new BinaryTrie(frequencyTable);
        bt.buildLookupTable();

        ObjectWriter ow = new ObjectWriter(args[0] + ".huf");
        ow.writeObject(bt);

        Map<Character, BitSequence> lookupTable = bt.lookupTable;
        ArrayList<BitSequence> bitSequenceList = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            BitSequence bs = lookupTable.get(input[i]);
            bitSequenceList.add(bs);
        }

        BitSequence fullBitSequence = BitSequence.assemble(bitSequenceList);
        ow.writeObject(fullBitSequence);

    }
}
