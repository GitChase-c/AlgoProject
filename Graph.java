import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Graph {
    private final ArrayList<LinkedList> adjacencyList = new ArrayList<>();
    private final boolean directed;
    private int edgeCount;

    public Graph() {
        this(true);
    }

    public Graph(boolean directed) {
        this.directed = directed;
    }

    public Graph(int vertices, boolean directed) {
        this(directed);

        if (vertices < 0) {
            throw new IllegalArgumentException("Vertex count cannot be negative");
        }

        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new LinkedList());
        }
    }

    public int addGraphNode() {
        int vertexId = adjacencyList.size();
        adjacencyList.add(new LinkedList());
        return vertexId;
    }

    public void addEdge(int from, int to) {
        validateVertex(from);
        validateVertex(to);

        adjacencyList.get(from).insert(to);

        if (!directed && from != to) {
            adjacencyList.get(to).insert(from);
        }

        edgeCount++;
    }

    public List<Integer> getNeighbors(int vertex) {
        List<Integer> neighbors = new ArrayList<>();

        for (int neighbor : neighborsOf(vertex)) {
            neighbors.add(neighbor);
        }

        return neighbors;
    }

    public Iterable<Integer> neighborsOf(int vertex) {
        validateVertex(vertex);
        final Node first = adjacencyList.get(vertex).head;

        return () -> new Iterator<Integer>() {
            private Node current = first;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Integer next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }

                int neighbor = current.data;
                current = current.next;
                return neighbor;
            }
        };
    }

    public int getFirstNeighbor(int vertex) {
        validateVertex(vertex);
        Node first = adjacencyList.get(vertex).head;
        return first == null ? -1 : first.data;
    }

    public int getVertexCount() {
        return adjacencyList.size();
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    public boolean isDirected() {
        return directed;
    }

    public void printGraph() {
        System.out.println(directed ? "DIRECTED GRAPH" : "UNDIRECTED GRAPH");

        for (int vertex = 0; vertex < getVertexCount(); vertex++) {
            System.out.println(vertex + " -> " + getNeighbors(vertex));
        }
    }

    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= getVertexCount()) {
            throw new IndexOutOfBoundsException(
                    "Vertex " + vertex + " is out of range [0, " + (getVertexCount() - 1) + "]"
            );
        }
    }
}
