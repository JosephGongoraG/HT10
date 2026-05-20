import java.util.List;

public class FloydWarshall {

    public static FloydWarshallResult compute(DirectedWeightedGraph graph) {
        int n = graph.size();
        double[][] dist = graph.getMatrixCopy();
        int[][] next = new int[n][n];
        double[][] original = graph.getMatrixCopy();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j || original[i][j] != DirectedWeightedGraph.INF) {
                    next[i][j] = j;
                } else {
                    next[i][j] = -1;
                }
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (dist[i][k] == DirectedWeightedGraph.INF) continue;

                for (int j = 0; j < n; j++) {
                    if (dist[k][j] == DirectedWeightedGraph.INF) continue;

                    double candidate = dist[i][k] + dist[k][j];
                    if (candidate < dist[i][j]) {
                        dist[i][j] = candidate;
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        return new FloydWarshallResult(dist, next, graph.getNames());
    }

    public static String center(DirectedWeightedGraph graph, FloydWarshallResult result) {
        int n = graph.size();
        double[][] dist = result.getDist();

        String bestVertex = null;
        double bestEccentricity = DirectedWeightedGraph.INF;

        for (int i = 0; i < n; i++) {
            double eccentricity = 0;
            boolean reachesAll = true;

            for (int j = 0; j < n; j++) {
                if (dist[i][j] == DirectedWeightedGraph.INF) {
                    reachesAll = false;
                    break;
                }
                if (dist[i][j] > eccentricity) {
                    eccentricity = dist[i][j];
                }
            }

            if (reachesAll && eccentricity < bestEccentricity) {
                bestEccentricity = eccentricity;
                bestVertex = graph.getName(i);
            }
        }

        if (bestVertex == null) {
            return "No existe centro: el grafo no es fuertemente conexo.";
        }

        return bestVertex + " (excentricidad = " + bestEccentricity + ")";
    }
}