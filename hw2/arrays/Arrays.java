package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/**
 * HW #2
 */

import com.sun.tools.corba.se.idl.Util;

/**
 * Array utilities.
 *
 * @author
 */
class Arrays {
    /* C. */

    /**
     * Returns a new array consisting of the elements of A followed by the
     * the elements of B.
     */
    static int[] catenate(int[] A, int[] B) {
        /* *Replace this body with the solution. */
        int n = A.length + B.length;
        int[] res = new int[n];
        for (int i = 0; i < A.length; i++) {
            res[i] = A[i];
        }
        for (int i = A.length; i < n; i++) {
            res[i] = B[i - A.length];
        }
        return res;
    }

    /**
     * Returns the array formed by removing LEN items from A,
     * beginning with item #START.
     */
    static int[] remove(int[] A, int start, int len) {
        /* *Replace this body with the solution. */
        int[] res = new int[A.length - len];
        for (int i = 0; i < start; i++) {
            res[i] = A[i];
        }
        for (int i = start; i < A.length - len; i++) {
            res[i] = A[i+len];
        }
        return res;
    }

    /* E. */

    /**
     * Returns the array of arrays formed by breaking up A into
     * maximal ascending lists, without reordering.
     * For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     * returns the three-element array
     * {{1, 3, 7}, {5}, {4, 6, 9, 10}}.
     */
    static int[][] naturalRuns(int[] A) {
        /* *Replace this body with the solution. */
        int n = 1;
        for (int i = 1;i<A.length;i++){
            if(A[i]<=A[i-1])
                n++;
        }
        int[][] res = new int[n][];
        int index = 0;
        int cnt = 0;
        for(int i = 1;i<A.length;i++){
            if(A[i]<=A[i-1]){
                res[cnt] = Utils.subarray(A,index,i-index);
                index = i;
                cnt++;
            }
        }
        res[cnt] = Utils.subarray(A,index,A.length-index);
        return res;
    }
}
