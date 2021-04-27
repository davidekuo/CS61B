package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    private ArrayHeap<Integer> fringe = new ArrayHeap<>();

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(v) - maze.toX(t)) + Math.abs(maze.toY(v) - maze.toY(t));
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex v. */
    private void astar(int v) {
        edgeTo[v] = v;
        distTo[v] = 0;
        marked[v] = true;
        announce();

        for (int i = 0; i < maze.V(); i += 1) {
            if (i != v) {
                fringe.insert(i, Double.MAX_VALUE);
            } else {
                fringe.insert(i, 0);
            }
        }

        while (fringe.peek() != null) {
            int vertex = fringe.removeMin();
            marked[vertex] = true;
            announce();

            if (vertex == t) {
                return;
            }

            /* Relax all edges pointing from vertex */
            for (int neighbor : maze.adj(vertex)) {
                int proposedDistToNeighbor = distTo[vertex] + 1;
                if (proposedDistToNeighbor < distTo[neighbor]) {
                    edgeTo[neighbor] = vertex;
                    distTo[neighbor] = proposedDistToNeighbor;
                    fringe.changePriority(neighbor, h(neighbor));
                }

            }
        }

    }

    @Override
    public void solve() {
        astar(s);
    }


}

