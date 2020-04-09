/*
Exam Prep 3

1. Flatten
Write a method flatten that takes a 2D array x and returns a 1D array that 
contains all of the arrays in x concatenated together.
Ex. flatten({1, 2, 3}, {}, {7, 8}) -> {1, 2, 3, 7, 8}
*/

public static int[] flatten(int[][] x) {
    int totalLength = 0;

    for (int[] i : x) {
        totalLength += i.length;
    }

    int[] a = new int[totalLength];
    int aIndex = 0;

    for(int[] i : x) {
        java.lang.System.arraycopy(i, 0, a, aIndex, i.length);
        aIndex = aIndex + i.length;
    }

    return a;
}

public static void main(String[] args) {
    int[][] test = {
        {1, 2, 3},
        {},
        {7, 8}
    };

    int[] result = flatten(test);

    for(int i : result) {
        System.out.print(i + " ");
    }      
    System.out.println();
}

// Tested at https://tinyurl.com/rft2ex7

/*
2. Skippify
Given the IntList class from lecture & lab and 2 IntLists
    IntList A = IntList.list(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    IntList B = IntList.list(9, 8, 7, 6, 5, 4, 3, 2, 1);
Fill in the method skippify such that 
    A.skippify() -> A = (1, 3, 6, 10)
    B.skippify() -> B = (9, 7, 4)
*/

public void skippify() {
    IntList p = this;
    int n = 1;
    while (p != null) {
        IntList next = p.next;
        for (int i = 0; i < n; i += 1) {
            if (next == null) {
                break;
            }
            next = next.next;
        }
        p.rest = next;
        p = next;
        n += 1;
    }
}

/*
3. Remove Duplicates
Given a sorted linked list of items - remove duplicates.
Ex. given 1-2-2-2-3, mutate it (destructively) to become 1-2-3
*/

public static void removeDuplicates(IntList p) {
    if (p == null) {
        return;
    }

    IntList current = p.next;
    IntList previous = p;

    while (current != null) {
        if (current.first == previous.first) {
            previous.rest = current.rest;
        } else {
            previous = current;
        }
        current = current.rest;
    }
}

