package algorithms;

import metrics.PerformanceTracker;
import java.util.Objects;


public final class BoyerMooreMajority {

    private BoyerMooreMajority() { /* utility */ }


    public static Integer findCandidate(int[] a, PerformanceTracker m) {
        if (a == null || a.length == 0) return null;
        int count = 0;
        int candidate = 0;

        for (int i = 0; i < a.length; i++) {
            if (m != null) m.incArrayAccesses();
            int value = a[i];

            if (count == 0) {
                candidate = value;
                count = 1;
            } else {
                if (m != null) m.incComparisons(); // compare candidate == value
                if (candidate == value) {
                    count++;
                } else {
                    count--;
                }
            }
        }
        return candidate;
    }


    public static Integer majorityElement(int[] a, PerformanceTracker m) {
        if (a == null || a.length == 0) return null;

        Integer cand = findCandidate(a, m);
        if (cand == null) return null;

        long occurrences = 0;
        for (int i = 0; i < a.length; i++) {
            if (m != null) m.incArrayAccesses();
            if (m != null) m.incComparisons();
            if (a[i] == cand) occurrences++;
        }

        return (occurrences > (a.length / 2)) ? cand : null;
    }
}
