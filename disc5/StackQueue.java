import java.util.Stack;

public class StackQueue<T> {

    private Stack<T> front;
    private Stack<T> back;

    public StackQueue() {
        front = new Stack<>();
        back = new Stack<>();
    }

    public void push(T item) {
        front.push(item);
    }

    public T pop() {
        while (!front.empty()) {
            back.push(front.pop());
        }

        T returnItem = back.pop();

        while (!back.empty()) {
            front.push(back.pop());
        }

        return returnItem;
    }

    public static void main(String[] args) {
        StackQueue<Integer> q = new StackQueue<>();
        for (int i = 0; i < 10; i++) {
            q.push(i);
        }
        for (int i = 0; i < 5; i++) {
            System.out.print(q.pop() + " ");
        }
    }
}
