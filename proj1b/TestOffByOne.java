import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {

    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
    public void testEqualChars1() {
        assertTrue(offByOne.equalChars('a', 'b'));
    }

    @Test
    public void testEqualChars2() {
        assertTrue(offByOne.equalChars('r', 'q'));
    }

    @Test
    public void testEqualChars3() {
        assertTrue(offByOne.equalChars('%', '&'));
    }

    @Test
    public void testEqualChars4() {
        assertFalse(offByOne.equalChars('a', 'e'));
    }

    @Test
    public void testEqualChars5() {
        assertFalse(offByOne.equalChars('b', 'b'));
    }

    @Test
    public void testEqualChars6() {
        assertFalse(offByOne.equalChars('A', 'b'));
    }
}
