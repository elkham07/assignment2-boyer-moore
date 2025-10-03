package cli;

import algorithms.BoyerMooreMajorityVote;
import metrics.PerformanceTracker;
import java.util.Random;


public class BenchmarkRunner {
    public static void main(String[] args) {
        int size = 1000000;
        if (args.length > 0) {
            try {
                size = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input size, using default: " + size);
            }
        }

        int[] arr = generateArray(size);

        BoyerMooreMajorityVote algorithm = new BoyerMooreMajorityVote();
        PerformanceTracker tracker = new PerformanceTracker();

        tracker.start();
        int result = algorithm.findMajorityElement(arr);
        tracker.stop();

        System.out.println("Array size: " + size);
        System.out.println("Majority element: " + result);
        System.out.println("Execution time: " + tracker.getExecutionTime() + " ms");
        System.out.println("Memory used: " + tracker.getMemoryUsed() + " KB");
    }


    private static int[] generateArray(int size) {
        int[] arr = new int[size];
        Random rand = new Random();
        int majority = 42;

        for (int i = 0; i < size; i++) {
            if (i < size / 2 + 1) {
                arr[i] = majority;
            } else {
                arr[i] = rand.nextInt(100);
            }
        }
        return arr;
    }
}
