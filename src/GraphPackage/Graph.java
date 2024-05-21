package GraphPackage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

    public class Graph{
        private int[][] graph = new int[9][9];

        public Graph() {

        }

        public ArrayList<Integer> dijkstras(int start, int end) {
            int[] values = new int[graph.length];
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
            try (BufferedReader br = new BufferedReader(new FileReader("Zaverecka\\src\\GraphPackage\\Matice.csv"))) {
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
                    System.out.print(graph[j][i]+" ");
                }
                System.out.println("");
            }
        }
    }
