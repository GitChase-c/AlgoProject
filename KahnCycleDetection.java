import java.util.ArrayDeque;
import java.util.Queue;

public class KahnCycleDetection {

    public static boolean hasCycle(Graph graph) {
        //calc indegrees
        //add zero ind egree verticies to queue
        //process teh queue
        //detemrien whether a cycle exists
        if (!graph.isDirected()) {
            throw new IllegalArgumentException("Kahn's algorithm requires a directed graph");
        }

        int vertices = graph.getVertexCount();
        int[] inDegree = new int[vertices];

        for (int vertex = 0; vertex < vertices; vertex++) {
            for (int neighbor : graph.neighborsOf(vertex)) {
                inDegree[neighbor]++;
            }
        }

        Queue<Integer> queue = new ArrayDeque<>();

        for (int vertex = 0; vertex < vertices; vertex++) {
            if (inDegree[vertex] == 0) {
                queue.add(vertex);
            }
        }

        int processedVertices = 0;

        while (!queue.isEmpty()) {
            int currentVertex = queue.remove();
            processedVertices++;

            for (int neighbor : graph.neighborsOf(currentVertex)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.add(neighbor);
                }
            }
        }

        return processedVertices < vertices;
    }
}
