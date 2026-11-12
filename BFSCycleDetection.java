import java.util.Queue;
import java.util.ArrayDeque;
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

        //keeps track of visited status (true = visited, false = not visited)
        int vertices = graph.getVertexCount();
        boolean[] visited = new boolean[vertices];
        int[] parent = new int[vertices];

        //stores veticies awating exploireation
        Queue<Integer> queue = new ArrayDeque<>();

        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                visited[i] = true;
                queue.add(i);
            }

            while (!queue.isEmpty()) {

                int currentVertex = queue.remove();

                for (int neighbor : graph.neighborsOf(currentVertex)) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        parent[neighbor] = currentVertex;
                        queue.add(neighbor);
                    }
                    else if(neighbor != parent[currentVertex]) {
                        return true;
                    }

                }
            }
        }
        return false;
    }
}

