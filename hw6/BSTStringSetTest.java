import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Mingjie Chen
 */
public class BSTStringSetTest  {
    // FIXME: Add your own tests for your BST StringSet

    @Test
    public void MyTest() {
        // FIXME: Delete this function and add your own tests
        bst.put("c");
        assertEquals(bst.contains("c"),true);

        bst.put("d");
        assertEquals(bst.contains("d"),true);

        ArrayList<String> tmp = new ArrayList();
        tmp.add("c");
        tmp.add("d");
        assertEquals(tmp,bst.asList());

        bst.put("a");
        ArrayList<String> tmp0 = new ArrayList();
        tmp0.add("a");
        tmp0.add("c");
        tmp0.add("d");
        assertEquals(tmp0,bst.asList());

        bst.put("v");
        bst.put("A");
        bst.put("r");
        ArrayList<String> tmp1 = new ArrayList();
        tmp1.add("A");
        tmp1.add("a");
        tmp1.add("c");
        tmp1.add("d");
        tmp1.add("r");
        tmp1.add("v");
        assertEquals(tmp1,bst.asList());

        bst.put("I LOVE 61B");
        ArrayList<String> tmp2 = new ArrayList();
        tmp2.add("A");
        tmp2.add("I LOVE 61B");
        tmp2.add("a");
        tmp2.add("c");
        tmp2.add("d");
        tmp2.add("r");
        tmp2.add("v");
        assertEquals(tmp2,bst.asList());
    }

    private BSTStringSet bst = new BSTStringSet();
}
