import java.util.Arrays;

public class FunctionalGraph {
    public static final int NO_SUCCESSOR = -1;

    private final int[] successor;
    private int edgeCount = 0;

    public FunctionalGraph(int vertices) {
        if (vertices < 0) {
            throw new IllegalArgumentException("Vertex count cannot be negative");
        }
        successor = new int[vertices];
        Arrays.fill(successor, NO_SUCCESSOR);
    }

    public void setSuccessor(int from, int to) {
        validateVertex(from);
        if (to != NO_SUCCESSOR) {
            validateVertex(to);
        }

        if (successor[from] == NO_SUCCESSOR && to != NO_SUCCESSOR) {
            edgeCount++;
        } else if (successor[from] != NO_SUCCESSOR && to == NO_SUCCESSOR) {
            edgeCount--;
        }
        successor[from] = to;
    }

    public int getSuccessor(int vertex) {
        validateVertex(vertex);
        return successor[vertex];
    }

    public int getVertexCount() {
        return successor.length;
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    public Graph toGraph() {
        Graph graph = new Graph(getVertexCount(), true);
        for (int vertex = 0; vertex < successor.length; vertex++) {
            if (successor[vertex] != NO_SUCCESSOR) {
                graph.addEdge(vertex, successor[vertex]);
            }
        }
        return graph;
    }

    public void printGraph() {
        System.out.println("FUNCTIONAL GRAPH");
        for (int vertex = 0; vertex < successor.length; vertex++) {
            System.out.println(vertex + " -> " +
                    (successor[vertex] == NO_SUCCESSOR ? "none" : successor[vertex]));
        }
    }

    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= successor.length) {
            throw new IndexOutOfBoundsException(
                "Vertex " + vertex + " is out of range [0, " + (successor.length - 1) + "]"
            );
        }
    }
}
