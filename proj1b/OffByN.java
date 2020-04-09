public class OffByN implements CharacterComparator {

    private int offset;

    public OffByN(int N) {
        offset = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return java.lang.Math.abs(x - y) == offset;
    }
}
