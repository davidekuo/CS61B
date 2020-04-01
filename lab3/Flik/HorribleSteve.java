public class HorribleSteve {
    public static void main(String [] args) {
        int i = 0;
        for (int j = 0; i < 500; ++i, ++j) {
            if (!Flik.isSameNumber(i, j)) {
                break; // break exits the for loop!
            }
        }
        System.out.println("i is " + i);
    }
}

/** The bug here is that Flik is comparing Integers (reference type) instead of ints (primitive type).
    As such, what is being compared are addresses in memory as opposed to integer values.
    When the addresses are different, even if integer values are the same, isSameNumber returns false.
    This can be fixed by changing
        public static boolean isSameNumber(Integer a, Integer b)
    to
        public static boolean isSameNumber(int a, int b)
 */