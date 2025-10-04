package cli;

import algorithms.BoyerMooreMajority;
import metrics.PerformanceTracker;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;


public class BenchmarkRunner {

    private static final int[] DEFAULT_SIZES = {100, 1000, 10_000, 100_000};
    private static final String[] DEFAULT_DISTS = {"random", "majority", "sorted", "reversed", "nearly-sorted", "all-equal", "all-negative"};
    private static final int DEFAULT_REPEATS = 5;

    public static void main(String[] args) throws IOException {
        int[] sizes = DEFAULT_SIZES;
        String[] dists = DEFAULT_DISTS;
        int repeats = DEFAULT_REPEATS;
        String outCsv = "bench_boyermoore.csv";

        // simple arg parsing: --repeats N --out file.csv
        for (int i = 0; i < args.length; i++) {
            if ("--repeats".equals(args[i]) && i + 1 < args.length) {
                repeats = Integer.parseInt(args[++i]);
            } else if ("--out".equals(args[i]) && i + 1 < args.length) {
                outCsv = args[++i];
            } else if ("--sizes".equals(args[i]) && i + 1 < args.length) {
                sizes = parseSizes(args[++i]);
            } else if ("--dists".equals(args[i]) && i + 1 < args.length) {
                dists = args[++i].split(",");
            }
        }

        PerformanceTracker.writeCsvHeader(outCsv);

        for (int n : sizes) {
            for (String dist : dists) {
                long[] times = new long[repeats];
                long[] comps = new long[repeats];
                long[] accesses = new long[repeats];
                long[] mems = new long[repeats];

                for (int r = 0; r < repeats; r++) {
                    int[] arr = generate(n, dist.trim());
                    PerformanceTracker m = new PerformanceTracker();
                    m.start();
                    Integer maj = BoyerMooreMajority.majorityElement(arr, m);
                    m.stop();

                    times[r] = m.getElapsedNs();
                    comps[r] = m.getComparisons();
                    accesses[r] = m.getArrayAccesses();
                    mems[r] = m.getMemoryDeltaBytes();

                    // small warmup between runs
                    try { Thread.sleep(10); } catch (InterruptedException ignored) {}
                }

                long medianTime = median(times);
                long medianComparisons = median(comps);
                long medianAccesses = median(accesses);
                long medianMem = median(mems);

                // create a synthetic tracker just to call appendCsv (we'll fill fields manually)
                PerformanceTracker out = new PerformanceTracker();
                // we cannot set private fields directly, so write special CSV row manually
                try (java.io.FileWriter fw = new java.io.FileWriter(outCsv, true)) {
                    fw.write(String.format("BoyerMoore,%d,%s,%d,%.3f,%d,%d,%d,%d\n",
                            n, dist, medianTime, medianTime / 1_000_000.0,
                            medianComparisons, medianAccesses, 0, medianMem));
                }
                System.out.printf("n=%d dist=%s median_time=%.3f ms comps=%d accesses=%d\n",
                        n, dist, medianTime / 1_000_000.0, medianComparisons, medianAccesses);
            }
        }

        System.out.println("Benchmark finished. CSV: bench_boyermoore.csv");
    }

    // --- helpers ---
    private static int[] parseSizes(String s) {
        String[] toks = s.split(",");
        int[] out = new int[toks.length];
        for (int i = 0; i < toks.length; i++) out[i] = Integer.parseInt(toks[i].trim());
        return out;
    }

    private static int[] generate(int n, String dist) {
        switch (dist.toLowerCase()) {
            case "random": return randomArray(n, -1000, 1000);
            case "sorted": return sortedArray(n);
            case "reversed": return reversedArray(n);
            case "nearly-sorted": return nearlySortedArray(n, Math.max(1, n / 100));
            case "majority": return majorityArray(n, 42);
            case "all-equal": return allEqualArray(n, 7);
            case "all-negative": return randomArray(n, -1000, -1);
            default: return randomArray(n, -1000, 1000);
        }
    }

    private static int[] randomArray(int n, int min, int max) {
        Random rnd = new Random(12345); // fixed seed for reproducibility
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = rnd.nextInt(max - min + 1) + min;
        return a;
    }

    private static int[] sortedArray(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        return a;
    }

    private static int[] reversedArray(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = n - i;
        return a;
    }

    private static int[] nearlySortedArray(int n, int swaps) {
        int[] a = sortedArray(n);
        Random rnd = new Random(123);
        for (int i = 0; i < swaps; i++) {
            int x = rnd.nextInt(n);
            int y = rnd.nextInt(n);
            int t = a[x]; a[x] = a[y]; a[y] = t;
        }
        return a;
    }

    private static int[] majorityArray(int n, int majorityValue) {
        int[] a = new int[n];
        Random rnd = new Random(1337);
        int majorityCount = n / 2 + 1;
        for (int i = 0; i < majorityCount; i++) a[i] = majorityValue;
        for (int i = majorityCount; i < n; i++) a[i] = rnd.nextInt(1000);
        // shuffle
        for (int i = 0; i < n; i++) {
            int j = rnd.nextInt(n);
            int t = a[i]; a[i] = a[j]; a[j] = t;
        }
        return a;
    }

    private static int[] allEqualArray(int n, int val) {
        int[] a = new int[n];
        Arrays.fill(a, val);
        return a;
    }

    private static long median(long[] arr) {
        long[] copy = Arrays.copyOf(arr, arr.length);
        Arrays.sort(copy);
        int mid = copy.length / 2;
        if (copy.length % 2 == 1) return copy[mid];
        return (copy[mid - 1] + copy[mid]) / 2;
    }
}