import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

//changed from recursive to iterative so that dfs works as expected on undirected and large graphs
public class DFSCycleDetection {

    private static class Frame {
        final int vertex;
        final int parent;
        final Iterator<Integer> neighbors;
        boolean skippedParentEdge;

        Frame(Graph graph, int vertex, int parent) {
            this.vertex = vertex;
            this.parent = parent;
            this.neighbors = graph.neighborsOf(vertex).iterator();
        }
    }

    public static boolean hasCycle(Graph graph) {
        if (graph.isDirected()) {
            return directedHasCycle(graph);
        }
        return undirectedHasCycle(graph);
    }

    private static boolean directedHasCycle(Graph graph) {
        int vertices = graph.getVertexCount();
        int[] state = new int[vertices];
        Deque<Frame> stack = new ArrayDeque<>();

        for (int start = 0; start < vertices; start++) {
            if (state[start] != 0) {
                continue;
            }

            state[start] = 1;
            stack.push(new Frame(graph, start, -1));

            while (!stack.isEmpty()) {
                Frame current = stack.peek();

                if (!current.neighbors.hasNext()) {
                    state[current.vertex] = 2;
                    stack.pop();
                    continue;
                }

                int neighbor = current.neighbors.next();

                if (state[neighbor] == 1) {
                    return true;
                }

                if (state[neighbor] == 0) {
                    state[neighbor] = 1;
                    stack.push(new Frame(graph, neighbor, current.vertex));
                }
            }
        }

        return false;
    }

    private static boolean undirectedHasCycle(Graph graph) {
        int vertices = graph.getVertexCount();
        boolean[] visited = new boolean[vertices];
        Deque<Frame> stack = new ArrayDeque<>();

        for (int start = 0; start < vertices; start++) {
            if (visited[start]) {
                continue;
            }

            visited[start] = true;
            stack.push(new Frame(graph, start, -1));

            while (!stack.isEmpty()) {
                Frame current = stack.peek();

                if (!current.neighbors.hasNext()) {
                    stack.pop();
                    continue;
                }

                int neighbor = current.neighbors.next();

                if (neighbor == current.parent && !current.skippedParentEdge) {
                    current.skippedParentEdge = true;
                    continue;
                }

                if (visited[neighbor]) {
                    return true;
                }

                visited[neighbor] = true;
                stack.push(new Frame(graph, neighbor, current.vertex));
            }
        }

        return false;
    }
}
