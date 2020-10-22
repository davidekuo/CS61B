package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private final int s;
    private final int t;
    private final Maze maze;

    private boolean cycleDetected = false;
    private int cycle;
    private boolean cycleDrawn = false;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        s = 0;
        t = maze.V() - 1;

        distTo[s] = 0;
        edgeTo[s] = s;

    }

    @Override
    public void solve() {
        dfs(s, s);
    }

    // Helper methods go here
    private void dfs(int v, int parent) {

        marked[v] = true;
        announce();

        if (v == t) {
            return;
        }

        // For every visited vertex v
        for (int u : maze.adj(v)) {

            if (!cycleDetected) {
                // If there is an adjacent u that is already visited
                // AND u is not parent of v
                // there is a cycle in the graph
                if (marked[u] && u != parent) {
                    cycleDetected = true;
                    cycle = u;
                    edgeTo[u] = v;
                    edgeTo[v] = parent;
                    announce();
                    return;
                }

                if (!marked[u]) {
                    distTo[u] = u;
                    //edgeTo[u] = v;
                    dfs(u, v);
                }
            }


        }

        if (v == cycle) {
            cycleDrawn = true;
        }

        if (cycleDetected && !cycleDrawn) {
            edgeTo[v] = parent;
            announce();
        }
    }

}

