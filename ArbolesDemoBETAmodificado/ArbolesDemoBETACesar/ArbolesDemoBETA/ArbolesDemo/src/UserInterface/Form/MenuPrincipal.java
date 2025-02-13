package UserInterface.Form;

import UserInterface.CustomerControls.Button;
import UserInterface.CustomerControls.Panel;
import UserInterface.CustomerControls.Label;
import UserInterface.Form.ArbolAVL.ArbolAVLFrame;
import UserInterface.Form.ArbolB.BTreeRenderer;
import UserInterface.Form.ArbolBPlus.BTreeRendererBPlus;
import UserInterface.Form.ArbolBinario.BinaryTreeGraph;
import UserInterface.Form.ArbolGeneral.GeneralTreeGraph;
import UserInterface.Styles;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        // Configuración básica de la ventana
        setTitle("Menú Principal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Cargar y mostrar la imagen en la parte superior
        try {
            URL imageUrl = Styles.URL_MENU; // Obtiene la URL de la imagen desde Styles
            Label labelImagen = new Label(imageUrl, 200, 200); // Redimensiona la imagen
            add(labelImagen, BorderLayout.NORTH);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar la imagen del menú.");
        }

        // Crear las opciones del menú
        Panel panelBotones = new Panel();
        panelBotones.setLayout(new GridLayout(5, 1, 10, 10)); // 3 filas, 1 columna, espaciado de 10px

        // Crear los botones del menú principal
        Button btnArbolGeneral = new Button("1. ARBOL GENERAL");
        Button btnArbolBinario = new Button("2. ARBOL BINARIO");
        Button btnArbolAVL = new Button("3. ARBOL AVL");
        Button btnArbolB = new Button("4. ARBOL B");
        Button btnArbolBmas = new Button("5. ARBOL B++");

        // Agregar acciones a las opciones del menú
        btnArbolGeneral.addActionListener(e -> abrirArbolGeneral());
        btnArbolBinario.addActionListener(e -> abrirArbolBinario());
        btnArbolAVL.addActionListener(e -> abrirArbolAVL());
        btnArbolB.addActionListener(e -> abrirArbolB());
        btnArbolBmas.addActionListener(e -> abrirArbolBPlus());

        // Añadir las opciones al menú
        add(panelBotones, BorderLayout.CENTER);
        panelBotones.add(btnArbolGeneral);
        panelBotones.add(btnArbolBinario);
        panelBotones.add(btnArbolAVL);
        panelBotones.add(btnArbolB);
        panelBotones.add(btnArbolBmas);

        // Añadir el menú a la barra de menús
        
    }

    private void abrirArbolBinario() {
        BinaryTreeGraph frame = new BinaryTreeGraph(); // Crear una instancia del submenú
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private void abrirArbolGeneral() {
        GeneralTreeGraph frame = new GeneralTreeGraph();
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    // Métodos para abrir cada frame correspondiente
    private void abrirArbolAVL() {
        ArbolAVLFrame frame = new ArbolAVLFrame();
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void abrirArbolB() {
        BTreeRenderer frame = new BTreeRenderer();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(BTreeRenderer.APP_WIDTH, BTreeRenderer.APP_HEIGHT);
        BTreeRenderer.centreWindow(frame);
        frame.render();
        frame.setVisible(true);
    }

    private void abrirArbolBPlus() {
        BTreeRendererBPlus frame = new BTreeRendererBPlus();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(BTreeRendererBPlus.APP_WIDTH, BTreeRendererBPlus.APP_HEIGHT);
        BTreeRendererBPlus.centreWindow(frame);
        frame.render();
        frame.setVisible(true);
    }

    // Método principal para ejecutar el programa

}
