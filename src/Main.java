import GraphPackage.Graph;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

               JFrame frame = new JFrame("Graph Visualization");

                Graph graph = new Graph();
        try {
            graph.loadGraph();
        } catch (IOException e) {
            e.printStackTrace();
        }
                frame.add(graph);
                frame.setSize(800, 600);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                frame.setVisible(true);
    }
    }