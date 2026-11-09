public class Main {
    public static void main(String[] args) {
        LinkedList myList = new LinkedList();

        myList.insert(10);
        myList.insert(20);
        myList.insert(30);
        myList.insert(40);
        myList.insert(50);

        myList.makeCycle(2);

        Integer cycle = myList.checkForCycle();

        if (cycle == null) {
            System.out.println("No cycle detected");
        } else {
            System.out.println(
                    "Cycle detected, starts at " + cycle
            );
        }

        // ==================================
        // 1. FLOYD CYCLE DETECTION TESTS
        // ==================================

        System.out.println("\n=== FLOYD CYCLE DETECTION ===");

        for (int n = 100; n <= 500; n += 100) {

            FunctionalGraph cyclicGraph =
                    GraphGenerator.functionalWithCycle(n, n / 2);

            FunctionalGraph acyclicGraph =
                    GraphGenerator.functionalPath(n);

            runTest(
                    "Floyd Cyclic (" + n + " vertices)",
                    n / 2,
                    FloydCycleDetection.findCycleStart(cyclicGraph, 0)
            );

            runTest(
                    "Floyd Acyclic (" + n + " vertices)",
                    -1,
                    FloydCycleDetection.findCycleStart(acyclicGraph, 0)
            );
        }

        // ==================================
        // 2. DIRECTED DFS TESTS
        // ==================================

        System.out.println("\n=== DIRECTED DFS CYCLE DETECTION ===");

        for (int n = 100; n <= 500; n += 100) {

            Graph acyclicGraph =
                    GraphGenerator.directedAcyclic(n);

            Graph cyclicGraph =
                    GraphGenerator.directedBranchingWithCycle(n);

            runTest(
                    "Directed DFS Acyclic (" + n + " vertices)",
                    false,
                    DFSCycleDetection.hasCycle(acyclicGraph)
            );

            runTest(
                    "Directed DFS Cyclic (" + n + " vertices)",
                    true,
                    DFSCycleDetection.hasCycle(cyclicGraph)
            );
        }

        // ==================================
        // 3. UNDIRECTED DFS TESTS
        // ==================================

        System.out.println("\n=== UNDIRECTED DFS CYCLE DETECTION ===");

        for (int n = 100; n <= 500; n += 100) {

            Graph tree = GraphGenerator.undirectedTree(n);

            Graph cyclicGraph =
                    GraphGenerator.undirectedWithCycle(n);

            runTest(
                    "Undirected DFS Acyclic (" + n + " vertices)",
                    false,
                    UndirectedCycleDetection.hasCycle(tree)
            );

            runTest(
                    "Undirected DFS Cyclic (" + n + " vertices)",
                    true,
                    UndirectedCycleDetection.hasCycle(cyclicGraph)
            );
        }
    }

    public static void runTest(
            String testName,
            int expected,
            int actual) {

        printResult(
                testName,
                expected == actual,
                String.valueOf(expected),
                String.valueOf(actual)
        );
    }

    public static void runTest(
            String testName,
            boolean expected,
            boolean actual) {

        printResult(testName, expected == actual,
                String.valueOf(expected), String.valueOf(actual));
    }

    private static void printResult(
            String testName,
            boolean passed,
            String expected,
            String actual) {

        System.out.println("Test: " + testName);
        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + actual);
        System.out.println(passed ? "PASS" : "FAIL");
        System.out.println();
    }

}