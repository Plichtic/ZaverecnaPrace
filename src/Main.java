import GraphPackage.Graph;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Graph Visualization");
                Graph graph = new Graph();
                frame.add(graph);
                frame.setSize(800, 600);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                try {
                    graph.loadGraph();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}