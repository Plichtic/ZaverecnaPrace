package GraphPackage;

import java.util.HashMap;
import java.util.TreeSet;

public class Node {
    private TreeSet<HashMap> linkedNodes;
    private String name;

    public Node(TreeSet<HashMap> linkedNodes, String name) {
        this.linkedNodes = linkedNodes;
        this.name = name;
    }

    public TreeSet<HashMap> getLinkedNodes() {
        return linkedNodes;
    }

    public String getName() {
        return name;
    }
}