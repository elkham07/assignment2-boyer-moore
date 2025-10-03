package metrics;

import java.io.FileWriter;
import java.io.IOException;


public class PerformanceTracker {

    private long comparisons = 0;
    private long arrayAccesses = 0;
    private long swaps = 0;
    private long allocations = 0;


    private long startNs;
    private long endNs;
    private long startUsedBytes;
    private long endUsedBytes;



    public void start() {
        System.gc();
        startNs = System.nanoTime();
        Runtime rt = Runtime.getRuntime();
        startUsedBytes = rt.totalMemory() - rt.freeMemory();
    }

    public void stop() {
        endNs = System.nanoTime();
        Runtime rt = Runtime.getRuntime();
        endUsedBytes = rt.totalMemory() - rt.freeMemory();
    }



    public void incComparisons() { comparisons++; }
    public void incArrayAccesses() { arrayAccesses++; }
    public void incSwaps() { swaps++; }
    public void incAllocations(long n) { allocations += n; }



    public long getComparisons() { return comparisons; }
    public long getArrayAccesses() { return arrayAccesses; }
    public long getSwaps() { return swaps; }
    public long getAllocations() { return allocations; }
    public long getElapsedNs() { return endNs - startNs; }
    public double getElapsedMs() { return (endNs - startNs) / 1_000_000.0; }
    public long getMemoryDeltaBytes() { return endUsedBytes - startUsedBytes; }

    public void resetCounters() {
        comparisons = arrayAccesses = swaps = allocations = 0;
        startNs = endNs = startUsedBytes = endUsedBytes = 0;
    }



    public static void writeCsvHeader(String path) throws IOException {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write("algorithm,n,distribution,elapsed_ns,elapsed_ms,comparisons,array_accesses,swaps,memory_bytes\n");
        }
    }

    public void appendCsv(String path, String algorithm, int n, String distribution) throws IOException {
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(String.format("%s,%d,%s,%d,%.3f,%d,%d,%d,%d\n",
                    algorithm,
                    n,
                    distribution,
                    getElapsedNs(),
                    getElapsedMs(),
                    getComparisons(),
                    getArrayAccesses(),
                    getSwaps(),
                    getMemoryDeltaBytes()));
        }
    }
}
