package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int N;
    private int numOpenSites;

    private int[][] indexArray; // reference for converting array [row][col] to disjointSet index

    private boolean[][] openArray; // tracks which array elements have been opened
    // Hopefully more memory efficient than adding 3rd dimension to indexArray b/c boolean instead of int

    private WeightedQuickUnionUF fullSet; // disjoint set for tracking whether each element is full
    private WeightedQuickUnionUF percolateSet; // disjoint set for tracking whether percolation has occurred

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        this.N = N;
        numOpenSites = 0;

        fullSet = new WeightedQuickUnionUF(N * N + 1);
        // + 1 because index 0 will be a virtual "top" element which we will connect to each element in the top row
        // indices 1 ... N*N correspond to [row][col] in index array which will be initialized below
        // this allows us to implement isFull() with connected(index, 0)

        percolateSet = new WeightedQuickUnionUF(N * N + 2);
        // + 2 because index 0 will be a virtual "top" element to be connected to each open element in the top row
        // and index N * N + 1 will be a virtual "bottom" element to be connect to each open element in the bottom row
        // as above, indices 1 ... N*N correspond to [row][col] in index array which will be initialized below
        // this allows us to implement percolate() with connected(0, N * N + 1)

        // initialize reference array
        indexArray = new int[N][N];
        openArray = new boolean[N][N];
        int index = 1; // start at 1 because index 0 is reserved for virtual "top" element
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
            fullSet.union(indexArray[row][col], indexArray[row - 1][col]);
            percolateSet.union(indexArray[row][col], indexArray[row - 1][col]);
        }
        if (row + 1 < N && isOpen(row + 1, col)) {
            fullSet.union(indexArray[row][col], indexArray[row + 1][col]);
            percolateSet.union(indexArray[row][col], indexArray[row + 1][col]);
        }
        if (col - 1 >= 0 && isOpen(row, col - 1)) {
            fullSet.union(indexArray[row][col], indexArray[row][col - 1]);
            percolateSet.union(indexArray[row][col], indexArray[row][col - 1]);
        }
        if (col + 1 < N && isOpen(row, col + 1)) {
            fullSet.union(indexArray[row][col], indexArray[row][col + 1]);
            percolateSet.union(indexArray[row][col], indexArray[row][col + 1]);
        }

        openArray[row][col] = true;
        numOpenSites += 1;

        // if new open element is in the top row, connect/union to virtual "top" element (index 0)
        if (row == 0) {
            fullSet.union(indexArray[row][col], 0);
            percolateSet.union(indexArray[row][col], 0);
        }

        // if new open element is in the bottom row, connect/union to virtual "bottom" element (index N * N + 1)
        if (row == N - 1) {
            percolateSet.union(indexArray[row][col], N * N + 1);
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
        // a site is full iff it is connected to one of the top row elements
        // and by extension, the virtual "top" element (index 0)
        return fullSet.connected(indexArray[row][col], 0);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        // percolation only occurs if a top row element (connected to the virtual "top" element [index 0])
        // is connected to a bottom row element (connected to the virtual "bottom" element [index N * N + 1])
        return percolateSet.connected(0, N * N + 1);
    }

    // use for unit testing (not required)
    public static void main(String[] args) {

    }

}
