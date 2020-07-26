package hw2;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Monte Carlo simulation to estimate percolation threshold of an N x N grid.
 * @author David Kuo
 */

public class PercolationStats {

    /** Stores percolation threshold for each independent experiment. */
    private double[] percolationThresholds;

    /** # of experiments to perform. */
    private int t;

    /** Coefficient for 95% confidence interval. */
    private static final double CI = 1.96;

    /** Measure percolation threshold T times.
     *  @param N - dimension of grid
     *  @param T - number of experiments to perform
     *  @param pf - method for constructing Percolation objects
     *  */
    public PercolationStats(int N, int T, PercolationFactory pf) {

        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        this.t = T;
        percolationThresholds = new double[t];

        double[] uniform = new double[N];
        for (int i = 0; i < N; i++) {
            uniform[i] = 1 / N;
        }

        pf = new PercolationFactory();
        for (int i = 0; i < t; i++) {
            Percolation p = pf.make(N);
            while (!p.percolates()) {
                int row = StdRandom.discrete(uniform);
                int col = StdRandom.discrete(uniform);
                p.open(row, col);
            }
            percolationThresholds[i] = p.numberOfOpenSites() / (N * N * 1.0);
        }

    }
    /** Calculate sample mean of percolation threshold.
     *  @return sample mean */
    public double mean() {
        return StdStats.mean(percolationThresholds);
    }

    /** Calculate sample standard deviation of percolation threshold.
     *  @return sample stdev */
    public double stddev() {
        return StdStats.stddev(percolationThresholds);
    }

    /** Calculate low endpoint of 95% confidence interval.
     *  @return low endpoint of 95% CI */
    public double confidenceLow() {
        return mean() - CI * stddev() / Math.sqrt(t);
    }

    /** Calculate high endpoint of 95% confidence interval.
     *  @return high endpoint of 95% CI */
    public double confidenceHigh() {
        return mean() + CI * stddev() / Math.sqrt(t);
    }
}
