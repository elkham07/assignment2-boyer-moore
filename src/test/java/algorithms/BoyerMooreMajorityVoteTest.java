package algorithms;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoyerMooreMajorityTest {

    @Test
    void testMajorityElementExists() {
        int[] nums = {3, 3, 4, 2, 3, 3, 5, 3};
        assertEquals(3, BoyerMooreMajorityVote.findMajorityElement(nums));
    }

    @Test
    void testNoMajorityElement() {
        int[] nums = {1, 2, 3, 4, 5};
        assertEquals(-1, BoyerMooreMajorityVote.findMajorityElement(nums));
    }

    @Test
    void testAllSameElements() {
        int[] nums = {7, 7, 7, 7, 7};
        assertEquals(7, BoyerMooreMajorityVote.findMajorityElement(nums));
    }

    @Test
    void testEmptyArray() {
        int[] nums = {};
        assertEquals(-1, BoyerMooreMajorityVote.findMajorityElement(nums));
    }
}
