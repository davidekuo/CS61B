public class Palindrome {

    public Deque<Character> wordToDeque(String word) {
        Deque<Character> dq = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i += 1) {
            dq.addLast(word.charAt(i));
        }
        return dq;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> w2dq = wordToDeque(word);
        return isPalindromeHelper(w2dq);
    }

    private boolean isPalindromeHelper(Deque<Character> dq) {
        if (dq.size() <= 1) {
            return true;
        } else if (! dq.removeFirst().equals(dq.removeLast()) ) {
            return false;
        } else {
            return isPalindromeHelper(dq);
        }
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> w2dq = wordToDeque(word);
        return isPalindromeHelperCC(w2dq, cc);
    }

    private boolean isPalindromeHelperCC(Deque<Character> dq, CharacterComparator cc) {
        if (dq.size() <= 1) {
            return true;
        } else if (! cc.equalChars(dq.removeFirst(), dq.removeLast()) ) {
            return false;
        } else {
            return isPalindromeHelperCC(dq, cc);
        }
    }
}
