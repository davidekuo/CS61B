public class LinkedListDeque<T> {

    private class Node {
        private T item;
        private Node prev;
        private Node next;
        public Node(T item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node sentinel;
        // sentinel.next points to FIRST element of DLL
        // sentinel.prev points to LAST element of DLL
    private int size;

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }
    /* public LinkedListDeque(T item) {
        Node first = new Node(item, sentinel, sentinel);
        sentinel.next = first;
        sentinel.prev = first;
        size = 1;
    }*/

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addFirst(T item) {
        Node firstNode = sentinel.next;
        Node newFirstNode = new Node(item, sentinel, firstNode);
        sentinel.next = newFirstNode;
        firstNode.prev = newFirstNode;
        size++;
    }

    public void addLast(T item) {
        Node lastNode = sentinel.prev;
        Node newLastNode = new Node(item, lastNode, sentinel);
        sentinel.prev = newLastNode;
        lastNode.next = newLastNode;
        size++;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Node firstNode = sentinel.next;
        Node secondNode = firstNode.next;
        sentinel.next = secondNode;
            // sentinel.next (always points to first element of DLL) now points to 2nd node
        secondNode.prev = sentinel;
            // make sure 2nd node's prev pointer now points to sentinel
        size--;
        return firstNode.item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node lastNode = sentinel.prev;
        Node secondLastNode = lastNode.prev;
        sentinel.prev = secondLastNode;
        secondLastNode.next = sentinel;
        size--;
        return lastNode.item;
    }

    public T get(int index) {
        // iterative approach
        if (size == 0 || index > size - 1) {
            return null;
        }
        Node ptr = sentinel.next;
            //set pointer to first element of DLL
        for (int i = 0; i < index; i++) {
            ptr = ptr.next;
                // traverse DLL until you reach index element
        }
        return ptr.item;
    }

    private T getRecursiveHelper(Node p, int i) {
        Node ptr = p;
        if (i == 0) {
            return ptr.item;
        } else {
            return getRecursiveHelper(p.next, i - 1);
        }
    }

    public T getRecursive(int index) {
        // recursive approach
        if (size == 0 || index > size - 1) {
            return null;
        }else {
             return getRecursiveHelper(sentinel.next, index);
        }
    }

    public void printDeque() {
        Node ptr = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(ptr.item + " ");
            ptr = ptr.next;
        }
        System.out.println();
    }

}
