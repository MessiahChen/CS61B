import java.util.Arrays;

/**
 * A partition of a set of contiguous integers that allows (a) finding whether
 * two integers are in the same partition set and (b) replacing two partitions
 * with their union.  At any given time, for a structure partitioning
 * the integers 1-N, each partition is represented by a unique member of that
 * partition, called its representative.
 *
 * @author Mingjie Chen
 */
public class UnionFind {

    /**
     * A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */
    public UnionFind(int N) {
        // FIXME
        _parent = new int[N + 1];
        _car = new int[N + 1];

        for (int i = 0; i <= N; i++) {
            _parent[i] = i;
            _car[i] = 1;
        }
    }

    /**
     * Return the representative of the partition currently containing V.
     * Assumes V is contained in one of the partitions.
     */
    public int find(int v) {
        while (v != _parent[v]) {
            _parent[v] = _parent[_parent[v]];
            v = _parent[v];
        }
        return v;
    }

    /**
     * Return true iff U and V are in the same partition.
     */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /**
     * Union U and V into a single partition, returning its representative.
     */
    public int union(int u, int v) {
        int p1 = find(u);
        int p2 = find(v);

        if (p2 == p1) {
            return p1;
        }

        if (_car[p1] > _car[p2]) {
            _parent[p2] = p1;
            return p1;
        } else if (_car[p2] > _car[p1]) {
            _parent[p1] = p2;
            return p2;
        } else {
            _parent[p2] = p1;
            _car[p1]++;
            return p1;
        }
    }

    // FIXME
    int[] _parent;
    int[] _car;
}
