package GraphPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class EditingWindow extends JPanel {
    private int node;
    private Graph graphMethods;
    private int[][] graph;
    private JFrame frame;
    private JTextField[] textFields;

    public EditingWindow(int node, int[][] graph, Graph graphMethods, JFrame frame) {
        this.node = node;
        this.graph = graph;
        this.graphMethods = graphMethods;
        this.frame = frame;
        setLayout(new BorderLayout());

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(graph.length, 2));
        textFields = new JTextField[graph.length];

        for (int i = 0; i < graph.length; i++) {
            if (i != node) {
                JLabel label = new JLabel("Edge to node " + (i + 1) + ": ");
                JTextField textField = new JTextField(Integer.toString(graph[node][i]));
                textFields[i] = textField;
                controlsPanel.add(label);
                controlsPanel.add(textField);
            }
        }

        JScrollPane scrollPane = new JScrollPane(controlsPanel);
        add(scrollPane, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitChanges();
            }
        });
        add(submitButton, BorderLayout.SOUTH);
    }

    private void submitChanges() {
        for (int i = 0; i < textFields.length; i++) {
            if (i != node) {
                int newWeight;
                try {
                    newWeight = Integer.parseInt(textFields[i].getText());
                    if (newWeight < 0) throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input for edge to node " + (i + 1) + "! Please enter a non-negative integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                graph[node][i] = newWeight;
                graph[i][node] = newWeight;
            }
        }
        graphMethods.repaint();
        frame.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        double radius = Math.min(getWidth(), getHeight()) / 2.5;
        double angleIncrement = 2 * Math.PI / graph.length;

        // Draw the selected node in blue
        g2d.setColor(Color.BLUE);
        g2d.fill(new Ellipse2D.Double(centerX - graphMethods.getNodeRadius(), centerY - graphMethods.getNodeRadius(), 2 * graphMethods.getNodeRadius(), 2 * graphMethods.getNodeRadius()));

        // Draw connections in black
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < graph.length; i++) {
            if (i != node && graph[node][i] > 0) {
                double angle = i * angleIncrement;
                int x = (int) (centerX + radius * Math.cos(angle));
                int y = (int) (centerY + radius * Math.sin(angle));

                g2d.draw(new Line2D.Double(centerX, centerY, x, y));
                g2d.drawString(Integer.toString(graph[node][i]), (centerX + x) / 2, (centerY + y) / 2);

                g2d.setColor(Color.RED);
                g2d.fill(new Ellipse2D.Double(x - graphMethods.getNodeRadius(), y - graphMethods.getNodeRadius(), 2 * graphMethods.getNodeRadius(), 2 * graphMethods.getNodeRadius()));
                g2d.setColor(Color.WHITE);
                g2d.drawString(Integer.toString(i + 1), x - 5, y + 5);
                g2d.setColor(Color.BLACK);
            }
        }

        // Draw node number
        g2d.setColor(Color.WHITE);
        g2d.drawString(Integer.toString(node + 1), centerX - 5, centerY + 5);
    }
}