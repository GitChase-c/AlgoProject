import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.IntSupplier;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<String> csvRows = new ArrayList<>();
    private static volatile long benchmarkSink;

    private static final int[] DIRECTED_SMALL_SIZES = {2, 10, 100, 1000};
    private static final int[] DIRECTED_LARGE_SIZES = {2000, 5000, 10000};
    private static final int[] UNDIRECTED_SIZES = {2, 10, 100, 1000};
    private static final int TRIALS = 7;

    public static void main(String[] args) {
        System.out.println("Choose algorithm:");
        System.out.println("0. Run all benchmarks");
        System.out.println("1. Floyd");
        System.out.println("2. BFS");
        System.out.println("3. Kahn");
        System.out.println("4. DFS");
        int algo = scanner.nextInt();

        csvRows.add("algorithm,graph_type,cyclic,requested_vertices,actual_vertices,correct,median_ns,median_us");

        if (algo == 0) {
            runAllBenchmarks();
        } else if (algo >= 1 && algo <= 4) {
            System.out.println("Choose graph type (Floyd uses a functional graph):");
            System.out.println("1. Directed (Small)");
            System.out.println("2. Directed (Large)");
            System.out.println("3. Undirected");
            int graphType = scanner.nextInt();

            System.out.print("Number of vertices: ");
            int vertices = scanner.nextInt();

            System.out.println("1. Cyclic");
            System.out.println("2. Acyclic");
            int cycleChoice = scanner.nextInt();

            if (graphType < 1 || graphType > 3 || (cycleChoice != 1 && cycleChoice != 2)) {
                System.out.println("Invalid graph type or cycle choice.");
                return;
            }
            testGraph(algo, graphType, vertices, cycleChoice == 1);
            saveCsv();
        } else {
            System.out.println("Invalid algorithm choice.");
        }
    }

    public static void runAllBenchmarks() {
        for (int algo = 1; algo <= 4; algo++) {
            for (int graphType = 1; graphType <= 3; graphType++) {
                int[] sizes = sizesForGraphType(graphType);
                for (int vertices : sizes) {
                    testGraph(algo, graphType, vertices, true);
                    testGraph(algo, graphType, vertices, false);
                }
            }
        }
        saveCsv();
    }

    public static void testGraph(int algo, int graphType, int vertices) {
        testGraph(algo, graphType, vertices, askCyclic());
    }

    private static boolean askCyclic() {
        System.out.println("1. Cyclic");
        System.out.println("2. Acyclic");
        int choice = scanner.nextInt();
        if (choice != 1 && choice != 2) {
            throw new IllegalArgumentException("Choose 1 or 2");
        }
        return choice == 1;
    }

    public static boolean runFloydGraph(int graphType, int vertices) {
        return runFloydGraph(graphType, vertices, askCyclic());
    }

    public static boolean runBFSGraph(int graphType, int vertices) {
        return runBFSGraph(graphType, vertices, askCyclic());
    }

    public static boolean runKahnGraph(int graphType, int vertices) {
        return runKahnGraph(graphType, vertices, askCyclic());
    }

    public static boolean runDFSGraph(int graphType, int vertices) {
        return runDFSGraph(graphType, vertices, askCyclic());
    }

    private static int[] sizesForGraphType(int graphType) {
        switch (graphType) {
            case 1: return DIRECTED_SMALL_SIZES;
            case 2: return DIRECTED_LARGE_SIZES;
            case 3: return UNDIRECTED_SIZES;
            default: throw new IllegalArgumentException("Unknown graph type: " + graphType);
        }
    }

    public static void testGraph(int algo, int graphType, int vertices, boolean cyclic) {
        switch (algo) {
            case 1: runFloydGraph(graphType, vertices, cyclic); break;
            case 2: runBFSGraph(graphType, vertices, cyclic); break;
            case 3: runKahnGraph(graphType, vertices, cyclic); break;
            case 4: runDFSGraph(graphType, vertices, cyclic); break;
            default: System.out.println("Invalid algorithm choice.");
        }
    }

    public static boolean runFloydGraph(int graphType, int vertices, boolean cyclic) {
        if (graphType == 3) {
            printUnsupported("Floyd", graphType);
            return false;
        }

        if (vertices < 1) {
            vertices = 1;
        }

        final int cycleStart = vertices / 2;
        FunctionalGraph graph = cyclic
                ? GraphGenerator.functionalWithCycle(vertices, cycleStart)
                : GraphGenerator.functionalPath(vertices);

        int expected = cyclic ? cycleStart : -1;
        int actual = report("Floyd", graphType, cyclic, vertices,
                graph.getVertexCount(), expected,
                () -> FloydCycleDetection.findCycleStart(graph, 0));

        return actual != -1;
    }

    public static boolean runBFSGraph(int graphType, int vertices, boolean cyclic) {
        if (graphType != 3) {
            printUnsupported("BFS", graphType);
            return false;
        }

        Graph graph = cyclic
                ? GraphGenerator.undirectedCyclic(vertices)
                : GraphGenerator.undirectedAcyclic(vertices);

        int actual = report("BFS", graphType, cyclic, vertices,
                graph.getVertexCount(), cyclic ? 1 : 0,
                () -> BFSCycleDetection.hasCycle(graph) ? 1 : 0);
        return actual == 1;
    }

    public static boolean runKahnGraph(int graphType, int vertices, boolean cyclic) {
        if (graphType == 3) {
            printUnsupported("Kahn", graphType);
            return false;
        }

        Graph graph;
        if (graphType == 1) {
            graph = cyclic ? GraphGenerator.directedCyclic(vertices)
                    : GraphGenerator.directedAcyclic(vertices);
        } else {
            graph = cyclic ? GraphGenerator.directedLCyclic(vertices)
                    : GraphGenerator.directedLAcyclic(vertices);
        }

        int actual = report("Kahn", graphType, cyclic, vertices,
                graph.getVertexCount(), cyclic ? 1 : 0,
                () -> KahnCycleDetection.hasCycle(graph) ? 1 : 0);
        return actual == 1;
    }

    public static boolean runDFSGraph(int graphType, int vertices, boolean cyclic) {
        Graph graph;
        if (graphType == 1) {
            graph = cyclic ? GraphGenerator.directedCyclic(vertices)
                    : GraphGenerator.directedAcyclic(vertices);
        } else if (graphType == 2) {
            graph = cyclic ? GraphGenerator.directedLCyclic(vertices)
                    : GraphGenerator.directedLAcyclic(vertices);
        } else {
            graph = cyclic ? GraphGenerator.undirectedCyclic(vertices)
                    : GraphGenerator.undirectedAcyclic(vertices);
        }

        int actual = report("DFS", graphType, cyclic, vertices,
                graph.getVertexCount(), cyclic ? 1 : 0,
                () -> DFSCycleDetection.hasCycle(graph) ? 1 : 0);
        return actual == 1;
    }

    private static int report(String algorithm, int graphType, boolean cyclic,
                              int requested, int actualVertices,
                              int expected, IntSupplier detector) {
        int actual = detector.getAsInt();
        boolean correct = actual == expected;
        double medianNs = benchmark(detector, actualVertices);

        String type = graphType == 1 ? "Directed Small"
                : graphType == 2 ? "Directed Large" : "Undirected";
        String cycleType = cyclic ? "Cyclic" : "Acyclic";

        System.out.printf("%-5s | %-14s | %-7s | requested=%-6d actual=%-6d | " +
                        "expected=%-3d actual=%-3d | %-4s | %.2f ns %n",
                algorithm, type, cycleType, requested, actualVertices,
                expected, actual, correct ? "PASS" : "FAIL", medianNs, medianNs / 1000.0);

        csvRows.add(String.format(java.util.Locale.US,
                "%s,%s,%s,%d,%d,%s,%.3f,%.6f",
                algorithm, type.replace(' ', '_'), cyclic, requested,
                actualVertices, correct, medianNs, medianNs / 1000.0));
        return actual;
    }

    private static double benchmark(IntSupplier detector, int vertices) {
        int repetitions = Math.max(100, Math.min(20_000, 1_000_000 / Math.max(1, vertices)));
        int warmups = Math.min(200, repetitions);
        long checksum = 0;

        for (int i = 0; i < warmups; i++) {
            checksum += detector.getAsInt();
        }
        benchmarkSink = checksum;

        double[] samples = new double[TRIALS];
        for (int trial = 0; trial < TRIALS; trial++) {
            checksum = 0;
            long start = System.nanoTime();
            for (int i = 0; i < repetitions; i++) {
                checksum += detector.getAsInt();
            }
            long elapsed = System.nanoTime() - start;
            benchmarkSink = checksum;
            samples[trial] = (double) elapsed / repetitions;
        }

        Arrays.sort(samples);
        return samples[TRIALS / 2];
    }

    private static void printUnsupported(String algorithm, int graphType) {
        String type = graphType == 1 ? "directed small"
                : graphType == 2 ? "directed large" : "undirected";
        System.out.println("SKIP: " + algorithm + " is not supported on " + type + " graphs.");
    }

    public static void runTest(String testName, int expected, int actual) {
        printResult(testName, expected == actual,
                String.valueOf(expected), String.valueOf(actual));
    }

    public static void runTest(String testName, boolean expected, boolean actual) {
        printResult(testName, expected == actual,
                String.valueOf(expected), String.valueOf(actual));
    }

    private static void printResult(String testName, boolean passed,
                                    String expected, String actual) {
        System.out.println("Test: " + testName);
        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + actual);
        System.out.println(passed ? "PASS" : "FAIL");
        System.out.println();
    }

    private static void saveCsv() {
        Path path = Path.of("benchmark_results.csv");
        try {
            Files.write(path, csvRows);
            System.out.println("\nCSV path: " + path.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Bad Save: " + e.getMessage());
        }
    }
}
