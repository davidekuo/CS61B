import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {

    static CharacterComparator offBy5 = new OffByN(5);

    @Test
    public void testOffByN1() {
        assertTrue(offBy5.equalChars('a', 'f'));
    }

    @Test
    public void testOffByN2() {
        assertTrue(offBy5.equalChars('f', 'a'));
    }

    @Test
    public void testOffByN3() {
        assertFalse(offBy5.equalChars('h', 'f'));
    }
}
