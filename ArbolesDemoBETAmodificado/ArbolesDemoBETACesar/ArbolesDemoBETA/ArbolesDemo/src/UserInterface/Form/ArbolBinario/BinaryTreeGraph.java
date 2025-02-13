package UserInterface.Form.ArbolBinario;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import UserInterface.CustomerControls.Button;

class BinaryTreeNode {
    int value;
    BinaryTreeNode left, right;

    public BinaryTreeNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }
}

public class BinaryTreeGraph extends JFrame {
    private mxGraph graph;
    private Object parent;
    private Map<BinaryTreeNode, Object> nodeMap;
    private BinaryTreeNode root;

    public BinaryTreeGraph() {
        this.graph = new mxGraph();
        this.parent = graph.getDefaultParent();
        this.nodeMap = new HashMap<>();

        setTitle("Árbol Binario - JGraphX");
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
                root = new BinaryTreeNode(rootValue);
                drawTree();
            }
        } else {
            int nodeValue = getUserInput("Ingrese el valor del nodo padre donde desea agregar hijos:");
            if (nodeValue != -1) {
                BinaryTreeNode parentNode = findNode(root, nodeValue);
                if (parentNode != null) {
                    addChildNodes(parentNode);
                    drawTree();
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el nodo con valor " + nodeValue);
                }
            }
        }
    }

    private BinaryTreeNode findNode(BinaryTreeNode node, int value) {
        if (node == null) return null;
        if (node.value == value) return node;

        BinaryTreeNode leftSearch = findNode(node.left, value);
        if (leftSearch != null) return leftSearch;

        return findNode(node.right, value);
    }

    private void addChildNodes(BinaryTreeNode node) {
        if (node.left == null) {
            int leftValue = getUserInput("Ingrese valor para el hijo izquierdo de " + node.value + " (o -1 para omitir):");
            if (leftValue != -1) {
                node.left = new BinaryTreeNode(leftValue);
            }
        } else {
            JOptionPane.showMessageDialog(this, "El nodo " + node.value + " ya tiene un hijo izquierdo.");
        }

        if (node.right == null) {
            int rightValue = getUserInput("Ingrese valor para el hijo derecho de " + node.value + " (o -1 para omitir):");
            if (rightValue != -1) {
                node.right = new BinaryTreeNode(rightValue);
            }
        } else {
            JOptionPane.showMessageDialog(this, "El nodo " + node.value + " ya tiene un hijo derecho.");
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
            drawNode(root, 400, 50, 200);
        } finally {
            graph.getModel().endUpdate();
        }

        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
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

    private void drawNode(BinaryTreeNode node, double x, double y, double offset) {
        if (node == null) return;

        Object vertex = graph.insertVertex(parent, null, node.value, x, y, 50, 50);
        nodeMap.put(node, vertex);

        if (node.left != null) {
            drawNode(node.left, x - offset, y + 100, offset / 2);
            graph.insertEdge(parent, null, "", vertex, nodeMap.get(node.left));
        }
        if (node.right != null) {
            drawNode(node.right, x + offset, y + 100, offset / 2);
            graph.insertEdge(parent, null, "", vertex, nodeMap.get(node.right));
        }
    }

    public static void main(String[] args) {
        new BinaryTreeGraph();
    }
}
