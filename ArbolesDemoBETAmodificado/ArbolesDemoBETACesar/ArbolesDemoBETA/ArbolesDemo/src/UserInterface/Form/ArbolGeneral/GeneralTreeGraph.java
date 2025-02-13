package UserInterface.Form.ArbolGeneral;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import UserInterface.CustomerControls.Button;

class GeneralTreeNode {
    int value;
    List<GeneralTreeNode> children;

    public GeneralTreeNode(int value) {
        this.value = value;
        this.children = new ArrayList<>();
    }
}

public class GeneralTreeGraph extends JFrame {
    private mxGraph graph;
    private Object parent;
    private Map<GeneralTreeNode, Object> nodeMap;
    private GeneralTreeNode root;

    public GeneralTreeGraph() {
        this.graph = new mxGraph();
        this.parent = graph.getDefaultParent();
        this.nodeMap = new HashMap<>();

        setTitle("Árbol General - JGraphX");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Button addButton = new Button("Agregar Nodo");
        addButton.addActionListener(e -> handleAddNode());

        add(addButton, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void handleAddNode() {
        if (root == null) {
            int rootValue = getUserInput("Ingrese el valor del nodo raíz:");
            if (rootValue != -1) {
                root = new GeneralTreeNode(rootValue);
                drawTree();
            }
        } else {
            int parentValue = getUserInput("Ingrese el valor del nodo padre donde desea agregar un hijo:");
            if (parentValue != -1) {
                GeneralTreeNode parentNode = findNode(root, parentValue);
                if (parentNode != null) {
                    addChildNode(parentNode);
                    drawTree();
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el nodo con valor " + parentValue);
                }
            }
        }
    }

    private GeneralTreeNode findNode(GeneralTreeNode node, int value) {
        if (node == null) return null;
        if (node.value == value) return node;

        for (GeneralTreeNode child : node.children) {
            GeneralTreeNode foundNode = findNode(child, value);
            if (foundNode != null) return foundNode;
        }
        return null;
    }

    private void addChildNode(GeneralTreeNode parentNode) {
        int childValue = getUserInput("Ingrese el valor del nuevo hijo de " + parentNode.value + ":");
        if (childValue != -1) {
            parentNode.children.add(new GeneralTreeNode(childValue));
        }
    }

    private int getUserInput(String message) {
        String input = JOptionPane.showInputDialog(this, message);
        if (input == null || input.isEmpty()) return -1;
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Entrada inválida. Ingrese un número entero.");
            return -1;
        }
    }

    private void drawTree() {
        graph.getModel().beginUpdate();
        try {
            graph.removeCells(graph.getChildVertices(parent)); // Limpiar el gráfico antes de dibujar
            nodeMap.clear();
            drawNode(root, 400, 50);
        } finally {
            graph.getModel().endUpdate();
        }

        mxCompactTreeLayout layout = new mxCompactTreeLayout(graph);
        layout.execute(parent);

        getContentPane().removeAll();
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent, BorderLayout.CENTER);

        Button addButton = new Button("Agregar Nodo");
        addButton.addActionListener(e -> handleAddNode());
        getContentPane().add(addButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void drawNode(GeneralTreeNode node, double x, double y) {
        if (node == null) return;

        Object vertex = graph.insertVertex(parent, null, node.value, x, y, 50, 50);
        nodeMap.put(node, vertex);

        for (GeneralTreeNode child : node.children) {
            drawNode(child, x, y + 100);
            graph.insertEdge(parent, null, "", vertex, nodeMap.get(child));
        }
    }

}
