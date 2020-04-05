public class ArrayDeque<T> {

    private T[] arr;
    private int size, first, last, RFACTOR;

    public ArrayDeque() {
        arr = (T []) new Object[8];
        size = 0;
        first = 4;
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
            return arr.length - 1;
        } else {
            return indexToMove - 1;
        }
    }

    private int moveNext(int indexToMove) {
        if (indexToMove == arr.length - 1) {
            return 0;
        } else {
            return indexToMove + 1;
        }
    }

    private int trueArrIndex(int index) {
        if (first + index >= arr.length) {
            return first + index - arr.length;
        } else {
            return first + index;
        }
    }

    public T get(int index) {
        if (size == 0 || index > size - 1) {
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
        if (size == arr.length) {
            resize(arr.length * RFACTOR);
        }
        first = movePrev(first);
        arr[first] = item;
        size++;
    }

    public void addLast(T item) {
        if (size == arr.length) {
            resize(arr.length * RFACTOR);
        }
        last = moveNext(last);
        arr[last] = item;
        size++;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            if (arr.length >= 16 && size * 4 <= arr.length) {
                resize(arr.length / 2);
            }
            T value = arr[first];
            arr[first] = null;
            first = moveNext(first);
            size--;
            return value;
        }
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        } else {
            if (arr.length >= 16 && size * 4 <= arr.length) {
                resize(arr.length / 2);
            }
            T value = arr[last];
            arr[last] = null;
            last = movePrev(last);
            size--;
            return value;
        }
    }

    private T[] extract() {
        // extracts the deque from Array straightened and without extra elements
        // Ex. [4 5 null null 1 2 3] -> [1 2 3 4 5]
        T[] extracted = (T []) new Object[size];
        if (size == 0) {
            return null;
        } else if (size == 1) {
            extracted[0] = arr[first];
        } else if (first < last) {
            System.arraycopy(arr, first, extracted, 0, size);
            // tested at https://tinyurl.com/yx7wtycc (Java Visualizer)
        } else if (first > last) {
            System.arraycopy(arr, first, extracted, 0, arr.length - first);
            System.arraycopy(arr, 0, extracted, arr.length - first, last + 1);
            // tested at https://tinyurl.com/uu8cc8q (Java Visualizer)
        }
        return extracted;
    }

    private void resize(int newSize) {
        T[] resized = (T []) new Object[newSize];
        T[] extracted = extract();
        first = newSize / 4;
        last = first + size - 1;
        System.arraycopy(extracted, 0, resized, first, size);
        arr = resized;
    } // tested at https://tinyurl.com/vqco8af (Java Visualizer)

    /** Local testing
    public static void main(String[] args) {
        ArrayDeque<Integer> test = new ArrayDeque<>();
        System.out.print("Testing isEmpty(): \nExpect true - ");
        System.out.println(test.isEmpty() + "\n");

        System.out.print("Testing get(): \nExpect null - ");
        System.out.print(test.get(0) + "\n");

        for (Integer i = 0; i < 11; i++) {
            test.addLast(i);
        }
        System.out.print("Testing addLast(): \nExpect 0 1 2 3 4 5 6 7 8 9 10 - ");
        test.printDeque();
        System.out.println();

        for (Integer i = 1; i < 11; i++) {
            test.addFirst(i);
        }
        System.out.print("Testing addFirst(): \n" +
            "Expect 10 9 8 7 6 5 4 3 2 1 0 1 2 3 4 5 6 7 8 9 10 - ");
        test.printDeque();
        System.out.println();

        System.out.print("Testing get(3): \nExpect 7 - ");
        System.out.print(test.get(3) + "\n");

        for (Integer i = 0; i < 15; i++) {
            test.removeFirst();
        }
        System.out.print("Testing removeFirst(): \nExpect 5 6 7 8 9 10 - ");
        test.printDeque();
        System.out.println();

        System.out.print("Testing get(): \nExpect null - ");
        System.out.print(test.get(10) + "\n");

        for (Integer i = 0; i < 5; i++) {
            test.removeLast();
        }
        System.out.print("Testing removeLast(): \nExpect 5 - ");
        test.printDeque();
        System.out.println();
    } */
}
