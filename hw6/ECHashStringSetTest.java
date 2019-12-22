import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Mingjie Chen
 */
public class ECHashStringSetTest  {
    // FIXME: Add your own tests for your ECHashStringSetTest

    @Test
    public void myTest() {
        // FIXME: Delete this function and add your own tests
        ech.put("c");
        assertEquals(ech.contains("c"),true);

        ech.put("d");
        assertEquals(ech.contains("d"),true);

        ArrayList<String> tmp = new ArrayList();
        tmp.add("d");
        tmp.add("c");
        assertEquals(tmp,ech.asList());

        ech.put("a");
        ArrayList<String> tmp0 = new ArrayList();
        tmp0.add("d");
        tmp0.add("a");
        tmp0.add("c");
        assertEquals(tmp0,ech.asList());

        ech.put("v");
        ech.put("A");
        ech.put("r");
        ArrayList<String> tmp1 = new ArrayList();
        tmp1.add("d");
        tmp1.add("A");
        tmp1.add("a");
        tmp1.add("v");
        tmp1.add("c");
        tmp1.add("r");
        assertEquals(tmp1,ech.asList());

        ech.put("I LOVE 61B");
        ArrayList<String> tmp2 = new ArrayList();
        tmp2.add("d");
        tmp2.add("A");
        tmp2.add("a");
        tmp2.add("I LOVE 61B");
        tmp2.add("v");
        tmp2.add("c");
        tmp2.add("r");
        assertEquals(tmp2,ech.asList());
        assertEquals(true,ech.contains("I LOVE 61B"));

        for(int i = 0;i<10000;i++){
            ech.put((Integer.toString(i)));
        }
        assertEquals(true,ech.contains("9999"));
    }
    private ECHashStringSet ech = new ECHashStringSet();
}
