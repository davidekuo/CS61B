package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;
import java.util.ArrayDeque;

public class Solver {

    private class SearchNode {

        WorldState state;
        int moves;
        int distToGoal;
        SearchNode prev;

        SearchNode(WorldState s, int m, SearchNode p) {
            state = s;
            moves = m;
            prev = p;
            distToGoal = s.estimatedDistanceToGoal();
        }
    }

    private class SearchNodeComparator implements Comparator<SearchNode> {
        public int compare(SearchNode s1, SearchNode s2) {
            int h1 = s1.moves + s1.distToGoal;
            int h2 = s2.moves + s2.distToGoal;

            if (h1 < h2) {
                return -1;
            } else if (h1 > h2) {
                return 1;
            } else {
                return 0;+18083923932
            }
        }
    }

    private MinPQ<SearchNode> pq = new MinPQ(new SearchNodeComparator());
    private int solutionMoves;
    private ArrayDeque<WorldState> solutionSequence;
    private int totalItemsEnqueued = 0;

    public Solver(WorldState initial) {
        /* Constructor which solves the puzzle, computing everything necessary for moves()
        and solution() to not have to solve the problem again. Solves puzzle using A*.
        Assumes solution exists.
         */

        pq.insert(new SearchNode(initial, 0, null));

        while (!pq.isEmpty()) {
            SearchNode currentNode = pq.delMin();
            if (currentNode.state.isGoal()) {
                solutionMoves = currentNode.moves;
                solutionSequence = new ArrayDeque<>();
                SearchNode pointer = currentNode;
                while (pointer != null) {
                    solutionSequence.addFirst(pointer.state);
                    pointer = pointer.prev;
                }
                break;
            } else {
                for (WorldState neighbor : currentNode.state.neighbors()) {
                    if (currentNode.prev == null) {
                        pq.insert(new SearchNode(neighbor, currentNode.moves + 1, currentNode));
                        totalItemsEnqueued++;

                    } else if (!neighbor.equals(currentNode.prev.state)) {
                        pq.insert(new SearchNode(neighbor, currentNode.moves + 1, currentNode));
                        totalItemsEnqueued++;
                    }
                }
            }
        }

    }

    public int moves() {
        // Return minimum number of moves to solve the puzzle starting at the initial WorldState
        return solutionMoves;
    }

    public Iterable<WorldState> solution() {
        // Returns a sequence of WorldStates from the initial WorldState to the solution
        return solutionSequence;
    }

}
