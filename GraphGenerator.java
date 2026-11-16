public final class GraphGenerator {
    private static final int SMALL_DIRECTED_MAX = 1000;
    private static final int LARGE_DIRECTED_MIN = 1001;
    private static final int SMALL_UNDIRECTED_MAX = 99999;

    private GraphGenerator() {
    }

    public static Graph directedCyclic(int vertices) {
        int size = validSize(vertices, 2, SMALL_DIRECTED_MAX);
        return buildDirectedCyclic(size);
    }

    public static Graph directedAcyclic(int vertices) {
        int size = validSize(vertices, 1, SMALL_DIRECTED_MAX);
        return buildDirectedAcyclic(size);
    }

    public static Graph directedLCyclic(int vertices) {
        int size = Math.max(vertices, LARGE_DIRECTED_MIN);
        return buildDirectedCyclic(size);
    }

    public static Graph directedLAcyclic(int vertices) {
        int size = Math.max(vertices, LARGE_DIRECTED_MIN);
        return buildDirectedAcyclic(size);
    }

    public static Graph undirectedCyclic(int vertices) {
        int size = validSize(vertices, 3, SMALL_UNDIRECTED_MAX);
        return buildUndirectedCyclic(size);
    }

    public static Graph undirectedAcyclic(int vertices) {
        int size = validSize(vertices, 1, SMALL_UNDIRECTED_MAX);
        return buildUndirectedTree(size);
    }

    public static Graph undirectredAcyclic(int vertices) {
        return undirectedAcyclic(vertices);
    }

    public static FunctionalGraph functionalPath(int vertices) {
        FunctionalGraph graph = new FunctionalGraph(vertices);

        for (int i = 0; i + 1 < vertices; i++) {
            graph.setSuccessor(i, i + 1);
        }

        return graph;
    }

    public static FunctionalGraph functionalWithCycle(int vertices, int cycleStart) {
        if (vertices < 1) {
            throw new IllegalArgumentException("A cyclic graph needs at least 1 vertex");
        }

        if (cycleStart < 0 || cycleStart >= vertices) {
            throw new IllegalArgumentException("Invalid cycle entry: " + cycleStart);
        }

        FunctionalGraph graph = functionalPath(vertices);
        graph.setSuccessor(vertices - 1, cycleStart);
        return graph;
    }

    public static Graph directedBranchingWithCycle(int vertices) {
        if (vertices < 3) {
            throw new IllegalArgumentException("Need at least 3 vertices");
        }

        return buildDirectedCyclic(vertices);
    }

    public static Graph directedCycleOnUnchosenBranch() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 2);
        return graph;
    }

    public static Graph undirectedTree(int vertices) {
        return buildUndirectedTree(vertices);
    }

    public static Graph undirectedWithCycle(int vertices) {
        if (vertices < 3) {
            throw new IllegalArgumentException("Need at least 3 vertices");
        }

        return buildUndirectedCyclic(vertices);
    }

    private static int validSize(int requested, int minimum, int maximum) {
        return requested < minimum || requested > maximum ? minimum : requested;
    }

    private static Graph buildDirectedAcyclic(int vertices) {
        Graph graph = new Graph(vertices, true);

        for (int i = 0; i < vertices; i++) {
            if (i + 1 < vertices) {
                graph.addEdge(i, i + 1);
            }

            if (i + 2 < vertices) {
                graph.addEdge(i, i + 2);
            }
        }

        return graph;
    }

    private static Graph buildDirectedCyclic(int vertices) {
        Graph graph = buildDirectedAcyclic(vertices);
        graph.addEdge(vertices - 1, vertices == 2 ? 0 : 1);
        return graph;
    }

    private static Graph buildUndirectedTree(int vertices) {
        Graph graph = new Graph(vertices, false);

        for (int i = 1; i < vertices; i++) {
            graph.addEdge((i - 1) / 2, i);
        }

        return graph;
    }

    private static Graph buildUndirectedCyclic(int vertices) {
        Graph graph = buildUndirectedTree(vertices);
        graph.addEdge(1, 2);
        return graph;
    }
}
