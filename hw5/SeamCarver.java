import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture pic;

    public SeamCarver(Picture picture) {
        // creates deep copy so that changes are nondestructive
        pic = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return pic;
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
        int width = pic.width();
        int height = pic.height();

        if (x < 0 || x >= width
            || y < 0 || y >= height) {
            throw new java.lang.IndexOutOfBoundsException();
        }

        // int r = (rgb >> 16) & 0xFF
        // int g = (rgb >>  8) & 0xFF
        // int b = (rgb >>  0) & 0xFF

        double gradXSquared, dRxSquared, dGxSquared, dBxSquared;
        if (x == 0) {
            // wrap around to right edge (col width - 1)
            dRxSquared = Math.pow(((pic.getRGB(width - 1, y) >> 16) & 0xFF)
                    - ((pic.getRGB(x + 1, y) >> 16) & 0xFF), 2);
            dGxSquared = Math.pow(((pic.getRGB(width - 1, y) >> 8) & 0xFF)
                    - ((pic.getRGB(x + 1, y) >> 8) & 0xFF), 2);
            dBxSquared = Math.pow(((pic.getRGB(width - 1, y) >> 0) & 0xFF)
                    - ((pic.getRGB(x + 1, y) >> 0) & 0xFF), 2);
        } else if (x == width - 1) {
            // wrap around to left edge (col 0)
            dRxSquared = Math.pow(((pic.getRGB(x - 1, y) >> 16) & 0xFF)
                    - ((pic.getRGB(0, y) >> 16) & 0xFF), 2);
            dGxSquared = Math.pow(((pic.getRGB(x - 1, y) >> 8) & 0xFF)
                    - ((pic.getRGB(0, y) >> 8) & 0xFF), 2);
            dBxSquared = Math.pow(((pic.getRGB(x - 1, y) >> 0) & 0xFF)
                    - ((pic.getRGB(0, y) >> 0) & 0xFF), 2);
        } else {
            dRxSquared = Math.pow(((pic.getRGB(x - 1, y) >> 16) & 0xFF)
                    - ((pic.getRGB(x + 1, y) >> 16) & 0xFF), 2);
            dGxSquared = Math.pow(((pic.getRGB(x - 1, y) >> 8) & 0xFF)
                    - ((pic.getRGB(x + 1, y) >> 8) & 0xFF), 2);
            dBxSquared = Math.pow(((pic.getRGB(x - 1, y) >> 0) & 0xFF)
                    - ((pic.getRGB(x + 1, y) >> 0) & 0xFF), 2);
        }

        gradXSquared = dRxSquared + dGxSquared + dBxSquared;

        double gradYSquared, dRySquared, dGySquared, dBySquared;
        if (y == 0) {
            // wrap around to bottom edge (row height - 1)
            dRySquared = Math.pow(((pic.getRGB(x, height - 1) >> 16) & 0xFF)
                    - ((pic.getRGB(x, y + 1) >> 16) & 0xFF), 2);
            dGySquared = Math.pow(((pic.getRGB(x, height - 1) >> 8) & 0xFF)
                    - ((pic.getRGB(x, y + 1) >> 8) & 0xFF), 2);
            dBySquared = Math.pow(((pic.getRGB(x, height - 1) >> 0) & 0xFF)
                    - ((pic.getRGB(x, y + 1) >> 0) & 0xFF), 2);
        } else if (y == height - 1) {
            // wrap around to top edge (row 0)
            dRySquared = Math.pow(((pic.getRGB(x, y - 1) >> 16) & 0xFF)
                    - ((pic.getRGB(x, 0) >> 16) & 0xFF), 2);
            dGySquared = Math.pow(((pic.getRGB(x, y - 1) >> 8) & 0xFF)
                    - ((pic.getRGB(x, 0) >> 8) & 0xFF), 2);
            dBySquared = Math.pow(((pic.getRGB(x, y - 1) >> 0) & 0xFF)
                    - ((pic.getRGB(x, 0) >> 0) & 0xFF), 2);
        } else {
            dRySquared = Math.pow(((pic.getRGB(x, y - 1) >> 16) & 0xFF)
                    - ((pic.getRGB(x, y + 1) >> 16) & 0xFF), 2);
            dGySquared = Math.pow(((pic.getRGB(x, y - 1) >> 8) & 0xFF)
                    - ((pic.getRGB(x, y + 1) >> 8) & 0xFF), 2);
            dBySquared = Math.pow(((pic.getRGB(x, y - 1) >> 0) & 0xFF)
                    - ((pic.getRGB(x, y + 1) >> 0) & 0xFF), 2);
        }

        gradYSquared = dRySquared + dGySquared + dBySquared;

        return gradXSquared + gradYSquared;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

        Picture picCopy = new Picture(pic); // deep copy
        Picture picTranspose = new Picture(height(), width());
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                int rgb = pic.getRGB(i, j);
                picTranspose.setRGB(j, i, rgb);
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
