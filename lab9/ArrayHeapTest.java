import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArrayHeapTest {

    /**
     * Basic test of adding, checking, and removing two elements from a heap
     */
    @Test
    public void simpleTest() {
        ArrayHeap<String> pq = new ArrayHeap<>();
        pq.insert("Tab", 2);
        pq.insert("Lut", 1);
        assertEquals(2, pq.size());

        String first = pq.removeMin();
        assertEquals("Lut", first);
        assertEquals(1, pq.size());

        String second = pq.removeMin();
        assertEquals("Tab", second);
        assertEquals(0, pq.size());
    }

    @Test
    public void myTest() {
        ArrayHeap<String> universities = new ArrayHeap<>();
        universities.insert("UCB", 100);
        universities.insert("Stanford", 1);
        universities.insert("CalTech", 98);
        universities.insert("MIT", 97);
        assertEquals(4, universities.size());

        String first = universities.peek().item();
        assertEquals("Stanford",first);

        String removed = universities.removeMin();
        assertEquals("Stanford",removed);
        assertEquals(3,universities.size());

        String first0 = universities.peek().item();
        assertEquals("MIT",first0);

        universities.changePriority("MIT",99);
        String first1 = universities.peek().item();
        assertEquals("CalTech",first1);

    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArrayHeapTest.class));
    }
}
