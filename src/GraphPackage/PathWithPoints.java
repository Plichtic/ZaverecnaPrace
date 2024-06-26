package GraphPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.*;

public class PathWithPoints extends JPanel {
    private int[][] graph;
    private int numberOfPoints;
    private final JComboBox<Integer> comboBox1 = new JComboBox<>();
    private final JComboBox<Integer> comboBox2 = new JComboBox<>();
    private final JComboBox<Integer> comboBox3 = new JComboBox<>();
    private final JButton submitButton = new JButton("Submit");
    private ArrayList<Integer> shortestPath = new ArrayList<>();
    private Integer[] numbers;
    private int nodeRadius = 20;
    private final int width = 800;
    private final int height = 600;
    private final Integer[] numberOfPointsBox = {1, 2, 3};
    private int node1;
    private int node2;
    private int node3;
    private Graph dijkstrGraph;
    private ArrayList<Integer> pathNodes;
    private int pathValue;

    /**
     *
     * @param graph Graph to show
     * @param selectedNumber1 Start node
     * @param selectedNumber2 End node
     * @param numberOfNodes Number of nodes in between
     * @param dijkstrGraph To use dijkstr
     */
    public PathWithPoints(int[][] graph, int selectedNumber1, int selectedNumber2, int numberOfNodes,Graph dijkstrGraph) {
        this.graph = graph;
        this.numbers = new Integer[numberOfNodes];
        this.dijkstrGraph=dijkstrGraph;
        for (int i = 0; i < numberOfNodes; i++) {
            numbers[i] = i + 1;
        }


        JFrame frame = new JFrame("Path with points");
        frame.setSize(380, 200);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new FlowLayout());


        JComboBox<Integer> numberOfPointsComboBox = new JComboBox<>(numberOfPointsBox);
        JButton submitNumberOfPoints = new JButton("Submit number of points ");
        submitNumberOfPoints.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberOfPoints = (Integer) numberOfPointsComboBox.getSelectedItem();

                frame.getContentPane().removeAll();

                switch (numberOfPoints) {
                    case 1:
                        addNumbersButton(comboBox1, numberOfNodes);
                        frame.add(comboBox1);
                        break;
                    case 2:
                        addNumbersButton(comboBox1, numberOfNodes);
                        addNumbersButton(comboBox2, numberOfNodes);
                        frame.add(comboBox1);
                        frame.add(comboBox2);
                        break;
                    case 3:
                        addNumbersButton(comboBox1, numberOfNodes);
                        addNumbersButton(comboBox2, numberOfNodes);
                        addNumbersButton(comboBox3, numberOfNodes);
                        frame.add(comboBox1);
                        frame.add(comboBox2);
                        frame.add(comboBox3);
                        break;
                }
                frame.add(submitButton);

                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                            shortestPath.clear();
                            switch (numberOfPoints) {
                                case 1:
                                    node1 = (Integer) comboBox1.getSelectedItem()-1;
                                    shortestPath.add(node1);
                                    break;
                                case 2:
                                    node1 = (Integer) comboBox1.getSelectedItem()-1;
                                    node2 = (Integer) comboBox2.getSelectedItem()-1;
                                    shortestPath.add(node1);
                                    shortestPath.add(node2);
                                    break;
                                case 3:
                                    node1 = (Integer) comboBox1.getSelectedItem()-1;
                                    node2 = (Integer) comboBox2.getSelectedItem()-1;
                                    node3 = (Integer) comboBox3.getSelectedItem()-1;
                                    shortestPath.add(node1);
                                    shortestPath.add(node2);
                                    shortestPath.add(node3);
                                    break;
                            }
                            shortestPath.add(0,selectedNumber1);
                            shortestPath.add(selectedNumber2);
                            pathNodes = new ArrayList<>();
                            ArrayList<Integer> helpList;
                            pathValue=0;
                            for(int i=0;i<shortestPath.size()-1;i++){
                                helpList=dijkstrGraph.dijkstras(shortestPath.get(i),shortestPath.get(i+1));
                                pathValue=pathValue+dijkstrGraph.getPathValue();
                                for (int j=0;j< helpList.size();j++){
                                    pathNodes.add(helpList.get(j));
                                }

                            }
                            repaint();

                    }
                });

                frame.revalidate();
                frame.repaint();
            }
        });

        frame.revalidate();
        frame.repaint();

        frame.add(numberOfPointsComboBox);
        frame.add(submitNumberOfPoints);
        frame.setVisible(true);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;


        int centerX = width / 2;
        int centerY = height / 2;
        double radius = Math.min(width, height) / 2.5;
        double angleIncrement = 2 * Math.PI / graph.length;

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

                    int labelX = (x1 + x2) / 2;
                    int labelY = (y1 + y2) / 2;
                    g2d.drawString(Integer.toString(graph[i][j]), labelX, labelY);
                }
            }
        }

        if (pathNodes != null) {
            g2d.setColor(Color.GREEN);
            g2d.setStroke(new BasicStroke(3));
            for (int i = 0; i < pathNodes.size() - 1; i++) {
                int node1 = pathNodes.get(i);
                int node2 = pathNodes.get(i + 1);
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

    /**
     *
     * @param comboBox ComboBox to add items to
     * @param number How many numbers to add
     */
    public void addNumbersButton(JComboBox comboBox, int number) {
        for (int i = 0; i < number; i++) {
            comboBox.addItem(i + 1);
        }
    }


}