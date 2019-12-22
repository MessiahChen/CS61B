import java.util.LinkedList;

/**
 * @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits visible fields:
    protected int[] distTo;
    protected int[] edgeTo;
    protected boolean[] marked;
    */

    /**
     * Source.
     */
    private int s;
    /**
     * Target.
     */
    private int t;
    /**
     * True iff target has been reached.
     */
    private boolean targetFound = false;

    /**
     * A breadth-first search of paths in M from (SOURCEX, SOURCEY) to
     * (TARGETX, TARGETY).
     */
    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY,
                                 int targetX, int targetY) {
        super(m);
        // Add more variables here!
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /**
     * Conducts a breadth first search of the maze starting at the source.
     */
    private void bfs(int s) {
        // TODO: Your code here. Don't forget to update distTo, edgeTo,
        // and marked, as well as call announce()

        LinkedList<Integer> q = new LinkedList<>();

        for (int i = 0; i < maze.V(); i++) {
            distTo[i] = (int) Float.POSITIVE_INFINITY;
        }
        distTo[s] = 0;
        marked[s] = true;
        announce();
        q.add(s);

        while (!q.isEmpty() && !targetFound) {
            int v = q.poll();
            if (v == t) {
                targetFound = true;
                return;
            }
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    announce();
                    q.add(w);
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs(s);
    }
}

