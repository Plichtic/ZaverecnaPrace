import GraphPackage.Graph;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {



        Graph graph = new Graph();

        graph.loadGraph();
        graph.getGraph();
        System.out.println(graph.dijkstras(0,7));
    }

}