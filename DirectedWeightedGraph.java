import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DirectedWeightedGraph {
    public static final double INF = Double.POSITIVE_INFINITY;

    private final Map<String, Integer> indexByName;
    private final List<String> names;
    private double[][] matrix;

    public DirectedWeightedGraph() {
        this.indexByName = new LinkedHashMap<>();
        this.names = new ArrayList<>();
        this.matrix = new double[0][0];
    }

    public int size() {
        return names.size();
    }

    public List<String> getNames() {
        return new ArrayList<>(names);
    }

    public boolean containsVertex(String name) {
        return indexByName.containsKey(normalize(name));
    }

    public int getIndex(String name) {
        Integer idx = indexByName.get(normalize(name));
        return idx == null ? -1 : idx;
    }

    public String getName(int index) {
        return names.get(index);
    }

    public void addVertex(String name) {
        name = normalize(name);
        if (name.isEmpty() || containsVertex(name)) {
            return;
        }

        int oldSize = size();
        names.add(name);
        indexByName.put(name, oldSize);

        double[][] newMatrix = new double[oldSize + 1][oldSize + 1];

        for (int i = 0; i < oldSize + 1; i++) {
            for (int j = 0; j < oldSize + 1; j++) {
                newMatrix[i][j] = (i == j) ? 0.0 : INF;
            }
        }

        for (int i = 0; i < oldSize; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, oldSize);
        }

        matrix = newMatrix;
    }

    public void addEdge(String from, String to, double weight) {
        from = normalize(from);
        to = normalize(to);

        if (from.isEmpty() || to.isEmpty()) {
            throw new IllegalArgumentException("Los nombres de las ciudades no pueden estar vacíos.");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("El peso no puede ser negativo.");
        }

        addVertex(from);
        addVertex(to);

        int i = getIndex(from);
        int j = getIndex(to);
        matrix[i][j] = weight;
    }

    public void removeEdge(String from, String to) {
        int i = getIndex(from);
        int j = getIndex(to);

        if (i == -1 || j == -1) {
            return;
        }
        matrix[i][j] = INF;
    }

    public double getEdgeWeight(String from, String to) {
        int i = getIndex(from);
        int j = getIndex(to);

        if (i == -1 || j == -1) {
            return INF;
        }
        return matrix[i][j];
    }

    public double[][] getMatrixCopy() {
        double[][] copy = new double[size()][size()];
        for (int i = 0; i < size(); i++) {
            System.arraycopy(matrix[i], 0, copy[i], 0, size());
        }
        return copy;
    }

    public void printMatrix() {
        System.out.print("          ");
        for (String name : names) {
            System.out.printf("%12s", name);
        }
        System.out.println();

        for (int i = 0; i < size(); i++) {
            System.out.printf("%10s", names.get(i));
            for (int j = 0; j < size(); j++) {
                if (matrix[i][j] == INF) {
                    System.out.printf("%12s", "INF");
                } else {
                    System.out.printf("%12.2f", matrix[i][j]);
                }
            }
            System.out.println();
        }
    }

    public static DirectedWeightedGraph fromFile(String filePath) throws IOException {
        DirectedWeightedGraph graph = new DirectedWeightedGraph();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("\\s+");
                if (parts.length < 3) {
                    continue;
                }

                // Ignora un posible encabezado
                if (parts[0].equalsIgnoreCase("Ciudad1")) {
                    continue;
                }

                String from = parts[0];
                String to = parts[1];
                double weight = Double.parseDouble(parts[2]);

                graph.addEdge(from, to, weight);
            }
        }

        return graph;
    }

    private String normalize(String text) {
        return text == null ? "" : text.trim();
    }
}