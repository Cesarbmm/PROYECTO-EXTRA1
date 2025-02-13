package UserInterface.Form.ArbolBPlus;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

// Clase que representa un nodo del árbol B
class BTreeNode {
    List<Integer> keys;      // Claves almacenadas en el nodo
    List<BTreeNode> children; // Hijos del nodo
    boolean leaf;            // Indica si el nodo es una hoja

    public BTreeNode(boolean leaf) {
        this.keys = new ArrayList<>();
        this.children = new ArrayList<>();
        this.leaf = leaf;
    }
}

// Clase que implementa el árbol B
class BTree {
    private BTreeNode root;
    private int degree; // Grado mínimo (t)

    public BTree(int degree) {
        this.root = new BTreeNode(true);
        this.degree = degree;
    }

    // Método para acceder a la raíz
    public BTreeNode getRoot() {
        return root;
    }

    // Método para insertar una clave en el árbol
    public void insert(int key) {
        BTreeNode r = root;
        if (r.keys.size() == 2 * degree - 1) { // Si la raíz está llena
            BTreeNode newRoot = new BTreeNode(false); // Nueva raíz
            newRoot.children.add(r); // La raíz anterior es ahora hijo de la nueva raíz
            splitChild(newRoot, 0); // Dividir la raíz anterior
            root = newRoot; // Actualizar la raíz
        }
        insertNonFull(root, key); // Insertar la clave
    }

    // Método para insertar una clave en un nodo no lleno
    private void insertNonFull(BTreeNode node, int key) {
        int i = node.keys.size() - 1;

        if (node.leaf) { // Si el nodo es una hoja
            // Insertar la clave en la posición correcta
            while (i >= 0 && key < node.keys.get(i)) {
                i--;
            }
            node.keys.add(i + 1, key);
        } else { // Si el nodo no es una hoja
            // Encontrar el hijo adecuado para la inserción
            while (i >= 0 && key < node.keys.get(i)) {
                i--;
            }
            i++;
            // Si el hijo está lleno, dividirlo
            if (node.children.get(i).keys.size() == 2 * degree - 1) {
                splitChild(node, i);
                if (key > node.keys.get(i)) {
                    i++;
                }
            }
            insertNonFull(node.children.get(i), key); // Insertar en el hijo
        }
    }

    // Método para dividir un nodo hijo
    private void splitChild(BTreeNode parent, int index) {
        BTreeNode child = parent.children.get(index);
        BTreeNode newChild = new BTreeNode(child.leaf);

        // Mover la clave media al padre
        parent.keys.add(index, child.keys.get(degree - 1));

        // Mover las claves superiores al nuevo hijo
        newChild.keys.addAll(child.keys.subList(degree, 2 * degree - 1));
        child.keys.subList(degree - 1, 2 * degree - 1).clear();

        // Mover los hijos superiores al nuevo hijo (si no es hoja)
        if (!child.leaf) {
            newChild.children.addAll(child.children.subList(degree, 2 * degree));
            child.children.subList(degree, 2 * degree).clear();
        }

        // Agregar el nuevo hijo al padre
        parent.children.add(index + 1, newChild);
    }

    // Método para eliminar una clave del árbol
    public void delete(int key) {
        deleteKey(root, key);
        if (root.keys.isEmpty() && !root.leaf) { // Si la raíz queda vacía
            root = root.children.get(0); // Actualizar la raíz
        }
    }

    // Método auxiliar para eliminar una clave
    private void deleteKey(BTreeNode node, int key) {
        int index = node.keys.indexOf(key);

        if (index != -1) { // Si la clave está en este nodo
            if (node.leaf) { // Caso 1: La clave está en una hoja
                node.keys.remove(index);
            } else { // Caso 2: La clave está en un nodo interno
                BTreeNode leftChild = node.children.get(index);
                BTreeNode rightChild = node.children.get(index + 1);

                if (leftChild.keys.size() >= degree) { // Subcaso 2a: Hijo izquierdo tiene suficientes claves
                    int predecessor = getPredecessor(leftChild);
                    node.keys.set(index, predecessor);
                    deleteKey(leftChild, predecessor);
                } else if (rightChild.keys.size() >= degree) { // Subcaso 2b: Hijo derecho tiene suficientes claves
                    int successor = getSuccessor(rightChild);
                    node.keys.set(index, successor);
                    deleteKey(rightChild, successor);
                } else { // Subcaso 2c: Ambos hijos tienen t-1 claves
                    mergeNodes(node, index);
                    deleteKey(leftChild, key);
                }
            }
        } else { // Si la clave no está en este nodo
            int i = 0;
            while (i < node.keys.size() && key > node.keys.get(i)) {
                i++;
            }

            BTreeNode child = node.children.get(i);
            if (child.keys.size() < degree) { // Asegurar que el hijo tenga al menos t claves
                if (i > 0 && node.children.get(i - 1).keys.size() >= degree) {
                    borrowFromLeft(node, i);
                } else if (i < node.children.size() - 1 && node.children.get(i + 1).keys.size() >= degree) {
                    borrowFromRight(node, i);
                } else {
                    if (i > 0) {
                        mergeNodes(node, i - 1);
                        i--;
                    } else {
                        mergeNodes(node, i);
                    }
                }
            }
            deleteKey(node.children.get(i), key);
        }
    }

    // Método para obtener el predecesor de una clave
    private int getPredecessor(BTreeNode node) {
        while (!node.leaf) {
            node = node.children.get(node.children.size() - 1);
        }
        return node.keys.get(node.keys.size() - 1);
    }

    // Método para obtener el sucesor de una clave
    private int getSuccessor(BTreeNode node) {
        while (!node.leaf) {
            node = node.children.get(0);
        }
        return node.keys.get(0);
    }

    // Método para tomar prestada una clave del hermano izquierdo
    private void borrowFromLeft(BTreeNode parent, int index) {
        BTreeNode child = parent.children.get(index);
        BTreeNode leftSibling = parent.children.get(index - 1);

        child.keys.add(0, parent.keys.get(index - 1));
        parent.keys.set(index - 1, leftSibling.keys.remove(leftSibling.keys.size() - 1));

        if (!child.leaf) {
            child.children.add(0, leftSibling.children.remove(leftSibling.children.size() - 1));
        }
    }

    // Método para tomar prestada una clave del hermano derecho
    private void borrowFromRight(BTreeNode parent, int index) {
        BTreeNode child = parent.children.get(index);
        BTreeNode rightSibling = parent.children.get(index + 1);

        child.keys.add(parent.keys.get(index));
        parent.keys.set(index, rightSibling.keys.remove(0));

        if (!child.leaf) {
            child.children.add(rightSibling.children.remove(0));
        }
    }

    // Método para fusionar dos nodos
    private void mergeNodes(BTreeNode parent, int index) {
        BTreeNode leftChild = parent.children.get(index);
        BTreeNode rightChild = parent.children.get(index + 1);

        leftChild.keys.add(parent.keys.remove(index));
        leftChild.keys.addAll(rightChild.keys);
        leftChild.children.addAll(rightChild.children);

        parent.children.remove(index + 1);
    }

    // Método de búsqueda: retorna true si la clave se encuentra en el árbol
    public boolean search(int key) {
        return search(root, key);
    }

    private boolean search(BTreeNode node, int key) {
        if (node == null) return false;
        int i = 0;
        while (i < node.keys.size() && key > node.keys.get(i)) {
            i++;
        }
        if (i < node.keys.size() && key == node.keys.get(i)) {
            return true;
        }
        if (node.leaf) return false;
        return search(node.children.get(i), key);
    }

    // Método para visualizar el árbol, con opción de resaltar los nodos que contengan highlightKey
    public void visualize(mxGraph graph, Object parent, BTreeNode node, int x, int y, int level, 
                          Map<BTreeNode, Object> nodeMap, Integer highlightKey) {
        if (node == null) return;

        // Definir el estilo según si el nodo contiene la clave buscada
        String style = "fillColor=white;strokeColor=black;";
        if (highlightKey != null && node.keys.contains(highlightKey)) {
            style = "fillColor=yellow;strokeColor=black;strokeWidth=2;";
        }
        Object vertex = graph.insertVertex(parent, null, node.keys.toString(), x, y, 80, 30, style);
        nodeMap.put(node, vertex);

        int childCount = node.children.size();
        // Distribuir horizontalmente a los hijos
        int totalWidth = childCount * 80 + (childCount - 1) * 20;
        int startX = x - totalWidth / 2;
        int childY = y + 100;
        for (int i = 0; i < childCount; i++) {
            int childX = startX + i * (80 + 20);
            visualize(graph, parent, node.children.get(i), childX, childY, level + 1, nodeMap, highlightKey);
            Object childVertex = nodeMap.get(node.children.get(i));
            graph.insertEdge(parent, null, "", vertex, childVertex);
        }
    }
}

// Clase principal que muestra la visualización del árbol B mediante mxGraph
public class BTreeVisualization extends JFrame {
    private BTree bTree;
    private mxGraph graph;
    private Object parent;
    private mxGraphComponent graphComponent;

    // Parámetros para la presentación mejorada
    private final int nodeWidth = 80;
    private final int hSpacing = 20;
    private final int verticalSpacing = 100;

    public BTreeVisualization() {
        setTitle("Visualización de Árbol B");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        bTree = new BTree(2); // Árbol B con grado mínimo 2
        graph = new mxGraph();
        parent = graph.getDefaultParent();
        graphComponent = new mxGraphComponent(graph);
        setLayout(new BorderLayout());
        add(graphComponent, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(169, 169, 169));
        JTextField keyField = new JTextField(5);
        JButton insertButton = new JButton("Insertar");
        JButton deleteButton = new JButton("Eliminar");
        JButton clearButton = new JButton("Reiniciar");
        JButton buscarButton = new JButton("Buscar");
        JButton generarButton = new JButton("Generar");

        insertButton.addActionListener(e -> {
            try {
                int key = Integer.parseInt(keyField.getText());
                bTree.insert(key);
                refreshGraph(null);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                int key = Integer.parseInt(keyField.getText());
                boolean found = bTree.search(key);
                if (found) {
                    bTree.delete(key);
                } else {
                    JOptionPane.showMessageDialog(this, "Clave no encontrada.", "Información", JOptionPane.INFORMATION_MESSAGE);
                }
                refreshGraph(null);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Botón Buscar: realiza la búsqueda de la clave y resalta los nodos que la contienen
        buscarButton.addActionListener(e -> {
            try {
                int key = Integer.parseInt(keyField.getText());
                boolean found = bTree.search(key);
                if (found) {
                    refreshGraph(key);
                } else {
                    JOptionPane.showMessageDialog(this, "Clave " + key + " no encontrada.", "Resultado de la búsqueda", JOptionPane.INFORMATION_MESSAGE);
                    refreshGraph(null);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Botón Generar: inserta automáticamente 10 claves aleatorias en el árbol B
        generarButton.addActionListener(e -> {
            bTree = new BTree(2); // Reinicia el árbol
            Random rand = new Random();
            for (int i = 0; i < 10; i++) {
                int key = rand.nextInt(100);
                bTree.insert(key);
            }
            refreshGraph(null);
        });

        clearButton.addActionListener(e -> {
            bTree = new BTree(2); // Reiniciar el árbol
            refreshGraph(null);
        });

        controlPanel.add(new JLabel("Clave:"));
        controlPanel.add(keyField);
        controlPanel.add(insertButton);
        controlPanel.add(deleteButton);
        controlPanel.add(clearButton);
        controlPanel.add(buscarButton);
        controlPanel.add(generarButton);
        add(controlPanel, BorderLayout.NORTH);

        setVisible(true);
    }

    /**
     * Calcula las posiciones de los nodos de forma recursiva para una presentación más ordenada.
     * Para cada nodo, se posiciona de modo que su coordenada x sea el centro del ancho total que ocupa su subárbol.
     */
    private int computePositions(BTreeNode node, int level, int x, Map<BTreeNode, Point> positions) {
        if (node == null) return x;
        if (node.leaf || node.children.isEmpty()) {
            // Si es hoja, se le asigna la posición actual y se reserva el ancho del nodo + espacio
            positions.put(node, new Point(x, level * verticalSpacing + 20));
            return x + nodeWidth + hSpacing;
        } else {
            // Para nodo interno, calcular posiciones de todos sus hijos
            int childX = x;
            for (BTreeNode child : node.children) {
                childX = computePositions(child, level + 1, childX, positions);
            }
            // La posición del nodo padre es el centro entre el primer y último hijo
            int firstChildX = positions.get(node.children.get(0)).x;
            int lastChildX = positions.get(node.children.get(node.children.size() - 1)).x;
            int parentX = (firstChildX + lastChildX) / 2;
            positions.put(node, new Point(parentX, level * verticalSpacing + 20));
            return childX;
        }
    }

    /**
     * Refresca la visualización del árbol B.
     * Si highlightKey no es nulo, se resaltarán los nodos que contengan esa clave.
     */
    private void refreshGraph(Integer highlightKey) {
        Map<BTreeNode, Point> positions = new HashMap<>();
        // Se inicia el cálculo de posiciones con una coordenada x inicial (por ejemplo, 50)
        computePositions(bTree.getRoot(), 0, 50, positions);

        graph.getModel().beginUpdate();
        try {
            graph.removeCells(graph.getChildVertices(parent));
            Map<BTreeNode, Object> nodeMap = new HashMap<>();
            // Se insertan los nodos usando las posiciones calculadas
            for (Map.Entry<BTreeNode, Point> entry : positions.entrySet()) {
                BTreeNode node = entry.getKey();
                Point p = entry.getValue();
                String style = "shape=rectangle;whiteSpace=wrap;html=1;fillColor=white;strokeColor=black;";
                if (highlightKey != null && node.keys.contains(highlightKey)) {
                    style = "shape=rectangle;whiteSpace=wrap;html=1;fillColor=yellow;strokeColor=black;strokeWidth=2;";
                }
                Object vertex = graph.insertVertex(parent, null, node.keys.toString(), p.x, p.y, nodeWidth, 30, style);
                nodeMap.put(node, vertex);
            }
            // Se añaden las aristas entre cada nodo y sus hijos
            addEdges(bTree.getRoot(), nodeMap);
        } finally {
            graph.getModel().endUpdate();
        }
        graphComponent.refresh();
    }

    /**
     * Agrega las aristas (edges) entre cada nodo y sus hijos.
     */
    private void addEdges(BTreeNode node, Map<BTreeNode, Object> nodeMap) {
        if (node == null) return;
        for (BTreeNode child : node.children) {
            graph.insertEdge(parent, null, "", nodeMap.get(node), nodeMap.get(child));
            addEdges(child, nodeMap);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BTreeVisualization::new);
    }
}
