import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese el nombre del archivo del grafo (ej. guategrafo.txt): ");
        String fileName = sc.nextLine().trim();

        DirectedWeightedGraph graph;
        try {
            graph = DirectedWeightedGraph.fromFile(fileName);
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo: " + e.getMessage());
            return;
        }

        if (graph.size() == 0) {
            System.out.println("El grafo está vacío.");
            return;
        }

        FloydWarshallResult result = FloydWarshall.compute(graph);

        System.out.println("\nMatriz de adyacencia inicial:");
        graph.printMatrix();

        boolean running = true;

        while (running) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Consultar ruta más corta entre dos ciudades");
            System.out.println("2. Mostrar centro del grafo");
            System.out.println("3. Modificar grafo");
            System.out.println("4. Mostrar matriz de adyacencia");
            System.out.println("5. Salir");
            System.out.print("Opción: ");

            String option = sc.nextLine().trim();

            switch (option) {
                case "1":
                    System.out.print("Ciudad origen: ");
                    String origin = sc.nextLine().trim();
                    System.out.print("Ciudad destino: ");
                    String destination = sc.nextLine().trim();

                    double distance = result.getDistance(origin, destination, graph);
                    if (distance == DirectedWeightedGraph.INF) {
                        System.out.println("No existe ruta entre esas ciudades.");
                    } else {
                        System.out.println("Distancia más corta: " + distance + " KM");
                        System.out.println("Ruta: " + result.pathAsString(origin, destination, graph));
                    }
                    break;

                case "2":
                    System.out.println("Centro del grafo: " + FloydWarshall.center(graph, result));
                    break;

                case "3":
                    modifyGraph(sc, graph);
                    result = FloydWarshall.compute(graph);
                    System.out.println("\nGrafo actualizado. Se recalcularon Floyd y el centro.");
                    break;

                case "4":
                    graph.printMatrix();
                    break;

                case "5":
                    running = false;
                    System.out.println("Programa finalizado.");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }
        }

        sc.close();
    }

    private static void modifyGraph(Scanner sc, DirectedWeightedGraph graph) {
        System.out.println("\n--- MODIFICAR GRAFO ---");
        System.out.println("1. Agregar arco");
        System.out.println("2. Eliminar arco");
        System.out.print("Opción: ");
        String option = sc.nextLine().trim();

        switch (option) {
            case "1":
                System.out.print("Ciudad origen: ");
                String from = sc.nextLine().trim();
                System.out.print("Ciudad destino: ");
                String to = sc.nextLine().trim();
                System.out.print("Distancia (KM): ");
                double weight = Double.parseDouble(sc.nextLine().trim());

                graph.addEdge(from, to, weight);
                System.out.println("Arco agregado.");
                break;

            case "2":
                System.out.print("Ciudad origen: ");
                from = sc.nextLine().trim();
                System.out.print("Ciudad destino: ");
                to = sc.nextLine().trim();

                graph.removeEdge(from, to);
                System.out.println("Arco eliminado.");
                break;

            default:
                System.out.println("Opción inválida.");
        }
    }
}