/**
 *  @author Josh Hug
 */
public class MazeDepthFirstPaths extends MazeExplorer {
    /* Inherits protected fields:
        protected int[] distTo;
        protected int[] edgeTo;
        protected boolean[] marked;
        protected Maze maze;
    */

    /** Source. */
    private int s;
    /** Target. */
    private int t;
    /** True iff target has been reached. */
    private boolean targetFound = false;

    /** Set up to find path through M from (SOURCEX, SOURCEY) to
     *  (TARGETX, TARGETY) by depth-first search. */
    public MazeDepthFirstPaths(Maze m,
                               int sourceX, int sourceY,
                               int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Perform depth-first search from V. */
    private void dfs(int v) {
        marked[v] = true;
        announce();

        if (v == t) {
            targetFound = true;
        }

        if (targetFound) {
            return;
        }

        for (int w : maze.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                announce();
                distTo[w] = distTo[v] + 1;
                dfs(w);
                if (targetFound) {
                    return;
                }
            }
        }
    }

    @Override
    public void solve() {
        dfs(s);
    }
}

