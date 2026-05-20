import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FloydWarshallResult {
    private final double[][] dist;
    private final int[][] next;
    private final List<String> names;

    public FloydWarshallResult(double[][] dist, int[][] next, List<String> names) {
        this.dist = dist;
        this.next = next;
        this.names = new ArrayList<>(names);
    }

    public double[][] getDist() {
        return dist;
    }

    public List<String> getPath(String from, String to, DirectedWeightedGraph graph) {
        int u = graph.getIndex(from);
        int v = graph.getIndex(to);

        if (u == -1 || v == -1 || next[u][v] == -1) {
            return Collections.emptyList();
        }

        List<String> path = new ArrayList<>();
        path.add(graph.getName(u));

        while (u != v) {
            u = next[u][v];
            if (u == -1) {
                return Collections.emptyList();
            }
            path.add(graph.getName(u));
        }

        return path;
    }

    public String pathAsString(String from, String to, DirectedWeightedGraph graph) {
        List<String> path = getPath(from, to, graph);
        if (path.isEmpty()) {
            return "No existe ruta.";
        }
        return String.join(" -> ", path);
    }

    public double getDistance(String from, String to, DirectedWeightedGraph graph) {
        int u = graph.getIndex(from);
        int v = graph.getIndex(to);

        if (u == -1 || v == -1) {
            return DirectedWeightedGraph.INF;
        }
        return dist[u][v];
    }
}