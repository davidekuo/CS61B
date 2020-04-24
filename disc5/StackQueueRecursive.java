import java.util.Stack;

public class StackQueueRecursive<T> {

    private Stack<T> stack;

    public StackQueueRecursive() {
        stack = new Stack<>();
    }

    public void push(T item) {
        stack.push(item);
    }

    public T pop() {
        return pollHelper(stack);
    }

    private T pollHelper(Stack<T> s) {
        T returnItem;
        T popped = s.pop();

        if (s.empty()) {
            returnItem = popped;
        } else {
            returnItem = pollHelper(s);
            s.push(popped);
        }

        return returnItem;
    }

    public static void main(String[] args) {
        StackQueueRecursive<Integer> q = new StackQueueRecursive<>();
        for (int i = 0; i < 10; i++) {
            q.push(i);
        }
        for (int i = 0; i < 5; i++) {
            System.out.print(q.pop() + " ");
        }
    }
}
