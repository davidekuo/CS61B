import edu.princeton.cs.algs4.Queue;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable> Item getMin(
            Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }

    /** Returns a queue of queues that each contain one item from items. */
    private static <Item extends Comparable> Queue<Queue<Item>>
            makeSingleItemQueues(Queue<Item> items) {
        // Your code here!
        Queue<Queue<Item>> queueOfQueues = new Queue<>();
        for (Item i : items) {
            Queue<Item> singleItemQueue = new Queue<>();
            singleItemQueue.enqueue(i);
            queueOfQueues.enqueue(singleItemQueue);
        }
        return queueOfQueues;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and q2.  After
     * running this method, q1 and q2 will be empty, and all of their items will be in the
     * returned queue.
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      A Queue containing all of the q1 and q2 in sorted order, from least to
     *              greatest.
     *
     */
    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(
            Queue<Item> q1, Queue<Item> q2) {
        // Your code here!
        Queue<Item> mergedQueue = new Queue<>();
        while (!q1.isEmpty() || !q2.isEmpty()) {
            mergedQueue.enqueue(getMin(q1, q2));
        }
        return mergedQueue;
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> mergeSort(
            Queue<Item> items) {
        // Your code here!

        // divide
        if (items.size() <= 1) {
            return items; // if only 1 element, already sorted
        } else {
            // split in half
            Queue<Item> unsorted1 = new Queue<>();
            Queue<Item> unsorted2 = new Queue<>();
            for (int i = 0; i < items.size() / 2; i++) {
                unsorted1.enqueue(items.dequeue());
            }
            while (!items.isEmpty()) {
                unsorted2.enqueue(items.dequeue());
            }
            // sort each half
            Queue<Item> sorted1 = mergeSort(unsorted1);
            Queue<Item> sorted2 = mergeSort(unsorted2);
            // merge sorted Queues
            Queue<Item> sorted = mergeSortedQueues(sorted1, sorted2);
            // return sorted Queue
            return sorted;
        }
    }

    public static void main(String[] args) {
        Queue<String> apples = new Queue<String>();
        apples.enqueue("Fuji");
        apples.enqueue("Gala");
        apples.enqueue("Honey_crisp");
        apples.enqueue("Cosmic_crisp");
        apples.enqueue("Pink_lady");
        apples.enqueue("Envy");

        System.out.print("Before sort: ");
        for (String s : apples) {
            System.out.print(s + " ");
        }

        Queue<String> sortedApples = MergeSort.mergeSort(apples);

        System.out.print("\nAfter  sort: ");
        for (String s : sortedApples) {
            System.out.print(s + " ");
        }
    }
}
