package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/**
 * HW #2, Problem #1.
 */

/** List problem.
 *  @author
 */
class Lists {
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    static IntListList naturalRuns(IntList L) {
        /* *Replace this body with the solution. */
        IntListList res= new IntListList();
        IntListList resTmp = res;
        while(L!=null) {
            IntList divList = new IntList(L.head, null);
            IntList tmp = divList;
            while (L.tail != null && L.tail.head > L.head) {
                tmp.head = L.head;
                tmp.tail = new IntList(L.tail.head, null);
                tmp = tmp.tail;
                L = L.tail;
            }
            L = L.tail;
            resTmp.head = divList;
            if(L!=null) {
                resTmp.tail = new IntListList(null, null);
                resTmp = resTmp.tail;
            }
        }
        return res;
    }
}
