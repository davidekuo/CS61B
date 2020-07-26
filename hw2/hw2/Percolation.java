package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Percolation data type modeling a percolation system.
 * @author David Kuo
 */
public class Percolation {

    /** size of percolation system (N x N grid). */
    private int n;

    /** # of open sites in percolation system. */
    private int numOpenSites;

    /** reference for converting array [row][col] to disjointSet index. */
    private int[][] index;

    /** tracks which array elements have been opened. */
    private boolean[][] open;

    /** disjoint set for tracking whether each element is full. */
    private WeightedQuickUnionUF full;

    /** disjoint set for tracking whether percolation has occurred. */
    private WeightedQuickUnionUF percolate;

    /**
     * Create N-by-N grid, with all sites initially blocked.
     * @param N - size of percolation system (N X N grid) */
    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        this.n = N;
        numOpenSites = 0;

        full = new WeightedQuickUnionUF(N * N + 1);
        percolate = new WeightedQuickUnionUF(N * N + 2);

        index = new int[N][N];
        open = new boolean[N][N];
        int x = 1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.index[i][j] = x;
                x += 1;
                open[i][j] = false;
            }
        }
    }

    /** Open the site (row, col) if it is not open already.
     *  @param row - row
     *  @param col - col */
    public void open(int row, int col) {
        if (row < 0 || row >= n || col < 0 || col >= n) {
            throw new java.lang.IndexOutOfBoundsException();
        }

        if (isOpen(row, col)) {
            return;
        }

        if (row - 1 >= 0 && isOpen(row - 1, col)) {
            full.union(index[row][col], index[row - 1][col]);
            percolate.union(index[row][col], index[row - 1][col]);
        }
        if (row + 1 < n && isOpen(row + 1, col)) {
            full.union(index[row][col], index[row + 1][col]);
            percolate.union(index[row][col], index[row + 1][col]);
        }
        if (col - 1 >= 0 && isOpen(row, col - 1)) {
            full.union(index[row][col], index[row][col - 1]);
            percolate.union(index[row][col], index[row][col - 1]);
        }
        if (col + 1 < n && isOpen(row, col + 1)) {
            full.union(index[row][col], index[row][col + 1]);
            percolate.union(index[row][col], index[row][col + 1]);
        }

        open[row][col] = true;
        numOpenSites += 1;

        if (row == 0) {
            full.union(index[row][col], 0);
            percolate.union(index[row][col], 0);
        }

        if (row == n - 1) {
            percolate.union(index[row][col], n * n + 1);
        }
    }

    /** Is the site (row,col) open?
     *  @param row - row
     *  @param col - col
     *  @return true if open, false if not */
    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= n || col < 0 || col >= n) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return open[row][col];
    }

    /** Is the site (row,col) full?
     *  @param row - row
     *  @param col - col
     *  @return true if open, false if not */
    public boolean isFull(int row, int col) {
        if (row < 0 || row >= n || col < 0 || col >= n) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return full.connected(index[row][col], 0);
    }

    /** Returns # of open sites.
     *  @return # of open sites */
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    /** Does the system percolate?
     *  @return true if so, false if not */
    public boolean percolates() {
        return percolate.connected(0, n * n + 1);
    }

    /** For unit testing, optional.
     * @param args - command line arguments */
    public static void main(String[] args) {

    }

}
