/* Discussion 6 Problem 2
    a. Exploit a flaw in this stack ADT implementation by filling in the main method
        so that it prints "Success" by causing BadIntegerStack to produce a NullPointerException
    b. Exploit another flaw in this stack ADT implementation by completing the main method
        so that the stack appears infinitely tall
    c. Fix these two flaws in the stack ADT implementation
*/
public class BadIntegerStack {

    public class Node {
        public Integer value;
        public Node prev;

        public Node(Integer v, Node p) {
            value = v;
            prev = p;
        }
    }

    public Node top;

    public boolean isEmpty() {
        return top == null;
    }

    public void push(Integer num) {
        top = new Node(num,top);
    }

    public Integer pop() {
        Integer ans = top.value;
        top = top.prev;
        return ans;
    }

    public Integer peek() {
        return top.value;
    }

    public static void partA() {
        try {
            BadIntegerStack b = new BadIntegerStack();
            System.out.println(b.pop());
        } catch (NullPointerException e) {
            System.out.println("Success");
        }
    }

    public static void partB() {
        BadIntegerStack b = new BadIntegerStack();
        b.push(0);
        b.top.prev = b.top; // set top Node's previous ptr to itself for infinite loop
        while(!b.isEmpty()) {
            System.out.print(b.pop() + " ");
        }
    }

    public static void main(String[] args) {
        // A.
        partA();

        // B.
        //partB();

        /*
        C.
            1. Have peek() and pop() methods check isEmpty() and throw exception
            2. make Node class and top (Node variable) private
        */

    }
}
