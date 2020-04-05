public class ArrayDeque<T> {

    private T[] arr;
    private int size, first, last, RFACTOR;

    public ArrayDeque() {
        arr = (T []) new Object[8];
        size = 0;
        first = 7;
        last = movePrev(first);
        RFACTOR = 2;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private int movePrev(int indexToMove) {
        if (indexToMove == 0) {
            return arr.length-1;
        } else {
            return indexToMove-1;
        }
    }

    private int moveNext(int indexToMove) {
        if(indexToMove == arr.length-1) {
            return 0;
        } else {
            return indexToMove+1;
        }
    }

    private int trueArrIndex(int index) {
        if(first + index >= arr.length) {
            return first + index - arr.length;
        } else {
            return first + index;
        }
    }

    public T get(int index) {
        if (size == 0 || index > size-1) {
            return null;
        } else {
            return arr[trueArrIndex(index)];
        }
    }

    public void printDeque() {
        int index = first;
        for (int i = 0; i < size; i++) {
            System.out.print(arr[index] + " ");
            index = moveNext(index);
        }
        System.out.println();
    }

    public void addFirst(T item) {
        first = movePrev(first);
        arr[first] = item;
        size++;
    }

    public void addLast(T item) {
        last = moveNext(last);
        arr[last] = item;
        size++;

    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            T value = arr[first];
            first = moveNext(first);
            size--;
            return value;
        }
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        } else {
            T value = arr[last];
            last = movePrev(last);
            size--;
            return value;
        }
    }

    public static void main(String[] args) {
        ArrayDeque<Integer> test = new ArrayDeque<>();
        System.out.print("Testing isEmpty(): \nExpect true - ");
        System.out.println(test.isEmpty() + "\n");

        System.out.print("Testing get(): \nExpect null - ");
        System.out.print(test.get(0) + "\n");

        test.addFirst(5);
        test.addLast(6);
        test.addFirst(4);
        test.addFirst(3);
        test.addFirst(2);
        test.addLast(7);
        test.addFirst(1);
        test.addFirst(0);
        System.out.print("Testing addFirst() & addLast(): \nExpect 0 1 2 3 4 5 6 7 - ");
        test.printDeque();
        System.out.println();

        System.out.print("Testing get(): \nExpect 3 - ");
        System.out.print(test.get(3) + "\n");

        test.removeLast();
        test.addFirst(1);
        test.removeLast();
        test.addFirst(2);
        test.removeLast();
        test.addFirst(3);
        test.removeLast();
        test.addFirst(4);
        test.addFirst(5);
        test.removeLast();
        test.removeLast();
        test.addFirst(6);
        test.removeLast();
        test.addFirst(7);
        System.out.print("Testing removeFirst() & removeLast(): \nExpect 7 6 5 4 3 2 1 0 - ");
        test.printDeque();
        System.out.println();

        test.removeFirst();
        test.removeLast();
        test.removeLast();
        test.removeFirst();
        System.out.print("Testing removeFirst() & removeLast(): \nExpect 5 4 3 2 - ");
        test.printDeque();
        System.out.println();

        System.out.print("Testing get(): \nExpect null - ");
        System.out.print(test.get(10) + "\n");
    }
}
