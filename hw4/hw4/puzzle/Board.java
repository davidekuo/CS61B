package hw4.puzzle;
import java.util.ArrayList;

public class Board implements WorldState {

    private final int BLANK = 0;
    private final int[][] currentBoard;

    public Board(int[][] tiles) {
        currentBoard = copyBoard(tiles);
    }

    private int[][] copyBoard(int[][] board) {
        int[][] boardCopy = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                boardCopy[i][j] = board[i][j];
            }
        }

        return boardCopy;
    }

    public int tileAt(int i, int j) {
        // returns value of tile at row i, column j (or 0 if blank)
        if (i < 0 || i >= this.size()
                || j < 0 || j >= this.size()) {
            throw new java.lang.IndexOutOfBoundsException();
        } else {
            return currentBoard[i][j];
        }

    }

    public int size() {
        return currentBoard.length;
    }

    @Override
    public Iterable<WorldState> neighbors() {

        /*
         * Find BLANK
         * Create copy of Board
         * As allowed, move BLANK up/down/left/right, swapping with the number there
         */

        ArrayList<WorldState> neighbors = new ArrayList<>();

        // Find BLANK
        int size = size();
        int blankRow = -1;
        int blankCol = -1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tileAt(i, j) == BLANK) {
                    blankRow = i;
                    blankCol = j;
                    break;
                }
            }
        }


        // Move BLANK up
        if (blankRow > 0) {
            int[][] boardCopy = copyBoard(currentBoard);
            int swapTile = boardCopy[blankRow - 1][blankCol];
            boardCopy[blankRow - 1][blankCol] = BLANK;
            boardCopy[blankRow][blankCol] = swapTile;
            Board neighbor = new Board(boardCopy);
            neighbors.add(neighbor);
        }

        // Move BLANK down
        if (blankRow < size - 1) {
            int[][] boardCopy = copyBoard(currentBoard);
            int swapTile = boardCopy[blankRow + 1][blankCol];
            boardCopy[blankRow + 1][blankCol] = BLANK;
            boardCopy[blankRow][blankCol] = swapTile;
            Board neighbor = new Board(boardCopy);
            neighbors.add(neighbor);
        }

        // Move BLANK left
        if (blankCol > 0) {
            int[][] boardCopy = copyBoard(currentBoard);
            int swapTile = boardCopy[blankRow][blankCol - 1];
            boardCopy[blankRow][blankCol - 1] = BLANK;
            boardCopy[blankRow][blankCol] = swapTile;
            Board neighbor = new Board(boardCopy);
            neighbors.add(neighbor);
        }

        // Move BLANK right
        if (blankCol < size - 1) {
            int[][] boardCopy = copyBoard(currentBoard);
            int swapTile = boardCopy[blankRow][blankCol + 1];
            boardCopy[blankRow][blankCol + 1] = BLANK;
            boardCopy[blankRow][blankCol] = swapTile;
            Board neighbor = new Board(boardCopy);
            neighbors.add(neighbor);
        }

        return neighbors;
    }

    private int goalRow(int n) {
        int size = size();
        if (n == BLANK) {
            int returnvalue = size - 1;
            return returnvalue;
        } else {
            int returnvalue = (n - 1) / size;
            return returnvalue;
        }
    }

    private int goalCol(int n) {
        int size = size();
        if (n == BLANK) {
            int returnvalue = size - 1;
            return returnvalue;
        } else {
            int returnvalue = n - goalRow(n) * size - 1;
            return returnvalue;
        }
    }

    public int hamming() {
        int hammingDistance = 0;

        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                int tile = currentBoard[i][j];
                if (tile != BLANK) {
                    if (i != goalRow(tile) || j != goalCol(tile)) {
                        hammingDistance++;
                    }
                }
            }
        }
        return hammingDistance;
    }

    public int manhattan() {
        int manhattanDistance = 0;

        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                int tile = currentBoard[i][j];
                if (tile != BLANK) {
                    int additionalError = Math.abs(goalRow(tile) - i) + Math.abs(goalCol(tile) - j);
                    manhattanDistance += additionalError;
                }

            }
        }
        return manhattanDistance;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (getClass() != y.getClass()) {
            return false;
        }

        Board otherBoard = (Board) y;

        if (otherBoard.size() != this.size()) {
            return false;
        }

        for (int i = 0; i < otherBoard.size(); i++) {
            for (int j = 0; j < otherBoard.size(); j++) {
                if (this.currentBoard[i][j] != otherBoard.currentBoard[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int sum = 0;
        for (int i = 0; i < currentBoard.length; i++) {
            int product = 1;
            for (int j = 0; j < currentBoard.length; j++) {
                product *= currentBoard[i][j];
            }
            sum += product;
        }
        return sum;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
