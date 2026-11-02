public final class GraphGenerator {
    private GraphGenerator() {

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

    public static Graph directedAcyclic(int vertices) {
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

    public static Graph directedBranchingWithCycle(int vertices) {
        if (vertices < 3) {
            throw new IllegalArgumentException("Need at least 3 vertices");
        }
        Graph graph = directedAcyclic(vertices);
        graph.addEdge(vertices - 1, 1);
        return graph;
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
        Graph graph = new Graph(vertices, false);
        for (int i = 1; i < vertices; i++) {
            graph.addEdge((i - 1) / 2, i);
        }
        return graph;
    }

    public static Graph undirectedWithCycle(int vertices) {
        if (vertices < 3) {
            throw new IllegalArgumentException("Need at least 3 vertices");
        }
        Graph graph = undirectedTree(vertices);
        graph.addEdge(1, 2);
        return graph;
    }
}
