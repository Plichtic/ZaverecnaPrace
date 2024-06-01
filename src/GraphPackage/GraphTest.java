package GraphPackage;

import org.junit.Assert;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @org.junit.jupiter.api.Test
    void dijkstrasTest() throws IOException {
        Graph graph = new Graph();
        graph.loadGraph();
        ArrayList<Integer> correct = new ArrayList<>();
        correct.add(2);
        correct.add(4);
        correct.add(7);
        assertEquals(correct,graph.dijkstras(2,7));
    }

    @org.junit.jupiter.api.Test
    void loadGraphTest() throws IOException {
        Graph graph = new Graph();
        graph.loadGraph();
        int[][] correct={{0,30,20,25,0,0,0,0,0},
                        {30,0,35,0,15,0,23,0,0},
                        {20,35,0,18,33,42,0,0,0},
                        {25,0,18,0,0,51,0,0,0},
                        {0,15,33,0,0,0,63,17,0},
                        {0,0,42,51,0,0,0,10,0},
                        {0,23,0,0,63,0,0,41,13},
                        {0,0,0,0,17,10,41,0,27},
                        {0,0,0,0,0,0,13,27,0}};
        assertArrayEquals(correct,graph.graph);
    }
    @org.junit.jupiter.api.Test
    void getConnectedNodes() throws IOException {
        Graph graph = new Graph();
        graph.loadGraph();
        ArrayList<Integer> correct = new ArrayList<>();
        correct.add(1);
        correct.add(2);
        correct.add(6);
        correct.add(7);
        assertEquals(correct,graph.getConnectedNodes(4));
    }
    @org.junit.jupiter.api.Test
    void addNode() throws IOException {
        Graph graph = new Graph();
        graph.loadGraph();
        graph.addNode();
        assertEquals(10,graph.graph.length);

    }
    @org.junit.jupiter.api.Test
    void removeNode() throws IOException {
        Graph graph = new Graph();
        graph.loadGraph();
        graph.removeNode(0);
        assertEquals(8, graph.graph.length);
    }
}