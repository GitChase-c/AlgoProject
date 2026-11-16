
public class UndirectedCycleDetection {

    public static boolean hasCycle(Graph graph) {

        // Make sure the graph is undirected
        if (graph.isDirected()) {
            throw new IllegalArgumentException(
                    "This algorithm requires an undirected graph"
            );
        }

        int vertices = graph.getVertexCount();
        boolean[] visited = new boolean[vertices];

        // Check all vertices, including disconnected components
        for (int i = 0; i < vertices; i++) {

            if (!visited[i]) {
                if (dfs(graph, i, -1, visited)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean dfs(
            Graph graph,
            int currentVertex,
            int parent,
            boolean[] visited) {

        // Mark current vertex as visited
        visited[currentVertex] = true;

        // Examine all neighboring vertices
        for (int neighbor : graph.neighborsOf(currentVertex)) {

            // Ignore the edge leading back to the parent
            if (neighbor == parent) {
                continue;
            }

            // Recursively explore unvisited neighbors
            if (!visited[neighbor]) {
                if (dfs(graph, neighbor, currentVertex, visited)) {
                    return true;
                }

                // Visited neighbor that isn't the parent
            } else {
                return true;
            }
        }

        // No cycle found
        return false;
    }
}
