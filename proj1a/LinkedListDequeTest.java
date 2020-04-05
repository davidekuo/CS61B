/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

	/* Utility method for printing out empty checks. */
	public static boolean checkEmpty(boolean expected, boolean actual) {
		if (expected != actual) {
			System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	/* Utility method for printing out empty checks. */
	public static boolean checkSize(int expected, int actual) {
		if (expected != actual) {
			System.out.println("size() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	/* Prints a nice message based on whether a test passed. 
	 * The \n means newline. */
	public static void printTestStatus(boolean passed) {
		if (passed) {
			System.out.println("Test passed!\n");
		} else {
			System.out.println("Test failed!\n");
		}
	}

	/** Adds a few things to the list, checking isEmpty() and size() are correct, 
	  * finally printing the results. 
	  *
	  * && is the "and" operation. */
	public static void addIsEmptySizeTest() {
		System.out.println("Running add/isEmpty/Size test.");

		LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		boolean passed = checkEmpty(true, lld1.isEmpty());

		lld1.addFirst("front");
		
		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
		passed = checkSize(1, lld1.size()) && passed;
		passed = checkEmpty(false, lld1.isEmpty()) && passed;

		lld1.addLast("middle");
		passed = checkSize(2, lld1.size()) && passed;

		lld1.addLast("back");
		passed = checkSize(3, lld1.size()) && passed;

		System.out.println("Printing out deque: ");
		lld1.printDeque();

		printTestStatus(passed);

	}

	/** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
	public static void addRemoveTest() {

		System.out.println("Running add/remove test.");

		LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty 
		boolean passed = checkEmpty(true, lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty 
		passed = checkEmpty(false, lld1.isEmpty()) && passed;

		lld1.removeFirst();
		// should be empty 
		passed = checkEmpty(true, lld1.isEmpty()) && passed;

		printTestStatus(passed);

	}

	public static void getTest() {
		System.out.println("Running get() test.");

		LinkedListDeque<Integer> lldl = new LinkedListDeque<>();

		System.out.print("Test getRecursive(0): expect null - ");
		System.out.println(lldl.getRecursive(0)); // expect: null

		lldl.addLast(1);
		lldl.addLast(2);
		lldl.addLast(3);
		lldl.addLast(4);

		System.out.println("0 1 2 3");
		lldl.printDeque();

		System.out.print("Test getRecursive(5): expect null - ");
		System.out.println(lldl.getRecursive(5));
		System.out.print("Test getRecursive(2): expect 3 - ");
		System.out.println(lldl.getRecursive(2) + "\n");
	}

	public static void getRecursiveTest() {
		System.out.println("Running getRecursive() test.");

		LinkedListDeque<String> lldl = new LinkedListDeque<>();

		System.out.print("Test getRecursive(0): expect null - ");
		System.out.println(lldl.getRecursive(0)); // expect: null

		lldl.addLast("c");
		lldl.addFirst("b");
		lldl.addFirst("a");
		lldl.addLast("d");

		System.out.println("0 1 2 3");
		lldl.printDeque();

		System.out.print("Test getRecursive(5): expect null - ");
		System.out.println(lldl.getRecursive(5));
		System.out.print("Test getRecursive(2): expect c - ");
		System.out.println(lldl.getRecursive(2));
	}

	public static void main(String[] args) {
		System.out.println("Running tests.\n");
		addIsEmptySizeTest();
		addRemoveTest();
		getTest();
		getRecursiveTest();
	}
}
