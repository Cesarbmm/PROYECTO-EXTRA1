package UserInterface.Form.ArbolAVL;

import UserInterface.CustomerControls.Button;
import UserInterface.CustomerControls.CustomOptionPane;
import UserInterface.CustomerControls.CustomPopupMenu;
import UserInterface.CustomerControls.MenuItem;
import UserInterface.CustomerControls.Panel;
import UserInterface.CustomerControls.TextArea;
import java.awt.*;
import javax.swing.*;

public class ArbolAVLFrame extends JFrame {

    private ArbolAVL arbolAVL;
    private Panel drawPanel;
    private TextArea consoleTextArea;

    public ArbolAVLFrame() {
        super("Gestión de Árbol AVL");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Panel de botones
        Panel buttonPanel = new Panel();
        Button insertRootButton = new Button("Insertar Nodo Raíz");
        Button insertNodeButton = new Button("Insertar Nodo");
        Button limpiarArbolButton = new Button("Limpiar Árbol");
        Button eliminarNodoButton = new Button("Eliminar Nodo");
        Button recorridoButton = new Button("Recorrido del Árbol");

        buttonPanel.add(insertRootButton);
        buttonPanel.add(insertNodeButton);
        buttonPanel.add(limpiarArbolButton);
        buttonPanel.add(eliminarNodoButton);
        buttonPanel.add(recorridoButton);

        // Panel de dibujo
        drawPanel = new Panel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (arbolAVL != null) {
                    arbolAVL.drawTree(g, getWidth() / 2, 50, getWidth() / 4, 80);
                }
            }
        };
        drawPanel.setBackground(Color.WHITE);

        // Panel de consola
        consoleTextArea = new TextArea(10, 50, true); // Estilo consola
        JScrollPane scrollPane = new JScrollPane(consoleTextArea);
        scrollPane.setPreferredSize(new Dimension(800, 200));

        // Agregar componentes al frame
        add(buttonPanel, BorderLayout.NORTH);
        add(drawPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Configuración de acciones para botones
        configureActions(insertRootButton, insertNodeButton, limpiarArbolButton, eliminarNodoButton, recorridoButton);

        setVisible(true);
    }

    private void configureActions(Button insertRootButton, Button insertNodeButton, Button limpiarArbolButton, Button eliminarNodoButton, Button recorridoButton) {
        // Acción para insertar nodo raíz
        insertRootButton.addActionListener(e -> {
            if (arbolAVL != null) {
                CustomOptionPane.showMessageDialog(this, "El nodo raíz ya ha sido insertado.", "Advertencia", CustomOptionPane.WARNING_MESSAGE);
                return;
            }
            String input = CustomOptionPane.showInputDialog(this, "Ingrese el valor del nodo raíz:");
            if (input != null) {
                try {
                    int rootValue = Integer.parseInt(input);
                    arbolAVL = new ArbolAVL();
                    arbolAVL.insert(rootValue);
                    consoleTextArea.setText("Nodo raíz insertado: " + rootValue + "\n");
                    drawPanel.repaint();
                } catch (NumberFormatException ex) {
                    CustomOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido.", "Error", CustomOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción para insertar nodo adicional
        insertNodeButton.addActionListener(e -> {
            if (arbolAVL == null) {
                CustomOptionPane.showMessageDialog(this, "Primero debe insertar el nodo raíz.", "Error", CustomOptionPane.ERROR_MESSAGE);
                return;
            }
            String input = CustomOptionPane.showInputDialog(this, "Ingrese el valor del nodo a insertar:");
            if (input != null) {
                try {
                    int value = Integer.parseInt(input);
                    if (arbolAVL.buscar(value)) {
                        consoleTextArea.append("El nodo " + value + " ya existe en el árbol.\n");
                    } else {
                        consoleTextArea.append("\nInsertando nodo: " + value + "\n");
                        arbolAVL.insertConExplicacion(value, consoleTextArea);
                        consoleTextArea.append("Nodo " + value + " insertado y balanceado correctamente.\n");
                        drawPanel.repaint();
                    }
                } catch (NumberFormatException ex) {
                    CustomOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido.", "Error", CustomOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción para limpiar el árbol
        limpiarArbolButton.addActionListener(e -> {
            if (arbolAVL != null) {
                arbolAVL = null;
                consoleTextArea.setText("Árbol AVL limpiado.\n");
                drawPanel.repaint();
            }
        });

        // Acción para eliminar un nodo
        eliminarNodoButton.addActionListener(e -> {
            if (arbolAVL == null) {
                CustomOptionPane.showMessageDialog(this, "Primero debe insertar el nodo raíz.", "Error", CustomOptionPane.ERROR_MESSAGE);
                return;
            }
            String input = CustomOptionPane.showInputDialog(this, "Ingrese el valor del nodo a eliminar:");
            if (input != null) {
                try {
                    int value = Integer.parseInt(input);
                    if (arbolAVL.buscar(value)) {
                        consoleTextArea.append("\nEliminando nodo: " + value + "\n");
                        arbolAVL.eliminarConExplicacion(value, consoleTextArea);
                        consoleTextArea.append("Nodo " + value + " eliminado y balanceado correctamente.\n");
                        drawPanel.repaint();
                    } else {
                        consoleTextArea.append("El nodo " + value + " no existe en el árbol.\n");
                    }
                } catch (NumberFormatException ex) {
                    CustomOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido.", "Error", CustomOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción para mostrar el menú de recorrido
        configureRecorridoMenu(recorridoButton);
    }

    private void configureRecorridoMenu(Button recorridoButton) {
        CustomPopupMenu recorridoMenu = new CustomPopupMenu();
        MenuItem inOrderItem = new MenuItem("In-order");
        MenuItem preOrderItem = new MenuItem("Pre-order");
        MenuItem postOrderItem = new MenuItem("Post-order");
        MenuItem levelOrderItem = new MenuItem("Level-order");

        recorridoMenu.add(inOrderItem);
        recorridoMenu.add(preOrderItem);
        recorridoMenu.add(postOrderItem);
        recorridoMenu.add(levelOrderItem);

        // Mostrar menú al pulsar el botón
        recorridoButton.addActionListener(e -> {
            if (arbolAVL == null) {
                CustomOptionPane.showMessageDialog(this, "El árbol está vacío.", "Error", CustomOptionPane.ERROR_MESSAGE);
                return;
            }
            recorridoMenu.show(recorridoButton, 0, recorridoButton.getHeight());
        });

        // Acciones de los ítems del menú
        inOrderItem.addActionListener(e -> arbolAVL.recorridoInordenConExplicacion(consoleTextArea));
        preOrderItem.addActionListener(e -> arbolAVL.recorridoPreordenConExplicacion(consoleTextArea));
        postOrderItem.addActionListener(e -> arbolAVL.recorridoPostordenConExplicacion(consoleTextArea));
        levelOrderItem.addActionListener(e -> arbolAVL.recorridoLevelOrderConExplicacion(consoleTextArea));
    }


}
