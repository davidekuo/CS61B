/** CS61B 2018 Discussion 3: More Practice with Linked Lists
	Exercises 1.1-1.3
*/

public class SLList {
	
	private class IntNode {
		public int item;
		public IntNode next;
		public IntNode(int item, IntNode next) {
			this.item = item;
			this.next = next;
		}
	}

	private IntNode first;

	public SLList(int x) {
		first = new IntNode(x, null);
	}

	public void addFirst(int x) {
		first = new IntNode(x, first);
	}

	public void print() {
		if (first == null) {
			System.out.println("null");
		} 
		else {
			IntNode current = first;
			while(current != null) {
				System.out.print(current.item + " ");
				current = current.next;
			}
			System.out.println();
		}
		
	}

	/** Exercise 1.1
		Implement SLList.insert which takes in an integer x and inserts it at the given
		position. If the position is after the end of the list, insert the new node at the end.
		For example, if SLList is 5 -> 6 -> 2, insert (10, 1) results in 5 -> 10 -> 6 -> 2.
	*/
	public void insert(int item, int position) {

		/** My attempt
		IntNode previous = null;
		IntNode current = first;
		int location = 0;
		while (current.next != null && location < position) {
			previous = current;
			current = current.next;
			location += 1;
		}
		if (current.next == null)  {
			current.next = new IntNode(item, null);
		}
		else if (location == position) {
			current = new IntNode(item, current);
			previous.next = current;
		}
		*/

		// Discussion solution
		if(position == 0 || first == null) {
			addFirst(item);
			return;
		}
		IntNode current = first;
		while (position > 1 && current.next != null) { 
		// position > 1 means while loop stops at IntNode BEFORE insertion
		// ex. if inserting at pos 1, stops while at position 0
			position--;
			current = current.next;
		}
		IntNode newNode = new IntNode(item, current.next);
		current.next = newNode;
	}


	/** Exercise 1.2
		Add another method to the SLList class that reverses the elements. 
		Do this using the existing IntNode objects (you should not use new)
	*/
	public void reverse() {
		if (first == null || first.next == null) {
			return;
		}
		
		/** My failed attempt
		IntNode prevNode = first;
		IntNode current = first.next;
		IntNode nextNode = current.next;
		while (current != null) {
			current.next = prevNode;
			prevNode = current;
			current = nextNode;
			nextNode = current.next; // only difference was order of operations
		}
		current.next = prevNode;
		first = current;
		*/

		IntNode current = first.next;
		first.next = null;
		while(current != null) {
			IntNode nextNode = current.next;
			current.next = first;
			first = current;
			current = nextNode;
		}
	}

	/** Exercise 1.3
		If you wrote reverse iteratively, write a second version that uses recursion
		(you may need a helper method).
	*/
	private IntNode recursive_reverse_helper(IntNode prev, IntNode head) {
		if (head.next == null) {
			head.next = prev;
			return head;
		} else {
			IntNode newHead = recursive_reverse_helper(head, head.next);
			head.next = prev;
			return newHead;
		}
	}

	public void recursive_reverse() {
		if (first == null || first.next == null) {
			return;
		} else {
			IntNode nextNode = first.next;
			first.next = null;
			first = recursive_reverse_helper(first, nextNode);
		}

	}
	

	public static void main(String[] args) {
		SLList testList = new SLList(4);
		testList.addFirst(3);
		testList.addFirst(1);
		testList.print();

		testList.insert(2, 1);
		testList.print();

		testList.insert(5,7);
		testList.print();

		testList.reverse();
		testList.print();

		testList.recursive_reverse();
		testList.print();

	}
}