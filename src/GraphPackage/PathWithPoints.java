package GraphPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.List;
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
    private int[] dist;
    private int[] prev;

    public PathWithPoints(int[][] graph, int selectedNumber1, int selectedNumber2, int numberOfNodes) {
        this.graph = graph;
        this.numbers = new Integer[numberOfNodes];
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
                        if ((!Objects.equals(comboBox1.getSelectedItem(), comboBox2.getSelectedItem())) &&
                                (!Objects.equals(comboBox1.getSelectedItem(), comboBox3.getSelectedItem())) &&
                                (!Objects.equals(comboBox2.getSelectedItem(), comboBox3.getSelectedItem())))
                        {
                            shortestPath.clear();
                            switch (numberOfPoints) {
                                case 1:
                                    shortestPath.add((Integer) comboBox1.getSelectedItem());
                                    break;
                                case 2:
                                    shortestPath.add((Integer) comboBox1.getSelectedItem());
                                    shortestPath.add((Integer) comboBox2.getSelectedItem());
                                    break;
                                case 3:
                                    shortestPath.add((Integer) comboBox1.getSelectedItem());
                                    shortestPath.add((Integer) comboBox2.getSelectedItem());
                                    shortestPath.add((Integer) comboBox3.getSelectedItem());
                                    break;
                            }
                            // findShortestPath(selectedNumber1, selectedNumber2, shortestPath);
                        } else {
                            JOptionPane.showMessageDialog(null, "The numbers must be distinct!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
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
/*
    public ArrayList<Integer> findShortestPath(int start,int end,ArrayList<Integer> points){

    }

 */


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;


        int centerX = width / 2;
        int centerY = height / 2;
        double radius = Math.min(width, height) / 2.5;
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
            g2d.setColor(Color.GREEN);
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

    public void addNumbersButton(JComboBox comboBox, int number) {
        for (int i = 0; i < number; i++) {
            comboBox.addItem(i + 1);
        }
    }

}



