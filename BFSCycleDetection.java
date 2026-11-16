import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class BFSCycleDetection {

    //queue.add(x) add vertex to back of queue
    //queue.remove removes the front vertex
    //queue.isempty determines whehter vertiices rtemain

    /* graph.getVertexCount();
    graph.neighborsOf(currentVertex);
    graph.isDirected(); */

    //validate graph type
    //crerated visit and parent info
    //explore vetricies using queue
    //return ture when cycle is found

    public static boolean hasCycle(Graph graph) {
        if (graph.isDirected()) {
            throw new IllegalArgumentException("This BFS cycle detector requires an undirected graph");
        }

        int vertices = graph.getVertexCount();
        boolean[] visited = new boolean[vertices];
        int[] parent = new int[vertices];
        boolean[] skippedParentEdge = new boolean[vertices];
        Arrays.fill(parent, -1);

        Queue<Integer> queue = new ArrayDeque<>();

        for (int start = 0; start < vertices; start++) {
            if (visited[start]) {
                continue;
            }

            visited[start] = true;
            queue.add(start);

            while (!queue.isEmpty()) {
                int currentVertex = queue.remove();

                for (int neighbor : graph.neighborsOf(currentVertex)) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        parent[neighbor] = currentVertex;
                        queue.add(neighbor);
                    } else if (neighbor == parent[currentVertex] && !skippedParentEdge[currentVertex]) {
                        skippedParentEdge[currentVertex] = true;
                    } else {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
