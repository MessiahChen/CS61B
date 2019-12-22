package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.util.ArrayList;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class.
 *
 * @author Mingjie Chen
 */
public class PermutationTest {

    /**
     * Testing time limit.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /**
     * Check that perm has an alphabet whose size is that of
     * FROMALPHA and TOALPHA and that maps each character of
     * FROMALPHA to the corresponding character of FROMALPHA, and
     * vice-versa. TESTID is used in error messages.
     */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                    ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testInvertChar() {
        Permutation p = new Permutation("(PNH) (ABDFIKLZYXW) (JC)", UPPER);
        assertEquals(p.invert('B'), 'A');
        assertEquals(p.invert('G'), 'G');
    }

    @Test
    public void testInvertInt() {
        Permutation p = new Permutation("(PNH) (ABDFIKLZYXW) (JC)", UPPER);
        assertEquals(p.invert(0), 22);
        assertEquals(p.invert(6), 6);
    }

    @Test
    public void testPermuteChar() {
        Permutation p = new Permutation("(PNH) (ABDFIKLZYXW) (JC)", UPPER);
        assertEquals(p.permute('H'), 'P');
        assertEquals(p.permute('G'), 'G');
        assertEquals(p.permute('J'), 'C');
    }

    @Test
    public void testPermuteInt() {
        Permutation p = new Permutation("(PNH) (ABDFIKLZYXW) (JC)", UPPER);
        assertEquals(p.permute(7), 15);
        assertEquals(p.permute(6), 6);
        assertEquals(p.permute(9), 2);
    }

    @Test
    public void checkSplitCycles() {
        String cycles0 = "(abc) (def) (hijk)";
        ArrayList<String> expected0 = new ArrayList<>();
        expected0.add("abc");
        expected0.add("def");
        expected0.add("hijk");
        assertArrayEquals(expected0.toArray(),
                Permutation.splitCycles(cycles0).toArray());

        String cycles1 = "";
        ArrayList<String> expected1 = new ArrayList<>();
        expected1.add("");
        assertArrayEquals(expected1.toArray(),
                Permutation.splitCycles(cycles1).toArray());

        String cycles2 = "(ABC) (DEF) (H)";
        ArrayList<String> expected2 = new ArrayList<>();
        expected2.add("ABC");
        expected2.add("DEF");
        expected2.add("H");
        assertArrayEquals(expected0.toArray(),
                Permutation.splitCycles(cycles0).toArray());
    }

    @Test
    public void checkDerangement() {
        String c0 = "(abc) (def) (hijk)";
        Permutation perm0 = new Permutation(c0, new Alphabet(UPPER_STRING));
        assertEquals(true, perm0.derangement());

        String c1 = "(abc) (def) (h)";
        Permutation perm1 = new Permutation(c1, new Alphabet(UPPER_STRING));
        assertEquals(false, perm1.derangement());
    }

}
