/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */

import java.util.ArrayDeque;

public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {

        String[] sorted = asciis.clone();

        // find longest string length
        int maxStringLength = 0;
        for (String s : asciis) {
            int stringLength = s.length();
            if (stringLength > maxStringLength) {
                maxStringLength = stringLength;
            }
        }

        for (int d = maxStringLength - 1; d >= 0; d--) {
            sortHelperLSDBucket(sorted, d);
        }

        return sorted;
    }

    private static int findIndex(String s, int index) {
        int stringLastIndex = s.length() - 1;
        if (stringLastIndex < index) {
            return 0;
        } else {
            return (int) s.charAt(index);
        }
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        // Using traditional counting sort

        // gather counts for each ASCII character
        int[] counts = new int[256];
        for (int i = 0; i < asciis.length; i++) {
            counts[findIndex(asciis[i], index)]++;
        }

        /* based on counts, calculate starting position
            for strings corresponding to each character */
        int[] starts = new int[256];
        int pos = 0;
        for (int i = 0; i < starts.length; i++) {
            starts[i] = pos;
            pos += counts[i];
        }

        String[] sorted = new String[asciis.length];
        for (int i = 0; i < asciis.length; i++) {
            int asciiIndex = findIndex(asciis[i], index);
            int insertLocation = starts[asciiIndex];
            sorted[insertLocation] = asciis[i];
            starts[asciiIndex]++;
        }

        for (int i = 0; i < sorted.length; i++) {
            asciis[i] = sorted[i];
        }
    }

    private static void sortHelperLSDBucket(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        // Using bucket sort

        ArrayDeque<String>[] bins = new ArrayDeque[256];

        for (int i = 0; i < bins.length; i++) {
            bins[i] = new ArrayDeque<>();
        }

        for (int i = 0; i < asciis.length; i++) {
            int asciiIndex = findIndex(asciis[i], index);
            bins[asciiIndex].add(asciis[i]);
        }

        int ptr = 0;
        for (int i = 0; i < bins.length; i++) {
            while (!bins[i].isEmpty()) {
                asciis[ptr] = bins[i].remove();
                ptr++;
            }
        }
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args) {
        String[] unsorted = {"oh", "for", "a", "thousand", "tongues", "to", "sing"};
        String[] sorted = sort(unsorted);

        for (int i = 0; i < unsorted.length; i++) {
            System.out.print(unsorted[i] + " ");
        }

        System.out.println("");

        for (int i = 0; i < sorted.length; i++) {
            System.out.print(sorted[i] + " ");
        }
    }
}
