import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    CharacterComparator obo = new OffByOne();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome1() {
        String test = "";
        assertTrue(palindrome.isPalindrome(test));
    }

    @Test
    public void testIsPalindrome2() {
        String test = "I";
        assertTrue(palindrome.isPalindrome(test));
    }

    @Test
    public void testIsPalindrome3() {
        String test = "oo";
        assertTrue(palindrome.isPalindrome(test));
    }

    @Test
    public void testIsPalindrome4() {
        String test = "ama";
        assertTrue(palindrome.isPalindrome(test));
    }

    @Test
    public void testIsPalindrome5() {
        String test = "Ama";
        assertFalse(palindrome.isPalindrome(test));
    }

    @Test
    public void testIsPalindromeOffByOne1() {
        String test = "noon";
        assertFalse(palindrome.isPalindrome(test, obo));
    }

    @Test
    public void testIsPalindromeOffByOne2() {
        String test = "racecar";
        assertFalse(palindrome.isPalindrome(test, obo));
    }

    @Test
    public void testIsPalindromeOffByOne3() {
        String test = "flake";
        assertTrue(palindrome.isPalindrome(test, obo));
    }

    @Test
    public void testIsPalindromeOffByOne4() {
        String test = "this";
        assertTrue(palindrome.isPalindrome(test, obo));
    }

    @Test
    public void testIsPalindromeOffByOne5() {
        String test = "This";
        assertFalse(palindrome.isPalindrome(test, obo));
    }
}
