import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DirectedWeightedGraphTest {

    @Test
    void testAddVertexAndEdge() {
        DirectedWeightedGraph g = new DirectedWeightedGraph();
        g.addVertex("Mixco");
        g.addVertex("Antigua");
        g.addEdge("Mixco", "Antigua", 30);

        assertEquals(2, g.size());
        assertEquals(30.0, g.getEdgeWeight("Mixco", "Antigua"));
    }

    @Test
    void testRemoveEdge() {
        DirectedWeightedGraph g = new DirectedWeightedGraph();
        g.addEdge("A", "B", 10);
        g.removeEdge("A", "B");

        assertEquals(DirectedWeightedGraph.INF, g.getEdgeWeight("A", "B"));
    }

    @Test
    void testFloydShortestPath() {
        DirectedWeightedGraph g = new DirectedWeightedGraph();
        g.addEdge("A", "B", 5);
        g.addEdge("B", "C", 7);
        g.addEdge("A", "C", 20);

        FloydWarshallResult r = FloydWarshall.compute(g);

        assertEquals(12.0, r.getDistance("A", "C", g));
        assertEquals("A -> B -> C", r.pathAsString("A", "C", g));
    }
}