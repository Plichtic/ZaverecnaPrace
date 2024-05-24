package GraphPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Graph extends JPanel {
    private int[][] graph = new int[9][9];
    private int nodeRadius = 20;
    public ArrayList<Integer> shortestPath;
    private int selectedNumber1;
    private int selectedNumber2;
    private int pathValue;

    public Graph() {
        JFrame frame = new JFrame("Choose Two Distinct Numbers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new FlowLayout());

        Integer[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        JComboBox<Integer> comboBox1 = new JComboBox<>(numbers);
        JComboBox<Integer> comboBox2 = new JComboBox<>(numbers);
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedNumber1 = (Integer) comboBox1.getSelectedItem() - 1;
                selectedNumber2 = (Integer) comboBox2.getSelectedItem() - 1;
                if (selectedNumber1 != selectedNumber2) {
                    shortestPath = dijkstras(selectedNumber1, selectedNumber2);
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "The numbers must be distinct!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(new JLabel("Select first number:"));
        frame.add(comboBox1);
        frame.add(new JLabel("Select second number:"));
        frame.add(comboBox2);
        frame.add(submitButton);

        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        double radius = Math.min(getWidth(), getHeight()) / 2.5;
        double angleIncrement = 2 * Math.PI / graph.length;

        // Draw edges
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < graph.length; i++) {
            for (int j = i + 1; j < graph.length; j++) {
                if (graph[i][j] > 0) {
                    double angle1 = i * angleIncrement;
                    int x1 = (int) (centerX + radius * Math.cos(angle1));
                    int y1 = (int) (centerY + radius * Math.sin(angle1));
                    double angle2 = j * angleIncrement;
                    int x2 = (int) (centerX + radius * Math.cos(angle2));
                    int y2 = (int) (centerY + radius * Math.sin(angle2));

                    g2d.draw(new Line2D.Double(x1, y1, x2, y2));

                    // Edge weight label
                    int labelX = (x1 + x2) / 2;
                    int labelY = (y1 + y2) / 2;
                    g2d.drawString(Integer.toString(graph[i][j]), labelX, labelY);
                }
            }
        }

        if (shortestPath != null) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3));
            for (int i = 0; i < shortestPath.size() - 1; i++) {
                int node1 = shortestPath.get(i);
                int node2 = shortestPath.get(i + 1);
                double angle1 = node1 * angleIncrement;
                int x1 = (int) (centerX + radius * Math.cos(angle1));
                int y1 = (int) (centerY + radius * Math.sin(angle1));
                double angle2 = node2 * angleIncrement;
                int x2 = (int) (centerX + radius * Math.cos(angle2));
                int y2 = (int) (centerY + radius * Math.sin(angle2));
                g2d.draw(new Line2D.Double(x1, y1, x2, y2));
                g2d.drawString(Integer.toString(pathValue), centerX, centerY);
            }
        }

        // Draw nodes
        for (int i = 0; i < graph.length; i++) {
            double angle = i * angleIncrement;
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));

            g2d.setColor(Color.BLUE);
            g2d.fill(new Ellipse2D.Double(x - nodeRadius, y - nodeRadius, 2 * nodeRadius, 2 * nodeRadius));
            g2d.setColor(Color.WHITE);
            g2d.drawString(Integer.toString(i + 1), x - 5, y + 5);
        }
    }

    int[] values = new int[graph.length];
    public ArrayList<Integer> dijkstras(int start, int end) {
        Arrays.fill(values, Integer.MAX_VALUE);
        int[] previousNode = new int[graph.length];
        Arrays.fill(previousNode, -1);
        values[start] = 0;
        int[] nodes = new int[graph.length];
        for (int i = 0; i < graph.length; i++) {
            nodes[i] = i;
        }
        int currentnode = start;
        ArrayList<Integer> connectedNodes;
        while (currentnode != end) {
            currentnode = smallestValue(nodes);
            if (currentnode == -1) {
                return null;
            }
            nodes[currentnode] = -1;
            connectedNodes = getConnectedNodes(currentnode);
            for (int connectedNode : connectedNodes) {
                if (values[connectedNode] > (values[currentnode] + getDistance(currentnode, connectedNode))) {
                    values[connectedNode] = (values[currentnode] + getDistance(currentnode, connectedNode));
                    previousNode[connectedNode] = currentnode;
                }
            }
        }
        ArrayList<Integer> shortestPath = new ArrayList<>();
        while (currentnode != -1) {
            shortestPath.add(0, currentnode);
            currentnode = previousNode[currentnode];
        }
        pathValue=values[end];
        return shortestPath;
    }

    public int smallestValue(int[] list) {
        int smallest = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < list.length; i++) {
            if (list[i] < smallest && list[i] > -1) {
                smallest = list[i];
                index = i;
            }
        }
        return index;
    }

    public ArrayList<Integer> getConnectedNodes(int node) {
        ArrayList<Integer> otherNodes = new ArrayList<>();
        for (int i = 0; i < graph[node].length; i++) {
            if (graph[node][i] > 0) {
                otherNodes.add(i);
            }
        }
        return otherNodes;
    }

    public int getDistance(int node, int connectedNode) {
        return graph[node][connectedNode];
    }

    public void loadGraph() throws IOException, FileNotFoundException {
        try (BufferedReader br = new BufferedReader(new FileReader("src/GraphPackage/Matice.csv"))) {
            String line;
            int column = 0;
            while ((line = br.readLine()) != null) {
                String[] rozdeleni = line.split(";");
                for (int i = 0; i < rozdeleni.length; i++) {
                    graph[column][i] = Integer.parseInt(rozdeleni[i]);
                }
                column++;
            }
        }
    }

    public void getGraph() {
        for (int j = 0; j < graph.length; j++) {
            for (int i = 0; i < graph.length; i++) {
                System.out.print(graph[j][i] + " ");
            }
            System.out.println("");
        }
    }}