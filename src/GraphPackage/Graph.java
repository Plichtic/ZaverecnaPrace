package GraphPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class Graph extends JPanel {
    private int numberOfNodes = 9;
    private int[][] graph = new int[numberOfNodes][numberOfNodes];
    protected int nodeRadius = 20;
    public ArrayList<Integer> shortestPath;
    private int selectedNumber1;
    private int selectedNumber2;
    private int selectedNode;
    private int pathValue;
    private PathWithPoints pathFinder;
    Integer[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    JComboBox<Integer> comboBox1 = new JComboBox<>(numbers);
    JComboBox<Integer> comboBox2 = new JComboBox<>(numbers);
    JComboBox<Integer> removeNodeBox = new JComboBox<>(numbers);
    JComboBox <Integer> changeNodeButton= new JComboBox<>(numbers);
    private int[] values = new int[numberOfNodes];

    public Graph() {
        JFrame frame = new JFrame("Choose Two Distinct Numbers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(410, 200);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new FlowLayout());

        JButton submitButton = new JButton("Submit");
        JButton submitNodeEdit = new JButton("Edit node");
        submitNodeEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedNode = (Integer) changeNodeButton.getSelectedItem()-1;
                editNode(selectedNode);
            }
        });
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
        JButton addNodeButton = new JButton("Add Node");

        addNodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNode();
            }
        });
        JButton submitNodeRemove = new JButton("Remove node");
        submitNodeRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeNode((Integer)removeNodeBox.getSelectedItem()-1);
            }
        });
        JButton saveGraph = new JButton("Save graph");
        saveGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGraphToCSV("src/GraphPackage/NewMatice");
            }
        });
        JButton pathWithPointsButton = new JButton("Path with points");
        pathWithPointsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedNumber1 = (Integer) comboBox1.getSelectedItem() - 1;
                selectedNumber2 = (Integer) comboBox2.getSelectedItem() - 1;
                if (selectedNumber1 != selectedNumber2) {
                    pathWithPointsCall();
                } else {
                    JOptionPane.showMessageDialog(null, "The numbers must be distinct!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(new JLabel("Select node to edit:"));
        frame.add(changeNodeButton);
        frame.add(submitNodeEdit);
        frame.add(new JLabel("Select first node: "));
        frame.add(comboBox1);
        frame.add(new JLabel("\nSelect second node: "));
        frame.add(comboBox2);
        frame.add(submitButton);
        frame.add(addNodeButton);
        frame.add(removeNodeBox);
        frame.add(submitNodeRemove);
        frame.add(pathWithPointsButton);

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

    public ArrayList<Integer> dijkstras(int start, int end) {
        try {
            values = new int[numberOfNodes];
            Arrays.fill(values, Integer.MAX_VALUE);
            boolean[] visited = new boolean[graph.length]; // Track visited nodes
            int[] previousNode = new int[graph.length];
            Arrays.fill(previousNode, -1);
            values[start] = 0;

            // Priority Queue for efficient node selection
            PriorityQueue<Integer> queue = new PriorityQueue<>((a, b) -> values[a] - values[b]);
            queue.offer(start);

            while (!queue.isEmpty()) {
                int currentnode = queue.poll();

                if (currentnode == end) { // Early exit if we reach the end node
                    break;
                }

                if (visited[currentnode]) {
                    continue; // Skip already visited nodes
                }

                visited[currentnode] = true;

                for (int connectedNode : getConnectedNodes(currentnode)) {
                    int newDistance = values[currentnode] + getDistance(currentnode, connectedNode);
                    if (newDistance < values[connectedNode]) {
                        values[connectedNode] = newDistance;
                        previousNode[connectedNode] = currentnode;
                        queue.offer(connectedNode); // Re-enqueue with updated distance
                    }
                }
            }

            // Check if a path to the end was found
            if (previousNode[end] == -1) {
                return null; // No path exists
            }

            // Reconstruct path
            ArrayList<Integer> shortestPath = new ArrayList<>();
            int node = end;
            while (node != -1) {
                shortestPath.add(0, node);
                node = previousNode[node];
            }

            pathValue = values[end];
            return shortestPath;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
    public void editNode(int node){
        int editFrameWidth = 800;
        int editFrameHeight = 600;
        JFrame frame = new JFrame("Editing node "+(node+1));
        EditingWindow editingWindow = new EditingWindow(node,graph,this,frame);
        frame.add(editingWindow);

        frame.setSize(editFrameWidth, editFrameHeight);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
    public void addNode() {
        int newNumberOfNodes = numberOfNodes + 1;
        int[][] newGraph = new int[newNumberOfNodes][newNumberOfNodes];

        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                newGraph[i][j] = graph[i][j];
            }
        }

        for (int i = 0; i < newNumberOfNodes; i++) {
            newGraph[i][newNumberOfNodes - 1] = 0;
            newGraph[newNumberOfNodes - 1][i] = 0;
        }
        Integer[] numbersUpdated=new Integer[newNumberOfNodes];
        for (int i = 1;i<newNumberOfNodes+1;i++){
            numbersUpdated[i-1]=i;
        }
        comboBox1.addItem(newNumberOfNodes);
        comboBox2.addItem(newNumberOfNodes);
        changeNodeButton.addItem(newNumberOfNodes);
        removeNodeBox.addItem(newNumberOfNodes);
        int[] newValues = new int[numberOfNodes+1];
        System.arraycopy(values, 0, newValues, 0, numberOfNodes);
        values = newValues;

        numbers=numbersUpdated;
        graph = newGraph;
        numberOfNodes = newNumberOfNodes;
        editNode(numberOfNodes - 1);
        repaint();
    }
    public void removeNode(int nodeToRemove) {
        if (nodeToRemove < 0 || nodeToRemove >= numberOfNodes) {
            System.out.println("Invalid node index.");
            return;
        }

        int newNumberOfNodes = numberOfNodes - 1;
        int[][] newGraph = new int[newNumberOfNodes][newNumberOfNodes];

        int newRow = 0, newCol;
        for (int i = 0; i < numberOfNodes; i++) {
            if (i == nodeToRemove) {
                continue;
            }
            newCol = 0;
            for (int j = 0; j < numberOfNodes; j++) {
                if (j == nodeToRemove) {
                    continue;
                }
                newGraph[newRow][newCol] = graph[i][j];
                newCol++;
            }
            newRow++;
        }

        Integer[] numbersUpdated = new Integer[newNumberOfNodes];
        for (int i = 0; i < newNumberOfNodes; i++) {
            numbersUpdated[i] = i + 1;
        }

        comboBox1.removeItem(numberOfNodes);
        comboBox2.removeItem(numberOfNodes);
        changeNodeButton.removeItem(numberOfNodes);
        removeNodeBox.removeItem(numberOfNodes);


        int[] newValues = new int[numberOfNodes - 1];
        int newIndex = 0;
        for (int i = 0; i < numberOfNodes; i++) {
            if (i != nodeToRemove) {
                newValues[newIndex] = values[i];
                newIndex++;
            }
        }
        values = newValues;

        numbers = numbersUpdated;
        graph = newGraph;
        numberOfNodes = newNumberOfNodes;
        repaint();
    }
    public void saveGraphToCSV(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < numberOfNodes; i++) {
                for (int j = 0; j < numberOfNodes; j++) {
                    writer.write(Integer.toString(graph[i][j]));
                    if (j < numberOfNodes - 1) {
                        writer.write(",");
                    }
                }
                writer.write("\n");
            }
            System.out.println("Graph saved to CSV file: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPathValue() {
        return pathValue;
    }
    private void pathWithPointsCall(){
        PathWithPoints pathWithPoints = new PathWithPoints(graph,selectedNumber1,selectedNumber2,numberOfNodes,this);
        JFrame frame = new JFrame("Graph Visualization");
        frame.add(pathWithPoints);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}