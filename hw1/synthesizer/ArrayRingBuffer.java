package synthesizer;
// import synthesizer.AbstractBoundedQueue;

import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        //       Create new array with capacity elements.
        //       first, last, and fillCount should all be set to 0.
        //       this.capacity should be set appropriately. Note that the local variable
        //       here shadows the field we inherit from AbstractBoundedQueue, so
        //       you'll need to use this.capacity to set the capacity.

        rb = (T[]) new Object[capacity];

        first = 0;
        last = 0;
        fillCount = 0;
        this.capacity = capacity;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        // Enqueue the item. Don't forget to increase fillCount and update last.

        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }

        rb[last] = x;

        if (last == (rb.length - 1)) {
            last = 0;
        } else {
            last += 1;
        }

        fillCount += 1;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        // Dequeue the first item. Don't forget to decrease fillCount and update

        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }

        T firstItem = rb[first];
        rb[first] = null;

        if (first == (rb.length - 1)) {
            first = 0;
        } else {
            first += 1;
        }

        fillCount -= 1;

        return firstItem;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        // Return the first item. None of your instance variables should change.
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        return rb[first];
    }

    // When you get to part 5, implement the needed code to support iteration.

    public Iterator<T> iterator() {
        return new ArrayRingBufferIterator();
    }

    private class ArrayRingBufferIterator implements Iterator {

        private int iterIndex = first;

        public boolean hasNext() {
            return !isEmpty() && iterIndex != last;
        }

        public T next() {
            T returnItem = rb[iterIndex];
            if (iterIndex == rb.length - 1) {
                iterIndex = 0;
            } else {
                iterIndex += 1;
            }
            return returnItem;
        }

    }
}
