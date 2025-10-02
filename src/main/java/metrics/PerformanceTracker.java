package metrics;

public class PerformanceTracker {
    private long startTime;
    private long endTime;
    private long startMemory;
    private long endMemory;

    public void start() {
        startTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // очистка перед замером
        startMemory = runtime.totalMemory() - runtime.freeMemory();
    }

    public void stop() {
        endTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        endMemory = runtime.totalMemory() - runtime.freeMemory();
    }

    public long getExecutionTime() {
        return (endTime - startTime) / 1_000_000;
    }

    public long getMemoryUsed() {
        return (endMemory - startMemory) / 1024; // в КБ
    }
}
