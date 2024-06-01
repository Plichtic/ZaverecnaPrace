package GraphPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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





}