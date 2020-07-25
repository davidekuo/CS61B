package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.util.HashSet;

public class Percolation {

    private int N;
    private int numOpenSites;

    private int[][] indexArray; // reference for converting array [row][col] to disjointSet index

    private boolean[][] openArray; // tracks which array elements have been opened
    // Hopefully more memory efficient than adding 3rd dimension to indexArray b/c boolean instead of int

    private WeightedQuickUnionUF disjointSet;

    private HashSet<Integer> openTop; // tracks disjoint set parents of open elements in top row (row = 0)
    // any element with one of these as their parent is full b/c it is connected to an open top row element

    private HashSet<Integer> openBottom; // tracks disjoint set parents of open elements in bottom row (row = N - 1)
    // percolates() is true iff openBottom and openTop intersect i.e. an open top element and open bottom element
    // are in the same set and thus connected to one another


    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        this.N = N;
        numOpenSites = 0;
        openTop = new HashSet<>();
        openBottom = new HashSet<>();
        disjointSet = new WeightedQuickUnionUF(N*N);

        // initialize reference array
        indexArray = new int[N][N];
        openArray = new boolean[N][N];
        int index = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                indexArray[i][j] = index;
                index += 1;
                openArray[i][j] = false;
            }
        }
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new java.lang.IndexOutOfBoundsException();
        }

        // if element is already open, stop
        if (isOpen(row, col)) {
            return;
        }

        // connect/union new open element with its legal & open neighbors (avoid index out of bounds)
        if (row - 1 >= 0 && isOpen(row - 1, col)) {
            disjointSet.union(indexArray[row][col], indexArray[row - 1][col]);
        }
        if (row + 1 < N && isOpen(row + 1, col)) {
            disjointSet.union(indexArray[row][col], indexArray[row + 1][col]);
        }
        if (col - 1 >= 0 && isOpen(row, col - 1)) {
            disjointSet.union(indexArray[row][col], indexArray[row][col - 1]);
        }
        if (col + 1 < N && isOpen(row, col + 1)) {
            disjointSet.union(indexArray[row][col], indexArray[row][col + 1]);
        }

        // after new connect/union operations, update parents of open elements in top row as these may have changed
        // important to keep this up-to-date for isFull() and percolate() methods
        if (!openTop.isEmpty()) {
            int[] oldparents = openTop.stream().mapToInt(Integer::intValue).toArray();
            for (int i : oldparents) {
                openTop.add(disjointSet.find(i));
            }
        }

        openArray[row][col] = true;
        numOpenSites += 1;

        // if new open element is in the top row, add its disjoint set parent to openTop
        if (row == 0) {
            openTop.add(disjointSet.find(indexArray[row][col]));
        }

        // if new open element is in the bottom row, add its disjoint set parent to openBottom
        if (row == N - 1) {
            openBottom.add(disjointSet.find(indexArray[row][col]));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new java.lang.IndexOutOfBoundsException();
        }

        return openArray[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new java.lang.IndexOutOfBoundsException();
        }

        int parent = disjointSet.find(indexArray[row][col]);

        return openTop.contains(parent);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (!openBottom.isEmpty()) {
            for (int i : openBottom) {
                // check up-to-date parents of open elements in bottom row
                // against parents of open elements in top row
                if (openTop.contains(disjointSet.find(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    // use for unit testing (not required)
    public static void main(String[] args) {

    }

}
