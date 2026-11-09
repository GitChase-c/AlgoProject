
public class DFSCycleDetection {

    public static boolean hasCycle(Graph graph) {

        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "This DFS implementation requires a directed graph"
            );
        }

        int vertices = graph.getVertexCount();

        //setting an array to keep track of vertex states
        int[] state = new int[vertices];

        for (int i = 0; i < vertices; i++) {

            if (state[i] == 0) {
                if (dfs(graph, i, state)) {
                    return true;
                }
            }
        }

        return false;
    }

    //recursive helper function to check for cycles down a graph path
    private static boolean dfs(
            Graph graph,
            int currentVertex,
            int[] state) {

        // 1 = Visiting
        state[currentVertex] = 1;

        for (int neighbor : graph.neighborsOf(currentVertex)) {

            // Neighbor is unvisited
            if (state[neighbor] == 0) {

                if (dfs(graph, neighbor, state)) {
                    return true;
                }

                // Neighbor is still on the DFS path
            } else if (state[neighbor] == 1) {
                return true;
            }
        }

        // 2 = Visited (fully processed)
        state[currentVertex] = 2;

        return false;
    }
}
