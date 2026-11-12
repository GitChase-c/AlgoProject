import java.util.Queue;
import java.util.ArrayDeque;

public class KahnCycleDetection {
    public static boolean hasCycle(Graph graph) {
        //calc indegrees
        //add zero ind egree verticies to queue
        //process teh queue
        //detemrien whether a cycle exists
        int vertices = graph.getVertexCount();
        int[] inDegree = new int[vertices];

//should loop through each vertex and calc outoging edges to neighbors
        for (int i = 0; i < vertices; i++) {
            for (int neighbor : graph.neighborsOf(i)) {
                inDegree[neighbor]++;
            }
        }

        Queue<Integer> queue = new ArrayDeque<>();

        for (int i = 0; i < vertices; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
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

        //if processed verticies arent equal to total verticies there isa cycle
        return processedVertices < vertices;
    }
}
