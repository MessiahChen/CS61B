package arrays;

import com.sun.xml.internal.xsom.impl.scd.Iterators;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * FIXME
 *
 * @author FIXME
 */

public class ArraysTest {
    /**
     * FIXME
     */
    @Test
    public void testCat() {
        int[] A = {1, 4, 7, 8};
        int[] B = {4, 6, 5, 9};
        int[] C = {1, 4, 7, 8, 4, 6, 5, 9};
        assertArrayEquals(C, Arrays.catenate(A, B));
    }

    @Test
    public void testRemove() {
        int[] A = {1, 4, 7, 8, 4, 6, 5, 9};
        int[] B = {1, 4, 7, 6, 5, 9};
        assertArrayEquals(B, Arrays.remove(A, 3, 2));
        int[] C = {1, 4, 7, 8, 4, 6, 5, 9};
        int[] D = {7, 8, 4, 6, 5, 9};
        assertArrayEquals(D, Arrays.remove(C, 0, 2));
    }

    @Test
    public void testNat(){
        int[] A = {1, 3, 7, 5, 4, 6, 9, 10};
        int[][] B = {{1, 3, 7}, {5}, {4, 6, 9, 10}};
        assertEquals(B, Arrays.naturalRuns(A));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
