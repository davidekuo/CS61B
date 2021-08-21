public class HuffmanDecoder {
    public static void main(String[] args) {
        ObjectReader or = new ObjectReader(args[0]);
        BinaryTrie bt = (BinaryTrie) or.readObject();
        BitSequence fullBitSequence = (BitSequence) or.readObject();
        BitSequence remainingSeq = new BitSequence(fullBitSequence);

        String output = "";
        while (remainingSeq.length() > 0) {
            Match prefix = bt.longestPrefixMatch(remainingSeq);
            output += prefix.getSymbol();
            int prefixLength = prefix.getSequence().length();
            remainingSeq = remainingSeq.allButFirstNBits(prefixLength);
        }

        char[] outputArr = output.toCharArray();
        FileUtils.writeCharArray(args[1], outputArr);
    }
}
