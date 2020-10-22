package lab11.graphs;

import java.util.ArrayDeque;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private Maze maze;
    private ArrayDeque<Integer> fringe = new ArrayDeque<Integer>();

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
        // Initialize queue with starting vertex s & mark that vertex
        fringe.add(s);
        marked[s] = true;
        announce();

        if (s == t) {
            return;
        }

        // Repeat until queue is empty
        while (!fringe.isEmpty()) {

            // remove vertex v from queue
            // aka visiting vertex v
            int v = fringe.remove();

            if (v == t) {
                return;
            }

            // for each unmarked neighbor of v:
            for (int w : maze.adj(v)) {
                // mark, add to queue, set edgeTo & distTo
                    // marking upon addition to queue prevents
                    // multiple additions -> infinite loops
                if (!marked[w]) {
                    marked[w] = true;
                    fringe.add(w);
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    announce();
                }
            }
        }

        return;

    }


    @Override
    public void solve() {
        bfs();
    }
}

