package algorithms;

import com.example.metrics.PerformanceTracker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoyerMooreMajorityVoteTest {

    @Test
    void testEmptyArray() {
        Integer res = com.example.algorithms.BoyerMooreMajority.majorityElement(new int[]{}, null);
        assertNull(res);
    }

    @Test
    void testSingleElement() {
        Integer res = com.example.algorithms.BoyerMooreMajority.majorityElement(new int[]{5}, null);
        assertEquals(5, res);
    }

    @Test
    void testAllEqual() {
        int[] a = new int[10];
        for (int i = 0; i < a.length; i++) a[i] = 7;
        Integer res = com.example.algorithms.BoyerMooreMajority.majorityElement(a, null);
        assertEquals(7, res);
    }

    @Test
    void testMajorityPresent() {
        int[] a = {2,2,1,2,3,2,2};
        Integer res = com.example.algorithms.BoyerMooreMajority.majorityElement(a, new PerformanceTracker());
        assertEquals(2, res);
    }

    @Test
    void testNoMajority() {
        int[] a = {1,2,3,4,5,6};
        Integer res = com.example.algorithms.BoyerMooreMajority.majorityElement(a, null);
        assertNull(res);
    }

    @Test
    void testMetricsCount() {
        int[] a = {2,2,1,2,3,2,2};
        PerformanceTracker m = new PerformanceTracker();
        m.start();
        Integer res = com.example.algorithms.BoyerMooreMajority.majorityElement(a, m);
        m.stop();
        assertEquals(2, res);
        assertTrue(m.getArrayAccesses() >= a.length); // at least n accesses across two passes
        assertTrue(m.getComparisons() >= 0);
    }
}