import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture pic;

    public SeamCarver(Picture picture) {
        // creates deep copy so that changes are nondestructive
        pic = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(pic);
    }

    // width of current picture
    public int width() {
        return pic.width();
    }

    // height of current picture
    public int height() {
        return pic.height();
    }

    // energy of pixel at column x and row y
    /* By convention, the indices x and y are integers
    between 0 and W − 1 and between 0 and H − 1 respectively.
    Throw a java.lang.IndexOutOfBoundsException
    if either x or y is outside its prescribed range. */

    public  double energy(int x, int y) {
        int width = width();
        int height = height();

        if (x < 0 || x >= width
            || y < 0 || y >= height) {
            throw new java.lang.IndexOutOfBoundsException("x: " +
                    + x + " y: " + y + " width:" + width +
                    " height: " + height);
        }

        double gradXSquared, dRx, dGx, dBx;
        int leftIndex, rightIndex;
        if (x == 0) {
            // wrap around to right edge (col width - 1)
            leftIndex = width - 1;
            rightIndex = x + 1;
        } else if (x == width - 1) {
            // wrap around to left edge (col 0)
            leftIndex = x - 1;
            rightIndex = 0;
        } else {
            leftIndex = x - 1;
            rightIndex = x + 1;
        }

        dRx = pic.get(leftIndex, y).getRed()
                - pic.get(rightIndex, y).getRed();
        dGx = pic.get(leftIndex, y).getGreen()
                - pic.get(rightIndex, y).getGreen();
        dBx = pic.get(leftIndex, y).getBlue()
                - pic.get(rightIndex, y).getBlue();

        gradXSquared = dRx * dRx + dGx * dGx + dBx * dBx;

        double gradYSquared, dRy, dGy, dBy;
        int topIndex, bottomIndex;
        if (y == 0) {
            // wrap around to bottom edge (row height - 1)
            topIndex = height - 1;
            bottomIndex = y + 1;
        } else if (y == height - 1) {
            // wrap around to top edge (row 0)
            topIndex = y - 1;
            bottomIndex = 0;
        } else {
            topIndex = y - 1;
            bottomIndex = y + 1;
        }
        dRy = pic.get(x, topIndex).getRed()
                - pic.get(x, bottomIndex).getRed();
        dGy = pic.get(x, topIndex).getGreen()
                - pic.get(x, bottomIndex).getGreen();
        dBy = pic.get(x, topIndex).getBlue()
                - pic.get(x, bottomIndex).getBlue();

        gradYSquared = dRy * dRy + dGy * dGy + dBy * dBy;

        return gradXSquared + gradYSquared;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

        Picture picCopy = new Picture(pic); // deep copy
        Picture picTranspose = new Picture(height(), width());
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                java.awt.Color color = pic.get(i, j);
                picTranspose.set(j, i, color);
            }
        }

        pic = picTranspose;
        int[] hSeam = findVerticalSeam();
        pic = picCopy;

        return hSeam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // stores minimum cost for path ending at (col i, row j)
        double[][] minCost = new double[width()][height()];
        // stores parent for minimum cost path to (i,j)
        int[][] parent = new int[width()][height()];

        for (int j = 0; j < height(); j++) {
            for (int i = 0; i < width(); i++) {
                if (j == 0) {
                    // no parents in top row (start of vSeam)
                    // so minimum cost = energy(i,j)
                    minCost[i][j] = energy(i, j);
                    parent[i][j] = -1;
                } else {
                    // for other rows, M(i,j) = e(i,j)
                    // + min(M(i−1,j−1),M(i,j−1),M(i+1,j−1))
                    // as long as indices are in bounds
                    double left = Double.MAX_VALUE;
                    double right = Double.MAX_VALUE;
                    double middle = minCost[i][j - 1];

                    if (i - 1 >= 0) {
                        left = minCost[i - 1][j - 1];
                    }
                    if (i + 1 <= width() - 1) {
                        right = minCost[i + 1][j - 1];
                    }

                    double minParentCost = Math.min(Math.min(left, middle), right);
                    minCost[i][j] = energy(i, j) + minParentCost;

                    // since we're here, add parent to parent array
                    if (minParentCost == left) {
                        parent[i][j] = i - 1;
                    } else if (minParentCost == middle) {
                        parent[i][j] = i;
                    } else { // minParentCost == right
                        parent[i][j] = i + 1;
                    }
                }
            }
        }

        // find index of the end (bottom row or height()-1)
        // of overall minimum total cost path
        double minValue = Double.MAX_VALUE;
        int minIndex = -1;
        for (int k = 0; k < width(); k++) {
            if (minCost[k][height() - 1] < minValue) {
                minValue = minCost[k][height() - 1];
                minIndex = k;
            }
        }

        int[] vSeam = new int[height()];
        vSeam[height() - 1] = minIndex;

        // then just follow parents for each node
        // of minimum total cost path to top row
        for (int j = height() - 1; j > 0; j--) {
            int nextParent = vSeam[j];
            vSeam[j - 1] = parent[nextParent][j];
        }

        return vSeam;
    }

    /* Throw a java.lang.IllegalArgumentException if removeVerticalSeam()
    or removeHorizontalSeam() is called with an array of the wrong length
    or if the array is not a valid seam
    (i.e., two consecutive entries differ by more than 1). */

    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam.length != width()) {
            throw new java.lang.IllegalArgumentException();
        }
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new java.lang.IllegalArgumentException();
            }
        }
        SeamRemover.removeHorizontalSeam(pic, seam);
    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        if (seam.length != height()) {
            throw new java.lang.IllegalArgumentException();
        }
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new java.lang.IllegalArgumentException();
            }
        }
        SeamRemover.removeVerticalSeam(pic, seam);
    }

}
